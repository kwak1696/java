import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.*;
import java.util.*;

/**
 * HANTEC ���� ���α�
 */
public class CJKfIndexHantec {
	static boolean deleting = false; // true during deletion pass
	static IndexReader reader; // existing index
	static IndexWriter writer; // new index being built
	static TermEnum uidIter; // document id iterator
	
    CJKfIndexHantec() {}

	public static void main(String[] argv) {
		try {
			File index = new File("index-hantec");
			boolean create = false;
			File root = null;

			String usage = "IndexHantec [-create] [-index <index>] <root_directory>";

			if (argv.length == 0) {
				System.err.println("Usage: " + usage);
				return;
			}

			for (int i = 0; i < argv.length; i++) {
				if (argv[i].equals("-index")) { // parse -index option
					index = new File(argv[++i]);
				} 
				else if (argv[i].equals("-create")) { // parse -create option
					create = true;
				} 
				else if (i != argv.length - 1) {
					System.err.println("Usage: " + usage);
					return;
				} 
				else
					root = new File(argv[i]);
			}

			Date start = new Date();
			if (!create) { // delete stale docs
				deleting = true;
				indexDocs(root, index, create);
			}

			
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, new CJKAnalyzer(Version.LUCENE_36));
			if (create)
				config.setOpenMode(OpenMode.CREATE);
			else
				config.setOpenMode(OpenMode.APPEND);
			/*
			LogMergePolicy mp = new LogByteSizeMergePolicy();
			mp.setUseCompoundFile(false);
			config.setMergePolicy(mp);*/
			writer = new IndexWriter(FSDirectory.open(index), config);
			
			
			
			indexDocs(root, index, create); // add new docs

			writer.close();
			Date end = new Date();

			System.out.print(end.getTime() - start.getTime());
			System.out.println(" total milliseconds");
		} 
		catch (Exception e) {
			System.out.println(" caught a " +
                    e.getClass() +
                    "\n with message: " +
                    e.getMessage());
		}
	}

	/*
	 * Walk directory hierarchy in uid order, while keeping uid iterator from /*
	 * existing index in sync. Mismatches indicate one of: (a) old documents to
	 * /* be deleted; (b) unchanged documents, to be left alone; or (c) new /*
	 * documents, to be indexed.
	 */
	//���� ����
	//u
	private static void indexDocs(File file, File index, boolean create) throws Exception {
		//���� ������ ������� ������Ʈ�ϴ� ���
		if (!create) {
			reader = IndexReader.open(FSDirectory.open(index)); // ���� �ε��� ����
			uidIter = reader.terms(new Term("uid", "")); // uid iterator �ʱ�ȭ

			//������ ���ϵ�� ���� ������ ������ ���ϸ� ���� ������Ʈ
			indexDocs(file);

			if (deleting) { // �ű� ������ �������� �ʴ� ������ ���� ������ ���� ���� ����
				while (uidIter.term() != null
						&& uidIter.term().field() == "uid") {
					System.out.println("deleting "
							+ HantecDocument.uid2url(uidIter.term().text()));
					reader.deleteDocuments(uidIter.term());
					uidIter.next();
				}
				deleting = false;
			}
			uidIter.close();
			reader.close();
		}
		//������ ���Ӱ� �����ϴ� ���
		else {
			indexDocs(file);
        }
	}

	//���� ���丮���� ��� ����
	private static void indexDocs(File file) throws Exception {
		// ���丮�� ���� ����Ʈ�� �̾Ƴ��� �����̸� ������ �� ������ ��� ����
		if (file.isDirectory()) { 
			String[] files = file.list();
			Arrays.sort(files);
			
            for (int i = 0; i < files.length; i++)
				indexDocs(new File(file, files[i]));
		}
		// �Ϲ� �����̸�
		else {
			System.out.println(file);
			//���� ������ �ִ� ���
			if (uidIter != null) {
				String uid = HantecDocument.uid(file); //������ ���� uid ����

				//�����Ϸ��� ���Ϻ��� ������ ������ ����
				while (uidIter.term() != null
						&& uidIter.term().field() == "uid"
						&& uidIter.term().text().compareTo(uid) < 0) {
					if (deleting) {
						System.out.println("deleting "
                                            + HantecDocument.uid2url(uidIter.term().text()));
						reader.deleteDocuments(uidIter.term());
					}
					uidIter.next();
				}

				//�����Ϸ��� ���ϰ� ������ ������ �״�� ����
				if (uidIter.term() != null && uidIter.term().field() == "uid"
						&& uidIter.term().text().compareTo(uid) == 0) {
					uidIter.next();
				}
				//�����Ϸ��� ������ �ֽ� ������ ��� ���� �߰�
				else if (!deleting) {
					System.out.println("adding " + file.getPath());
					addDocuments(writer, file);
				}
			}
			//���� ������ ��� �ִ� ���: ���ο� ���� �߰�
			else {
				System.out.println("adding " + file.getPath());
				addDocuments(writer, file);
			}
		}
	}

	//file�� �о�� ���ο� �߰� 
	private static void addDocuments(IndexWriter writer, File file) throws IOException, InterruptedException {
		ArrayList<HashMap<String, String>> docsList = readDocs(file);
        for ( int i=0; i<docsList.size(); i++) {
			HashMap<String, String> docsMap = docsList.get(i);
			String docNo = docsMap.get("DOCNO");
			String content = docsMap.get("DOC");
			String title = docsMap.get("TITLE");
			
			StringReader sr = new StringReader(content);
			
			System.out.println("[docNo: "+ docNo + "]");
			
			if (title == null)
			{
				System.out.println("TITLE_NULL: " + docNo);
				title = "";
			}
			
            Document doc= null;
            doc = HantecDocument.Document(sr,content, docNo, title);
			writer.addDocument(doc);
		}
	}

	//������ ���� �ʿ��� ������ file���� �ε�
	//�� ���Ͽ� ���� ������ �����ϹǷ�,
	//�� ������ ���� ������ȣ, ����, ��ü������ �и����� ������
	public static ArrayList<HashMap<String, String>> readDocs(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"EUC-KR"));
		ArrayList<HashMap<String, String>> docsList = new ArrayList<HashMap<String, String>>();
		try {
			String line = null;
			String sep = null;
			String newline = System.getProperty("line.separator");

			StringBuffer sb = null;
			HashMap<String, String> docsMap = null;
			boolean title=false;
			StringBuffer titleBuffer = null;
			while (null != (line=reader.readLine())) {
				sep = newline;
				if (line.equals("<DOC>")) {
					sb = new StringBuffer();
					docsMap = new HashMap<String, String>();
					sep = "";
					titleBuffer = new StringBuffer();
					title = false;
				}
               
                sb.append(sep+line);
                
				if (line.startsWith("<DOCID>")) {
		    		int endIndex = line.indexOf("</DOCID>");
		    		String docNo = line.substring(7, endIndex).trim();
		    		docsMap.put("DOCNO", docNo);
				}
				else if (line.startsWith("<TI>")) {
					title = true;
					int endIndex = line.indexOf("</TI>");
					if (endIndex > 0) {
						String titleStr = line.substring(4, endIndex).trim();
						docsMap.put("TITLE", titleStr);
						title = false;
					}
					else {
						sep = "";
						line = line.substring(4);
					}
				}
				else if (line.equals("</TI>")) {
					docsMap.put("TITLE", titleBuffer.toString().trim());
					title = false;
				}
				else if (line.equals("</DOC>")) {
					docsMap.put("DOC", sb.toString());
					docsList.add(docsMap);
				}
				
				if (title)
					titleBuffer.append(sep+line);
				
			}
		} 
		finally {
			reader.close();
		}
		
		return docsList;
	}
}

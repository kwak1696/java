import java.io.*;
import java.util.Date;

import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;
import org.apache.lucene.util.Version;

public class CJKSearcher {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			throw new Exception("Usage: java " + CJKSearcher.class.getName() + " <index-dir>");
		}
		
		File indexDir = new File(args[0]);
		if (!indexDir.exists() || !indexDir.isDirectory()) {
			throw new Exception(indexDir + " does not exist or is not a directory.");
		}

		// 반복적으로 검색어 입력 받아 검색 수행
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.print("\n검색어: ");
			String q = in.readLine();
			if (q.length() == 0)
				break;
			System.out.println();
			
			search(indexDir, q);
		}
	}
	
	public static void search(File indexDir, String q) throws Exception {
		Directory fsDir = FSDirectory.open(indexDir);
		IndexSearcher is = new IndexSearcher(fsDir);

		QueryParser qp = new QueryParser(Version.LUCENE_36, "contents", new CJKAnalyzer(Version.LUCENE_36));
		Query query = qp.parse(q);
		
		long start = new Date().getTime();
		TopDocs tdocs = is.search(query, 100);
		long end = new Date().getTime();
		
		System.err.println("Top " + tdocs.scoreDocs.length + " result(s) out of "+ tdocs.totalHits + " matched document(s) in " 
				+ (end-start) + " milliseconds by query '" + q + "'");
		
		for (int i=0; i<tdocs.scoreDocs.length; i++) {
			Document doc = is.doc(tdocs.scoreDocs[i].doc);
			System.out.println("[" + (i+1) + "] "+doc.get("txt"));
		}
		
		is.close();
	}
}

import java.io.*;
import org.apache.lucene.document.*;

/** 
 * HANTEC ������ ����� Document�� �߰��ϱ� ���� Ŭ����
 */
public class HantecDocument {
    static char dirSep = System.getProperty("file.separator").charAt(0);

    private HantecDocument() {}
    
    //�����̸�(URL)���κ��� ������ ID ���ڿ� ����. ���ʿ� ���� �ð� ������ ������
    //�����̸��� �����ϴ��� �������� �ð��� �ٸ��� ID�� �޶���
    public static String uid(File f) {
    	// /�� �ι��ڷ� ġȯ
        return f.getPath().replace(dirSep, '\u0000') +"\u0000" +
                DateTools.timeToString(f.lastModified(), DateTools.Resolution.SECOND);
    }

    //uid�� �����̸�(URL) ���·� ��ȯ
    public static String uid2url(String uid) {
        String url = uid.replace('\u0000', '/');	  // �� ���ڸ� /�� ����
        return url.substring(0, url.lastIndexOf('/')); // ���ʿ� ���� �ð����� ����
    }

    //HANTEC �������� �ʿ��� ������ ��� Document�� �ʵ�� �߰��Ͽ� ����
    //�ٸ� ������ �����ϴ� ��� �� �Լ��� �����ؾ� �� �� ����
    public static Document Document(Reader reader,String txt, String docNo, String title) throws IOException, InterruptedException  {
        Document doc = new Document();

        // ���� ���밡������
        doc.add(new Field("txt", txt, Field.Store.YES,Field.Index.NOT_ANALYZED));
        // ���� ����
        doc.add(new Field("contents", reader));

        // ���� ��ȣ
        doc.add(new Field("DOCNO", docNo, Field.Store.YES, Field.Index.NOT_ANALYZED));

        // ����
        doc.add(new Field("title", title, Field.Store.YES, Field.Index.ANALYZED));
        
        return doc;
    }
}

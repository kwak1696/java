import java.io.*;
import org.apache.lucene.document.*;

/** 
 * HANTEC 문서를 루씬의 Document로 추가하기 위한 클래스
 */
public class HantecDocument {
    static char dirSep = System.getProperty("file.separator").charAt(0);

    private HantecDocument() {}
    
    //파일이름(URL)으로부터 유일한 ID 문자열 생성. 뒷쪽에 파일 시간 정보를 덧붙임
    //파일이름이 동일하더라도 최종수정 시간이 다르면 ID가 달라짐
    public static String uid(File f) {
    	// /는 널문자로 치환
        return f.getPath().replace(dirSep, '\u0000') +"\u0000" +
                DateTools.timeToString(f.lastModified(), DateTools.Resolution.SECOND);
    }

    //uid를 파일이름(URL) 형태로 변환
    public static String uid2url(String uid) {
        String url = uid.replace('\u0000', '/');	  // 널 문자를 /로 복원
        return url.substring(0, url.lastIndexOf('/')); // 뒷쪽에 붙은 시간정보 제거
    }

    //HANTEC 문서에서 필요한 내용을 루씬 Document의 필드로 추가하여 리턴
    //다른 문서를 색인하는 경우 이 함수를 수정해야 할 수 있음
    public static Document Document(Reader reader,String txt, String docNo, String title) throws IOException, InterruptedException  {
        Document doc = new Document();

        // 문서 내용가져오기
        doc.add(new Field("txt", txt, Field.Store.YES,Field.Index.NOT_ANALYZED));
        // 문서 내용
        doc.add(new Field("contents", reader));

        // 문서 번호
        doc.add(new Field("DOCNO", docNo, Field.Store.YES, Field.Index.NOT_ANALYZED));

        // 제목
        doc.add(new Field("title", title, Field.Store.YES, Field.Index.ANALYZED));
        
        return doc;
    }
}

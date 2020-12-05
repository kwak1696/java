import java.io.*;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;
import org.apache.lucene.util.Version;

import org.apache.lucene.benchmark.quality.QualityQuery;
import org.apache.lucene.benchmark.quality.trec.TrecTopicsReader;


public class CJKSearchHantec {

	public static void main(String[] args) throws Exception {
		
		
		File indexDir = new File(args[0]);
		if (!indexDir.exists() || !indexDir.isDirectory()) {
			throw new Exception(indexDir + " does not exist or is not a directory.");
		}

		IndexReader reader = IndexReader.open(FSDirectory.open(indexDir));
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser qp = new QueryParser(Version.LUCENE_36, "contents", new CJKAnalyzer(Version.LUCENE_36));
		
		//HANTEC topic을 읽어들여, 각 topic 단위로 검색
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(args[1])));
		QualityQuery[] queries = new TrecTopicsReader().readQueries(in);
		int topicCount=0;
		
		for (QualityQuery q : queries) {
			// topic의 title만으로 query를 구성함
			Query query = qp.parse(q.getValue("title"));
			TopDocs tdocs = searcher.search(query, 1000);
			
			// Evaluation하기 위한 포맷으로 출력
			for (int i=0; i<tdocs.scoreDocs.length; i++) {
				Document doc = searcher.doc(tdocs.scoreDocs[i].doc);
				System.out.println(q.getQueryID() + "\tQ0\t" + doc.get("DOCNO") + "\t" + (i+1) +
						"\t" + tdocs.scoreDocs[i].score + "\tTeam_Name");
			}
			
			topicCount++;
		}

		searcher.close();
		reader.close();
		
		System.err.println("총 " + topicCount + "개의 topic이 처리됐습니다.");
	}
}


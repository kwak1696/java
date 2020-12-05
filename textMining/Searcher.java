import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;
import org.apache.lucene.util.Version;

public class Searcher {
public Vector<String[]> vc;
public Searcher(String filePath,String query) throws Exception{
	if (filePath==null) {
		throw new Exception("Usage: java " + Searcher.class.getName() + " <index-dir>");
	}
	
	vc = new Vector<String[]>();
	
	File indexDir = new File(filePath);
	if (!indexDir.exists() || !indexDir.isDirectory()) {
		throw new Exception(indexDir + " does not exist or is not a directory.");
	}

	// 반복적으로 검색어 입력 받아 검색 수행
	search(indexDir, query);
	
}

	
	
	public void search(File indexDir, String q) throws Exception {
		Directory fsDir = FSDirectory.open(indexDir);
		IndexSearcher is = new IndexSearcher(fsDir);
		
		QueryParser qp = new QueryParser(Version.LUCENE_36, "contents", new KoreanVer3Analyzer(Version.LUCENE_36));
		Query query = qp.parse(q);
		
		long start = new Date().getTime();
		TopDocs tdocs = is.search(query, 100);
		long end = new Date().getTime();
		
		System.err.println("Top " + tdocs.scoreDocs.length + " result(s) out of "+ tdocs.totalHits + " matched document(s) in " 
				+ (end-start) + " milliseconds by query '" + q + "'");
		
		for (int i=0; i<tdocs.scoreDocs.length; i++) {
			Document doc = is.doc(tdocs.scoreDocs[i].doc);
			String str[] = new String[4];
			str[0]=i+1+"";
			str[1]=doc.get("DOCNO");
			str[2]=doc.get("title");
			str[3]=doc.get("txt");
			vc.add(str);
			
		}
		
		
		
		is.close();
		
	}
}

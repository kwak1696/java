import java.io.*;
import java.util.*;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class AnalyzerUtils {
	public static List<String> tokensFromAnalysis(Analyzer anal, String text) throws IOException {
		TokenStream ts = anal.tokenStream("contents", new StringReader(text));
		CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);
		ArrayList<String> tokenList = new ArrayList<String>();
		try {
			ts.reset();
			while (ts.incrementToken()) {
				tokenList.add(termAtt.toString());
			}
			ts.end();
		}
		finally {
			ts.close();
		}

		return tokenList;
	}
	
	
	public static void displayTokens(Analyzer anal, String text) throws IOException {
		List<String> tokens = tokensFromAnalysis(anal, text);
		
		for (int i=0; i<tokens.size(); i++) {
			System.out.print("[" + tokens.get(i) + "]");
		}
	}
}

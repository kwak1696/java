import java.io.*;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.util.Version;

public class AnalyzerTest {
	public static final Version matchVersion = Version.LUCENE_36;
	
	private static final String[] examples = {
		/*
		"나는 학생이고, 너는 선생입니다",//'입니다.'에서 .의 유무에 따른차이
		"니다",//하나의 스테머 무한반복<while>
		"안녕하세요. 안녕하세요?",//. ,  ? 등을 일일히 끝에 붙여줘야하는 차이
		"은은 싸지만, 금은 비싸다.",// '은' 불용어처리문제
		*/
		"나는 학생이고, 너는 선생입니다" 

	};
	
	private static final Analyzer[] analyzers = new Analyzer[] {
		new CJKAnalyzer(matchVersion),
		new KoreanVer3Analyzer(matchVersion)
	};
	
	public static void main(String[] args) throws IOException {
		String[] strings = examples;
		if (args.length > 0) {
			strings = args;
		}
		
		for(int i=0; i<strings.length; i++) {
			analyze(strings[i]);
		}
	}
	
	private static void analyze(String text) throws IOException {
		System.out.println("Analyzing \"" + text + "\"");
		for(int i=0; i<analyzers.length; i++) {
			Analyzer analyzer = analyzers[i];
			String name = analyzer.getClass().getName();
			name = name.substring(name.lastIndexOf(".")+1);
			System.out.println("  " + name + ":");
			System.out.print("    ");
			AnalyzerUtils.displayTokens(analyzer, text);
			System.out.println();
		}
		System.out.println();
	}
}

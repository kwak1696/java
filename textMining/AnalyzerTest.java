import java.io.*;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.util.Version;

public class AnalyzerTest {
	public static final Version matchVersion = Version.LUCENE_36;
	
	private static final String[] examples = {
		/*
		"���� �л��̰�, �ʴ� �����Դϴ�",//'�Դϴ�.'���� .�� ������ ��������
		"�ϴ�",//�ϳ��� ���׸� ���ѹݺ�<while>
		"�ȳ��ϼ���. �ȳ��ϼ���?",//. ,  ? ���� ������ ���� �ٿ�����ϴ� ����
		"���� ������, ���� ��δ�.",// '��' �ҿ��ó������
		*/
		"���� �л��̰�, �ʴ� �����Դϴ�" 

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

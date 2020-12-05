import java.io.IOException;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.AttributeSource;

public class KoreanStemFilter extends TokenFilter {
	private static final String[] endings = {
		"입니다.", "이어요.", "습니다.", "가요?","하세요."
	};
	
	private final CharTermAttribute termAtt;

	public KoreanStemFilter(TokenStream input) {
		super(input);
		termAtt = addAttribute(CharTermAttribute.class);
	}

	public boolean incrementToken() throws IOException {
		if (!input.incrementToken())
			return false;

		String stem = stem(termAtt.toString());
		termAtt.copyBuffer(stem.toCharArray(), 0, stem.length());
		return true;
	}

	private String stem(String term) {
		int longestLeng = 0;
		int longestId = -1;
		
		for (int i=0; i<endings.length; i++) {
			if (term.endsWith(endings[i])) {
				longestLeng = endings[i].length();
				longestId = i;
			}
		}
		
		if (longestLeng == 0 || longestId == -1)
			return term;
		else
			return term.substring(0, term.length() - longestLeng);
	}
}

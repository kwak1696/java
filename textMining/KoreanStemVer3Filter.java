import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.AttributeSource;

public class KoreanStemVer3Filter extends TokenFilter {
	ArrayList<String> endingWord;// 어미
	ArrayList<String> assistWord;// 조사
	ArrayList<String> meanWord;// 뜻 있는 단어

	public ArrayList<String> getWords(String txt) {
		String data;
		ArrayList<String> ar = new ArrayList<String>();
		try {
			FileInputStream fin = new FileInputStream(txt);
			BufferedReader br = new BufferedReader(new InputStreamReader(fin));

			while ((data = br.readLine()) != null) {
				ar.add(data);

			}

		} catch (IOException e) {
			System.err.println(e);
		}
		return ar;
	}

	// ///////////////////////////////////////////////
	private HashSet<String> exclusions = new HashSet<String>();

	public void addExclusionWord(ArrayList<String> wordAdd) {

		for (int i = 0; i < wordAdd.size(); i++) {
			exclusions.add(wordAdd.get(i));
		}
	}

	// ////////////////////////////////////////////////

	private final CharTermAttribute termAtt;

	public KoreanStemVer3Filter(TokenStream input) {
		super(input);
		termAtt = addAttribute(CharTermAttribute.class);
		// ////////////////////////////////////////////////
		endingWord = getWords("어미.txt");
		assistWord = getWords("조사.txt");
		meanWord = getWords("제외.txt");
		addExclusionWord(meanWord);
		// ////////////////////////////////////////////////
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
		String subterm = null;
		ArrayList<ArrayList<String>> stemWords = new ArrayList<ArrayList<String>>();
		stemWords.add(endingWord);
		stemWords.add(assistWord);
		String str[] = new String[2];
		
		
		subterm = term;
		for (int j = 0; j < stemWords.size(); j++) {

			
			for (int i = 0; i < stemWords.get(j).size(); i++) {
				while (subterm.endsWith(stemWords.get(j).get(i))) {
					// ///////////////////////////////////////////////
					if (exclusions.contains(subterm)) {
						return subterm;
					}

					// ///////////////////////////////////////////////
					longestLeng = stemWords.get(j).get(i).length();
					longestId = i;
					// ////////////////////////////////////////////////////////
					subterm = term.substring(0, subterm.length() - longestLeng);
					// /////////////////////////////////////////////////////////
				}
			}
			
			
		}
		str[0]=subterm;
		
		subterm = term;
		for (int j = stemWords.size()-1; j >=0; j--) {
			
			
			for (int i = stemWords.get(j).size()-1; i >=0 ; i--) {
				
				
				if (subterm.endsWith(stemWords.get(j).get(i))) {
					// ///////////////////////////////////////////////
					if (exclusions.contains(subterm)) {
						
						return subterm;
					}

					// ///////////////////////////////////////////////
					longestLeng = stemWords.get(j).get(i).length();
					longestId = i;
					// ////////////////////////////////////////////////////////
					
					subterm = term.substring(0, subterm.length() - longestLeng);
					// /////////////////////////////////////////////////////////
					
				}
				
				
			}
			
			
		}
		str[1]=subterm;
		
		if(str[0].length()<str[1].length()){
			subterm=str[0];
		}else{
			subterm=str[1];
		}
		if (longestLeng == 0 || longestId == -1){
			return term;
		}
		else{
			
			return subterm;
		}
	}
}

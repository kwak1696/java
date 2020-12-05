import java.io.Reader;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

public class KoreanVer3Analyzer extends Analyzer {
	
	private Version version;
	
	public KoreanVer3Analyzer(Version version) {
		this.version = version;
	}
	
	public TokenStream tokenStream(String fieldName, Reader reader) {
		return new KoreanStemVer3Filter(new StandardTokenizer(version, reader)); 
	}
}

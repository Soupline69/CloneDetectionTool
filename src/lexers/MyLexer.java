package lexers;

import java.io.File;
import java.util.List;
import java.util.Map;

import model.CustomToken;

public interface MyLexer {

	public Map<File, List<CustomToken>> getTokens(List<File> files);

}
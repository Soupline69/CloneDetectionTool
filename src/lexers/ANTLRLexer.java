package lexers;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenFactory;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.UnbufferedCharStream;
import org.antlr.v4.runtime.atn.ATN;

import model.CustomToken;

public class ANTLRLexer extends Lexer implements MyLexer {
	
	protected ANTLRLexer() {
		super();
	}
	
	/**
	 * Récupère les tokens de tous les fichiers d'un projet
	 */
	@Override
	public Map<File, List<CustomToken>> getTokens(List<File> files) {
		Map<File, List<CustomToken>> myTokens = new HashMap<>();
 
		files.stream().forEach(file -> {
			try {
				CharStream in = new UnbufferedCharStream(new FileInputStream(file));
				this.setInputStream(in);
				this.setTokenFactory(new CommonTokenFactory(true));

				CommonTokenStream tokens = new CommonTokenStream(this);
				tokens.fill();
					
				List<CustomToken> tokensList = new ArrayList<>();
					
				tokens.getTokens().stream().forEach(t -> tokensList.add(new CustomToken(file.getPath(), t.getLine(), t.getType(), t.getText())));
				myTokens.put(file, tokensList);
			} catch (Exception ignored) {}
		});
		
		return myTokens;	
	}

	@Override
	public ATN getATN() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGrammarFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getRuleNames() {
		// TODO Auto-generated method stub
		return null;
	}
}
package ecosystems;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import lexers.MyLexer;
import lexers.Python3Lexer;

public class PyPi extends EcosystemFromList {
	private static final String URLLIST = "https://pypi.python.org/simple/";
	private static final String URLPACKAGE1 = "https://pypi.python.org/pypi/";
	private static final String URLPACKAGE2 = "/json";

	public PyPi() {
		super();
	}

	@Override
	protected List<String> getPackageNames() throws Exception {
		Scanner sc = new Scanner(new URL(URLLIST).openStream());
		List<String> packageNames = new ArrayList<>();
		
		while (sc.hasNextLine()) {
			String name = sc.nextLine();
			
			if(name.contains("href")) {
				packageNames.add(name.split("'")[1]);
			}
		}
		
		sc.close();
				
		return packageNames;
	}

	@Override
	protected String getPackageName(String url) {	
		return url.split("/")[url.split("/").length - 1];
	}
	
	@Override
	protected String getURL(String fileName) throws Exception {
		Scanner sc = new Scanner(new URL(URLPACKAGE1 + fileName + URLPACKAGE2).openStream());
		String url = "";
		
		String text = "";
		while(sc.hasNextLine()) {
			text += sc.nextLine();
		}
		
		if(text.contains("\"urls\":")) {
			String[] s = text.split("\"urls\":")[1].split("\"url\":");
			
		    int i = 1;
			while(!s[i].contains(".tar") && i < s.length) {
				i++;
			}
			
			url = s[i].split(",")[0].replace("\"", "");
		}
		
		sc.close();
		
		return url;
	}

	@Override
	public MyLexer getLexer() {
		return new Python3Lexer();
	}

	@Override
	public String getExtension() {
		return ".py";
	}

}
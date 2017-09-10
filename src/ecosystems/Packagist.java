package ecosystems;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import lexers.MyLexer;
import lexers.PHPLexer;

public class Packagist extends EcosystemFromList {
	private static final String URLLIST = "https://packagist.org/packages/list.json";
	private static final String URLPACKAGE1 = "https://packagist.org/p/";
	private static final String URLPACKAGE2 = ".json";
	
	public Packagist() {
		super();
	}
	
	@Override
	protected List<String> getPackageNames() throws Exception {
		Scanner sc = new Scanner(new URL(URLLIST).openStream());
		List<String> names = new ArrayList<>(Arrays.asList(sc.nextLine().split("\\[")[1].replace("\"", "").replace("\\", "").split(",")));
		sc.close();
		return names;
	}

	@Override
	protected String getPackageName(String url) {
		return url.split("/")[url.split("/").length - 3]+".zip";
	}

	@Override
	protected String getURL(String fileName) throws Exception {
		String url = "";
		Scanner sc = new Scanner(new URL(URLPACKAGE1 + fileName + URLPACKAGE2).openStream());
		
		String next = sc.nextLine();
		if(next.contains("\"source\":")) {
			url = next.split("\"source\":")[1].split("\"dist\":")[1].split("\"url\":")[1].split(",")[0].replace("\"", "").replace("\\", "");
		}
		
		sc.close();

		return url;
	}

	@Override
	public MyLexer getLexer() {
		return new PHPLexer();
	}

	@Override
	public String getExtension() {
		return ".php";
	}

}
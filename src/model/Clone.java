package model;

import java.util.List;

public class Clone {
	private String systemFile;
	private int beginSystem;
	private int endSystem;
	private String projectFile;
	private int beginProject;
	private int endProject;
	private List<CustomToken> tokens;

	public Clone(String systemFile, int beginSystem, int endSystem, String projectFile, int beginProject, int endProject, List<CustomToken> tokens) {
		this.systemFile = systemFile;
		this.beginSystem = beginSystem;
		this.endSystem = endSystem;
		this.projectFile = projectFile;
		this.beginProject = beginProject;
		this.endProject = endProject;
		this.tokens = tokens;
	}
	
	public List<CustomToken> getTokens() {
		return tokens;
	}

	@Override
	public String toString() {
		return " -> clone trouvé de taille "+tokens.size()+"\n"
				+ "fichier système : "+systemFile+" (de la ligne "+beginSystem+" à la ligne "+endSystem+")\n"
				+ "a matché avec le fichier "+projectFile+" du projet (de la ligne "+beginProject+" à la ligne "+endProject+")\n"
				+ "La séquence trouvée est "+tokens.toString()+"\n\n";
	}

}
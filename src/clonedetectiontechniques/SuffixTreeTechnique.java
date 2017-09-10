package clonedetectiontechniques;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import managers.FileManager;
import model.Clone;
import model.CustomToken;
import model.SuffixTree;
import writers.Writer;
import ecosystems.Ecosystem;

public class SuffixTreeTechnique extends CloneDetectionTechnique {

	public SuffixTreeTechnique() {}

	@Override
	public void getClones(Ecosystem ecosystem, List<Writer> writers, File systemProject, int threshold) throws Exception {
		System.out.println("\n---------- Construction de l'arbre des suffixes ----------");
		
		long timeTokens = System.currentTimeMillis();
		Map<File, List<CustomToken>> tokens = getTokens(ecosystem, systemProject);
		System.out.println("\nTokenization en "+Long.toString((System.currentTimeMillis() - timeTokens)/1000)+" sec");
		
		long timeAS = System.currentTimeMillis();
		SuffixTree suffixTree = new SuffixTree(tokens.values());
		tokens.clear();
		System.out.println("Taille : "+suffixTree.getSize()+" tokens\nNoeuds : "+suffixTree.getNumberNode()+"\nTemps : "+Long.toString((System.currentTimeMillis() - timeAS)/1000)+" sec");

		foundClones(ecosystem, writers, systemProject, suffixTree, threshold);
	}
	
	/**
	 * Trouve tout les clones en comparant chaque archive contenu dans le dossier "projetArchives" avec l'arbre des suffixes
	 * @param ecosystem
	 * @param writers
	 * @param systemProject
	 * @param suffixTree
	 * @param threshold
	 * @throws Exception
	 */
	private void foundClones(Ecosystem ecosystem, List<Writer> writers, File systemProject, SuffixTree suffixTree, int threshold) throws Exception {
		System.out.println("\n---------- Recherche de similitudes ----------");
		long timeSearch = System.currentTimeMillis();
		File directory = new File(ecosystem.getAccessPath());

		List<File> archiveFiles = Arrays.asList(directory.listFiles());
		List<Integer> countClone = new ArrayList<>();
		
		archiveFiles.parallelStream().forEach(file -> { 
			if(!systemProject.getName().equals(file.getName())) {
				try {
					List<Clone> clones = suffixTree.search(getTokens(ecosystem, file), threshold);
					if(!clones.isEmpty()) {
						countClone.add(clones.size());
						write(writers, file, clones);
					}
				} catch (Exception ignored) {}
			}
		});
		
		updateNumberOfClonesFound(writers, countClone);
				
		System.out.println("Clones trouvés : "+countClone.parallelStream().mapToInt(Integer::intValue).sum()+"\nTemps : "+Long.toString((System.currentTimeMillis() - timeSearch)/1000)+" sec");
	}
	
	/**
	 * Décompresse l'archive "file" et récupère la liste de tokens associée à tous les fichiers de ce projet
	 * @param ecosystem
	 * @param file
	 * @return une Map contenant chaque fichier et la liste de tokens associée à ce fichier d'un répertoire
	 * @throws Exception
	 */
	private Map<File, List<CustomToken>> getTokens(Ecosystem ecosystem, File file) throws Exception {
		File directory = FileManager.getInstance().decompress(file, ecosystem.getExtension());						
		return tokenization(directory, ecosystem);
	}
	
	/**
	 * Récupère pour tous les fichiers d'un projet la liste de tokens associée à ces projets
	 * @param directory
	 * @param ecosystem
	 * @return une Map contenant chaque fichier et la liste de tokens associée à ce fichier d'un répertoire
	 */
	private Map<File, List<CustomToken>> tokenization(File directory, Ecosystem ecosystem) {
		Map<File, List<CustomToken>> tokens = new HashMap<>();
		
		if(directory.exists()) {
			List<File> files = FileManager.getInstance().getUsefullFiles(directory);
			tokens = ecosystem.getLexer().getTokens(files);
		}
		
		return tokens;
	}

}
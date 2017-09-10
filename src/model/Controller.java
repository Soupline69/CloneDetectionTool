package model;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import writers.FileWriter;
import writers.Writer;
import managers.EcosystemManager;
import managers.FileManager;
import managers.WriterManager;
import managers.CloneDetectionTechniqueManager;
import ecosystems.Ecosystem;

public class Controller {
	private String ecosystemName;
	private int numberProjectToCompare;
	private String cloneDetectionTechniqueName;
	private File systemProjectName;
	private String outputDirectory;
	private List<String> outputExtensions;
	private int threshold;
	
	public Controller(List<String> args) {
		try {
	        long time = System.currentTimeMillis();        

	        System.out.println("\n---------- Vérification des paramètres ----------");
			long timeArguments = System.currentTimeMillis();
			checkArguments(args);
			System.out.println("en "+Long.toString(System.currentTimeMillis() - timeArguments)+" ms");
			
			Ecosystem ecosystem = EcosystemManager.getInstance().getEcosystem(ecosystemName);
			ecosystem.download(numberProjectToCompare);
			
			List<Writer> writers = WriterManager.getInstance().getWriters(outputExtensions, outputDirectory);
			
			CloneDetectionTechniqueManager.getInstance().getTechnique(cloneDetectionTechniqueName).getClones(ecosystem, writers, systemProjectName, threshold);
			
			writers.forEach(writer -> {
				if(writer instanceof FileWriter) {
					try {
						((FileWriter) writer).updateInfo((System.currentTimeMillis() - time)/1000, numberProjectToCompare);
						((FileWriter) writer).getFile().close();
					} catch (Exception ignored) {}
				}
			});
			
	        System.out.println("\n--> Le programme a été exécuté en : "+(System.currentTimeMillis() - time)/1000+" sec");

			FileManager.getInstance().deleteRootDirectory(new File(ecosystem.getAccessPath()));
			FileManager.getInstance().deleteProjects();
		} catch (Exception ignored) {}
	}
	
	/**
	 * Vérifie la pertinence des paramètres fournis par l'utilisateur
	 * @param args
	 * @throws Exception
	 */
	private void checkArguments(List<String> args) throws Exception {
		this.ecosystemName = args.get(checkArgument(args.indexOf("-ecosysteme"), "-ecosysteme") + 1);
		this.numberProjectToCompare = args.indexOf("-nombre") == -1 ? -1 : Integer.parseInt(args.get(args.indexOf("-nombre") + 1));
		this.cloneDetectionTechniqueName = args.get(checkArgument(args.indexOf("-technique"), "-techique") + 1);
		this.threshold = Integer.parseInt(args.get(checkArgument(args.indexOf("-seuil"), "-seuil") + 1));
		
		File system = new File(args.get(checkArgument(args.indexOf("-projetSysteme"), "-projetSysteme") + 1));

		if(!system.exists()) {
			throw new Exception("Le projet système n'existe pas");
		} else {
			this.systemProjectName = system;
		}
		
		this.outputDirectory = args.get(checkArgument(args.indexOf("-sortie"), "-sortie") + 1);
		this.outputExtensions = Arrays.asList(args.get(checkArgument(args.indexOf("-extensionsSortie"), "-extensionsSortie") + 1).split(","));
	}
	
	private int checkArgument(int index, String name) throws Exception {
		if(index == -1) {
			throw new Exception("L'argument "+name+" est manquant");
		}
		
		return index;
	}

}
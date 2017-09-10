package ecosystems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class EcosystemFromList extends Ecosystem {
	
	protected EcosystemFromList() {
		super();
	}
	
	/**
	 * Télécharge l'écosystème et plus précisement le nombre de projets de l'écosystème passé en paramètre
	 */
	@Override
	public void download(int number) throws Exception {
		System.out.println("\n---------- Téléchargement de l'écosystème ----------");
		
		long timeGetPackageNames = System.currentTimeMillis();
		List<String> names = getPackageNames();
		System.out.println("Récupèrer la liste de tous les paquets en "+Long.toString((System.currentTimeMillis() - timeGetPackageNames)/1000)+" sec");

		long timeDownload = System.currentTimeMillis();
		if(number > 0 && number < names.size()) {
			downloadSubList(names, number);
		} else {
			downloadList(names);
		}
		System.out.println("Téléchargement des paquets en "+Long.toString((System.currentTimeMillis() - timeDownload)/1000)+" sec");
	}
	
	/**
	 * Télécharge une sous-liste de projets de l'écosystème
	 * @param names
	 * @param number
	 */
	private void downloadSubList(List<String> names, int number) {
		List<Integer> randomListInt = new ArrayList<>();
		List<String> randomNames = new ArrayList<>();
		Random random = new Random();
		int i = 0;

		while(i < number) {
			int randomInt = getRandomInt(names.size(), randomListInt, random);
			randomListInt.add(randomInt);
			randomNames.add(names.get(randomInt));
			i++;
		}
		
		randomNames.parallelStream().forEach(name -> downloadOnePackage(name, names, randomListInt, random));
	}
	
	/**
	 * @param size
	 * @param randomListInt
	 * @param random
	 * @return un nombre entier au hasard compris entre 0 et size qui n'est pas déjà dans la liste randomListInt
	 */
	private int getRandomInt(int size, List<Integer> randomListInt, Random random) {
		int randomInt = random.nextInt(size + 1);
		
		if(!randomListInt.contains(randomInt)) {
			return randomInt;
		} 
		
		return getRandomInt(size, randomListInt, random);
	}
	
	/**
	 * Télécharge un projet et si une erreur survient pendant le téléchargement (dépôt github introuvable par exemple) en télécharge un nouveau
	 * @param name
	 * @param names
	 * @param randomListInt
	 * @param random
	 */
	private void downloadOnePackage(String name, List<String> names, List<Integer> randomListInt, Random random) {
		try {
			String url = getURL(name);
			downloadPackage(getPackageName(url), url);
		} catch(Exception ignored) {
			int randomInt = getRandomInt(names.size(), randomListInt, random);
			randomListInt.add(randomInt);
			downloadOnePackage(names.get(randomInt), names, randomListInt, random);
		}
	}
	
	/**
	 * Télécharge tous les projets de l'écosystème
	 * @param names
	 */
	private void downloadList(List<String> names) {
		names.parallelStream().forEach(name -> {
			try {
				downloadPackage(getPackageName(name), getURL(name));
			} catch(Exception ignored) {}
		});
	}
	
	/**
	 * @return la liste des noms de tous les projets contenus dans l'écosystème
	 * @throws Exception
	 */
	protected abstract List<String> getPackageNames() throws Exception;
	
	/**
	 * @param url
	 * @return le nom du projet en se basant sur son url
	 */
	protected abstract String getPackageName(String url);
	
	/**
	 * @param fileName
	 * @return l'url de téléchargement d'un projet
	 * @throws Exception
	 */
	protected abstract String getURL(String fileName) throws Exception;
}
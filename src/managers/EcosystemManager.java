package managers;

import ecosystems.Ecosystem;
import ecosystems.Packagist;
import ecosystems.PyPi;

public class EcosystemManager {
    private static EcosystemManager instance = new EcosystemManager();
	
	private EcosystemManager() {}
	
	public static EcosystemManager getInstance() {
        return instance;
    }
	
	/**
	 * @param ecosystemName
	 * @return l'écosystème choisi par l'utilisateur
	 * @throws Exception
	 */
	public Ecosystem getEcosystem(String ecosystemName) throws Exception {	
		switch(ecosystemName.toUpperCase()) {
			case "PACKAGIST" :
				return new Packagist();
			case "PYPI" :
				return new PyPi();
			default : 
				throw new Exception("Ecosystème inconnu");
		}
	}

}

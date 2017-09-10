package managers;

import clonedetectiontechniques.CloneDetectionTechnique;
import clonedetectiontechniques.SuffixTreeTechnique;

public class CloneDetectionTechniqueManager {
	 private static CloneDetectionTechniqueManager instance = new CloneDetectionTechniqueManager();
		
		private CloneDetectionTechniqueManager() {}
		
		public static CloneDetectionTechniqueManager getInstance() {
	        return instance;
	    }
		
		/**
		 * @param techniqueName
		 * @return la technique de d�tection de clones choisi par l'utilisateur
		 * @throws Exception
		 */
		public CloneDetectionTechnique getTechnique(String techniqueName) throws Exception {	
			switch(techniqueName.toUpperCase()) {
				case "SUFFIXTREE" : 
					return new SuffixTreeTechnique();
				default : 
					throw new Exception("Technique de d�tection de clones inconnue");
			}
		}

}

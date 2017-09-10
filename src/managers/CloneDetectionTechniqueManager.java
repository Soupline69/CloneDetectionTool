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
		 * @return la technique de détection de clones choisi par l'utilisateur
		 * @throws Exception
		 */
		public CloneDetectionTechnique getTechnique(String techniqueName) throws Exception {	
			switch(techniqueName.toUpperCase()) {
				case "SUFFIXTREE" : 
					return new SuffixTreeTechnique();
				default : 
					throw new Exception("Technique de détection de clones inconnue");
			}
		}

}

package clonedetectiontechniques;

import java.io.File;
import java.util.List;

import model.Clone;
import writers.FileWriter;
import writers.Writer;
import ecosystems.Ecosystem;

public abstract class CloneDetectionTechnique {
	
	/**
	 * Méthode qui met à jour les Writer à chaque fois qu'un projet est comparé à l'arbre des suffixes
	 * @param writers
	 * @param file
	 * @param clones
	 * @throws Exception
	 */
	protected void write(List<Writer> writers, File file, List<Clone> clones) throws Exception {
		for(Writer writer : writers) {
			writer.write(file, clones);
		}
		
		clones.clear();
	}
	
	/**
	 * Méthode qui met à jour les FileWriter avec le nombre total de clones trouvés
	 * @param writers
	 * @param clonesFound
	 * @throws Exception
	 */
	protected void updateNumberOfClonesFound(List<Writer> writers, List<Integer> clonesFound) throws Exception {
		for(Writer writer : writers) {
			if(writer instanceof FileWriter)
				((FileWriter) writer).updateNumberOfClonesFound(clonesFound);
		}
	}

	public abstract void getClones(Ecosystem ecosystem, List<Writer> writers, File systemProject, int threshold) throws Exception;
}
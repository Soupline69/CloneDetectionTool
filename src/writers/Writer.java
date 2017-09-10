package writers;

import java.io.File;
import java.util.List;

import model.Clone;

public interface Writer {
	
	/**
	 * Ecrit dans le format de sortie sp�cifi� par l'utilisateur la liste des clones trouv�e pour un projet
	 * @param file
	 * @param clones
	 * @throws Exception
	 */
	public void write(File file, List<Clone> clones) throws Exception;

}

package writers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.Clone;

public class TextWriter extends FileWriter {

	public TextWriter(String outputDirectory) throws Exception {
		super(outputDirectory, new File(outputDirectory+"/resultats.txt"));
		updateInfo(0, 0);
		updateNumberOfClonesFound(new ArrayList<>());
	}

	@Override
	public void write(File file, List<Clone> clones) throws Exception {    	
		this.file.write(("\n\n********** "+file.getName()+"********** "+Math.round(file.length()/1000)+" ko // " 
				+clones.size()+" clones trouvés avec ce projet\n\n").getBytes());
		
		clones.stream().forEach(clone -> {
			try {
				this.file.write(clone.toString().getBytes());
			} catch(Exception ignored) {}
		});
	}
	
	@Override
	public void updateNumberOfClonesFound(List<Integer> clonesFound) throws Exception {
		file.seek(70);
		file.write(("\n Il a trouvé "+clonesFound.size()+" projets qui ont du code en commun avec le projet système.\n"
				+ "Au total, il y a "+clonesFound.parallelStream().mapToInt(Integer::intValue).sum()
	    		+" clones.\n\n\nLes clones trouvés sont les suivants : \n\n\n").getBytes());
	}
	
	@Override
	public void updateInfo(long executionTime, int numberProjects) throws Exception {
		file.seek(0);
		file.write(("Le programme a été exécuté en "+executionTime+" secondes.\nIl a comparé "+numberProjects+" projets.\n").getBytes());
	}
	
	

}
package writers;

import java.io.File;
import java.util.List;
import java.util.Locale;

import model.Clone;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class CSVWriter extends FileWriter {

	public CSVWriter(String outputDirectory) throws Exception {
		super(outputDirectory, new File(outputDirectory+"/statistiques.csv"));
		file.write(("NomProjet,TailleProjet,NombreClones,MoyenneTailleClone,EcartTypeTailleClone,"
				+ "MinTailleClone,25%TailleClone,MedianTailleClone,75%TailleClone,MaxTailleClone\n").getBytes());
	}

	@Override
	public void write(File file, List<Clone> clones) throws Exception {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		clones.stream().forEach(clone -> stats.addValue(clone.getTokens().size()));
					
		this.file.write((file.getName()+","+Math.round(file.length()/1000)+","+clones.size()
			+","+String.format(Locale.US, "%.2f", stats.getMean())
			+","+String.format(Locale.US, "%.2f", stats.getStandardDeviation())
			+","+String.format(Locale.US, "%.2f", stats.getMin())
			+","+String.format(Locale.US, "%.2f", stats.getPercentile(25))
			+","+String.format(Locale.US, "%.2f", stats.getPercentile(50))
			+","+String.format(Locale.US, "%.2f", stats.getPercentile(75))
			+","+String.format(Locale.US, "%.2f", stats.getMax())
			+"\n").getBytes());
	}

	@Override
	public void updateNumberOfClonesFound(List<Integer> clonesFound) {
		
	}

	@Override
	public void updateInfo(long executionTime, int numberProjects) throws Exception {

	}

}
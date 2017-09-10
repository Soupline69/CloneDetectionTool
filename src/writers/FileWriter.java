package writers;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;

import model.Clone;

public abstract class FileWriter implements Writer {
	protected String outputDirectory;
	protected RandomAccessFile file;
	
	protected FileWriter(String outputDirectory, File file) throws Exception {
		this.outputDirectory = outputDirectory;
		file.delete();
		this.file = new RandomAccessFile(file, "rw");
	}
	
	@Override
	public abstract void write(File file, List<Clone> clones) throws Exception;
	
	public abstract void updateNumberOfClonesFound(List<Integer> clonesFound) throws Exception;

	public abstract void updateInfo(long executionTime, int numberProjects) throws Exception;
	
	public RandomAccessFile getFile() {
		return file;
	}

}
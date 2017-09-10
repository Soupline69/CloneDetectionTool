package managers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import writers.CSVWriter;
import writers.TextWriter;
import writers.Writer;

public class WriterManager {
    private static WriterManager instance = new WriterManager();
    
    private WriterManager() {}
    
    public static WriterManager getInstance() {
        return instance;
    }
    
    /**
     * @param outputExtensions
     * @param outputDirectory
     * @return une liste de Writer demandé par l'utilisateur
     * @throws Exception
     */
    public List<Writer> getWriters(List<String> outputExtensions, String outputDirectory) throws Exception { 
    	List<Writer> writers = new ArrayList<>();
    	
    	if(!new File(outputDirectory).exists()) {
    		outputDirectory = ".";
    	}
    	
    	if(outputExtensions.contains("txt")) {
    		writers.add(new TextWriter(outputDirectory));
    	}
    	
    	if(outputExtensions.contains("csv")) {
    		writers.add(new CSVWriter(outputDirectory));
    	}
    	
    	return writers;
	}
	
}

package managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;

public class FileManager {
	private static final String ACCESSPATH = "." + File.separator + "projets" + File.separator;
    private static FileManager instance = new FileManager();
	
	private FileManager() {
		deleteProjects();
	}
	
	public static FileManager getInstance() {
        return instance;
    }
	
	/**
	 * Décompresse l'archive "archiveDirectory"
	 * @param archiveDirectory
	 * @param extensionFiles
	 * @return le dossier décompréssé
	 * @throws Exception
	 */
	public File decompress(File archiveDirectory, String extensionFiles) throws Exception {
		String name = archiveDirectory.getName();
		String extensionArchive = name.substring(name.lastIndexOf('.'), name.length());
		File directory = new File(ACCESSPATH + name.substring(0, name.indexOf('.')));

		switch(extensionArchive) {
			case ".zip" :
				return genericDecompress(new ZipArchiveInputStream(new FileInputStream(archiveDirectory)), directory, extensionFiles);
			case ".gz" :
				return genericDecompress(new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(archiveDirectory))), directory, extensionFiles);
			case ".bz2" : 
				return genericDecompress(new TarArchiveInputStream(new BZip2CompressorInputStream(new FileInputStream(archiveDirectory))), directory, extensionFiles);
			default : 
				throw new Exception("Format inconnu pour le fichier "+archiveDirectory.getName());
		}
	}

	/**
	 * Méthode générique qui permet de décompresser une archive peu importe son extension
	 * @param in
	 * @param directory
	 * @param extensionFiles
	 * @return le fichier décompréssé
	 * @throws Exception
	 */
	private File genericDecompress(ArchiveInputStream in, File directory, String extensionFiles) throws Exception {		
    	ArchiveEntry entry = in.getNextEntry();
    	
    	while(entry != null) {	
	    	getFile(extensionFiles, directory.getPath() + File.separator + entry.getName(), in);	     	
		    entry = in.getNextEntry();
    	}
		    			
	   	in.close();	
		
		return directory;
	}
	
	/**
	 * Récupère le contenu d'un fichier seulement s'il est de la bonne extension
	 * @param extensionFiles
	 * @param name
	 * @param in
	 */
	private void getFile(String extensionFiles, String name, InputStream in) {		
	    if(name.toLowerCase().endsWith(extensionFiles)) {
			byte[] buffer = new byte[4096];
	
			try {
				File newFile = new File(name);
		        new File(newFile.getParent()).mkdirs();
		    	
		        FileOutputStream fos = new FileOutputStream(newFile);
		
		        int length;
		        while ((length = in.read(buffer)) > 0) {
		        	fos.write(buffer, 0, length);
		        }
		
		        fos.close();
			} catch(Exception ignored) {}
	    }
	}
	
	/**
	 * 
	 * @param rootDirectory
	 * @return la liste des fichiers contenus dans le répertoire et les sous-répertoires
	 */
	public List<File> getUsefullFiles(File rootDirectory) {		
		List<File> files = new ArrayList<>();
		
		for(File file : rootDirectory.listFiles()) {
			if(file.isDirectory()) {
				files.addAll(getUsefullFiles(file));
			} else {
				files.add(file);
			}
		}
		
		return files;
	}
	
	public void deleteRootDirectory(File rootDirectory) {
		try {
			FileUtils.deleteQuietly(rootDirectory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteProjects() {
		deleteRootDirectory(new File(ACCESSPATH));
	}

}
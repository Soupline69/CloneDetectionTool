package ecosystems;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import lexers.MyLexer;
import managers.FileManager;

public abstract class Ecosystem {
	private static final String ACCESSPATH = "." + File.separator + "projetArchives" + File.separator;
	
	Ecosystem() { 
		File file = new File(ACCESSPATH);
		FileManager.getInstance().deleteRootDirectory(file);
		file = new File(ACCESSPATH);
		file.mkdir();
	}
	
	/**
	 * Télécharge le projet via l'url fournie en paramètre
	 * @param packageName
	 * @param urlName
	 * @throws Exception
	 */
	protected void downloadPackage(String packageName, String urlName) throws Exception {
		String target = ACCESSPATH + packageName;
		
		if(!new File(target).exists()) {
			URL url = new URL(urlName);
	        InputStream in = url.openConnection().getInputStream();
	        FileOutputStream out = new FileOutputStream(target);
	        byte[] b = new byte[4096];
	        
	        int count;
	        while ((count = in.read(b)) >= 0) {
	            out.write(b, 0, count);
	        }
	        
	        out.close();
	        in.close();
		} else {
			throw new Exception("Le package existe déjà");
		}
	}
	
	public String getAccessPath() {
		return ACCESSPATH;
	}
		
	public abstract MyLexer getLexer();
	
	public abstract String getExtension();
	
	public abstract void download(int number) throws Exception;
}

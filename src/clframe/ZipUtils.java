package clframe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;



class ZipUtils {

	
    /**
     * Processes a zip file with a zip processor! Each zip entry is passed to the zip processor while looping through the zip entries!!!
     */
    public static void zipProcess(String zipFile, IZipEntryProcessor processor){
    	zipProcess(new File(zipFile), processor);
    }
    
    /**
     * Processes a zip file with a zip processor! Each zip entry is passed to the zip processor while looping through the zip entries!!!
     */
    public static void zipProcess(File zipFile, IZipEntryProcessor processor){
    	 try {
			zipProcess(new FileInputStream(zipFile), processor);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    }
    
    
    
    public static void zipProcess(InputStream is, IZipEntryProcessor processor){
    	ZipInputStream zis = null;
    	try{
    		if(processor == null) return;
        	//get the zip file content
        	 zis = new ZipInputStream(is);
        	//get the zipped file list entry
        	ZipEntry ze = zis.getNextEntry();
        	while(ze!=null){
        		String ename = ze.getName();
        		processor.process(ze, zis);
                ze = zis.getNextEntry();
        	}
        	//System.out.println("Done");
        }catch(IOException ex){
           throw new RuntimeException(ex);
        }finally{
        	try {
				zis.closeEntry();
				zis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e);
			}
         	
        }
    }
  
    
    public interface IZipEntryProcessor {
    	public void process(ZipEntry entry, ZipInputStream zis) ;
    }
}

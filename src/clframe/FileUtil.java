
package clframe;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;


final class FileUtil {
	
	public static void saveToFile(File file, byte [] bytes) throws IOException{
		FileOutputStream fOutputStream = new FileOutputStream(file);
		try{
			fOutputStream.write(bytes);
			fOutputStream.flush();
			fOutputStream.close();
		}finally{
			fOutputStream.close();
		}
	}
	
	private static class ByteArray {
		ByteArray(byte []b, int size){
			this.b = b;
			this.size = size;
		}
	
		byte [] b;
		int size;
	}
	
	public static byte[] readFileContent(File file ){
		return readFileContent(file, 1024*1024);
	}
	
	public static byte[] readFileContent(File file ,int bufferSize){
		// contain bytes read from file
	      //Vector fileBytes = new Vector();
		 List<ByteArray>  barrayList = new ArrayList<ByteArray>();
		 byte [] buffer = new byte [bufferSize];
		 int size = 0;
		 
	      // read contents from file 
	      try {
	         FileInputStream in = new FileInputStream( file );
	         // read bytes from stream.
	         int blength;
	        
	         while ((blength = in.read(buffer))!=-1) {
	        	 barrayList.add(new ByteArray(buffer, blength));
	        	 size+=blength;
	            //contents = ( byte )in.read();
	            //fileBytes.add( new Byte( contents ) );
	         }
	           
	         in.close();
	      } 
	      // handle IOException
	      catch ( IOException exception ) {
	         exception.printStackTrace();
	      }
	      
	      // create byte array from contents in Vector fileBytes
	      byte[] fileContent = new byte[ size];
	      
	      int offset = 0;
	      for ( int i = 0; i < barrayList.size(); i++ ) {
	    	  ByteArray b = barrayList.get(i);
	    	  
	    	  for(int j=0; j < b.size; j++){
	    		  fileContent[offset+j] = b.b[j];
	    	  }
	    	  offset+=b.size;
	      }
	      return fileContent;
	}
	
	
	/**
	 * Copies the sourceLocation directory together with all its contents to the targetLocation directory!!! If targetLocation doesn't exists it is being created!!!
	 * @param sourceLocation - the source folder location 
	 * @param targetLocation - the destination folder location
	 * @throws IOException
	 */
	public static  void copyDirectory(File sourceLocation , File targetLocation)
		    throws IOException {

		        if (sourceLocation.isDirectory()) {
		            if (!targetLocation.exists()) {
		                targetLocation.mkdir();
		            }

		            String[] children = sourceLocation.list();
		            for (int i=0; i<children.length; i++) {
		                copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
		            }
		        } else {

		            InputStream in = new FileInputStream(sourceLocation);
		            OutputStream out = new FileOutputStream(targetLocation);
		            try{
			            // Copy the bits from instream to outstream
			            byte[] buf = new byte[1024*1024];
			            int len;
			            while ((len = in.read(buf)) > 0) {
			                out.write(buf, 0, len);
			            }
		            }finally{
		            	in.close();
		            	out.close();
		            }
		        }
	}
	
	
	public static String md5Hash(byte [] b) {
		try{
		  MessageDigest md;
          md = MessageDigest.getInstance("MD5");
          byte[] md5hash = new byte[32];
          md.update(b, 0, b.length);
          md5hash = md.digest();
          return convertToHex(md5hash);
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
	public static String md5Hash(String text)  { 
		try{
	        MessageDigest md;
	        md = MessageDigest.getInstance("MD5");
	        byte[] md5hash = new byte[32];
	        md.update(text.getBytes("utf8"), 0, text.length());
	        md5hash = md.digest();
	        return convertToHex(md5hash);
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	} 
	
	
	
	private static String convertToHex(byte[] data) { 
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) { 
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do { 
                if ((0 <= halfbyte) && (halfbyte <= 9)) 
                    buf.append((char) ('0' + halfbyte));
                else 
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        } 
        return buf.toString();
    } 
	
	/**
	 * Copies a file from one destination to another!!! 
	 * @param sourceFile
	 * @param destFile
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static void copyFile(File sourceFile, File destFile)
			throws IOException {
		if(sourceFile.getCanonicalPath().equals(destFile.getCanonicalPath())) return;
		if (!sourceFile.exists()) {
			return;
		}
		if (!destFile.exists()) {
			destFile.createNewFile();
		}
		FileChannel source = null;
		FileChannel destination = null;
		source = new FileInputStream(sourceFile).getChannel();
		destination = new FileOutputStream(destFile).getChannel();
		if (destination != null && source != null) {
			destination.transferFrom(source, 0, source.size());
		}
		if (source != null) {
			source.close();
		}
		if (destination != null) {
			destination.close();
		}
	}
	
	
	


	/**Traverse directory trees
	 * 
	 * @param node - the current file node
	 * @param trcallback  - what is to be done forward & backward
	 */
	public static void traverseDirs(File node, TraverseDirsCallBack trcallback){
		
		if(trcallback != null)trcallback.OnForward(node);
		//System.out.println(node.getAbsoluteFile());
		
		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				traverseDirs(new File(node, filename), trcallback);
				if(trcallback != null) trcallback.OnReturnFromRecursion(node);//act on the directory
			}
		}
		if(trcallback != null) trcallback.OnReturnFromRecursion(node);  //act on the file
	}
	
	/***
	 * The names of files in the File if file is directory! If File is a File returns null !
	 * @param node
	 * @return
	 */
	public static String [] getDirContent(File node){
		if(node == null) return null;
		return node.isDirectory() ?  node.list() : null;
	}
	
	/***
	 * The names of files in the File if file is directory! If File is a File returns null !
	 * @param node
	 * @return
	 */
	public static String [] getDirContent(String pathname){
		if(pathname == null) return null;
		File node = new File(pathname);
		return node.isDirectory() ?  node.list() : null;
	}
	
/*	*//***
	 *//*
	public static void traverseDirsBfs(File node, int level){
		BfsFileQueue<String> queue = new BfsFileQueue<String>();
		queue.add(node.getAbsolutePath());
		
		while(!queue.isEmpty()){
			String currentFile = queue.moveHeadPointer();
			for(String s : getDirContent(currentFile)){
				if(s!=null) queue.add(s);
			}
		}
	}*/
	
	public static void textFileExporter(List<String> list, String path, String filename, 
			String charsetName) throws FileNotFoundException, UnsupportedEncodingException {
		File f = new File(path, filename);
    	FileOutputStream os = new FileOutputStream(f);
    	OutputStreamWriter osw = new OutputStreamWriter(os, charsetName);
    	//BufferedWriter out = new BufferedWriter(osw);

    	try {
	    	for (int i = 0; i < list.size(); i++) {
					osw.write(list.get(i));
					//System.out.println(list.get(i));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
				try {
					osw.flush();
					osw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
		}
	}
	
	
	/**CRC calculator*/
	public static long checksumInputStream(String filepath) throws IOException {
		  InputStream inputStreamn = new FileInputStream(filepath);
		  CRC32 crc = new CRC32();
		  int cnt;
		  while ((cnt = inputStreamn.read()) != -1) {crc.update(cnt); }
		  inputStreamn.close();
		  return crc.getValue();
	}

	public static long checksumBufferedInputStream(String filepath) throws IOException {

		  InputStream inputStream = new BufferedInputStream(new FileInputStream(filepath));
		  CRC32 crc = new CRC32();
		  int cnt;
		  while ((cnt = inputStream.read()) != -1) {
			  crc.update(cnt);
		  }
		  inputStream.close();
		  return crc.getValue();
	}
    
	public static long checksumRandomAccessFile(String filepath) throws IOException {

		  RandomAccessFile randAccfile = new RandomAccessFile(filepath, "r");
		  long length = randAccfile.length();
		  CRC32 crc = new CRC32();

		  for (long i = 0; i < length; i++) {
			  	randAccfile.seek(i);
			  	int cnt = randAccfile.readByte();
			  	crc.update(cnt);
		  }
		  randAccfile.close();
		  return crc.getValue();
	}

    public static long checksumMappedFile(String filepath) throws IOException {
		  FileInputStream inputStream = new FileInputStream(filepath);
		  return checksumMappedFile(inputStream);
    }
    /**end of CRC calculator*/
    
    public static long checksumMappedFile(FileInputStream inputStream) throws IOException {
		  FileChannel fileChannel = inputStream.getChannel();
		  int len = (int) fileChannel.size();
		  MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, len);

		  CRC32 crc = new CRC32();

		  for (int cnt = 0; cnt < len; cnt++) {
			  	int i = buffer.get(cnt);
			  	crc.update(i);
		  }
		  inputStream.close();
		  return crc.getValue();
  }
	
	
 
	/*
	 * public static char[] DOSCYR2UTF8(byte[] b) throws IOException { int code = 0;
	 * 
	 * if (b == null) return null; char bb[] = new char[b.length];
	 * 
	 * for (int i = 0; i < b.length; i++) { if (b[i] < 0) code = b[i] + 192; else
	 * code = b[i]; if (code >= 128 ) code = (code + 912); else code = b[i]; if
	 * (code == 185) code = 'N'; //№ bb[i] = (char)code; } return bb; }
	 */
	
}

package clframe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * A zip processor that decrypts the zip entries using SimpleOffsetEncoderDecoder!!!
 * @author lubo
 *
 */
class DecryptGEEZipProcessor extends GEERawZipProcessor  {
	
	final String ALPHABET = "_-+[]{}1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";    //alphabet for encrypting  the source files names
	private SimpleOffsetEncoderDecoder encdec;
	private GEERawZipProcessor geeZipProcessor;
	
	DecryptGEEZipProcessor(GEERawZipProcessor geeZipProcessor, String pass, int bufferSize){
		super(bufferSize);
		this.geeZipProcessor = geeZipProcessor;
		if(pass!=null && !pass.equals("")) encdec =  new SimpleOffsetEncoderDecoder(ALPHABET, pass); 
		this.outData  = geeZipProcessor.outData;
	}
	
	DecryptGEEZipProcessor(GEERawZipProcessor geeZipProcessor){
		super(1024*1024);
		this.geeZipProcessor = geeZipProcessor;
		this.outData  = geeZipProcessor.outData;
	}
	

	@Override
	public void process(ZipEntry entry, ZipInputStream zis) {
		processs(entry, zis);
		/*String entryName = entry.getName();
		RawData raw = geeZipProcessor.outData.getRawData().get(entryName);
		if(raw == null) return;
		byte [] rawBytesDecoded = decrypt(raw.bytes);
		String decodedEntryName = decrypt(raw.getName().getFullName());
		String dottedEntryName = decodedEntryName.replace("/", ".");
		
		if(decodedEntryName.endsWith(ClFrameConst.CLASS_EXTENSION)){
			//put into the class loader hashMap
			geeZipProcessor.outData.getClassMap().put(dottedEntryName, new ClassInfo(rawBytesDecoded,FileNamePath.fromFileNamePath(decodedEntryName),FileNamePath.fromFileNamePath(entryName)));
		}else{
			//load resource file
			if(decodedEntryName.equals(ClFrameConst.ENGINE_PROP_FILE_NAME)){
            	//read properties
            	ByteArrayInputStream ins = new ByteArrayInputStream(rawBytesDecoded);
            	geeZipProcessor.outData.setProperties(Utils.loadproperties(ins));
            }
			geeZipProcessor.outData.getResources().put(dottedEntryName, new ResourceInfo(rawBytesDecoded,FileNamePath.fromFileNamePath(decodedEntryName), FileNamePath.fromFileNamePath(entryName)));
		}*/
	}
	
	private void processs(ZipEntry entry, ZipInputStream zis){
		boolean isFile = !entry.isDirectory();
		byte [] buffer =  new byte[bufferSize];
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			//load row data
			if(isFile){
				int len;
	            while ((len = zis.read(buffer)) > 0) {
	            	os.write(buffer, 0, len);
	            }
	            GEEngineUtils.log("Processing zip entry " + entry.getName());
	            String decodedEntryName = decrypt(entry.getName());
	            if(decodedEntryName.endsWith(".jar")){
	            	ByteArrayInputStream ins = new ByteArrayInputStream(os.toByteArray());
	            	ZipUtils.zipProcess((ins), this);
	            }else{
	            	outData.getRawData().put(entry.getName(), new RawData(os.toByteArray(), FileNamePath.fromFileNamePath(entry.getName())));
	            }
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}
	
	
	/** 
	 *Gets the entries in outData.rowData decrypts if necessary & fills classes & resources
	 */
	void decryptRawDataAndFillClassesResources(){
		for(String entryName:geeZipProcessor.outData.getRawData().keySet()){
			RawData raw = geeZipProcessor.outData.getRawData().get(entryName);
			byte [] rawBytesDecoded = decrypt(raw.bytes);
			String decodedEntryName = decrypt(raw.getName().getFullName());
			String dottedEntryName = decodedEntryName.replace("/", ".");
			
			if(decodedEntryName.endsWith(ClFrameConst.CLASS_EXTENSION)){
				//put into the class loader hashMap
				GEEngineUtils.log("Adding to class data  " + decodedEntryName);
				geeZipProcessor.outData.getClassMap().put(dottedEntryName, new ClassInfo(rawBytesDecoded,FileNamePath.fromFileNamePath(decodedEntryName),FileNamePath.fromFileNamePath(entryName)));
			}else{
				//load resource file
				if(decodedEntryName.equals(ClFrameConst.ENGINE_PROP_FILE_NAME)){
	            	//read properties
	            	ByteArrayInputStream ins = new ByteArrayInputStream(rawBytesDecoded);
	            	geeZipProcessor.outData.setProperties(Utils.loadproperties(ins));
	            }
				GEEngineUtils.log("Adding to resource data  " + decodedEntryName);
				geeZipProcessor.outData.getResources().put(dottedEntryName, new ResourceInfo(rawBytesDecoded,FileNamePath.fromFileNamePath(decodedEntryName), FileNamePath.fromFileNamePath(entryName)));
			}
		}
	}
	
	
	private String decrypt(String name){
		return encdec == null ? name : encodeDecodePath(name, "", encdec, false);
	}
	
	private byte[] decrypt(byte [] b){
		return encdec == null ? b: encdec.decode(b);
	}

	
	private  String encodeDecodePath(String path, String root, SimpleOffsetEncoderDecoder enc, boolean encode){
		String pathnoRoot = path.replace(root, "");   //remove root
		
		//split to folders 
		String [] folders = pathnoRoot.replace(File.separator, "/").split("/");
		for(int i = 0; i < folders.length; i++){   //encode folder names
			folders[i]= encode ?  enc.encode(folders[i]) : enc.decode(folders[i]);
		}
		
		//construct back folder path
		String res = (root == null || root.equals("")) ? "" : root + "/";
		int i = 0;
		for(String s : folders) {
			res+= (i > 0) ? "/"+ s :  s;
			i++;
		}
		return res;
	}
}

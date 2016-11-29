package clframe;

import java.io.ByteArrayInputStream;
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
		geeZipProcessor.process(entry, zis);
		String entryName = entry.getName();
		RawData raw = geeZipProcessor.outData.getRowData().get(entryName);
		if(raw == null) return;
		String decodedEntryName = decrypt(raw.name);
		String dottedEntryName = decodedEntryName.replace("/", ".");
		if(decodedEntryName.endsWith(".class")){
			//put into the class loader hashMap
			geeZipProcessor.outData.getClassMap().put(dottedEntryName, new ClassInfo(decrypt(raw.bytes), dottedEntryName));
		}else{
			//load resource file
			if(decodedEntryName.equals("engine.properties")){
            	//read properties
            	ByteArrayInputStream ins = new ByteArrayInputStream(decrypt(raw.bytes));
            	geeZipProcessor.outData.setProperties( Utils.loadproperties(ins));
            }
			geeZipProcessor.outData.getResources().put(dottedEntryName, new ResourceInfo(decrypt(raw.bytes), decodedEntryName, entryName));
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

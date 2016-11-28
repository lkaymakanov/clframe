package clframe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import clframe.ZipUtils.IZipEntryProcessor;

class DecryptGEEZipProcessor implements IZipEntryProcessor  {
	
	final String ALPHABET = "_-+[]{}1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";    //alphabet for encrypting  the source files names
	private SimpleOffsetEncoderDecoder encdec;
	private IDecrypt dec;
	private int bufferSize;
	GEEngineData outData = new GEEngineData();
	
	DecryptGEEZipProcessor(String pass, int bufferSize){
		this.bufferSize = bufferSize;
		encdec = new SimpleOffsetEncoderDecoder(ALPHABET, pass); 
		dec = new IDecrypt() {
			@Override
			public byte[] decode(byte[] bytes, int len) {
				// TODO Auto-generated method stub
				return encdec.decode(bytes, len);
			}
			
			@Override
			public String decode(String s) {
				// TODO Auto-generated method stub
				return encdec.decode(s);
			}
		};
			
		
	}

	@Override
	public void process(ZipEntry entry, ZipInputStream zis) {
		// TODO Auto-generated method stub
		boolean isFile = !entry.isDirectory();
		byte [] buffer =  new byte[bufferSize];
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		try {
			if(isFile && dec.decode(entry.getName()).endsWith(".class")) {
					String cName = entry.getName().replace("/", ".");
					//System.out.println(cName);
					
					//put into the class loader hashMap
					int len;
		            while ((len = zis.read(buffer)) > 0) {
		            	os.write(encdec.decode(buffer,len), 0, len);
		            }
		            outData.classMap.put(cName, new ClassInfo(os.toByteArray()));
				
			}else if(isFile){
				//load resource file
				String name = dec.decode(entry.getName()).replace("/", ".");
				int len;
	            while ((len = zis.read(buffer)) > 0) {
	            	os.write(encdec.decode(buffer,len), 0, len);
	            }
	            ResourceInformation info = new ResourceInformation(os.toByteArray(), name);
	            if(name.equals("engine.properties")){
	            	//read properties
	            	ByteArrayInputStream ins = new ByteArrayInputStream(info.bytes);
	            	outData.properties = Utils.loadproperties(ins);
	            }
	            outData.resources.put(name, info);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	
	}

	
	static interface IDecrypt{
		String decode(String s);
		byte []  decode(byte [] bytes,  int len);
	}
}

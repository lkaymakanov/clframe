package clframe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import clframe.ZipUtils.IZipEntryProcessor;

/***
 * A Zip processor callback that launches a jar file containing engine!!!
 * @author lubo
 *
 */
class GEEZipProcessor implements IZipEntryProcessor {
	GEEngineData outData = new GEEngineData();
	
	GEEZipProcessor(){
	}

	@Override
	public void process(ZipEntry entry, ZipInputStream zis) {
		// TODO Auto-generated method stub
		boolean isFile = !entry.isDirectory();
		byte [] buffer =  new byte[1024*1024];
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		try {
			if(isFile && entry.getName().endsWith(".class")) {
					String cName = entry.getName().replace("/", ".");
					//System.out.println(cName);
					
					//put into the class loader hashMap
					int len;
		            while ((len = zis.read(buffer)) > 0) {
		            	os.write(buffer, 0, len);
		            }
		            outData.classMap.put(cName, new ClassInfo(os.toByteArray()));
				
			}else if(isFile){
				//load resource file
				String name = entry.getName().replace("/", ".");
				int len;
	            while ((len = zis.read(buffer)) > 0) {
	            	os.write(buffer, 0, len);
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

}

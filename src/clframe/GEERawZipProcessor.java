package clframe;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import clframe.ZipUtils.IZipEntryProcessor;

/***
 * A Zip processor callback that launches a jar file containing engine!!!
 * @author lubo
 *
 */
class GEERawZipProcessor implements IZipEntryProcessor {
	GEEngineData outData = new GEEngineData();
	protected int bufferSize = 1024*1024;
	
	GEERawZipProcessor(int bufferSize){
		this.bufferSize = bufferSize;
	}

	@Override
	public void process(ZipEntry entry, ZipInputStream zis) {
		/*boolean isFile = !entry.isDirectory();
		byte [] buffer =  new byte[bufferSize];
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			//load row data
			if(isFile){
				int len;
	            while ((len = zis.read(buffer)) > 0) {
	            	os.write(buffer, 0, len);
	            }
	            
	            if(entry.getName().endsWith(".jar")){
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
		}*/
	}
}


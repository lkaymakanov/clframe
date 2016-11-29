package clframe;

import java.io.ByteArrayOutputStream;
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
	private int bufferSize = 1024*1024;
	
	GEERawZipProcessor(int bufferSize){
		this.bufferSize = bufferSize;
	}

	@Override
	public void process(ZipEntry entry, ZipInputStream zis) {
		// TODO Auto-generated method stub
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
	            outData.getRowData().put(entry.getName(), new RawData(os.toByteArray(), entry.getName()));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}
}


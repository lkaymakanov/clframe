package clframe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

class GEEngineFileExporter implements IGEEngineExporter {
	private IGEEngineData data;
	private String parentFolder;
	private static String pathSeparator = "\\";  //File.pathSeparator
	
	
	GEEngineFileExporter(String parentFolder, IGEEngineData data){
		this.parentFolder = parentFolder;
		this.data = data;
	}
	
	GEEngineFileExporter(File parentFolder, IGEEngineData data){
		this(parentFolder == null ? "" : parentFolder.getAbsolutePath(),  data);
	}
	
	
	@Override
	public void exportClasses() {
		try {
			GEEngineFileExporter.exportClasses(new File(parentFolder == null ? "" : parentFolder), data);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void exportResources() {
		try {
			GEEngineFileExporter.exportResources(new File(parentFolder == null ? "" : parentFolder), data);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void exportRawData() {
		try {
			GEEngineFileExporter.exportRawData(new File(parentFolder == null ? "" : parentFolder), data);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void exportEngineData() {
		exportClasses();
		exportResources();
	}
	
	/**
	 * Saves data to a file!!!
	 * @param parentFolder
	 * @param filePathName
	 * @param fContent
	 * @throws IOException 
	 */
	private static void saveDataToFile(File parentFolder, FileNamePath filePathName, byte [] fContent) throws IOException{
		GEEngineUtils.log("Exporting file " + filePathName.getFileName());
		FileNamePath.mkDir(parentFolder == null ? "" : parentFolder.getAbsolutePath(), filePathName.getPath(), pathSeparator);
		createFile(parentFolder == null ? null : parentFolder.getAbsolutePath(), filePathName, fContent);
	}
	
	/***
	 * Creates a file and writes the content of the byte array in it!
	 * @param parentFolder
	 * @param fileNamePath
	 * @param fcontent
	 * @throws IOException
	 */
	private static void createFile(String parentFolder, FileNamePath fileNamePath, byte [] fcontent) throws IOException {
		File f = new File(parentFolder == null || parentFolder.isEmpty() ?  fileNamePath.getFullName() : parentFolder + pathSeparator+ fileNamePath.getFullName() );
		FileOutputStream fos = new FileOutputStream(f);
		try {
			fos.write(fcontent, 0, fcontent.length);
		}finally {
			fos.flush();
			fos.close();
		}
	}
	
	
	/**
	 * Exports Engine Raw Data to a Files!!!
	 * @param file
	 * @param data
	 * @throws IOException 
	 */
	private static void exportRawData(File outDir, IGEEngineData data) throws IOException{
		for(String k :data.getRawData().keySet()){
			RawData r = data.getRawData().get(k); 
			saveDataToFile(outDir,  r.getName(), r.bytes);
		}
	}
	
	/**
	 * Exports Engine Classes to a Files!!!
	 * @param file
	 * @param data
	 * @throws IOException 
	 */
	private static void exportClasses(File outDir, IGEEngineData data) throws IOException{
		for(String k :data.getClassMap().keySet()){
			ClassInfo ci = data.getClassMap().get(k); 
			saveDataToFile(outDir,  ci.getName(), ci.bytes);
		}
	}
	
	/***
	 * Exports Engine Resources to a Files!!!
	 * @param file
	 * @param data
	 * @throws IOException 
	 */
	private static void exportResources(File outDir, IGEEngineData data) throws IOException{
		for(String k :data.getResources().keySet()){
			ResourceInfo ri = data.getResources().get(k); 
			saveDataToFile(outDir,  ri.getResourceName(), ri.bytes);
		}
	}
    
	/**
	 * Saves data to a file!!!
	 * @param parentFolder
	 * @param filePathName
	 * @param fContent
	 * @throws IOException 
	 */
	/*private static void saveDataToFile(String parentFolder, FileNamePath filePathName, byte [] fContent) throws IOException{
		GEEngineUtils.log("Exporting file " + filePathName.getFullName());
		FileNamePath.mkDir(parentFolder, filePathName.getPath());
		createFile(parentFolder, filePathName, fContent);
	}*/
	

}

package clframe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


/**
 * A generic execution engine that will load classes and functionality at runtime!!!!
 * @author lubo
 *
 */
public class GEEngineUtils {
	
	private static Map<String, IGEEngineData> engines = new HashMap<String, IGEEngineData>();
	
    /**
     * Returns Engine by Engine name!!!
     * @return
     */
    public static IGEEngine getEngine(String engineName){
    	synchronized (engines) {
			return  engines.get(engineName).getEnigine();
    	}
    }
    
    /***
     * Gets an engine name from engine properties!!!
     * @param p
     * @return
     */
    private static String getEngineName(Properties p){
    	return p.getProperty("name");
    }
    
    /***
     * Creates an engine from engine File!!! 
     * If engine exists under that name the existing engine is returned!!! Otherwise registers & returns new IGEEngine!!!
     * @param f
     * @return
     * @throws FileNotFoundException 
     */
    public static IGEEngine createEngine(File f) throws FileNotFoundException{
    	return createEngine(f, null);
    }
    
    /**
     * Creates an engine from engine File and a decryption pass!!! 
     * @param f
     * @param pass
     * @return
     * @throws FileNotFoundException 
     */
    public static IGEEngine createEngine(File f,  String pass) throws FileNotFoundException{
    	IGEEngineData data = loadEngineData(f, 1024*1024, pass);
    	IGEEngineData e = null;
    	synchronized (engines) {
			e = engines.get(getEngineName(data.getProperties()));
			if(e!=null) return e.getEnigine();
			data.setEnigine(createEngine(data));
			engines.put(getEngineName(data.getProperties()), (data));
		}
    	return data.getEnigine();
    }
    
    /***
     * Creates an engine from engine content passed as byte array!!!
     *  If engine exists under that name the existing engine is returned!!! Otherwise registers & returns new IGEEngine!!!
     * @param engine
     * @return
     */
    public static IGEEngine createEngine(byte[] engine){
    	return createEngine(engine, null);
    }
    
    /**
     * Creates an engine from engine content passed as byte array and a decryption pass!!!
     * @param engine
     * @param pass
     * @return
     */
    public static IGEEngine createEngine(byte[] engine, String pass){
    	IGEEngineData data = loadEngineData(engine, 1024*1024, pass);
    	IGEEngineData e=null;
    	synchronized (engines) {
			e = engines.get(getEngineName(data.getProperties()));
			if(e!=null) return e.getEnigine();
			data.setEnigine(createEngine(data));
			engines.put(getEngineName(data.getProperties()), data);
		}
    	return data.getEnigine();
    }
    
    
    /***
     * *Unregisters an engine and calls the engine unload method!!!
     * @param engineName
     */
    public static void destroyEngine(String  engineName){
    	synchronized (engines) {
    		IGEEngine engine = null;
    		try{
    			engine = getEngine(engineName);
    			if(engine!=null) engine.unload();
    		}finally{
    			engines.remove(engineName);
    		}
    	}
    }
    
    /***
     * Creates an engine from engine data!!!
     * @param data
     * @return
     */
    private static IGEEngine createEngine(IGEEngineData data){
    	IGEEngine engine=null;
    	try {
			engine = (IGEEngine)data.getEngineClassLoader().loadClass(getEngineName(data.getProperties())).newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
    	return engine;
    }
    
    /***
     * Loads an engine class by classname!!!
     * @param engineName
     * @param className
     * @return
     */
    @SuppressWarnings("rawtypes")
	private static Class loadClass(String engineName, String className){
    	IGEEngineData data = engines.get(engineName);
        return loadClass(data, className);
    }
    
    /**
     * Loads a class by IGEEngineData & className!!!
     * @param data
     * @param className
     * @return
     */
    @SuppressWarnings("rawtypes")
	private static Class loadClass(IGEEngineData data, String className){
    	 if(data == null) return null;
     	try {
 			return data.getEngineClassLoader().loadClass(className);
 		} catch (ClassNotFoundException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 			throw new RuntimeException(e);
 		}
    }
    
    /***
     * Creates engine by the the class  name of the class located in the source files in the application itself!!!
     * @return
     */
    public static IGEEngine createEngineByClassName(String className){
    	IGEEngineData data = loadEngineData(className);
    	IGEEngineData e=null;
    	synchronized (engines) {
			e = engines.get(getEngineName(data.getProperties()));
			if(e!=null) return e.getEnigine();
			data.setEnigine(createEngine(data));
			engines.put(getEngineName(data.getProperties()), data);
		}
    	return data.getEnigine();
    }
    
    /**
     * Creates engine by the the class  located in the source files in the application itself!!!
     * @param c
     * @return
     */
    public static IGEEngine createEngineByClass(Class c){
    	return createEngineByClassName(c.getName());
    }
    
    
    /***
     * Load engine data for className located in the source file of the application!!!
     * @param className
     * @return
     */
    private static IGEEngineData loadEngineData(String className){
    	IGEEngineData data = new GEEngineData();
    	data.setEngineClassLoader( new GEEngineCl(data, null));
    	data.setProperties( new Properties());
    	data.getProperties().put(ClFrameConst.NAME, className);
		data.getResources().put(ClFrameConst.ENGINE_PROP_FILE_NAME, new ResourceInfo("key=d55720e30d36024dbfa38b86c9d14077ecdf66699ee0444432249bf98844955df35f15985e6f290f6cd10b3f47382c0fec241c1dfba5692f3adb6b28a0a6852c".getBytes(),FileNamePath.fromFileNamePath("engine.properties"), FileNamePath.fromFileNamePath("originalResourceName")));
     	return data;
    }
    
  /***
   * Loads from byte array into memory the data needed by the engine!!!
   * @param engine
   * @param pass
   * @return
   */
    private static IGEEngineData loadEngineData(byte[] engine, int bufferSize, String pass){
    	return loadEngineData(new ByteArrayInputStream(engine), bufferSize, pass);
    }
    
    
    /**
     * Loads from input stream into memory the data needed by the engine!!!
     * @param is
     * @param bufferSize
     * @param pass
     * @return
     */
    private static IGEEngineData loadEngineData(InputStream is, int bufferSize, String pass){
    	DecryptGEEZipProcessor pr = new DecryptGEEZipProcessor(new GEERawZipProcessor(bufferSize), pass, bufferSize);   
    	ZipUtils.zipProcess(is, pr);
    	pr.decryptRawDataAndFillClassesResources();
    	pr.outData.setEngineClassLoader(new GEEngineCl(pr.outData, null));
    	return pr.outData;
    }
    

    /**
     * Loads from engine file into memory the data needed by the engine !!!
     * @param f
     * @param pass
     * @return
     * @throws FileNotFoundException 
     */
    private static IGEEngineData loadEngineData(File f, int bufferSize, String pass) throws FileNotFoundException{
    	return loadEngineData(new FileInputStream(f), bufferSize, pass);
    }
    
    
    
    /***
     * Returns the names of registered engines!!!
     * @return
     */
    public static Set<String> getEngineNames(){
    	return engines.keySet();
    }
    
    /***
     * Returns the names of the classes needed by this engine!!!
     * @param engineName
     * @return
     */
    public static Set<String> getEngineClassNames(String engineName){
       IGEEngineData data = 	engines.get(engineName);
       if(data == null) return null;
       return data.getClassMap().keySet();
    }
    
    
    /***
     * Returns the names of the resources needed by this engine!!!
     * @param engineName
     * @return
     */
    public static Set<String> getEngineResourceNames(String engineName){
       IGEEngineData data = 	engines.get(engineName);
       if(data == null) return null;
       return data.getResources().keySet();
    }
    
    
    /***
     * Returns a resource by engine name & resource name!!!
     * @param engineName
     * @param resourceName
     * @return
     */
    public static byte [] getEngineResource(String engineName, String resourceName){
        IGEEngineData data = engines.get(engineName);
        if(data == null) return null;
        return data.getResources().get(resourceName).bytes;
    }
    
	/***
     * Add a resource to engine resources!!!
     */
    public static void addResourceToEngine(String engineName, String resourceName, byte [] resourceBytes){
    	addResourceToEngine(engineName, resourceName, resourceBytes, "originalName");
    }
    
    
    /***
     * Add a resource to engine resources!!!
     */
    public static void addResourceToEngine(String engineName, String resourceName, byte [] resourceBytes, String originalResourceName){
    	  IGEEngineData data = 	engines.get(engineName);
          if(data == null) return ;
          data.getResources().put(resourceName, new ResourceInfo(resourceBytes, FileNamePath.fromFileNamePath(resourceName), FileNamePath.fromFileNamePath(originalResourceName)));
    }
    
    
    /***
     * Returns class bytes for by engineName & class name!!!
     * @param engineName
     * @param className
     * @return
     */
    public static byte [] getEngineClass(String engineName, String className){
        IGEEngineData data = engines.get(engineName);
        if(data == null) return null;
        return data.getClassMap().get(className).bytes;
    }
    
    
    
    /**
     * Get the names of the properties names of the engine!!!
     * @param engineName
     * @return
     */
    public static Enumeration<Object>  getEnginePropertyNames(String engineName){
    	IGEEngineData data = engines.get(engineName);
        if(data == null) return null;
        return data.getProperties().keys();
    }
    
    /***
     * Get a property of the engine properties by property name!!!
     * @param engineName
     * @param propertyKey
     * @return
     */
    public static Object  getEngineProperty(String engineName, String propertyKey){
    	IGEEngineData data = engines.get(engineName);
        if(data == null) return null;
        return data.getProperties().get(propertyKey);
    }
    
    
    /**
	 * Get an instance of Object of Class<T> type invoking a constructor
	 * with argument types specified by argtypes and argument References
	 * args!
	 * 
	 * @param type
	 *            Class object!
	 * @param argtypes
	 *            Class objects of arguments!
	 * @param args
	 *            References to arguments!
	 * @return Instance of T!
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object getInstance(Class type,
					Class<?>[] argtypes,
					Object[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException  {
	    if (argtypes == null || args == null)
		return type.newInstance();
	    return type.getConstructor(argtypes).newInstance(args);
	}
	
	/**
	 * Prints the names of the resources loaded in the GEEngineData!!!
	 * @param data
	 */
	private static StringBuilder printResourcesNames(IGEEngineData data){
		StringBuilder sb = new StringBuilder();
		for(String k :data.getResources().keySet()){
			ResourceInfo cinfo = data.getResources().get(k);
			sb.append("ResourceName : " + k + ", Original ResourceName: " + cinfo.getOriginalName().getFullName());
			sb.append("\n");
		}
		return sb;
	}
	
	/**
	 * Prints the names of the classes loaded in the GEEngineData!!!
	 * @param data
	 */
	private static StringBuilder printClassesNames(IGEEngineData data){
		StringBuilder sb = new StringBuilder();
		for(String k :data.getClassMap().keySet()){
			ClassInfo cinfo = data.getClassMap().get(k);
			sb.append("Class : " + k + ", Original Class: " + cinfo.getOriginalName().getFullName());
			sb.append("\n");
		}
		return sb;
	}
	
	
	private static StringBuilder printRawDataNames(IGEEngineData data){
		StringBuilder sb = new StringBuilder();
		for(String k :data.getRawData().keySet()){
			sb.append("RawDataName : " + k );
			sb.append("\n");
		}
		return sb;
	}
	
	/**
	 * Exports Engine Raw Data to a Files!!!
	 * @param file
	 * @param data
	 */
	private static void exportRawData(File outDir, IGEEngineData data){
		for(String k :data.getRawData().keySet()){
			RawData r = data.getRawData().get(k); 
			saveDataToFile(outDir,  r.getName(), r.bytes);
		}
	}
	
	/**
	 * Exports Engine Classes to a Files!!!
	 * @param file
	 * @param data
	 */
	private static void exportClasses(File outDir, IGEEngineData data){
		for(String k :data.getClassMap().keySet()){
			ClassInfo ci = data.getClassMap().get(k); 
			saveDataToFile(outDir,  ci.getName(), ci.bytes);
		}
	}
	
	/***
	 * Exports Engine Resources to a Files!!!
	 * @param file
	 * @param data
	 */
	private static void exportResources(File outDir, IGEEngineData data){
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
	 */
	private static void saveDataToFile(String parentFolder, FileNamePath filePathName, byte [] fContent){
		
	}
	
	
	/**
	 * Saves data to a file!!!
	 * @param parentFolder
	 * @param filePathName
	 * @param fContent
	 */
	private static void saveDataToFile(File parentFolder, FileNamePath filePathName, byte [] fContent){
		
	}
	
	/***
	 * Serializes engine data!!!
	 * @param data
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private static byte[] serializeData(Object data) throws IOException{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ObjectOutputStream oaout = new ObjectOutputStream(os);
		oaout.writeObject(data);
		oaout.flush();
		oaout.close();
		return os.toByteArray();
	}
    
    /***
     * Creates an object by calling constructor with types argtypes, and arguments args!!!
     * @param engine
     * @param className
     * @return
     */
    @SuppressWarnings("unchecked")
	private static Object createObjectByClassName(String engineName, String className, Class<?>[] argtypes,
			Object[] args){
    	try {
    		@SuppressWarnings("rawtypes")
			Class c = loadClass(engineName, className);
    		if (argtypes == null || args == null)
    			return c.newInstance();
    		    return c.getConstructor(argtypes).newInstance(args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
    
    /***
     * Creates an object by calling constructor non-argument constructor!!!
     */
    private static Object createObjectByClassName(String engineName, String className){
    	return createObjectByClassName(engineName, className, null, null);
    }
    
    /***
     * Returns row data map for list of  classes containing its name as header beginning!!! The first 2 bytes show header name length!!!
     * @param l
     * @return
     * @throws UnsupportedEncodingException 
     */
    private static IMapRawData getRawDataForClassBytes(List<byte []> l) throws UnsupportedEncodingException{
    	MapRawData  map = new MapRawData();
    	
    	for(byte [] a:l){
    		RawData rd = getRawInfo(a);
    		map.getRawData().put(rd.getName().getFullName(), rd);
    	}
    	return map;
    }
    
    /***
     * Creates IGEEngineData from raw data!!!
     * @param rawData
     * @return
     */
    private static IGEEngineData fromRawData(IMapRawData rawData, ClassLoader parent){
    	GEEngineData data = new GEEngineData();
    	data.setEngineClassLoader(new GEEngineCl(data, parent));
    	data.setRowData(rawData.getRawData());
    	for(RawData rd: rawData.getRawData().values()){
    		FileNamePath fname = rd.getName();
    		if(fname.getFileName().endsWith(ClFrameConst.CLASS_EXTENSION))	data.getClassMap().put(rd.getName().getFullName(), new ClassInfo(rd.bytes, fname, fname));
    		else data.getResources().put(rd.getName().getFullName(), new ResourceInfo(rd.bytes, fname, fname));
    	}
    	return data;
    }
    
    /***
     * Returns row data for class containing its name as header beginning!!! The first 2 bytes show header name length!!!
     * @param b
     * @return
     * @throws UnsupportedEncodingException 
     */
    private static RawData getRawInfo(byte [] b) throws UnsupportedEncodingException{
    	 int offset = 2;
	   	 int length = (((b[0] << 8 ) | b[1])) & 0x0000ffff;
	   	 byte [] nb;
	   	 nb = new byte[length];
	   	 for(int i = 0; i < length; i++){
	   		 nb[i] = b[i+offset]; 
	   	 }
	   	 byte [] clb = Arrays.copyOfRange(b, length+offset, b.length);
	   	 String name = new String(nb, "UTF-8");
	     RawData d = new RawData(clb, new FileNamePath(null, name, false));
	   	 return d;
    }
    
    /**
     * Get class loader for raw data!!!
     * @param data
     * @param parent
     * @return
     * @throws UnsupportedEncodingException 
     */
    public static ClassLoader getClassLoader(List<byte []> data, ClassLoader parent) throws UnsupportedEncodingException{
    	IMapRawData  m = getRawDataForClassBytes(data);
    	IGEEngineData edata = fromRawData(m, parent);
    	return edata.getEngineClassLoader();
    }
    
    
    
    private static void printByteArrayMatrix(byte [] b,  int bytesPerRow, String className) throws UnsupportedEncodingException{
	   	 if(bytesPerRow > b.length) bytesPerRow = b.length;
	   	 String byteArrayPrexif = " byte []  " + className + " = new byte[]{ \n" ;
	   	 StringBuilder bd = new StringBuilder();
	   	 bd.append(byteArrayPrexif);
	   	 //add class name bytes
	   	 byte [] classnameBytes = className.getBytes("UTF-8");
	   	 int l = classnameBytes.length;
	   	 bd.append((classnameBytes.length &  0x0000ff00 )+ ", ");  //high byte 
	   	 bd.append((classnameBytes.length &  0x000000ff )+ ", ");  //low byte
	   	 getByteArrayAsMatrix(bd, classnameBytes, bytesPerRow);
	   	 getByteArrayAsMatrix(bd, b, bytesPerRow).toString();
	   	 bd.append("};");
	   	 System.out.println(bd);
    }
    
    /**
     * Creates a comma separated rectangular matrix of byte array into the StringBuilder sb!!!
     * @param sb
     * @param b
     * @param bytesPerRow
     * @return
     */
    private static StringBuilder getByteArrayAsMatrix(StringBuilder sb, byte [] b, int bytesPerRow){
	   	 int row = 0;
	   	 int i = 0;
	   	 StringBuilder bd = sb;
	   	 for(; row * bytesPerRow < b.length; row++){
	   		 i = row * bytesPerRow;
	   		 int jEnd = i + bytesPerRow;
	   		 for(; i < jEnd && i < b.length; i++){
	   			 bd.append(b[i] + ", ");      //collect elements in each row
	   		 }
	   		 bd.append("\n");   //add new line
	   	 }
	   	 return bd;
    }
    
    
    
    public static void main(String [] args) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException{
    	//IGEEngineData data = loadEngineData(new File("C:\\Users\\my\\Desktop\\eng.jar"), 1024*1024, null);
    	/*System.out.println(printClassesNames(data).toString());
    	System.out.println(printResourcesNames(data).toString());
    	System.out.println(printRawDataNames(data).toString());
    	byte [] b = serializeData(data.getRowData());
    	System.out.println(b.length + "  End...");*/
    	
    	List<byte []> datad= new ArrayList<byte[]>();
    	datad.add(ByteArrays.en);
    	datad.add(ByteArrays.enf);
    	datad.add(ByteArrays.def);
    	ClassLoader ld = getClassLoader(datad, null); 
    	
    	
    	IGEEngineData data = loadEngineData(new File("C:\\Users\\lubo\\Desktop\\eneng.eng"), 1024*1024, "geengine");
    	System.out.println(printResourcesNames(data));
    	System.out.println(printClassesNames(data));
    	
    	
    	//test loading jar files
    	//IGEEngineData denc = loadEngineData(new File("C:\\Users\\lubo\\Desktop\\gee\\deng.eng"), 1024*1024, "geengine");
    	//IGEEngineData noen = loadEngineData(new File("C:\\Users\\lubo\\Desktop\\gee\\eng.eng"), 1024*1024, null);
    	/*
    	System.out.println(printClassesNames(denc));
    	System.out.println(printRawDataNames(denc));
    	System.out.println(printResourcesNames(denc));
    	
    	System.out.println(printClassesNames(noen));
    	System.out.println(printRawDataNames(noen));
    	System.out.println(printResourcesNames(noen));
    	*/
    	/*IEncoderFactory ef = (IEncoderFactory)getInstance(ld.loadClass("token.SimpleOffsetEncoderFactory"), new Class[]{String.class}, new Object[]{"mypass"});
    	IDecoderFactory def = (IDecoderFactory)getInstance(ld.loadClass("token.SimpleOffsetDecoderFactory"), new Class[]{String.class}, new Object[]{"mypass"});
    	IToken token = new SharedToken();
    	byte [] tenc = TokenUtils.encryptToken(token, ef);
    	byte [] dec = TokenUtils.decryptToken(tenc, def);
    	Object t = TokenUtils.deserialize(dec, dec.length);
    	System.out.println(t);*/
    	//System.out.println(ci.getName().getFullName());
    }
    
   
    
    
    
    static class ByteArrays {

   	 public final static byte []  en = new byte[]{ 
   		 0, 38, 116, 111, 107, 101, 110, 46, 83, 105, 109, 112, 108, 101, 79, 102, 102, 115, 101, 116, 69, 110, 99, 111, 100, 101, 114, 68, 101, 99, 111, 100, 101, 114, 46, 99, 108, 97, 115, 115, 
   		 -54, -2, -70, -66, 0, 0, 0, 51, 0, -108, 7, 0, 2, 1, 0, 32, 116, 111, 107, 101, 110, 47, 83, 105, 109, 112, 108, 101, 79, 102, 102, 115, 101, 116, 69, 110, 99, 111, 100, 101, 114, 68, 101, 99, 111, 100, 101, 114, 7, 0, 4, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 7, 0, 6, 1, 0, 14, 116, 111, 107, 101, 110, 47, 73, 69, 110, 99, 111, 100, 101, 114, 7, 0, 8, 1, 0, 14, 116, 111, 107, 101, 
   		 110, 47, 73, 68, 101, 99, 111, 100, 101, 114, 1, 0, 15, 100, 101, 102, 97, 117, 108, 116, 65, 108, 112, 104, 97, 98, 101, 116, 1, 0, 18, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 1, 0, 7, 111, 102, 102, 115, 101, 116, 115, 1, 0, 2, 91, 73, 1, 0, 8, 97, 108, 112, 104, 97, 98, 101, 116, 1, 0, 6, 108, 101, 110, 103, 116, 104, 1, 0, 1, 73, 1, 0, 13, 115, 99, 114, 97, 98, 108, 101, 70, 97, 
   		 99, 116, 111, 114, 1, 0, 8, 60, 99, 108, 105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 8, 0, 21, 1, 0, 90, 32, 46, 47, 92, 126, 33, 64, 35, 36, 37, 94, 38, 42, 40, 41, 95, 43, 123, 125, 91, 93, 59, 58, 124, 39, 44, 34, 95, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 
   		 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 9, 0, 1, 0, 23, 12, 0, 9, 0, 10, 1, 0, 15, 76, 105, 110, 101, 78, 117, 109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 18, 76, 111, 99, 97, 108, 86, 97, 114, 105, 97, 98, 108, 101, 84, 97, 98, 108, 101, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 39, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 
   		 105, 110, 103, 59, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 41, 86, 10, 0, 29, 0, 31, 7, 0, 30, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 12, 0, 14, 0, 32, 1, 0, 3, 40, 41, 73, 10, 0, 1, 0, 34, 12, 0, 26, 0, 35, 1, 0, 40, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 76, 106, 97, 118, 97, 47, 
   		 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 73, 41, 86, 1, 0, 4, 116, 104, 105, 115, 1, 0, 34, 76, 116, 111, 107, 101, 110, 47, 83, 105, 109, 112, 108, 101, 79, 102, 102, 115, 101, 116, 69, 110, 99, 111, 100, 101, 114, 68, 101, 99, 111, 100, 101, 114, 59, 1, 0, 10, 112, 97, 115, 115, 80, 104, 114, 97, 115, 101, 1, 0, 21, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 41, 86, 10, 0, 3, 0, 
   		 41, 12, 0, 26, 0, 18, 9, 0, 1, 0, 43, 12, 0, 16, 0, 15, 9, 0, 1, 0, 45, 12, 0, 13, 0, 10, 9, 0, 1, 0, 47, 12, 0, 14, 0, 15, 10, 0, 1, 0, 49, 12, 0, 50, 0, 51, 1, 0, 20, 112, 97, 115, 115, 80, 104, 114, 97, 115, 101, 84, 111, 73, 110, 116, 65, 114, 114, 97, 121, 1, 0, 40, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 76, 106, 97, 118, 97, 47, 108, 97, 110, 
   		 103, 47, 83, 116, 114, 105, 110, 103, 59, 41, 91, 73, 9, 0, 1, 0, 53, 12, 0, 11, 0, 12, 10, 0, 1, 0, 55, 12, 0, 56, 0, 57, 1, 0, 16, 115, 99, 114, 97, 109, 98, 108, 101, 65, 108, 112, 104, 97, 98, 101, 116, 1, 0, 20, 40, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 1, 0, 14, 115, 99, 114, 97, 109, 98, 108, 101, 70, 97, 99, 116, 111, 114, 7, 0, 60, 1, 0, 23, 106, 97, 118, 
   		 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 66, 117, 105, 108, 100, 101, 114, 10, 0, 59, 0, 41, 10, 0, 29, 0, 63, 12, 0, 64, 0, 65, 1, 0, 6, 99, 104, 97, 114, 65, 116, 1, 0, 4, 40, 73, 41, 67, 10, 0, 1, 0, 67, 12, 0, 68, 0, 69, 1, 0, 4, 115, 119, 97, 112, 1, 0, 7, 40, 91, 67, 73, 73, 41, 86, 10, 0, 59, 0, 71, 12, 0, 72, 0, 73, 1, 0, 6, 97, 112, 112, 101, 110, 100, 1, 0, 29, 
   		 40, 91, 67, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 66, 117, 105, 108, 100, 101, 114, 59, 10, 0, 59, 0, 75, 12, 0, 76, 0, 57, 1, 0, 8, 116, 111, 83, 116, 114, 105, 110, 103, 1, 0, 2, 98, 100, 1, 0, 25, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 66, 117, 105, 108, 100, 101, 114, 59, 1, 0, 15, 99, 117, 114, 114, 101, 110, 116, 65, 108, 112, 104, 97, 98, 101, 
   		 116, 1, 0, 1, 112, 1, 0, 13, 97, 108, 112, 104, 97, 98, 101, 116, 67, 104, 97, 114, 115, 1, 0, 2, 91, 67, 1, 0, 1, 105, 1, 0, 13, 111, 102, 102, 115, 101, 116, 115, 76, 101, 110, 103, 116, 104, 1, 0, 13, 83, 116, 97, 99, 107, 77, 97, 112, 84, 97, 98, 108, 101, 7, 0, 82, 1, 0, 5, 99, 104, 97, 114, 115, 1, 0, 6, 105, 110, 100, 101, 120, 49, 1, 0, 6, 105, 110, 100, 101, 120, 50, 1, 0, 2, 99, 49, 1, 0, 1, 67, 
   		 10, 0, 29, 0, 93, 12, 0, 94, 0, 95, 1, 0, 11, 99, 111, 100, 101, 80, 111, 105, 110, 116, 65, 116, 1, 0, 4, 40, 73, 41, 73, 1, 0, 3, 97, 114, 114, 1, 0, 10, 97, 112, 108, 104, 108, 101, 110, 103, 116, 104, 7, 0, 12, 1, 0, 6, 101, 110, 99, 111, 100, 101, 1, 0, 38, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 
   		 110, 103, 59, 10, 0, 29, 0, 102, 12, 0, 103, 0, 104, 1, 0, 7, 105, 115, 69, 109, 112, 116, 121, 1, 0, 3, 40, 41, 90, 10, 0, 1, 0, 106, 12, 0, 107, 0, 108, 1, 0, 24, 99, 104, 97, 114, 80, 111, 115, 105, 115, 105, 116, 105, 111, 110, 73, 110, 65, 108, 112, 104, 97, 98, 101, 116, 1, 0, 4, 40, 67, 41, 73, 10, 0, 59, 0, 110, 12, 0, 72, 0, 111, 1, 0, 28, 40, 67, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 
   		 83, 116, 114, 105, 110, 103, 66, 117, 105, 108, 100, 101, 114, 59, 10, 0, 1, 0, 113, 12, 0, 99, 0, 114, 1, 0, 5, 40, 67, 73, 41, 67, 1, 0, 1, 115, 1, 0, 2, 115, 98, 1, 0, 11, 99, 117, 114, 114, 101, 110, 116, 67, 104, 97, 114, 1, 0, 6, 100, 101, 99, 111, 100, 101, 10, 0, 1, 0, 120, 12, 0, 118, 0, 114, 10, 0, 1, 0, 122, 12, 0, 123, 0, 124, 1, 0, 14, 110, 101, 119, 67, 104, 97, 114, 80, 111, 115, 116, 105, 111, 
   		 110, 1, 0, 6, 40, 73, 73, 73, 41, 73, 1, 0, 1, 99, 1, 0, 15, 99, 104, 97, 114, 80, 111, 115, 73, 110, 83, 116, 114, 105, 110, 103, 10, 0, 29, 0, -128, 12, 0, -127, 0, 95, 1, 0, 7, 105, 110, 100, 101, 120, 79, 102, 10, 0, 1, 0, -125, 12, 0, 123, 0, -124, 1, 0, 5, 40, 73, 73, 41, 73, 1, 0, 18, 112, 111, 115, 105, 116, 105, 111, 110, 73, 110, 65, 108, 112, 104, 97, 98, 101, 116, 1, 0, 16, 112, 111, 115, 105, 116, 105, 
   		 111, 110, 73, 110, 83, 116, 114, 105, 110, 103, 1, 0, 4, 109, 117, 108, 116, 1, 0, 6, 111, 102, 102, 115, 101, 116, 1, 0, 6, 40, 91, 66, 41, 91, 66, 10, 0, 1, 0, -117, 12, 0, 99, 0, -116, 1, 0, 7, 40, 91, 66, 73, 41, 91, 66, 1, 0, 5, 98, 121, 116, 101, 115, 1, 0, 2, 91, 66, 1, 0, 3, 108, 101, 110, 10, 0, 1, 0, -111, 12, 0, 118, 0, -116, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 31, 
   		 83, 105, 109, 112, 108, 101, 79, 102, 102, 115, 101, 116, 69, 110, 99, 111, 100, 101, 114, 68, 101, 99, 111, 100, 101, 114, 46, 106, 97, 118, 97, 0, 33, 0, 1, 0, 3, 0, 2, 0, 5, 0, 7, 0, 5, 0, 10, 0, 9, 0, 10, 0, 0, 0, 2, 0, 11, 0, 12, 0, 0, 0, 2, 0, 13, 0, 10, 0, 0, 0, 2, 0, 14, 0, 15, 0, 0, 0, 2, 0, 16, 0, 15, 0, 0, 0, 18, 0, 8, 0, 17, 0, 18, 0, 1, 0, 19, 0, 0, 0, 
   		 38, 0, 1, 0, 0, 0, 0, 0, 6, 18, 20, -77, 0, 22, -79, 0, 0, 0, 2, 0, 24, 0, 0, 0, 6, 0, 1, 0, 0, 0, 10, 0, 25, 0, 0, 0, 2, 0, 0, 0, 1, 0, 26, 0, 27, 0, 1, 0, 19, 0, 0, 0, 77, 0, 4, 0, 3, 0, 0, 0, 11, 42, 43, 44, 44, -74, 0, 28, -73, 0, 33, -79, 0, 0, 0, 2, 0, 24, 0, 0, 0, 10, 0, 2, 0, 0, 0, 22, 0, 10, 0, 23, 0, 25, 0, 0, 0, 32, 0, 3, 
   		 0, 0, 0, 11, 0, 36, 0, 37, 0, 0, 0, 0, 0, 11, 0, 13, 0, 10, 0, 1, 0, 0, 0, 11, 0, 38, 0, 10, 0, 2, 0, 1, 0, 26, 0, 39, 0, 1, 0, 19, 0, 0, 0, 69, 0, 4, 0, 2, 0, 0, 0, 13, 42, -78, 0, 22, 43, 43, -74, 0, 28, -73, 0, 33, -79, 0, 0, 0, 2, 0, 24, 0, 0, 0, 10, 0, 2, 0, 0, 0, 31, 0, 12, 0, 32, 0, 25, 0, 0, 0, 22, 0, 2, 0, 0, 0, 13, 0, 36, 0, 
   		 37, 0, 0, 0, 0, 0, 13, 0, 38, 0, 10, 0, 1, 0, 2, 0, 26, 0, 35, 0, 1, 0, 19, 0, 0, 0, -106, 0, 4, 0, 4, 0, 0, 0, 50, 42, -73, 0, 40, 42, 16, 10, -75, 0, 42, 42, 43, -75, 0, 44, 42, 29, -75, 0, 42, 42, 43, -74, 0, 28, -75, 0, 46, 42, 42, 42, -76, 0, 44, 44, -73, 0, 48, -75, 0, 52, 42, 42, -73, 0, 54, -75, 0, 44, -79, 0, 0, 0, 2, 0, 24, 0, 0, 0, 34, 0, 8, 0, 0, 0, 
   		 35, 0, 4, 0, 14, 0, 10, 0, 36, 0, 15, 0, 37, 0, 20, 0, 38, 0, 28, 0, 39, 0, 41, 0, 40, 0, 49, 0, 41, 0, 25, 0, 0, 0, 42, 0, 4, 0, 0, 0, 50, 0, 36, 0, 37, 0, 0, 0, 0, 0, 50, 0, 13, 0, 10, 0, 1, 0, 0, 0, 50, 0, 38, 0, 10, 0, 2, 0, 0, 0, 50, 0, 58, 0, 15, 0, 3, 0, 2, 0, 56, 0, 57, 0, 1, 0, 19, 0, 0, 1, 81, 0, 6, 0, 7, 0, 0, 0, -125, -69, 
   		 0, 59, 89, -73, 0, 61, 76, 42, -76, 0, 44, 77, 3, 62, -89, 0, 106, -69, 0, 59, 89, -73, 0, 61, 76, 44, -74, 0, 28, -68, 5, 58, 4, 3, 54, 5, -89, 0, 17, 25, 4, 21, 5, 44, 21, 5, -74, 0, 62, 85, -124, 5, 1, 21, 5, 44, -74, 0, 28, -95, -1, -20, 42, -76, 0, 52, -66, 54, 5, 3, 54, 6, -89, 0, 24, 42, 25, 4, 21, 6, 42, -76, 0, 52, 21, 6, 21, 5, 112, 46, -73, 0, 66, -124, 6, 1, 21, 6, 44, -74, 
   		 0, 28, -95, -1, -27, 43, 25, 4, -74, 0, 70, 87, 43, -74, 0, 74, 77, -124, 3, 1, 29, 42, -76, 0, 42, -95, -1, -108, 44, -80, 0, 0, 0, 3, 0, 24, 0, 0, 0, 66, 0, 16, 0, 0, 0, 47, 0, 8, 0, 48, 0, 13, 0, 49, 0, 18, 0, 50, 0, 26, 0, 51, 0, 34, 0, 52, 0, 40, 0, 53, 0, 51, 0, 52, 0, 63, 0, 55, 0, 70, 0, 56, 0, 76, 0, 57, 0, 94, 0, 56, 0, 106, 0, 59, 0, 113, 0, 60, 0, 118, 
   		 0, 49, 0, -127, 0, 62, 0, 25, 0, 0, 0, 82, 0, 8, 0, 0, 0, -125, 0, 36, 0, 37, 0, 0, 0, 8, 0, 123, 0, 77, 0, 78, 0, 1, 0, 13, 0, 118, 0, 79, 0, 10, 0, 2, 0, 15, 0, 114, 0, 80, 0, 15, 0, 3, 0, 34, 0, 84, 0, 81, 0, 82, 0, 4, 0, 37, 0, 26, 0, 83, 0, 15, 0, 5, 0, 70, 0, 48, 0, 84, 0, 15, 0, 5, 0, 73, 0, 33, 0, 83, 0, 15, 0, 6, 0, 85, 0, 0, 0, 28, 
   		 0, 6, -2, 0, 18, 7, 0, 59, 7, 0, 29, 1, -3, 0, 21, 7, 0, 86, 1, 13, -4, 0, 21, 1, 20, -8, 0, 23, 0, 2, 0, 68, 0, 69, 0, 1, 0, 19, 0, 0, 0, 103, 0, 4, 0, 5, 0, 0, 0, 17, 43, 28, 52, 54, 4, 43, 28, 43, 29, 52, 85, 43, 29, 21, 4, 85, -79, 0, 0, 0, 2, 0, 24, 0, 0, 0, 10, 0, 2, 0, 0, 0, 72, 0, 16, 0, 73, 0, 25, 0, 0, 0, 52, 0, 5, 0, 0, 0, 17, 0, 
   		 36, 0, 37, 0, 0, 0, 0, 0, 17, 0, 87, 0, 82, 0, 1, 0, 0, 0, 17, 0, 88, 0, 15, 0, 2, 0, 0, 0, 17, 0, 89, 0, 15, 0, 3, 0, 5, 0, 12, 0, 90, 0, 91, 0, 4, 0, 2, 0, 50, 0, 51, 0, 1, 0, 19, 0, 0, 0, -81, 0, 4, 0, 6, 0, 0, 0, 46, 44, -74, 0, 28, -68, 10, 78, 43, -74, 0, 28, 54, 4, 3, 54, 5, -89, 0, 19, 45, 21, 5, 44, 21, 5, -74, 0, 92, 21, 4, 112, 79, -124, 
   		 5, 1, 21, 5, 44, -74, 0, 28, -95, -1, -22, 45, -80, 0, 0, 0, 3, 0, 24, 0, 0, 0, 26, 0, 6, 0, 0, 0, 82, 0, 7, 0, 83, 0, 13, 0, 84, 0, 19, 0, 85, 0, 32, 0, 84, 0, 44, 0, 87, 0, 25, 0, 0, 0, 62, 0, 6, 0, 0, 0, 46, 0, 36, 0, 37, 0, 0, 0, 0, 0, 46, 0, 13, 0, 10, 0, 1, 0, 0, 0, 46, 0, 38, 0, 10, 0, 2, 0, 7, 0, 39, 0, 96, 0, 12, 0, 3, 0, 13, 0, 
   		 33, 0, 97, 0, 15, 0, 4, 0, 16, 0, 28, 0, 83, 0, 15, 0, 5, 0, 85, 0, 0, 0, 11, 0, 2, -2, 0, 19, 7, 0, 98, 1, 1, 15, 0, 1, 0, 99, 0, 100, 0, 1, 0, 19, 0, 0, 0, -32, 0, 4, 0, 5, 0, 0, 0, 85, 43, -57, 0, 5, 1, -80, 43, -74, 0, 101, -103, 0, 5, 43, -80, -69, 0, 59, 89, -73, 0, 61, 77, 3, 62, -89, 0, 47, 43, 29, -74, 0, 62, 54, 4, 42, 21, 4, -73, 0, 105, -100, 0, 13, 
   		 44, 21, 4, -74, 0, 109, 87, -89, 0, 18, 44, 42, 43, 29, -74, 0, 62, 29, -73, 0, 112, -74, 0, 109, 87, -124, 3, 1, 29, 43, -74, 0, 28, -95, -1, -49, 44, -74, 0, 74, -80, 0, 0, 0, 3, 0, 24, 0, 0, 0, 38, 0, 9, 0, 0, 0, 96, 0, 6, 0, 97, 0, 15, 0, 98, 0, 23, 0, 99, 0, 28, 0, 100, 0, 35, 0, 101, 0, 54, 0, 102, 0, 69, 0, 99, 0, 80, 0, 104, 0, 25, 0, 0, 0, 52, 0, 5, 0, 0, 0, 
   		 85, 0, 36, 0, 37, 0, 0, 0, 0, 0, 85, 0, 115, 0, 10, 0, 1, 0, 23, 0, 62, 0, 116, 0, 78, 0, 2, 0, 25, 0, 55, 0, 83, 0, 15, 0, 3, 0, 35, 0, 34, 0, 117, 0, 91, 0, 4, 0, 85, 0, 0, 0, 19, 0, 6, 6, 8, -3, 0, 12, 7, 0, 59, 1, -4, 0, 25, 1, -6, 0, 14, 2, 0, 1, 0, 118, 0, 100, 0, 1, 0, 19, 0, 0, 0, -32, 0, 4, 0, 5, 0, 0, 0, 85, 43, -57, 0, 5, 1, -80, 
   		 43, -74, 0, 101, -103, 0, 5, 43, -80, -69, 0, 59, 89, -73, 0, 61, 77, 3, 62, -89, 0, 47, 43, 29, -74, 0, 62, 54, 4, 42, 21, 4, -73, 0, 105, -100, 0, 13, 44, 21, 4, -74, 0, 109, 87, -89, 0, 18, 44, 42, 43, 29, -74, 0, 62, 29, -73, 0, 119, -74, 0, 109, 87, -124, 3, 1, 29, 43, -74, 0, 28, -95, -1, -49, 44, -74, 0, 74, -80, 0, 0, 0, 3, 0, 24, 0, 0, 0, 38, 0, 9, 0, 0, 0, 113, 0, 6, 0, 114, 0, 
   		 15, 0, 115, 0, 23, 0, 116, 0, 28, 0, 117, 0, 35, 0, 118, 0, 54, 0, 119, 0, 69, 0, 116, 0, 80, 0, 121, 0, 25, 0, 0, 0, 52, 0, 5, 0, 0, 0, 85, 0, 36, 0, 37, 0, 0, 0, 0, 0, 85, 0, 115, 0, 10, 0, 1, 0, 23, 0, 62, 0, 116, 0, 78, 0, 2, 0, 25, 0, 55, 0, 83, 0, 15, 0, 3, 0, 35, 0, 34, 0, 117, 0, 91, 0, 4, 0, 85, 0, 0, 0, 19, 0, 6, 6, 8, -3, 0, 12, 7, 0, 
   		 59, 1, -4, 0, 25, 1, -6, 0, 14, 2, 0, 2, 0, 99, 0, 114, 0, 1, 0, 19, 0, 0, 0, 81, 0, 5, 0, 3, 0, 0, 0, 19, 42, -76, 0, 44, 42, 42, 27, -73, 0, 105, 28, 4, -73, 0, 121, -74, 0, 62, -84, 0, 0, 0, 2, 0, 24, 0, 0, 0, 6, 0, 1, 0, 0, 0, -125, 0, 25, 0, 0, 0, 32, 0, 3, 0, 0, 0, 19, 0, 36, 0, 37, 0, 0, 0, 0, 0, 19, 0, 125, 0, 91, 0, 1, 0, 0, 0, 19, 0, 
   		 126, 0, 15, 0, 2, 0, 2, 0, 118, 0, 114, 0, 1, 0, 19, 0, 0, 0, 81, 0, 5, 0, 3, 0, 0, 0, 19, 42, -76, 0, 44, 42, 42, 27, -73, 0, 105, 28, 2, -73, 0, 121, -74, 0, 62, -84, 0, 0, 0, 2, 0, 24, 0, 0, 0, 6, 0, 1, 0, 0, 0, -115, 0, 25, 0, 0, 0, 32, 0, 3, 0, 0, 0, 19, 0, 36, 0, 37, 0, 0, 0, 0, 0, 19, 0, 125, 0, 91, 0, 1, 0, 0, 0, 19, 0, 126, 0, 15, 0, 2, 
   		 0, 2, 0, 107, 0, 108, 0, 1, 0, 19, 0, 0, 0, 61, 0, 2, 0, 2, 0, 0, 0, 9, 42, -76, 0, 44, 27, -74, 0, 127, -84, 0, 0, 0, 2, 0, 24, 0, 0, 0, 6, 0, 1, 0, 0, 0, -106, 0, 25, 0, 0, 0, 22, 0, 2, 0, 0, 0, 9, 0, 36, 0, 37, 0, 0, 0, 0, 0, 9, 0, 125, 0, 91, 0, 1, 0, 2, 0, 123, 0, 124, 0, 1, 0, 19, 0, 0, 0, 92, 0, 5, 0, 4, 0, 0, 0, 20, 42, 27, 42, 
   		 -76, 0, 52, 28, 42, -76, 0, 52, -66, 112, 46, 29, 104, -73, 0, -126, -84, 0, 0, 0, 2, 0, 24, 0, 0, 0, 6, 0, 1, 0, 0, 0, -95, 0, 25, 0, 0, 0, 42, 0, 4, 0, 0, 0, 20, 0, 36, 0, 37, 0, 0, 0, 0, 0, 20, 0, -123, 0, 15, 0, 1, 0, 0, 0, 20, 0, -122, 0, 15, 0, 2, 0, 0, 0, 20, 0, -121, 0, 15, 0, 3, 0, 2, 0, 123, 0, -124, 0, 1, 0, 19, 0, 0, 0, 116, 0, 2, 0, 3, 0, 
   		 0, 0, 42, 27, 28, 96, 42, -76, 0, 46, -95, 0, 14, 27, 28, 96, 42, -76, 0, 46, 100, -89, 0, 23, 27, 28, 96, -100, 0, 14, 27, 28, 96, 42, -76, 0, 46, 96, -89, 0, 6, 27, 28, 96, -84, 0, 0, 0, 3, 0, 24, 0, 0, 0, 6, 0, 1, 0, 0, 0, -84, 0, 25, 0, 0, 0, 32, 0, 3, 0, 0, 0, 42, 0, 36, 0, 37, 0, 0, 0, 0, 0, 42, 0, -123, 0, 15, 0, 1, 0, 0, 0, 42, 0, -120, 0, 15, 0, 2, 0, 
   		 85, 0, 0, 0, 6, 0, 3, 21, 16, 66, 1, 0, 1, 0, 99, 0, -119, 0, 1, 0, 19, 0, 0, 0, 60, 0, 3, 0, 2, 0, 0, 0, 8, 42, 43, 43, -66, -74, 0, -118, -80, 0, 0, 0, 2, 0, 24, 0, 0, 0, 6, 0, 1, 0, 0, 0, -80, 0, 25, 0, 0, 0, 22, 0, 2, 0, 0, 0, 8, 0, 36, 0, 37, 0, 0, 0, 0, 0, 8, 0, -115, 0, -114, 0, 1, 0, 1, 0, 99, 0, -116, 0, 1, 0, 19, 0, 0, 0, -124, 0, 
   		 6, 0, 4, 0, 0, 0, 35, 3, 62, -89, 0, 26, 43, 29, 43, 29, 51, 42, -76, 0, 52, 29, 42, -76, 0, 52, -66, 112, 46, 96, -111, 84, -124, 3, 1, 29, 28, -95, -1, -25, 43, -80, 0, 0, 0, 3, 0, 24, 0, 0, 0, 18, 0, 4, 0, 0, 0, -76, 0, 5, 0, -75, 0, 25, 0, -76, 0, 33, 0, -73, 0, 25, 0, 0, 0, 42, 0, 4, 0, 0, 0, 35, 0, 36, 0, 37, 0, 0, 0, 0, 0, 35, 0, -115, 0, -114, 0, 1, 0, 0, 
   		 0, 35, 0, -113, 0, 15, 0, 2, 0, 2, 0, 31, 0, 83, 0, 15, 0, 3, 0, 85, 0, 0, 0, 7, 0, 2, -4, 0, 5, 1, 22, 0, 1, 0, 118, 0, -119, 0, 1, 0, 19, 0, 0, 0, 60, 0, 3, 0, 2, 0, 0, 0, 8, 42, 43, 43, -66, -74, 0, -112, -80, 0, 0, 0, 2, 0, 24, 0, 0, 0, 6, 0, 1, 0, 0, 0, -69, 0, 25, 0, 0, 0, 22, 0, 2, 0, 0, 0, 8, 0, 36, 0, 37, 0, 0, 0, 0, 0, 8, 0, 
   		 -115, 0, -114, 0, 1, 0, 1, 0, 118, 0, -116, 0, 1, 0, 19, 0, 0, 0, -124, 0, 6, 0, 4, 0, 0, 0, 35, 3, 62, -89, 0, 26, 43, 29, 43, 29, 51, 42, -76, 0, 52, 29, 42, -76, 0, 52, -66, 112, 46, 100, -111, 84, -124, 3, 1, 29, 28, -95, -1, -25, 43, -80, 0, 0, 0, 3, 0, 24, 0, 0, 0, 18, 0, 4, 0, 0, 0, -65, 0, 5, 0, -64, 0, 25, 0, -65, 0, 33, 0, -62, 0, 25, 0, 0, 0, 42, 0, 4, 0, 0, 
   		 0, 35, 0, 36, 0, 37, 0, 0, 0, 0, 0, 35, 0, -115, 0, -114, 0, 1, 0, 0, 0, 35, 0, -113, 0, 15, 0, 2, 0, 2, 0, 31, 0, 83, 0, 15, 0, 3, 0, 85, 0, 0, 0, 7, 0, 2, -4, 0, 5, 1, 22, 0, 1, 0, -110, 0, 0, 0, 2, 0, -109, 
   	 };
   	 
   	 public final static byte [] enf = new byte[]{ 
   		 0, 38, 116, 111, 107, 101, 110, 46, 83, 105, 109, 112, 108, 101, 79, 102, 102, 115, 101, 116, 69, 110, 99, 111, 100, 101, 114, 70, 97, 99, 116, 111, 114, 121, 46, 99, 108, 97, 115, 115, 
   		 -54, -2, -70, -66, 0, 0, 0, 51, 0, 34, 7, 0, 2, 1, 0, 32, 116, 111, 107, 101, 110, 47, 83, 105, 109, 112, 108, 101, 79, 102, 102, 115, 101, 116, 69, 110, 99, 111, 100, 101, 114, 70, 97, 99, 116, 111, 114, 121, 7, 0, 4, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 7, 0, 6, 1, 0, 21, 116, 111, 107, 101, 110, 47, 73, 69, 110, 99, 111, 100, 101, 114, 70, 97, 99, 116, 111, 114, 121, 1, 0, 7, 
   		 101, 110, 99, 111, 100, 101, 114, 1, 0, 16, 76, 116, 111, 107, 101, 110, 47, 73, 69, 110, 99, 111, 100, 101, 114, 59, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 21, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 41, 86, 1, 0, 4, 67, 111, 100, 101, 10, 0, 3, 0, 13, 12, 0, 9, 0, 14, 1, 0, 3, 40, 41, 86, 7, 0, 16, 1, 0, 32, 116, 111, 107, 101, 110, 47, 83, 105, 109, 112, 108, 101, 
   		 79, 102, 102, 115, 101, 116, 69, 110, 99, 111, 100, 101, 114, 68, 101, 99, 111, 100, 101, 114, 8, 0, 18, 1, 0, 64, 45, 95, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 10, 0, 15, 0, 20, 12, 0, 9, 0, 21, 
   		 1, 0, 39, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 41, 86, 9, 0, 1, 0, 23, 12, 0, 7, 0, 8, 1, 0, 15, 76, 105, 110, 101, 78, 117, 109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 18, 76, 111, 99, 97, 108, 86, 97, 114, 105, 97, 98, 108, 101, 84, 97, 98, 108, 101, 1, 0, 4, 116, 104, 105, 115, 1, 0, 
   		 34, 76, 116, 111, 107, 101, 110, 47, 83, 105, 109, 112, 108, 101, 79, 102, 102, 115, 101, 116, 69, 110, 99, 111, 100, 101, 114, 70, 97, 99, 116, 111, 114, 121, 59, 1, 0, 3, 107, 101, 121, 1, 0, 18, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 1, 0, 10, 103, 101, 116, 69, 110, 99, 111, 100, 101, 114, 1, 0, 18, 40, 41, 76, 116, 111, 107, 101, 110, 47, 73, 69, 110, 99, 111, 100, 101, 114, 59, 1, 0, 10, 83, 
   		 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 31, 83, 105, 109, 112, 108, 101, 79, 102, 102, 115, 101, 116, 69, 110, 99, 111, 100, 101, 114, 70, 97, 99, 116, 111, 114, 121, 46, 106, 97, 118, 97, 0, 33, 0, 1, 0, 3, 0, 1, 0, 5, 0, 1, 0, 2, 0, 7, 0, 8, 0, 0, 0, 2, 0, 1, 0, 9, 0, 10, 0, 1, 0, 11, 0, 0, 0, 79, 0, 5, 0, 2, 0, 0, 0, 19, 42, -73, 0, 12, 42, -69, 0, 15, 89, 18, 17, 43, -73, 
   		 0, 19, -75, 0, 22, -79, 0, 0, 0, 2, 0, 24, 0, 0, 0, 14, 0, 3, 0, 0, 0, 5, 0, 4, 0, 6, 0, 18, 0, 7, 0, 25, 0, 0, 0, 22, 0, 2, 0, 0, 0, 19, 0, 26, 0, 27, 0, 0, 0, 0, 0, 19, 0, 28, 0, 29, 0, 1, 0, 1, 0, 30, 0, 31, 0, 1, 0, 11, 0, 0, 0, 47, 0, 1, 0, 1, 0, 0, 0, 5, 42, -76, 0, 22, -80, 0, 0, 0, 2, 0, 24, 0, 0, 0, 6, 0, 1, 0, 0, 0, 
   		 14, 0, 25, 0, 0, 0, 12, 0, 1, 0, 0, 0, 5, 0, 26, 0, 27, 0, 0, 0, 1, 0, 32, 0, 0, 0, 2, 0, 33, 
   	};
   	 
   	 public final static byte [] def = new byte[]{ 
   		 0, 38, 116, 111, 107, 101, 110, 46, 83, 105, 109, 112, 108, 101, 79, 102, 102, 115, 101, 116, 68, 101, 99, 111, 100, 101, 114, 70, 97, 99, 116, 111, 114, 121, 46, 99, 108, 97, 115, 115, 
   		 -54, -2, -70, -66, 0, 0, 0, 51, 0, 34, 7, 0, 2, 1, 0, 32, 116, 111, 107, 101, 110, 47, 83, 105, 109, 112, 108, 101, 79, 102, 102, 115, 101, 116, 68, 101, 99, 111, 100, 101, 114, 70, 97, 99, 116, 111, 114, 121, 7, 0, 4, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 7, 0, 6, 1, 0, 21, 116, 111, 107, 101, 110, 47, 73, 68, 101, 99, 111, 100, 101, 114, 70, 97, 99, 116, 111, 114, 121, 1, 0, 7, 
   		 100, 101, 99, 111, 100, 101, 114, 1, 0, 16, 76, 116, 111, 107, 101, 110, 47, 73, 68, 101, 99, 111, 100, 101, 114, 59, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 21, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 41, 86, 1, 0, 4, 67, 111, 100, 101, 10, 0, 3, 0, 13, 12, 0, 9, 0, 14, 1, 0, 3, 40, 41, 86, 7, 0, 16, 1, 0, 32, 116, 111, 107, 101, 110, 47, 83, 105, 109, 112, 108, 101, 
   		 79, 102, 102, 115, 101, 116, 69, 110, 99, 111, 100, 101, 114, 68, 101, 99, 111, 100, 101, 114, 8, 0, 18, 1, 0, 64, 45, 95, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 10, 0, 15, 0, 20, 12, 0, 9, 0, 21, 
   		 1, 0, 39, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 41, 86, 9, 0, 1, 0, 23, 12, 0, 7, 0, 8, 1, 0, 15, 76, 105, 110, 101, 78, 117, 109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 18, 76, 111, 99, 97, 108, 86, 97, 114, 105, 97, 98, 108, 101, 84, 97, 98, 108, 101, 1, 0, 4, 116, 104, 105, 115, 1, 0, 
   		 34, 76, 116, 111, 107, 101, 110, 47, 83, 105, 109, 112, 108, 101, 79, 102, 102, 115, 101, 116, 68, 101, 99, 111, 100, 101, 114, 70, 97, 99, 116, 111, 114, 121, 59, 1, 0, 3, 107, 101, 121, 1, 0, 18, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 1, 0, 10, 103, 101, 116, 68, 101, 99, 111, 100, 101, 114, 1, 0, 18, 40, 41, 76, 116, 111, 107, 101, 110, 47, 73, 68, 101, 99, 111, 100, 101, 114, 59, 1, 0, 10, 83, 
   		 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 31, 83, 105, 109, 112, 108, 101, 79, 102, 102, 115, 101, 116, 68, 101, 99, 111, 100, 101, 114, 70, 97, 99, 116, 111, 114, 121, 46, 106, 97, 118, 97, 0, 33, 0, 1, 0, 3, 0, 1, 0, 5, 0, 1, 0, 2, 0, 7, 0, 8, 0, 0, 0, 2, 0, 1, 0, 9, 0, 10, 0, 1, 0, 11, 0, 0, 0, 79, 0, 5, 0, 2, 0, 0, 0, 19, 42, -73, 0, 12, 42, -69, 0, 15, 89, 18, 17, 43, -73, 
   		 0, 19, -75, 0, 22, -79, 0, 0, 0, 2, 0, 24, 0, 0, 0, 14, 0, 3, 0, 0, 0, 6, 0, 4, 0, 7, 0, 18, 0, 8, 0, 25, 0, 0, 0, 22, 0, 2, 0, 0, 0, 19, 0, 26, 0, 27, 0, 0, 0, 0, 0, 19, 0, 28, 0, 29, 0, 1, 0, 1, 0, 30, 0, 31, 0, 1, 0, 11, 0, 0, 0, 47, 0, 1, 0, 1, 0, 0, 0, 5, 42, -76, 0, 22, -80, 0, 0, 0, 2, 0, 24, 0, 0, 0, 6, 0, 1, 0, 0, 0, 
   		 12, 0, 25, 0, 0, 0, 12, 0, 1, 0, 0, 0, 5, 0, 26, 0, 27, 0, 0, 0, 1, 0, 32, 0, 0, 0, 2, 0, 33, 
   	};


   }

}

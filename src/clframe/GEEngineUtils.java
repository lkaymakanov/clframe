package clframe;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
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
    	data.setEngineClassLoader( new GEEngineCl(data));
    	data.setProperties( new Properties());
    	data.getProperties().put("name", className);
		data.getResources().put("engine.properties", new ResourceInfo("key=myencryptedsha512key".getBytes(), "engine.properties", "originalResourceName"));
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
    	GEERawZipProcessor pr = new DecryptGEEZipProcessor(new GEERawZipProcessor(bufferSize), pass, bufferSize);   
    	ZipUtils.zipProcess(is, pr);
    	pr.outData.setEngineClassLoader(new GEEngineCl(pr.outData));
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
          data.getResources().put(resourceName, new ResourceInfo(resourceBytes, resourceName, originalResourceName));
    }
    
    
    /***
     * Returns class bytes for by engineName & classname!!!
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
	private static Object getInstance(Class type,
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
	private static void printResourcesNames(IGEEngineData data){
		for(String k :data.getResources().keySet()){
			ResourceInfo cinfo = data.getResources().get(k);
			System.out.println("ResourceName : " + k + ", Original ResourceName: " + cinfo.originalName);
		}
	}
	
	/**
	 * Prints the names of the classes loaded in the GEEngineData!!!
	 * @param data
	 */
	private static void printClassesNames(IGEEngineData data){
		for(String k :data.getClassMap().keySet()){
			ClassInfo cinfo = data.getClassMap().get(k);
			System.out.println("Class : " + k + ", Original Class: " + cinfo.originalName);
		}
	}
	
	
	private static void printRawDataNames(IGEEngineData data){
		for(String k :data.getRowData().keySet()){
			System.out.println("RawDataName : " + k );
		}
	}
    
    
    /***
     * Creates an object by calling constructor with types argtypes, and arguments args!!!
     * @param engine
     * @param className
     * @return
     */
    public static Object createObjectByClassName(String engineName, String className, Class<?>[] argtypes,
			Object[] args){
    	try {
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
    public static Object createObjectByClassName(String engineName, String className){
    	return createObjectByClassName(engineName, className, null, null);
    }
    
    public static void main(String [] args) throws FileNotFoundException{
    	IGEEngineData data = loadEngineData(new File("C:\\Users\\lubo\\Desktop\\gee\\deng.jar"), 1024*1024, "geengine");
    	printClassesNames(data);
    	printResourcesNames(data);
    	printRawDataNames(data);
    }
}

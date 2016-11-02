package clframe;

import java.io.ByteArrayInputStream;
import java.io.File;
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
	
	private static Map<String, GEEngineData> engines = new HashMap<String, GEEngineData>();
	
    /**
     * Returns Engine by Engine name!!!
     * @return
     */
    public static IGEEngine getEngine(String engineName){
    	synchronized (engines) {
			return  engines.get(engineName).enigine;
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
     */
    public static IGEEngine createEngine(File f){
    	GEEngineData data = loadEngineData(f);
    	GEEngineData e=null;
    	synchronized (engines) {
			e = engines.get(getEngineName(data.properties));
			if(e!=null) return e.enigine;
			data.enigine = createEngine(data);
			engines.put(getEngineName(data.properties), (data));
		}
    	
    	return data.enigine;
    }
    
    
    /***
     * Creates an engine from engine content passed as byte array!!!
     *  If engine exists under that name the existing engine is returned!!! Otherwise registers & returns new IGEEngine!!!
     * @param engine
     * @return
     */
    public static IGEEngine createEngine(byte[] engine){
    	GEEngineData data = loadEngineData(engine);
    	GEEngineData e=null;
    	synchronized (engines) {
			e = engines.get(getEngineName(data.properties));
			if(e!=null) return e.enigine;
			data.enigine =  createEngine(data);
			engines.put(getEngineName(data.properties), data);
		}
    	return data.enigine;
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
    private static IGEEngine createEngine(GEEngineData data){
    	IGEEngine engine=null;
    	try {
			engine = (IGEEngine)data.engineClassLoader.loadClass(getEngineName(data.properties)).newInstance();
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
    	GEEngineData data = 	engines.get(engineName);
        if(data == null) return null;
    	try {
			return data.engineClassLoader.loadClass(className);
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
    	GEEngineData data = loadEngineData(className);
    	GEEngineData e=null;
    	synchronized (engines) {
			e = engines.get(getEngineName(data.properties));
			if(e!=null) return e.enigine;
			data.enigine =  createEngine(data);
			engines.put(getEngineName(data.properties), data);
		}
    	return data.enigine;
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
    private static GEEngineData loadEngineData(String className){
    	GEEngineData data = new GEEngineData();
    	data.engineClassLoader = new GEEngineCl(data);
    	data.properties = new Properties();
    	data.properties.put("name", className);
		data.resources.put("engine.properties", new ResourceInformation("key=myencryptedsha512key".getBytes(), "engine.properties"));
     	return data;
    }
    
    /***
     * Loads from byte array into memory the data needed by the engine!!!
     * @param engine
     * @return
     */
    private static GEEngineData loadEngineData(byte[] engine){
    	GEEZipProcessor pr = new GEEZipProcessor();
    	ZipUtils.zipProcess(new ByteArrayInputStream(engine), pr);
    	pr.outData.engineClassLoader = new GEEngineCl(pr.outData);
    	return pr.outData;
    }
    
    /**
     * Loads from engine file into memory the data needed by the engine !!!
     * @param f
     * @return
     */
    private static GEEngineData loadEngineData(File f){
    	GEEZipProcessor pr = new GEEZipProcessor();
    	ZipUtils.zipProcess(f, pr);
    	pr.outData.engineClassLoader = new GEEngineCl(pr.outData);
    	return pr.outData;
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
       GEEngineData data = 	engines.get(engineName);
       if(data == null) return null;
       return data.classMap.keySet();
    }
    
    
    /***
     * Returns the names of the resources needed by this engine!!!
     * @param engineName
     * @return
     */
    public static Set<String> getEngineResourceNames(String engineName){
       GEEngineData data = 	engines.get(engineName);
       if(data == null) return null;
       return data.resources.keySet();
    }
    
    
    /***
     * Returns a resource by engine name & resource name!!!
     * @param engineName
     * @param resourceName
     * @return
     */
    public static byte [] getEngineResource(String engineName, String resourceName){
        GEEngineData data = 	engines.get(engineName);
        if(data == null) return null;
        return data.resources.get(resourceName).bytes;
    }
    
	/***
     * Add a resource to engine resources!!!
     */
    public static void addResourceToEngine(String engineName, String resourceName, byte [] resourceBytes){
    	  GEEngineData data = 	engines.get(engineName);
          if(data == null) return ;
          data.resources.put(resourceName, new ResourceInformation(resourceBytes, resourceName));
    }
    
    
    /***
     * Returns class bytes for by engineName & classname!!!
     * @param engineName
     * @param className
     * @return
     */
    public static byte [] getEngineClass(String engineName, String className){
        GEEngineData data = engines.get(engineName);
        if(data == null) return null;
        return data.classMap.get(className).bytes;
    }
    
    
    
    /**
     * Get the names of the properties names of the engine!!!
     * @param engineName
     * @return
     */
    public static Enumeration<Object>  getEnginePropertyNames(String engineName){
    	GEEngineData data = engines.get(engineName);
        if(data == null) return null;
        return data.properties.keys();
    }
    
    /***
     * Get a property of the engine properties by property name!!!
     * @param engineName
     * @param propertyKey
     * @return
     */
    public static Object  getEngineProperty(String engineName, String propertyKey){
    	GEEngineData data = engines.get(engineName);
        if(data == null) return null;
        return data.properties.get(propertyKey);
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
}

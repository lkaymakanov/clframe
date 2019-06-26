package clframe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Map;

/**
 * A class loader that loads classes from {@link GEEngineData}!!!
 * @author lubo
 *
 */
class GEEngineCl extends ClassLoader  {
	
	IModuleData data;
	
	GEEngineCl(IModuleData data, ClassLoader parent){
		super(parent == null ? GEEngineCl.class.getClassLoader():parent);
		this.data = data;
	}
	
	
	GEEngineCl(InputStream is, ClassLoader parent){
		this((IModuleData)GEEngineUtils.MODULE.loadModule(is), parent);
	}
	
	
	byte [] getClassBytes(String clName) {
	  ClassInfo i = data.getClassMap().get(clName);
	  return i == null ? null : copy(i.bytes);
	}
	
	
	/***
	 * Finds a class by package.classname (fully qualified class name) example test.test.TestClass  !!
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public synchronized Class findClass(String className)throws ClassNotFoundException{
    	ClassInfo b = data.getClassMap().get(className + ".class");
    	
        byte[] classBytes = b == null ? null : b.bytes;
        
        if (classBytes == null){
        	@SuppressWarnings("rawtypes")
			Class c = getParent().loadClass(className);
        	if(c!= null) return c;
            throw new ClassNotFoundException();
        }
        else{
            GEEngineUtils.log("=============== Looking for class " + className + " ================");
        	if(b.clazzz != null) return b.clazzz;
            b.clazzz = defineClass(className, classBytes, 0, classBytes.length);
            resolveClass(b.clazzz);
            GEEngineUtils.log(className + " class finded...");
            return b.clazzz;
        }
    }
    
    

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
    	GEEngineUtils.log(name + " class loaded  ");
    	return super.loadClass(name);
    }
    
    
    //==================== resource management ===================================
    @Override
    public URL getResource(String name) {
    	URL urlP =  super.getResource(name);
    	if(urlP!=null) return urlP;
    	URL myUrl = null;
		try {
			myUrl = new URL("", "", 1, name,  new MyStreamHandler(data.getResources()));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
    	
    	return myUrl;
    }
    
    
    
    private static class MyStreamHandler extends URLStreamHandler{
    	private Map<String, ResourceInfo> resources;
    	private MyStreamHandler(Map<String, ResourceInfo> resources) {
    		this.resources = resources;
		}
		@Override
		protected URLConnection openConnection(URL u) throws IOException {
			return new MyURLConnection(u, resources);
		}
    }
    
    
    private static class MyURLConnection extends URLConnection{
    	private String resourceName;
    	private Map<String, ResourceInfo> resources;
		protected MyURLConnection(URL url, Map<String, ResourceInfo> resources) {
			super(url);
			resourceName = url.getFile();
			resourceName = (resourceName == null ? null : resourceName.replaceFirst("/", "").replaceAll("/", "."));
			this.resources = resources;
		}

		@Override
		public void connect() throws IOException {
			
		}
		
		@Override
		public InputStream getInputStream() throws IOException {
			ResourceInfo rInfo = resources.get(resourceName);
			if(rInfo == null) return null;
			return new ByteArrayInputStream(rInfo.bytes);
		}
    }
    
    private static byte[] copy(byte [] a) {
    	if(a == null) return null;
    	byte [] cpy = new byte[a.length];
    	for(int i =0; i < a.length; i++) {
    		cpy[i] = a[i];
    	}
    	return cpy;
    }
    
}

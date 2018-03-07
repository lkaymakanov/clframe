package clframe;


/**
 * A class loader that loads classes from {@link GEEngineData}!!!
 * @author lubo
 *
 */
class GEEngineCl extends ClassLoader  {
	
	IGEEngineData data;
	GEEngineCl(IGEEngineData data, ClassLoader parent){
		super(parent == null ? GEEngineCl.class.getClassLoader():parent);
		this.data = data;
	}
	
	
	/***
	 * Finds a class by package.classname (fully qualified class name) example test.test.TestClass  !!
	 */
    public synchronized Class findClass(String className)throws ClassNotFoundException{
    	ClassInfo b = data.getClassMap().get(className + ".class");
    	
        byte[] classBytes = b == null ? null : b.bytes;
        
        if (classBytes == null){
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
}

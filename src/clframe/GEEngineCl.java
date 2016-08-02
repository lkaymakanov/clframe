package clframe;





/**
 * A class loader that loads classes from {@link GEEngineData}!!!
 * @author lubo
 *
 */
class GEEngineCl extends ClassLoader  {
	
	GEEngineData data;
	GEEngineCl(GEEngineData data){
		super(GEEngineCl.class.getClassLoader());
		this.data = data;
	}
	
	
	/***
	 * Finds a class by package.classname (fully qualified class name) example test.test.TestClass  !!
	 */
    public synchronized Class findClass(String className)throws ClassNotFoundException{
    	ClassInfo b = data.classMap.get(className + ".class");
    	
        byte[] classBytes = b == null ? null : b.bytes;
        
        if (classBytes == null){
        	Class c = getParent().loadClass(className);
        	if(c!= null) return c;
            throw new ClassNotFoundException();
        }
        else{
        	//System.out.println("=============== Looking for class " + className + " ================");
        	if(b.clazzz != null) return b.clazzz;
            b.clazzz = defineClass(className, classBytes, 0, classBytes.length);
            resolveClass(b.clazzz);
            return b.clazzz;
        }
    }

}

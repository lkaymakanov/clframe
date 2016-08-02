package clframe;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Loads a class from a class file!!!
 * @author lubo
 *
 */
class FileSystemCl  extends ClassLoader{

	private String pathToFile;
	
	/***
	 * A constructor with path to the file to be loaded!!!
	 * @param pathToFile
	 */
	public FileSystemCl(String pathToFile){
		this.pathToFile = pathToFile;
	}
	
	/***
	 * Reads the file content of the class file as byte array!
	 * @param pathToFile
	 * @return
	 */
	public byte[] findClassBytes(String pathToFile) {
		FileInputStream inFile = null;
        try{
            inFile = new FileInputStream(pathToFile);
            byte[] classBytes = new  byte[inFile.available()];
            inFile.read(classBytes);
            return classBytes;
        }
        catch (java.io.IOException ioEx){
            return null;
        }finally{
        	try {
				inFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw  new RuntimeException(e);
			}
        }
    }

	/***
	 * Finds a class by package.classname (fully qualified class name) example test.test.TestClass  !!
	 */
    public Class findClass(String className)throws ClassNotFoundException{
        byte[] classBytes = findClassBytes(pathToFile);
        if (classBytes == null){
            throw new ClassNotFoundException();
        }
        else{
        	System.out.println("=============== Looking for class " + className);
            return defineClass(className, classBytes, 0, classBytes.length);
        }
    }

/*    private Class findClass(String name, byte[] classBytes)throws ClassNotFoundException{

        if (classBytes==null){
            throw new ClassNotFoundException(
                "(classBytes==null)");
        }
        else{
            return defineClass(name, classBytes,
                0, classBytes.length);
        }
    }*/

/*    public void execute(String codeName,
        byte[] code){

        Class klass = null;
        try{
            klass = findClass(codeName, code);
            TaskIntf task = (TaskIntf)
                klass.newInstance();
            task.execute();
        }
        catch(Exception exception){
            exception.printStackTrace();
        }
    }*/
}

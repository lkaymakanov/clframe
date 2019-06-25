package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import clframe.GEEngineUtils;
import clframe.IModuleHandle;

public class Mytest {

	public static void main(String [] a) throws FileNotFoundException {
		InputStream is = null;
		GEEngineUtils.setLoggerName("sysout");
    	//create encrypted stream for ceaser key 
    	is = new FileInputStream(new File("D:\\testengine\\jaro.jar"));
    	
        IModuleHandle moduleHandle = 	GEEngineUtils.MODULE.loadModule(is);
        ClassLoader ld =  GEEngineUtils.MODULE.createClassLoader(moduleHandle, Mytest.class.getClassLoader());
       
        System.out.println(moduleHandle);
	}
}

package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import clframe.GEEngineUtils;
import clframe.IModuleHandle;
import clframe.StreamUtils;

public class Mytest {

	public static void main(String [] a) throws UnsupportedEncodingException, IOException {
		InputStream is = null;
		GEEngineUtils.setLoggerName("sysout");
    	//create encrypted stream for ceaser key 
    	is = new FileInputStream(new File("D:\\testengine\\md.zip"));
    	
        IModuleHandle moduleHandle = 	GEEngineUtils.MODULE.loadModule(is);
        ClassLoader ld =  GEEngineUtils.MODULE.createClassLoader(moduleHandle, Mytest.class.getClassLoader());
       
        is = new FileInputStream("D:\\GlobalsInit.class");
        
        byte [] myInverse = new byte[256];
        for(int i = 0; i < 256; i++) {
        	myInverse[i] = (byte)i;
        }
        InputStream inv =   GEEngineUtils.MODULE.createReverseInputStream(StreamUtils.toInputStream(myInverse));
        myInverse = StreamUtils.toByteArray(inv);
        
        GEEngineUtils.printByteArrayMatrix(StreamUtils.toByteArray(is), 100, "0");
        
        System.out.println(moduleHandle);
	}
}

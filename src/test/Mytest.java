package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import clframe.GEEngineUtils;
import clframe.IModuleHandle;
import clframe.StreamUtils;

public class Mytest {

	public static void main(String [] a) throws UnsupportedEncodingException, IOException {
		test2();
	}
	
	
	private static void test1() throws UnsupportedEncodingException, IOException{
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
	
	
	private static void test2() throws IOException {
		InputStream is = null;
		GEEngineUtils.setLoggerName("sysout");
    	//create encrypted stream for ceaser key 
    	is = new FileInputStream(new File("C:\\check.png"));
    	
    	byte [] pad = "bat_lubo2001".getBytes("UTF-8");
    	is =  StreamUtils.getPaddingIStreamConverter(is, StreamUtils.toInputStream(pad), false).convert();
		
    	FileOutputStream fos = new FileOutputStream(new File("check1.png"));
    	int  b = is.read();
    	while(b!=-1) {
    		 fos.write(b);
    		 b = is.read();
    	}
    	fos.close();
    	is.close();
	}
}

package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import clframe.GEEngineUtils;
import clframe.GEEngineUtils.ENCRYPT_DECRYPT;
import clframe.GEEngineUtils.ENCRYPT_DECRYPT.CIPHER_MODE;
import clframe.GEEngineUtils.ENGINE;
import clframe.GEEngineUtils.MODULE;
import clframe.IModuleHandle;
import clframe.StreamUtils;

public class Test {

	public static void main(String[] args) throws Exception {
		//testLoadingModule();
		//testToPlainTxt();
    	//testFromPlainText(readPlainTxtFromFile(new File("res.txt")).get(0));
		GEEngineUtils.setLoggerName("sysout");
		//testLoadingModule();
		
		List<String> lines =   readPlainTxtFromFile(new File("MyTest.java"));
		for(String l:lines) {
			for(int i =0; i < l.length(); i++) {
				 int  cp =  l.codePointAt(i);
				 
				 System.out.println(cp);
				 
			}
		}
		System.out.println(lines);
	}
	
	
	private static void testLoadingModule() throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException,
	IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
		InputStream is = null;
    	
		IModuleHandle h = loadModule(new File("C:\\testengine\\md.zip"));
		
		
        ClassLoader cl = MODULE.createClassLoader(h, Test.class.getClassLoader());
        Set<String> resNames =  MODULE.getResourcesNames(h);
        resNames.clear();
       // Set<String> resNames =  MODULE.getResourcesNames(h);
        Set<String> classNames =  MODULE.getClassesNames(h);
        		
        Object s =  cl.getResourceAsStream("/resources/pages/newhorizont/newhorizont_org.xhtml");
        
        //Class c = 	cl.loadClass("appgeengine.engine.chat.ChatBean");
        MODULE.InstanceBuilder ibd = new GEEngineUtils.MODULE.InstanceBuilder();
        ibd.setClassLoader(cl).setClassName("java.lang.String");
        Object o = ibd.build();
    	//System.out.println(o);
        /** wrtie to file 
    	is = EncryptedInputStream.createCipherInputStream(new FileInputStream(new File("C:\\Users\\Lubo\\Desktop\\fileen.file")), CeaserKey.createCeaserKey("mypass"), ENCRYPT_MODE.DECRYPT);
        encbytes =  toByteArray(is);
    	fos = new FileOutputStream("C:\\Users\\Lubo\\Desktop\\filede.file");
    	fos.write(encbytes);
    	fos.close();
    	*/
        
        //creates engine from engine fileInputStream
        ENGINE.EngineBuilder bd = new ENGINE.EngineBuilder();
        bd.setInputStream(new FileInputStream(new File("C:\\Users\\Lubo\\Desktop\\eng.eng")));
        bd.setPass(null);
        bd.setOff(0);
        bd.build();
	}
	
	
	
	private static void testToPlainTxt() throws IOException {
		InputStream is = new FileInputStream(new File("C:\\testengine\\md.zip"));
		writePlainTxtToFile(StreamUtils.toBase64Txt(is), new File("res.txt"));
	}
	
	
	
	private static void testFromPlainText(String txt) throws IOException {
	    byte []  b = StreamUtils.fromBase64Txt(txt);
		OutputStream os =  new FileOutputStream(new File("mdres.zip"));
		os.write(b);
		os.flush();
		os.close();
	}
	
	
	
	/**
	 * Writes String s to file as one line...
	 * @param s
	 * @param f
	 * @throws FileNotFoundException
	 */
	private static void writePlainTxtToFile(String s, File f) throws FileNotFoundException {
		PrintWriter p = new PrintWriter(f);
		p.println(s);
		p.close();
	}
	
	
	
	/**
	 * Reads a text file line by line...
	 * @param f
	 * @return
	 * @throws FileNotFoundException
	 */
	private static List<String> readPlainTxtFromFile(File f) throws FileNotFoundException{
		List<String> l =  new ArrayList<String>();
		Scanner s = new Scanner((f));
	    String a = null; a = s.nextLine();
	    while(a!= null) {
	    	l.add(a);
	    	try { 
	    		a = s.nextLine();
	    	}catch(Exception e) {
	    		break;
	    	}
	    }
	    s.close();
	    return l;
	}
	
	
	//returns a new encrypted stream on input stream
	private static InputStream createCeaserEncryptStream(InputStream is, String pass) throws IOException {
		is = ENCRYPT_DECRYPT.CEASER.createCeaserCipherInputStream(is, ENCRYPT_DECRYPT.CEASER.createCeaserKey(pass), CIPHER_MODE.ENCRYPT);
		return is;
	}
	
	
	//creates a new decrrypted stream on input stream
	private static InputStream createCeaserDecryptStream(InputStream is, String pass) throws IOException {
		is =  ENCRYPT_DECRYPT.CEASER.createCeaserCipherInputStream(is, ENCRYPT_DECRYPT.CEASER.createCeaserKey(pass), CIPHER_MODE.DECRYPT);
		return is;
	}
	
	private static IModuleHandle loadModule(InputStream is) {
		IModuleHandle h = MODULE.loadModule(is);
		return h;
	}
	
	
	private static IModuleHandle loadModule(File  f) throws FileNotFoundException {
		IModuleHandle h = MODULE.loadModule(new FileInputStream(f));
		return h;
	}
	
}

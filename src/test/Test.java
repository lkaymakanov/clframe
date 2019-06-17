package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import clframe.GEEngineUtils.ENCRYPT_DECRYPT;
import clframe.GEEngineUtils.ENCRYPT_DECRYPT.CIPHER_MODE;

public class Test {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		InputStream is = null;
    	//create encrypted stream for ceaser key 
    	is = ENCRYPT_DECRYPT.CEASER.createCeaserCipherInputStream(new FileInputStream(new File("C:\\Users\\Lubo\\Desktop\\eng.eng")), ENCRYPT_DECRYPT.CEASER.createCeaserKey("mypass"), CIPHER_MODE.ENCRYPT);
    	//is.close();
    	ENCRYPT_DECRYPT.CEASER.createEngine(ENCRYPT_DECRYPT.CEASER.createCeaserCipherInputStream(is , ENCRYPT_DECRYPT.CEASER.createCeaserKey("mypass"), CIPHER_MODE.DECRYPT), 0);
    	
    	/** wrtie to file 
    	is = EncryptedInputStream.createCipherInputStream(new FileInputStream(new File("C:\\Users\\Lubo\\Desktop\\fileen.file")), CeaserKey.createCeaserKey("mypass"), ENCRYPT_MODE.DECRYPT);
        encbytes =  toByteArray(is);
    	fos = new FileOutputStream("C:\\Users\\Lubo\\Desktop\\filede.file");
    	fos.write(encbytes);
    	fos.close();
    	*/
    	
    	System.out.println("end test");
    	
    	//create memory input stream on file
	}
}
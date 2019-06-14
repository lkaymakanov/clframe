package clframe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;


enum ENCRYPT_MODE{
	ENCRYPT,
	DECRYPT,
}


/***
 * Input stream wrapper that encrypt/decrypts the wrapped stream based on encryption mode mode!
 * @author Lubo
 *
 */
class EncryptedInputStream  extends InputStream {
	private InputStream is;  //the wrapped istream
	private byte [] bytes;  
	private ByteArrayInputStream bis;
	
	EncryptedInputStream(InputStream is) throws IOException{
		this.is = is;
	}
	
	@Override
	public int read() throws IOException {
		if(bis == null) {
			//convert stream to byte array
			bytes = GEEngineUtils.toByteArray(is);
			
			//encrypt decrypt byte array based on mode
			encryptDecrpyt();
			
			//create byte array input stream
			bis = new ByteArrayInputStream(bytes);
		}
		return bis.read();
	}
	
	/**Encrypts or decrypts byte array based on mode*/
	private void encryptDecrpyt() {
		//do nothing for now
	}
	
	
	/***
	 * Static factory method!!!
	 */
	static EncryptedInputStream getEncryptedInputStream(InputStream is,  Key key, String algorithm, ENCRYPT_MODE mode) throws IOException {
		return new EncryptedInputStream(is);
	}
}

package clframe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;


enum CIPHER_MODE{
	ENCRYPT,
	DECRYPT,
}


/***
 * Input stream wrapper that encrypt/decrypts the wrapped stream based on encryption mode mode!
 * @author Lubo
 *
 */
class CipherInputStream  extends InputStream {
	private InputStream is;  //the wrapped istream
	private byte [] bytes;  
	private ByteArrayInputStream bis;
	private Key key;
	private CIPHER_MODE mode;
	
	CipherInputStream(InputStream is, Key key, CIPHER_MODE mode) throws IOException{
		this.is = is;
		this.key = key;
		this.mode = mode;
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
		if(key instanceof CeaserKey ) {
			CeaserKey  k = (CeaserKey)key;
			if(mode == CIPHER_MODE.ENCRYPT) {bytes = k.encode(bytes);}
			else {bytes = k.decode(bytes);}
		}
	}
	
	
	/***
	 * Static factory method!!!
	 */
	static CipherInputStream createCipherInputStream(InputStream is,  Key key,  CIPHER_MODE mode) throws IOException {
		return new CipherInputStream(is, key, mode);
	}
}

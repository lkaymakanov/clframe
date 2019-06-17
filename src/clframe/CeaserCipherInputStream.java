package clframe;

import java.io.IOException;
import java.io.InputStream;


enum CIPHER_MODE{
	ENCRYPT,
	DECRYPT,
}


/***
 * Input stream wrapper that encrypt/decrypts the wrapped stream based on encryption mode mode!
 * @author Lubo
 *
 */
class CeaserCipherInputStream  extends CipherInputStream  {
	
	private CeaserCipherInputStream(InputStream is, CeaserKey key, CIPHER_MODE mode) throws IOException {
		super(is, key, mode);
	}
	

	/**Encrypts or decrypts byte array based on mode*/
	protected byte [] encryptDecrpyt(byte[] bytes) {
		//do nothing for now
		CeaserKey  k = (CeaserKey)key;
		if(mode == CIPHER_MODE.ENCRYPT) {return  k.encode(bytes);}
		else {return  k.decode(bytes);}
	}
	
	
	/***
	 * Static factory method!!!
	 */
	static CeaserCipherInputStream createCeaserCipherInputStream(InputStream is,  CeaserKey key,  CIPHER_MODE mode) throws IOException {
		return new CeaserCipherInputStream(is, key, mode);
	}
}

package clframe;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;


import clframe.GEEngineUtils.ENCRYPT_DECRYPT.CIPHER_MODE;
/***
 * Input stream wrapper that encrypts/decrypts the wrapped stream based on encryption mode mode!
 * @author Lubo
 *
 */
class CeaserCipherInputStream  extends CipherInputStream  {
	
	private CeaserCipherInputStream(InputStream is, Key key, CIPHER_MODE mode) throws IOException {
		super(is, key, mode);
	}
	

	/**Encrypts or decrypts byte array based on mode*/
	protected byte [] encryptDecrpyt(byte[] bytes) {
		//do nothing for now
		if(!(key instanceof CeaserKey)) throw new RuntimeException("Key must  be an instance of Ceaser key....");
		CeaserKey  k = (CeaserKey)key;
		if(mode == CIPHER_MODE.ENCRYPT) {return  k.encode(bytes);}
		else {return  k.decode(bytes);}
	}
	
	
	/***
	 * Static factory method!!!
	 */
	static CeaserCipherInputStream createCeaserCipherInputStream(InputStream is,  Key key,  CIPHER_MODE mode) throws IOException {
		return new CeaserCipherInputStream(is, key, mode);
	}
}

package clframe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;

abstract class CipherInputStream extends InputStream {
	private InputStream is;  //the wrapped istream
	private byte [] bytes;  
	private ByteArrayInputStream bis;
	protected Key key;
	protected CIPHER_MODE mode;
	
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
			bytes = encryptDecrpyt(bytes);
			
			//create byte array input stream
			bis = new ByteArrayInputStream(bytes);
		}
		return bis.read();
	}
	
	/**Encrypts or decrypts byte array based on mode*/
	protected abstract byte[] encryptDecrpyt(byte[] b);// 
		
}

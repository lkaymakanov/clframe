package clframe;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;


enum ENCRYPT_MODE{
	ENCRYPT,
	DECRYPT,
}



abstract class EncryptedInputStream  extends InputStream {
	InputStream is;
	
	EncryptedInputStream(InputStream is){
		this.is = is;
	}
	
	@Override
	public int read() throws IOException {
		return 0;
	}
	
	static EncryptedInputStream getEncryptedInputStream(InputStream is,  Key key, String algorithm, ENCRYPT_MODE mode) {
		return null;
	}
	
}

package clframe;

import java.io.IOException;
import java.io.InputStream;

/***
 * Cipher InputStream that does no transformation on bytes!!!
 * @author Lubo
 *
 */
class MemoryInputStream extends CipherInputStream {

	MemoryInputStream(InputStream is) throws IOException {
		super(is, null, null);
	}

	@Override
	protected byte[] encryptDecrpyt(byte[] b) {
		return b;
	}
}

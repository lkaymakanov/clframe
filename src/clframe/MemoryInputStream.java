package clframe;

import java.io.IOException;
import java.io.InputStream;

class MemoryInputStream extends CipherInputStream {

	MemoryInputStream(InputStream is) throws IOException {
		super(is, null, null);
	}

	@Override
	protected byte[] encryptDecrpyt(byte[] b) {
		return b;
	}
}

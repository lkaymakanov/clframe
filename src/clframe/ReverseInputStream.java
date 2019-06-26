package clframe;

import java.io.IOException;
import java.io.InputStream;


class ReverseInputStream extends MemoryInputStream{
	ReverseInputStream(InputStream is) throws IOException {
		super(is);
	}

	@Override
	protected byte[] encryptDecrpyt(byte[] b) {
		int l = b.length;
		for(int i=0; i < l/2; i++) {
			byte c = b[i];
			b[i] = b[l-1-i];
			b[l-1-i] = c;
		}
		return b;
	}

}

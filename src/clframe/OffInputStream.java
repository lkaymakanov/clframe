package clframe;

import java.io.IOException;
import java.io.InputStream;

class OffInputStream  extends MemoryInputStream{
	
	private int off;
    
	OffInputStream(InputStream is, int off) throws IOException {
		super(is);
		this.off= off;
	}
	
	@Override
	protected byte[] encryptDecrpyt(byte[] b) {
		return StreamUtils.copy(b, off);
	}

}

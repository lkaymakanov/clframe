package clframe;

import java.io.UnsupportedEncodingException;
import java.security.Key;

/***
 * Ceaser key for encoding decoding based on CeaserEncoderDecoder!!! 
 * @author Lubo
 *
 */
class CeaserKey implements Key {

	/**
	 * 
	 */
	private static final long serialVersionUID = 890328104370616991L;
	private CeaserEncoderDecoder encdec;
	private String pass;

	@Override
	public String getAlgorithm() {
		return "CEASER";
	}

	@Override
	public String getFormat() {
		return null;
	}

	@Override
	public byte[] getEncoded() {
		try {
			return pass ==  null? null : pass.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	static CeaserKey createCeaserKey(String p) {
		CeaserKey k =  new  CeaserKey();
		k.encdec = new CeaserEncoderDecoder(p);
		k.pass = p;
		return k;
	}
	
	byte [] encode(byte [] bytes) {
		return encdec.encode(bytes);
	}
	
	byte [] decode(byte [] bytes) {
		return encdec.decode(bytes);
	}
}

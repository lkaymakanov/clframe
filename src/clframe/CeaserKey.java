package clframe;

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
		return null;
	}
	
	static Key createCeaserKey(String p) {
		CeaserKey k =  new  CeaserKey();
		k.encdec = new CeaserEncoderDecoder(p);
		return k;
	}
	
	byte [] encode(byte [] bytes) {
		return encdec.encode(bytes);
	}
	
	byte [] decode(byte [] bytes) {
		return encdec.decode(bytes);
	}
}

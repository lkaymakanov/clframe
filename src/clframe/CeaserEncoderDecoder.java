package clframe;


/**
 * Ceaser encoder decoder!!!
 * @author lubo
 *
 */
class CeaserEncoderDecoder  {
	protected int [] offsets;
	private boolean additive = true;
	
	protected CeaserEncoderDecoder(int[] offsets) {
		this.offsets = offsets;
	}
	
	protected CeaserEncoderDecoder(int[] offsets, boolean additive) {
		this.offsets = offsets;
		this.additive = additive;
	}
	
	protected CeaserEncoderDecoder(String pass) {
		this(pass, true);
	}
	
	protected CeaserEncoderDecoder(String pass, boolean additive) {
		offsets = new  int [pass.length()];
		for(int i = 0; i < pass.length(); i++){
			offsets[i] = (pass.codePointAt(i))& 0xffff;
		}
	}

	byte [] encode(byte [] bytes){
		return encode(bytes, bytes.length);
	}
	
	byte [] encode(byte [] bytes, int len){
		byte [] ret = new byte[len];
		for(int i = 0; i < len; i++){
			if(additive)  ret[i] = (byte)(bytes[i] + (offsets[(i % this.offsets.length)]));
			else ret[i] = (byte)(bytes[i] - (offsets[(i % this.offsets.length)]));
		}
		return ret;
	}
	
	byte [] decode(byte [] bytes){
		return decode(bytes, bytes.length);
	}
	
	byte [] decode(byte [] bytes, int len){
		byte [] ret = new byte[len];
		for(int i = 0; i < len; i++){
			if(additive) ret[i] = (byte)(bytes[i] - (offsets[(i % this.offsets.length)]));
			else ret[i] = (byte)(bytes[i] + (offsets[(i % this.offsets.length)]));
		}
		return ret;
	}
}
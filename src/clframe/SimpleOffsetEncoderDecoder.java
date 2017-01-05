package clframe;


/**
 * 
 * @author lubo
 *
 */
class SimpleOffsetEncoderDecoder {
	private static String defaultAlphabet = " ./\\~!@#$%^&*()_+{}[];:|',\"_1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private int [] offsets;
	private String alphabet;
	private int length ;
	private int scrableFactor = 10;    //factor for scrambling alphabet default 10

	/**
	 * Creates SimpleOffsetEncoderDecoder by an alphabet & pass phrase!!!
	 * @param alphabet
	 * @param passPhrase
	 */
	SimpleOffsetEncoderDecoder(String alphabet, String passPhrase){
		this(alphabet, passPhrase, passPhrase.length());
	}
	

	/***
	 * Creates SimpleOffsetEncoderDecoder by  pass phrase!!!
	 * @param passPhrase
	 */
	SimpleOffsetEncoderDecoder(String passPhrase){
		this(defaultAlphabet, passPhrase, passPhrase.length());
	}
	
	
	private SimpleOffsetEncoderDecoder(String alphabet, String passPhrase, int scrambleFactor){
		this.alphabet = alphabet;
		this.scrableFactor = scrambleFactor;
		this.length = alphabet.length();
		this.offsets = passPhraseToIntArray(this.alphabet, passPhrase);
		this.alphabet = scrambleAlphabet();
	}
	
	/***
	 * Scrambles alphabet by offsets as many times as pass length!
	 */
	private String scrambleAlphabet(){
		StringBuilder bd = new StringBuilder();
		String currentAlphabet = alphabet;
		for(int p=0; p < scrableFactor; p++){
			bd = new StringBuilder();
			char alphabetChars [] = new char [currentAlphabet.length()];
			for(int i=0; i < currentAlphabet.length(); i++){
				alphabetChars[i] = currentAlphabet.charAt(i);
			}
			int offsetsLength = offsets.length;
			for(int i=0; i < currentAlphabet.length(); i++){
				swap(alphabetChars, i, offsets[i % offsetsLength]);
			}
			bd.append(alphabetChars);
			currentAlphabet = bd.toString();
		}
		return currentAlphabet;
	}
	
	/***
	 * Swaps 2 chars in a char array!!!
	 * @param chars
	 * @param index1
	 * @param index2
	 */
	private void swap(char [] chars, int index1, int index2){
	   char c1=	chars[index1];  chars[index1] = chars[index2]; chars[index2] = c1;
	}
	
	/***
	 * Converts  string to array of integers!!!
	 * @param alphabet
	 * @param passPhrase
	 * @return
	 */
	private int [] passPhraseToIntArray(String alphabet, String passPhrase){
		int arr [] = new int [passPhrase.length()];
		int aplhlength = alphabet.length();
		for(int i = 0; i < passPhrase.length(); i++){
			arr[i] = (passPhrase.codePointAt(i) % aplhlength);
		}
		return arr;
	}
	
	/***
	 * Encodes a String
	 * @param s
	 * @return
	 */
	String encode(String s){
		if(s == null) return null;
		if(s.isEmpty()) return s;
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < s.length(); i++){
			char currentChar = s.charAt(i);
			if(charPosisitionInAlphabet(currentChar) < 0) sb.append(currentChar);
			else sb.append(encode(s.charAt(i), i));
		}
		return sb.toString();
	}
	
	/**
	 * Decodes a String
	 * @param s
	 * @return
	 */
	String decode(String s){
		if(s == null) return null;
		if(s.isEmpty()) return s;
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < s.length(); i++){
			char currentChar = s.charAt(i);
			if(charPosisitionInAlphabet(currentChar) < 0) sb.append(currentChar);
			else sb.append(decode(s.charAt(i), i));
		}
		return sb.toString();
	}
	
	/***
	 * Encodes a single character!!!
	 * @param c
	 * @param charPosInString
	 * @return
	 */
	private char encode(char c, int charPosInString){
		return alphabet.charAt(newCharPostion(charPosisitionInAlphabet(c), charPosInString, 1));
	}
	
	/***
	 * Decodes a single character!!!
	 * @param c
	 * @param charPosInString
	 * @return
	 */
	private char decode(char c, int charPosInString){
		return alphabet.charAt(newCharPostion(charPosisitionInAlphabet(c), charPosInString, -1));
	}
	
	/***
	 * Finds the char index in the alphabet -1 if char is not in the alphabet!!!
	 * @param c
	 * @return
	 */
	private int charPosisitionInAlphabet(char c){
		return alphabet.indexOf(c);
	}
	
	/***
	 * Finds the new char position based on the char position in alphabet & char position in encoded/decode string!!!  
	 * @param positionInAlphabet
	 * @param positionInString
	 * @param mult
	 * @return
	 */
	private int newCharPostion(int positionInAlphabet, int positionInString, int  mult){
	   return newCharPostion(positionInAlphabet,  (offsets[(positionInString % this.offsets.length)]* mult));
	}
	
	
	/**
	 * Finds the new char position by chat position in alphabet & offset!!!
	 * @param positionInAlphabet
	 * @param offset
	 * @return
	 */
	private int newCharPostion(int positionInAlphabet,  int offset){
	   return 	(positionInAlphabet + offset) >= length ? ((positionInAlphabet + offset) - length) : ((positionInAlphabet + offset) < 0 ? (positionInAlphabet + offset)  + length : (positionInAlphabet + offset) );
	}
	
	byte [] encode(byte [] bytes){
		return encode(bytes, bytes.length);
	}
	
	byte [] encode(byte [] bytes, int len){
		byte [] ret = new byte[len];
		for(int i = 0; i < len; i++){
			ret[i] = (byte)(bytes[i] + (offsets[(i % this.offsets.length)]));
		}
		return ret;
	}
	
	byte [] decode(byte [] bytes){
		return decode(bytes, bytes.length);
	}
	
	byte [] decode(byte [] bytes, int len){
		byte [] ret = new byte[len];
		for(int i = 0; i < len; i++){
			ret[i] = (byte)(bytes[i] - (offsets[(i % this.offsets.length)]));
		}
		return ret;
	}
}

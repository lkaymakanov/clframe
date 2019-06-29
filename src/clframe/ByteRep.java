package clframe;

import java.io.UnsupportedEncodingException;

/***
 * String  Representation of bytes!
 * @author Lubo
 *
 */
class ByteRep {

	 private static final char [] codes = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'}; 
	 private static final byte [] literals = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0xa, 0xb, 0xc, 0xd, 0xe, 0xf}; 
	
	 static String  rep(byte [] b) {
		 if(b == null) return null;
		 StringBuilder bd = new StringBuilder();
		 for(byte bb: b) {
			 bd.append(rep((byte)(bb >> 4)));
			 bd.append(rep(bb));
		 }
		 return bd.toString();
	 }
	 
	 private static char rep(byte b) {
		 b = (byte)(b & 0x000000000000000f);
		 return codes[b];
	 }
	 
	 private static byte mask(int b, int mask) {
		 return   (byte)(b & mask);
	 }
	 
	 private static byte merge(int b1, int literalIndex, boolean even) {
		 int quoter = literals[literalIndex];
		 if(even) {
			 return (byte)(quoter<<4);
		 }
		 return (byte)((mask(b1,  0xf0)) | quoter);
	 }
	 
	 
	 public static byte[] fromStringByteRep(String byteRep) {
		 if(byteRep == null) return null;
		 int l = byteRep.length();
		 if(l%2!=0) throw new RuntimeException("Odd symbols of string representation....");
		 byte res [] = new byte[l/2];
		 int byteIndex=0;
		 boolean even=true;
		 byte b = 0;
		 for(int i =0; i < l; i++) {
			 int c = byteRep.codePointAt(i);
			 byteIndex = i/2;
			 even = (i%2==0);
			 int offset = (Character.isLetter(c)? (87) : 48);
			 b = (byte) merge(b, c - offset, even);
			 res[byteIndex] = b;
		 }
		 return res;
	 }
	 
	 
	 
	 
	 public static void main(String []a) throws UnsupportedEncodingException {
		 byte [] off = new byte[]{6,8,9,8};
		 
		final String SLASH_NAME = "net/is_bg/ltfn/grao/controller/GlobalsInit.class";
		final String DOT_NAME = "net.is_bg.ltfn.grao.controller.GlobalsInit";
		final String APP_PROP = "/application.properties";
		 
		String ss = StreamUtils.mangle(APP_PROP, off);
		System.out.println(ss);
		System.out.println( StreamUtils.demangle(ss, off));
		 
		ss = StreamUtils.mangle(SLASH_NAME, off);
		System.out.println(ss);
		System.out.println( StreamUtils.demangle(ss, off));
		 
		ss = StreamUtils.mangle(DOT_NAME, off);
		System.out.println(ss);
		System.out.println( StreamUtils.demangle(ss, off));
		 
		 
		
		 
	 }
	 
}

package clframe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import clframe.Base64MeinePTool.Decoder;
import clframe.Base64MeinePTool.Encoder;

public class StreamUtils {
	private static final int ONE_MBYTE = 1024*1024;
	
	public static byte[] getByteRange() {
		byte[] b = new byte[256];
		for(int i = 0; i < 256; i ++) {
			b[i] = (byte)i;
		}
		return b;
	}
	
	public static IStreamConverter getPaddingIStreamConverter(InputStream orgstream, InputStream padding, boolean left) {
		return new PaddingInpustStreamConverter(orgstream, padding, left);
	}
	
	/***
     * merge 2 byte  arrays..
     * @param a1
     * @param a2
     * @return
     */
    public static byte[] merge(byte[] a1, int a1Size, byte [] a2, int a2Size) {
    	if(a1==null && a2 ==null) return null;
    	ByteArrayOutputStream bos =new ByteArrayOutputStream();
    	
    	if(a1==null) { for(int j=0; j < a2Size;j++) {bos.write(a2[j]); }   return bos.toByteArray(); }
    	if(a2==null) { byte [] merge = new byte[a1Size]; for(int j=0; j < a1Size;j++) {merge[j] = a1[j]; }   return merge; }
    	
    	for(int i=0; i < a1Size; i++) {bos.write(a1[i]);}
    	for(int j=0; j < a2Size;) {bos.write(a2[j++]); }
    	return bos.toByteArray();
    }
    
    public static byte [] reverse (byte [] arr) {
    	if(arr ==null) return arr;
    	byte []res = new byte [arr.length];
    	int j=0;
    	for(int i = arr.length-1; i >=0; i--) {
    		res[j++] = arr[i];
    	}
    	return res;
    }
    
    
    /**
     * Converts inputStream to byte array!!
     * @param is
     * @return
     * @throws IOException
     */
    public static byte [] toByteArray(InputStream is) throws IOException {
    	int size = ONE_MBYTE;
    	if(is ==null) return null;
    	byte [] b = new byte[size];
    	byte [] engine = null;byte[] ba= null;
    	int i = 0;
    	while((i = is.read(b)) > 0) {
    		ba = merge(engine, (engine == null ? 0: engine.length), b, i);
    		engine = ba;
    	}
    	is.close();
		return engine;
    }
    
    
    public static String  toBase64Txt(InputStream is) throws IOException{
    	Encoder endec =  Base64MeinePTool.getEncoder();
    	return endec.encodeToString(toByteArray(is));
    }
    
    public static byte [] fromBase64Txt(String txt) throws IOException{
    	Decoder d =  Base64MeinePTool.getDecoder();
    	return d.decode(txt);
    }
    
    public static String  toBase64Txt(byte []arr) throws IOException{
    	Encoder endec =  Base64MeinePTool.getEncoder();
    	return endec.encodeToString(arr);
    }
    
    /***
     * Converts byte array to input stream!!!
     * @param bytes
     * @return
     */
    public static ByteArrayInputStream toInputStream(byte [] bytes) {
    	ByteArrayInputStream is = new ByteArrayInputStream(bytes);
    	return is;
    }
    
    /**
     * Merges 2 input streams to byte array!!!
     * @param is
     * @param is2
     * @return
     * @throws IOException
     */
    public static byte[] merge(InputStream is, InputStream is2) throws IOException {
    	byte [] ar1 = toByteArray(is);
    	int l1 = ar1 == null ? 0 : ar1.length;
    	byte [] ar2 = toByteArray(is2);
    	int l2 = ar2 == null ? 0 : ar2.length;
    	return  merge(ar1, l1, ar2, l2);
    }
    
    /**
     * Returns a fresh copy of  byte array!
     * @param a
     * @return
     */
    public static byte [] copy(byte [] a) {
    	if(a == null) return null;
    	byte [] b = new byte[a.length];
    	for(int i =0; i < a.length; i++) {
    		b[i]=a[i];
    	}
    	return b;
    }
    
    
    public static byte [] copy(byte [] a, int loffset) {
    	if(a == null) return null;
    	byte [] b = new byte[a.length - loffset];
    	int j = 0;
    	for(int i = loffset; i < a.length; i++) {
    		b[j++]=a[i];
    	}
    	return b;
    }
   
    
    public static byte [] copy(byte [] a, int loffset, int roffset) {
    	if(a == null) return null;
    	if(loffset + roffset >= a.length) return null;
    	byte [] b = new byte[a.length - (loffset + roffset)];
    	int j = 0;
    	for(int i = loffset; i < (a.length - roffset); i++) {
    		b[j++]=a[i];
    	}
    	return b;
    }
    
    
    
    public static byte [] copyNBytes(byte [] a, int loffset, int count) {
    	if(a == null) return null;
    	byte [] b = new byte[a.length - (loffset)];
    	int j = 0;
    	for(int i = loffset; i < (loffset + count); i++) {
    		b[j++]=a[i];
    	}
    	return b;
    }
    
	/*
	 * public static byte [] reverseCopy(byte [] b) { if(b == null) return null; int
	 * l = b.length; byte [] a = new byte[b.length]; for(int i=0; i < l; i++) {
	 * a[i]=b[i]; } return a; }
	 */
    
    public static byte [] xor(byte [] b, byte xor) {
    	if(b == null) return b;
    	byte [] newArr = new byte[b.length];
    	int i =0;
    	for(byte bb:b) {
    		newArr[i++] = (byte)(bb ^ xor); 
    	}
    	return newArr;
    }
    
    public static byte [] xor(byte [] b, byte [] xor) {
    	if(b == null) return b;
    	byte [] newArr = new byte[b.length];
    	int i =0;
    	for(byte bb:b) {
    		newArr[i++] = (byte)(bb ^ xor[i % xor.length]); 
    	}
    	return newArr;
    }
    
    public static String mangle(String s,  byte [] off) throws UnsupportedEncodingException {
    	byte [] b = mangleB(s.getBytes("UTF-8"), off);
    	return ByteRep.rep(b);
    }
    
    public static String demangle(String s,  byte [] off) throws UnsupportedEncodingException {
    	byte [] b =  demangleB(ByteRep.fromStringByteRep(s), off);
    	return new String(b);
    }
    
    private static byte[] mangleB(byte [] b, byte [] off)  {
    	CeaserEncoderDecoder d= new CeaserEncoderDecoder(widen(off));
    	return d.encode(b);
    }
    
    
    private static byte []  demangleB(byte[] b, byte [] off)  {
    	CeaserEncoderDecoder d = new CeaserEncoderDecoder(widen(off));
    	return d.decode(b);
    }
    
    
    public static InputStream copy(InputStream is) throws IOException {
    	if(is == null) return is;
    	return new MemoryInputStream(StreamUtils.toInputStream(StreamUtils.copy(StreamUtils.toByteArray(is))));
    }
    
    /**
     * Converts byte [] to ByteArrayOutputSream
     * @param bytes
     * @return
     * @throws IOException
     */
    public static ByteArrayOutputStream toOutputStream(byte [] bytes) throws IOException {
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
    	os.write(bytes);
    	os.close();
    	return os;
    }
    
    /**Widens byte array to int array!*/
    public static int [] widen(byte [] b) {
    	if(b == null) return null;
    	int [] cpy = new int [b.length]; 
    	for(int i=0; i < b.length; i ++) {
    		cpy[i] = b[i];
    	}
    	return cpy;
    }
    
    
    public static ByteArrayInputStream iFileIStreamToByteArrayInputStream(FileInputStream is, int offset) {
    	List<Byte> fileBytes = new ArrayList<Byte>();
    	try {
			is.getChannel().position(offset);
			int b = is.read();
			while(b!=-1) {
				fileBytes.add((byte)b);
				b = is.read();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
    	
    	byte [] buffer = new byte[fileBytes.size()];
    	int i = 0;
    	for(Byte b: fileBytes) {
    		buffer[i++] = b;
    	}
    	return new ByteArrayInputStream(buffer);
    }
}

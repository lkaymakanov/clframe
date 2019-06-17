package clframe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {
	private static final int ONE_MBYTE = 1024*1024;
	/***
     * merge 2 byte  arrays..
     * @param a1
     * @param a2
     * @return
     */
    public static byte[] merge(byte[] a1, int a1Size, byte [] a2, int a2Size) {
    	if(a1==null && a2 ==null) return null;
    	if(a1==null) { byte [] merge = new byte[a2Size]; for(int j=0; j < a2Size;j++) {merge[j] = a2[j]; }   return merge; }
    	if(a2==null) { byte [] merge = new byte[a1Size]; for(int j=0; j < a1Size;j++) {merge[j] = a1[j]; }   return merge; }
    	byte [] merge = new byte[a1Size + a2Size];
    	int i = 0;
    	for(; i < a1Size; i++) {merge[i] = a1[i];}
    	for(int j=0; j < a2Size;) {merge[i++] = a2[j++]; }
    	return merge;
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
}

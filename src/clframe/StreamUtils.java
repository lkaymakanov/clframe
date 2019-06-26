package clframe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
    
    public static byte [] copy(byte [] a) {
    	if(a == null) return null;
    	byte [] b = new byte[a.length];
    	for(int i =0; i < a.length; i++) {
    		b[i]=a[i];
    	}
    	return b;
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

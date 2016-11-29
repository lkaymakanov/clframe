package clframe;

/**
 * Class bytes wrapper used by MemoryClass loader!!!
 * @author lubo
 *
 */
class ClassInfo {
  
	byte []  bytes;
	Class clazzz;
	String originalName;
	
	public ClassInfo(byte []  bytes, String originalName){
		this.bytes = bytes;
		this.originalName = originalName;
	}

}

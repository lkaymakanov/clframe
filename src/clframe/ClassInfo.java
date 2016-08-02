package clframe;

/**
 * Class bytes wrapper used by MemoryClass loader!!!
 * @author lubo
 *
 */
class ClassInfo {
  
	byte []  bytes;
	Class clazzz;
	
	public ClassInfo(byte []  bytes){
		this.bytes = bytes;
	}

}

package clframe;

import java.io.Serializable;

/**
 * Class bytes wrapper used by MemoryClass loader!!!
 * @author lubo
 *
 */
class ClassBytes implements Serializable {
  
	/**
	 * 
	 */
	private static final long serialVersionUID = -5728308783952114837L;
	public byte []  bytes;
	
	public ClassBytes(byte []  bytes){
		this.bytes = bytes;
	}
}

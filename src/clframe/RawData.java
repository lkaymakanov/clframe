package clframe;

import java.io.Serializable;

class RawData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5630289562120133798L;
	private FileNamePath name;
	byte [] bytes;
	
	RawData(byte[] byteArray, FileNamePath name) {
		// TODO Auto-generated constructor stub
		this.bytes = byteArray;
		this.name = name;
	}
	
	public FileNamePath getName() {
		return name;
	}
	
}

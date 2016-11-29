package clframe;

class RawData {
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

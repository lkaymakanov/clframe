package clframe;

/**
 * Class bytes wrapper used by MemoryClass loader!!!
 * @author lubo
 *
 */
class ClassInfo {
  
	byte []  bytes;
	Class clazzz;
	private FileNamePath originalName;
	private FileNamePath name;
	
	public ClassInfo(byte []  bytes, FileNamePath name, FileNamePath originalName){
		this.bytes = bytes;
		this.name = name;
		this.originalName = originalName;
	}

	public FileNamePath getOriginalName() {
		return originalName;
	}

	public FileNamePath getName() {
		return name;
	}

}
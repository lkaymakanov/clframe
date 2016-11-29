package clframe;

class ResourceInfo {
	byte []  bytes;
	String resourceName;
	String originalName;
	
	
	public ResourceInfo(byte []  bytes, String name, String originalName){
		this.bytes = bytes;
		this.resourceName = name;
		this.originalName = originalName;
	}
}

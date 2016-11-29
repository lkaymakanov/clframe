package clframe;

class ResourceInfo {
	byte []  bytes;
	private FileNamePath resourceName;
	private FileNamePath originalName;
	
	
	public ResourceInfo(byte []  bytes, FileNamePath resourceName, FileNamePath originalName){
		this.bytes = bytes;
		this.resourceName = resourceName;
		this.originalName = originalName;
	}

	public FileNamePath getResourceName() {
		return resourceName;
	}


	public FileNamePath getOriginalName() {
		return originalName;
	}
	
}

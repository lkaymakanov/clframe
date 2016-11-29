package clframe;

import java.io.Serializable;

class ResourceInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2511942196545444025L;
	byte []  bytes;
	private FileNamePath resourceName;
	private FileNamePath originalName;
	
	
	ResourceInfo(byte []  bytes, FileNamePath resourceName, FileNamePath originalName){
		this.bytes = bytes;
		this.resourceName = resourceName;
		this.originalName = originalName;
	}

	FileNamePath getResourceName() {
		return resourceName;
	}


	FileNamePath getOriginalName() {
		return originalName;
	}
	
}

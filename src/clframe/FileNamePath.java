package clframe;

import java.io.Serializable;

/**
 * A file name & path to the file name!!!
 * @author lubo
 *
 */
class FileNamePath implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8975810387985498303L;
	private String [] path;
	private String fileName;
	private boolean addSeparator;
	
	
	FileNamePath(String [] path, String fileName){
		this(path, fileName, true);
	}
	
	FileNamePath(String [] path, String fileName, boolean addSeparator){
		this.path = path;
		this.fileName = fileName;
		this.addSeparator = addSeparator;
	}

	String[] getPath() {
		return path;
	}


	String getFileName() {
		return fileName;
	}
	
	String getFullName(){
		String s = getPathPrivate();
		if(addSeparator) s+="/";
		return  s +  fileName;
	}
	
	private  String getPathPrivate(){
		if(path == null || path.length == 0){
			return "";
		}
		String res = "";
		int i = 0;
		for(String s : path) {
			res+= (i > 0) ? "/"+ s :  s;
			i++;
		}
		return res;
	}
	
	private static String [] shallowCoppy(String [] arr, int len){
		if(len > arr.length) len = arr.length;
		String [] a = new String[len];
		for(int i = 0; i < len;i++){
			a[i] = arr[i];
		}
		return a;
	}
	
	
	static FileNamePath fromFileNamePath(String fileNamePath){
		String [] path = fileNamePath.split("/");
		String fName = fileNamePath;
		if(path != null && path.length > 1){
			fName =  path[path.length-1];
			path = shallowCoppy(path, path.length-1);
		}else{
			path = new String[]{""};
		}
		return new FileNamePath(path, fName);
	}
	
}

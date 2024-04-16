package clframe;

import java.io.File;
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
		if(!s.equals("") && addSeparator) s+="/";
		return  s +  fileName;
	}
	
	private  String getPathPrivate(){
		return getPathPrivate(path, "/");
	}
	
	private static String getPathPrivate(String path [], String delimiter){
		if(path == null || path.length == 0){
			return "";
		}
		String res = "";
		int i = 0;
		for(String s : path) {
			res+= (i > 0) ? delimiter + s :  s;
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
	
    static void mkDir(String parent, String [] mkdirpath, String fileSeparator){
		if(parent == null || mkdirpath == null) return;
		String fullpath = parent.equals("") ? getPathPrivate(mkdirpath, fileSeparator) : parent + "\\" + getPathPrivate(mkdirpath, fileSeparator);
		File f = new File(fullpath);
		boolean exists = f.exists();
		if(!exists ){
			if(mkdirpath.length > 1) {mkDir(parent, shallowCoppy(mkdirpath, mkdirpath.length -1), fileSeparator);}
			f.mkdir();
			System.out.println("creating dir " + f.getAbsolutePath());
		}
	}
	
    
	@Override
	public String toString() {
		return getFullName(); //super.toString();
	}
	
	
	public static void main(String [] args){
	   	String p [] = new String [] {"d1", "d2", "d3","d4", "d5", "d6","d7", "d8", "d9","d10", "d11", "d12"};
		//mkDir("D:", p, "\\");
		
	   System.out.println(	new File("D:\\mydir\\mydir").exists());
		
	}
}

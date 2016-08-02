package clframe;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

/***
 * Raw functionality & resources needed by the GEEngine!!!!
 * @author lubo
 *
 */
class GEEngineData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2112408647982050770L;
	Map<String, ClassInfo> classMap = new Hashtable<String, ClassInfo>();
	Map<String, ResourceInformation> resources = new Hashtable<String, ResourceInformation>();
	Properties properties;
	ClassLoader engineClassLoader;
	IGEEngine enigine;
	
}
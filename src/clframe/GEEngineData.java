package clframe;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

/***
 * Raw functionality & resources needed by the GEEngine!!!!
 * @author lubo
 *
 */
class GEEngineData implements IGEEngineData{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2112408647982050770L;
	private Map<String, ClassInfo> classMap = new Hashtable<String, ClassInfo>();
	private Map<String, ResourceInfo> resources = new Hashtable<String, ResourceInfo>();
	private Map<String, RawData> rawData = new Hashtable<String, RawData>();
	private Properties properties;
	private transient ClassLoader engineClassLoader;
	private transient IGEEngine enigine;
	
	GEEngineData(){
		
	}
	
	@Override
	public Map<String, ClassInfo> getClassMap() {
		// TODO Auto-generated method stub
		return classMap;
	}
	@Override
	public Map<String, ResourceInfo> getResources() {
		// TODO Auto-generated method stub
		return resources;
	}
	@Override
	public Properties getProperties() {
		// TODO Auto-generated method stub
		return properties;
	}
	@Override
	public ClassLoader getEngineClassLoader() {
		// TODO Auto-generated method stub
		return engineClassLoader;
	}
	@Override
	public IGEEngine getEnigine() {
		// TODO Auto-generated method stub
		return enigine;
	}
	@Override
	public void setClassMap(Map<String, ClassInfo> m) {
		// TODO Auto-generated method stub
		classMap = m;
	}
	@Override
	public void setResources(Map<String, ResourceInfo> r) {
		// TODO Auto-generated method stub
		resources = r;
	}
	@Override
	public void setProperties(Properties p) {
		// TODO Auto-generated method stub
		properties = p;
	}
	@Override
	public void setEngineClassLoader(ClassLoader c) {
		// TODO Auto-generated method stub
		engineClassLoader = c;
	}
	@Override
	public void setEnigine(IGEEngine e) {
		// TODO Auto-generated method stub
		enigine = e;
	}

	@Override
	public Map<String, RawData> getRawData() {
		// TODO Auto-generated method stub
		return rawData;
	}

	@Override
	public void setRowData(Map<String, RawData> rowData) {
		// TODO Auto-generated method stub
		this.rawData = rowData;
	}

	
}
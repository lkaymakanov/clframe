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
	private Map<String,Properties> properties;
	private transient ClassLoader engineClassLoader;
	private transient IGEEngine enigine;
	
	GEEngineData(){
		
	}
	
	@Override
	public Map<String, ClassInfo> getClassMap() {
		return classMap;
	}
	@Override
	public Map<String, ResourceInfo> getResources() {
		return resources;
	}
	@Override
	public Map<String, Properties> getProperties() {
		return properties;
	}
	@Override
	public ClassLoader getEngineClassLoader() {
		return engineClassLoader;
	}
	@Override
	public IGEEngine getEnigine() {
		return enigine;
	}
	@Override
	public void setClassMap(Map<String, ClassInfo> m) {
		classMap = m;
	}
	@Override
	public void setResources(Map<String, ResourceInfo> r) {
		resources = r;
	}
	@Override
	public void setProperties(Map<String,Properties> p) {
		properties = p;
	}
	@Override
	public void setEngineClassLoader(ClassLoader c) {
		engineClassLoader = c;
	}
	@Override
	public void setEngine(IGEEngine e) {
		enigine = e;
	}

	@Override
	public Map<String, RawData> getRawData() {
		return rawData;
	}

	@Override
	public void setRowData(Map<String, RawData> rowData) {
		this.rawData = rowData;
	}

	@Override
	public Properties getEngineProperties() {
		return properties.get(ClFrameConst.ENGINE_PROP_FILE_NAME);
	}
	
}
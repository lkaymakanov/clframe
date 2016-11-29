package clframe;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;


interface IGEEngineData extends Serializable {
	 Map<String, ClassInfo> getClassMap();
	 Map<String, ResourceInfo> getResources();
	 Properties getProperties();
	 ClassLoader getEngineClassLoader();
	 IGEEngine getEnigine();
	 Map<String, RawData> getRowData();
	 
	 void setClassMap(Map<String, ClassInfo> m);
	 void setResources(Map<String, ResourceInfo> r);
	 void setProperties(Properties p);
	 void setEngineClassLoader(ClassLoader c);
	 void setEnigine(IGEEngine e);
	 void setRowData(Map<String, RawData> rowData);
}

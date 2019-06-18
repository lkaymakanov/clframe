package clframe;

import java.util.Properties;

interface IGEEngineData extends IModule{
	/* Map<String, ClassInfo> getClassMap();
	 Map<String, ResourceInfo> getResources();
	 Properties getProperties();*/
	 ClassLoader getEngineClassLoader();
	 IGEEngine getEnigine();
	 Properties getEngineProperties();
	 
	/* void setClassMap(Map<String, ClassInfo> m);
	 void setResources(Map<String, ResourceInfo> r);
	 void setProperties(Properties p);*/
	 void setEngineClassLoader(ClassLoader c);
	 void setEngine(IGEEngine e);
}

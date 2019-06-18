package clframe;

import java.util.Map;
import java.util.Properties;


/***
 * Interface representing module data!!
 * @author Lubo
 *
 */
interface IModule extends IMapRawData, IModuleHandle{
	 Map<String, ClassInfo> getClassMap();
	 Map<String, ResourceInfo> getResources();
	 Map<String, Properties> getProperties();
	 //ClassLoader getEngineClassLoader();
	 //IGEEngine getEnigine();
	 
	 void setClassMap(Map<String, ClassInfo> m);
	 void setResources(Map<String, ResourceInfo> r);
	 void setProperties(Map<String, Properties> p);
	 //void setEngineClassLoader(ClassLoader c);
	 //void setEnigine(IGEEngine e);
}


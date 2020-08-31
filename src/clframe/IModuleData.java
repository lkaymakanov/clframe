package clframe;

import java.util.Map;
import java.util.Properties;


/***
 * Interface representing module data!!
 * @author Lubo
 *
 */
interface IModuleData extends IMapRawData, IModuleHandle{
	 Map<String, ClassInfo> getClassMap();
	 Map<String, ResourceInfo> getResources();
	 Map<String, Properties> getProperties();
	 
	 void setClassMap(Map<String, ClassInfo> m);
	 void setResources(Map<String, ResourceInfo> r);
	 void setProperties(Map<String, Properties> p);
}


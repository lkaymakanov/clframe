package clframe;

import java.io.Serializable;
import java.util.Map;

interface IMapRawData extends  Serializable {
	Map<String, RawData> getRawData();
}

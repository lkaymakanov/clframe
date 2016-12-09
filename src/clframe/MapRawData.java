package clframe;

import java.util.HashMap;
import java.util.Map;

class MapRawData implements IMapRawData {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6715821628447619887L;
	Map<String, RawData>  map = new HashMap<String, RawData>();
	
	
	@Override
	public Map<String, RawData> getRawData() {
		// TODO Auto-generated method stub
		return map;
	}
	
	

}

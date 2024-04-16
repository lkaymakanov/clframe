package clframe;
class UrlClLoader extends GEEngineCl{
	
	UrlClLoader(String  streamUrl, ClassLoader parent) {
		super(loadStream(streamUrl), parent);
	}
	

	private static IModuleData loadStream(String streamUrl) {
		return null;
	}
	
}

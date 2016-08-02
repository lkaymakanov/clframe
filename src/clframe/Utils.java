package clframe;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class Utils {

	/***
	 * Load properties form input stream!!!
	 * @param inStream
	 * @return
	 */
	static Properties loadproperties(InputStream inStream){
		Properties prop = new Properties();
		try {
			prop.load(inStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prop;
	}
	
}

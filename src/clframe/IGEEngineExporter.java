package clframe;


/***
 * Interface for exporting engine data!
 * @author Lubo
 *
 */
public interface IGEEngineExporter {
	/**Export classes**/
	public void exportClasses();
	/**Export resources*/
	public void exportResources();
	/***Export raw data*/
	public void exportRawData();
	/**Export engine data**/
	public void exportEngineData();
}

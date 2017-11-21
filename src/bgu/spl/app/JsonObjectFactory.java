package bgu.spl.app;
// TODO: Auto-generated Javadoc


/**
 * A factory for creating JsonObject objects, created to connect between the JSON file and our code
 */
public class JsonObjectFactory {
	
	/** The initial storage. */
	public ShoeStorageInfo[] initialStorage;
	
	/** The services. */
	public JsonServices services;
}

class JsonServices{
	public WebsiteClientService[] customers;
	public int sellers;
	public int factories;
	public ManagementService manager;
	public TimeService time;
	
	

}

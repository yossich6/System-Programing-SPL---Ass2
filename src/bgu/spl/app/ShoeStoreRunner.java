package bgu.spl.app;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;


/**
 * The Class ShoeStoreRunner- The main of the project
 */
public class ShoeStoreRunner {
	public static Logger log=Logger.getLogger("logger");


	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		//creating singleton for store
		Store store=Store.getInstance();
		//reading from JSON
		Gson gson=new Gson();
		JsonReader reader=new JsonReader(new FileReader(args[0]));
		JsonObjectFactory infoJson=gson.fromJson(reader, JsonObjectFactory.class);
		reader.close();
		
		ArrayList<Thread> threads=new ArrayList<Thread>();
		store.load(infoJson.initialStorage);
		WebsiteClientService[] customers=infoJson.services.customers;
		ShoeFactoryService[] factories=new ShoeFactoryService[infoJson.services.factories];
		SellingService[] sellers=new SellingService[infoJson.services.sellers];
		CountDownLatch theMainLatch = new CountDownLatch (customers.length+factories.length+sellers.length+1);
		ManagementService manager=infoJson.services.manager;
		manager.setLatch(theMainLatch);
		TimeService timer=infoJson.services.time;
		timer.setLatch(theMainLatch);
		Thread managerThread=new Thread(manager);
		Thread timerThread=new Thread(timer);
		threads.add(timerThread);
		threads.add(managerThread);


		
		for(int i=1; i<= factories.length; i++){
			factories[i-1]=new ShoeFactoryService("Factory "+ i);
			factories[i-1].setLatch(theMainLatch);
			threads.add(new Thread(factories[i-1]));
		}
		for(int i=1; i<= sellers.length; i++){
			sellers[i-1]=new SellingService("Seller "+ i);
			sellers[i-1].setLatch(theMainLatch);
			threads.add(new Thread(sellers[i-1]));
		}
		for(int i=0; i< customers.length; i++){
			customers[i].setLatch(theMainLatch);
			threads.add(new Thread(customers[i]));
		}

		for(int i=0; i<threads.size(); i++){
			threads.get(i).start();
			
		}
		for(int i=0; i<threads.size(); i++){
			
			threads.get(i).join();
			
			
		}

	        store.print();
	}
}




















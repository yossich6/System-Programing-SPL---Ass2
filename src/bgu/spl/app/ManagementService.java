package bgu.spl.app;

import java.util.HashMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import bgu.spl.mics.*;

/**
 * This micro-service can add discount to shoes in the store and send
 * NewDiscountBroadcast to notify clients about them
 */
public class ManagementService extends MicroService {

	/** The discount schedule. */
	private List<DiscountSchedule> discountSchedule;
	
	/** The tick. */
	private int tick;
	
	/** The ordered. */
	private Map< String, DataStructureFactoryRequests> ordered = new HashMap<>();
	
	/** The store. */
	private Store store=Store.getInstance();
	
	/**
	 * Instantiates a new management service.
	 */
	public ManagementService(){
		super("manager");
		tick=0;
	}


	/**
	 * Instantiates a new management service.
	 *
	 * @param name the name
	 * @param discountSchedule the discount schedule
	 */
	public ManagementService(String name, List<DiscountSchedule>  discountSchedule ) {
		super("manager");
		this.discountSchedule=discountSchedule;
		tick=0;
	}

	/**
	 * Gets the ds.
	 *
	 * @return the ds
	 */
	public List<DiscountSchedule> getDs() {
		return discountSchedule;
	}

	/**
	 * Factory answer.
	 *
	 * @param req the req
	 * @param ans the ans
	 */
	private void factoryAnswer(RestockRequest req,Receipt ans)
	{
		if(ans!=null) //if there is any shoes
		{
			ShoeStoreRunner.log.info("The manger got the "+req.getShoeType()+" from the factory, the recipt is:");
			ans.print();
			store.file(ans);
			int factoryMade=ans.getAmountSold();
			int requestedAmount=ordered.get(req.getShoeType()).getQ().size();
			int storeAmount=0;
			if(factoryMade>requestedAmount){
				storeAmount=factoryMade-requestedAmount;
			}else{
				requestedAmount=factoryMade;
			}
			if(!ordered.get(req.getShoeType()).getQ().isEmpty())
			{
				for(int i=0;i<requestedAmount;i++)
				{
					complete(ordered.get(req.getShoeType()).getQ().remove(), true);
					ordered.get(req.getShoeType()).lessAmount(1);
				}
				//ShoeStoreRunner.log.info("Before "); store.print();
				store.add(req.getShoeType(), storeAmount);
				//ShoeStoreRunner.log.info("After "); store.print();

			}
		}
		else
		{
			ShoeStoreRunner.log.info("The manager got answer that there are no factories that can make the "+req.getShoeType());
			for(RestockRequest rr : ordered.get(req.getShoeType()).getQ()){
				complete(rr,false);
				ordered.get(req.getShoeType()).getQ().remove();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see bgu.spl.mics.MicroService#initialize()
	 */
	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, req -> {
			if(req.getTick()==-1){	
				terminate();
			}else{
				tick=req.getTick();
				for(int i=0; i<discountSchedule.size(); i++){
					if(discountSchedule.get(i).getTick()==tick){
						ShoeStoreRunner.log.info("Manager sending discount for: "+ discountSchedule.get(i).getType());
						sendBroadcast(new NewDiscountBroadcast(discountSchedule.get(i).getType(), discountSchedule.get(i).getAmount()));
						store.addDiscount(discountSchedule.get(i).getType(), discountSchedule.get(i).getAmount());
					}
				}
			}});
		subscribeRequest(RestockRequest.class, req->{
			ShoeStoreRunner.log.info("The manager got restock request for "+req.getShoeType());
			if(ordered.get(req.getShoeType()) == null){ //if no one has ordered the shoes yet		
				ordered.put(req.getShoeType(), new DataStructureFactoryRequests(new LinkedList(), tick%5+1));
				ordered.get(req.getShoeType()).setQ(req);
				ShoeStoreRunner.log.info("The manager sending restock request for the factory, trying to get "+req.getShoeType());
				boolean reqAns=sendRequest(new ManufacturingOrderRequest(req.getShoeType(),tick%5+1,tick), ans->{
					factoryAnswer(req,ans);
				});
				if (!reqAns)
				{
					ShoeStoreRunner.log.info("The manager got answer that there are no factories that can make the "+req.getShoeType());
					for(RestockRequest rr : ordered.get(req.getShoeType()).getQ()){
						complete(rr,false);
						ordered.get(req.getShoeType()).getQ().remove();
					}		
				}
			}else{
				
				if(ordered.get(req.getShoeType()).getQ().size()<=ordered.get(req.getShoeType()).getAmount()) //if there are already enough orders for this shoe
				{
					
					ShoeStoreRunner.log.info("The manger see he have enough orders for "+req.getShoeType()+" and he doesnt need to send another Manfuactury request for these shoes");
					ordered.get(req.getShoeType()).setQ(req);
				}
				else //if not get another shoes from the factory
				{
					
					ShoeStoreRunner.log.info("The manger see he have to much orders for "+req.getShoeType()+" and he needs to send another Manfuactury request for these shoes");
					ordered.get(req.getShoeType()).addAmount(tick%5+1);
					ordered.get(req.getShoeType()).setQ(req);
					boolean reqAns=sendRequest(new ManufacturingOrderRequest(req.getShoeType(),tick%5+1,tick), ans->{
						factoryAnswer(req,ans);
					});
					if (!reqAns)
					{
						ShoeStoreRunner.log.info("The manager got answer that there are no factories that can make the "+req.getShoeType());
						
						complete(req,false);
							
						
						
					}
				}
			}
		});
		theLatch.countDown();
	}
}

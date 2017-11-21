package bgu.spl.app;


//import java.util.logging.*;

import bgu.spl.app.Store.BuyResult;
import bgu.spl.mics.*;



// TODO: Auto-generated Javadoc
/**
 * This micro-service handles PurchaseOrderRequest.
 */
public class SellingService extends MicroService {

	
	/** The store. */
	private Store store=Store.getInstance();
	
	/** The tick. */
	private int tick;
	//Logger log=new Logger(String);

	/**
	 * Instantiates a new selling service.
	 *
	 * @param name the name
	 */
	public SellingService(String name) {
		super(name);
		tick=0;
		// TODO Auto-generated constructor stub
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

				tick=req.getTick();}});	 

		subscribeRequest(PurchaseOrderRequest.class, req -> {
			ShoeStoreRunner.log.info(getName() +" got request from "+req.getSenderName()+" wanting to buy "+req.getShoeType());
			bgu.spl.app.Store.BuyResult res=store.take(req.getShoeType(), req.isDiscount());
			if(res.equals(BuyResult.REGULAR_PRICE))
			{
				//store.takeShoe(req.getShoeType(),false);
				Receipt rec=new Receipt(getName(),req.getSenderName(),req.getShoeType(),false,tick,req.getTick(),1);
				ShoeStoreRunner.log.info(this.getName()+" sending the "+req.getShoeType()+" in normal price to "+req.getSenderName());
				store.file(rec);
				complete(req,rec);
			}
			if(res.equals(BuyResult.DISCOUNTED_PRICE))
			{
				//store.takeShoe(req.getShoeType(),true);
				Receipt rec=new Receipt(getName(),req.getSenderName(),req.getShoeType(),true,tick,req.getTick(),1);
				ShoeStoreRunner.log.info(this.getName()+" sending the "+req.getShoeType()+" in discount price to "+req.getSenderName());
				store.file(rec);
				complete(req,rec);
			}
			if(res.equals(BuyResult.NOT_ON_DISCOUNT))
			{
				ShoeStoreRunner.log.info(getName()+" need to send that the shoes couldnt be bought because there is no discount on "+req.getShoeType());
				complete(req,null);
			}
			if(res.equals(BuyResult.NOT_IN_STOCK))
			{
				ShoeStoreRunner.log.info(this.getName()+" sending 'not in stock' to manager because we dont have "+req.getShoeType());
				RestockRequest restock=new RestockRequest(req.getShoeType(),tick);
				boolean reqAns=sendRequest(restock, ans->{ 
					if(ans==false)
					{
						ShoeStoreRunner.log.info(getName()+ " got from the manager that "+req.getShoeType()+" can not be manufactured, and sending that to "+req.getSenderName());
						complete(req,null);
					}
					else
					{
						ShoeStoreRunner.log.info(getName()+" got the "+ req.getShoeType()+" that is now in stock and sending it to "+req.getSenderName()+" in normal price");	
						Receipt rec=new Receipt(getName(),req.getSenderName(),req.getShoeType(),false,tick,req.getTick(),1);
						store.file(rec);
						complete(req,rec);
		
					}
				});
				if(!reqAns)
				{
					ShoeStoreRunner.log.info(getName()+ " got from the manager that "+req.getShoeType()+" can not be manufactured, and sending that to "+req.getSenderName());
					complete(req,null);
				}
			}          
		});
		theLatch.countDown();

	}




}

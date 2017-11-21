package bgu.spl.app;
import java.util.*;
import bgu.spl.mics.MicroService;
// TODO: Auto-generated Javadoc

/**
 * This micro-service describes a shoe factory that manufacture shoes for the store.
 */
public class ShoeFactoryService extends MicroService {
	
	/** The tick. */
	private int tick;
	
	/** The wcounter. */
	private int wcounter;
	
	/** The waitq. */
	private Queue<ManufacturingOrderRequest> waitq;
	
	/** The working. */
	private boolean working;
	
	/** The curr. */
	private ManufacturingOrderRequest curr;

	/**
	 * Instantiates a new shoe factory service.
	 *
	 * @param name the name
	 */
	public ShoeFactoryService(String name) {
		super(name);
		tick=0;
		wcounter=0;
		waitq=new LinkedList<ManufacturingOrderRequest>();
		working=false;
		curr=null;
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
				if(wcounter!=0) //the factory is working on a shoe so a tick should be passed
				{
					ShoeStoreRunner.log.info(getName()+" manufacturing the " +curr.getShoeName());
					wcounter--;
				}
				else
				{
					if((wcounter==0)&&(working)) //the case that the factory just finished working on a shoe
					{
						ShoeStoreRunner.log.info("end of manufacturing of " + getName() );
						Receipt reci=new Receipt(getName(),"manager",curr.getShoeName(),false,tick,curr.getTick(),curr.getAmount());
						working=false;
						complete(curr,reci);
						curr=null;

					}
					if((wcounter==0)&&(!working)&&(!waitq.isEmpty())) //the factory is not working on a shoe and it have a shoe order in line
					{

						working=true;
						curr=waitq.remove();
						ShoeStoreRunner.log.info("received request for "+curr.getShoeName());
						wcounter=curr.getAmount();

					}
				}					
			}
		}
				);

		subscribeRequest(ManufacturingOrderRequest.class,req->{
			ShoeStoreRunner.log.info(getName()+" got request to make "+req.getShoeName());
			waitq.add(req);
		}
				);
		theLatch.countDown();

	}


}

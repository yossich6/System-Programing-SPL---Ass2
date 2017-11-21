package bgu.spl.app;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import bgu.spl.mics.MicroService;


/**
 * This micro-service describes one client connected to the web-site
 */
public class WebsiteClientService extends MicroService {
	
	/** The purchase schedule. */
	private List<PurchaseSchedule> purchaseSchedule;
	
	/** The wish list. */
	private Set<String> wishList;
	
	/** The tick. */
	private int tick=0;


	/**
	 * Instantiates a new website client service.
	 */
	private WebsiteClientService() {
		super("Client");

	}

	/**
	 * Instantiates a new website client service.
	 *
	 * @param name the name
	 */
	public WebsiteClientService(String name) {
		super(name);
		purchaseSchedule=new LinkedList<PurchaseSchedule>();
		wishList=new LinkedHashSet<String>();
		tick=0;
	}
	
	/**
	 * Instantiates a new website client service.
	 *
	 * @param name the name
	 * @param purchaseSchedul the purchase schedule
	 * @param wishList the wish list
	 */
	public WebsiteClientService(String name,List<PurchaseSchedule> purchaseSchedul,Set<String> wishList) {
		super(name);
		this.purchaseSchedule=purchaseSchedul;
		this.wishList=wishList;
		tick=0;
	}
	
	/**
	 * Tick.
	 *
	 * @return the int
	 */
	public int tick(){
		return tick;
	}

	/**
	 * Gets the purchase schedule.
	 *
	 * @return the purchase schedule
	 */
	public List<PurchaseSchedule> getPurchaseSchedul() {
		return purchaseSchedule;
	}

	/**
	 * Sets the purchase schedule.
	 *
	 * @param purchaseSchedul the new purchase schedule
	 */
	public void setPurchaseSchedul(List<PurchaseSchedule> purchaseSchedul) {
		this.purchaseSchedule = purchaseSchedul;
	}

	/**
	 * Gets the wish list.
	 *
	 * @return the wish list
	 */
	public Set<String> getWishList() {
		return wishList;
	}

	/**
	 * Sets the wish list.
	 *
	 * @param wishList the new wish list
	 */
	public void setWishList(Set<String> wishList) {
		this.wishList = wishList;
	}


	/* (non-Javadoc)
	 * @see bgu.spl.mics.MicroService#initialize()
	 */
	@Override
	protected void initialize() {

		subscribeBroadcast(NewDiscountBroadcast.class, req-> {
			if(wishList.contains(req.getShoeType())){
				ShoeStoreRunner.log.info(this.getName()+" want to buy "+req.getShoeType()+" in discount");
				boolean reqAns=sendRequest(new PurchaseOrderRequest(getName(), req.getShoeType(), true , tick), req2->{
					if (req2!=null)
					{
						ShoeStoreRunner.log.info(getName() + " got the "+req2.getShoeType()+" and this is the receipt: ");
						req2.print();
						wishList.remove(req.getShoeType());
					}
					else
					{

						ShoeStoreRunner.log.info(getName()+" is dissapointed because he couldnt have his"+req.getShoeType() );
					}
				});
				if(!reqAns)
				{
					ShoeStoreRunner.log.info(getName()+" is dissapointed because he couldnt have his "+req.getShoeType() );
				}
			}
		});

		subscribeBroadcast(TickBroadcast.class, req -> {
			if(req.getTick()==-1){
				terminate();
			}else{
				tick=req.getTick();
				if( purchaseSchedule.isEmpty() && wishList.isEmpty()){
					terminate();
				}
				for(int i=0; i<purchaseSchedule.size(); i++){
					if(purchaseSchedule.get(i).getTick()== this.tick){
						String chosenShoes=purchaseSchedule.get(i).getType();
						ShoeStoreRunner.log.info(this.getName()+" want to buy "+chosenShoes+" in normal Price");

						boolean reqAns=sendRequest(new PurchaseOrderRequest(getName(),chosenShoes , purchaseSchedule.get(i).getDiscount() , tick), req2->{
							if (req2!=null)
							{
								purchaseSchedule.remove(chosenShoes);
								ShoeStoreRunner.log.info(getName() + " got the "+req2.getShoeType()+" and this is the receipt: ");
								req2.print();
							}
							else
							{
								ShoeStoreRunner.log.info(getName()+" is dissapointed because he couldnt have his "+chosenShoes );

							}
						});
						if(!reqAns)
						{
							ShoeStoreRunner.log.info(getName()+" is Dissapointed because there is no sellers who can handle his request");
						}

					}
		
				}

			}});	

		theLatch.countDown();
	}
}

package bgu.spl.app;

// TODO: Auto-generated Javadoc
/**
 * The Class PurchaseSchedule.
 */
public class PurchaseSchedule {
	
	/** The shoe type. */
	private String shoeType;//: string - the type of shoe to purchase.
	
	/** The tick. */
	private int tick;//: int
	
	/** The discount. */
	private boolean discount;
	
	/**
	 * Instantiates a new purchase schedule.
	 *
	 * @param shoe the shoe
	 * @param tic the tic
	 * @param discount the discount
	 */
	public PurchaseSchedule(String shoe,int tic, boolean discount)
	{
		this.shoeType=shoe;
		this.tick=tic;
		this.discount=discount;
	}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType()
	{
		return shoeType;
	}
	
	/**
	 * Gets the tick.
	 *
	 * @return the tick
	 */
	public int getTick()
	{
		return tick;
	}
	
	/**
	 * Gets the discount.
	 *
	 * @return the discount
	 */
	public boolean getDiscount(){
		return discount;
	}
}

 
package bgu.spl.app;

// TODO: Auto-generated Javadoc
/**
 * The Class DiscountSchedule.
 */
public class DiscountSchedule {
	
	/** The shoe type. */
	public String shoeType;//: string - the type of shoe to add discount to.
	
	/** The tick. */
	public int tick;//: int - the tick number to send the add the discount at.
	
	/** The amount. */
	public int amount;//: int - the amount of
	
	/**
	 * Instantiates a new discount schedule.
	 *
	 * @param shoe the shoe
	 * @param tic the tic
	 * @param amo the amo
	 */
	public DiscountSchedule(String shoe,int tic,int amo)
	{
		shoeType=shoe;
		tick=tic;
		amount=amo;
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
	 * Gets the amount.
	 *
	 * @return the amount
	 */
	public int getAmount()
	{
		return amount;
	}
	
}

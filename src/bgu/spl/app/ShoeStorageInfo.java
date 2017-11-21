package bgu.spl.app;

// TODO: Auto-generated Javadoc
/**
 * The Class ShoeStorageInfo.
 */
public class ShoeStorageInfo {

	/** The shoe type. */
	private String shoeType;
	
	/** The amount. */
	private int amount;
	
	/** The discounted amount. */
	private int discountedAmount;
	
	/**
	 * Instantiates a new shoe storage info.
	 *
	 * @param type the type
	 * @param amount the amount
	 */
	public ShoeStorageInfo(String type, int amount)
	{
		shoeType=type;
		this.amount=amount;
		discountedAmount=0;

	}
	
	/**
	 * Instantiates a new shoe storage info.
	 */
	public ShoeStorageInfo()
	{
		shoeType="";
		amount=0;
		discountedAmount=0;


	}


	/**
	 * Instantiates a new shoe storage info.
	 *
	 * @param type the type
	 * @param amount the amount
	 * @param discount the discount
	 */
	public ShoeStorageInfo(String type, int amount, int discount)
	{
		shoeType=type;
		this.amount=amount;
		discountedAmount=discount;

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
	 * Gets the amount.
	 *
	 * @return the amount
	 */
	public int getAmount()
	{
		return amount;
	}
	
	/**
	 * Gets the discounted amount.
	 *
	 * @return the discounted amount
	 */
	public int getDiscountedAmount()
	{
		return discountedAmount;
	}

	/**
	 * Sets the amount.
	 *
	 * @param amount the new amount
	 */
	public void setAmount(int amount)
	{
		this.amount=amount;
	}

	/**
	 * Adds the discount.
	 *
	 * @param amount the amount
	 */
	public void addDiscount(int amount)
	{
		
		if(discountedAmount+amount<=this.amount){
			discountedAmount=discountedAmount+amount;
		}
		else{
			if(amount <= this.amount)
				discountedAmount=amount;

			else discountedAmount=this.amount;
		}
	}
	
	/**
	 * Reduce discount.
	 */
	public void reduceDiscount()
	{
		discountedAmount--;
	}
	
}

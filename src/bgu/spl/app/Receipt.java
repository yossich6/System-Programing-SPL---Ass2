package bgu.spl.app;


// TODO: Auto-generated Javadoc
/**
 * The Class Receipt.
 */
public class Receipt {
	
	/** The seller. */
	private String seller;//: string - the name of the service which issued the receipt
	
	/** The customer. */
	private String customer;// string - the name of the service this receipt issued to (the client name or �store�)
	
	/** The shoe type. */
	private String shoeType;// string - the shoe type bought
	
	/** The discount. */
	private boolean discount;// boolean- indicating if the shoe was sold at a discounted price
	
	/** The issued tick. */
	private int issuedTick;// int - tick in which this receipt was issued (upon completing the corresponding
	
	/** The request tick. */
	private int requestTick;//: int - tick in which the customer requested to buy the shoe.
	
	/** The amount sold. */
	private int amountSold;
	
	/**
	 * Instantiates a new receipt.
	 *
	 * @param sell the sell
	 * @param cus the cus
	 * @param shoe the shoe
	 * @param dis the dis
	 * @param iss the iss
	 * @param req the req
	 * @param amo the amo
	 */
	public Receipt(String sell,String cus,String shoe,boolean dis,int iss,int req,int amo)
	{
	
		seller=sell;
		customer=cus;
		shoeType=shoe;
		discount=dis;
		issuedTick=iss;
		requestTick=req;
		amountSold=amo;
		
		
		
		
	}
	
	/**
	 * Gets the seller.
	 *
	 * @return the seller
	 */
	public String getSeller()
	{
		return seller;
	}
	
	/**
	 * Gets the customer.
	 *
	 * @return the customer
	 */
	public String getCustomer()
	{
		return customer;
	}
	
	/**
	 * Gets the shoe type.
	 *
	 * @return the shoe type
	 */
	public String getShoeType()
	{
		return shoeType;
	}
	
	/**
	 * Gets the discount.
	 *
	 * @return the discount
	 */
	public boolean getDiscount()
	{
		return discount;
	}
	
	/**
	 * Gets the issued tick.
	 *
	 * @return the issued tick
	 */
	public int getIssuedTick()
	{
		return issuedTick;
	}
	
	/**
	 * Gets the request tick.
	 *
	 * @return the request tick
	 */
	public int getRequestTick()
	{
		return requestTick;
	}
	
	/**
	 * Gets the amount sold.
	 *
	 * @return the amount sold
	 */
	public int getAmountSold()
	{
		return amountSold;
	}
	
	/**
	 * Prints the.
	 */
	public void print()
	{
		ShoeStoreRunner.log.info("**** seller: "+getSeller()+
		"|| customer: "+getCustomer()+
		"|| shoe type: "+getShoeType()+
		"|| discount: " + getDiscount()+
		"|| issued tick: " + getIssuedTick()+
		"|| request tick: " + getRequestTick()+
		"|| amount sold: " + getAmountSold()+" ***");
		
	}
	
	

}

package bgu.spl.app;

import bgu.spl.mics.*;

// TODO: Auto-generated Javadoc
/**
 * The Class PurchaseOrderRequest.
 */
public class PurchaseOrderRequest implements Request<Receipt> {
	
	/** The sender name. */
	private String senderName;
	
	/** The shoe type. */
	private String shoeType;
	
	/** The discount. */
	private boolean discount;
	
	/** The tick. */
	private int tick;
	
	/**
	 * Instantiates a new purchase order request.
	 *
	 * @param senderName the sender name
	 * @param shoeType the shoe type
	 * @param discount the discount
	 * @param tick the tick
	 */
	public PurchaseOrderRequest(String senderName, String shoeType, boolean discount,int tick) {
		super();
		this.senderName = senderName;
		this.shoeType = shoeType;
		this.discount = discount;
		this.tick=tick;
	}
    
    /**
     * Gets the sender name.
     *
     * @return the sender name
     */
    public String getSenderName() {
        return senderName;
    }

	/**
	 * Gets the shoe type.
	 *
	 * @return the shoe type
	 */
	public String getShoeType() {
		return shoeType;
	}

	/**
	 * Sets the shoe type.
	 *
	 * @param shoeType the new shoe type
	 */
	public void setShoeType(String shoeType) {
		this.shoeType = shoeType;
	}

	/**
	 * Checks if is discount.
	 *
	 * @return true, if is discount
	 */
	public boolean isDiscount() {
		return discount;
	}

	/**
	 * Sets the discount.
	 *
	 * @param discount the new discount
	 */
	public void setDiscount(boolean discount) {
		this.discount = discount;
	}

	/**
	 * Sets the sender name.
	 *
	 * @param senderName the new sender name
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
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
	
}

package bgu.spl.app;

import bgu.spl.mics.Broadcast;

// TODO: Auto-generated Javadoc
/**
 * The Class NewDiscountBroadcast.
 */
public class NewDiscountBroadcast implements Broadcast {
	
	/** The shoe type. */
	private String shoeType;
	
	/** The discount. */
	private int discount;

	/**
	 * Instantiates a new new discount broadcast.
	 *
	 * @param shoeType the shoe type
	 * @param discount the discount
	 */
	public NewDiscountBroadcast(  String shoeType, int discount) {
		this.shoeType = shoeType;
		this.discount = discount;
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
	 * Gets the discount.
	 *
	 * @return the discount
	 */
	public int getDiscount() {
		return discount;
	}
	
	/**
	 * Sets the discount.
	 *
	 * @param discount the new discount
	 */
	public void setDiscount(int discount) {
		this.discount = discount;
	}

}

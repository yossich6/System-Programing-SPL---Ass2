package bgu.spl.app;
import bgu.spl.mics.*;

// TODO: Auto-generated Javadoc
/**
 * The Class ManufacturingOrderRequest.
 */
public class ManufacturingOrderRequest implements Request<Receipt> {
	
	/**
	 * Instantiates a new manufacturing order request.
	 *
	 * @param shoeName the shoe name
	 * @param amount the amount
	 * @param tick the tick
	 */
	public ManufacturingOrderRequest(String shoeName, int amount, int tick) {
		super();
		this.shoeName = shoeName;
		this.amount = amount;
		this.tick = tick;
	}
	
	/** The shoe name. */
	private String shoeName;
	
	/** The amount. */
	private int amount;
	
	/** The tick. */
	private int tick;
	
	/**
	 * Gets the shoe name.
	 *
	 * @return the shoe name
	 */
	public String getShoeName() {
		return shoeName;
	}
	
	/**
	 * Sets the shoe name.
	 *
	 * @param shoeName the new shoe name
	 */
	public void setShoeName(String shoeName) {
		this.shoeName = shoeName;
	}
	
	/**
	 * Gets the amount.
	 *
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}
	
	/**
	 * Sets the amount.
	 *
	 * @param amount the new amount
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	/**
	 * Gets the tick.
	 *
	 * @return the tick
	 */
	public int getTick() {
		return tick;
	}
	
	/**
	 * Sets the tick.
	 *
	 * @param tick the new tick
	 */
	public void setTick(int tick) {
		this.tick = tick;
	}

}

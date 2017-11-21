package bgu.spl.app;

import bgu.spl.mics.Request;

// TODO: Auto-generated Javadoc
/**
 * The Class RestockRequest.
 */
public class RestockRequest implements Request<Boolean> {
	
	/**
	 * Instantiates a new restock request.
	 *
	 * @param shoeType the shoe type
	 * @param tick the tick
	 */
	public RestockRequest(String shoeType, int tick) {
		this.shoeType = shoeType;
		this.tick = tick;
	}
	
	/** The shoe type. */
	private String shoeType;
	
	
	/** The tick. */
	private int tick;
	
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

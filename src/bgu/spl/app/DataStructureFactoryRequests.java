package bgu.spl.app;

import java.util.LinkedList;
import java.util.Queue;

// TODO: Auto-generated Javadoc
/**
 * The Class Pair.
 */
public class DataStructureFactoryRequests {
	
	/** The queue request. */
	private Queue<RestockRequest> queueRequest;
	
	/** The shoe amount. */
	private int shoeAmount;

	/**
	 * Instantiates a new pair.
	 *
	 * @param queueRequest the queue request
	 * @param shoeAmount the shoe amount
	 */
	public DataStructureFactoryRequests(LinkedList <RestockRequest> queueRequest, int shoeAmount){
		this.queueRequest = queueRequest;
		this.shoeAmount = shoeAmount;
	}
	
	/**
	 * Gets the q.
	 *
	 * @return the q
	 */
	public Queue<RestockRequest> getQ()
	{ return queueRequest; }

	/**
	 * Gets the amount.
	 *
	 * @return the amount
	 */
	public int getAmount(){ 
		return shoeAmount; }

	/**
	 * Sets the q.
	 *
	 * @param r the new q
	 */
	public void setQ(RestockRequest r){ 
		this.queueRequest.add(r); }

	/**
	 * Adds the amount.
	 *
	 * @param shoeAmount the shoe amount
	 */
	public void addAmount(int shoeAmount){
		this.shoeAmount =this.shoeAmount+ shoeAmount; }

	/**
	 * Less amount.
	 *
	 * @param shoeAmount the shoe amount
	 */
	public void lessAmount(int shoeAmount){ 
		this.shoeAmount =this.shoeAmount -shoeAmount; }

}
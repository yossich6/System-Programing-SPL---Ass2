package bgu.spl.app;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * The store object holds a collection of ShoeStorageInfo: One for each shoe type the store offers. In
* addition, it contains a list of receipts issued to and by the store.
 */
public class Store{
	
	/** The shoes. */
	private ShoeStorageInfo [ ] shoes;
	
	/** The recipts. */
	private LinkedBlockingQueue<Receipt> recipts=new LinkedBlockingQueue<Receipt>();

	/**
	 * The Class SingletonHolder.
	 */
	private static class SingletonHolder {
		
		/** The instance. */
		private static Store instance = new Store();
	}

	/**
	 * Instantiates a new store.
	 */
	private Store() {

	}
	
	/**
	 * Gets the single instance of Store.
	 *
	 * @return single instance of Store
	 */
	public static Store getInstance() {
		return SingletonHolder.instance;
	}
	
	/**
	 * Gets the shoes storage.
	 *
	 * @return the shoes storage
	 */
	public ShoeStorageInfo [ ] getShoesStorage(){
		return shoes;
	}
	
	/**
	 * Gets the recipts.
	 *
	 * @return the recipts
	 */
	public LinkedBlockingQueue<Receipt>  getRecipts(){
		return recipts;
	}

	/**
	 * Load.
	 *
	 * @param storage the storage
	 */
	public void load ( ShoeStorageInfo [ ]  storage ){
		shoes=new ShoeStorageInfo [storage.length];
		for(int i=0; i<storage.length; i++){
			shoes[i]=storage[i];
		}
	}
	
	/**
	 * The Enum BuyResult.
	 */
	public enum BuyResult {
		
		/** The not in stock. */
		NOT_IN_STOCK, 
 /** The not on discount. */
 NOT_ON_DISCOUNT, 
 /** The regular price. */
 REGULAR_PRICE, 
 /** The discounted price. */
 DISCOUNTED_PRICE
	}
	
	/**
	 * Take.
	 *
	 * @param shoeType the shoe type
	 * @param onlyDiscount the only discount
	 * @return the buy result
	 */
	public synchronized BuyResult take ( String  shoeType ,boolean onlyDiscount ){
		

		boolean found=false;
		for(int i=0; i<shoes.length && !found; i++){
			if(shoes[i].getType().equals(shoeType)){
				//ShoeStoreRunner.log.info("we have "+shoes[i].getDiscountedAmount()+" shoes in discount");
				found=true;
				if(shoes[i].getAmount()>0)
				{
					if(onlyDiscount)
					{
						if(shoes[i].getDiscountedAmount()>0)
						{
							ShoeStoreRunner.log.info("The store looked for the "+shoeType+" returning answer that the shoes can be bought in discount");
							takeShoe(shoeType, true);
							return BuyResult.DISCOUNTED_PRICE;
							
						} else
						{
							//there is storage amount but the store doesn't have discount amount
							ShoeStoreRunner.log.info("The store looked for the "+shoeType+" returning answer that the shoes couldnt be bought in discount");
							return BuyResult.NOT_ON_DISCOUNT;
							
						}
					}
					else
					{
						if(shoes[i].getDiscountedAmount()>0)
						{
							ShoeStoreRunner.log.info("The store looked for the "+shoeType+" returning answer that the shoes can be bought in discount");
							takeShoe(shoeType, true);
							return BuyResult.DISCOUNTED_PRICE;
						} else
						{//client asked without Discount and there isn't discount amount 
							ShoeStoreRunner.log.info("The store looked for the "+shoeType+" returning answer that the shoes could be bought in normal price");
							takeShoe(shoeType, false);
							return BuyResult.REGULAR_PRICE;
						}
					}
				}
				else
				{
					if(onlyDiscount)
					{
						//no storage amount and clint asked for Discount 
						ShoeStoreRunner.log.info("The store looked for the "+shoeType+" returning answer that the shoes couldnt be bought in discount");
						return BuyResult.NOT_ON_DISCOUNT;
					}
					else
					{
						//no storage amount and clint didn't asked for Discount 
						ShoeStoreRunner.log.info("The store looked for the "+shoeType+" returning answer that the shoes couldnt be bought and new ones should be ordered");
						return BuyResult.NOT_IN_STOCK;
					}

				}
			}


		}
		
		//the shoe type is not in storage
		if(onlyDiscount)
		{
			ShoeStoreRunner.log.info("The store looked for the "+shoeType+" returning answer that the shoes couldnt be bought in discount");
			return BuyResult.NOT_ON_DISCOUNT;
		}
		else
		{
			ShoeStoreRunner.log.info("The store looked for the "+shoeType+" returning answer that the shoes couldnt be bought and new ones should be ordered");
			return BuyResult.NOT_IN_STOCK;
		}
		

	}



	/**
	 * Adds the.
	 *
	 * @param shoeType the shoe type
	 * @param amount the amount
	 */
	public void add ( String  shoeType ,int amount){
		boolean found=false; 
		
		for(int i=0; i<shoes.length && !found; i++){
			if(shoes[i].getType().equals (shoeType)){
				shoes[i].setAmount(shoes[i].getAmount()+amount);
				found=true;
			}
		}
		//means that the shoe type is not in storage
			if(found==false){
				addNewShoeType(new ShoeStorageInfo(shoeType,amount));
				
			}
		

	}
	
	/**
	 * Adds the new shoe type.
	 *
	 * @param shoeAdd the shoe add
	 */
	//private method to insert new type shoe and it's amount
	private void addNewShoeType(ShoeStorageInfo shoeAdd){
		ShoeStorageInfo [ ] newShoes=new ShoeStorageInfo [shoes.length +1];
		for(int i=0; i<shoes.length ; i++){
			newShoes[i]=shoes[i];
		}
		newShoes[newShoes.length-1]=shoeAdd;
		
		shoes=newShoes;
		
		
	}
	
	/**
	 * Take shoe.
	 *
	 * @param shoeType the shoe type
	 * @param discount the discount
	 */
	//method which will take shoe from the store
	private void takeShoe ( String  shoeType, boolean discount){
		if (discount)
		{
			boolean found=false;
			for(int i=0; i<shoes.length && !found; i++){
				if(shoes[i].getType().equals(shoeType)){
					shoes[i].setAmount(shoes[i].getAmount()-1);
					shoes[i].reduceDiscount();
					found=true;
				}
			}
		}
		else
		{	
			for(int i=0; i<shoes.length; i++){
				if(shoes[i].getType().equals(shoeType)){
					shoes[i].setAmount(shoes[i].getAmount()-1);
				}
			}	
		}
	}
	
	/**
	 * Adds the discount.
	 *
	 * @param shoeType the shoe type
	 * @param amount the amount
	 */
	public void addDiscount ( String  shoeType ,int amount) 
	{
		boolean found=false;
		for(int i=0; i<shoes.length && !found; i++){
			if(shoes[i].getType().equals (shoeType)){
				shoes[i].addDiscount(amount);
				found=true;
			}
		}
	}

	/**
	 * File.
	 *
	 * @param receipt the receipt
	 */
	public synchronized void file ( Receipt  receipt){

		try {
			recipts.put(receipt);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Prints the.
	 */
	public void print (){
		ShoeStoreRunner.log.info("The store condition is:");
		for(int i=0; i<shoes.length ; i++){
			ShoeStoreRunner.log.info("shoe name " + shoes[i].getType()+ "  amount:"+shoes[i].getAmount()+ "   discount:"+shoes[i].getDiscountedAmount());

		}

	


		Iterator<Receipt> iter=recipts.iterator();	
		ShoeStoreRunner.log.info("recipts:");
		while(iter.hasNext()){
			
			Receipt rec=iter.next();
			ShoeStoreRunner.log.info("***seller: "+rec.getSeller()+
			"|| customer: "+rec.getCustomer()+
			"|| shoe type: "+rec.getShoeType()+
			"|| discount: " + rec.getDiscount()+
			"|| tick: " + rec.getIssuedTick()+
			"|| request tick: " + rec.getRequestTick()+
			"|| amount sold: " + rec.getAmountSold()+"***");
		
		}
		
		
	}
}



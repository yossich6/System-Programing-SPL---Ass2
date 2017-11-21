package bgu.spl.app;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bgu.spl.app.Store.BuyResult;

public class StoreTest {
	Store store;

	@Before
	public void setUp() throws Exception {
		store=Store.getInstance();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInstance() {
		assertEquals(store, Store.getInstance());
	}

	@Test
	public void testLoad() {
		ShoeStorageInfo [] example={new ShoeStorageInfo("sports",5,0), 
				new ShoeStorageInfo("source",3,1),
				new ShoeStorageInfo("flip-flop",19,6)};
		store.load(example);
		assertEquals(example[0], store.getShoesStorage()[0]);
		assertEquals(example[1], store.getShoesStorage()[1]);
		assertEquals(example[2], store.getShoesStorage()[2]);
	}

	@Test
	public void testTake() {
		assertEquals(BuyResult.REGULAR_PRICE, store.take("sports",false));
		assertEquals(BuyResult.NOT_IN_STOCK, store.take("baskatball",false));
		assertEquals(BuyResult.DISCOUNTED_PRICE, store.take("flip-flop",true));
		assertEquals(BuyResult.NOT_ON_DISCOUNT, store.take("sports",true));
		
	}

	@Test
	public void testAdd() {
		store.add("flip-flop", 6);
		assertEquals(store.getShoesStorage()[2].getAmount(),25);
		store.add("source", 4);
		assertEquals(store.getShoesStorage()[1].getDiscountedAmount(),1);
		
	}


	@Test
	public void testAddDiscount() {
		store.addDiscount("sports", 4);
		assertEquals(store.getShoesStorage()[0].getDiscountedAmount(), 4);
		
	}

	@Test
	public void testFile() {
		assertEquals(store.getRecipts().size(), 0);
		Receipt rec=new Receipt("meir","avi","sports",true,3,3,1);
		store.file(rec);
		assertEquals(store.getRecipts().size(),1);	
	}

}

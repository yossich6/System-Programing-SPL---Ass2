package bgu.spl.mics.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bgu.spl.app.PurchaseOrderRequest;
import bgu.spl.app.Receipt;
import bgu.spl.app.SellingService;
import bgu.spl.app.TickBroadcast;
import bgu.spl.app.WebsiteClientService;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.RequestCompleted;

public class MessageBusImplTest {
	MessageBusImpl bus;
	MicroService seller;
	MicroService client;
	PurchaseOrderRequest req;
	TickBroadcast tick;

	@Before
	public void setUp() throws Exception {
		bus=MessageBusImpl.getInstance();
		seller=new SellingService("seller");
		client=new WebsiteClientService("client");
		bus.register(seller);
		bus.register(client);
		req= new PurchaseOrderRequest("client","sports",true,6);
		tick=new TickBroadcast(4);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInstance() {
		assertEquals(bus, MessageBusImpl.getInstance());
	}

	@Test
	public void testSubscribeRequest() throws InterruptedException {
		bus.subscribeRequest(PurchaseOrderRequest.class,seller);
		bus.sendRequest(req, client);
		assertTrue(bus.getMessageQ().get("seller").contains(req));
		
	}

	@Test
	public void testSubscribeBroadcast() {
		bus.subscribeBroadcast(TickBroadcast.class,seller);
		bus.sendBroadcast(tick);
		assertTrue(bus.getMessageQ().get("seller").contains(tick));
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testComplete() throws InterruptedException {
		bus.subscribeRequest(PurchaseOrderRequest.class,seller);
		bus.sendRequest(req, client);
		Receipt rec=new Receipt("meir","avi","sports",true,3,3,1);
		bus.complete( req, rec);
		assertFalse(bus.getMessageQ().get("client").isEmpty());	
		assertEquals(((RequestCompleted) bus.getMessageQ().get("client").take()).getResult(), rec);
	}

	@Test
	public void testSendBroadcast() {
		bus.subscribeBroadcast(TickBroadcast.class,client);
		bus.sendBroadcast(tick);
		assertTrue(bus.getMessageQ().get("client").contains(tick));
		
	}

	@Test
	public void testSendRequest() {
		bus.subscribeRequest(PurchaseOrderRequest.class,seller);
		bus.sendRequest(req, client);
		assertTrue(bus.getMessageQ().get("seller").contains(req));
	}

	@Test
	public void testRegister() {
		assertTrue(bus.getMessageQ().containsKey("seller"));
		assertTrue(bus.getMessageQ().containsKey("client"));
	}

	@Test
	public void testUnregister() {
		bus.unregister(seller);
		assertFalse(bus.getMessageQ().containsKey("seller"));
	}

	@Test
	public void testAwaitMessage() throws InterruptedException {
		bus.subscribeRequest(PurchaseOrderRequest.class,seller);
		bus.sendRequest(req, client);
		bus.awaitMessage(seller); 
		assertEquals(bus.getMessageQ().get("seller").size(),0);
		
	}

}

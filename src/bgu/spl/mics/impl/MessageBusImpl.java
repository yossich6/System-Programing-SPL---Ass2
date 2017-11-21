package bgu.spl.mics.impl;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

//log.log(Level.SEVERE, "abcd");
import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Message;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.Request;
import bgu.spl.mics.RequestCompleted;




// TODO: Auto-generated Javadoc
/**
 * The Class MessageBusImpl.
 */
public class MessageBusImpl implements MessageBus{

	/** The request messages. */
	@SuppressWarnings("rawtypes")
	private Map< Class<? extends Request>, BlockingQueue<String> > requestMessages;

	/** The oldRequest messages. */
	@SuppressWarnings("rawtypes")
	private Map< Class<? extends Request>, BlockingQueue<String> > oldrequestMessages;

	/** The broadcast messages. */
	private Map< Class<? extends Broadcast>, BlockingQueue<String>> broadcastMessages;

	/** The messageQ. */
	private Map< String, BlockingQueue<Message>> messageq;

	/** The senders. */
	private Map< Request<?> , MicroService> senders ;

	/**
	 * The Class SingletonHolder.
	 */
	private static class SingletonHolder {

		/** The instance. */
		private static MessageBusImpl instance = new MessageBusImpl();
	}


	/**
	 * Instantiates a new message bus implementation.
	 */
	private MessageBusImpl() {
		senders = new ConcurrentHashMap<>();
		messageq = new ConcurrentHashMap<>();
		broadcastMessages = new ConcurrentHashMap<>();
		oldrequestMessages = new ConcurrentHashMap<>();
		requestMessages = new ConcurrentHashMap<>();
	}

	/**
	 * Gets the single instance of MessageBusImpl.
	 *
	 * @return single instance of MessageBusImpl
	 */
	public static MessageBusImpl getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * Gets the message q.
	 *
	 * @return the message q
	 */
	public Map< String, BlockingQueue<Message>> getMessageQ(){
		return messageq;
	}

	/* (non-Javadoc)
	 * @see bgu.spl.mics.MessageBus#subscribeRequest(java.lang.Class, bgu.spl.mics.MicroService)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void subscribeRequest(Class<? extends Request> type, MicroService m) {

		if (requestMessages.get(type)==null)
		{
			requestMessages.put(type, (BlockingQueue<String>) new LinkedBlockingQueue<String>());

			try {
				requestMessages.get(type).put(m.getName());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
			oldrequestMessages.put(type, (BlockingQueue<String>) new LinkedBlockingQueue<String>());

		}
		else
		{
			try {
				requestMessages.get(type).put(m.getName());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/* (non-Javadoc)
	 * @see bgu.spl.mics.MessageBus#subscribeBroadcast(java.lang.Class, bgu.spl.mics.MicroService)
	 */
	@Override
	public synchronized void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if(broadcastMessages.get(type)==null){

			broadcastMessages.put(type, (BlockingQueue<String>) new LinkedBlockingQueue<String>());
			try {
				broadcastMessages.get(type).put(m.getName());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			try {
				broadcastMessages.get(type).put(m.getName());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see bgu.spl.mics.MessageBus#complete(bgu.spl.mics.Request, java.lang.Object)
	 */
	@Override
	public <T> void complete(Request<T> r, T result) {
		RequestCompleted<T> rc=new RequestCompleted<T>(r,result);
		MicroService ms=senders.get(r);
		try {
			messageq.get(ms.getName()).put(rc);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see bgu.spl.mics.MessageBus#sendBroadcast(bgu.spl.mics.Broadcast)
	 */
	@Override
	public synchronized void sendBroadcast(Broadcast b) {
		if(broadcastMessages.containsKey(b.getClass())){
			for (String str: broadcastMessages.get(b.getClass())){
				try {
					messageq.get(str).put(b);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
		}
	}

	/* (non-Javadoc)
	 * @see bgu.spl.mics.MessageBus#sendRequest(bgu.spl.mics.Request, bgu.spl.mics.MicroService)
	 */
	@SuppressWarnings("unused")
	@Override
	public synchronized boolean sendRequest(Request<?> r, MicroService requester) {

		if (requestMessages.containsKey(r.getClass()))
		{
			String name="";
			if(requestMessages.get(r.getClass()).isEmpty())
			{
				for (String queue:  oldrequestMessages.get(r.getClass()))
				{
					try {
						requestMessages.get(r.getClass()).put(oldrequestMessages.get(r.getClass()).take());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try {
				name = requestMessages.get(r.getClass()).take();
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			try {
				messageq.get(name).put(r);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				oldrequestMessages.get(r.getClass()).put(name);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			senders.put(r, requester);

			return true;

		}
		else return false;
	}

	/* (non-Javadoc)
	 * @see bgu.spl.mics.MessageBus#register(bgu.spl.mics.MicroService)
	 */
	@Override
	public void register(MicroService m) {
		messageq.put(m.getName(), (BlockingQueue<Message>) new LinkedBlockingQueue<Message>());	

	}

	/* (non-Javadoc)
	 * @see bgu.spl.mics.MessageBus#unregister(bgu.spl.mics.MicroService)
	 */
	@Override
	public synchronized void unregister(MicroService m) {
		
		Iterator<Class<? extends Request>> itr=requestMessages.keySet().iterator();
		while(itr.hasNext()){
			Class<? extends Request> i=itr.next();
			if(requestMessages.containsKey(i)){
				requestMessages.get(i).remove(m.getName());
			}
		}
		
		Iterator<Class<? extends Request>> itr2=oldrequestMessages.keySet().iterator();
		while(itr.hasNext()){
			Class<? extends Request> i=itr2.next();
			if(oldrequestMessages.containsKey(i)){
				oldrequestMessages.get(i).remove(m.getName());
			}
		}
		
		Iterator<Class<? extends Broadcast>> itr3=broadcastMessages.keySet().iterator();
		while(itr.hasNext()){
			Class<? extends Broadcast> i=itr3.next();
			if(broadcastMessages.containsKey(i)){
				broadcastMessages.get(i).remove(m.getName());
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see bgu.spl.mics.MessageBus#awaitMessage(bgu.spl.mics.MicroService)
	 */
	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {

		return messageq.get(m.getName()).take();

	}

}

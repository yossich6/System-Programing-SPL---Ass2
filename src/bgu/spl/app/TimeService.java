package bgu.spl.app;

import java.util.Timer;
import java.util.TimerTask;

import bgu.spl.mics.MicroService;


// TODO: Auto-generated Javadoc
/**
 * This micro-service is our global system timer (handles the clock ticks in the system). It is responsible
 * for counting how much clock ticks passed since the beggining of its execution
 */
public class TimeService extends MicroService{
	
	/** The store. */
	Store store=Store.getInstance();
	
	/** The tick. */
	private int tick=1;
	
	/** The duration. */
	private int duration;
	
	/** The speed. */
	private int speed;
	

	/**
	 * Instantiates a new time service.
	 */
	public TimeService()
	{
		super("timer");
	}
	
	/**
	 * Instantiates a new time service.
	 *
	 * @param name the name
	 * @param speed the speed
	 * @param duration the duration
	 */
	public TimeService(String name, int speed, int duration) {
		super(name);
		this.speed=speed;
		this.duration=duration;

	}

	/* (non-Javadoc)
	 * @see bgu.spl.mics.MicroService#initialize()
	 */
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, req -> {
			if(req.getTick()==-1){
			
				terminate();}
		});	
		Timer timey=new Timer(true);
		TimerTask task=new TimerTask(){
			public void run(){
				if(duration==0){
					sendBroadcast(new TickBroadcast(-1));		
					this.cancel();
					timey.cancel();
					timey.purge();		
				}
				else{
					try {
						theLatch.await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sendBroadcast(new TickBroadcast(tick));
					tick++;
					duration--;
				}}
		};		
		timey.scheduleAtFixedRate(task, 0 ,speed);
	}
}

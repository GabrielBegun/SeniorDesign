package FireScout;

import java.io.IOException;
import java.util.TooManyListenersException;

import Logger.Logger;
import sensor.SensorManager.SensorManager;
import communication.IRCamera;
import communication.PilotController;
import communication.XbeeInterface;

/* This is the main class of our project. On bootup, the beagleboard should run this class
 * All other classes are created from here. Their respective main() are meant to be only for testing
 * 
 * This class was previously called FSM
 * 
 */



public class FireScout {
	private FireScout() { 
	}

	public static FireScout getInstance() {
		if(myQuadcopter == null) myQuadcopter = new FireScout();
		return myQuadcopter;
	}

	private static FireScout myQuadcopter;
	private Logger logger;
	private PilotController pilotController;
	private SensorManager sensorManager;
	private IRCamera irCamera;
	private XbeeInterface xbeeInterface;

	private static State currState = State.DISARM;
	private static State nextState = State.DISARM;

	void init(){
		// Creates all instances of the lower classes 
		logger = Logger.getInstance();
		pilotController = PilotController.getInstance();
		sensorManager = SensorManager.getInstance();
		irCamera = IRCamera.getInstance();
		xbeeInterface = XbeeInterface.getInstance();
		// Navigation? 

		try {
			logger.init();
			pilotController.init();
			sensorManager.init();
			irCamera.init();
			xbeeInterface.init();
			// navigation
		} catch (TooManyListenersException e) {
			e.printStackTrace();
			// logg error
			// maybe fall through so program exists.
		} catch (IOException e) { // from xbee init
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Thread pilotController_thread = new Thread(pilotController);
		Thread sensorManager_thread = new Thread(sensorManager);
		// navigation
		pilotController_thread.start();
		sensorManager_thread.start();
	}

/***********
 FSM
 0 = Disarm
 1 = Test 1
 2 = Takeoff
 3 = Test 2
 4 = Hover | Room | Hallway
 5 = Land
***********/
	public enum State{
		DISARM,
		TEST1,
		TAKEOFF,
		TEST2,
		NAVIGATION,
		LAND
	}

	// Called by xBeeInterface when it receives data
	public void parseXBeeCommand (String str) { 
		if (str.equals("Start")) {
			nextState = State.TEST1;
			return;
		} else if (str.equals("Stop")){
			nextState = State.LAND;
			return;
		} else if (str.equals("Test1")){
			//if( test1() ) logger.write("");
			//else logger.write("fail");
			return;
		} else if (str.equals("Return")) {
			//return to base (Navigation?)
			return;
		} else if (str.equals("TakeoffLand")) {
			pilotController.takeoffLand();
			return;
		} else {
			try{
				xbeeInterface.write("Error parsing.");
			}
			catch (Exception e) {
				System.err.println(e.toString()); 
			}

		}
	}

	private boolean test1() throws IOException{
		xbeeInterface.write("FireScout: Test 1 started");
		logger.writeStandard("FireScout: Test 1 started");
		// TODO
		return true;
	}

	private boolean takeoff() throws InterruptedException, IOException{
		xbeeInterface.write("FireScout: takeoff 1 started");
		logger.writeStandard("FireScout: takeoff 1 started");
		pilotController.arm();
		Thread.sleep(1000);
		pilotController.setDesAlt(1000);
		Thread.sleep(8000); // Get throttle to around 300
		pilotController.setDesAlt(60);
		// TODO
		return true;
	}

	private boolean test2(){
		// TODO
		return true;
	}

	private boolean navigation() throws IOException{
		//xbeeInterface.write("FireScout: navigation 1 started");
		//logger.writeStandard("FireScout: navigation 1 started");
		// TODO
		return true;
	}

	private boolean land() throws InterruptedException, IOException{
		xbeeInterface.write("FireScout: land 1 started");
		logger.writeStandard("FireScout: land 1 started");
		pilotController.setDesAlt(10);
		Thread.sleep(8000);
		pilotController.setDesAlt(0);
		pilotController.disarm();
		
		
		// TODO
		return true;
	}

	// HANDLE Exceptions! Main SHOULD NOT THROW	 
	public static void main (String[] args) throws IOException, InterruptedException{
		FireScout fireScout = FireScout.getInstance();
		fireScout.init();
		while(true){
			switch (currState) {
			case DISARM: 
				// Stay here until xBee tells you to start;
				// TODO
				break;

			case TEST1:
				if (fireScout.test1()) nextState = State.TAKEOFF;
				else nextState = State.DISARM;
				break;

			case TAKEOFF: 
				if (fireScout.takeoff()) nextState = State.TEST2;
				else nextState = State.LAND;
				break;

			case TEST2:
				if (fireScout.test2()) nextState = State.NAVIGATION;
				else nextState = State.LAND;
				break;

			case NAVIGATION:
				fireScout.navigation();
				//if (fireScout.navigation()) nextState = State.LAND;
				//else nextState = State.LAND;
				break;

			case LAND:
				fireScout.land();
				nextState = State.DISARM;
				// temp
				return;
				//break;

			default:
				nextState = State.LAND;
				break;
			}
			currState = nextState;
		}
	}
}

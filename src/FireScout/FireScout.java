package FireScout;

import java.util.TooManyListenersException;

import Logger.Logger;
import sensor.SensorManager.SensorManager;
import util.UartDriver.UartFailException;
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
			xbeeInterface.init();
			logger.init();
			pilotController.init();
			sensorManager.init();
			irCamera.init();
			// navigation
		} catch (TooManyListenersException e) {
			e.printStackTrace();
			logger.writeError("FireScout: ToManyListenersException, quitting program");
			xbeeInterface.write("FireScout: ToManyListenersException, quitting program");
			System.exit(0);
		} catch (UartFailException e) {
			e.printStackTrace();
			logger.writeError("FireScout: UartFailException, quitting program");
			xbeeInterface.write("FireScout: UartFailException, quitting program");
			e.printStackTrace();
			System.exit(0);
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
	private enum State{
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
			xbeeInterface.write("FireScout: Running TakeoffLand");
			pilotController.takeoffLand();
			return;
		} else {
			xbeeInterface.write("Error parsing. " + str);
			}

		
	}

	private boolean test1() {
		xbeeInterface.write("FireScout: Test 1 started");
		logger.writeStandard("FireScout: Test 1 started");
		// TODO
		return true;
	}

	private boolean takeoff() throws InterruptedException {
		xbeeInterface.write("FireScout: takeoff started");
		logger.writeStandard("FireScout: takeoff started");
		pilotController.takeoff();
		return true;
	}

	private boolean test2(){
		// TODO
		return true;
	}

	private boolean navigation() {
		//xbeeInterface.write("FireScout: navigation 1 started");
		//logger.writeStandard("FireScout: navigation 1 started");
		// TODO
		return true;
	}

	private boolean land() throws InterruptedException {
		xbeeInterface.write("FireScout: land started");
		logger.writeStandard("FireScout: land started");
		pilotController.land();
		// TODO
		return true;
	}

	// HANDLE Exceptions! Main SHOULD NOT THROW	 
	public static void main (String[] args) {
		FireScout fireScout = FireScout.getInstance();
		fireScout.init();
		try{
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
					break;
					//return;
	
				default:
					nextState = State.LAND;
					break;
				}
				currState = nextState;
			}
		} catch( InterruptedException e){
			Thread.currentThread().interrupt();
			e.printStackTrace();
			Logger.getInstance().writeError("FireScout: InterruptedException. Exit");
			XbeeInterface.getInstance().write("FireScout: InterruptedException. Exit");
			System.exit(0);
		}
	}
}

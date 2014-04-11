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

	private  static FireScout myQuadcopter;
	

	private static State currState = State.DISARM;
	private static State nextState = State.DISARM;

	private Logger logger;
	private PilotController pilotController;
	private SensorManager sensorManager;
	private IRCamera irCamera;
	private XbeeInterface xbeeInterface;

	void init(){
		// Creates all instances of the lower classes 
		logger = Logger.getInstance();
		pilotController = PilotController.getInstance();
		sensorManager = SensorManager.getInstance();
		irCamera = IRCamera.getInstance();
		xbeeInterface = XbeeInterface.getInstance();
		// Navigation? Quadcopter?

		try {
			logger.init();
			pilotController.init();
			sensorManager.init();
			irCamera.init();
			xbeeInterface.init();
		} catch (TooManyListenersException e) {
			e.printStackTrace();
			// logg error
			// maybe fall through so program exists.
		}

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
	public void parseCommand (String str) { 
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

	private boolean test1(){
		// TODO
		return true;
	}

	private boolean takeoff(){

		// TODO
		return true;
	}

	private boolean test2(){
		// TODO
		return true;
	}

	private boolean navigation(){
		// TODO
		return true;
	}

	private boolean land(){
		// TODO
		return true;
	}

	public static void main (String[] args){
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
				if (fireScout.navigation()) nextState = State.LAND;
				else nextState = State.LAND;
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

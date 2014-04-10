package FireScout;
import java.io.IOException;
import java.util.TooManyListenersException;

import Logger.Logger;
import sensor.SensorManager.SensorManager;
import communication.PilotController;
import communication.XbeeInterface;

/* This is the main class of our project. On bootup, the beagleboard should run this class
 * All other classes are created from here. Their respective main() are meant to be only for testing
 * 
 * This class was previously called FSM
 * 
 */

// TODO USE AN ENUM! GEEZ!


public class FireScout {
	private  static FireScout myQuadcopter;
	private XbeeInterface xbeeInterface;

	private State currState = State.DISARM;
	private State nextState = State.DISARM;

	private Logger logger;
	private PilotController pilotController;
	private SensorManager sensorManager;

 //Variables
 private Boolean Xtest1 = false;
 private Boolean Xtest2 = false;
 private Boolean XtakeoffLand = false;
 
 private Boolean Xreturn = false;
 private Boolean Xmanual = false;
 
 private Boolean Xstart = false;
 
 private Boolean Qtest1 = false;
 private Boolean Qtest2 = false;
 //Quadcopter next actions;
 //10 = hover; 11 = room; 12 = hallway; 13 = Land
 //each of the hover(), room(), hallway() functions 
 //  should return one of these numbers
 private int QcurrAction = 10;

 private FireScout() { 
  myQuadcopter = FireScout.getInstance(); 
 }
 
 public static FireScout getInstance() {
  if(myQuadcopter == null) myQuadcopter = new FireScout();
  return myQuadcopter;
 }
 
 void init(){
	 // Creates all instances of the lower classes 
	 logger = Logger.getInstance();
	 pilotController = PilotController.getInstance();
	 sensorManager = SensorManager.getInstance();
	 // Navigation? Quadcopter?
	 
	 try {
		logger.init();
		pilotController.init();
		sensorManager.init();
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
 
 private void parseCommand (String str) {
  if (str.equals("Start"))
   //do something....
    return;
  else if (str.equals("Test1"))
   Xtest1 = true;
  else if (str.equals("Test2"))
   Xtest2 = true;
  else if (str.equals("Return"))
   //return to base (Navigation?)
    return;
  else if (str.equals("Takeoff and land"))
   //PilotController.takeoffLand();
    return;
  else
    try{
    xbeeInterface.sendCopterData("Error parsing.");
  }
  catch (Exception e) {
     System.err.println(e.toString()); 
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
 
 public void main (String[] args){
	 FireScout fireScout = FireScout.getInstance();
	 fireScout.init();
	 while(true){
	   switch (currState) {
	    case DISARM: 
	    	// Stay here until xBee tells you to start;
	    	// TODO
	    
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
		     break;
		     
	    default:
	    	nextState = State.LAND;
	     break;
	   }
	   currState = nextState;
	  }
 }
}

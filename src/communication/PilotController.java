package communication;

import java.util.TooManyListenersException;

import sensor.SensorManager.SensorManager;
import util.Param;
import util.UartDriver.UartFailException;
import Logger.Logger;

public class PilotController implements Runnable {
	private Pilot pilot;
	private volatile boolean shutdown = false;
	private Logger logger; 
	private SensorManager sensorManager;
	private XbeeInterface xbeeInterface;

	// PID variables
	// Throttle - Altitude
	private double desAltitude;
	private int prevThrottle; //throttle starts at 0
	private double prevErrorAlt_t; //set to zero for start
	private double errorIntegral_t; //errorIntegral starts at 0
	private double prevTime_t;

	// Pitch - Laser Range Finder
	private double desDist_front;
	private int prevPitch;
	private double prevErrorAlt_p; //set to zero for start
	private double errorIntegral_p; //errorIntegral starts at 0
	private double prevTime_p;

	// Roll - Side sensors... Must use both sides
	private double desDist_left;
	private double desDist_right;
	private int prevRoll;
	private double prevErrorAlt_r; //set to zero for start
	private double errorIntegral_r; //errorIntegral starts at 0
	private double prevTime_r;

	// Yaw - Uses compass data (?)
	private double desAngle;

	// Private constructor
	private static PilotController myPilotController;
	private PilotController() { }

	public static PilotController getInstance() {
		if(myPilotController == null) myPilotController = new PilotController();
		return myPilotController;
	}

	public void init() throws TooManyListenersException, UartFailException{
		pilot = Pilot.getInstance(); 
		logger = Logger.getInstance();
		sensorManager = SensorManager.getInstance();
		xbeeInterface = XbeeInterface.getInstance();

		this.clearVariables();
	}

	private void clearVariables(){
		// Throttle
		desAltitude = 0;
		prevThrottle = 0;
		prevErrorAlt_t = 0;
		errorIntegral_t = 0.0f;
		prevTime_t = (double)System.currentTimeMillis();

		// Pitch
		desDist_front = 0;
		prevPitch = 0;
		prevErrorAlt_p = 0;
		errorIntegral_p = 0.0f;
		prevTime_p = (double)System.currentTimeMillis();

		desDist_left = 0;
		desDist_right = 0;
		prevRoll = 0;
		prevErrorAlt_r = 0; 
		errorIntegral_r = 0.0f; 
		prevTime_r = (double)System.currentTimeMillis();
	}

	// PID loops
	private void setThrottleWithAltitude(double current_altitude) {
		if(desAltitude == 0) return;

		// Calculate time since last read 
		double currentTime = (double)System.currentTimeMillis();
		double timeDiff = currentTime - prevTime_t;

		//Calculate the error
		double errorAlt = desAltitude - current_altitude;

		//Data collection for discrete time integration, limit data to 1000 entries
		errorIntegral_t += timeDiff*((prevErrorAlt_t+errorAlt)/2.0) / 1000000; //add midpoint approximation to total error integral

		//Data for differentiation
		double differentialAlt = errorAlt - prevErrorAlt_t;

		//adding the PID to current throttle command
		double addPAlt = Param.kP_t*(errorAlt);
		double addIAlt = Param.kI_t*errorIntegral_t;
		double addDAlt = Param.kD_t*(differentialAlt);

		double throttleChange = Param.throttleScale*(addPAlt+addIAlt+addDAlt);
		//int throttleChange = (int)(Param.throttleScale * addPAlt); // Remove for full PID
		if(throttleChange > Param.throttleDeltaMaxUP) throttleChange = Param.throttleDeltaMaxUP;
		if(throttleChange < Param.throttleDeltaMaxDOWN) throttleChange = -Param.throttleDeltaMaxDOWN;
		int newThrottle = prevThrottle + (int)throttleChange;

		// Boundry check the new Throttle
		if(newThrottle > Param.throttleMax ){
			newThrottle = Param.throttleMax;
		} else if(newThrottle < Param.throttleMin) {
			newThrottle = Param.throttleMin;
		}

		pilot.setThrottle(newThrottle);
		//System.out.println("newThrottle" +  newThrottle);


		xbeeInterface.write("PilotControllerPID_t: "+newThrottle + " " + current_altitude +  " "+ addPAlt + " " + addIAlt + " " + addDAlt);
		logger.writeDebug("PilotControllerPID_t: "+newThrottle + " " + current_altitude +  " "+ addPAlt + " " + addIAlt + " " + addDAlt);


		prevThrottle = newThrottle;
		prevErrorAlt_t = errorAlt; 
		prevTime_t = currentTime;
	}

	private void setPitchWithLaser(double current_dist) {
		if(desDist_front == 0) return;
		// Calculate time since last read 
		double currentTime = (double)System.currentTimeMillis();
		double timeDiff = currentTime - prevTime_p;

		//Calculate the error
		double errorDist = desDist_front - current_dist;

		//Data collection for discrete time integration, limit data to 1000 entries
		errorIntegral_p += timeDiff*((prevErrorAlt_p+errorDist)/2.0) / 1000000; //add midpoint approximation to total error integral

		//Data for differentiation
		double differentialDist = errorDist - prevErrorAlt_p;

		//adding the PID to current throttle command
		double addPDist = Param.kP_p*(errorDist);
		double addIDist = Param.kI_p*errorIntegral_p;
		double addDDist = Param.kD_p*(differentialDist);

		double pitchChange = Param.pitchScale*(addPDist+addIDist+addDDist);
		//int throttleChange = (int)(Param.throttleScale * addPAlt); // Remove for full PID
		if(pitchChange > Param.pitchDeltaMaxUP) pitchChange = Param.pitchDeltaMaxUP;
		if(pitchChange < Param.pitchDeltaMaxDOWN) pitchChange = -Param.pitchDeltaMaxDOWN;
		int newPitch = prevPitch + (int)pitchChange;

		// Boundry check the new Throttle
		if(newPitch > Param.pitchMax ){
			newPitch = Param.pitchMax;
		} else if(newPitch < Param.pitchMin) {
			newPitch = Param.pitchMin;
		}

		pilot.setPitch(newPitch);
		//System.out.println("newThrottle" +  newThrottle);

		prevPitch = newPitch;
		prevErrorAlt_p = errorDist; 
		prevTime_p = currentTime;
	}

	private void setPitchWithDistances(double current_dist_left, double current_dist_right) {
		// TODO
	}

	private void setYawWithAngle(double current_angle){
		
	}

	private void arm() { pilot.setArmed(1); }
	private void disarm() { pilot.setArmed(0); }

	public void setDesAltitude(double v) { desAltitude = v; } // Inches
	public void setDesDistFront(double v) {desDist_front = v; }	
	public void setDesDistLeft(double v) { desDist_left = v; }
	public void setDesDistRight(double v) { desDist_right = v; }
	public void setDesAngle(double v) { desAngle = v; }
	public void shutdownConroller() { shutdown = false; }

	
	private void sendXBeePIDTarget(){
		xbeeInterface.write("PilotController: PIDTarget "+desAltitude+","+desDist_front+","+desDist_left+","+desDist_right+","+desAngle);
		logger.writeStandard("PilotController: PIDTarget "+desAltitude+","+desDist_front+","+desDist_left+","+desDist_right+","+desAngle);
	}
	
	private enum State {
		TAKEOFF,
		PIDCONTROL,
		LAND,
		TAKEOFFLAND
	}

	private volatile static State nextState = State.PIDCONTROL;


	// state control
	public void takeoff() { nextState = State.TAKEOFF; }
	public void land() { nextState = State.LAND; }
	public void takeoffLand(){ nextState = State.TAKEOFFLAND; }
	public void emergencyStop() { pilot.powerOff(); }
	
	private void takeoff_run() throws InterruptedException {
		this.clearVariables();
		this.arm();
		pilot.sendMessage();
		Thread.sleep(1500);
		//pilot.setDesHeight(45);
		//pilot.setThrottle(120);
		//pilot.sendMessage();
		
		int current_throttle = Param.throttleMin;
		while(current_throttle < Param.throttleHover) {
			current_throttle += Param.throttleDeltaMaxUP;
			pilot.setThrottle(current_throttle);
			pilot.sendMessage();
			Thread.sleep(Param.loopDelay);
		}
		prevThrottle = current_throttle;
		this.setDesAltitude(60);
	}

	private void land_run() throws InterruptedException{
		int current_throttle = prevThrottle;
		while(current_throttle > Param.throttleLand){
			current_throttle += Param.throttleDeltaMaxDOWN;
			pilot.setThrottle(current_throttle);
			pilot.sendMessage();
			Thread.sleep(Param.loopDelay);
		}
		//pilot.setDesHeight(10);
		//pilot.sendMessage();
		Thread.sleep(10000);
		this.disarm();
		pilot.sendMessage();
		this.clearVariables();
	}
	
	private void takeoff_land_run() throws InterruptedException{
		this.takeoff_run();
		Thread.sleep(5000);
		this.land_run();
	}

	public void run() {
		double current_altitude = 0;
		double current_lasetDist = 0;
		int status_update_count = 0;
		while (!shutdown) {  
			try {
				switch (nextState){
				case TAKEOFF:
					this.takeoff_run();
					nextState = State.PIDCONTROL;
					break;
				case LAND:
					this.land_run();
					nextState = State.PIDCONTROL;
					break;
				case TAKEOFFLAND:
					takeoff_land_run();
					nextState = State.PIDCONTROL;
					break;
				case PIDCONTROL:
					if(status_update_count++ %Param.statusUpdatedCountMod == 0) sendXBeePIDTarget();
					// TODO
					current_altitude = sensorManager.ranges[1];
					setThrottleWithAltitude(current_altitude); 
					current_lasetDist = sensorManager.ranges[0];
					setPitchWithLaser(current_lasetDist);
					
					pilot.sendMessage();
					Thread.sleep(Param.loopDelay);  
					break;
				}

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
				logger.writeError("PilotController: InterruptedException. Exit");
				xbeeInterface.write("PilotController: InterruptedException. Exit");
				System.exit(0);
			}
		}  
	}



	/*
    public static void main(String[] args) {
    	PilotController u;
		try {
			  u = PilotController.getInstance();
			  u.run();
			  u.setDesAlt(40);
			  Thread.sleep(3000);
			  u.setDesAlt(4);;
			  Thread.sleep(3000);
			  u.shutdownConroller();
		  } catch (InterruptedException e) {
		  Thread.currentThread().interrupt();
			  e.printStackTrace();
		  }
		  System.out.println("DONE");
    } */
}

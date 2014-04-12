package communication;

import java.io.IOException;
import java.util.TooManyListenersException;

import util.Param;
import Logger.Logger;

public class PilotController implements Runnable {
	private Pilot pilot;
	private volatile boolean shutdown = false;
	private Logger logger; 

	// PID variables
	// Throttle - Altitude
	private double desAlt;
	private int prevThrottle; //throttle starts at 0
	private double prevErrorAlt_t; //set to zero for start
	private double errorIntegral_t; //errorIntegral starts at 0
	private double prevTime_t;

	// Pitch - Laser Range Finder
	private double desDist;
	private int prevPitch;
	private double prevErrorAlt_p; //set to zero for start
	private double errorIntegral_p; //errorIntegral starts at 0
	private double prevTime_p;

	// Roll - Side sensors... Must use both sides TODO
	private double desDist_left;
	private double desDist_right;
	private int prevRoll;
	private double prevErrorAlt_r; //set to zero for start
	private double errorIntegral_r; //errorIntegral starts at 0
	private double prevTime_r;

	// Yaw - Uses compass data (?)
	private double desAngle;

	// PID loops
	private void setThrottleWithAltitude(double current_altitude) {
		// Calculate time since last read 
		double currentTime = (double)System.currentTimeMillis();
		double timeDiff = currentTime - prevTime_t;

		//Calculate the error
		double errorAlt = desAlt - current_altitude;

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
		if(throttleChange > Param.throttleDeltaMax) throttleChange = Param.throttleDeltaMax;
		if(throttleChange < -Param.throttleDeltaMax) throttleChange = -Param.throttleDeltaMax;
		int newThrottle = prevThrottle + (int)throttleChange;

		// Boundry check the new Throttle
		if(newThrottle > Param.throttleMax ){
			newThrottle = Param.throttleMax;
		} else if(newThrottle < Param.throttleMin) {
			newThrottle = Param.throttleMin;
		}

		pilot.setThrottle(newThrottle);
		//System.out.println("newThrottle" +  newThrottle);

		prevThrottle = newThrottle;
		prevErrorAlt_t = errorAlt; 
		prevTime_t = currentTime;
	}


	private void sePitchWithDistance(double current_dist) {
		// Calculate time since last read 
		double currentTime = (double)System.currentTimeMillis();
		double timeDiff = currentTime - prevTime_p;

		//Calculate the error
		double errorDist = desDist - current_dist;

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
		if(pitchChange > Param.pitchDeltaMax) pitchChange = Param.pitchDeltaMax;
		if(pitchChange < -Param.pitchDeltaMax) pitchChange = -Param.pitchDeltaMax;
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
		// TODO
	}

	public void setDesAlt(double v) { desAlt = v; }
	public void setDesDist(double v) {desDist = v; }
	public void setDesDist_left(double v) { desDist_left = v; }
	public void setDesDist_right(double v) { desDist_right = v; }
	public void setDesAngle(double v) { desAngle = v; }
	public void shutdownConroller() { shutdown = false; }

	// Private constructor
	private static PilotController myPilotController;
	private PilotController() { }

	public static PilotController getInstance() {
		if(myPilotController == null) myPilotController = new PilotController();
		return myPilotController;
	}

	public void init() throws TooManyListenersException{
		pilot = Pilot.getInstance(); 
		logger = Logger.getInstance();
		// Throttle
		desAlt = 0;
		prevThrottle = 0;
		prevErrorAlt_t = 0;
		errorIntegral_t = 0.0f;
		prevTime_t = (double)System.currentTimeMillis();

		// Pitch
		desDist = 0;
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

	public void run() {
		//System.out.println("HERE i AM!");
		double current_altitude = 0;
		while (!shutdown) {  
			try {
				//current_altitude = SensorMonitor.getAltitude(); or something like this
				setThrottleWithAltitude(current_altitude++); 
				pilot.sync();
				Thread.sleep(333);          
			} catch (InterruptedException e) {
				// good practice
				Thread.currentThread().interrupt();
				return;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}  
	}

	public void takeoffLand(){
		System.out.println("PilotController called takeoffLand!");
		// TODO
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
			  e.printStackTrace();
		  }
		  System.out.println("DONE");
    } */
}

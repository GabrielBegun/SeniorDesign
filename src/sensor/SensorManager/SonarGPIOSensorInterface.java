package sensor.SensorManager;

import java.lang.Process;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Exception;

import Logger.Logger;



public class SonarGPIOSensorInterface{
	final String C_CALL_NAME = "s_gpio";
	private int ID;
	private SensorManager theBoss;
	private int trig_pin, echo_pin;
	private Logger log;

	public SonarGPIOSensorInterface(int trig_pin, int echo_pin){
		this.trig_pin = trig_pin;
		this.echo_pin = echo_pin;
	}

	public void init(){
		System.out.println(String.format("Init Sensor with ID %d, trig pin %d, echo pin %d", ID, trig_pin, echo_pin));
		theBoss = SensorManager.getInstance();
		log = Logger.getInstance();
		log.writeStandard(String.format("Sensor with ID %d initialized\n",ID));
	}

	public void getRanging(){
		double range = -1;
		String s = "";
		try{
			System.out.println("GPIO..");
			Process p = Runtime.getRuntime().exec("./" + C_CALL_NAME + " " + trig_pin + " " + echo_pin);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while((s = stdInput.readLine()) != null){
				break;
			}
			range = Double.parseDouble(s);
		} catch(IOException e){
			System.out.println("Exception");
			log.writeError(String.format("SensorGPIOSensorInterface::getRanging error - Sensor with ID %d\n",ID));
			range = -1;
			//System.out.println("Exception occured. " + s);
			//e.printStackTrace();
		}
		
		theBoss.addRange(range, ID);
	}

	public void giveID(int ID){
		this.ID = ID;
	}

/*
	public static void main(String[] args){
		SonarGPIOSensorInterface sg = new SonarGPIOSensorInterface("sense.txt");
		System.out.println(sg.getRanging());
	}
	*/
}
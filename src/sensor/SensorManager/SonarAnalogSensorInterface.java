package sensor.SensorManager;

import java.io.*;
import Logger.Logger;

public class SonarAnalogSensorInterface{
	String port;
	private int ID;
	private SensorManager theBoss;
	private Logger log;
	private final double VCC = 3.3;
	private final double DIVISION_FACTOR = 5120;
	private final double REFERENCE_VOLTAGE = 1.8;
	private final double BITMAX_VALUE = 1024;
	private final double MM_TO_CM = 10;

	private double ANALOG_TO_CM = 0;

	public SonarAnalogSensorInterface(String port){
		this.port = port;
	}

	public void init(){
		theBoss = SensorManager.getInstance();
		log = Logger.getInstance();
		ANALOG_TO_CM = 1;
		ANALOG_TO_CM *= REFERENCE_VOLTAGE;
		ANALOG_TO_CM *= DIVISION_FACTOR;
		ANALOG_TO_CM /= BITMAX_VALUE;
		ANALOG_TO_CM /= VCC;
		ANALOG_TO_CM /= MM_TO_CM;
	}

	public void getRanging(){
		double rr = -1, rr1, rr2, rr3;
		try{
			BufferedReader br = new BufferedReader(new FileReader(port));
			String range = br.readLine();
			br.close();
			rr1 = Double.parseDouble(range);
			//rr1 /= 3.2;
			try{Thread.sleep(30);} catch(Exception e){}
			br = new BufferedReader(new FileReader(port));
			range = br.readLine();
			br.close();
			rr2 = Double.parseDouble(range);
			//rr2 /= 3.2;
			try{Thread.sleep(30);} catch(Exception e){}
			br = new BufferedReader(new FileReader(port));
			range = br.readLine();
			br.close();
			rr3 = Double.parseDouble(range);
			//rr3 /= 3.2;
			rr = (rr1 + rr2 + rr3)/3.0;
			rr *= ANALOG_TO_CM;
		} catch(IOException e){
			log.writeError(String.format("SensorAnalogSensorInterface::getRanging IO error - Sensor with ID %d\n",ID));
			rr = -1;
			//System.out.println("Error. IOException with reading port");
		} 

		theBoss.addRange(rr, ID);
	}

	public void giveID(int ID){
		this.ID = ID;
	}

/*
	public static void main(String[] args){
		SonarAnalogSensorInterface sa = new SonarAnalogSensorInterface("/sys/devices/ocp.2/helper.14/AIN1");
		while(true){
			System.out.print(sa.getRanging());
			System.out.println(" cm.");
			try{Thread.sleep(30);} catch(Exception e){}
			try{
				Thread.sleep(500);
			} catch(Exception e){

			}
		}
	}
*/
}

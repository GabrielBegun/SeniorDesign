package sensor.SensorManager;

import java.io.IOException;
import java.util.TooManyListenersException;

import communication.XbeeInterface;

import Logger.Logger;
import util.Param;
import util.UartDriver.UartFailException;

//TODO: sonar gpio pins fix & ports and stuff

public class SensorManager implements Runnable{
	private static SensorManager theBoss;

	private final int NUM_LASER_SENSORS = 1;
	private final int NUM_ANALOG_SENSORS = 1;
	private final int NUM_GPIO_SENSORS = 8;

	private final int LASER_TIMER = 17;
	private final int SONAR_ANALOG_TIMER = 20;
	private final int SONAR_GPIO_TIMER = 24;
	private final int PRINT_DELAY = 100;

	private final int[] GPIOA = {0, 2, 4, 6};
	private final int[] GPIOB = {1, 3, 5, 7};

	private final int DELAY = 5;

	private final boolean PRINT_GPIO = true;


	private LaserSensorInterface underling_laser;
	private SonarAnalogSensorInterface[] underling_sonar_analog;
	private SonarGPIOSensorInterface[] underling_sonar_gpio;
	public double[] sensorOrientation;	//orientation from front of copter. 0 = laser, 1-2 = analog sonar, 3-10 = gpio sonar
	public double[] sensorPosition; //position from middle of the side in cm. +is to the right, - is to the left. 0 = laser, 1-2 = analog sonar, 3-10 = gpio sonar
	public double[] ranges;		//most recent ranging data 0 = laser, 1 = analog sonar, 2-9 = gpio sonar. If this value is -1, then there was an error with the sensor (implemented)
								//most recent ranging data 0 = laser, 1-2 = analog sonar, 3-10 = gpio sonar. If this value is -1, then there was an error with the sensor
	
	private final int[] GPIO_PINS = {48, 49, 60, 51, 7, 66, 69, 45, 23, 47, 27, 22, 67, 68, 44, 26, 46, 65};
	private final int[] ANALOG_PINS = {1, 2, 3, 4, 5, 6, 0};

	private SensorManager(){
	}

	public void init() throws TooManyListenersException, UartFailException{

		underling_laser = new LaserSensorInterface();
		underling_laser.giveID(0);
		underling_sonar_analog = new SonarAnalogSensorInterface[NUM_ANALOG_SENSORS];
		for(int ii = 0; ii < NUM_ANALOG_SENSORS; ii++){
			underling_sonar_analog[ii] = new SonarAnalogSensorInterface(String.format("/sys/devices/ocp.2/helper.11/AIN%d", ANALOG_PINS[ii]));
			underling_sonar_analog[ii].giveID(ii + NUM_LASER_SENSORS);
		} 
		underling_sonar_gpio = new SonarGPIOSensorInterface[NUM_GPIO_SENSORS];
		for(int ii = 0; ii < NUM_GPIO_SENSORS; ii++){
			underling_sonar_gpio[ii] = new SonarGPIOSensorInterface( GPIO_PINS[ii*2], GPIO_PINS[(ii*2)+1]);
			underling_sonar_gpio[ii].giveID(ii + NUM_LASER_SENSORS + NUM_ANALOG_SENSORS);
		}
		sensorOrientation = new double[NUM_LASER_SENSORS + NUM_ANALOG_SENSORS + NUM_GPIO_SENSORS];
		sensorPosition = new double[NUM_LASER_SENSORS + NUM_ANALOG_SENSORS + NUM_GPIO_SENSORS];
		ranges = new double[NUM_LASER_SENSORS + NUM_ANALOG_SENSORS + NUM_GPIO_SENSORS];

		underling_laser.init();
		for(int ii = 0; ii < underling_sonar_analog.length; ii++)
			underling_sonar_analog[ii].init();
		for(int ii = 0; ii < underling_sonar_gpio.length; ii++)
			underling_sonar_gpio[ii].init();
	}

	public static SensorManager getInstance(){
		if(theBoss == null) theBoss = new SensorManager();
		return theBoss;
	}

	public void addRange(double range, int ID){
		ranges[ID] = range;
		if(!PRINT_GPIO) System.out.println(String.format("ID %d = %f", ID, range));
	}

	public void run(){
		int laser_counter = 0;
		int sonar_a_counter = 0;
		int sonar_g_counterA = 0;
		int sonar_g_counterB = SONAR_GPIO_TIMER/2;
		int print_counter = 0;

		while(true){
			try{
				if(laser_counter == LASER_TIMER){
					if(Param.LASER_ACTIVE && !PRINT_GPIO)
						underling_laser.getRanging();
					laser_counter = 0;
				} else{
					laser_counter++;
				}

				if(sonar_a_counter == SONAR_ANALOG_TIMER){
					if(Param.ANALOG_ACTIVE && !PRINT_GPIO){
						for(int ii = 0; ii < NUM_ANALOG_SENSORS; ii++)
							underling_sonar_analog[ii].getRanging();
					}
					sonar_a_counter = 0;
				} else{
					sonar_a_counter++;
				}

				if(sonar_g_counterA == SONAR_GPIO_TIMER){
					if(Param.GPIO_ACTIVE || PRINT_GPIO){
						System.out.println("GPIOA");
						for(int ii = 0; ii < GPIOA.length; ii++){
							underling_sonar_gpio[GPIOA[ii]].getRanging();
						}
					}
					sonar_g_counterA = 0;
				} else{
					sonar_g_counterA++;
				}

				if(sonar_g_counterB == SONAR_GPIO_TIMER){
					if(Param.GPIO_ACTIVE || PRINT_GPIO){
						System.out.println("GPIOB");
						for(int ii = 0; ii < GPIOB.length; ii++){
							underling_sonar_gpio[GPIOB[ii]].getRanging();
						}
					}
					sonar_g_counterB = 0;
				} else{
					sonar_g_counterB++;
				}

				if(print_counter == PRINT_DELAY){
					System.out.println("###################################");
					if(PRINT_GPIO){
						Runtime.getRuntime().exec("clear");
						for(int ii = 0; ii < NUM_GPIO_SENSORS; ii++){
							System.out.println(String.format("ID[%d] = %f", NUM_LASER_SENSORS+NUM_ANALOG_SENSORS+ii, ranges[NUM_LASER_SENSORS+NUM_ANALOG_SENSORS+ii]));
						}
					}
					print_counter = 0;
				}else{
					print_counter++;
				}
				
				Thread.sleep(DELAY);
			} catch(IOException e){
				System.out.println("Error in SensorManager run");
				e.printStackTrace();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
				Logger.getInstance().writeError("PilotController: InterruptedException. Exit");
				XbeeInterface.getInstance().write("PilotController: InterruptedException. Exit");
				System.exit(0);
				
			}
		}
	}
	/*
	public static void main(String[] args){
		//SensorManager sm = SensorManager.getInstance();
		Thread t = new Thread(SensorManager.getInstance());
		t.start();
	}
	 */

}
package sensor.SensorManager;



//TODO: sonar gpio pins fix & ports and stuff

public class SensorManager implements Runnable{
	private static SensorManager theBoss;

	private final int NUM_LASER_SENSORS = 1;
	private final int NUM_ANALOG_SENSORS = 2;
	private final int NUM_GPIO_SENSORS = 8;

	private LaserSensorInterface underling_laser;
	//private SonarAnalogSensorInterface[] underling_sonar_analog;
	private SonarGPIOSensorInterface[] underling_sonar_gpio;
	public double[] sensorOrientation;	//orientation from front of copter. 0 = laser, 1-2 = analog sonar, 3-10 = gpio sonar
	public double[] sensorPosition; //position from middle of the side in cm. +is to the right, - is to the left. 0 = laser, 1-2 = analog sonar, 3-10 = gpio sonar
	public double[] ranges;		//most recent ranging data 0 = laser, 1-2 = analog sonar, 3-10 = gpio sonar. If this value is -1, then there was an error with the sensor

	private final int[] GPIO_PINS = {48, 49, 60, 51, 7, 66, 69, 45, 23, 47, 27, 22, 67, 68, 44, 26, 46, 65};

	private SensorManager(){
		try{
			underling_laser = new LaserSensorInterface("/dev/ttyO4");
			underling_laser.giveID(0);
			/*underling_sonar_analog = new SonarAnalogSensorInterface[NUM_ANALOG_SENSORS];
			for(int ii = 0; ii < NUM_ANALOG_SENSORS; ii++){
				underling_sonar_analog[ii] = new SonarAnalogSensorInterface("/sys/devices/ocp.2/helper.14/AIN1",String.format("gpio%d.txt",ii));
				underling_sonar_analog[ii].giveID(ii + NUM_LASER_SENSORS);
			} */
			underling_sonar_gpio = new SonarGPIOSensorInterface[NUM_GPIO_SENSORS];
			for(int ii = 0; ii < NUM_GPIO_SENSORS; ii++){
				underling_sonar_gpio[ii] = new SonarGPIOSensorInterface( GPIO_PINS[ii*2], GPIO_PINS[(ii*2)+1]);
				underling_sonar_gpio[ii].giveID(ii + NUM_LASER_SENSORS + NUM_ANALOG_SENSORS);
			}
			sensorOrientation = new double[NUM_LASER_SENSORS + NUM_ANALOG_SENSORS + NUM_GPIO_SENSORS];
			sensorPosition = new double[NUM_LASER_SENSORS + NUM_ANALOG_SENSORS + NUM_GPIO_SENSORS];
			ranges = new double[NUM_LASER_SENSORS + NUM_ANALOG_SENSORS + NUM_GPIO_SENSORS];
		} catch(Exception e){
			System.out.println("Error in Sensor Manager construction");
			e.printStackTrace();
		}
	}

	public void init(){
		underling_laser.init();
		for(int ii = 0; ii < underling_sonar_gpio.length; ii++)
			underling_sonar_gpio[ii].init();
	}

	public static SensorManager getInstance(){
		if(theBoss == null) theBoss = new SensorManager();
		return theBoss;
	}

	public void addRange(double range, int ID){
		ranges[ID] = range;
		System.out.println(String.format("ID %d = %f", ID, range));
	}

	public void run(){
		init();
		while(true){
			try{
				//underling_laser.getRanging();
			/*	for(int ii = 0; ii < NUM_ANALOG_SENSORS; ii++)
					underling_sonar_analog[ii].getRanging(); */
				for(int ii = 0; ii < NUM_GPIO_SENSORS; ii++){
					Thread.sleep(10);
					System.out.println("Starting " + (ii + NUM_LASER_SENSORS + NUM_ANALOG_SENSORS));
					underling_sonar_gpio[ii].getRanging();
				}
				Thread.sleep(500);
			} catch(Exception e){
				System.out.println("Error in SensorManager run");
				e.printStackTrace();
			}
		}
	}
/*
	public static void main(String[] args){
		SensorManager sm = SensorManager.getInstance();
		//Thread t = new Thread(SensorManager.getInstance());
		//t.start();
	}
	*/

}
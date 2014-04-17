package util;

public class Param {
	// Pilot Controller
	public static final int throttleHover = 530;
	public static final int throttleLand = 250;
	public static final int loopDelay = 300;
	public static final int statusUpdatedCountMod = 10;
	
	// Throttle - Altitude 
	public static final int throttleMax = 580; //very high, will change
	public static final int throttleMin = 130;
	public static final int throttleDeltaMaxUP = 7;
	public static final int throttleDeltaMaxDOWN = -10;
	public static final double throttleScale = 1;
	public static double kP_t = 0.4;
	public static double kI_t = 0.1;
	public static double kD_t = 0;

	// Pitch - Laser Range Finder
	public static final int pitchMax = 20; //very high, will change
	public static final int pitchMin = -20;
	public static final int pitchDeltaMaxUP = 5;
	public static final int pitchDeltaMaxDOWN = -10;
	public static final double pitchScale = 1;
	public static double kP_p = 0.1;
	public static double kI_p = 0.01;
	public static double kD_p = 0.005;
	
	// Roll - two side sensors
	public static double kP_r = 0.1;
	public static double kI_r = 0.01;
	public static double kD_r = 0.005;

	
	// Yaw - compass
	public static double kP_y = 0.1;
	public static double kI_y = 0.01;
	public static double kD_y = 0.005;

	
	// IRCamera
	public static final int consistencyCount = 10;
	public static final String positive = "1";
	public static final String negative = "0";

	// UART
	public static final String UARTPILOT = "/dev/ttyO1"; // RX: 26 TX: 24 (9)
	public static final String UARTLASER = "/dev/ttyO2"; // RX: 22 TX: 21 (9)
	public static final String UARTXBEE = "/dev/ttyO4";  // RX: 11 TX: 13 (9)
	public static final String UARTIRCAM = "/dev/ttyO5"; // RX: 38 TX: 37 (8)

	// Logger
	public static final String LOGDIR = "../previousLogs/"; // in BBB, you are sitting inside bin/
	public static final String FILEBASE = "log";
	public static final String FILEEND = ".txt";

	//Sensor mananger
	public static final boolean LASER_ACTIVE = true;
	public static final boolean ANALOG_ACTIVE = true;
	public static final boolean GPIO_ACTIVE = true;
}

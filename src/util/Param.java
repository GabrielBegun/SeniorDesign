package util;

public class Param {
	// Throttle - Altitude 
	public static final int throttleMax = 500; //very high, will change
	public static final int throttleMin = 130;
	public static final int throttleDeltaMax = 5;
	public static final double throttleScale = 1;
	public static final double kP_t = 0.08;
	public static final double kI_t = 0.02;
	public static final double kD_t = 0.008;

	// Pitch - Laser Range Finder
	public static final int pitchMax = 20; //very high, will change
	public static final int pitchMin = -20;
	public static final int pitchDeltaMax = 10;
	public static final double pitchScale = 1;
	public static final double kP_p = 0.1;
	public static final double kI_p = 0.01;
	public static final double kD_p = 0.005;

	// IRCamera
	public static final int consistencyCount = 10;
	public static final String positive = "1";
	public static final String negative = "0";

	// UART
	public static final String UARTPILOT = "/dev/ttyO1"; // RX: 26 TX: 24 (9)
	public static final String UARTLASER = "/dev/ttyO2"; // RX: 22 TX: 21 (9)
	public static final String UARTXBEE = "/dev/ttyO4";  // RX: 11 RX: 13 (9)
	public static final String UARTIRCAM = "/dev/ttyO5"; // RX: 38 TX: 37 (8)

	// Logger
	public static final String LOGDIR = "../previousLogs/"; // in BBB, you are sitting inside bin/
	public static final String FILEBASE = "log";
	public static final String FILEEND = ".txt";

	//Sensor mananger
	public static final boolean LASER_ACTIVE = true;
	public static final boolean ANALOG_ACTIVE = true;
	public static final boolean GPIO_ACTIVE = false;
}

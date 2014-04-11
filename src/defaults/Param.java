package defaults;

public class Param {
	// Throttle - Altitude 
	public static final int throttleMax = 500; //very high, will change
	 public static final int throttleMin = 130;
	 public static final int throttleDeltaMax = 10;
	 public static final double throttleScale = 1;
	 public static final double kP_t = 0.1;
	 public static final double kI_t = 0.02;
	 public static final double kD_t = 0.005;
	 
	 // Pitch - Laser Range Finder
	 public static final int pitchMax = 20; //very high, will change
	 public static final int pitchMin = -20;
	 public static final int pitchDeltaMax = 10;
	 public static final double pitchScale = 1;
	 public static final double kP_p = 0.1;
	 public static final double kI_p = 0.02;
	 public static final double kD_p = 0.005;
	 
	 // IRCamera
	 public static final int consistencyCount = 10;
	 public static String positive = "1";
	 public static String negative = "0";
	 
	 // UART
	 public static String UARTPILOT = "/dev/ttyO1";
	 public static String UARTLASER = "/dev/ttyO2";
	 public static String UARTXBEE = "/dev/ttyO4";
	 public static String UARTIRCAM = "/dev/ttyO5";
	 
	 // Logger
	 public static String LOGDIR = "../previousLogs/"; // in BBB, you are sitting inside bin/
	 public static String FILEBASE = "log";
	 public static String FILEEND = ".txt";
		
}

package util;

import java.awt.Color;
import java.awt.Point;

public class Param {
	public static final int SEED = 34;

	/**** Code Defines ****/
	/* code description:
	 *  0 - unassigned
	 *  1 - Wall
	 *  2 - Movable area
	 *  3 - person
	 *  4 - quadcopter
	 *  5 - sensed area
	 *  6 - sensed and searched
	 *  7 - currently sensing
	 *  8 - currently searching
	 *  9 - path (sensed and searched)
	 */
	public static final int UNASSIGNED_CODE = 0;
	public static final int WALL_CODE = 1;
	public static final int MOVABLE_AREA_CODE = 2;
	public static final int PERSON_CODE = 3;
	public static final int QUADCOPTER_CODE = 4;
	public static final int SENSED_AREA_CODE = 5;
	public static final int SENSED_AND_SEARCHED_AREA_CODE = 6;
	public static final int CURRENTLY_SENSING_AREA_CODE = 7;
	public static final int CURRENTLY_SEARCHING_AREA_CODE = 8;
	public static final int PATH_CODE = 9;
	public static final int CUSTOM_COLOR_1 = 10;
	public static final int CUSTOM_COLOR_2 = 11;

	/** End Code Defines **/

	/** Directional Defines **/
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	/** End Directional defines **/


	/** Sensor Map Defines **/
	public static final int SENSOR_MAP_SIZE_X = 90;
	public static final int SENSOR_MAP_SIZE_Y = 90;
	public static final Point SENSOR_MAP_QUADCOPTER_POSITION = new Point(22, 22);
	/** End Sensor Map Defines **/

	/** Dynamic Map Defines **/
	public static final int DYNAMIC_MAP_INITIAL_SIZE_X = 50;
	public static final int DYNAMIC_MAP_INITIAL_SIZE_Y = 50;
	/** End Dynamic Map Defines **/

	/** Sensor Defines **/
	public static final double SONAR_SENSOR_CUTOFF = 6.01;
	public static final double SONAR_SENSOR_MAX_DISTANCE = 8.00;
	public static final double SONAR_SENSOR_MIN_DISTANCE = 0.50;
	public static final double SONAR_SENSOR_RANGING_DELAY = 200; //200us

	public static final double LASER_SENSOR_CUTOFF = 40.00;
	public static final double LASER_SENSOR_MAX_DISTANCE = 41.00;
	public static final double LASER_SENSOR_MIN_DISTANCE = 0.50;
	public static final double LASER_SENSOR_RANGING_DELAY = 200; //200 us
	/** End Sensor Defines **/

	/** GUI Defines **/
	public static final String LOG_FILE_PATH = "log.txt";
	public static final int FRAME_SIZE_X = 450;
	public static final int FRAME_SIZE_Y = 450;
	public static final int FRAME_POSITION_X = 50;
	public static final int FRAME_POSITION_Y = 50;
	public static final int MAP_SIZE_PIXELS_X = 400;
	public static final int MAP_SIZE_PIXELS_Y = 400;
	public static final int MAP_SIZE_FT_X = 200;
	public static final int MAP_SIZE_FT_Y = 200;
	public static final int FT_PER_SQUARE = 2;

	public static final int FRAME_DYNAMIC_SIZE_X = 450;
	public static final int FRAME_DYNAMIC_SIZE_Y = 450;
	public static final int FRAME_DYNAMIC_POSITION_X = 900;
	public static final int FRAME_DYNAMIC_POSITION_Y = 50;

	public static final int FRAME_SENSOR_SIZE_X = 300;
	public static final int FRAME_SENSOR_SIZE_Y = 300;
	public static final int FRAME_SENSOR_POSITION_X = 550;
	public static final int FRAME_SENSOR_POSITION_Y = 50;


	public static final int FRAME_STATS_SIZE_X = 300;
	public static final int FRAME_STATS_SIZE_Y = 400;
	public static final int FRAME_STATS_POSITION_X = 550;
	public static final int FRAME_STATS_POSITION_Y = 400;

	public static final Color FRAME_COLOR = new Color(5, 5, 5, 100);

	public static final Color WALL_COLOR = new Color(0,0,0);		//black
	public static final Color MOVABLE_AREA_COLOR = new Color(72,209,204);	//mediumturquoise
	public static final Color PERSON_COLOR = new Color(255,69,0);	//orangered1
	public static final Color QUADCOPTER_LOCATION_COLOR = new Color(0, 238, 0);	//green2
	public static final Color SENSED_AREA_COLOR = new Color(3, 168, 158);	//manganeseblue
	public static final Color SENSED_AND_SEARCHED_AREA_COLOR = new Color(0, 104, 139);	//deepskyblue4
	public static final Color /* CURRENTLY_SENSING_AREA_COLOR = new Color(135, 206, 255);	//skyblue1*/ CURRENTLY_SENSING_AREA_COLOR = new Color(0, 104, 139);	//deepskyblue4
	public static final Color CURRENTLY_SEARCHING_AREA_COLOR = new Color(255, 255, 0);	//yellow
	public static final Color PATH_COLOR = new Color(0, 0, 255);		//blue1
	public static final Color CUSTOM_COLOR = new Color(255, 0, 0);		//red1
	/** End GUI defines **/


	/** Generation Defines **/
	public static final int HALLWAY_LENGTH_FT = 6;	//if this isn't a multiple of ft/square then crash
	public static final int NUMBER_OF_ROOMS = 8;
	public static final int NUMBER_OF_SURVIVORS = 10;
	public static final int MINIMUM_SIZE_OF_ROOM_FT = 20;		//has to match with ft/square
	public static final int MAXIMUM_SIZE_OF_ROOM_FT = 40;		//has to match with ft/sq
	public static final int MAXIMUM_NUMBER_OF_DOORS_PER_ROOM = 2;
	public static final int DOOR_SIZE_FT = 4;
	public static final int HALLWAY_INCREMENTS = 1000;
	public static final int ROOM_CONNECTIONS_MIN = 2;
	public static final int ROOM_CONNECTIONS_MAX = 3;

	public static final double PROBABILITY_OF_ROOM = 0.1;
	public static final double PROBABILITY_OF_FORK = 0.05;
	public static final double PROBABILITY_OF_TURN = 0.05;
	public static final double PROBABILITY_OF_DEADEND = 0.01;

	public static final String SAMPLE_MAP_1 = "sample_map_1.txt";
	public static final String SAMPLE_MAP_2 = "sample_map_2.txt";

	public static final int TRY_ATTEMPTS = 100;

	//Quadcopter
	public static final int NUMBER_OF_SENSORS = 16;		//can be 8 or 16
	public static final int SENSOR_RANGE_FT = 8;		//anything else may have undefined results

	public static final int INTERNAL_VIEW_X_PX = 200;
	public static final int INTERNAL_VIEW_Y_PX = 200;
	/** End Generation Defines **/

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

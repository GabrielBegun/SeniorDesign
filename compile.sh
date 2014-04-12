# Compile the quadcopter source
COMM_PACKAGE="src/communication/IRCamera.java src/communication/Pilot.java src/communication/PilotController.java src/communication/XbeeInterface.java"
UTIL_PACKAGE="src/util/UartDriver.java src/util/Param.java"
LOGGER_PACKAGE="src/Logger/Logger.java"
FIRESCOUT_PACKAGE="src/FireScout/FireScout.java"
SENSOR_PACKAGE="src/sensor/SensorManager/SensorManager.java src/sensor/SensorManager/LaserSensorInterface.java src/sensor/SensorManager/SonarGPIOSensorInterface.java src/sensor/SensorManager/SonarAnalogSensorInterface.java"
RXTX_JAR="lib/RXTXcomm.jar"
OUTPUT_FOLDER="bin"
ERROR_FILE="error.txt"

javac -d $OUTPUT_FOLDER -classpath $RXTX_JAR $LOGGER_PACKAGE $COMM_PACKAGE $UTIL_PACKAGE $FIRESCOUT_PACKAGE $SENSOR_PACKAGE 2> $ERROR_FILE

gcc -o s_gpio src/sensor/SensorManager/sonar_gpio.c
mv src/sensor/SensorManager/s_gpio bin/sensor/SensorManager/

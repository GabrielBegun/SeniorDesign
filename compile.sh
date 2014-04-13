# Compile the quadcopter source
cd /home/root/SeniorDesign
JAVA_HOME=/home/root/java/jdk1.7.0_51/

COMM_PACKAGE="src/communication/IRCamera.java src/communication/Pilot.java src/communication/PilotController.java src/communication/XbeeInterface.java"
UTIL_PACKAGE="src/util/UartDriver.java src/util/Param.java"
LOGGER_PACKAGE="src/Logger/Logger.java"
FIRESCOUT_PACKAGE="src/FireScout/FireScout.java"
SENSOR_PACKAGE="src/sensor/SensorManager/SensorManager.java src/sensor/SensorManager/LaserSensorInterface.java src/sensor/SensorManager/SonarGPIOSensorInterface.java src/sensor/SensorManager/SonarAnalogSensorInterface.java"
RXTX_JAR="lib/RXTXcomm.jar"
OUTPUT_FOLDER="/home/root/SeniorDesign/bin"
ERROR_FILE="/home/root/logs/compilation_errors.txt"

echo `pwd` >> ERROR_FILE

/home/root/java/jdk1.7.0_51/bin/javac -d $OUTPUT_FOLDER -classpath $RXTX_JAR $LOGGER_PACKAGE $COMM_PACKAGE $UTIL_PACKAGE $FIRESCOUT_PACKAGE $SENSOR_PACKAGE 2>> $ERROR_FILE

gcc -o bin/sensor/SensorManager/s_gpio src/sensor/SensorManager/sonar_gpio.c

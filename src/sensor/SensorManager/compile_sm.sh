#compile pilot
./clean.sh
gcc -o s_gpio sonar_gpio.c
javac -cp /usr/share/java/rxtx.jar SensorManager.java LaserSensorInterface.java SonarAnalogSensorInterface.java UartDriver.java SonarGPIOSensorInterface.java

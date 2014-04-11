#compile pilot
./clean.sh
gcc -o s_gpio sonar_gpio.c
javac -cp /usr/share/java/rxtx.jar SensorManager.java LaserSensorInterface.java UartDriver2.java Quadcopter.java SonarGPIOSensorInterface.java

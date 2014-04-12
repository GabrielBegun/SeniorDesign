#!/bin/bash
scp compile_sm.sh run_sm.sh LaserSensorInterface.java SensorManager.java UartDriver.java clean.sh SonarGPIOSensorInterface.java sonar_gpio.c SonarAnalogSensorInterface.java root@192.168.7.2:~/sensor_manager

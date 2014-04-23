This folder contains the main code for the Senior Design Project called Fire Scout, class of 2014. The team members of the team were Gabriel Begun, Mikhail Andreev, Jonah Lou, Kelsey Warda and Benjamin Corman.

This code is meant to be run in a BeagleBoard Black and it contains the main body of our project. The majority of the code is written in Java but this folder also contains some VERY useful shell scripts that ran on boot up. 

---------
Java code
---------

Code structure:

Fire Scout
	Logger
	XbeeInterface
		UartDriver
	IRCamera
		UartDriver
	PilotController
		Pilot
			UartDriver
	SensorManager
		SonarAnalogSensorInterface
		SonarGPIOSensorInterface
		LaserSensorInterface
			UartDriver
	Params
	
Most classes have private constructors, this allows us to create a static instance of everything and it allows us for any part of the code to call another function. A good example of the uses a private constructor has can be seen by following the logger class	

Additionally, there are certain classes that implement Runnable. The idea behind this is to be able to continuously (or nearly) execute. This files are the PilotController, the SensorManager and the (to be added) Navigation. The main thread of the program is in the FireScout class.

Also worth mentioning is the use of the Params and UartDriver classes located in the Util folder. These classes are shared by all. The UartDriver provides other classes with a UART they can use to communicate with other hardware components. The Params class gives a set of parameters that are used by other classes. They can be thought of as global macros. The information of which UART should be connected to which component can be found in the Params class.

The gui and map folders contain files that are not integrated with the rest of the system and involved an earlier version of the navigation code. The most recent navigation code can be found in a separate folder created by Mikhail.

Lastly, it is important to note that without an XBee connection, the program will not be doing much. The program's main code consists of an FSM that will only change stages if it receives the appropriate command to do so. (look at the FireScout class). Also, please note the way things are initialized. All constructors are called before any init() or thread is created.

					
-------------
Shell Scripts 
-------------

The shell scripts provided are VERY useful for effective code work. Although they are not needed for the program to run, they sure help make work easier.

Our system used crontab to execute onReboot.sh after 10 seconds. What the scripts do is connect to github to update the source code, compile the code and run. This allowed us to update on reboot by simply connecting an Ethernet cable to the BeagleBoard. Nice right? 

Note that the scripts may not work perfectly on the future - for example, if the git gets changed.

-----------
Other files
-----------

Flight logs are stored under the previousLogs folder

The scripts also write log files that are not part of the program. Those are sent as an email to us by the mail.py program and then erased

Inside Lib there are two files RXTXcomm.jar and rxtxSerial.dll. These files are used to get the UartDriver working. 

If you have any other questions feel free to contact me (Gabriel) at begunbegun@gmail.com
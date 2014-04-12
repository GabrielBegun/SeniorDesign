# run pilot
stty -F /dev/ttyO2 raw
stty -F /dev/ttyO2 9600
echo cape-bone-iio > /sys/devices/bone_capemgr.8/slots 
echo BB-UART1 > /sys/devices/bone_capemgr.8/slots 
echo BB-UART2 > /sys/devices/bone_capemgr.8/slots 
echo BB-UART4 > /sys/devices/bone_capemgr.8/slots 
echo BB-UART5 > /sys/devices/bone_capemgr.8/slots 
JAVA_HOME=/home/root/java/jdk1.7.0_51/
rm /var/lock/LCK..ttyO2
cd /home/root/sensor_manager
/home/root/java/jdk1.7.0_51/bin/java -cp .:/usr/share/java/rxtx.jar -Djava.library.path=/usr/lib/jni/ -Dgnu.io.rxtx.SerialPorts=/dev/ttyO2 SensorManager 2>> error.txt

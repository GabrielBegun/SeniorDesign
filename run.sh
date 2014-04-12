#run stuff
rm /var/lock/LCK..*
echo cape-bone-iio > /sys/devices/bone_capemgr.8/slots 
echo BB-UART1 > /sys/devices/bone_capemgr.8/slots
echo BB-UART2 > /sys/devices/bone_capemgr.8/slots
echo BB-UART4 > /sys/devices/bone_capemgr.8/slots
echo BB-UART5 > /sys/devices/bone_capemgr.8/slots
stty -F /dev/ttyO1 9600 raw
stty -F /dev/ttyO2 9600 raw
stty -F /dev/ttyO4 9600 raw
stty -F /dev/ttyO5 9600 raw
JAVA_HOME=/home/root/java/jdk1.7.0_51/
cd bin
/home/root/java/jdk1.7.0_51/bin/java -cp .:/usr/share/java/rxtx.jar -Djava.library.path=/usr/lib/jni/ -Dgnu.io.rxtx.SerialPorts=/dev/ttyO1:/dev/ttyO2:/dev/ttyO4:/dev/ttyO5 FireScout.FireScout 2>> "run_error.txt"

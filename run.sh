#run stuff
echo "Removing lock files..."
rm /var/lock/LCK..*
echo "Setting up analog ports..."
echo cape-bone-iio > /sys/devices/bone_capemgr.8/slots
echo "Setting up UART ports..." 
echo BB-UART1 > /sys/devices/bone_capemgr.8/slots
echo BB-UART2 > /sys/devices/bone_capemgr.8/slots
echo BB-UART4 > /sys/devices/bone_capemgr.8/slots
echo BB-UART5 > /sys/devices/bone_capemgr.8/slots
stty -F /dev/ttyO1 9600 raw
stty -F /dev/ttyO2 9600 raw
stty -F /dev/ttyO4 9600 raw
stty -F /dev/ttyO5 9600 raw
echo "Setting up java paths..."
JAVA_HOME=/home/root/java/jdk1.7.0_51/
cd /home/root/SeniorDesign
cd bin
echo "Starting main code..."
/home/root/java/jdk1.7.0_51/bin/java -cp .:/usr/share/java/rxtx.jar -Djava.library.path=/usr/lib/jni/ -Dgnu.io.rxtx.SerialPorts=/dev/ttyO1:/dev/ttyO2:/dev/ttyO4:/dev/ttyO5 FireScout.FireScout 2>> "/home/root/logs/execution_error.txt"

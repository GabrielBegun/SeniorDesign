#run stuff
echo BB-UART1 > /sys/devices/bone_capemgr.8/slots
echo BB-UART2 > /sys/devices/bone_capemgr.8/slots
echo BB-UART4> /sys/devices/bone_capemgr.8/slots
echo BB-UART5 > /sys/devices/bone_capemgr.8/slots
stty -F /dev/ttyO1 9600 raw
stty -F /dev/ttyO2 9600 raw
stty -F /dev/ttyO4 9600 raw
stty -F /dev/ttyO5 9600 raw
cd bin
java -cp .:/usr/share/java/rxtx.jar -Djava.library.path=/usr/lib/jni/ -Dgnu.io.rxtx.SerialPorts=/dev/ttyO4 FireScout.FireScout 2>> "run_error.txt"

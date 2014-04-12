#script to run on reboot
cd /home/root/source/SeniorDesign
./update.sh
echo "update"
echo 65 > /sys/class/gpio/export
echo in > /sys/class/gpio/gpio65/direction

val=$(cat /sys/class/gpio/gpio65/value)

if [[ "$val" == "1" ]]
	then 
		echo "compile"
		./compile.sh
		echo "run"
		./run.sh
else
	echo "stop"
fi

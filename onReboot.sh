#script to run on reboot
echo "Sleeping..."
echo 46 > /sys/class/gpio/export
echo out > /sys/class/gpio/gpio46/direction
echo 1 > /sys/class/gpio/gpio46/value
sleep 10

echo "Try to send mail...."
BASE_DIRECTORY="/home/root/SeniorDesign"
LOG_DIRECTORY="/home/root/logs"
$BASE_DIRECTORY/send_mail.sh
sleep 10

echo "Updating..."
mkdir $BASE_DIRECTORY/bin
rm $LOG_DIRECTORY/*.txt
printf "\n\n" >> $LOG_DIRECTORY/update_log.txt
echo $(date) >> $LOG_DIRECTORY/update_log.txt
printf "\n\n" >> $LOG_DIRECTORY/onRebootRun_log.txt
echo $(date) >> $LOG_DIRECTORY/onRebootRun_log.txt
printf "\n\n" >> $LOG_DIRECTORY/compile_log.txt
echo $(date) >> $LOG_DIRECTORY/compile_log.txt
printf "\n\n" >> $LOG_DIRECTORY/run_log.txt
echo $(date) >> $LOG_DIRECTORY/run_log.txt
$BASE_DIRECTORY/update.sh &>> $LOG_DIRECTORY/update_log.txt
echo "update" >> $LOG_DIRECTORY/onRebootRun_log.txt
echo 65 > /sys/class/gpio/export
echo in > /sys/class/gpio/gpio65/direction

val=$(cat /sys/class/gpio/gpio65/value)


echo "Update complete..."
if [[ "$val" == "1" ]]
	then 
		echo "Compiling..."
		echo "compile" >> $LOG_DIRECTORY/onRebootRun_log.txt
		$BASE_DIRECTORY/compile.sh &>> $LOG_DIRECTORY/compile_log.txt
		cp $BASE_DIRECTORY/bin/sensor/SensorManager/s_gpio $BASE_DIRECTORY/bin/s_gpio
		echo 0 > /sys/class/gpio/gpio46/value
		sleep 1
		echo 1 > /sys/class/gpio/gpio46/value
		echo "Running..."
		echo "run" >> $LOG_DIRECTORY/onRebootRun_log.txt
		$BASE_DIRECTORY/run.sh &>> $LOG_DIRECTORY/run_log.txt
else
	echo "stop"
	while :
	do
		echo 0 > /sys/class/gpio/gpio46/value
		sleep 1
		echo 1 > /sys/class/gpio/gpio46/value
		sleep 1
	done
fi

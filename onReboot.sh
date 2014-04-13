#script to run on reboot
BASE_DIRECTORY="/home/root/SeniorDesign"
LOG_DIRECTORY="/home/root/logs"
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

if [[ "$val" == "1" ]]
	then 
		echo "compile" >> $LOG_DIRECTORY/onRebootRun_log.txt
		$BASE_DIRECTORY/compile.sh &>> $LOG_DIRECTORY/compile_log.txt
		echo "run" >> $LOG_DIRECTORY/onRebootRun_log.txt
		$BASE_DIRECTORY/run.sh &>> $LOG_DIRECTORY/run_log.txt
else
	echo "stop"
fi

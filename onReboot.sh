#script to run on reboot
cd /home/root/source/SeniorDesign
./update.sh
echo 65 > /sys/class/gpio/export

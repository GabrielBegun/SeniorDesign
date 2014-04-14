#SEND MAIL
cd /home/root/logs

if [ ! -f version ]; then
	echo 1 > version 
fi
VER=$(cat version)

python mail.py $VER compilation_errors.txt
python mail.py $VER compile_log.txt
python mail.py $VER execution_error.txt
python mail.py $VER onRebootRun_log.txt
python mail.py $VER run_log.txt
python mail.py $VER update_log.txt

((VER++))
echo $VER > version

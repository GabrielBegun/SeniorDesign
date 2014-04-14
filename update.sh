#update script 
cd /home/root/SeniorDesign
git fetch --all
git reset --hard origin/master
mv mail.py ../logs
chmod 777 run.sh
chmod 777 compile.sh
chmod 777 update.sh
chmod 777 onReboot.sh
chmod 777 send_mail.sh
#rm run_error.txt
#rm error.txt

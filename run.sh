#run stuff
cd bin
java -cp .:../lib/RXTXcomm.jar -Djava.library.path=/lib/ FireScout.FireScout 2> ../"run_error.txt"

package communication;

//import java.io.IOException;
//import java.util.ArrayList;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

//import java.util.Queue;
//import java.util.LinkedList;







import java.util.TooManyListenersException;
import java.io.*;

import FireScout.FireScout;
import Logger.Logger;
import util.Param;
import util.UartDriver;
import util.UartDriver.UartFailException;

public class XbeeInterface {

	private UartDriver uartXbee;
	private FireScout fireScout;
	private Logger logger;

	private static XbeeInterface myXbee;

	private XbeeInterface() {
	}


	public static XbeeInterface getInstance() {
		if (myXbee == null)
			myXbee = new XbeeInterface();
		return myXbee;
	}

	public void init() throws TooManyListenersException, UartFailException {
		uartXbee = new UartDriver(Param.UARTXBEE);
		uartXbee.initialize();
		uartXbee.serialPort.addEventListener(new xBeeSerialPortEventListener()); // Throws
		fireScout = FireScout.getInstance();
		logger = Logger.getInstance();
		this.write("XBeeInterface: Connection started, WE BUILT THIS CITY!");
	}
	
	public synchronized void write(String str) {
		try{
			uartXbee.output.write((str + "\n").getBytes());
		} catch (IOException e) {
			System.out.println("Error writing to xBee" + str);
		}
		return;
	}

	private class xBeeSerialPortEventListener implements
			SerialPortEventListener {
		public synchronized void serialEvent(SerialPortEvent oEvent) {
			if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				try {
					String str = uartXbee.input.readLine();
					fireScout.parseXBeeCommand(str);
					logger.writeStandard("XbeeInterface: "+str);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else { // Ignore all the other eventTypes, but you should consider
				// the other ones.
				System.out.println("Serial Port Event not handled received");
			}

		}
	}

}
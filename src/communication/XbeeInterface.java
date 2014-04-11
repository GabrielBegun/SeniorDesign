package communication;

//import java.io.IOException;
//import java.util.ArrayList;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

//import java.util.Queue;
//import java.util.LinkedList;


import java.util.TooManyListenersException;
import java.io.*;

import defaults.Param;
import FireScout.FireScout;
import util.UartDriver;

public class XbeeInterface {

	private UartDriver uartXbee;
	private FireScout fireScout;

	private static XbeeInterface myXbee;

	private XbeeInterface() {
	}

	public static XbeeInterface getInstance() {
		if (myXbee == null)
			myXbee = new XbeeInterface();
		return myXbee;
	}

	public void init() throws TooManyListenersException{
		uartXbee = new UartDriver(Param.UARTXBEE);
		uartXbee.initialize();
		uartXbee.serialPort.addEventListener(new xBeeSerialPortEventListener()); // Throws
		fireScout = FireScout.getInstance();
	}
	
	public void write(String str) throws IOException {
		uartXbee.output.write(str.getBytes());
		System.out.println("XBEE");
		return;
	}

	private class xBeeSerialPortEventListener implements
			SerialPortEventListener {
		public synchronized void serialEvent(SerialPortEvent oEvent) {
			if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				try {
					String str = uartXbee.input.readLine();
					fireScout.parseCommand(str);
					// Logger
				} catch (Exception e) {
					System.err.println(e.toString()); // TODO
				}
			} else { // Ignore all the other eventTypes, but you should consider
				// the other ones.
				System.out.println("Serial Port Event not handled received");
			}

		}
	}

}
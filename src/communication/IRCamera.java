package communication;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.util.TooManyListenersException;

import Logger.Logger;
import util.Param;
import util.UartDriver;
import util.UartDriver.UartFailException;

/* This class interfaces with the RPI, which is connected to the DRS thermal camera and is running HOG
 * One of the future plans is to add feature detection to go through doors. This feature is not implemented */

public class IRCamera {
	private UartDriver uartRPI;
	private Logger logger;
	private XbeeInterface xbeeInterface;

	private static IRCamera myIRCamera;

	private IRCamera() {
	}

	public static IRCamera getInstance() {
		if (myIRCamera == null)
			myIRCamera = new IRCamera();
		return myIRCamera;
	}

	public void init() throws TooManyListenersException, UartFailException{
		uartRPI = new UartDriver(Param.UARTIRCAM);
		uartRPI.initialize();
		uartRPI.serialPort.addEventListener(new IRCameraSerialPortEventListener()); // Throws
		logger = Logger.getInstance();
		xbeeInterface = XbeeInterface.getInstance();
	}

	private int matchCounter = 0;
	private class IRCameraSerialPortEventListener implements
	SerialPortEventListener {
		public synchronized void serialEvent(SerialPortEvent oEvent) {
			if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				try {
					String str = uartRPI.input.readLine();
					if(str.equals(Param.positive)) {
						matchCounter++;
						if(matchCounter == Param.consistencyCount){
							// Call function TODO
							logger.writeStandard("IRCamera: Person detected");
							//System.out.println("IRCamera: Person detected");
							xbeeInterface.write("IRCamera: Person detected");
							matchCounter = 0;
						}
					} else if(str.equals(Param.negative)){
						// NO NEED TO DO THIS
						matchCounter = (matchCounter>1) ? matchCounter-1 : 0;
						logger.writeStandard("IRCamera: Person NOT detected");
						//System.out.println("IRCamera: Person NOT detected");
						xbeeInterface.write("IRCamera: Person NOT detected");
					}
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
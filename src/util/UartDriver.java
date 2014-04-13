package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier; 
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.util.Enumeration;

// When creating a UartDriver it is important to create a Listener for it. No default Listener is provided
public class UartDriver {
	public UartDriver(String str){
		port_name = str;
	}

	public SerialPort serialPort;
	private String port_name;

	/**
	 * A BufferedReader which will be fed by a InputStreamReader 
	 * converting the bytes into characters 
	 * making the displayed results codepage independent
	 */
	public BufferedReader input;
	/** The output stream to the port */
	public OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	public void initialize() throws UartFailException {
		CommPortIdentifier portId = null;

		@SuppressWarnings("rawtypes")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			if (currPortId.getName().equals(port_name)) { 
				portId = currPortId;
				System.out.println("Connected to " + currPortId.getName());
				break;
			}
		}
		if (portId == null) {
			throw( new UartFailException("Could not find COM port " + port_name));
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			serialPort.notifyOnDataAvailable(true);
			
		} catch (PortInUseException e) {
			throw( new UartFailException("PortInUseException " + port_name));
		} catch (UnsupportedCommOperationException e) {
			throw( new UartFailException("UnsupportedCommOperationException " + port_name));
		} catch (IOException e) {
			throw( new UartFailException("IOException " + port_name));
			
		}
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
	
	public class UartFailException extends Exception {
		private static final long serialVersionUID = 1L;
		public UartFailException () {}
		public UartFailException (String str) { super(str); } 
	}
}

package communication;


import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.util.Queue;
import java.util.LinkedList;
import java.util.TooManyListenersException;
import java.io.*;

import Logger.Logger;
import util.Param;
import util.UartDriver;
import util.UartDriver.UartFailException;

public class Pilot{
	private static Pilot myPilot;
	private Pilot() throws TooManyListenersException, UartFailException { 
		uartArduPilot = new UartDriver(Param.UARTPILOT); 
		uartArduPilot.initialize();
		uartArduPilot.serialPort.addEventListener(new PilotSerialPortEventListener()); // Throws, fails if initialize fails
		logger = Logger.getInstance();
		xBeeInterface = XbeeInterface.getInstance();
	}
	public static Pilot getInstance() throws TooManyListenersException, UartFailException{
		if(myPilot == null) myPilot = new Pilot();
		return myPilot;
	}

	private UartDriver uartArduPilot;
	private Logger logger;
	private XbeeInterface xBeeInterface;
	private static Queue<String> messageQueue = new LinkedList<String>();

	// Variables read from ArduPilot
	private boolean flyMode;
	private boolean armMode;
	private double accX;
	private double accY;
	private double accZ;
	private double gyroX;
	private double gyroY;
	private double gyroZ;
	private double compassX;
	private double compassY;
	private double compassZ;
	private double battery1;
	private double battery2;
	private double battery3;

	public void sendMessage() {
		try {
			String message = String.format("ss%02d ",messageQueue.size());
			while(messageQueue.size() > 0){
				message += messageQueue.poll();
			}
			message += "\n";
			uartArduPilot.output.write(message.getBytes());
			logger.writeStandard("PilotOut: "+message);
			xBeeInterface.write("PilotOut: "+message);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return;
	}


	public void setPitch(int v){ messageQueue.add(String.format("p%04d ",v)); }
	public void setYaw(int v){ messageQueue.add(String.format("y%04d ",v)); }
	public void setRoll(int v){ messageQueue.add(String.format("r%04d ",v)); }
	public void setThrottle(int v){ messageQueue.add(String.format("t%04d ",v)); }
	public void setArmed(int v){ messageQueue.add(String.format("a%04d ",v)); }  
	public void powerOff() {
		messageQueue.add("z0001");
		sendMessage();
		return;
	}

	public boolean getFlyMode() { return flyMode; }
	public boolean getArmMode() { return armMode; }
	public double getAccX() { return accX; }
	public double getAccY() { return accY; }
	public double getAccZ() { return accZ; }
	public double getGyroX() { return gyroX; }
	public double getGyroY() { return gyroY; }
	public double getGyroZ() { return gyroZ; }
	public double getCompassX() { return compassX; }
	public double getCompassY() { return compassY; }
	public double getCompassZ() { return compassZ; }
	public double getBattery1() { return battery1; }
	public double getBattery2() { return battery2; }
	public double getBattery3() { return battery3; }

	public void parseCommand(String str){
		if (str.length() < 8)
			return;
		else {
			String dataArray[] = str.substring(9).split(",");
			
			switch (dataArray[0].substring(0, 1)) {
				case "a" :
					accX = Double.parseDouble(dataArray[0].substring(2));
					accY = Double.parseDouble(dataArray[1].substring(2));
					accZ = Double.parseDouble(dataArray[2].substring(2));
					break;
				case "g" :
					gyroX = Double.parseDouble(dataArray[0].substring(2));
					gyroY = Double.parseDouble(dataArray[1].substring(2));
					gyroZ = Double.parseDouble(dataArray[2].substring(2));
					break;
				case "c" :
					compassX = Double.parseDouble(dataArray[0].substring(2));
					compassY = Double.parseDouble(dataArray[1].substring(2));
					compassZ = Double.parseDouble(dataArray[2].substring(2));
					break;
				case "b" :
					battery1 = Double.parseDouble(dataArray[0].substring(2));
					battery2 = Double.parseDouble(dataArray[1].substring(2));
					battery3 = Double.parseDouble(dataArray[2].substring(2));
					break;
				case "m" :
					if (dataArray[0].substring(2) == "0")
						flyMode = false;
					else
						flyMode = true;
					
					if (dataArray[1].substring(2) == "0")
						armMode = false;
					else
						armMode = true;
					break;
					
			}
		}
	}

	private class PilotSerialPortEventListener implements SerialPortEventListener{
		public synchronized void serialEvent(SerialPortEvent oEvent) {
			if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				try{
					String str = uartArduPilot.input.readLine();
					parseCommand(str);
					logger.writeStandard("PilotIn: "+str);
					xBeeInterface.write("PilotIn: "+str);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else { // Ignore all the other eventTypes, but you should consider the other ones.
				System.out.println("Serial Port Event not handled received");
			}

		}
	}

} 
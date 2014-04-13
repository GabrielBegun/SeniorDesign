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
	private double accX;
	private double accY;
	private double accZ;
	private double gyroX;
	private double gyroY;
	private double gyroZ;
	private int battery;
	private int compass;

	String temp;
	public void sendMessage() {
		try {
			uartArduPilot.output.write(String.format("s%02d\n",messageQueue.size()).getBytes());
			//System.out.println(String.format("s%02d",messageQueue.size()));
			while(messageQueue.size() > 0){
				temp = messageQueue.poll();
				uartArduPilot.output.write(temp.getBytes());
				logger.writeStandard("PilotOut: "+temp);
				xBeeInterface.write("PilotOut:"+temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return;
	}


	public void setPitch(int v){ messageQueue.add(String.format("p%04d\n",v)); }
	public void setYaw(int v){ messageQueue.add(String.format("y%04d\n",v)); }
	public void setRoll(int v){ messageQueue.add(String.format("r%04d\n",v)); }
	public void setThrottle(int v){ messageQueue.add(String.format("t%04d\n",v)); }
	public void setArmed(int v){ messageQueue.add(String.format("a%04d\n",v)); }  
	public void powerOff() {
		messageQueue.add("z0001");
		sendMessage();
		return;
	}

	public boolean getFyMode() { return flyMode; }
	public double getAccX() { return accX; }
	public double getAccY() { return accY; }
	public double getAccZ() { return accZ; }
	public double getGyroX() { return gyroX; }
	public double getGyroY() { return gyroY; }
	public double getGyroZ() { return gyroZ; }
	public int getBattery() { return battery; }
	public int getCompass() { return compass; }

	public void parseCommand(String str){
		//	  String delims = ",";
		//	  String messageTokens[] = str.split(delims);
		// TODO
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
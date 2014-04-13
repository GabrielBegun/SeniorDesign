package sensor.SensorManager;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.util.TooManyListenersException;
import java.io.*;

import util.Param;
import util.UartDriver;
import util.UartDriver.UartFailException;

public class LaserSensorInterface{
	private UartDriver uart;
	private SensorManager theBoss;
	private int ID;

	public LaserSensorInterface() throws TooManyListenersException, UartFailException{
		uart = new UartDriver(Param.UARTLASER);
		uart.initialize();
		uart.serialPort.addEventListener(new LaserSerialPortEventListner());
	}

	public void init(){
		theBoss = SensorManager.getInstance();
	}

	private void sendRangeCommand() throws IOException{
		uart.output.write("D".getBytes());
	}

	private class LaserSerialPortEventListner implements SerialPortEventListener{
		public synchronized void serialEvent(SerialPortEvent oEvent){
			String str = "";
			if(oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE){
				try{
					str = uart.input.readLine();
					if(!str.equals("--.--"))
						theBoss.addRange(Double.parseDouble(str), ID);
					else
						theBoss.addRange(-1, ID);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else{
				System.out.println("Serial Port Event not handled received");			}
		}
	}

	public void getRanging() throws IOException{
		sendRangeCommand();
	}

	public void giveID(int ID){
		this.ID = ID;
	}
/*
	public static void main(String[] args){
		try{
			LaserSensorInterface lsi = LaserSensorInterface.getInstance();
			lsi.getRanging();
		} catch(Exception e){
			System.out.println("Exception occured");
			e.printStackTrace();
		}
	}
	*/
}
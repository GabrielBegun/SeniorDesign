package sensor.SonarAnalog;

import java.io.*;


public class SonarTest{
	public static void main(String[] args){
		SonarAnalogSensorInterface2 sa = new SonarAnalogSensorInterface2("/sys/devices/ocp.2/helper.14/AIN1");
		while(true){
			System.out.print(sa.getRanging());
			System.out.println(" cm.");
			try{Thread.sleep(30);} catch(Exception e){}
			try{
				Thread.sleep(500);
			} catch(Exception e){

			}
		}
	}
}

class SonarAnalogSensorInterface2{
	String port;
	public SonarAnalogSensorInterface2(String port){
		this.port = port;
		System.out.println("Sensor Active.");
	}

	public double getRanging(){
		double rr = -1, rr1, rr2, rr3;
		try{
			BufferedReader br = new BufferedReader(new FileReader(port));
			String range = br.readLine();
			br.close();
			rr1 = Double.parseDouble(range);
			rr1 /= 3.2;
			try{Thread.sleep(30);} catch(Exception e){}
			br = new BufferedReader(new FileReader(port));
			range = br.readLine();
			br.close();
			rr2 = Double.parseDouble(range);
			rr2 /= 3.2;
			try{Thread.sleep(30);} catch(Exception e){}
			br = new BufferedReader(new FileReader(port));
			range = br.readLine();
			br.close();
			rr3 = Double.parseDouble(range);
			rr3 /= 3.2;
			rr = (rr1 + rr2 + rr3)/3.0;
		} catch(IOException e){
			System.out.println("Error. IOException with reading port");
		}
		return rr;
	}
}
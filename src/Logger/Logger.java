package Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import defaults.Param;

public class Logger {
	private static Logger myLogger;

	private Logger() {
	}

	public static Logger getInstance() {
		if (myLogger == null)
			myLogger = new Logger();
		return myLogger;
	}

	private static FileWriter fw;
	private static BufferedWriter bw;

	public void init() {

		File dir = new File(Param.LOGDIR);
		File files[] = dir.listFiles();
		String filename = Param.LOGDIR + Param.FILEBASE + files.length +  Param.FILEEND;
		
		try {
			fw = new FileWriter(filename);
			bw = new BufferedWriter(fw);
			
		} catch (IOException e) {

			System.out.printf("ERROR: %s\n", e);
		}
		writeStandard("Logger: logger created");
	}
	
	public void writeStandard(String msg) {
		Date date = new Date();	
		try {
			bw.write("[S]" + date.toString() + " " + msg + "\n");
			bw.flush();
		} catch (IOException e) {
			System.out.println("Error writing to file");
		}
	}
	
	public void writeWarning(String msg) {
		Date date = new Date();	
		try {
			bw.write("[W]" + date.toString() + " " + msg + "\n");
			bw.flush();
		} catch (IOException e) {
			System.out.println("Error writing to file");
		}
	}
	
	public void writeError(String msg) {
		Date date = new Date();	
		try {
			bw.write("[E]" + date.toString() + " " + msg + "\n");
			bw.flush();
		} catch (IOException e) {
			System.out.println("Error writing to file");
		}
	}
	
	/* Main for testing 
	public static void main (String[] args){
		Logger logger = Logger.getInstance();
		logger.init();
		logger.writeError("HEY THERE BUDDY!");
	}
	*/
}

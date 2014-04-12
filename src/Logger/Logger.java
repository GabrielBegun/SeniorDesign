package Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import util.Param;

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
		try {
			File dir = new File(Param.LOGDIR);
			File files[] = dir.listFiles();
			String filename;
			if(files != null) 
				filename = Param.LOGDIR + Param.FILEBASE + files.length +  Param.FILEEND;
			else 
				filename = "Error_log.txt";
		
		
			fw = new FileWriter(filename);
			bw = new BufferedWriter(fw);
			
		} catch (IOException e) {
			System.out.printf("ERROR: %s\n", e);
		
		}
		writeStandard("Logger: logger created");
	}
	
	public synchronized void writeStandard(String msg) {
		Date date = new Date();	
		try {
			bw.write("[S]" + date.toString() + " " + msg + "\n");
			bw.flush();
		} catch (IOException e) {
			System.out.println("Error writing to file");
		}
	}
	
	public synchronized void writeWarning(String msg) {
		Date date = new Date();	
		try {
			bw.write("[W]" + date.toString() + " " + msg + "\n");
			bw.flush();
		} catch (IOException e) {
			System.out.println("Error writing to file");
		}
	}
	
	public synchronized void writeError(String msg) {
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

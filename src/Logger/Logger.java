package Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

	}

	public void write(String str) {
		try {
			bw.write(str + "\n");
			bw.flush();
		} catch (IOException e) {
			System.out.println("Error writing to file");
		}
	}
	
	/* Main for testing
	public static void main (String[] args){
		Logger logger = Logger.getInstance();
		logger.init();
		logger.write("HEY THERE BUDDY!");
	}
	*/
}

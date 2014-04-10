package Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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
			// create text file within previousLogs directory
			fw = new FileWriter(
					"previousLogs/myLog.txt");
			bw = new BufferedWriter(fw);

		} catch (IOException e) {

			System.out.printf("ERROR: %s\n", e);
		}

	}

	public void write(String str) {
		// function to write to file
		try {
			bw.write(str + "\n");
			// save changes to file via flush command
			bw.flush();
		} catch (IOException e) {
			System.out.println("Error writing to file");
		}
	}
}

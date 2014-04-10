package Logger;

public class Logger {
	private static Logger myLogger;
	private Logger(){ }
	
	public static Logger getInstance(){
		if(myLogger == null) myLogger = new Logger();
		return myLogger;
	}
	
	public void init(){
		
	}
}

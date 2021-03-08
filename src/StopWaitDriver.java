
/**
 *  StopWaitDriver class
 * 
 * CPSC 441
 * Assignment 4
 * 
 * @author 	Majid Ghaderi
 * @version 2021
 *
 */


import java.io.*;
import java.util.*;
import java.util.logging.*;


public class StopWaitDriver {

	private static final Logger logger = Logger.getLogger("StopWaitFtp"); // global logger
	
	public static void main(String[] args) {
		// input file name is required
		if (args.length == 0) {
			System.out.println("incorrect usage, input file name is required");
			System.out.println("try again");
			System.exit(0);
		}
			
		// parse command line args
		HashMap<String, String> params = parseCommandLine(args);

		// set the parameters
		// may through illegal argumenrt exception
		// if inputs do not have proper format/type/value
		String fileName = params.getOrDefault("-i", args[0]); // name of the file to be sent to the server
		String serverName = params.getOrDefault("-s", "localhost"); // server name
		int serverPort = Integer.parseInt( params.getOrDefault("-p", "2025") ); // server port number
		int timeoutInterval = Integer.parseInt( params.getOrDefault("-t", "1000") ); // duraiton of tim-out interval 
		Level logLevel = Level.parse( params.getOrDefault("-v", "all").toUpperCase() ); // log levels: all, info, off

		// set log level
		setLogLevel(logLevel);
		
		try {
			// send the file
			StopWaitFtp ftp = new StopWaitFtp(timeoutInterval);
			System.out.printf("sending file \'%s\' to the server...\n", fileName);
			ftp.send(serverName, serverPort, fileName);
			System.out.println("send completed.");
		} catch (FtpException e) {
			System.out.println(e.getMessage());
		}
	}

	
	// parse command line arguments
	private static HashMap<String, String> parseCommandLine(String[] args) {
		HashMap<String, String> params = new HashMap<String, String>();

		int i = 0;
		while ((i + 1) < args.length) {
			params.put(args[i], args[i+1]);
			i += 2;
		}
		
		return params;
	}
	
	// set the global log level and format
	private static void setLogLevel(Level level) {
		System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s %n");
		
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(level);
		logger.addHandler(handler);		
		logger.setLevel(level);
		logger.setUseParentHandlers(false);
	}
	
}

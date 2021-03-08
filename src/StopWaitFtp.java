

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.*;

public class StopWaitFtp {
	
	private static final Logger logger = Logger.getLogger("StopWaitFtp"); // global logger	

	/**
	 * Constructor to initialize the program
	 *
	 * @param timeout The time-out interval for the retransmission timer
	 */
	public StopWaitFtp(int timeout) {



	}


	/**
	 * Send the specified file to the specified remote server
	 *
	 * @param serverName Name of the remote server
	 * @param serverPort Port number of the remote server
	 * @param fileName   Name of the file to be trasferred to the rmeote server
	 * @throws FtpException If anything goes wrong while sending the file
	 */
	public void send(String serverName, int serverPort, String fileName) throws FtpException {



	}

	/*
	 *	1. Send the name of the file as a UTF encoded string
	 *	2. Send the length (in bytes) of the file as a long value
	 *	3. Send the local UDP port number used for file transfer as an int value
	 *	4. Receive the server UDP port number used for file transfer as an int value
	 *  5. Receive the initial sequence number used by the server as an int value
	 */
	public void handshake(String serverName, int serverPort, String fileName, int udpPort){
		try{
			Socket tcpSocket = new Socket(serverName, serverPort);
			DataOutputStream tcpOutput = new DataOutputStream(tcpSocket.getOutputStream());
			DataInputStream tcpInput = new DataInputStream(tcpSocket.getInputStream());
			//Send the UTF encoded file name
			byte[] bytes = fileName.getBytes(StandardCharsets.UTF_8);
			String utf8EncodedString = new String(bytes, StandardCharsets.UTF_8);
			tcpOutput.writeBytes(utf8EncodedString);
			//Send the file length
			File file = new File(fileName);
			long fileLength = file.length();
			tcpOutput.writeLong(fileLength);
			//Send the local UDP port number
			tcpOutput.writeInt(udpPort);
			//Receive the server Port
			int serverUdpPort = tcpInput.readInt();
			//Receive the seq number
			int initSeqNo = tcpInput.readInt();





		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

} // end of class


import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.*;

public class StopWaitFtp {

	public class ResendTimer extends TimerTask {
		private DatagramPacket pkt;
		private DatagramSocket udpSocket;
		private int seq;



		public ResendTimer(DatagramPacket pkt_, DatagramSocket udpSocket_, int seq_){
			pkt = pkt_;
			udpSocket = udpSocket_;
			seq = seq_;
		}

		@Override
		public void run() {
				System.out.println("timeout\t");
				try {
					udpSocket.send(pkt);
					System.out.println("retx\t" + seq);
				} catch (Exception e) {
					e.printStackTrace();
				}

		}

	}

	public long getFileLen() {
		return fileLen;
	}

	public void setFileLen(long fileLen) {
		this.fileLen = fileLen;
	}

	private long fileLen;
	
	private static final Logger logger = Logger.getLogger("StopWaitFtp"); // global logger	

	public int getServerUdpPort() {
		return serverUdpPort;
	}

	public int getInitSeqNo() {
		return initSeqNo;
	}

	public void setServerUdpPort(int serverUdpPort) {
		this.serverUdpPort = serverUdpPort;
	}

	public void setInitSeqNo(int initSeqNo) {
		this.initSeqNo = initSeqNo;
	}

	private int serverUdpPort;
	private int initSeqNo;
	public final static int MAX_PAYLOAD_SIZE = 1400; // bytes

	public int getTimeout() {
		return timeout;
	}

	private int timeout;
	/**
	 * Constructor to initialize the program
	 *
	 * @param timeout The time-out interval for the retransmission timer
	 */
	public StopWaitFtp(int timeout) {
		this.timeout = timeout;

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
		try{
			setServerUdpPort(serverPort);
			DatagramSocket udpSocket = new DatagramSocket();
			handshake(serverName, serverPort, fileName, udpSocket.getLocalPort());
			// create the payload
			byte[] payload = new byte[MAX_PAYLOAD_SIZE];
			byte[] buffer = new byte[MAX_PAYLOAD_SIZE];
			// create a segment with all-zero payload and seqNum 1



			FileInputStream inFile = new FileInputStream(fileName);
			int readBytes = 0;
			int seqNo = getInitSeqNo();
			Timer timer = new Timer();

			//read from file(inName) then send to server until entire file is sent
			while ((readBytes = inFile.read(payload)) != -1) {
				FtpSegment seg = new FtpSegment(seqNo, payload);
				FtpSegment ackSeg = new FtpSegment(seqNo+1, buffer);
				// create a DatagramPacket that can be used to send segment
				DatagramPacket pkt = FtpSegment.makePacket(seg, InetAddress.getByName(serverName), getServerUdpPort());
				DatagramPacket ack = FtpSegment.makePacket(ackSeg, InetAddress.getByName(serverName), getServerUdpPort());

				ResendTimer resendTimer = new ResendTimer(pkt,udpSocket,seqNo);
				udpSocket.send(pkt);
				System.out.println("send\t"+seqNo);
				timer.scheduleAtFixedRate(resendTimer,getTimeout(),getTimeout());
				udpSocket.receive(ack);
				System.out.println("ack \t" + Integer.toString(seqNo+1));
				resendTimer.cancel();
				seqNo += 1;


			}
			timer.cancel();
			timer.purge();


		}
		catch(Exception e){
			e.printStackTrace();
		}

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
			tcpOutput.writeUTF(fileName);
			//Send the file length
			File file = new File(fileName);
			setFileLen(file.length());
			tcpOutput.writeLong(getFileLen());
			//Send the local UDP port number
			tcpOutput.writeInt(udpPort);
			//Receive the server Port
			setServerUdpPort(tcpInput.readInt());
			//Receive the seq number
			setInitSeqNo(tcpInput.readInt());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

} // end of class
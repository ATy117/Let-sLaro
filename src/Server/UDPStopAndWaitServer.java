import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;

class UDPStopAndWaitServer{
	private static final int BUFFER_SIZE = 1024;
	private static final int PORT = 6789;
	private static byte[] dataForSend;
	private static byte[] receiveData;
	private static DatagramSocket serverSocket;
	private TriviaGame mainGame;
	private static boolean beginGame = false;

	public static void main(String[] args) throws IOException {

		// Create a server socket
		serverSocket = new DatagramSocket(PORT);

		// Set up byte arrays for sending/receiving data
		receiveData = new byte[ BUFFER_SIZE ];
		dataForSend = new byte[ BUFFER_SIZE ];

		//Welcome message
		System.out.println("Running server at " + InetAddress.getLocalHost() + ":" + PORT);

		// Infinite loop to check for connections
		while(!beginGame){
			// Get the received packet
			DatagramPacket received = new DatagramPacket(receiveData, receiveData.length );
			serverSocket.receive( received );

			// Get the message from the packet
			int message = ByteBuffer.wrap(received.getData( )).getInt();
			System.out.println("FROM CLIENT: " + message);

			// Get packet's IP and port
			InetAddress IPAddress = received.getAddress();
			int port = received.getPort();

			// Convert message to uppercase
			dataForSend = ByteBuffer.allocate(4).putInt( message ).array();

			try {
				// Send the packet data back to the client
				DatagramPacket packet = new DatagramPacket(dataForSend, dataForSend.length, IPAddress, port);
				serverSocket.send(packet);
			}
			catch (IOException e) {
				System.out.println("Packet with sequence number "+ message + " was dropped");
			}

		}


	}

	public void waitForConnections() {
		while (mainGame.getPlayersList().size() != 4) {
			mainGame.connectPlayer(null);
		}
		beginGame = true;
	}


	public static void sendUDPMessage(String message, String ipAddress, int port) throws IOException {
		InetAddress group = InetAddress.getByName(ipAddress);
		byte[] msg = message.getBytes();
		DatagramPacket packet = new DatagramPacket(msg, msg.length, group, port);
		serverSocket.send(packet);
	}


}
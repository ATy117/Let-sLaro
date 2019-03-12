package Client;

import java.net.*;
import java.nio.*;
import java.util.Scanner;

public class UDPStopAndWaitClient {
	private static final int BUFFER_SIZE = 1024;
	private static int PORT;
	private static String HOSTNAME;
	private static final int BASE_SEQUENCE_NUMBER = 1;

	public static void main(String args[]) throws Exception{

		Scanner sc = new Scanner(System.in);

		System.out.print("Enter server ip: ");
		HOSTNAME = sc.nextLine();

		System.out.print("Enter server port: ");
		PORT = sc.nextInt();

		System.out.print("Enter a username: ");
		String msg = sc.nextLine();
		msg = sc.nextLine();

		// Create a socket
		DatagramSocket socket = new DatagramSocket();
		socket.setSoTimeout(1000);

		// The message we're going to send converted to bytes
		Integer sequenceNumber = BASE_SEQUENCE_NUMBER;

		//retry if packet not received
		for (int counter = 0; counter < 1; counter++) {
			boolean timedOut = true;

			while( timedOut ){
				sequenceNumber++;

				// Create a byte array for sending and receiving data
				byte[] sendData = new byte[ BUFFER_SIZE ];
				byte[] receiveData = new byte[ BUFFER_SIZE ];

				// Get the IP address of the server
				InetAddress IPAddress = InetAddress.getByName( HOSTNAME );

				System.out.println( msg +  " (Sequence Number " + sequenceNumber + ")" );

				// Get byte data for message
				sendData = ByteBuffer.allocate(4).putInt( sequenceNumber ).array();

				try{
					// Send the UDP Packet to the server
					DatagramPacket packet = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
					socket.send( packet );

					// Receive the server's packet
					DatagramPacket received = new DatagramPacket(receiveData, receiveData.length);
					socket.receive( received );

					// Get the message from the server's packet
					int returnMessage = ByteBuffer.wrap( received.getData( ) ).getInt();

					System.out.println( "FROM SERVER:" + returnMessage );

					// If we receive an ack, stop the while loop
					timedOut = false;
				} catch( SocketTimeoutException exception ){
					// If we don't get an ack, prepare to resend sequence number
					System.out.println( "Timeout (Sequence Number " + sequenceNumber + ")" );
					sequenceNumber--;
				}
			}
		}
		socket.close();
	}
}


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class ClientController {

	// Maximum Segment Size - Quantity of data from the application layer in the segment
	public static final int MSS = 4;
	// Window size - Number of packets sent without acking
	public static final int WINDOW_SIZE = 2;
	//Buffer size
	private final static int BUFFER = 1024;
	// Time (ms) before REsending all the non-acked packets
	public static final int TIMER = 30;
	// PORT
	public final static int PORT = 7331;

	private DatagramSocket socket;
	private String hostname;
	private String username;
	private GameState mystate;
	private InetAddress address;

	private boolean waiting=true;
	private boolean error=false;

	public ClientController (String hostname) throws Exception {

		this.hostname = hostname;

		System.out.print("Enter a username: ");
		Scanner sc = new Scanner (System.in);
		username = sc.nextLine();
		username = username.trim();

		if (connectServer(username)) {
			gameLobby();
			gameProper();
			gameEnd();
		}
		else {
			System.out.println("Unable to connect to ip");
		}

	}

	private void gameEnd() {
		printScores(mystate);
	}

	private void gameProper() throws Exception {
		while (!error) {

			mystate = convertToGameState(receivePacket());

			if (mystate.isDone()) {
				break;
			}

			printQuestion(mystate);

			Scanner sc = new Scanner (System.in);
			int answer = sc.nextInt();

			Answer myans = mystate.getCurrentQuestion().getAnswersList().get(answer-1);
			System.out.println("My ans : " + myans.getAnswer());

			PlayerResponse response = formulateResponse(myans);
			byte[] state = Serializer.toBytes(response);
			sendPacket(address, PORT, state);
		}
	}

	private void gameLobby () throws  Exception {
		while (waiting) {

			byte[] receive = new byte[BUFFER];
			DatagramPacket packet = new DatagramPacket(receive, receive.length);
			socket.receive(packet);
			String msg = new String(receive);
			msg = msg.trim();

			if (msg.equals("START")) {
				waiting = false;
			}
			else if (msg.equals("ERROR")) {
				error = true;
				waiting = false;
			}
			System.out.println(msg);
		}
	}

	private void printScores(GameState state) {

		System.out.println("SCORES");
		System.out.println(mystate.getCurrentPlayer().getName() + ": " + mystate.getCurrentPlayer().getScore());

		System.out.println("Other Players\n");

		for (Player p: state.getPlayersList()) {
			System.out.println(p.getName() + ": " + p.getScore());
		}
	}


	private PlayerResponse formulateResponse (Answer sagot) {
		PlayerResponse response = new PlayerResponse(mystate.getCurrentPlayer(), sagot);

		return response;
	}

	private void printQuestion (GameState state) {

		System.out.println("Player: " + state.getCurrentPlayer().getName() + " - " + state.getCurrentPlayer().getScore());
		System.out.println(state.getQuestionNumber() + ": " + state.getCurrentQuestion().getQuestion());

		for (Answer a: state.getCurrentQuestion().getAnswersList()) {
			System.out.println(a.getAnswer());
		}

	}

	private GameState convertToGameState (byte[] gameBytes) throws IOException, ClassNotFoundException {
		GameState gameState = (GameState) Serializer.toObject(gameBytes);

		return gameState;
	}

	private boolean connectServer(String username) throws Exception {

		try {
			byte buf[] = username.getBytes();
			socket = new DatagramSocket();
			address = InetAddress.getByName(hostname);
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
			socket.send(packet);
		}
		catch (UnknownHostException e) {
			return false;
		}

		return true;
	}

	private void sendMessage(String s) throws Exception {
		byte buf[] = s.getBytes();
		InetAddress address = InetAddress.getByName(hostname);
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
		socket.send(packet);
	}

	private byte[] receivePacket () throws Exception{

		byte[] receivedData = new byte[ClientController.MSS + 83];

		int waitingFor = 0;

		ArrayList<RDTPacket> received = new ArrayList<RDTPacket>();

		boolean end = false;

		while(!end){

			System.out.println("Waiting for packet");

			// Receive packet
			DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
			socket.receive(receivedPacket);

			// Unserialize to a RDTPacket object
			RDTPacket packet = (RDTPacket) Serializer.toObject(receivedPacket.getData());

			System.out.println("Packet with sequence number " + packet.getSeq() + " received (last: " + packet.isLast() + " )");

			if(packet.getSeq() == waitingFor && packet.isLast()){

				waitingFor++;
				received.add(packet);

				System.out.println("Last packet received");

				end = true;

			}else if(packet.getSeq() == waitingFor){
				waitingFor++;
				received.add(packet);
				System.out.println("Packed stored in buffer");
			}else{
				System.out.println("Packet discarded (not in order)");
			}

			// Create an RDTAck object
			RDTAck ackObject = new RDTAck(waitingFor);

			// Serialize
			byte[] ackBytes = Serializer.toBytes(ackObject);


			DatagramPacket ackPacket = new DatagramPacket(ackBytes, ackBytes.length, receivedPacket.getAddress(), receivedPacket.getPort());

			// Send with some probability of loss

			socket.send(ackPacket);

		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

		for(RDTPacket p : received){
			for(byte b: p.getData()){
				outputStream.write(b);
			}
		}

		byte[] obj = outputStream.toByteArray();

		return obj;

	}

	public void sendPacket (InetAddress address, int port, byte[] object) throws Exception {

		// Sequence number of the last packet sent (rcvbase)
		int lastSent = 0;

		// Sequence number of the last acked packet
		int waitingForAck = 0;

		System.out.println("Data size: " + object.length + " bytes");

		// Last packet sequence number
		int lastSeq = (int) Math.ceil( (double) object.length / MSS);

		System.out.println("Number of packets to send: " + lastSeq);

		DatagramSocket toReceiver = new DatagramSocket();

		// ServerController address
		InetAddress receiverAddress = InetAddress.getByName("localhost");

		// List of all the packets sent
		ArrayList<RDTPacket> sent = new ArrayList<RDTPacket>();

		while(true){

			// Sending loop
			while(lastSent - waitingForAck < WINDOW_SIZE && lastSent < lastSeq){

				// Array to store part of the bytes to send
				byte[] filePacketBytes = new byte[MSS];

				// Copy segment of data bytes to array
				filePacketBytes = Arrays.copyOfRange(object, lastSent*MSS, lastSent*MSS + MSS);

				// Create RDTPacket object
				RDTPacket rdtPacketObject = new RDTPacket(lastSent, filePacketBytes, (lastSent == lastSeq-1) ? true : false);

				// Serialize the RDTPacket object
				byte[] sendData = Serializer.toBytes(rdtPacketObject);

				// Create the packet
				DatagramPacket packet = new DatagramPacket(sendData, sendData.length, address, port );

				System.out.println("Sending packet with sequence number " + lastSent +  " and size " + sendData.length + " bytes");

				// Add packet to the sent list
				sent.add(rdtPacketObject);

				// Send with some probability of loss
				toReceiver.send(packet);


				// Increase the last sent
				lastSent++;

			} // End of sending while

			// Byte array for the ACK sent by the receiver
			byte[] ackBytes = new byte[40];

			// Creating packet for the ACK
			DatagramPacket ack = new DatagramPacket(ackBytes, ackBytes.length);

			try{
				// If an ACK was not received in the time specified (continues on the catch clausule)
				toReceiver.setSoTimeout(TIMER);

				// Receive the packet
				toReceiver.receive(ack);

				// Unserialize the RDTAck object
				RDTAck ackObject = (RDTAck) Serializer.toObject(ack.getData());

				System.out.println("Received ACK for " + ackObject.getPacket());

				// If this ack is for the last packet, stop the sender (Note: gbn has a cumulative acking)
				if(ackObject.getPacket() == lastSeq){
					break;
				}

				waitingForAck = Math.max(waitingForAck, ackObject.getPacket());

			}catch(SocketTimeoutException e){
				// then send all the sent but non-acked packets

				for(int i = waitingForAck; i < lastSent; i++){

					// Serialize the RDTPacket object
					byte[] sendData = Serializer.toBytes(sent.get(i));

					// Create the packet
					DatagramPacket packet = new DatagramPacket(sendData, sendData.length, address, port );

					// Send with some probability
					toReceiver.send(packet);

				}
			}


		}

		System.out.println("Finished transmission");

	}

}

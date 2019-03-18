import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;


public class ServerController {

	// Maximum Segment Size - Quantity of data from the application layer in the segment
	public static final int MSS = 4;
	// Window size - Number of packets sent without acking
	public static final int WINDOW_SIZE = 2;
	// Time (ms) before REsending all the non-acked packets
	public static final int TIMER = 30;
	// PORT
	public final static int PORT = 7331;
	// Buffer size
	private final static int BUFFER = 1024;
	// MAX players
	private int MAXPLAYER;

	private DatagramSocket socket;
	private ArrayList<InetAddress> clientAddresses;
	private ArrayList<Integer> clientPorts;
	private ArrayList<Player> playerList;
	private HashSet<String> existingClients;
	private TriviaGame game;

	public ServerController(int max) throws Exception {
		MAXPLAYER = max;
		initServer();
	}

	private void initServer () throws Exception {

		socket = new DatagramSocket(PORT);
		clientAddresses = new ArrayList();
		clientPorts = new ArrayList();
		existingClients = new HashSet();
		playerList = new ArrayList<>();

		Scanner sc = new Scanner(System.in);
		System.out.print("Enter number of questions: ");
		int n = sc.nextInt();
		game = new TriviaGame(n);

		initLobby();
		castGameStart();
		castGameProper();
		castGameEnd();
	}

	private void castGameProper() throws Exception {

		while (game.askQuestion()) {
			for (int i = 0; i < playerList.size(); i++) {
				GameState playerstate = game.getGameState(playerList.get(i));
				byte[] state = Serializer.toBytes(playerstate);
				sendPacket(clientAddresses.get(i), clientPorts.get(i), state);
			}

			while (!game.questionDone()) {
				PlayerResponse response = convertToResponse(receivePacket());
				recordScore(response);
			}
		}

	}

	private void initLobby() {
		System.out.println("Waiting for enough players to connect.");
		while (existingClients.size() < MAXPLAYER) {

			byte[] buf = new byte[BUFFER];

			try {
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);

				Player noob = registerPlayer(buf);

				InetAddress clientAddress = packet.getAddress();
				int clientPort = packet.getPort();

				if (noob != null) {
					String id = clientAddress.toString() + "," + clientPort;
					if (!existingClients.contains(id)) {
						existingClients.add(id);
						clientPorts.add(clientPort);
						clientAddresses.add(clientAddress);
						playerList.add(noob);
						game.connectPlayer(noob);
						sendConnectConfirmation(clientAddress, clientPort);
					}
				}
				else {
					sendConnectionError(clientAddress, clientPort);
				}

			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}

	private Player registerPlayer(byte[] buf) {
		String content = new String(buf);
		content = content.trim();

		// verify if new player
		for (Player p: playerList) {
			if (p.getName().equals(content)) {
				return null;
			}
		}

		Player noob = new Player(content);

		return noob;
	}

	private void castGameEnd () throws  Exception{
		game.endGame();
		for (int i = 0; i < playerList.size(); i++) {
			GameState playerstate = game.getGameState(playerList.get(i));
			byte[] state = Serializer.toBytes(playerstate);
			sendPacket(clientAddresses.get(i), clientPorts.get(i), state);
		}
		System.out.println("Game has ended");
	}

	private PlayerResponse convertToResponse (byte[] answer) throws IOException, ClassNotFoundException {
		PlayerResponse response = (PlayerResponse) Serializer.toObject(answer);
		return response;
	}

	private void recordScore(PlayerResponse response) {
		game.checkAnswer(response.getAnswer(), response.getPlayer());
	}

	private void sendConnectConfirmation (InetAddress address, int port) throws Exception {
		String msg = "Connected to Game. Please wait until all players connect";
		byte buf[] = msg.getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
		socket.send(packet);
	}

	private void sendConnectionError (InetAddress address, int port) throws Exception {
		String msg = "ERROR";
		byte buf[] = msg.getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
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

	private void castGameStart() throws Exception {

		System.out.println("Game will begin\n\n");

		game.startGame();

		for (int i = 0; i<existingClients.size(); i++) {
			String start = "START";
			byte buf[] = start.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, clientAddresses.get(i), clientPorts.get(i));
			socket.send(packet);
		}

	}

	private void sendPacket (InetAddress address, int port, byte[] object) throws Exception {

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

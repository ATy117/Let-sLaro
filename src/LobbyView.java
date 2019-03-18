import javafx.stage.Stage;

import java.util.Scanner;

public class LobbyView extends View {

	public LobbyView(ClientController controller, Stage primaryStage) throws Exception {
		super(controller);
		this.primaryStage = primaryStage;
		System.out.println("You are in lobby view");
		joinGame();
	}

	public void joinGame() throws Exception {

		System.out.print("Enter server ip: ");
		Scanner sc = new Scanner (System.in);
		String hostname = sc.nextLine();

		System.out.print("Enter a username: ");
		sc = new Scanner (System.in);
		String username = sc.nextLine();
		username = username.trim();

		controller.submitUsername(hostname ,username);
	}


	@Override
	public void Update() {

	}
}

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.util.Scanner;

public class LobbyView extends View {

	public LobbyView(ClientController controller, Stage primaryStage) throws Exception {
		super(controller);
		this.primaryStage = primaryStage;

		System.out.println("You are in lobby view");

		FXMLLoader loader = new FXMLLoader(getClass().getResource("lobbyTemplate.fxml"));
		loader.setController(this);

		sm = new StageManager(primaryStage);
		sm.loadScene(loader);
		sm.setWindowName("Lobby");
	}


	@Override
	public void Update() {

	}

	public void joinGame(ActionEvent actionEvent) throws Exception {
		System.out.print("Enter server ip: ");
		Scanner sc = new Scanner (System.in);
		String hostname = sc.nextLine();

		System.out.print("Enter a username: ");
		sc = new Scanner (System.in);
		String username = sc.nextLine();
		username = username.trim();

		controller.submitUsername(hostname ,username);
	}
}

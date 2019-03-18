import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Scanner;

public class ClientApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception{
		System.out.print("Enter server ip: ");
		Scanner sc = new Scanner (System.in);
		String hostname = sc.nextLine();
		ClientController controller = new ClientController(hostname);
	}

	public static void main(String[] args) {
		launch(args);
	}
}

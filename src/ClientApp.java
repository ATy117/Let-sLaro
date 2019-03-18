import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Scanner;

public class ClientApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception{
		ClientController controller = new ClientController(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}

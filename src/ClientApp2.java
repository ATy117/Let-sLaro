import javafx.application.Application;
import javafx.stage.Stage;

public class ClientApp2 extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception{
		ClientController controller = new ClientController(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}

import javafx.application.Application;
import javafx.stage.Stage;

public class ServerApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception{
		ServerController controller = new ServerController(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
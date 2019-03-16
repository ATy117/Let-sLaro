package ServerPack;

import javafx.application.Application;
import javafx.stage.Stage;

public class ServerApp extends Application  {

	@Override
	public void start(Stage primaryStage) {
		ServerController s=new ServerController();
		while(true){
			s.run();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}

import javafx.stage.Stage;

public abstract class View {

	public abstract void Update();
	public Stage primaryStage;
	public GameState state;
	public ClientController controller;

	public View () {

	}

	public View(ClientController controller) {
		this.controller = controller;
	}

}

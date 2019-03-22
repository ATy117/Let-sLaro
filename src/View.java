import Model.GameState;
import com.jfoenix.controls.JFXPopup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public abstract class View {

	public abstract void Update() ;
	public Stage primaryStage;
	public GameState state;
	public ClientController controller;
	public StageManager sm;
	public JFXPopup popup;
	public AnchorPane anchorPane;


	public View () {

	}

	public View(ClientController controller) {
		this.controller = controller;
	}


	public abstract void popUp();
}

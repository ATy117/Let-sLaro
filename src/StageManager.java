import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StageManager {

	Stage stage;

	public StageManager(Stage stage){
		this.stage = stage;
	}

	public boolean loadScene(FXMLLoader loader) {

		Parent root = null;

		try {
			root = (Parent) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();

		return true;
	}

	public void setWindowName(String title) {
		stage.setTitle(title);
	}
}

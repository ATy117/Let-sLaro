import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.util.Scanner;

public class FinishView extends View {

	public FinishView(ClientController controller, Stage primaryStage){
		super(controller);
		this.primaryStage = primaryStage;
		System.out.println("You are in finish view");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("finishTemplate.fxml"));
		loader.setController(this);

		Update();
		Platform.exit();
	}


	private void printScores(GameState state) {

		System.out.println("SCORES");
		System.out.println(state.getCurrentPlayer().getName() + ": " + state.getCurrentPlayer().getScore());

		System.out.println("Other Players\n");

		for (Player p: state.getPlayersList()) {
			System.out.println(p.getName() + ": " + p.getScore());
		}
	}


	@Override
	public void Update() {
		this.state = controller.getMystate();
		printScores(this.state);
	}
}

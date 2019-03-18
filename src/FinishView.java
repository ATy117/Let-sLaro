import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Scanner;

public class FinishView extends View {

	@FXML Label remarksLabel;

	public FinishView(ClientController controller, Stage primaryStage){
		super(controller);
		this.primaryStage = primaryStage;
		System.out.println("You are in finish view");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("finishTemplate.fxml"));
		loader.setController(this);

		init();
		sm = new StageManager(primaryStage);
		sm.loadScene(loader);
		sm.setWindowName("Game Finished");

		Update();
	}


	private void printScores(GameState state) {

		System.out.println("SCORES");
		System.out.println(state.getCurrentPlayer().getName() + ": " + state.getCurrentPlayer().getScore());

		System.out.println("Other Players\n");

		for (Player p: state.getPlayersList()) {
			System.out.println(p.getName() + ": " + p.getScore());
		}
	}

	public void init(){
		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(7.0);
		dropShadow.setColor(Color.color(0, 0, 0.10));
		remarksLabel.setEffect(dropShadow);
	}

	@Override
	public void Update() {
		this.state = controller.getMystate();
		printScores(this.state);
	}
}

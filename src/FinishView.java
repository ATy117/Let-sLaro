import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class FinishView extends View {

	@FXML Label remarksLabel;
	@FXML Label username1, username2, username3, score1, score2, score3;

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
	}


	private void printScores(GameState state) {

		System.out.println("SCORES");
		System.out.println(state.getCurrentPlayer().getName() + ": " + state.getCurrentPlayer().getScore());

		System.out.println("Other Players\n");

		Collections.sort(state.getPlayersList(), new Comparator<Player>() {
			@Override
			public int compare(Player o1, Player o2) {
				return o1.getScore() - o2.getScore();
			}
		});

		List<Player> top3 = new ArrayList<>();

		int n = 3;
		if (state.getPlayersList().size() < 3)
			n = state.getPlayersList().size();
		
		for (int i = 0; i < n; i++){
			top3.add(state.getPlayersList().get(i));
		}

		for (Player p: top3) {
			System.out.println(p.getName() + ": " + p.getScore());
		}
	}

	public void init(){
		/*
		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(7.0);
		dropShadow.setColor(Color.color(0, 0, 0.10));
		remarksLabel.setEffect(dropShadow);
		*/

	}

	@Override
	public void Update() {
		this.state = controller.getMystate();
		printScores(this.state);
	}
}

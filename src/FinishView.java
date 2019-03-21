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

		sm = new StageManager(primaryStage);
		sm.loadScene(loader);
		sm.setWindowName("Game Finished");
		init();
	}


	private void printScores(GameState state) {
		
		Collections.sort(state.getPlayersList(), new Comparator<Player>() {
			@Override
			public int compare(Player o1, Player o2) {
				return o2.getScore() - o1.getScore();
			}
		});

		List<Player> top3 = new ArrayList<>();

		int n = 3;
		if (state.getPlayersList().size() < 3)
			n = state.getPlayersList().size();
		
		for (int i = 0; i < n; i++){
			top3.add(state.getPlayersList().get(i));

			if (state.getPlayersList().get(i).getName().equals(state.getCurrentPlayer().getName())) {
				remarksLabel.setText("WINNER");
			}
		}


		if (top3.size() > 0) {
			username1.setText(top3.get(0).getName());
			score1.setText(top3.get(0).getScore()+"");
			if (top3.get(0).getName().equals(state.getCurrentPlayer().getName())) {
				username1.getStylesheets().add("theme.css");
				score1.getStylesheets().add("theme.css");
				username1.getStyleClass().add("selected-finish-list");
				score1.getStyleClass().add("selected-finish-list");
			}
		}

		if (top3.size() > 1) {
			username2.setText(top3.get(1).getName());
			score2.setText(top3.get(1).getScore()+"");
			if (top3.get(1).getName().equals(state.getCurrentPlayer().getName())) {
				username2.getStylesheets().add("theme.css");
				score2.getStylesheets().add("theme.css");
				username2.getStyleClass().add("selected-finish-list");
				score2.getStyleClass().add("selected-finish-list");
			}
		}

		if (top3.size() > 2) {
			username3.setText(top3.get(2).getName());
			score3.setText(top3.get(2).getScore()+"");
			if (top3.get(2).getName().equals(state.getCurrentPlayer().getName())) {
				username3.getStylesheets().add("theme.css");
				score3.getStylesheets().add("theme.css");
				username3.getStyleClass().add("selected-finish-list");
				score3.getStyleClass().add("selected-finish-list");
			}
		}



	}

	public void init(){

		/*
		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(7.0);
		dropShadow.setColor(Color.color(0, 0, 0.10));
		remarksLabel.setEffect(dropShadow);
		*/

		remarksLabel.setText("LOSER");
		username1.setText("");
		username2.setText("");
		username3.setText("");
		score1.setText("");
		score2.setText("");
		score3.setText("");
	}

	@Override
	public void Update() {
		this.state = controller.getMystate();
		printScores(this.state);
	}

	@Override
	public void popUp() {}
}

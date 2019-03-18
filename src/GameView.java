import javafx.stage.Stage;

import java.util.Scanner;

public class GameView extends View{

	public GameView(ClientController controller, Stage primaryStage) throws Exception{
		super(controller);
		this.primaryStage = primaryStage;
		System.out.println("You are in game view");

	}

	@Override
	public void Update() {
		this.state = controller.getMystate();
		printQuestion(this.state);
	}

	private void printQuestion (GameState state) {

		System.out.println("Player: " + state.getCurrentPlayer().getName() + " - " + state.getCurrentPlayer().getScore());
		System.out.println(state.getQuestionNumber() + ": " + state.getCurrentQuestion().getQuestion());

		for (Answer a: state.getCurrentQuestion().getAnswersList()) {
			System.out.println(a.getAnswer());
		}

	}
}

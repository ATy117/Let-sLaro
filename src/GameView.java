import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.util.Scanner;

public class GameView extends View{

	public GameView(ClientController controller, Stage primaryStage) throws Exception{
		super(controller);
		this.primaryStage = primaryStage;
		System.out.println("You are in game view");

		FXMLLoader loader = new FXMLLoader(getClass().getResource("gameTemplate.fxml"));
		loader.setController(this);

		sm = new StageManager(primaryStage);
		sm.loadScene(loader);
		sm.setWindowName("Trivia");
	}

	@Override
	public void Update()  {
		this.state = controller.getMystate();
		printQuestion(this.state);


		try {
			selectAnswer(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectAnswer(int n) throws Exception {
		Scanner sc = new Scanner (System.in);
		int answer = sc.nextInt();
		controller.selectAnswer(answer);
	}

	private void printQuestion (GameState state) {

		System.out.println("Player: " + state.getCurrentPlayer().getName() + " - " + state.getCurrentPlayer().getScore());
		System.out.println(state.getQuestionNumber() + ": " + state.getCurrentQuestion().getQuestion());

		for (Answer a: state.getCurrentQuestion().getAnswersList()) {
			System.out.println(a.getAnswer());
		}

	}
}

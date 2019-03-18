import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {

	// Serial version ID – Just a random number
	private static final long serialVersionUID = 5230549922091722630L;

	private Question currentQuestion;
	private int questionNumber;
	private List<Player> playersList;
	private Player currentPlayer;
	private boolean isQuitting;
	private boolean isDone;

	public GameState (){
		isQuitting = false;
	}

	public Question getCurrentQuestion() {
		return currentQuestion;
	}

	public List<Player> getPlayersList() {
		return playersList;
	}

	public int getQuestionNumber() {
		return questionNumber;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public boolean isQuitting() {
		return isQuitting;
	}

	public void setCurrentQuestion(Question currentQuestion) {
		this.currentQuestion = currentQuestion;
	}

	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}

	public void setPlayersList(List<Player> playersList) {
		this.playersList = playersList;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public void setQuitting(boolean quitting) {
		isQuitting = quitting;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean done) {
		isDone = done;
	}





}

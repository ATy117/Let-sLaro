import java.io.Serializable;

public class Player implements Serializable {
	// Serial version ID – Just a random number
	private static final long serialVersionUID = 9182731932719191L;

	String name;
	int score;
	int correctAnswers;
	boolean answered;

	public Player(String name) {
		this.name = name;
		score = 0;
		correctAnswers = 0;
		answered = false;
	}
	public boolean isAnswered() {
		return answered;
	}

	public void setAnswered(boolean answered) {
		this.answered = answered;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getCorrectAnswers() {
		return correctAnswers;
	}

	public void setCorrectAnswers(int correctAnswers) {
		this.correctAnswers = correctAnswers;
	}
}

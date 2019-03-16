package GameModel;

public class Player {

	String name;
	int score;
	int correctAnswers;

	public Player(String name) {
		this.name = name;
		score = 0;
		correctAnswers = 0;
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

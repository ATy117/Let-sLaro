import java.io.Serializable;

public class Answer implements Serializable {
	// Serial version ID – Just a random number
	private static final long serialVersionUID = 12312379879798L;

	private String answer;
	private boolean isCorrect;

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean correct) {
		isCorrect = correct;
	}





}

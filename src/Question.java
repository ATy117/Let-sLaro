import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {

	// Serial version ID – Just a random number
	private static final long serialVersionUID = 981379128371923L;

	String question;
	List<Answer> answersList;
	int points;

	public Question() {
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}


	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public List<Answer> getAnswersList() {
		return answersList;
	}

	public void setAnswersList(List<Answer> answersList) {
		this.answersList = answersList;
	}




}

package Database;

import Model.Answer;

import java.util.List;

public interface AnswersDAO {

	public List<Answer> getAnswers(int question_id);
}

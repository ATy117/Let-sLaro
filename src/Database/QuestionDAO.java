package Database;

import Model.Question;

public interface QuestionDAO {

	public Question getQuestion(int n);
	public int getNQuestions();
}

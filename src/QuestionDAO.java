import java.sql.SQLException;

public interface QuestionDAO {

	public Question getQuestion(int n);
	public int getNQuestions();
}

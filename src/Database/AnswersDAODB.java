package Database;

import Model.Answer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnswersDAODB implements AnswersDAO{

	Connection connection;
	private final String COL_QUESTIONID = "answers.question_id";
	private final String COL_ANSWER = "answers.answer";
	private final String COL_ISCORRECT = "answers.isCorrect";
	private final String TABLE = "answers";

	public AnswersDAODB(Connection connection) {

		this.connection = connection;
	}


	@Override
	public List<Answer> getAnswers(int question_id) {
		List<Answer> answerList =  new ArrayList<>();

		String query = "SELECT * FROM "+ this.TABLE + " WHERE question_id = "+ question_id;

		try {
			PreparedStatement statement = this.connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				answerList.add(toAnswer(rs));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		Collections.shuffle(answerList);
		return answerList;
	}

	private Answer toAnswer(ResultSet rs) throws SQLException{
		Answer answer= new Answer();

		answer.setAnswer(rs.getString(this.COL_ANSWER));
		answer.setCorrect(rs.getBoolean(this.COL_ISCORRECT));

		return answer;
	}
}

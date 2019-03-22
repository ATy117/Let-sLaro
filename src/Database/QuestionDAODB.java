package Database;

import Model.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionDAODB implements  QuestionDAO {

	Connection connection;
	private final String COL_QUESTIONID = "questions.question_id";
	private final String COL_QUESTION = "questions.question";
	private final String COL_POINTS = "questions.points";
	private final String TABLE = "questions";

	public QuestionDAODB (Connection connection) {
		this.connection = connection;
	}

	@Override
	public Question getQuestion(int n) {
		Question q = new Question();

		String query = "SELECT * FROM " + this.TABLE + " WHERE " + this.COL_QUESTIONID + " = " + n;

		try {
			PreparedStatement statement = this.connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				q = toQuestion(rs);
			}
			else
				return null;
		}
		catch (SQLException e){
			e.printStackTrace();
			return null;
		}


		return q;
	}

	public int getNQuestions () {
		int n=0;

		String query = "SELECT COUNT(*) FROM " + this.TABLE;

		try {
			PreparedStatement statement = this.connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				n = rs.getInt("COUNT(*)");
			}
			else
				return 0;
		}
		catch (SQLException e){
			e.printStackTrace();
			return 0;
		}

		return n;

	}

	private Question toQuestion(ResultSet rs) throws SQLException {
		Question q = new Question();

		q.setQuestion(rs.getString(this.COL_QUESTION));
		q.setPoints(rs.getInt(this.COL_POINTS));

		return q;
	}


}

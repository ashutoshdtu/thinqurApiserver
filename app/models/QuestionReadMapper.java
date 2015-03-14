package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import controllers.Question;

public class QuestionReadMapper implements ResultSetMapper<Question> {

	@Override
	public Question map(int index, ResultSet r, StatementContext ctx)
			throws SQLException {

		Question que = new Question();
		Question.setQuestionID(r.getString("id"));
		Question.setQuestion(r.getString("question"));
		Question.setDescription(r.getString("description"));
		return que;
	}

}
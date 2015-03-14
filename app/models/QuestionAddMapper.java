package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import controllers.Question;

public class QuestionAddMapper implements ResultSetMapper<Question> {

	@Override
	public Question map(int index, ResultSet r, StatementContext ctx)
			throws SQLException {

		Question que = new Question();
		// que.date=r.getDate("date");
		// que.status=r.getString("status");
		return que;
	}

}
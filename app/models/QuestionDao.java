package models;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import controllers.Question;

//@RegisterMapper(QuestionAddMapper.class)
public interface QuestionDao extends Transactional<QuestionDao> {
	@SqlUpdate("insert into question(id, fk_user, question, description, is_anonymous) "
			+ "VALUES(unhex(:uuid), unhex('100E4400E21211D4A716346645440000'), :question, "
			+ ":description, :is_anonymous)")
	Question addQuestion(@Bind("uuid") String uuid, @Bind("question") String question,
			@Bind("description") String description, @Bind("is_anonymous") boolean is_anonymous);
}

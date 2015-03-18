package models;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import controllers.Question;

@RegisterMapper(QuestionReadMapper.class)
public interface QuestionDao extends Transactional<QuestionDao> {
	@SqlUpdate("insert into question(id, fk_user, question, description, is_anonymous, fk_question_type) "
			+ "VALUES(unhex(:uuid), unhex('100E4400E21211D4A716346645440000'), :question, "
			+ ":description, :is_anonymous, '1')")
	int addQuestion(@Bind("uuid") String uuid, @Bind("question") String question,
			@Bind("description") String description, @Bind("is_anonymous") boolean is_anonymous);
	
	@SqlUpdate("insert into question_tags(id, fk_question, fk_tag) "
			+ "VALUES(unhex(:uuid), unhex(:questionID), unhex(:tagID))")
	int addTags(@Bind("uuid") String generateUUID, @Bind("questionID") String questionID,
			@Bind("tagID") String tag);
	
	@SqlUpdate("insert into answer(id, fk_question, answer) "
			+ "VALUES(unhex(:uuid), unhex(:questionID), :answer)")
	int addAnswers(@Bind("uuid") String generateUUID, @Bind("questionID") String questionID,
			@Bind("answer") String option);
	
	@SqlQuery("select id, question, description, created_at, updated_at from question where id = :uuid")
	List<Question> readQuestion(@Bind("uuid") String uuid);
}

package models;

import org.skife.jdbi.v2.Handle;

import controllers.Question;

public class QuestionReadEntityManager {
	public static void ReadQuestion(String questionID) throws ClassNotFoundException {
		Handle h = BaseEntityManager.getDBIh();
		QuestionDao dao;
		dao = h.attach(QuestionDao.class);
		h.begin();
		dao.readQuestion(questionID);
		h.commit();
		h.close();
	}
}

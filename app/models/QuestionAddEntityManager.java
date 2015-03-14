package models;

import org.skife.jdbi.v2.Handle;

import controllers.Question;

public class QuestionAddEntityManager {
	public static void addQuestion() throws ClassNotFoundException {
		Handle h = BaseEntityManager.getDBIh();
		QuestionDao dao;
		dao = h.attach(QuestionDao.class);
		h.begin();
		dao.addQuestion(Question.getQuestionID(), Question.getQuestion(),
				Question.getDescription(), Question.isAnonymous());
		h.commit();
		h.close();
	}
}

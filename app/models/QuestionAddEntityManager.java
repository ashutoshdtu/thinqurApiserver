package models;

import org.skife.jdbi.v2.Handle;

import controllers.Question;
import controllers.Utilities;

public class QuestionAddEntityManager {
	public static void addQuestion() throws ClassNotFoundException {
		Handle h = BaseEntityManager.getDBIh();
		QuestionDao dao;
		dao = h.attach(QuestionDao.class);
		h.begin();
		dao.addQuestion(Question.getQuestionID(), Question.getQuestion(),
				Question.getDescription(), Question.isAnonymous());
		for(String tag: Question.getTags()){
			dao.addTags(Utilities.generateUUID(), Question.getQuestionID(), tag);
		}
		for(String option: Question.getOptions()) {
			dao.addAnswers(Utilities.generateUUID(), Question.getQuestionID(), option);
		}
		h.commit();
		h.close();
	}
}

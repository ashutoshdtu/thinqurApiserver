package models;

import org.skife.jdbi.v2.Handle;

import controllers.Question;
import controllers.Utilities;

public class QuestionAddEntityManager {
	@SuppressWarnings("unused")
	public static void addQuestion() {
		Handle h = null;
		try {
			h = BaseEntityManager.getDBIh();

			QuestionDao dao;
			dao = h.attach(QuestionDao.class);
			h.begin();
			dao.addQuestion(Question.getQuestionID(), Question.getQuestion(),
					Question.getDescription(), Question.isAnonymous());
			for (String tag : Question.getTags()) {
				dao.addTags(Utilities.generateUUID(), Question.getQuestionID(),
						tag);
			}
			for (String option : Question.getOptions()) {
				dao.addAnswers(Utilities.generateUUID(),
						Question.getQuestionID(), option);
			}
			h.commit();
			h.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (h != null) {
				h.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (h != null) {
				h.close();
			}
		}
		
	}
}

package controllers;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import models.QuestionAddEntityManager;

import org.json.JSONException;
import org.json.JSONObject;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import static play.data.Form.*;



public class Question extends Controller {
	
	static String questionID; 		// question unique id
	static String userID; 			// user id of the user who created this question (-1 for admin)
	static String question; 		// Question
	static String description;		// Question description
	static String questionType; 	// question type 
	static boolean isAnonymous; 	// whether to show question as anonymous
	static String[] tags; 			// question tags
	static String[] options; 		// options of question
	static String createdAtTimeMilli; // time of creation in milli secs (epoch time)
	static String lastUpdatedAtTimeMilli; // time of last update in milli secs (epoch time)
	List<Question> history;
	
	static UUID uid;
	
	public Question() {
		super();
		Question.questionID = null;
		Question.userID = null;
		Question.question = null;
		Question.description = null;
		Question.questionType = null;
		Question.isAnonymous = false;
		Question.tags = null;
		Question.options = null;
		Question.createdAtTimeMilli = null;
		Question.lastUpdatedAtTimeMilli = null;
		this.history = null;
		uid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
	}

	public static class QuestionForm {
		public String userid;
		public String question;
		public String description;
		public String questiontype;
		public boolean isAnonymous;
		public String tags;
		public String options;
        public String internal;
	}
	
	@SuppressWarnings("static-access")
	public static Result addQuestion() {
		int success = 0;
		String statusMessage;
		try {
			Form<QuestionForm> form = form(QuestionForm.class).bindFromRequest();
			if (form.hasErrors()) {
				success = 0;
				statusMessage = "Error in form!!! Cannot add question";
				//return notFound();
			} else {
				QuestionForm newQuestion = form.get();
				questionID = generateQuestionID();
				if(mapQuestionFormToQuestion(newQuestion)) {
					Logger.info("Adding Question <questionID: "+ questionID + "> to DB...");
					QuestionAddEntityManager que= new QuestionAddEntityManager(); 
					que.addQuestion();
					success = 1;
					statusMessage = "Question added successfully";					
				} else {
					Logger.warn("Invalid question!!! Cannot add to DB");
					success = 0;
					statusMessage = "Invalid question!!! Cannot add to DB";
				}
			}
		} catch (Exception e) {
			success = 0;
			statusMessage = e.toString() ;
		}
		JSONObject debugInfo = new JSONObject();
		JSONObject result = new JSONObject();
		try {
			if(success==1) {
				result.put("status",  "ok");
				debugInfo.put("code", 1);
				debugInfo.put("message", statusMessage);
				debugInfo.put("questionID", questionID);
			}
			else {
				result.put("status",  "failed");
				debugInfo.put("code", 0);
				debugInfo.put("message", statusMessage);
			}
			result.put("debugInfo", debugInfo);
		} catch (JSONException e) {
			Logger.warn("JSON exception!!!");
			e.printStackTrace();
			if(success==1) 
				return ok("{\"status\":\"ok\",\"debugInfo\":{\"message\":\"Question added successfully. JSON Exception occured\"}}");
			else 
				return ok("{\"status\":\"failed\",\"debugInfo\":{\"message\":\"Question not added. JSON Exception occured\"}}");
		}
		return ok(result.toString());
	}

	private static boolean mapQuestionFormToQuestion(QuestionForm que) {
		try {
			userID = que.userid != null ? que.userid : "-1";
			question = que.question;
			description = que.description;
			options = que.options != null ? que.options.split(",") : null;
			tags = que.tags != null ? que.tags.split(",") : null;
			isAnonymous = que.isAnonymous;
			Long millis = System.currentTimeMillis();
			createdAtTimeMilli = millis.toString();
			lastUpdatedAtTimeMilli = millis.toString();
			if(isValidQuestion()) {
				return true;
			}
		} catch(Exception e) {
			Logger.error(e.toString());
		}
		return false;
	}

	private static boolean isValidQuestion() throws Exception{
		boolean valid = true;
		if(questionID == null || questionID.isEmpty() || questionID.length()<32  
				|| question == null || question.isEmpty() 
				|| options == null || options.length < 2) {
			valid = false;
		}
		return valid;
	}

	private static String generateQuestionID() {
		String qid;
		try{
			qid = UUID.randomUUID().toString().replaceAll("-", "");;
		} catch(Exception e) {
			Logger.error(e.toString());
			return null;
		}
		return qid;
	}
	
	public static String getQuestionID() {
		return questionID;
	}

	public static void setQuestionID(String questionID) {
		Question.questionID = questionID;
	}	

	public static String getUserID() {
		return userID;
	}

	public static void setUserID(String userID) {
		Question.userID = userID;
	}

	public static String getQuestion() {
		return question;
	}

	public static void setQuestion(String question) {
		Question.question = question;
	}

	public static String getDescription() {
		return description;
	}

	public static void setDescription(String description) {
		Question.description = description;
	}

	public static boolean isAnonymous() {
		return isAnonymous;
	}

	public static void setAnonymous(boolean isAnonymous) {
		Question.isAnonymous = isAnonymous;
	}

}

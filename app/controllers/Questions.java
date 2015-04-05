/**
 * 
 */
package controllers;

import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.WriteConcern;

import models.HTTPResponse;
import models.HTTPStatus;
import models.HTTPStatusCode;
import models.Question;
import play.Logger;
import play.data.Form;
import play.libs.Json;
//import play.modules.spring.Spring;
import play.mvc.Controller;
import play.mvc.Result;
//import scala.annotation.meta.getter;
import services.QuestionDAO;
import utils.DAOUtils;

//import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ashutosh
 *
 */
public class Questions extends Controller {
	public static QuestionDAO questionDAO = null;
	
	public static boolean createQuestionsDAO() {
		if(DAOUtils.questionMongo!=null) {
			try {
				questionDAO = new QuestionDAO(DAOUtils.questionMongo.datastore);
				questionDAO.ensureIndexes();
			} catch (Exception e) {
				questionDAO = null;
				e.printStackTrace();
			}
		}
		if(questionDAO == null) {
			Logger.info("Could not initialize Question DAO" );
			return false;
		}
		return true;
	}
	
	public static Result getQuestions() {
		HTTPStatus status = new HTTPStatus();
		Question question = null;
		String metadata = null;
		String debugInfo = null;
		/* 
		 * 1) Create a form class in models
		 * 2) Read form and check if it has errors. Also check for default values (i'm sending you an example of how to do this)
		 * 3) If method == "generic"
		 * 4) 	Leave this part for me. There are some issues with this thing and i have a solution for it. I'll do it in a day. 
		 * 5) else
		 * 6) 	do this instead (the normal query that we discussed)
		 * 7) set status and other response values and return response
		 */
		return ok();
	}
	
	public static Result postQuestion() {
		HTTPStatus status = new HTTPStatus();
		Question question = null;
		String metadata = null;
		String debugInfo = null;
		
		Form<Question> form = Form.form(Question.class);
		if(form.hasErrors()) {
			status.setCode(HTTPStatusCode.BAD_REQUEST);
			status.setDeveloperMessage("Error in submitted query!! Check models.Question.java file");
		} else if(questionDAO==null) {
			// if not connected to Questions DB
			status.setCode(HTTPStatusCode.GONE);
			status.setDeveloperMessage("Not connected to Questions DB");
		} else {
			// Aal eez well. 
			question = form.bindFromRequest().get();
			try {
				questionDAO.save(question, WriteConcern.ACKNOWLEDGED);
				question = questionDAO.get(question.id);
				if(question == null) {
					status.setCode(HTTPStatusCode.GONE);
					status.setDeveloperMessage("Question was written to DB but was not returned successfully");
				} else {
					status.setCode(HTTPStatusCode.RESOURCE_CREATED);
					status.setDeveloperMessage("Question was successfully written to DB");
				}
			} catch (Exception e) {
				question = null;
				status.setCode(HTTPStatusCode.INTERNAL_SERVER_ERROR);
				status.setDeveloperMessage("Exception occured while writing to Question DB");
				e.printStackTrace();
			}
			
		}
		HTTPResponse<Question, String, String> httpResponse = new HTTPResponse<Question, String, String>(status, question, metadata, debugInfo);
		return status(status.code, Json.toJson(httpResponse));
	}
	
	public static Result getQuestionById(String id) {
		HTTPStatus status = new HTTPStatus();
		Question question = null;
		String metadata = null;
		String debugInfo = null;
		
		//check if id is invalid
		if(isInvalidQuestionId(id)) {
			status.setCode(HTTPStatusCode.BAD_REQUEST);
			status.setDeveloperMessage("Question id invalid. Make sure id is not empty or null. Also check if its a valid UUID");
		} else if(questionDAO==null) {
			status.setCode(HTTPStatusCode.GONE);
			status.setDeveloperMessage("Not connected to Questions DB");
		} else {
			try {
				//ObjectId questionId = new ObjectId(id);
				question = questionDAO.get(new ObjectId(id));
			} catch (Exception e) {
				status.setCode(HTTPStatusCode.NOT_FOUND);
				status.setDeveloperMessage("Question not found. \n"
						+ "Either id is invalid or question doesnot exist in database. \n"
						+ "Also check that api is pointed to correct database. \n"
						+ "If all seems ok, notify the fucking developers.");
				e.printStackTrace();
			}
		}

		HTTPResponse<Question, String, String> httpResponse = new HTTPResponse<Question, String, String>(status, question, metadata, debugInfo);
		return status(status.code, Json.toJson(httpResponse));
	}

	/**
	 * @param id
	 * @return
	 */
	private static boolean isInvalidQuestionId(String id) {
		return id.isEmpty() || id==null;
	}
	
	
}

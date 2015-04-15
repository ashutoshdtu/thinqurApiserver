/**
 * 
 */
package controllers;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import com.fasterxml.classmate.util.ResolvedTypeCache.Key;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.util.JSON;

import models.QuestionGetForm;
import models.HTTPResponse;
import models.HTTPStatus;
import models.HTTPStatusCode;
import models.Question;
import play.Logger;
import play.Play;
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
	
	static QuestionDAO questionDAO = DAOUtils.questionDAO;
	
	public static Result postQuestion() {
		HTTPStatus httpStatus = new HTTPStatus();
		Question question = null;
		String metadata = null;
		String debugInfo = null;
		
		try {
			Form<Question> form = Form.form(Question.class);
			if(form.hasErrors()) {
				httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
				httpStatus.setDeveloperMessage("Error in submitted query!! Check models.Question.java file");
			} else if(questionDAO==null) {
				// if not connected to Questions DB
				httpStatus.setCode(HTTPStatusCode.GONE);
				httpStatus.setDeveloperMessage("Not connected to Questions DB");
			} else {
				question = form.bindFromRequest().get();
				try {
					questionDAO.save(question, WriteConcern.SAFE);
					question = questionDAO.get(question.id);
					if(question == null) {
						httpStatus.setCode(HTTPStatusCode.GONE);
						httpStatus.setDeveloperMessage("Question was written to DB but was not returned successfully");
					} else {
						httpStatus.setCode(HTTPStatusCode.RESOURCE_CREATED);
						httpStatus.setDeveloperMessage("Question was successfully written to DB");
					}
				} catch (Exception e) {
					question = null;
					httpStatus.setCode(HTTPStatusCode.INTERNAL_SERVER_ERROR);
					httpStatus.setDeveloperMessage("Exception occured while writing to Question DB");
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			httpStatus.setCode(HTTPStatusCode.INTERNAL_SERVER_ERROR);
			httpStatus.setDeveloperMessage("Unknown exception. See DebugInfo for more info");
			debugInfo = e.getMessage();
			e.printStackTrace();
		}
		HTTPResponse<Question, String, String> httpResponse = new HTTPResponse<Question, String, String>(httpStatus, question, metadata, debugInfo);
		return status(httpStatus.code, Json.toJson(httpResponse)); 
	}
	
	public static Result getQuestionById(String id) {
		HTTPStatus httpStatus = new HTTPStatus();
		Question question = null;
		String metadata = null;
		String debugInfo = null;
		
		//check if id is invalid
		if(isInvalidQuestionId(id)) {
			httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
			httpStatus.setDeveloperMessage("Question id invalid. Make sure id is not empty or null. Also check if its a valid UUID");
		} else if(questionDAO==null) {
			httpStatus.setCode(HTTPStatusCode.GONE);
			httpStatus.setDeveloperMessage("Not connected to Questions DB");
		} else {
			try {
				//ObjectId questionId = new ObjectId(id);
				question = questionDAO.get(id);
			} catch (Exception e) {
				httpStatus.setCode(HTTPStatusCode.NOT_FOUND);
				httpStatus.setDeveloperMessage("Question not found. \n"
						+ "Either id is invalid or question doesnot exist in database. \n"
						+ "Also check that api is pointed to correct database. \n"
						+ "If all seems ok, notify the fucking developers.");
				e.printStackTrace();
			}
		}

		HTTPResponse<Question, String, String> httpResponse = new HTTPResponse<Question, String, String>(httpStatus, question, metadata, debugInfo);
		return status(httpStatus.code, Json.toJson(httpResponse));
	}
	
	public static Result getQuestions() {
		HTTPStatus httpStatus = new HTTPStatus();
		String metadata = null;
		String debugInfo = null;
		List<DBObject> questions = null;
		
		DBObject dbObjQuery = null;
		QuestionGetForm questionGetForm = null;
		int resultCount = 0;
		
		Form<QuestionGetForm> form = Form.form(QuestionGetForm.class);
		if(form.hasErrors()) {
			httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
			httpStatus.setDeveloperMessage("Error in submitted query!! Check models.Question.java file");
		}  else if (questionDAO == null) {
			// if not connected to Questions DB
			httpStatus.setCode(HTTPStatusCode.GONE);
			httpStatus.setDeveloperMessage("Not connected to Questions DB");
		} else {
			questionGetForm = form.bindFromRequest().get();
			
			if (questionGetForm.q == null || questionGetForm.q == "") {
				httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
				httpStatus.setDeveloperMessage("Request Query invalid. Make sure query is not empty or null.");
			} else if (questionGetForm.method.equals("basic")) {
				// do nothing as of now
			} else if (questionGetForm.method.equals("generic")) {
				try {
					DB db = DAOUtils.questionMongo.datastore.getDB();
					DBCollection dbCollection = db.getCollection(Question.class.getSimpleName());
					dbObjQuery = (DBObject) JSON.parse(questionGetForm.q);
					DBObject dbObjSortQuery = (DBObject) JSON.parse(questionGetForm.sort);
					questions = dbCollection.find(dbObjQuery).sort(dbObjSortQuery).skip(questionGetForm.start).limit(questionGetForm.rows).toArray();
					resultCount = questions.size();
					httpStatus.setCode(HTTPStatusCode.OK);
					httpStatus.setDeveloperMessage("Query executed successfully.");
				} catch (Exception e) {
					httpStatus.setCode(HTTPStatusCode.NOT_FOUND);
					httpStatus.setDeveloperMessage("Question not found. \n"
							+ "Either id is invalid or question doesnot exist in database. \n"
							+ "Also check that api is pointed to correct database. \n"
							+ "If all seems ok, notify the fucking developers."
							+ e.toString());
					e.printStackTrace();
				}
			} else {
				httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
				httpStatus.setDeveloperMessage("Request method invalid. Make sure method parameter is passed correctly.");
			}
		}
		
		HTTPResponse<List<DBObject>, String, String> httpResponse = new HTTPResponse<List<DBObject>, String, String>(httpStatus, questions, metadata, debugInfo);
		return status(httpStatus.code, Json.toJson(httpResponse));
	}

	/**
	 * @param id
	 * @return
	 */
	private static boolean isInvalidQuestionId(String id) {
		return id.isEmpty() || id==null;
	}
	
	
}

/**
 * 
 */
package controllers;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.util.JSON;

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
	
	public static Result getQuestions(String method, String q, int limit, int start) {
		HTTPStatus status = new HTTPStatus();
		String metadata = null;
		String debugInfo = null;
		List<DBObject> questions = null;
		DBObject dbObjQuery = null;
		int resultCount = 0;
		DBObject response = new BasicDBObject();
		if (q == null) {
			status.setCode(HTTPStatusCode.BAD_REQUEST);
			status.setDeveloperMessage("Request Query invalid. Make sure query is not empty or null.");
		} else if (questionDAO == null) {
			// if not connected to Questions DB
			status.setCode(HTTPStatusCode.GONE);
			status.setDeveloperMessage("Not connected to Questions DB");
		} else {
			// Aal eez well.
			if (method.equals("basic")) {
				// do nothing as of now
			} else if (method.equals("generic")) {
				try {
					DB db = DAOUtils.questionMongo.mongo.getDB(Play
							.application().configuration()
							.getString("question.mongodb.uri.db"));
					DBCollection dbCollection = db.getCollection("Question");
					dbObjQuery = (DBObject) JSON.parse(q);
					resultCount = dbCollection.find(dbObjQuery).count();
					questions = dbCollection.find(dbObjQuery).skip(start).limit(limit).toArray();
					status.setDeveloperMessage("Query executed successfully.");
				} catch (Exception e) {
					status.setCode(HTTPStatusCode.NOT_FOUND);
					status.setDeveloperMessage("Question not found. \n"
							+ "Either id is invalid or question doesnot exist in database. \n"
							+ "Also check that api is pointed to correct database. \n"
							+ "If all seems ok, notify the fucking developers."
							+ e.toString());
					e.printStackTrace();
				}
			} else {
				status.setCode(HTTPStatusCode.BAD_REQUEST);
				status.setDeveloperMessage("Request method invalid. Make sure method parameter is passed correctly.");
			}
		}
		response.put("status", status);
		response.put("count", resultCount);
		response.put("response", questions);
		response.put("metadata", metadata);
		response.put("debugInfo", debugInfo);
		return status(status.code, Json.toJson(response));
	}

	/**
	 * @param id
	 * @return
	 */
	private static boolean isInvalidQuestionId(String id) {
		return id.isEmpty() || id==null;
	}
	
	
}

/**
 * 
 */
package controllers;

import java.util.List;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bson.types.ObjectId;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.util.JSON;

import models.Metadata;
import models.MetadataGetCollection;
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
		// 1. Start stopwatch
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		// 2. Initialize http response objects
		HTTPStatus httpStatus = new HTTPStatus();
		Question question = null;
		Metadata metadata = new Metadata();
		String debugInfo = null;
		
		// 3. Calculate response
		Form<Question> form;
		try {
			form = Form.form(Question.class);
			if(form.hasErrors()) {
				throw new Exception("Form has errors");
			}
			question = form.bindFromRequest().get();

			if(questionDAO==null) {
				// if not connected to Questions DB
				httpStatus.setCode(HTTPStatusCode.GONE);
				httpStatus.setDeveloperMessage("Not connected to Questions DB");
			} else {
				try {
					questionDAO.save(question, WriteConcern.SAFE);
					question = questionDAO.get(question.get_id());
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
					//debugInfo = ExceptionUtils.getFullStackTrace(e.fillInStackTrace());
					e.printStackTrace();
				}
			}
		} catch (Exception e1) {
			httpStatus.setCode(HTTPStatusCode.INTERNAL_SERVER_ERROR);
			httpStatus.setDeveloperMessage("Error in submitted query!! Check models.Question.java file");
			e1.printStackTrace();
		}

		// 4. Stop stopwatch
		stopWatch.stop();

		// 5. Calculate final HTTP response
		metadata.setQTime(stopWatch.getTime());
		HTTPResponse<Question, Metadata, String> httpResponse = new HTTPResponse<Question, Metadata, String>(httpStatus, metadata, question, debugInfo);
		return status(httpStatus.code, Json.toJson(httpResponse)); 
	}
	
	public static Result getQuestionById(String id) {
		// 1. Start stopwatch
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		// 2. Initialize http response objects
		HTTPStatus httpStatus = new HTTPStatus();
		Question question = null;
		Metadata metadata = new Metadata();
		String debugInfo = null;
		
		// 3. Calculate response
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
				if(question == null) {
					httpStatus.setCode(HTTPStatusCode.NOT_FOUND);
					httpStatus.setDeveloperMessage("Question not found in DB");
				} else {
					httpStatus.setCode(HTTPStatusCode.OK);
					httpStatus.setDeveloperMessage("Question found in DB");
				}
			} catch (Exception e) {
				httpStatus.setCode(HTTPStatusCode.NOT_FOUND);
				httpStatus.setDeveloperMessage("Question not found. \n"
						+ "Either id is invalid or question doesnot exist in database. \n"
						+ "Also check that api is pointed to correct database. \n"
						+ "If all seems ok, notify the fucking developers.");
				e.printStackTrace();
			}
		}

		// 4. Stop stopwatch
		stopWatch.stop();

		// 5. Calculate final HTTP response
		metadata.setQTime(stopWatch.getTime());
		HTTPResponse<Question, Metadata, String> httpResponse = new HTTPResponse<Question, Metadata, String>(httpStatus, metadata, question, debugInfo);
		return status(httpStatus.code, Json.toJson(httpResponse));
	}
	
	public static Result getQuestions() {
		// 1. Start stopwatch
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		// 2. Initialize http response objects
		HTTPStatus httpStatus = new HTTPStatus();
		MetadataGetCollection metadata = new MetadataGetCollection();
		String debugInfo = null;
		List<DBObject> questions = null;

		// 3. Calculate response
		DBObject dbObjQuery = null;
		QuestionGetForm questionGetForm = null;
		Integer numFound = 0;

		try {
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
						numFound = dbCollection.find(dbObjQuery).count();
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
		} catch (Exception e) {
			httpStatus.setCode(HTTPStatusCode.INTERNAL_SERVER_ERROR);
			httpStatus.setDeveloperMessage("Unknown exception. See DebugInfo for more info");
			debugInfo = ExceptionUtils.getFullStackTrace(e.fillInStackTrace());
			e.printStackTrace();
		}

		// 4. Stop stopwatch
		stopWatch.stop();

		// 5. Calculate final HTTP response
		metadata.setQTime(stopWatch.getTime());
		metadata.setNumFound(numFound);
		HTTPResponse<List<DBObject>, MetadataGetCollection, String> httpResponse = new HTTPResponse<List<DBObject>, MetadataGetCollection, String>(httpStatus, metadata, questions, debugInfo);
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

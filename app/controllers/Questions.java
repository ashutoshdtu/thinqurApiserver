/**
 * 
 */
package controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.util.JSON;

import models.Question;
import models.UserRef;
import models.restapi.APIForm;
import models.restapi.HTTPResponse;
import models.restapi.HTTPStatus;
import models.restapi.HTTPStatusCode;
import models.restapi.Metadata;
import models.restapi.MetadataGetCollection;
import models.restapi.QuestionGetForm;
import play.data.Form;
import play.libs.Json;
//import play.modules.spring.Spring;
import play.mvc.Controller;
import play.mvc.Result;
import services.QuestionDAO;
import utils.DAOUtils;
import utils.commonUtils;

//import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ashutosh
 *
 */
public class Questions extends Controller {

	static QuestionDAO questionDAO = DAOUtils.questionDAO;
	
	// GET     	/v1/questions/:id 			controllers.Questions.getQuestionById(id: String)
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
				question = questionDAO.get(id);
				if(question == null) {
					httpStatus.setCode(HTTPStatusCode.NOT_FOUND);
					httpStatus.setDeveloperMessage("Question not found in DB");
				} else {
					httpStatus.setCode(HTTPStatusCode.OK);
					httpStatus.setDeveloperMessage("Question found in DB");
					try {
						Form<APIForm> form = Form.form(APIForm.class);
						if(form.hasErrors()) {
							throw new Exception("Form has errors");
						}
						APIForm apiForm = form.bindFromRequest().get();
						if(apiForm.userId!=null && commonUtils.isValidUUID(apiForm.userId)) {
							question.setUserId(apiForm.userId);
						}
					} catch(Exception e){
						
					}
				}
			} catch (Exception e) {
				httpStatus.setCode(HTTPStatusCode.NOT_FOUND);
				httpStatus.setDeveloperMessage("Question not found. "
						+ "Either id is invalid or question doesnot exist in database. "
						+ "Also check that api is pointed to correct database. "
						+ "If all seems ok, notify the fucking developers.");
				debugInfo = ExceptionUtils.getFullStackTrace(e.fillInStackTrace());
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

	// DELETE  	/v1/questions/:id 			controllers.Questions.deleteQuestionById(id: String)
	public static Result deleteQuestionById(String id) {
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
					questionDAO.delete(question, WriteConcern.SAFE);
					question = questionDAO.get(question.get_id());
					if(question == null) {
						httpStatus.setCode(HTTPStatusCode.RESOURCE_DELETED);
						httpStatus.setDeveloperMessage("Question was successfully deleted from DB");
					} else {
						httpStatus.setCode(HTTPStatusCode.GONE);
						httpStatus.setDeveloperMessage("Question could not be deleted from DB. Report the problem.");
					}
				}
			} catch (Exception e) {
				httpStatus.setCode(HTTPStatusCode.INTERNAL_SERVER_ERROR);
				httpStatus.setDeveloperMessage("Could not complete delete action. "
						+ "Either id is invalid or question doesnot exist in database. "
						+ "Also check that api is pointed to correct database. "
						+ "If all seems ok, notify the fucking developers.");
				debugInfo = ExceptionUtils.getFullStackTrace(e.fillInStackTrace());
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

	// GET 		/v1/questions 				controllers.Questions.getQuestions()
	public static Result getQuestions() {
		// 1. Start stopwatch
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		// 2. Initialize http response objects
		HTTPStatus httpStatus = new HTTPStatus();
		MetadataGetCollection metadata = new MetadataGetCollection();
		List<Question> questions = new ArrayList<Question>();
		String debugInfo = null;

		// 3. Calculate response
		DBObject dbObjQuery = null;
		QuestionGetForm questionGetForm = null;
		Integer numFound = 0;

		try {
			Form<QuestionGetForm> form = Form.form(QuestionGetForm.class);
			if(form.hasErrors()) {
				throw new Exception("Form has errors");
			}
			questionGetForm = form.bindFromRequest().get();

			if(questionDAO==null) {
				// if not connected to Questions DB
				httpStatus.setCode(HTTPStatusCode.GONE);
				httpStatus.setDeveloperMessage("Not connected to Questions DB");
			} else {
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
						List<DBObject> questionsDBObject = dbCollection.find(dbObjQuery).sort(dbObjSortQuery).skip(questionGetForm.start).limit(questionGetForm.rows).toArray();
						numFound = dbCollection.find(dbObjQuery).count();
						questions = new ObjectMapper().readValue(questionsDBObject.toString(), new TypeReference<List<Question>>() { });
						if(questionGetForm.userId!=null && commonUtils.isValidUUID(questionGetForm.userId)) {
							for(Question q: questions) {
								q.setUserId(questionGetForm.userId);
							}
						}
						httpStatus.setCode(HTTPStatusCode.OK);
						httpStatus.setDeveloperMessage("Query executed successfully.");
					} catch (Exception e) {
						httpStatus.setCode(HTTPStatusCode.NOT_FOUND);
						httpStatus.setDeveloperMessage("Question not found. "
								+ "Either id is invalid or question doesnot exist in database. "
								+ "Also check that api is pointed to correct database. "
								+ "If all seems ok, notify the fucking developers. "
								+ e.toString());
						debugInfo = ExceptionUtils.getFullStackTrace(e.fillInStackTrace());
						e.printStackTrace();
					}
				} else {
					httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
					httpStatus.setDeveloperMessage("Request method invalid. Make sure method parameter is passed correctly.");
				}
			}
		} catch (Exception e) {
			httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
			httpStatus.setDeveloperMessage("Error in submitted query!! Check models.QuestionGetForm.java file");
			debugInfo = ExceptionUtils.getFullStackTrace(e.fillInStackTrace());
			e.printStackTrace();
		}

		// 4. Stop stopwatch
		stopWatch.stop();

		// 5. Calculate final HTTP response
		metadata.setQTime(stopWatch.getTime());
		metadata.setNumFound(numFound);
		HTTPResponse<List<Question>, MetadataGetCollection, String> httpResponse = new HTTPResponse<List<Question>, MetadataGetCollection, String>(httpStatus, metadata, questions, debugInfo);
		return status(httpStatus.code, Json.toJson(httpResponse));
	}

	// POST 	/v1/questions 				controllers.Questions.postQuestion()
	// PUT 		/v1/questions 				controllers.Questions.postQuestion()
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
					debugInfo = ExceptionUtils.getFullStackTrace(e.fillInStackTrace());
					e.printStackTrace();
				}
			}
		} catch (Exception e1) {
			httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
			httpStatus.setDeveloperMessage("Error in submitted query!! Check models.Question.java file");
			debugInfo = ExceptionUtils.getFullStackTrace(e1.fillInStackTrace());
			e1.printStackTrace();
		}

		// 4. Stop stopwatch
		stopWatch.stop();

		// 5. Calculate final HTTP response
		metadata.setQTime(stopWatch.getTime());
		HTTPResponse<Question, Metadata, String> httpResponse = new HTTPResponse<Question, Metadata, String>(httpStatus, metadata, question, debugInfo);
		return status(httpStatus.code, Json.toJson(httpResponse)); 
	}
	
	// POST 	/v1/questions/<id>/answers/<id>/upvotedBy 				controllers.Questions.postAnswerUpvote()
	public static Result postAnswerUpvote(String questionId, String answerId) {
		// 1. Start stopwatch
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		// 2. Initialize http response objects
		HTTPStatus httpStatus = new HTTPStatus();
		UserRef userRef = null;
		Metadata metadata = new Metadata();
		String debugInfo = null;

		// 3. Calculate response
		Form<UserRef> form;
		try {
			form = Form.form(UserRef.class);
			if(form.hasErrors()) {
				throw new Exception("Form has errors");
			}
			userRef = form.bindFromRequest().get();

			if(questionDAO==null) {
				// if not connected to Questions DB
				httpStatus.setCode(HTTPStatusCode.GONE);
				httpStatus.setDeveloperMessage("Not connected to Questions DB");
			} else {
				try {
					Question question = questionDAO.get(new ObjectId(questionId));
					question.setUserId(userRef.getId());
					if(!question.isAnswerUpvotedByUser){
						Query<Question> q = questionDAO.getDatastore().createQuery(Question.class).field("answers._id").equal(new ObjectId(answerId));
						UpdateOperations<Question> ops = questionDAO.getDatastore().createUpdateOperations(Question.class).disableValidation().add("answers.$.upvotedBy", userRef).enableValidation();
						questionDAO.update(q, ops);
						question = questionDAO.get(new ObjectId(questionId));
						if(question == null) {
							httpStatus.setCode(HTTPStatusCode.GONE);
							httpStatus.setDeveloperMessage("Question was written to DB but was not returned successfully");
						} else {
							httpStatus.setCode(HTTPStatusCode.RESOURCE_CREATED);
							httpStatus.setDeveloperMessage("Question was successfully written to DB");
						}
					} else {
						httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
						httpStatus.setDeveloperMessage("Question was already answered by the user");
					}
				} catch (Exception e) {
					userRef = null;
					httpStatus.setCode(HTTPStatusCode.INTERNAL_SERVER_ERROR);
					httpStatus.setDeveloperMessage("Exception occured while writing to Question DB");
					debugInfo = ExceptionUtils.getFullStackTrace(e.fillInStackTrace());
					e.printStackTrace();
				}
			}
		} catch (Exception e1) {
			httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
			httpStatus.setDeveloperMessage("Error in submitted query!! Check models.UserRef.java file");
			debugInfo = ExceptionUtils.getFullStackTrace(e1.fillInStackTrace());
			e1.printStackTrace();
		}

		// 4. Stop stopwatch
		stopWatch.stop();

		// 5. Calculate final HTTP response
		metadata.setQTime(stopWatch.getTime());
		HTTPResponse<UserRef, Metadata, String> httpResponse = new HTTPResponse<UserRef, Metadata, String>(httpStatus, metadata, userRef, debugInfo);
		return status(httpStatus.code, Json.toJson(httpResponse)); 
	}
	
	// POST 	/v1/questions/<id>/upvotedBy 				controllers.Questions.postQuestionUserUpdates()
	// POST 	/v1/questions/<id>/downvotedBy 				controllers.Questions.postQuestionUserUpdates()
	// POST 	/v1/questions/<id>/followedBy 				controllers.Questions.postQuestionUserUpdates()
	public static Result postQuestionUserUpdates(String questionId, String operation) {
		// 1. Start stopwatch
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		// 2. Initialize http response objects
		HTTPStatus httpStatus = new HTTPStatus();
		UserRef userRef = null;
		Metadata metadata = new Metadata();
		String debugInfo = null;

		// 3. Calculate response
		Form<UserRef> form;
		try {
			form = Form.form(UserRef.class);
			if(form.hasErrors()) {
				throw new Exception("Form has errors");
			}
			userRef = form.bindFromRequest().get();

			if(questionDAO==null) {
				// if not connected to Questions DB
				httpStatus.setCode(HTTPStatusCode.GONE);
				httpStatus.setDeveloperMessage("Not connected to Questions DB");
			} else {
				try {
					Question question = questionDAO.get(new ObjectId(questionId));
					question.setUserId(userRef.getId());
					if( isValidOperation(question, operation) ){
						Query<Question> q = questionDAO.getDatastore().createQuery(Question.class).field("_id").equal(new ObjectId(questionId));
						UpdateOperations<Question> ops = questionDAO.getDatastore().createUpdateOperations(Question.class).disableValidation().add(operation, userRef).enableValidation();
						questionDAO.update(q, ops);
						question = questionDAO.get(new ObjectId(questionId));
						if(question == null) {
							httpStatus.setCode(HTTPStatusCode.GONE);
							httpStatus.setDeveloperMessage("Question was written to DB but was not returned successfully");
						} else {
							httpStatus.setCode(HTTPStatusCode.RESOURCE_CREATED);
							httpStatus.setDeveloperMessage("Question was successfully written to DB");
						}
					} else {
						httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
						httpStatus.setDeveloperMessage("Invalid Operation");
					}
				} catch (Exception e) {
					userRef = null;
					httpStatus.setCode(HTTPStatusCode.INTERNAL_SERVER_ERROR);
					httpStatus.setDeveloperMessage("Exception occured while writing to Question DB");
					debugInfo = ExceptionUtils.getFullStackTrace(e.fillInStackTrace());
					e.printStackTrace();
				}
			}
		} catch (Exception e1) {
			httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
			httpStatus.setDeveloperMessage("Error in submitted query!! Check models.UserRef.java file");
			debugInfo = ExceptionUtils.getFullStackTrace(e1.fillInStackTrace());
			e1.printStackTrace();
		}

		// 4. Stop stopwatch
		stopWatch.stop();

		// 5. Calculate final HTTP response
		metadata.setQTime(stopWatch.getTime());
		HTTPResponse<UserRef, Metadata, String> httpResponse = new HTTPResponse<UserRef, Metadata, String>(httpStatus, metadata, userRef, debugInfo);
		return status(httpStatus.code, Json.toJson(httpResponse)); 
	}

	/**
	 * @param question
	 * @param operation 
	 * @return
	 */
	private static boolean isValidOperation(Question question, String operation) {
		if(operation.equals("upvotedBy") || operation.equals("downvotedBy")) {
			return !(question.isQuestionUpvotedByUser || question.isQuestionDownvotedByUser);
		} else if(operation.equals("followedBy")) {
			return !question.isFollowedByUser;
		}
		return false;
	}

	/**
	 * @param id
	 * @return
	 */
	private static boolean isInvalidQuestionId(String id) {
		return id.isEmpty() || id==null;
	}

}

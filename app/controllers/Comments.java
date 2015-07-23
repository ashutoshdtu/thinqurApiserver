/**
 * 
 */
package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Comment;
import models.Question;
import models.User;
import models.restapi.CommentGetForm;
import models.restapi.HTTPResponse;
import models.restapi.HTTPStatus;
import models.restapi.HTTPStatusCode;
import models.restapi.Metadata;
import models.restapi.MetadataGetCollection;
import models.restapi.UserGetForm;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.StopWatch;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import play.data.Form;
import play.libs.Json;
//import play.modules.spring.Spring;
import play.mvc.Controller;
import play.mvc.Result;
import services.CommentDAO;
import services.QuestionDAO;
import utils.DAOUtils;
import utils.commonUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.util.JSON;

//import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author sanket
 */
public class Comments extends Controller {
	static CommentDAO commentDAO = DAOUtils.commentDAO;
	static QuestionDAO questionDAO = DAOUtils.questionDAO;
	// POST /v1/comments controllers.Comments.postComment()
	// PUT  /v1/comments controllers.Comments.postComment()
	public static Result postComment() {
		// 1. Start stopwatch
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		// 2. Initialize http response objects
		HTTPStatus httpStatus = new HTTPStatus();
		Comment comment = null;
		Metadata metadata = new Metadata();
		String debugInfo = null;
		Question question = null;

		// 3. Calculate response
		Form<Comment> form;
		try {
			form = Form.form(Comment.class);
			if (form.hasErrors()) {
				throw new Exception("Form has errors");
			}
			comment = form.bindFromRequest().get();
			if (commentDAO == null) {
				// if not connected to Comments DB
				httpStatus.setCode(HTTPStatusCode.GONE);
				httpStatus.setDeveloperMessage("Not connected to Comments DB");
			} else {
				try {
					commentDAO.save(comment, WriteConcern.SAFE);
					Query<Question> q = questionDAO.getDatastore().createQuery(Question.class).field("_id").equal(new ObjectId(comment.questionId));
					UpdateOperations<Question> ops;
					ops = questionDAO.getDatastore().createUpdateOperations(Question.class).inc("totalComments");
					questionDAO.update(q, ops);
					/*question = questionDAO.get(comment.questionId);
					question.totalComments++;
					questionDAO.save(question, WriteConcern.SAFE);*/
					comment = commentDAO.get(comment.get_id());
					if (comment == null) {
						httpStatus.setCode(HTTPStatusCode.GONE);
						httpStatus
								.setDeveloperMessage("Comment was written to DB but was not returned successfully");
					} else {
						httpStatus.setCode(HTTPStatusCode.RESOURCE_CREATED);
						httpStatus
								.setDeveloperMessage("Comment was successfully written to DB");
					}
				} catch (Exception e) {
					comment = null;
					httpStatus.setCode(HTTPStatusCode.INTERNAL_SERVER_ERROR);
					httpStatus
							.setDeveloperMessage("Exception occured while writing to Comment DB");
					debugInfo = ExceptionUtils.getFullStackTrace(e
							.fillInStackTrace());
					e.printStackTrace();
				}
			}
		} catch (Exception e1) {
			httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
			httpStatus
					.setDeveloperMessage("Error in submitted query!! Check models.Comment.java file");
			debugInfo = ExceptionUtils.getFullStackTrace(e1.fillInStackTrace());
			e1.printStackTrace();
		}

		// 4. Stop stopwatch
		stopWatch.stop();

		// 5. Calculate final HTTP response
		metadata.setQTime(stopWatch.getTime());
		HTTPResponse<Comment, Metadata, String> httpResponse = new HTTPResponse<Comment, Metadata, String>(
				httpStatus, metadata, comment, debugInfo);
		return status(httpStatus.code, Json.toJson(httpResponse));
	}	

	public static Result getCommentById(String id) {
		// 1. Start stopwatch
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		// 2. Initialize http response objects
		HTTPStatus httpStatus = new HTTPStatus();
		Comment comment = null;
		Metadata metadata = new Metadata();
		String debugInfo = null;

		// 3. Calculate response
		if (isInvalidId(id)) {
			httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
			httpStatus
					.setDeveloperMessage("Comment id invalid. Make sure id is not empty or null. Also check if its a valid UUID");
		} else if (commentDAO == null) {
			httpStatus.setCode(HTTPStatusCode.GONE);
			httpStatus.setDeveloperMessage("Not connected to Comment DB");
		} else {
			try {
				comment = commentDAO.get(id);
				if (comment == null) {
					httpStatus.setCode(HTTPStatusCode.NOT_FOUND);
					httpStatus.setDeveloperMessage("Comment not found in DB");
				} else {
					httpStatus.setCode(HTTPStatusCode.OK);
					httpStatus.setDeveloperMessage("Comment found in DB");
				}
			} catch (Exception e) {
				httpStatus.setCode(HTTPStatusCode.NOT_FOUND);
				httpStatus
						.setDeveloperMessage("User not found. \n"
								+ "Either id is invalid or comment doesnot exist in database. \n"
								+ "Also check that api is pointed to correct database. \n"
								+ "If all seems ok, notify the fucking developers.");
				debugInfo = ExceptionUtils.getFullStackTrace(e
						.fillInStackTrace());
				e.printStackTrace();
			}
		}

		// 4. Stop stopwatch
		stopWatch.stop();

		// 5. Calculate final HTTP response
		metadata.setQTime(stopWatch.getTime());
		HTTPResponse<Comment, Metadata, String> httpResponse = new HTTPResponse<Comment, Metadata, String>(
				httpStatus, metadata, comment, debugInfo);
		return status(httpStatus.code, Json.toJson(httpResponse));
	}
	
	public static Result getComments() {
		// 1. Start stopwatch
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		// 2. Initialize http response objects
		HTTPStatus httpStatus = new HTTPStatus();
		MetadataGetCollection metadata = new MetadataGetCollection();
		List<Comment> comments = new ArrayList<Comment>();
		String debugInfo = null;

		// 3. Calculate response
		DBObject dbObjQuery = null;
		CommentGetForm commentGetForm = null;
		Integer numFound = 0;

		try {
			Form<CommentGetForm> form = Form.form(CommentGetForm.class);
			if(form.hasErrors()) {
				throw new Exception("Form has errors");
			}
			commentGetForm = form.bindFromRequest().get();

			if(commentDAO==null) {
				// if not connected to Users DB
				httpStatus.setCode(HTTPStatusCode.GONE);
				httpStatus.setDeveloperMessage("Not connected to Comments DB");
			} else {
				if (commentGetForm.q == null || commentGetForm.q == "") {
					httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
					httpStatus.setDeveloperMessage("Request Query invalid. Make sure query is not empty or null.");
				} else if (commentGetForm.method.equals("basic")) {
					// do nothing as of now
				} else if (commentGetForm.method.equals("generic")) {
					try {
						DB db = DAOUtils.commentMongo.datastore.getDB();
						DBCollection dbCollection = db.getCollection(Comment.class.getSimpleName());
						dbObjQuery = (DBObject) JSON.parse(commentGetForm.q);
						DBObject dbObjSortQuery = (DBObject) JSON.parse(commentGetForm.sort);
						List<DBObject> commentDBObject = dbCollection.find(dbObjQuery).sort(dbObjSortQuery).skip(commentGetForm.start).limit(commentGetForm.rows).toArray();
						numFound = dbCollection.find(dbObjQuery).count();
						comments = new ObjectMapper().readValue(commentDBObject.toString(), new TypeReference<List<Comment>>() {});
						httpStatus.setCode(HTTPStatusCode.OK);
						httpStatus.setDeveloperMessage("Query executed successfully.");
					} catch (Exception e) {
						httpStatus.setCode(HTTPStatusCode.NOT_FOUND);
						httpStatus.setDeveloperMessage("Comments not found. "
								+ "Either id is invalid or comment doesnot exist in database. "
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
			httpStatus.setDeveloperMessage("Error in submitted query!! Check models.CommentGetForm.java file");
			debugInfo = ExceptionUtils.getFullStackTrace(e.fillInStackTrace());
			e.printStackTrace();
		}

		// 4. Stop stopwatch
		stopWatch.stop();

		// 5. Calculate final HTTP response
		metadata.setQTime(stopWatch.getTime());
		metadata.setNumFound(numFound);
		HTTPResponse<List<Comment>, MetadataGetCollection, String> httpResponse = new HTTPResponse<List<Comment>, MetadataGetCollection, String>(httpStatus, metadata, comments, debugInfo);
		return status(httpStatus.code, Json.toJson(httpResponse));
	}

	private static boolean isInvalidId(String id) {
		return id.isEmpty() || id == null;
	}
}
package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import models.HTTPResponse;
import models.HTTPStatus;
import models.HTTPStatusCode;
import models.LinkedAccount;
import models.Metadata;
import models.MetadataGetCollection;
import models.MetadataPostUser;
import models.Question;
import models.QuestionGetForm;
import models.User;
import models.UserGetForm;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.StopWatch;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.util.JSON;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserDAO;
import utils.commonUtils;
import utils.DAOUtils;

public class Users extends Controller {
	static UserDAO userDAO = DAOUtils.userDAO;

	public static Result postUser() {
		// 1. Start stopwatch
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		// 2. Initialize http response objects
		HTTPStatus httpStatus = new HTTPStatus();
		User user = null;
		User existingUser = null;
		MetadataPostUser metadata = new MetadataPostUser();
		String debugInfo = null;

		// 3. Calculate response
		Form<User> form;
		try {
			form = Form.form(User.class);
			if (form.hasErrors()) {
				throw new Exception("Form has errors");
			}
			user = form.bindFromRequest().get();
			if (userDAO == null) {
				// if not connected to Users DB
				httpStatus.setCode(HTTPStatusCode.GONE);
				httpStatus.setDeveloperMessage("Not connected to User DB");
			} else {
				try {
					existingUser = userDAO.getByEmail(user.email);
					if (existingUser != null) {
						metadata.setNewRecord(false);
						user.set_id(existingUser.get_id());
						if (compareUserList(existingUser.linkedAccounts, user.linkedAccounts)) {
							//System.out.print("asgagdsfhgsdf");
							existingUser.linkedAccounts
									.addAll(user.linkedAccounts);
						}
						user = existingUser;
					}
					userDAO.save(user, WriteConcern.SAFE);
					user = userDAO.get(user.get_id());
					if (user == null) {
						httpStatus.setCode(HTTPStatusCode.GONE);
						httpStatus
								.setDeveloperMessage("User was written to DB but was not returned successfully");
					} else {
						httpStatus.setCode(HTTPStatusCode.RESOURCE_CREATED);
						httpStatus.setDeveloperMessage("User was successfully written to DB");
					}
				} catch (Exception e) {
					user = null;
					httpStatus.setCode(HTTPStatusCode.INTERNAL_SERVER_ERROR);
					httpStatus
							.setDeveloperMessage("Exception occured while writing to User DB");
					debugInfo = ExceptionUtils.getFullStackTrace(e
							.fillInStackTrace());
					e.printStackTrace();
				}
			}
		} catch (Exception e1) {
			httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
			httpStatus.setDeveloperMessage("Error in submitted query!! Check models.Users.java file");
			debugInfo = ExceptionUtils.getFullStackTrace(e1.fillInStackTrace());
			e1.printStackTrace();
		}

		// 4. Stop stopwatch
		stopWatch.stop();
		// 5. Calculate final HTTP response
		metadata.setQTime(stopWatch.getTime());
		HTTPResponse<User, MetadataPostUser, String> httpResponse = new HTTPResponse<User, MetadataPostUser, String>(
				httpStatus, metadata, user, debugInfo);
		return status(httpStatus.code, Json.toJson(httpResponse));
	}

	public static Result getUserById(String id) {
		// 1. Start stopwatch
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		// 2. Initialize http response objects
		HTTPStatus httpStatus = new HTTPStatus();
		User user = null;
		Metadata metadata = new Metadata();
		String debugInfo = null;

		// 3. Calculate response
		if (isInvalidUserId(id)) {
			httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
			httpStatus
					.setDeveloperMessage("User id invalid. Make sure id is not empty or null. Also check if its a valid UUID");
		} else if (userDAO == null) {
			httpStatus.setCode(HTTPStatusCode.GONE);
			httpStatus.setDeveloperMessage("Not connected to User DB");
		} else {
			try {
				user = userDAO.get(id);
				if (user == null) {
					httpStatus.setCode(HTTPStatusCode.NOT_FOUND);
					httpStatus.setDeveloperMessage("User not found in DB");
				} else {
					httpStatus.setCode(HTTPStatusCode.OK);
					httpStatus.setDeveloperMessage("User found in DB");
				}
			} catch (Exception e) {
				httpStatus.setCode(HTTPStatusCode.NOT_FOUND);
				httpStatus
						.setDeveloperMessage("User not found. \n"
								+ "Either id is invalid or user doesnot exist in database. \n"
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
		HTTPResponse<User, Metadata, String> httpResponse = new HTTPResponse<User, Metadata, String>(
				httpStatus, metadata, user, debugInfo);
		return status(httpStatus.code, Json.toJson(httpResponse));
	}

	public static Result getUserByEmail(String email) {
		// 1. Start stopwatch
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		// 2. Initialize http response objects
		HTTPStatus httpStatus = new HTTPStatus();
		User user = null;
		Metadata metadata = new Metadata();
		String debugInfo = null;

		// 3. Calculate response
		if (isInvalidUserId(email)) {
			httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
			httpStatus
					.setDeveloperMessage("User id invalid. Make sure id is not empty or null. Also check if its a valid UUID");
		} else if (userDAO == null) {
			httpStatus.setCode(HTTPStatusCode.GONE);
			httpStatus.setDeveloperMessage("Not connected to User DB");
		} else {
			try {
				user = userDAO.getByEmail(email);
				if (user == null) {
					httpStatus.setCode(HTTPStatusCode.NOT_FOUND);
					httpStatus.setDeveloperMessage("User not found in DB");
				} else {
					httpStatus.setCode(HTTPStatusCode.OK);
					httpStatus.setDeveloperMessage("User found in DB");
				}
			} catch (Exception e) {
				httpStatus.setCode(HTTPStatusCode.NOT_FOUND);
				httpStatus
						.setDeveloperMessage("User not found. \n"
								+ "Either id is invalid or user doesnot exist in database. \n"
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
		HTTPResponse<User, Metadata, String> httpResponse = new HTTPResponse<User, Metadata, String>(
				httpStatus, metadata, user, debugInfo);
		return status(httpStatus.code, Json.toJson(httpResponse));
	}
	
	public static Result getUsers() {
		// 1. Start stopwatch
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		// 2. Initialize http response objects
		HTTPStatus httpStatus = new HTTPStatus();
		MetadataGetCollection metadata = new MetadataGetCollection();
		List<User> users = new ArrayList<User>();
		String debugInfo = null;

		// 3. Calculate response
		DBObject dbObjQuery = null;
		UserGetForm userGetForm = null;
		Integer numFound = 0;

		try {
			Form<UserGetForm> form = Form.form(UserGetForm.class);
			if(form.hasErrors()) {
				throw new Exception("Form has errors");
			}
			userGetForm = form.bindFromRequest().get();

			if(userDAO==null) {
				// if not connected to Users DB
				httpStatus.setCode(HTTPStatusCode.GONE);
				httpStatus.setDeveloperMessage("Not connected to Users DB");
			} else {
				if (userGetForm.q == null || userGetForm.q == "") {
					httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
					httpStatus.setDeveloperMessage("Request Query invalid. Make sure query is not empty or null.");
				} else if (userGetForm.method.equals("basic")) {
					// do nothing as of now
				} else if (userGetForm.method.equals("generic")) {
					try {
						DB db = DAOUtils.userMongo.datastore.getDB();
						DBCollection dbCollection = db.getCollection(User.class.getSimpleName());
						dbObjQuery = (DBObject) JSON.parse(userGetForm.q);
						DBObject dbObjSortQuery = (DBObject) JSON.parse(userGetForm.sort);
						List<DBObject> userDBObject = dbCollection.find(dbObjQuery).sort(dbObjSortQuery).skip(userGetForm.start).limit(userGetForm.rows).toArray();
						numFound = dbCollection.find(dbObjQuery).count();
						users = new ObjectMapper().readValue(userDBObject.toString(), new TypeReference<List<User>>() {});
						httpStatus.setCode(HTTPStatusCode.OK);
						httpStatus.setDeveloperMessage("Query executed successfully.");
					} catch (Exception e) {
						httpStatus.setCode(HTTPStatusCode.NOT_FOUND);
						httpStatus.setDeveloperMessage("User not found. "
								+ "Either id is invalid or user doesnot exist in database. "
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
			httpStatus.setDeveloperMessage("Error in submitted query!! Check models.UserGetForm.java file");
			debugInfo = ExceptionUtils.getFullStackTrace(e.fillInStackTrace());
			e.printStackTrace();
		}

		// 4. Stop stopwatch
		stopWatch.stop();

		// 5. Calculate final HTTP response
		metadata.setQTime(stopWatch.getTime());
		metadata.setNumFound(numFound);
		HTTPResponse<List<User>, MetadataGetCollection, String> httpResponse = new HTTPResponse<List<User>, MetadataGetCollection, String>(httpStatus, metadata, users, debugInfo);
		return status(httpStatus.code, Json.toJson(httpResponse));
	}
	
	public static Result putUser() {
		// 1. Start stopwatch
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		// 2. Initialize http response objects
		HTTPStatus httpStatus = new HTTPStatus();
		User user = null;
		User existingUser = null;
		Metadata metadata = new Metadata();
		String debugInfo = null;

		// 3. Calculate response
		Form<User> form;
		try {
			form = Form.form(User.class);
			if (form.hasErrors()) {
				throw new Exception("Form has errors");
			}
			user = form.bindFromRequest().get();
			if (userDAO == null) {
				// if not connected to Users DB
				httpStatus.setCode(HTTPStatusCode.GONE);
				httpStatus.setDeveloperMessage("Not connected to User DB");
			} else {
				try {					
					userDAO.save(user, WriteConcern.SAFE);
					user = userDAO.get(user.get_id());
					if (user == null) {
						httpStatus.setCode(HTTPStatusCode.GONE);
						httpStatus
								.setDeveloperMessage("User was written to DB but was not returned successfully");
					} else {
						httpStatus.setCode(HTTPStatusCode.RESOURCE_CREATED);
						httpStatus.setDeveloperMessage("User was successfully written to DB");
					}
				} catch (Exception e) {
					user = null;
					httpStatus.setCode(HTTPStatusCode.INTERNAL_SERVER_ERROR);
					httpStatus
							.setDeveloperMessage("Exception occured while writing to User DB");
					debugInfo = ExceptionUtils.getFullStackTrace(e
							.fillInStackTrace());
					e.printStackTrace();
				}
			}
		} catch (Exception e1) {
			httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
			httpStatus.setDeveloperMessage("Error in submitted query!! Check models.Users.java file");
			debugInfo = ExceptionUtils.getFullStackTrace(e1.fillInStackTrace());
			e1.printStackTrace();
		}

		// 4. Stop stopwatch
		stopWatch.stop();
		// 5. Calculate final HTTP response
		metadata.setQTime(stopWatch.getTime());
		HTTPResponse<User, Metadata, String> httpResponse = new HTTPResponse<User, Metadata, String>(
				httpStatus, metadata, user, debugInfo);
		return status(httpStatus.code, Json.toJson(httpResponse));
	}
	
	public static Result deleteUserById(String id) {
		// 1. Start stopwatch
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		// 2. Initialize http response objects
		HTTPStatus httpStatus = new HTTPStatus();
		User user = null;
		Metadata metadata = new Metadata();
		String debugInfo = null;

		// 3. Calculate response
		if(isInvalidUserId(id)) {
			httpStatus.setCode(HTTPStatusCode.BAD_REQUEST);
			httpStatus.setDeveloperMessage("User id invalid. Make sure id is not empty or null. Also check if its a valid UUID");
		} else if(userDAO==null) {
			httpStatus.setCode(HTTPStatusCode.GONE);
			httpStatus.setDeveloperMessage("Not connected to User DB");
		} else {
			try {
				//ObjectId userId = new ObjectId(id);
				user = userDAO.get(id);
				if(user == null) {
					httpStatus.setCode(HTTPStatusCode.NOT_FOUND);
					httpStatus.setDeveloperMessage("User not found in DB");
				} else {
					userDAO.delete(user, WriteConcern.SAFE);
					user = userDAO.get(user.get_id());
					if(user == null) {
						httpStatus.setCode(HTTPStatusCode.RESOURCE_DELETED);
						httpStatus.setDeveloperMessage("User was successfully deleted from DB");
					} else {
						httpStatus.setCode(HTTPStatusCode.GONE);
						httpStatus.setDeveloperMessage("User could not be deleted from DB. Report the problem.");
					}
				}
			} catch (Exception e) {
				httpStatus.setCode(HTTPStatusCode.INTERNAL_SERVER_ERROR);
				httpStatus.setDeveloperMessage("Could not complete delete action. \n"
						+ "Either id is invalid or user doesnot exist in database. \n"
						+ "Also check that api is pointed to correct database. \n"
						+ "If all seems ok, notify the fucking developers.");
				debugInfo = ExceptionUtils.getFullStackTrace(e.fillInStackTrace());
				e.printStackTrace();
			}
		}

		// 4. Stop stopwatch
		stopWatch.stop();

		// 5. Calculate final HTTP response
		metadata.setQTime(stopWatch.getTime());
		HTTPResponse<User, Metadata, String> httpResponse = new HTTPResponse<User, Metadata, String>(httpStatus, metadata, user, debugInfo);
		return status(httpStatus.code, Json.toJson(httpResponse));
	}

	/**
	 * @param id
	 * @return
	 */
	private static boolean isInvalidUserId(String id) {
		return id.isEmpty() || id == null;
	}

	private static boolean compareUserList(List<LinkedAccount> list1,
			List<LinkedAccount> list2) {
		boolean isNew = true;
		for (int i = 0; i < list1.size(); i++) {
			if (list1.get(i).providerKey.equals(list2.get(0).providerKey)) {
				isNew = false;
			}
		}
		return isNew;
	}

}

package utils;

/**
 * Created by ashutosh on 3/04/15.
 */

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import models.*;

import org.mongodb.morphia.Morphia;

import play.Logger;
import play.Play;
import services.QuestionDAO;
import services.UserDAO;

import java.net.UnknownHostException;

public final class DAOUtils {

    public static MongoDBMorphia questionMongo = null;
    public static MongoDBMorphia userMongo = null;
    public static QuestionDAO questionDAO = null;
    public static UserDAO userDAO = null;
    
	/**
     * Connects to MongoDB based on the configuration settings.
     * <p/>
     * If the database is not reachable, an error message is written and the
     * application continues.
     */
    public static boolean connect() {
    	
    	Logger.info("Trying to connect to Question DB");
    	questionMongo = connectToQuestionMongo();
    	if(questionMongo!=null) {
    		Logger.info("Found DB instance at given address:port");
    		try {
				questionDAO = new QuestionDAO(DAOUtils.questionMongo.datastore);
				questionDAO.ensureIndexes();
				Logger.info("Connected to Question DB");
			} catch (Exception e) {
				questionDAO = null;
				Logger.info("Could not connect to Question DB!!");
				e.printStackTrace();
			}
    	} else {
    		Logger.info("Question DB instance not found at given address:port!!");
    	}
    	
    	Logger.info("Trying to connect to User DB");
    	userMongo = connectToUserMongo();
    	if(userMongo!=null) {
    		Logger.info("Found DB instance at given address:port");
    		try {
				userDAO = new UserDAO(DAOUtils.userMongo.datastore);
				userDAO.ensureIndexes();
				Logger.info("Connected to User DB");
			} catch (Exception e) {
				userDAO = null;
				Logger.info("Could not connect to User DB!!");
				e.printStackTrace();
			}
    	} else {
    		Logger.info("User DB instance not found at given address:port!!");
    	}
    	
    	return true;
    }


    /**
	 * Connects to User MongoDB database by reading the configuration file
	 * @return MongoDBMorphia
	 * @throws UnknownHostException 
	 *  
	 */
	private static MongoDBMorphia connectToUserMongo() {
		try {
			String host = Play.application().configuration().getString("user.mongodb.uri.host");
			String port = Play.application().configuration().getString("user.mongodb.uri.port");
			String db = Play.application().configuration().getString("user.mongodb.uri.db");
			userMongo = new MongoDBMorphia(host, port, db);
			userMongo.morphia.map(User.class).map(LinkedAccount.class);
			userMongo.datastore.ensureIndexes();
			userMongo.datastore.ensureCaps();
		} catch (Exception e) {
			userMongo = null;
			e.printStackTrace();
		}

		return userMongo;
		
	}


	/**
	 * Connects to Questions MongoDB database by reading the configuration file
	 * @return MongoDBMorphia
	 * @throws UnknownHostException 
	 *  
	 */
	private static MongoDBMorphia connectToQuestionMongo(){

		try {
			String host = Play.application().configuration().getString("question.mongodb.uri.host");
			String port = Play.application().configuration().getString("question.mongodb.uri.port");
			String db = Play.application().configuration().getString("question.mongodb.uri.db");
			questionMongo = new MongoDBMorphia(host, port, db);
			questionMongo.morphia.map(Question.class).map(Answer.class).map(Tag.class).map(UserRef.class);
			questionMongo.datastore.ensureIndexes();
			questionMongo.datastore.ensureCaps();
		} catch (Exception e) {
			questionMongo = null;
			e.printStackTrace();
		}

		return questionMongo;
	}
	
	
	/**
     * Disconnect from MongoDB.
     */
    public static void disconnect() {
    	questionMongo.closeMongoConnection();
    	userMongo.closeMongoConnection();
    }

}


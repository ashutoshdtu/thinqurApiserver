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

import java.net.UnknownHostException;

public final class DAOUtils {

    public static MongoDBMorphia questionMongo = null;
    public static MongoDBMorphia userMongo = null;
    public static QuestionDAO questionDAO = null;
    //public static UserDAO userDAO = null;
    
	/**
     * Connects to MongoDB based on the configuration settings.
     * <p/>
     * If the database is not reachable, an error message is written and the
     * application continues.
     */
    public static boolean connect() {
    	
    	/*questionMongo = connectToQuestionMongo();
    	if(questionDAO!=null) {
    		Logger.info("Connected to Question DB");
    		
    	}*/
    	
        boolean returnValue = true; 
    	try {
			connectToQuestionMongo();
		} catch (Exception e) {
			questionMongo = null;
			e.printStackTrace();
			returnValue = false;
		}
        try {
			connectToUserMongo();
		} catch (Exception e) {
			userMongo = null;
			e.printStackTrace();
			returnValue = false;
		}
    	return returnValue;
    }


    /**
	 * 
	 */
	private static void connectToUserMongo() {
		// TODO Auto-generated method stub
		
	}


	/**
	 * Connects to Questions MongoDB database by reading the configuration file
	 * @return 
	 * @throws UnknownHostException 
	 *  
	 */
	private static MongoDBMorphia connectToQuestionMongo() throws UnknownHostException {

		String host = Play.application().configuration().getString("question.mongodb.uri.host");
		String port = Play.application().configuration().getString("question.mongodb.uri.port");
		String db = Play.application().configuration().getString("question.mongodb.uri.db");
		questionMongo = new MongoDBMorphia(host, port, db);
		questionMongo.morphia.map(Question.class).map(Answer.class).map(Tag.class).map(UserRef.class);
		questionMongo.datastore.ensureIndexes();
		questionMongo.datastore.ensureCaps();

		return questionMongo;
	}
	
	
	/**
     * Disconnect from MongoDB.
     */
    public static void disconnect() {
    	questionMongo.closeMongoConnection();
    	//userMongo.closeMongoConnection();
    }

}


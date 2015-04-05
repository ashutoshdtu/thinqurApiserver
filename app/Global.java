
import controllers.Questions;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import services.QuestionDAO;
import utils.DAOUtils;


/**
 * @author ashutosh
 *
 */
public class Global extends GlobalSettings {

	    public void onStart(Application app) {
	        Logger.info("Application started!");

	        //DAOUtils.connect();
	        
	        if(DAOUtils.connect()) {
	        	Logger.info("Connected to Databases!");
	        	if(Questions.createQuestionsDAO()) 
	        		Logger.info("Created Questions DAO!");
	        	else 
	        		Logger.error("Could not create Questions DAO!!!");
	        } else {
				Logger.warn("Could not connect to all the databases!!");
			}
	        
	    }

	    public void onStop(Application app) {
	        Logger.info("Appplication stopped!");
	        DAOUtils.disconnect();
	    }
	    
}

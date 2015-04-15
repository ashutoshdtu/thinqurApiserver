
import play.Application;
import play.GlobalSettings;
import play.Logger;
import utils.DAOUtils;


/**
 * @author ashutosh
 *
 */
public class Global extends GlobalSettings {

	    public void onStart(Application app) {
	        Logger.info("Application started!");
	        DAOUtils.connect();
	    }

	    public void onStop(Application app) {
	        Logger.info("Appplication stopped!");
	        DAOUtils.disconnect();
	    }
	    
}

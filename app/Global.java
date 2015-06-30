import java.lang.reflect.Method;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Logger.ALogger;
import play.mvc.Action;
import play.mvc.Http.Request;
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

	@SuppressWarnings("rawtypes")
	public Action onRequest(Request request, Method method) {
		Logger.info("method=" + request.method() + "   uri=" + request.uri()
				+ "   remote-address=" + request.remoteAddress());
		return super.onRequest(request, method);
	}
}

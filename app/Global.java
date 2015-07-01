import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	Date dNow = new Date();
	SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void onStart(Application app) {
		Logger.info(ft.format(dNow) + " Application started!");
		DAOUtils.connect();
	}

	public void onStop(Application app) {
		Logger.info(ft.format(dNow) + " Appplication stopped!");
		DAOUtils.disconnect();
	}

	@SuppressWarnings("rawtypes")
	public Action onRequest(Request request, Method method) {
		Logger.info(ft.format(dNow) + " method=" + request.method() + "   uri="
				+ request.uri() + "   remote-address="
				+ request.remoteAddress());
		return super.onRequest(request, method);
	}
}

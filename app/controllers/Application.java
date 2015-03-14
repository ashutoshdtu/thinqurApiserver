package controllers;

import play.mvc.Controller;
import play.mvc.Result;

@play.db.jpa.Transactional
public class Application extends Controller {

	public static Result index() {
		return ok("Babaji ka Thullu is ready.");
	}

	public static Result output() {
		return ok("good");
////		String query = "select hex(id) AS `id`, category from categories";
//		ResultSet queryResult;
//		//User
//		List<User> user = null;
//		JSONArray categoryResult = new JSONArray();
//		JSONObject result = new JSONObject();
//		JSONObject status = new JSONObject();
//		String s = null;
//		try {
//			
//			//ResultSet resultset = DB.executeQuery("SELECT * from cars");
//			user = User.getResultFromName("Hello");
////			queryResult = PDMySQL.readDataBase(query);
////			categoryResult = PDJsonUtil.convertToJSON(queryResult);
//			status.put("code", 0);
//			status.put("message", "Awesome");
//			PDMySQL.close();
//		} catch (Exception e) {
//			status.put("code", 2);
//			status.put("message", e);
//		}
//		result.put("status", status);
//		result.put("categories", user);
//		s = result.toString();
//		return ok(s);
	}

}

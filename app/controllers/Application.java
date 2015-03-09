package controllers;

import java.sql.ResultSet;

import play.mvc.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import views.html.*;

public class Application extends Controller {

	public static Result index() {
		return ok("Babaji ka Thullu is ready.");
	}

	public static Result output() throws JSONException {
		String query = "select hex(id) AS `id`, category from categories";
		ResultSet queryResult;
		JSONArray categoryResult = new JSONArray();
		JSONObject result = new JSONObject();
		JSONObject status = new JSONObject();
		String s = null;
		try {
			queryResult = PDMySQL.readDataBase(query);
			categoryResult = PDJsonUtil.convertToJSON(queryResult);
			status.put("code", 0);
			status.put("message", "Awesome");
			PDMySQL.close();
		} catch (Exception e) {
			status.put("code", 2);
			status.put("message", e);
		}
		result.put("status", status);
		result.put("categories", categoryResult);
		s = result.toString();
		return ok(s);
	}

}

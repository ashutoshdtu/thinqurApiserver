package controllers;

import java.sql.ResultSet;

import play.mvc.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import views.html.*;

public class Application extends Controller {

	public static Result index() {
		return ok(index.render("Babaji ka Thullu is ready."));
	}

	public static Result output() throws JSONException {
		String query = "select * from categories";
		ResultSet data;
		JSONArray json = new JSONArray();
		JSONObject result = new JSONObject();
		JSONObject status = new JSONObject();
		String s = null;
		try {
			data = MySQL.readDataBase(query);
			json = Json.convertToJSON(data);
			status.put("code", 0);
			status.put("message", "Awesome");
			MySQL.close();
		} catch (Exception e) {
			status.put("code", 2);
			status.put("message", e);
		}
		result.put("status", status);
		result.put("categories", json);
		s = result.toString();
		return ok(s);
	}

}

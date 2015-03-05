package controllers;

import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Json {
	public static JSONObject jsonStatus(JSONObject object, JSONArray array,
			String status, Exception e) throws JSONException {
		if (status == "error") {
			object.put("code", 2);
			object.put("message", e);
		} else {
			object.put("code", 0);
			object.put("message", "Working awesome");
		}
		object.put("result", array);
		return object;
	}

	/**
	 * Convert a result set into a JSON Array
	 * 
	 * @param resultSet
	 * @return a JSONArray
	 * @throws Exception
	 */
	public static JSONArray convertToJSON(ResultSet resultSet) throws Exception {
		JSONArray jsonArray = new JSONArray();
		while (resultSet.next()) {
			int total_rows = resultSet.getMetaData().getColumnCount();
			JSONObject obj = new JSONObject();
			for (int i = 0; i < total_rows; i++) {
				obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
						.toLowerCase(), resultSet.getObject(i + 1));
			}
			jsonArray.put(obj);
		}
		return jsonArray;
	}
}

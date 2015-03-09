package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import play.Play;
import play.mvc.*;

public class PDMySQL extends Controller {

	private static Connection connect = null;
	private static Statement statement = null;
	private static ResultSet resultSet = null;
	private static String host = Play.application().configuration()
			.getString("mysql.url");
	private static String username = Play.application().configuration()
			.getString("mysql.username");
	private static String password = Play.application().configuration()
			.getString("mysql.password");
	private static String database = Play.application().configuration()
			.getString("mysql.database");

	public static ResultSet readDataBase(String query) throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			if (connect == null) {
			connect = DriverManager.getConnection(host + "/" + database 
            		+ "?" + "user=" + username + "&password=" + password);	
			}
			// Statements allow to issue SQL queries to the database
			if (statement == null) {
			statement = connect.createStatement();
			}
			// Result set get the result of the SQL query
			resultSet = statement.executeQuery(query);

			return resultSet;
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} finally {
		}

	}

	// You need to close the resultSet
	public static void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}
}

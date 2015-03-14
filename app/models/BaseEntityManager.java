package models;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import play.Play;

public final class BaseEntityManager {

    private BaseEntityManager() {
    }
    private static DBI dbi;
    static Handle h = null;

    public static DBI getDbi() {
        return dbi;
    }

    public static void setDbi(DBI dbi) {
        BaseEntityManager.dbi = dbi;
    }

    public static void setDbi(String url, String username, String password, String driver) {
        try {
            Class.forName(driver);
            dbi = new DBI(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static Handle getDBIh() throws ClassNotFoundException { 
    	String url = Play.application().configuration().getString("db.default.url");
    	String dbUsername = Play.application().configuration().getString("db.default.user");
    	String dbPassword = Play.application().configuration().getString("db.default.password");
        dbi = new DBI(url, dbUsername, dbPassword);
        if ( h == null) {
        	h = dbi.open();
        }
        return h;
    }
}

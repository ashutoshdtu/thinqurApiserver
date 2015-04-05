package models;

/**
 * Created by ntenisOT on 16/10/14.
 */

import java.net.UnknownHostException;

import com.avaje.ebeaninternal.server.cluster.mcast.IncomingPacketsProcessed.GotAllPoint;
import com.mongodb.DB;
import com.mongodb.MongoClient;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import play.Logger;

public class MongoDBMorphia {

    private String host = null;
    private int port = 0;
    private String db = null;
    private String collection = null;
	public MongoClient mongo = null;
    public Morphia morphia = null;
    public Datastore datastore = null;
    
    /**
     * @throws UnknownHostException 
	 * 
	 */
	public MongoDBMorphia(String host, String port, String db) throws UnknownHostException {
		this.host = host;
		this.port = Integer.parseInt(port);
		this.db = collection;
		
		mongo = new MongoClient(this.host, this.port);
		Logger.info("Mongo databases list: " + mongo.getDatabaseNames());
		morphia = new Morphia();
		datastore = morphia.createDatastore(mongo, db);
	}

	/**
	 * 
	 */
	public void closeMongoConnection() {
		morphia = null;
		datastore = null;
		if(mongo!=null)
			mongo.close();
	}
}

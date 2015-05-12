/**
 * 
 */
package services;


import models.Question;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * @author ashutosh
 *
 */
public class QuestionDAO extends BasicDAO<Question, ObjectId> {

	String collectionName = null;
	
	/**
	 * @param datastore
	 */
	public QuestionDAO(Datastore datastore) {
		super(Question.class, datastore);
		
	}

	public Question get(String id) {
		return super.get(new ObjectId(id));
	}
	
	
}

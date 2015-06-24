/**
 * 
 */
package services;


import models.Comment;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * @author sanket
 *
 */
public class CommentDAO extends BasicDAO<Comment, ObjectId> {

	String collectionName = null;
	
	/**
	 * @param datastore
	 */
	public CommentDAO(Datastore datastore) {
		super(Comment.class, datastore);
		
	}

	public Comment get(String id) {
		return super.get(new ObjectId(id));
	}
	
	
}

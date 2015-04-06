/**
 * 
 */
package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;

/**
 * @author ashutosh
 *
 */
@Entity
public class Answer {
	
	@Id
	public ObjectId id = new ObjectId();	
	public String idString = id.toString();	
	public Date lastUpdatedAt = new Date();
	public Date createdAt = new Date();
	
	@Required
	@MaxLength(50)
	public String statement;
	
	@Embedded
	public List<UserRef> upvotedBy = new ArrayList<UserRef>();
	
	public Integer totalUpvotes;
	
	@PrePersist void prePersist() {lastUpdatedAt = new Date();}
	
}

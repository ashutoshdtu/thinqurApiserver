/**
 * this file specifies the class structure of Question
 */
package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import akka.japi.Option;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.query.Query;

import play.Play;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;


/** class to store Question
 * 
 * @author ashutosh
 * 
 */
@Entity
/*@Indexes({
	   @Index("user, -cs"),
	   @Index("changedRecord, -cs")})
	   */
public class Question {
	
	public enum Type {
		MCSC,        // Multiple choice single correct 
		MCMC         // Multiple choice multiple correct
	}
	
	@Id
	public ObjectId id = new ObjectId();
	
	public Date lastUpdatedAt = new Date();
	public Date createdAt = new Date();
	public boolean isAnonymous;
	public Type type = Type.MCSC;
	
	@Required
	@MaxLength(150)
	public String statement;
	
	@MaxLength(700)
	public String description;
	
	@Embedded
	public List<Answer> answers = new ArrayList<Answer>();
	
	@Embedded
	public List<Tag> tags = new ArrayList<Tag>();
	
	@Required
	@Embedded
	UserRef createdBy = new UserRef();
	
	@Embedded
	public List<UserRef> updatedBy = new ArrayList<UserRef>();
	
	@Embedded
	public List<UserRef> followedBy = new ArrayList<UserRef>();
	
	@Embedded
	public List<UserRef> upvotedBy = new ArrayList<UserRef>();
	
	@Embedded
	public List<UserRef> downvotedBy = new ArrayList<UserRef>();
	
	
	@PrePersist void prePersist() {lastUpdatedAt = new Date();}
}

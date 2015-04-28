/**
 * this file specifies the class structure of Question
 */
package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.NotSaved;
import org.mongodb.morphia.annotations.PrePersist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import utils.ObjectID_Serializer;



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
	
	@JsonIgnore
	@Id
	ObjectId _id = new ObjectId();
	
	String id = _id.toString();
	
	public Date lastUpdatedAt = new Date();
	public Date createdAt = new Date();
	public boolean isAnonymous = false;
	public Type type = Type.MCSC;
	
	@Required
	@MaxLength(150)
	public String statement;
	
	public String imageURL;
	
	//@MaxLength(700)
	//public String description;
	
	@Embedded
	public List<Answer> answers = new ArrayList<Answer>();
	
	@Embedded
	public List<Tag> tags = new ArrayList<Tag>();
	
	@Required
	@Embedded
	public UserRef createdBy = new UserRef();
	
	@Embedded
	public List<UserRef> updatedBy = new ArrayList<UserRef>();
	
	@Embedded
	public List<UserRef> followedBy = new ArrayList<UserRef>();
	
	@Embedded
	public List<UserRef> upvotedBy = new ArrayList<UserRef>();
	
	@Embedded
	public List<UserRef> downvotedBy = new ArrayList<UserRef>();
	
	
	@PrePersist void prePersist() {lastUpdatedAt = new Date();}
	
	public void set_id(ObjectId _id) {
		this._id = _id;
		this.id = _id.toString();
	}
	
	public void setId(String id) {
		this.id = id;
		this._id = new ObjectId(id);
	}
	
	public ObjectId get_id() {
		return _id;
	}
	
	public String getId() {
		return id;
	}
	
	/*
	//@JsonSerialize(using=ObjectID_Serializer.class) 
    public ObjectId getId() {
        if(id == null){
            return id = new ObjectId();
        }
        return id;
    }
    
    //@JsonSerialize(using=ObjectID_Serializer.class) 
    public void setId(ObjectId id) {
        this.id = id;
    }
    */
}

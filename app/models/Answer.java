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
import org.mongodb.morphia.annotations.NotSaved;
import org.mongodb.morphia.annotations.PrePersist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import utils.ObjectID_Serializer;

/**
 * @author ashutosh
 *
 */
@Entity
public class Answer {
	
	@JsonIgnore
	@Id
	ObjectId _id = new ObjectId();
	String id = _id.toString();
	
	public Date lastUpdatedAt = new Date();
	public Date createdAt = new Date();
	
	@Required
	@MaxLength(50)
	public String statement;
	
	public String imageURL;
	
	@Embedded
	public List<UserRef> upvotedBy = new ArrayList<UserRef>();
	
	public Integer totalUpvotes=0;
	
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
    }*/
}

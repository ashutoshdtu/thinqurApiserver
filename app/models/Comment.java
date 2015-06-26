package models;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author sanket
 *
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {
	
	@JsonIgnore
	@Id
	ObjectId _id = new ObjectId();
	String id = _id.toString();
	
	public String lastUpdatedAt = new DateTime( DateTimeZone.UTC ).toString();
	public String createdAt = new DateTime( DateTimeZone.UTC ).toString();
	
	@Required
	public String questionId = null;
	
	@Required
	@MaxLength(200)
	public String comment = null;
	
	@Required
	@Embedded
	public UserRef commentedBy = null;
		
	public boolean isActive = true;
	
	@PrePersist void prePersist() {lastUpdatedAt = new DateTime( DateTimeZone.UTC ).toString();}
	
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
}

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
import org.mongodb.morphia.annotations.PrePersist;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.data.validation.Constraints.Required;

/**
 * class to store User
 * 
 * @author sanket
 * 
 */
@Entity
public class User {

	@JsonIgnore
	@Id
	ObjectId _id = new ObjectId();

	String id = _id.toString();

	@Required
	public String displayName;
	@Required
	public String firstName;
	public String middleName = null;
	@Required
	public String lastName;
	public String profileImage = null;
	@Required
	public String email;
	public boolean isVerified = false;
	public boolean isActive = true;
	public String timezone = null;
	public Date createdAt = new Date();
	public Date lastUpdatedAt = new Date();

	@Required
	@Embedded
	public List<LinkedAccount> linkedAccounts = new ArrayList<LinkedAccount>();

	@PrePersist
	void prePersist() {
		lastUpdatedAt = new Date();
	}

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

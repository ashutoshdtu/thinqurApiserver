/**
 * this file specifies the class structure of Question
 */
package models;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;

import play.data.validation.Constraints.Required;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Class to store User
 * 
 * @author sanket
 * 
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
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
	public List<String> emailList = new ArrayList<String>();
	public boolean isVerified = false;
	public boolean isActive = true;
	public String timezone = null;
	public int userRole = 10; //default - 10 for normal user, max. - 100 for admin, min. - 0 for blacklisted user 
	public String lastUpdatedAt = new DateTime(DateTimeZone.UTC).toString();
	public String createdAt = new DateTime(DateTimeZone.UTC).toString();

	@Required
	@Embedded
	public List<LinkedAccount> linkedAccounts = new ArrayList<LinkedAccount>();

	@PrePersist
	void prePersist() {
		lastUpdatedAt = new DateTime(DateTimeZone.UTC).toString();
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

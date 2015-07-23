/**
 * 
 */
package models;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.NotSaved;
import org.mongodb.morphia.annotations.PrePersist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import services.UserDAO;
import utils.DAOUtils;
import utils.commonUtils;

/** Reference to user objects. Contains only very important details of user.
 * @author ashutosh
 *
 */

@Embedded
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRef {
	@JsonIgnore
	@NotSaved
	static UserDAO userDAO = DAOUtils.userDAO;
	
	@Required
	String id = null;
	
	@MaxLength(100)
	String name=null;
	
	@MaxLength(50)
	String displayName=null;
	
	
	String profileImage;
	public String lastUpdatedAt = new DateTime(DateTimeZone.UTC).toString();
	public String createdAt = new DateTime(DateTimeZone.UTC).toString();

	@PrePersist
	void prePersist() {
		lastUpdatedAt = new DateTime(DateTimeZone.UTC).toString();
	}
	
	public void setId(String id) throws Exception {
		this.id = id;
		if(id!=null && !id.isEmpty() && commonUtils.isValidUUID(id)) {
			User user = userDAO.get(new ObjectId(id));
			this.name = user.firstName + " " + user.lastName;
			this.displayName = user.displayName;
			this.profileImage = user.profileImage;
		}
	}
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public UserRef(String id) throws Exception {
		super();
		setId(id);
	}

	public UserRef() {
		super();
	}
	
}

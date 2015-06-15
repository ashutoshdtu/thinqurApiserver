/**
 * 
 */
package models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.NotSaved;

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
	
	@Email
	String email=null;
	
	String profileImage;
	
	public void setId(String id) throws Exception {
		this.id = id;
		if(id!=null && !id.isEmpty() && commonUtils.isValidUUID(id)) {
			User user = userDAO.get(new ObjectId(id));
			this.name = user.firstName + " " + user.lastName;
			this.displayName = user.displayName;
			this.email = user.email;
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

	public String getEmail() {
		return email;
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

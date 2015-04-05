/**
 * 
 */
package models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;

/**
 * @author ashutosh
 *
 */

@Entity
public class UserRef {
	@Required
	public ObjectId id = new ObjectId();
	
	@Required
	@MaxLength(50)
	String name;
	
	@MaxLength(50)
	String screenName;
	
	@Email
	String email;
}

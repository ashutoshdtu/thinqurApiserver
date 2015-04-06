/**
 * 
 */
package models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;

/**
 * @author ashutosh
 *
 */

@Embedded
public class UserRef {
	@Required
	public ObjectId id = new ObjectId();
	
	public String idString = id.toString();
	
	@Required
	@MaxLength(50)
	String name;
	
	@MaxLength(50)
	String screenName;
	
	@Email
	String email;
}

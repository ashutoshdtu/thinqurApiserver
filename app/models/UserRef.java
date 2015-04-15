/**
 * 
 */
package models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

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
	public String id = null;
	
	@Required
	@MaxLength(100)
	public String name=null;
	
	@MaxLength(50)
	public String screenName=null;
	
	@Required
	@Email
	public String email=null;
}

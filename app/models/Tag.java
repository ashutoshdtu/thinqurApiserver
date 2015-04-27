/**
 * 
 */
package models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;

/**
 * @author ashutosh
 *
 */

@Embedded
public class Tag {
	//@Required
	public String id=null;
	
	@Required
	@MaxLength(100)
	@Indexed
	public String name=null;
	
	public String imageURL;
	
	public boolean isActive=false;

}

/**
 * 
 */
package models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;

/**
 * @author ashutosh
 *
 */

@Entity
public class Tag {
	@Required
	@Id
	public String id;
	
	@Required
	@MaxLength(100)
	String name;
	
	boolean isActive;

}

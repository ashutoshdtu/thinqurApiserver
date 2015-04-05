/**
 * 
 */
package models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import play.data.validation.Constraints.MaxLength;

/**
 * @author ashutosh
 *
 */

@Entity
public class Tag {
	
	public ObjectId id;
	
	@MaxLength(200)
	String name;
	
	boolean isActive;

}

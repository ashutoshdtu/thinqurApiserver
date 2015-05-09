/**
 * 
 */
package models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import utils.ObjectID_Serializer;

/** Reference to user objects. Contains only very important details of user.
 * @author ashutosh
 *
 */

@Embedded
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRef {
	@Required
	public String id = null;
	
	@Required
	@MaxLength(100)
	public String name=null;
	
	@MaxLength(50)
	public String screenName=null;
	
	@Email
	public String email=null;
	
	public String imageURL;
	
	/*
	//@JsonSerialize(using=ObjectID_Serializer.class) 
    public ObjectId getId() {
        if(id == null){
            return id = new ObjectId();
        }
        return id;
    }
    
    //@JsonSerialize(using=ObjectID_Serializer.class) 
    public void setId(ObjectId id) {
        this.id = id;
    }*/
}

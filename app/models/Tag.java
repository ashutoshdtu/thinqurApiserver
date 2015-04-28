/**
 * 
 */
package models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import utils.ObjectID_Serializer;

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
    }
    */

}

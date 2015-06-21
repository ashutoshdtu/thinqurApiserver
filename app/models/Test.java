/**
 * this file specifies the class structure of Question
 */
package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bson.types.ObjectId;

import play.data.validation.Constraints.Required;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * class to store User
 * 
 * @author sanket
 * 
 */
@Entity
@Table(name="User")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Test {

	@JsonIgnore
	@Id
	ObjectId _id = new ObjectId();

	String id = _id.toString();

	@Required
	public String name;

	public void set_id(ObjectId _id) {
		this._id = _id;
		this.id = _id.toString();
	}

	public void setId(String id) {
		this.id = id;
		this._id = new ObjectId(id);
	}

	public ObjectId get_id() {
		return _id;
	}

	public String getId() {
		return id;
	}
}

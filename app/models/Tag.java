/**
 * 
 */
package models;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;

/**
 * @author ashutosh
 *
 */

@Embedded
@JsonIgnoreProperties(ignoreUnknown = true)
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

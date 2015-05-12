/**
 * 
 */
package models;

/** Class to store response format for metadata of a post method on users collection
 * @author ashutosh
 *
 */
public class MetadataPostUser extends Metadata {
	public boolean newRecord = true;
	
	public void setNewRecord(boolean isNew) {
		this.newRecord = isNew; 
	}
	
	/**
	 * 
	 */
	public MetadataPostUser() {
		super();
	}
}

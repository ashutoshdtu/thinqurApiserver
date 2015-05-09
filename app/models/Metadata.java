/**
 * 
 */
package models;


/**Interface to store response format for metadata of a route
 * @author ashutosh
 *
 */
public class Metadata {
	public long QTime = 0;
	public boolean newRecord = true;
	
	public void setQTime(long QTime) {
		this.QTime = QTime; 
	}
	
	public void setNewRecord(boolean isNew) {
		this.newRecord = isNew; 
	}

}

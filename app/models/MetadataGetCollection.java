/**
 * 
 */
package models;

/** Class to store response format for metadata of a get method on a collection
 * @author ashutosh
 *
 */
public class MetadataGetCollection extends Metadata{
	public Integer numFound = 0;
	
	public void setNumFound(Integer num) {
		numFound = num;
	}
	
	/**
	 * 
	 */
	public MetadataGetCollection() {
		super();
	}
}

/**
 * 
 */
package models;



/**
 * @author ashutosh
 *
 */
public class HTTPResponse<Response, Metadata, DebugInfo> {
	/**
	 * @param status
	 * @param metadata
	 * @param response
	 * @param debugInfo
	 */
	public HTTPResponse(HTTPStatus status, Metadata metadata,
			Response response, DebugInfo debugInfo) {
		this.status = status;
		this.metadata = metadata;
		this.response = response;
		this.debugInfo = debugInfo;
	}
	
	public HTTPStatus status;
	public Metadata metadata;
	public Response response;
	public DebugInfo debugInfo;
}

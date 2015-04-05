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
	 * @param response
	 * @param metaData
	 * @param debugInfo
	 */
	public HTTPResponse(HTTPStatus status, Response response,
			Metadata metadata, DebugInfo debugInfo) {
		this.status = status;
		this.response = response;
		this.metadata = metadata;
		this.debugInfo = debugInfo;
	}
	
	public HTTPStatus status;
	public Response response;
	public Metadata metadata;
	public DebugInfo debugInfo;
}

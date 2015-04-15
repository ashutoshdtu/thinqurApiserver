/**
 * 
 */
package models;


/**
 * This class provides structure of status message returned for any api call
 * @author ashutosh
 *
 */
public class HTTPStatus {
	
	/**
	 * <p>HTTP code value</p>
	 * <p>eg. {@code 400} for {@code "Bad Request"}
	 */
	public int code;
	
	/**
	 * Message displayed to user. Does not contain technical info
	 */
	public String message;
	
	/**
	 * Description of HTTP code
	 */
	public String description;
	
	/**
	 * Message for developer. Contains more technical info and hence NOT exposed to user
	 */
	public String developerMessage;
	
	
	/**
	 * 
	 */
	public HTTPStatus() {
		setCode(HTTPStatusCode.OK);
		description = null;
	}
	
	
	/**
	 * <p>Sets the HTTP code using HTTPStatusCode enum.</p>
	 * <p>
	 * <pre>
	 * usage: 
	 * 
	 *     {@code HTTPStatus status = new HTTPStatus();}
	 *     {@code status.setCode(HTTPStatusCode.INTERNAL_SERVER_ERROR);}
	 * </pre>
	 * </p>
	 * @param httpCode
	 */
	public void setCode(HTTPStatusCode httpCode) {
		code = httpCode.getCode();
		message = httpCode.getMessage();
		description = httpCode.getDescription();
	}
	
	/**
	 * <p>Sets developer message</p>
	 * <p>Developer message contains technical info and hence is not exposed to user</p>
	 * @param message
	 */
	public void setDeveloperMessage(String message) {
		developerMessage = message;
	}

}

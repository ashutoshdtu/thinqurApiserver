/**
 * 
 */
package models;

/**
 * <p>
 * This enum provides easy access to a number of frequently used http status codes.
 * </p> 
 * 
 * <p>
 * eg. the http response code for Internal server error can be accessed as:
 * <br>
 * <pre>
 *     {@code int code = HTTPStatusCode.INTERNAL_SERVER_ERROR.getCode();}
 * </pre>
 * here {@code code = 500}
 * </p> 
 * 
 * <p>
 * Similarly http code message and description for the http code can also be accessed as:
 * <br>
 * <pre>
 *     {@code String message=HTTPStatusCode.INTERNAL_SERVER_ERROR.getMessage();}
 *     {@code String description=HTTPStatusCode.INTERNAL_SERVER_ERROR.getDescription();}
 * </pre>
 * here {@code message = "Internal Server Error"} and {@code description = "Some exception occured"}
 * </p>
 * @author ashutosh
 *
 */
public enum HTTPStatusCode {
	/*
	200 – OK – Eyerything is working
	201 – OK – New resource has been created
	204	– OK – The resource was successfully deleted

	304 – Not Modified – The client can use cached data

	400 – Bad Request – The request was invalid or cannot be served. The exact error should be explained in the error payload. E.g. „The JSON is not valid“
	401 – Unauthorized – The request requires an user authentication
	403 – Forbidden – The server understood the request, but is refusing it or the access is not allowed.
	404 – Not found – There is no resource behind the URI.
	405 - Method Not Allowed - When an HTTP method is being requested that isn't allowed for the authenticated user
	410 - Gone - Indicates that the resource at this end point is no longer available. Useful as a blanket response for old API versions
	415 - Unsupported Media Type - If incorrect content type was provided as part of the request
	422 – Unprocessable Entity – Should be used if the server cannot process the enitity, e.g. if an image cannot be formatted or mandatory fields are missing in the payload.
	429 - Too Many Requests - When a request is rejected due to rate limiting
	500 – Internal Server Error – API developers should avoid this error. If an error occurs in the global catch blog, the stracktrace should be logged and not returned as response.
	 */
	OK 	 					(200, "OK", "Eyerything is working"),
	RESOURCE_CREATED 		(201, "Created", "New resource has been created"),
	RESOURCE_DELETED 		(204, "No Content", "The resource was successfully deleted"),
	NOT_MODIFIED 			(304, "Not Modified", "The client can use cached data"),
	BAD_REQUEST 			(400, "Bad Request", "The request was invalid or cannot be served"),
	UNAUTHORIZED 			(401, "Unauthorized", "No or invalid authentication details are provided"),
	FORBIDDEN 				(403, "Forbidden", "Authentication succeeded but authenticated user doesn't have access to the resource"),
	NOT_FOUND 				(404, "Not found", "There is no resource behind the URI."),
	METHOD_NOT_ALLOWED 		(405, "Method Not Allowed", "Requested HTTP method is not allowed for the authenticated user"),
	GONE 					(410, "Gone", "Resource at this end point is no longer available"),
	UNSUPPORTED_MEDIA_TYPE 	(415, "Unsupported Media Type", "Incorrect content type was provided as part of the request"),
	UNPROCESSABLE_ENTITY 	(422, "Unprocessable Entity", "Server cannot process the enitity"),
	TOO_MANY_REQUESTS 		(429, "Too Many Requests", "When a request is rejected due to rate limiting"),
	INTERNAL_SERVER_ERROR 	(500, "Internal Server Error", "Some exception occured");
	
	
	int code;
	String message;
	String description;
	
	HTTPStatusCode(int code, String message, String description) {
		this.code = code;
		this.message = message;
		this.description = description;
	}
	
	/**
	 * @return http code value
	 * <br>eg. {@code 400} for {@code BAD_REQUEST}
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * @return http code message
	 * <br> eg. {@code "Bad Request"} for {@code BAD_REQUEST} 
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * @return http code value
	 * <br>eg. {@code "The request was invalid or cannot be served"} for {@code BAD_REQUEST}
	 */
	public String getDescription() {
		return description;
	}
}

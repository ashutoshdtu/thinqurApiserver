package models.restapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentGetForm {
	
	public String method = "generic";
	
	public String q = "{isActive:true}";
	
	public Integer start = 0;
	
	public Integer rows = 10;
	
	public String sort = "{createdAt:-1}";
	
}

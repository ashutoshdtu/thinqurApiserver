package models.restapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionGetForm extends APIForm{
	
	public String method = "generic";
	
	public String q = "{isAnonymous:false}";
	
	public Integer start = 0;
	
	public Integer rows = 10;
	
	public String sort = "{createdAt:-1}";
	
}

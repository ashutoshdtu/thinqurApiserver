package models;

import org.mongodb.morphia.annotations.Embedded;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import play.data.validation.Constraints.Required;

@Embedded
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkedAccount {
	@Required
	public String providerUserId;
	@Required
	public String providerKey;
	
	public String accessToken = null;

}
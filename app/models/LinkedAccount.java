package models;

import org.mongodb.morphia.annotations.Embedded;

import play.data.validation.Constraints.Required;

@Embedded
public class LinkedAccount {
	@Required
	public String providerUserId;
	@Required
	public String providerKey;

}
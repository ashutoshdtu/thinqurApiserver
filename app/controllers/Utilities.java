package controllers;

import java.util.UUID;

import play.Logger;

public class Utilities {
	public static String generateUUID() {
		String qid;
		try{
			qid = UUID.randomUUID().toString().replaceAll("-", "");;
		} catch(Exception e) {
			Logger.error(e.toString());
			return null;
		}
		return qid;
	}
}

package controllers;

import play.mvc.Controller;
import play.mvc.Result;

@play.db.jpa.Transactional
public class Application extends Controller {

	public static Result index() {
		return ok("Babaji ka Thullu is ready.");
	}

	public static Result output() {
		return ok("good");
	}

}

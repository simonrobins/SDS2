package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class Time extends Controller
{
	public static Result index()
	{
		Long currentTime = new Long(System.currentTimeMillis());

		return ok(currentTime.toString());
	}
}
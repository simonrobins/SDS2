package controllers;

import java.io.File;

import misc.Utilities;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller
{
	public static Result index()
	{
		return ok(views.html.index.render(getInfo()));
	}

	public static Result logout()
	{
		session().clear();
		flash("success", "You've been logged out");
		return redirect(routes.Application.index());
	}

	public static Result unauthorised()
	{
		session().clear();
		return unauthorized(views.html.unauthorised.render());
	}

	public static Result expired()
	{
		session().clear();
		return unauthorized(views.html.expired.render());
	}

	private static String getInfo()
	{
		return Utilities.getInfo(new File("nginx/html"));
	}
}
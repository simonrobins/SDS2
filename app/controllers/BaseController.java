package controllers;

import play.mvc.Controller;
import play.mvc.Http.Session;

public class BaseController extends Controller
{
	public static Session session()
	{
		return Controller.session();
	}
}

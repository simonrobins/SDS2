package controllers;

import finders.AccountContactFinder;
import helpers.SessionHelper;
import misc.Settings;
import models.AccountContact;
import play.mvc.Controller;
import play.mvc.Result;
import security.AutoLogin;

public class Secure extends Controller
{
	public static Result fullAccess(final String id, final String expires, final String md5)
	{
		return restrictedAccess(id, expires, SessionHelper.FULL_ACCESSS, md5);
	}

	public static Result restrictedAccess(final String id, final String expires, final String access, final String md5)
	{
		final boolean valid = AutoLogin.getInstance().validateMd5(Settings.SECURE_PASSWORD, id, expires, access, md5);

		if(valid)
		{
			final AccountContact contact = AccountContactFinder.find(Integer.parseInt(id));

			try
			{
				session().clear();
				SessionHelper.setContact(session(), contact);
				SessionHelper.setAccess(session(), access);

				return redirect(routes.Application.index());
			}
			catch(final Exception ex)
			{
				session().clear();
				ex.printStackTrace();
			}
		}

		return redirect(routes.Application.expired());
	}
}

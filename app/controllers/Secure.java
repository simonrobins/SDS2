package controllers;

import finders.AccountContactFinder;
import helpers.SessionHelper;
import misc.Settings;
import models.AccountContact;
import play.Logger;
import play.mvc.Result;
import security.AutoLogin;

public class Secure extends BaseController
{
	public static Result fullAccess(final String id, final String expires, final String md5)
	{
		return restrictedAccess(id, expires, SessionHelper.FULL_ACCESSS, md5);
	}

	public static Result restrictedAccess(final String id, final String expires, final String access, final String md5)
	{
		final boolean valid = AutoLogin.validateMd5(Settings.SECURE_PASSWORD, id, expires, access, md5);

		if (valid)
		{
			final AccountContact contact = AccountContactFinder.find(Integer.parseInt(id));

			try
			{
				SessionHelper.setContact(session(), contact, Settings.APPLICATION_SECRET);
				SessionHelper.setAccess(session(), access);

				return redirect(routes.Application.index());
			}
			catch (final Exception ex)
			{
				session().clear();
				Logger.error("Contact " + id + " not found", ex);
			}
		}

		return redirect(routes.Application.expired());
	}
}

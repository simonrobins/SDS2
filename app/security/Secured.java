package security;

import helpers.SessionHelper;

import org.eclipse.jdt.annotation.Nullable;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class Secured extends Security.Authenticator
{
	@Override
	public @Nullable String getUsername(final @Nullable Context ctx)
	{
		if(ctx != null)
			return SessionHelper.getAccountIdAsString(ctx.session());
		else
			return null;
	}

	@Override
	public @Nullable Result onUnauthorized(final @Nullable Context ctx)
	{
		if(ctx != null)
			ctx.session().clear();
		return unauthorized(views.html.unauthorised.render());
	}
}

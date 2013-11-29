import static play.mvc.Results.internalServerError;

import java.lang.reflect.Method;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import play.Play;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;

@NonNullByDefault
public class Global extends play.GlobalSettings
{
	@Override
	public @Nullable Result onError(@Nullable RequestHeader request, @Nullable Throwable t)
	{
		if(Play.isDev())
		{
			return super.onError(request, t);
		}
		else
		{
			Object id = getId(t);
			return internalServerError(views.html.error.render(id));
		}
	}

	// I know the id is there, there just isn't another way to get to it.
	private @Nullable Object getId(@Nullable Throwable t)
	{
		try
		{
			if(t == null)
				throw new NullPointerException("");
			Method getId = t.getClass().getMethod("id");
			return getId.invoke(t);
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			return null;
		}
	}
}

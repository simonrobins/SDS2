package misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.Nullable;

import play.api.http.MediaRange;
import play.i18n.Lang;
import play.mvc.Http.Cookies;
import play.mvc.Http.Request;
import play.mvc.Http.RequestBody;

public class FakeRequest extends Request
{
	private Map<String, String[]> headers;

	@Override
	public RequestBody body()
	{
		// TODO Auto-generated method stub
		return new RequestBody();
	}

	@Override
	@Deprecated
	public List<String> accept()
	{
		// TODO Auto-generated method stub
		return new ArrayList<String>();
	}

	@Override
	public List<Lang> acceptLanguages()
	{
		// TODO Auto-generated method stub
		return new ArrayList<Lang>();
	}

	@Override
	public List<MediaRange> acceptedTypes()
	{
		// TODO Auto-generated method stub
		return new ArrayList<MediaRange>();
	}

	@Override
	public boolean accepts(@Nullable String arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cookies cookies()
	{
		// TODO Auto-generated method stub
		return new Cookies()
			{
				@Override
				public play.mvc.Http.Cookie get(@Nullable String arg0)
				{
					// TODO Auto-generated method stub
					return new play.mvc.Http.Cookie(null, null, null, null, null, false, false);
				}
			};
	}

	@Override
	public Map<String, String[]> headers()
	{
		// TODO Auto-generated method stub
		return headers;
	}

	@Override
	public String host()
	{
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String method()
	{
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String path()
	{
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public Map<String, String[]> queryString()
	{
		// TODO Auto-generated method stub
		return new HashMap<String, String[]>();
	}

	@Override
	public String remoteAddress()
	{
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String uri()
	{
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String version()
	{
		// TODO Auto-generated method stub
		return "";
	}

	public void setHeaders(Map<String, String[]> headers)
	{
		this.headers = headers;
	}
}

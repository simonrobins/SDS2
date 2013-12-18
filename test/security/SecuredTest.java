package security;

import static play.mvc.Http.Status.UNAUTHORIZED;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;
import static play.test.Helpers.status;
import static play.test.Helpers.stop;

import java.util.HashMap;

import org.fest.assertions.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import play.mvc.Result;
import play.test.FakeApplication;
import controllers.AbstractTest;

public class SecuredTest
{
	protected static FakeApplication	app;

	@BeforeClass
	public static void startApp()
	{
		app = AbstractTest.startApp(new HashMap<String, String>());
	}

	@AfterClass
	public static void stopApp()
	{
		stop(app);
	}

	@Test
	public void testOnUnauthorizedContext()
	{
		final Result result = route(fakeRequest("GET", "/support"));
		Assertions.assertThat(status(result)).isEqualTo(UNAUTHORIZED);
	}

	@Test
	public void testGetusernameWithNullContext()
	{
		Secured s = new Secured();
		Assertions.assertThat(s.getUsername(null)).isNull();
	}

	@Test
	public void testOnUnauthorizedWithNullContext()
	{
		Secured s = new Secured();
		Assertions.assertThat(s.onUnauthorized(null)).isNotNull();
	}
}

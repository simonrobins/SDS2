package security;

import static play.mvc.Http.Status.UNAUTHORIZED;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;
import static play.test.Helpers.start;
import static play.test.Helpers.status;
import static play.test.Helpers.stop;

import java.util.HashMap;
import java.util.Map;

import org.fest.assertions.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import play.mvc.Result;
import play.test.FakeApplication;

public class SecuredTest
{
	protected static FakeApplication app;

	@BeforeClass
	public static void startApp()
	{
		Map<String, String> map = new HashMap<String, String>();

		app = fakeApplication(map);
		start(app);
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
}

package controllers;

import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.mvc.Http.Status.UNAUTHORIZED;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;
import static play.test.Helpers.status;

import org.fest.assertions.Assertions;
import org.junit.Test;

import play.mvc.Result;

public class ApplicationTest extends AbstractTest
{
	@Test
	public void testReadonly()
	{
		final Result result = callAction(controllers.routes.ref.Secure.restrictedAccess("41724", "1678382192", "0", "3f5f5c105462d19791978cb9a4bd6782"));
		Assertions.assertThat(status(result)).isEqualTo(SEE_OTHER);
	}

	@Test
	public void testInvalid()
	{
		deleteCompanyDownload();
		final Result result = callAction(controllers.routes.ref.Secure.restrictedAccess("41724", "1678382193", "3", "3f5f5c105462d19791978cb9a4bd6782"));
		Assertions.assertThat(status(result)).isEqualTo(SEE_OTHER);
		Assertions.assertThat(countCompanyDownload()).isEqualTo(0L);
	}

	@Test
	public void testIndex()
	{
		final Result result = route(fakeRequest("GET", "/"));
		Assertions.assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testLogout()
	{
		final Result result = route(fakeRequest("GET", "/logout"));
		Assertions.assertThat(status(result)).isEqualTo(SEE_OTHER);
	}

	@Test
	public void testExpired()
	{
		final Result result = route(fakeRequest("GET", "/expired"));
		Assertions.assertThat(status(result)).isEqualTo(UNAUTHORIZED);
	}

	@Test
	public void testUnauthorised()
	{
		final Result result = route(fakeRequest("GET", "/unauthorised"));
		Assertions.assertThat(status(result)).isEqualTo(UNAUTHORIZED);
	}
}
package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;
import static play.test.Helpers.status;

import org.junit.Test;

import play.mvc.Result;

public class ValidateTest extends AbstractTest
{
	@Test
	public void testIndex()
	{
		final Result result = route(fakeRequest("GET", "/validate"));
		assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testMissingProducts()
	{
		final Result result = route(fakeRequest("GET", "/validate/missing_products"));
		assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testUnmappedProducts()
	{
		final Result result = route(fakeRequest("GET", "/validate/unmapped_products"));
		assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testMissingLanguages()
	{
		final Result result = route(fakeRequest("GET", "/validate/missing_languages"));
		assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testUnmappedLanguages()
	{
		Validate.findUnmapped(false, 5);
//		final Result result = route(fakeRequest("GET", "/validate/unmapped_languages"));
//		assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testReleases()
	{
		final Result result = route(fakeRequest("GET", "/validate/releases"));
		assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testLanguages()
	{
		final Result result = route(fakeRequest("GET", "/validate/languages"));
		assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testServicepacks()
	{
		final Result result = route(fakeRequest("GET", "/validate/servicepacks"));
		assertThat(status(result)).isEqualTo(OK);
	}
}

package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.status;

import org.junit.Test;

import play.mvc.Result;

public class SupportAreaTest extends AbstractTest
{
	@Test
	public void testIndex()
	{
		final Result result = get("/support");
		assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testDownload1()
	{
		final Result result = get("/support/one");
		assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testMissingDownload1()
	{
		final Result result = get("/support/missing");
		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	@Test
	public void testDownload2()
	{
		final Result result = get("/support/one/two.txt");
		assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testMissingDownload2()
	{
		final Result result = get("/support/one/missing.txt");
		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	@Test
	public void testInternal1()
	{
		final Result result = get("/support/internal/id/filename");
		assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testInternal2()
	{
		final Result result = get("/support/internal/id/directory/filename");
		assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testUpload()
	{
		final Result result = post("/support/upload", "");
		assertThat(status(result)).isEqualTo(OK);
	}
}

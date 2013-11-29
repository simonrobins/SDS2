package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.header;
import static play.test.Helpers.status;

import org.junit.Test;

import play.mvc.Result;

;

public class DocumentsTest extends AbstractTest
{
	@Test
	public void testIndex()
	{
		Result result = get("/documents");
		assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testDownload3()
	{
		Result result = get("/documents/one/two/three.txt");
		assertThat(status(result)).isEqualTo(OK);
		assertThat(header("X-Accel-Redirect", result)).isEqualTo("/documents/internal/one/two/three.txt");
		assertThat(header("Content-Disposition", result)).isEqualTo("attachment; filename=three.txt");
	}

	@Test
	public void testMissingDownload3()
	{
		Result result = get("/documents/one/two/missing.txt");
		assertThat(status(result)).isEqualTo(NOT_FOUND);
		assertThat(contentAsString(result)).isEqualTo("missing.txt");
	}

	@Test
	public void testDownload4()
	{
		Result result = get("/documents/one/two/three/four.txt");
		assertThat(status(result)).isEqualTo(OK);
		assertThat(header("X-Accel-Redirect", result)).isEqualTo("/documents/internal/one/two/three/four.txt");
		assertThat(header("Content-Disposition", result)).isEqualTo("attachment; filename=four.txt");
	}

	@Test
	public void testMissingDownload4()
	{
		Result result = get("/documents/one/two/three/missing.txt");
		assertThat(status(result)).isEqualTo(NOT_FOUND);
		assertThat(contentAsString(result)).isEqualTo("missing.txt");
	}

	@Test
	public void testInternal3()
	{
		Result result = Documents.internal3("one", "two", "three.txt");
		assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testInternal4()
	{
		Result result = Documents.internal4("one", "two", "three", "four.txt");
		assertThat(status(result)).isEqualTo(OK);
	}
}

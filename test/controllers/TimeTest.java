package controllers;

import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.status;

import org.fest.assertions.Assertions;
import org.junit.Test;

import play.mvc.Result;

public class TimeTest extends AbstractTest
{
	@Test
	public void testIndex()
	{
		long current = System.currentTimeMillis();
		Result result = get("/time");
		Assertions.assertThat(status(result)).isEqualTo(OK);
		long time = Long.parseLong(contentAsString(result));
		Assertions.assertThat(time).isGreaterThanOrEqualTo(current);
		Assertions.assertThat(time).isLessThanOrEqualTo(current + 1000);
	}
}

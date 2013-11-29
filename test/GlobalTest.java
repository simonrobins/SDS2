import static play.mvc.Http.Status.INTERNAL_SERVER_ERROR;
import static play.test.Helpers.status;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.fest.assertions.Assertions;
import org.junit.Test;

import play.mvc.Result;
import controllers.AbstractTest;

@NonNullByDefault
public class GlobalTest extends AbstractTest
{
	@SuppressWarnings("serial")
	private class TestException extends Exception
	{
		@SuppressWarnings("unused")
		public Long id()
		{
			return 99L;
		}
	}

	@Test
	public void testOnErrorRequestHeaderThrowable()
	{
		Global g = new Global();
		Result result = g.onError(null, new TestException());
		Assertions.assertThat(status(result)).isEqualTo(INTERNAL_SERVER_ERROR);
	}

	@Test
	public void testOnErrorRequestHeaderThrowableWithNoId()
	{
		Global g = new Global();
		Result result = g.onError(null, new Exception());
		Assertions.assertThat(status(result)).isEqualTo(INTERNAL_SERVER_ERROR);
	}
}

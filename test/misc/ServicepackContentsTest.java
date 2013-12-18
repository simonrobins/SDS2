package misc;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import org.fest.assertions.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import play.test.FakeApplication;

public class ServicepackContentsTest
{
	private static FakeApplication	app;

	@BeforeClass
	public static void beforeClass()
	{
		app = fakeApplication();
		start(app);
	}

	@AfterClass
	public static void afterClass()
	{
		stop(app);
	}

	@Test
	public void testNewSettings()
	{
		Assertions.assertThat(new ServicepackContents()).isNotNull();
	}
}

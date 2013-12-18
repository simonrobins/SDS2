package misc;

import static play.test.Helpers.stop;

import java.util.HashMap;

import org.fest.assertions.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import play.test.FakeApplication;
import controllers.AbstractTest;

public class VersionsTest
{
	private static FakeApplication	app;

	@BeforeClass
	public static void beforeClass()
	{
		app = AbstractTest.startApp(new HashMap<String, String>());
		// Just to force 100% coverage
		new Versions();
	}

	@AfterClass
	public static void afterClass()
	{
		stop(app);
	}

	@Test
	public void testGetValidVersion112()
	{
		Assertions.assertThat(Versions.get("11.200")).isNotNull();
		Assertions.assertThat(Versions.get("11.200")).isEqualTo(261);
	}

	@Test
	public void testGetValidVersion113()
	{
		Assertions.assertThat(Versions.get("11.300")).isNotNull();
		Assertions.assertThat(Versions.get("11.300")).isEqualTo(281);
	}

	@Test
	public void testGetValidVersion120()
	{
		Assertions.assertThat(Versions.get("12.000")).isNotNull();
		Assertions.assertThat(Versions.get("12.000")).isEqualTo(325);
	}

	@Test
	public void testGetInvalidversion()
	{
		Assertions.assertThat(Versions.get("11.000")).isNull();
	}

	@Test
	public void testContainsKey()
	{
		Assertions.assertThat(Versions.containsKey("12.000")).isNotNull();
		Assertions.assertThat(Versions.get("12.000")).isEqualTo(325);
	}
}

package data;

import static play.test.Helpers.stop;

import java.util.HashMap;
import java.util.Map;

import org.fest.assertions.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import play.test.FakeApplication;
import controllers.AbstractTest;

public class DocumentMapTest
{
	protected static FakeApplication	app;

	@BeforeClass
	public static void startApp()
	{
		Map<String, String> configuration = new HashMap<String, String>();
		configuration.put("sds.download.dir", "\\SDS\\Missing\\");

		app = AbstractTest.startApp(configuration);
	}

	@AfterClass
	public static void stopApp()
	{
		stop(app);
	}

	@Test
	public void testGetDocumentMap()
	{
		new DocumentMap();
		DocumentNode node = DocumentMap.getDocumentMap();
		Assertions.assertThat(node != null);
	}

	@Test
	public void testGetSupportMap()
	{
		DocumentNode node = DocumentMap.getSupportMap("999999");
		Assertions.assertThat(node != null);
	}

	@Test
	public void testGetSupportMapNoAccount()
	{
		DocumentNode node = DocumentMap.getSupportMap("888888");
		Assertions.assertThat(node != null);
	}
}

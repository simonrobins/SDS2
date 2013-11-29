package data;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import java.util.HashMap;
import java.util.Map;

import org.fest.assertions.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import play.test.FakeApplication;

public class DocumentMapTest
{
	protected static FakeApplication app;

	@BeforeClass
	public static void startApp()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("sds.download.dir", "\\SDS\\Missing\\");

		app = fakeApplication(map);
		start(app);
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

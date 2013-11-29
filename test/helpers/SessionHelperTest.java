package helpers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.mvc.Http;
import controllers.AbstractTest;

public class SessionHelperTest extends AbstractTest
{
	private static Http.Session session;

	@BeforeClass
	public static void startApp2()
	{
		Map<String, String> map = new HashMap<String, String>();
		session = new Http.Session(map);
	}

	@Before
	public void setUp()
	{
		session.clear();
	}

	@Test
	public void testHasReadOnlyAccess()
	{
		assertTrue(SessionHelper.INSTANCE.hasReadOnlyAccess(session));

		setAccess("");
		assertTrue(SessionHelper.INSTANCE.hasReadOnlyAccess(session));

		setAccess("0");
		assertTrue(SessionHelper.INSTANCE.hasReadOnlyAccess(session));
	}

	@Test
	public void testHasUpdateAccess()
	{
		assertFalse(SessionHelper.INSTANCE.hasUpdateAccess(session));

		setAccess("");
		assertFalse(SessionHelper.INSTANCE.hasUpdateAccess(session));

		setAccess("1");
		assertTrue(SessionHelper.INSTANCE.hasUpdateAccess(session));

		setAccess("3");
		assertTrue(SessionHelper.INSTANCE.hasUpdateAccess(session));
	}

	@Test
	public void testHasDownloadAccess()
	{
		assertFalse(SessionHelper.INSTANCE.hasDownloadAccess(session));

		setAccess("");
		assertFalse(SessionHelper.INSTANCE.hasDownloadAccess(session));

		setAccess("2");
		assertTrue(SessionHelper.INSTANCE.hasDownloadAccess(session));

		setAccess("3");
		assertTrue(SessionHelper.INSTANCE.hasDownloadAccess(session));
	}

	private void setAccess(String access)
	{
		try
		{
			SessionHelper.INSTANCE.setAccess(session, access);
		}
		catch(Exception e)
		{
			fail();
		}
	}
}

package helpers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.mvc.Http;
import controllers.AbstractTest;

public class SessionHelperTest extends AbstractTest
{
	private static Http.Session	session;

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
	public void testExceptionOnNullAccess()
	{
		setAccess(null);
		assertTrue(SessionHelper.hasReadOnlyAccess(session));
	}

	@Test
	public void testHasReadOnlyAccess()
	{
		assertTrue(SessionHelper.hasReadOnlyAccess(session));

		setAccess("");
		assertTrue(SessionHelper.hasReadOnlyAccess(session));

		setAccess("0");
		assertTrue(SessionHelper.hasReadOnlyAccess(session));

		setAccess("3");
		assertFalse(SessionHelper.hasReadOnlyAccess(session));
	}

	@Test
	public void testHasUpdateAccess()
	{
		assertFalse(SessionHelper.hasUpdateAccess(session));

		setAccess("");
		assertFalse(SessionHelper.hasUpdateAccess(session));

		setAccess("1");
		assertTrue(SessionHelper.hasUpdateAccess(session));

		setAccess("3");
		assertTrue(SessionHelper.hasUpdateAccess(session));
	}

	@Test
	public void testHasDownloadAccess()
	{
		assertFalse(SessionHelper.hasDownloadAccess(session));

		setAccess("");
		assertFalse(SessionHelper.hasDownloadAccess(session));

		setAccess("2");
		assertTrue(SessionHelper.hasDownloadAccess(session));

		setAccess("3");
		assertTrue(SessionHelper.hasDownloadAccess(session));
	}

	private void setAccess(String access)
	{
		SessionHelper.setAccess(session, access);
	}
}

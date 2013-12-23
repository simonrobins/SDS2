package helpers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import misc.Settings;
import models.Account;
import models.AccountContact;

import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.mvc.Http;
import controllers.AbstractTest;

public class SessionHelperTest extends AbstractTest
{
	private static Http.Session	session;
	private AccountContact		contact;

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

		Account account = new Account();
		account.setId(999999);
		account.setName("Testing, testing");

		contact = new AccountContact();
		contact.setId(1234);
		contact.setEmail("email@example.com");
		contact.setAccount(account);
	}

	@Test
	public void testConstructor()
	{
		Assertions.assertThat(new SessionHelper()).isNotNull();
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

	@Test
	public void testGetAccountIdAsString()
	{
		SessionHelper.setContact(session, contact, Settings.APPLICATION_SECRET);
		Assertions.assertThat(SessionHelper.getAccountIdAsString(session, "")).isNull();
		SessionHelper.setContact(session, contact, Settings.APPLICATION_SECRET);
		Assertions.assertThat(SessionHelper.getAccountIdAsString(session, Settings.APPLICATION_SECRET)).isEqualTo("999999");
	}

	@Test
	public void testGetAccountIdAsStringWithEmptySession()
	{
		Assertions.assertThat(SessionHelper.getAccountIdAsString(session, "")).isNull();
		Assertions.assertThat(SessionHelper.getAccountIdAsString(session, Settings.APPLICATION_SECRET)).isNull();
	}

	@Test
	public void getAccountId()
	{
		SessionHelper.setContact(session, contact, Settings.APPLICATION_SECRET);
		Assertions.assertThat(SessionHelper.getAccountId(session, Settings.APPLICATION_SECRET)).isEqualTo(999999);
	}

	@Test(expected = NumberFormatException.class)
	public void getAccountIdWithInvalidIs()
	{
		SessionHelper.setContact(session, contact, Settings.APPLICATION_SECRET);
		Assertions.assertThat(SessionHelper.getAccountId(session, "")).isNull();
	}

	@Test
	public void testGetAccountContactId()
	{
		SessionHelper.setContact(session, contact, Settings.APPLICATION_SECRET);
		Assertions.assertThat(SessionHelper.getAccountContactId(session, Settings.APPLICATION_SECRET)).isEqualTo(1234);
	}

	@Test(expected = RuntimeException.class)
	public void testGetAccountContactIdWithInvalidSecret()
	{
		SessionHelper.setContact(session, contact, Settings.APPLICATION_SECRET);
		Assertions.assertThat(SessionHelper.getAccountContactId(session, "")).isNull();
		fail("Expected RuntimeException");
	}

	@Test(expected = RuntimeException.class)
	public void testGetAccountContactIdWithEmptySessionAndInvalidId()
	{
		Assertions.assertThat(SessionHelper.getAccountContactId(session, "")).isNull();
		fail("Expected RuntimeException");
	}

	@Test(expected = RuntimeException.class)
	public void testGetAccountContactIdWithEmptySession()
	{
		Assertions.assertThat(SessionHelper.getAccountContactId(session, Settings.APPLICATION_SECRET)).isNull();
		fail("Expected RuntimeException");
	}

	@Test
	public void getAccountName()
	{

	}

	@Test
	public void getAccountUsername()
	{

	}

	private void setAccess(String access)
	{
		SessionHelper.setAccess(session, access);
	}
}

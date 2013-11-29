package helpers;

import misc.Settings;
import models.AccountContact;

import org.eclipse.jdt.annotation.Nullable;

import play.Logger;
import play.mvc.Http.Session;
import security.Cipher;

public class SessionHelper
{
	public final static String USERNAME = "username";
	public final static String ACCOUNT_ID = "account_id";
	public final static String ACCOUNT_NAME = "account_name";
	public final static String ACCOUNT_CONTACT_ID = "account_contact_id";

	private final static String ACCESS = "access";

	public final static String READ_ONLY = "0";
	public final static String UPDATE_ONLY = "1";
	public final static String DOWNLOAD_ONLY = "2";
	public final static String FULL_ACCESSS = "3";

	public final static SessionHelper INSTANCE = new SessionHelper();

	private SessionHelper()
	{
	}

	public boolean hasReadOnlyAccess(final Session session)
	{
		return !hasAccess(session, 3);
	}

	public boolean hasUpdateAccess(final Session session)
	{
		return hasAccess(session, 1);
	}

	public boolean hasDownloadAccess(final Session session)
	{
		return hasAccess(session, 2);
	}

	private static boolean hasAccess(final Session session, final int flags)
	{
		int access = 0;

		try
		{
			String accessValue = session.get(ACCESS);
			if(accessValue == null)
				throw new NullPointerException("access is null");

			String secret = Settings.APPLICATION_SECRET;
			if(secret == null)
				throw new NullPointerException("secret is null");

			final String accessString = Cipher.getInstance().aesDecoder(accessValue, secret);
			access = Integer.parseInt(accessString);
		}
		catch(final Exception ex)
		{
		}

		return (access & flags) != 0;
	}

	public void setAccess(final Session session, final String access)
		throws Exception
	{
		final String encryptAccess = Cipher.getInstance().aesEncoder(access, Settings.APPLICATION_SECRET);

		if(encryptAccess != null)
			session.put(ACCESS, encryptAccess);
		else
			session.clear();
	}

	public void setContact(final Session session, final AccountContact contact)
		throws Exception
	{
		final String accountId = new Integer(contact.getAccount().getId()).toString();
		final String encryptAccountId = Cipher.getInstance().aesEncoder(accountId, Settings.APPLICATION_SECRET);

		final String name = contact.getAccount().getName();
		final String encryptName = Cipher.getInstance().aesEncoder(name, Settings.APPLICATION_SECRET);

		final String accountContactId = new Integer(contact.getId()).toString();
		final String encryptAccountContactId = Cipher.getInstance().aesEncoder(accountContactId, Settings.APPLICATION_SECRET);

		final String username = contact.getUsername();
		final String encryptUsername = Cipher.getInstance().aesEncoder(username, Settings.APPLICATION_SECRET);

		if(encryptAccountId != null && encryptName != null && encryptAccountContactId != null && encryptUsername != null)
		{
			session.put(ACCOUNT_ID, encryptAccountId);
			session.put(ACCOUNT_NAME, encryptName);
			session.put(ACCOUNT_CONTACT_ID, encryptAccountContactId);
			session.put(USERNAME, encryptUsername);
		}
		else
			session.clear();

		Logger.info(contact.getUsername() + " for (" + contact.getAccount().getName() + ") logged in");
	}

	@Nullable
	public String getAccountIdAsString(final Session session)
	{
		try
		{
			final String encryptAccountId = session.get(ACCOUNT_ID);
			if(encryptAccountId == null)
				return null;
			else
				return Cipher.getInstance().aesDecoder(encryptAccountId, Settings.APPLICATION_SECRET);
		}
		catch(final Exception ex)
		{
			session.clear();
			return null;
		}
	}

	public int getAccountId(final Session session)
	{
		String accountId = getAccountIdAsString(session);
		if(accountId == null)
			throw new NumberFormatException("null");
		return Integer.parseInt(accountId);
	}

	public int getAccountContactId(final Session session)
	{
		try
		{
			final String encryptAccountContactId = session.get(ACCOUNT_CONTACT_ID);
			if(encryptAccountContactId == null)
				throw new NullPointerException("encryptAccountContactId is null");

			String secret = Settings.APPLICATION_SECRET;
			if(secret == null)
				throw new NullPointerException("secret is null");

			final String accountContactId = Cipher.getInstance().aesDecoder(encryptAccountContactId, secret);
			return Integer.parseInt(accountContactId);
		}
		catch(final Exception e)
		{
			session.clear();
			return 0;
		}
	}

	@Nullable
	public String getAccountName(final Session session)
	{
		try
		{
			String account = session.get(ACCOUNT_NAME);
			if(account == null)
				throw new NullPointerException("account is null");

			String secret = Settings.APPLICATION_SECRET;
			if(secret == null)
				throw new NullPointerException("secret is null");

			return Cipher.getInstance().aesDecoder(account, secret);
		}
		catch(final Exception ex)
		{
			Logger.error(ex.getMessage() == null ? "" : ex.getMessage(), ex);
			session.clear();
			return null;
		}
	}

	@Nullable
	public String getAccountUsername(final Session session)
	{
		try
		{
			String username = session.get(USERNAME);
			if(username == null)
				throw new NullPointerException("username is null");
			String secret = Settings.APPLICATION_SECRET;
			if(secret == null)
				throw new NullPointerException("secret is null");

			return Cipher.getInstance().aesDecoder(username, secret);
		}
		catch(final Exception ex)
		{
			Logger.error(ex.getMessage() == null ? "" : ex.getMessage());
			session.clear();
			return null;
		}
	}
}

package helpers;

import misc.Settings;
import models.AccountContact;

import org.eclipse.jdt.annotation.Nullable;

import play.Logger;
import play.mvc.Http.Session;
import security.Cipher;

public class SessionHelper
{
	public final static String	EMAIL				= "username";
	public final static String	ACCOUNT_ID			= "account_id";
	public final static String	ACCOUNT_NAME		= "account_name";
	public final static String	ACCOUNT_CONTACT_ID	= "account_contact_id";

	private final static String	ACCESS				= "access";

	public final static String	READ_ONLY			= "0";
	public final static String	UPDATE_ONLY			= "1";
	public final static String	DOWNLOAD_ONLY		= "2";
	public final static String	FULL_ACCESSS		= "3";

	static public boolean hasReadOnlyAccess(final Session session)
	{
		return !hasAccess(session, 3);
	}

	static public boolean hasUpdateAccess(final Session session)
	{
		return hasAccess(session, 1);
	}

	static public boolean hasDownloadAccess(final Session session)
	{
		return hasAccess(session, 2);
	}

	static private boolean hasAccess(final Session session, final int flags)
	{
		int access = 0;

		try
		{
			String accessValue = session.get(ACCESS);
			if (accessValue == null)
				return false;

			String secret = Settings.APPLICATION_SECRET;

			final String accessString = Cipher.aesDecoder(accessValue, secret);
			access = Integer.parseInt(accessString);
		}
		catch (final Exception ex)
		{
			Logger.error(ex.getMessage(), ex);
		}

		return (access & flags) != 0;
	}

	static public void setAccess(final Session session, final String access)
	{
		try
		{
			final String encryptAccess = Cipher.aesEncoder(access, Settings.APPLICATION_SECRET);
			session.put(ACCESS, encryptAccess);
		}
		catch (RuntimeException e)
		{
			session.clear();
		}
	}

	static public void setContact(final Session session, final AccountContact contact) throws Exception
	{
		final String accountId = new Integer(contact.getAccount().getId()).toString();
		final String encryptAccountId = Cipher.aesEncoder(accountId, Settings.APPLICATION_SECRET);

		final String company = contact.getAccount().getName();
		final String encryptCompany = Cipher.aesEncoder(company, Settings.APPLICATION_SECRET);

		final String accountContactId = new Integer(contact.getId()).toString();
		final String encryptAccountContactId = Cipher.aesEncoder(accountContactId, Settings.APPLICATION_SECRET);

		final String email = contact.getEmail();
		final String encryptEmail = Cipher.aesEncoder(email, Settings.APPLICATION_SECRET);

		if (encryptAccountId != null && encryptCompany != null && encryptAccountContactId != null && encryptEmail != null)
		{
			session.put(ACCOUNT_ID, encryptAccountId);
			session.put(ACCOUNT_NAME, encryptCompany);
			session.put(ACCOUNT_CONTACT_ID, encryptAccountContactId);
			session.put(EMAIL, encryptEmail);
		}
		else
			session.clear();

		Logger.info(email + " for (" + company + ") logged in");
	}

	@Nullable
	static public String getAccountIdAsString(final Session session)
	{
		try
		{
			final String encryptAccountId = session.get(ACCOUNT_ID);
			if (encryptAccountId == null)
				return null;
			else
				return Cipher.aesDecoder(encryptAccountId, Settings.APPLICATION_SECRET);
		}
		catch (final Exception ex)
		{
			Logger.error(ex.getMessage(), ex);
			session.clear();
			return null;
		}
	}

	static public int getAccountId(final Session session)
	{
		String accountId = getAccountIdAsString(session);
		if (accountId == null)
			throw new NumberFormatException("null");
		return Integer.parseInt(accountId);
	}

	static public int getAccountContactId(final Session session)
	{
		try
		{
			final String encryptAccountContactId = session.get(ACCOUNT_CONTACT_ID);
			if (encryptAccountContactId == null)
				throw new NullPointerException("encryptAccountContactId is null");

			String secret = Settings.APPLICATION_SECRET;
			if (secret == null)
				throw new NullPointerException("secret is null");

			final String accountContactId = Cipher.aesDecoder(encryptAccountContactId, secret);
			return Integer.parseInt(accountContactId);
		}
		catch (final Exception e)
		{
			Logger.error(e.getMessage(), e);
			session.clear();
			return 0;
		}
	}

	@Nullable
	static public String getAccountName(final Session session)
	{
		try
		{
			String account = session.get(ACCOUNT_NAME);
			if (account == null)
				throw new NullPointerException("account is null");

			String secret = Settings.APPLICATION_SECRET;

			return Cipher.aesDecoder(account, secret);
		}
		catch (final Exception ex)
		{
			Logger.error(ex.getMessage(), ex);
			session.clear();
			return null;
		}
	}

	@Nullable
	static public String getAccountUsername(final Session session)
	{
		try
		{
			String username = session.get(EMAIL);
			if (username == null)
				throw new NullPointerException("username is null");
			String secret = Settings.APPLICATION_SECRET;

			return Cipher.aesDecoder(username, secret);
		}
		catch (final Exception ex)
		{
			Logger.error(ex.getMessage());
			session.clear();
			return null;
		}
	}
}

package helpers;

import misc.Settings;
import misc.Utilities;
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

	static public void setContact(final Session session, final AccountContact contact, String secret)
	{
		final String accountId = new Integer(contact.getAccount().getId()).toString();
		final String encryptAccountId = Cipher.aesEncoder(accountId, secret);

		final String company = contact.getAccount().getName();
		final String encryptCompany = Cipher.aesEncoder(company, secret);

		final String accountContactId = new Integer(contact.getId()).toString();
		final String encryptAccountContactId = Cipher.aesEncoder(accountContactId, secret);

		final String email = contact.getEmail();
		final String encryptEmail = Cipher.aesEncoder(email, secret);

		session.clear();

		if (Utilities.isNotEmpty(encryptAccountId) && Utilities.isNotEmpty(encryptCompany) && Utilities.isNotEmpty(encryptAccountContactId) && Utilities.isNotEmpty(encryptEmail))
		{
			session.put(ACCOUNT_ID, encryptAccountId);
			session.put(ACCOUNT_NAME, encryptCompany);
			session.put(ACCOUNT_CONTACT_ID, encryptAccountContactId);
			session.put(EMAIL, encryptEmail);
		}

		Logger.info(email + " for (" + company + ") logged in");
	}

	@Nullable
	static public String getAccountIdAsString(final Session session, String secret)
	{
		try
		{
			final String encryptAccountId = session.get(ACCOUNT_ID);
			return Cipher.aesDecoder(encryptAccountId, secret);
		}
		catch (final Exception ex)
		{
			Logger.error(ex.getMessage(), ex);
			session.clear();
			return null;
		}
	}

	static public Integer getAccountId(final Session session, String secret)
	{
		String accountId = getAccountIdAsString(session, secret);
		return new Integer(accountId);
	}

	static public Integer getAccountContactId(final Session session, String secret)
	{
		try
		{
			final String encryptAccountContactId = session.get(ACCOUNT_CONTACT_ID);
			final String accountContactId = Cipher.aesDecoder(encryptAccountContactId, secret);
			return new Integer(accountContactId);
		}
		catch (final Exception e)
		{
			Logger.error(e.getMessage(), e);
			session.clear();
			throw new RuntimeException(e);
		}
	}

	@Nullable
	static public String getAccountName(final Session session, String secret)
	{
		try
		{
			String account = session.get(ACCOUNT_NAME);
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
	static public String getAccountUsername(final Session session, String secret)
	{
		try
		{
			String username = session.get(EMAIL);
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

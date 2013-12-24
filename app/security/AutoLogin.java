package security;

import java.security.MessageDigest;
import java.util.Date;

import org.eclipse.jdt.annotation.Nullable;

import play.Logger;
import play.cache.Cache;

public class AutoLogin
{
	private static char[]	HEX	=
								{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static boolean validateMd5(final String secret, final String id, final String expires, final String md5)
	{
		return validateMd5(secret, id, expires, null, md5);
	}

	public static boolean validateMd5(final String secret, final String id, final String expires, @Nullable final String access, final String md5)
	{
		// Convert expired from seconds to milliseconds
		long until = Long.parseLong(expires) * 1000;
		long currentTimeMillis = System.currentTimeMillis();

		Logger.debug("Current = " + (new Date(currentTimeMillis)));
		Logger.debug("Until = " + (new Date(until)));

		final String calculatedMd5 = generateMd5(secret, id, expires, access);

		if (!md5.equals(calculatedMd5))
			return false;

		// Has it expired?
		if (currentTimeMillis > until)
			return false;

		// Only cache the MD5 if it came from the CODA Portal - i.e. the access
		// level is null
		if (access == null)
		{
			// Have we seen this MD5 before?
			if (Cache.get(md5) != null)
				return false;

			int expiresIn = (int) ((until - currentTimeMillis) / 1000);
			Cache.set(md5, md5, expiresIn);
		}

		return true;
	}

	public static String generateMd5(final String secret, final String id, final String expires, @Nullable final String access)
	{
		final StringBuffer sb = new StringBuffer();
		final MessageDigest digest = digest("MD5");
		digest.update(secret.getBytes());
		digest.update(new Integer(id).toString().getBytes());
		digest.update(expires.toString().getBytes());
		if (access != null)
			digest.update(new Integer(access).toString().getBytes());
		for (final byte hash : digest.digest())
		{
			final int h = hash >> 4 & 0xf;
			final int l = hash & 0xf;
			sb.append(HEX[h]);
			sb.append(HEX[l]);
		}

		return sb.toString();
	}

	public static MessageDigest digest(String algorithm)
	{
		try
		{
			return MessageDigest.getInstance(algorithm);
		}
		catch (final Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}
}

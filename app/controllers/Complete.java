package controllers;

import finders.CompanyDownloadFinder;
import helpers.SessionHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import misc.Settings;
import models.CompanyDownload;

import org.eclipse.jdt.annotation.Nullable;

import play.Logger;
import play.db.ebean.Transactional;
import play.mvc.Http.Request;
import play.mvc.Result;

import com.avaje.ebean.Ebean;

public class Complete extends BaseController
{
	public static boolean testRef(final String ref)
	{
		final String account = SessionHelper.getAccountIdAsString(session(), Settings.APPLICATION_SECRET);

		Logger.info("Testing download ref: " + account + ":" + ref);

		if (account == null)
			return false;

		final Set<CompanyDownload> cd = CompanyDownloadFinder.findAccountReference(account, ref);

		return (cd.size() != 0);
	}

	@Transactional
	public static Result complete()
	{
		final String accountId = SessionHelper.getAccountIdAsString(session(), Settings.APPLICATION_SECRET);

		final String[] parts = getUriParts();

		if (parts == null || parts.length < 2)
			return internalServerError();

		final String downloadRef = parts[parts.length - 1];
		final String filename = parts[parts.length - 2];

		Logger.info("Download Ref = " + downloadRef);
		Logger.info("Filename = " + filename);

		if (accountId != null && downloadRef != null && filename != null)
		{
			final String x_request_completion = get("X-Request-Completion");
			final String x_body_bytes_sent = get("X-Body-Bytes-Sent");

			long bytesSent = -1;
			if (x_body_bytes_sent != null)
			{
				try
				{
					bytesSent = Long.parseLong(x_body_bytes_sent);
				}
				catch (final Exception ex)
				{
					Logger.error(ex.getMessage());
				}
			}

			Logger.info(downloadRef + " X-BODY-BYTES-SENT = " + bytesSent);

			final boolean success = x_request_completion != null && "OK".equals(x_request_completion);

			final Set<CompanyDownload> cds = CompanyDownloadFinder.findAccountReference(accountId, downloadRef);
			for (final CompanyDownload cd : cds)
			{
				cd.setType(filename);
				cd.setSuccess(success ? 'Y' : 'N');
				cd.setEndDate(new Date());
				cd.setBytesSent(bytesSent);
				Ebean.update(cd);
				Logger.info(cd.toString());
			}
		}

		return ok();
	}

	private static @Nullable
	String[] getUriParts()
	{
		try
		{
			final String x_request_uri = get("X-Request-Uri");
			final String url = URLDecoder.decode(x_request_uri, "utf-8");
			final String[] parts = url.split("/");
			return parts;
		}
		catch (final UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private static String get(String key)
	{
		final Request request = request();
		final Map<String, String[]> headers = request.headers();

		if (headers.get(key) == null)
			key = key.toUpperCase();

		String[] header = headers.get(key);
		return header[0];
	}
}

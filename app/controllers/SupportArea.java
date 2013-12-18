package controllers;

import helpers.SessionHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import misc.Settings;
import misc.Utilities;

import org.eclipse.jdt.annotation.Nullable;

import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Http.RequestBody;
import play.mvc.Result;
import play.mvc.Security;
import security.Secured;
import data.DocumentMap;
import data.DocumentNode;

@Security.Authenticated(Secured.class)
public class SupportArea extends Controller
{
	public static Result index()
	{
		final String accountId = SessionHelper.getAccountIdAsString(session());
		if(accountId == null)
			return internalServerError();

		final DocumentNode documents = DocumentMap.getSupportMap(accountId);

		final Double ieVersion = Utilities.getInternetExplorerVersion(request());

		return ok(views.html.supportarea.index.render(documents, ieVersion));
	}

	public static Result download1(final String filename)
	{
		final String accountId = SessionHelper.getAccountIdAsString(session());
		if(accountId == null)
			return notFound(filename);

		final File file = Utilities.fileFromPathComponents(Settings.SUPPORT_DIR, accountId, filename);
		if(!file.exists())
			return notFound(filename);

		final String url = routes.SupportArea.internal1(accountId, filename).url();

		response().setHeader("X-Accel-Redirect", url);
		response().setHeader("Content-Disposition", "attachment; filename=" + filename);

		return ok();
	}

	public static Result download2(final String level1, final String filename)
	{
		final String accountId = SessionHelper.getAccountIdAsString(session());
		if(accountId == null)
			return internalServerError();

		final File file = Utilities.fileFromPathComponents(Settings.SUPPORT_DIR, accountId, level1, filename);
		if(!file.exists())
			return notFound(filename);

		final String url = routes.SupportArea.internal2(accountId, level1, filename).url();

		response().setHeader("X-Accel-Redirect", url);
		response().setHeader("Content-Disposition", "attachment; filename=" + filename);

		return ok();
	}

	// These are just a dummy actions so that X-Accel-Redirect can calculate the
	// necessary URL
	public static Result internal1(final String accountId, final String filename)
	{
		return ok();
	}

	public static Result internal2(final String accountId, final String level1, final String filename)
	{
		return ok();
	}

	public static Result upload()
	{
		final RequestBody body = request().body();
		final MultipartFormData formData = body.asMultipartFormData();
		final FilePart filepart = formData.getFile("file");
		if(filepart != null)
		{
			final String filename = filepart.getFilename();
			final File srcFile = filepart.getFile();

			final String accountId = SessionHelper.getAccountIdAsString(session());
			if(accountId == null)
				return internalServerError();

			File support = new File(Settings.SUPPORT_DIR, accountId);
			support.mkdirs();

			final File destFile = new File(support, filename);

			@Nullable
			FileChannel src = null;
			@Nullable
			FileChannel dest = null;
			try
			{
				if(!destFile.exists())
					destFile.createNewFile();

				src = new FileInputStream(srcFile).getChannel();
				if(src == null)
					throw new NullPointerException("File not found: " + srcFile);
				dest = new FileOutputStream(destFile).getChannel();
				if(dest == null)
					throw new NullPointerException("File not found: " + destFile);
				dest.transferFrom(src, 0, src.size());
			}
			catch(final Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				Utilities.quietClose(src);
				Utilities.quietClose(dest);
			}
		}
		return redirect(routes.SupportArea.index());
	}
}
package controllers;

import java.io.File;

import misc.Settings;
import misc.Utilities;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import security.Secured;
import data.AbstractNode;
import data.DocumentMap;

@Security.Authenticated(Secured.class)
public class Documents extends Controller
{
	public static Result index()
	{
		final AbstractNode documents = DocumentMap.getDocumentMap();

		final Double ieVersion = Utilities.getInternetExplorerVersion(request());

		return ok(views.html.documents.index.render(documents, ieVersion));
	}

	public static Result download3(final String level1, final String level2, final String filename)
	{
		final File file = Utilities.fileFromPathComponents(Settings.DOCUMENTS_DIR, level1, level2, filename);
		if(!file.exists())
			return notFound(filename);

		final String url = routes.Documents.internal3(level1, level2, filename).url();

		response().setHeader("X-Accel-Redirect", url);
		response().setHeader("Content-Disposition", "attachment; filename=" + filename);

		return ok();
	}

	public static Result download4(final String level1, final String level2, String level3, final String filename)
	{
		final File file = Utilities.fileFromPathComponents(Settings.DOCUMENTS_DIR, level1, level2, level3, filename);
		if(!file.exists())
			return notFound(filename);

		final String url = routes.Documents.internal4(level1, level2, level3, filename).url();

		response().setHeader("X-Accel-Redirect", url);
		response().setHeader("Content-Disposition", "attachment; filename=" + filename);

		return ok();
	}

	// This is just a dummy action so that X-Accel-Redirect can calculate the
	// necessary URL
	public static Result internal3(final String level1, final String level2, final String level3)
	{
		return ok();
	}

	// This is just a dummy action so that X-Accel-Redirect can calculate the
	// necessary URL
	public static Result internal4(final String level1, final String level2, final String level3, String level4)
	{
		return ok();
	}
}
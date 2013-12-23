package controllers;

import finders.CompanyDownloadFinder;
import helpers.Helpers;
import helpers.SessionHelper;

import java.io.File;
import java.util.Set;

import misc.Settings;
import misc.Utilities;
import models.CodaSdsValidate;
import models.CompanyDownload;
import play.Logger;
import play.mvc.Result;
import play.mvc.Security;
import security.Secured;
import data.AbstractNode;
import data.ProductMap;
import data.ProductNode;
import data.ProductVersion;

@Security.Authenticated(Secured.class)
public class Releases extends BaseController
{
	public static Result index()
	{
		final int accountId = SessionHelper.getAccountId(session(), Settings.APPLICATION_SECRET);
		final Set<ProductVersion> allowed = CodaSdsValidate.getProductVersionMap2(accountId);

		final ProductNode products = ProductMap.getProductMap();
		products.removeDisallowed(allowed);

		final Double ieVersion = Utilities.getInternetExplorerVersion(request());

		return ok(views.html.releases.index.render(products, ieVersion));
	}

	public static Result download(final String level1, final String level2, final String filename)
	{
		if (!SessionHelper.hasDownloadAccess(session()))
			return index();

		final File file = Utilities.fileFromPathComponents(Settings.RELEASES_DIR, level1, level2, filename);
		if (!file.exists())
			return notFound(file);

		final int accountId = SessionHelper.getAccountId(session(), Settings.APPLICATION_SECRET);
		final Set<ProductVersion> allowedProducts2 = CodaSdsValidate.getProductVersionMap2(accountId, true);

		final AbstractNode releases = ProductMap.getProductMap();
		final ProductNode node = releases.get(level1, level2, filename);
		if (node == null)
			return notFound(file);

		final Set<ProductVersion> products = node.getProducts();
		products.retainAll(allowedProducts2);

		if (products.isEmpty())
			return notFound(file);

		String ref = "";
		if (SessionHelper.hasUpdateAccess(session()))
			ref = Helpers.updateShippingOrderReleases(SessionHelper.getAccountContactId(session(), Settings.APPLICATION_SECRET), products, file);

		return redirect(routes.Releases.redirect(level1, level2, filename, ref).url());
	}

	public static Result redirect(final String level1, final String level2, final String filename, final String ref)
	{
		final Set<CompanyDownload> cds = CompanyDownloadFinder.findReference(ref);

		if (cds.size() == 0)
			return redirect(routes.Releases.index());

//		final String url = "/releases/internal/" + level1 + "/" + level2 + "/" + filename;
		final String url = routes.Releases.internal(level1, level2, filename).url();

		Logger.debug("Redirect to " + url + "(" + url + ")");

		response().setHeader("X-Accel-Redirect", url);
		response().setHeader("Content-Disposition", "attachment; filename=" + filename);

		return ok();
	}

	// This is just a dummy action so that X-Accel-Redirect can calculate the
	// necessary URL
	public static Result internal(final String level1, final String level2, final String level3)
	{
		return ok();
	}
}

package controllers;

import finders.ServicePackFinder;
import helpers.Helpers;
import helpers.SessionHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import misc.Constants;
import misc.Settings;
import misc.Utilities;
import models.CodaSdsValidate;
import models.ServicePack;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import security.Secured;
import data.ProductMap;
import data.ProductVersion;

@Security.Authenticated(Secured.class)
public class Ccm extends Controller
{
	public static Result ccm()
	{
		final Map<String, Set<ProductVersion>> files = ProductMap.parseProducts(fileFromPathComponents("servicepacks.txt"));

		final int accountId = SessionHelper.getAccountId(session());

		final Map<Integer, Set<Integer>> allowedProducts = CodaSdsValidate.getProductVersionMap(accountId);

		final List<File> fileList = new ArrayList<File>();

		final Set<Integer> allowedVersions = allowedProducts.get(Constants.ProductId.CCM);
		for(final String name : files.keySet())
		{
			final Set<ProductVersion> servicePacks = files.get(name);
			final ProductVersion servicePack = servicePacks.iterator().next();
			final ServicePack sp = ServicePackFinder.find(servicePack.productId);
			if(sp.getProduct().getId() == Constants.ProductId.CCM && sp.getStatus().startsWith("Available") && allowedVersions.contains(sp.getVersion().getId()))
			{
				final File file = fileFromPathComponents(name);
				fileList.add(file);
			}
		}

		return ok(views.html.servicepack.ccm.render(getCcmInfo(), fileList));
	}

	public static Result download(final String filename)
	{
		if(!SessionHelper.hasDownloadAccess(session()))
			return ccm();

		final Map<String, Set<ProductVersion>> files = ProductMap.parseProducts(fileFromPathComponents("servicepacks.txt"));

		final File dir = fileFromPathComponents(filename);
		if(dir.isFile())
		{
			final int accountContactId = SessionHelper.getAccountContactId(session());

			final Set<ProductVersion> servicePacks = files.get(filename);
			final ProductVersion servicePack = servicePacks.iterator().next();
			final ServicePack sp = ServicePackFinder.find(servicePack.productId);

			String ref = "";
			if(SessionHelper.hasUpdateAccess(session()))
				ref = Helpers.updateShippingOrderServicePacks(accountContactId, sp, filename);

			return redirect(routes.Ccm.stream(filename, ref));
		}
		else
		{
			return ccm();
		}
	}

	public static Result stream(final String archive, final String ref)
	{
		if(Complete.testRef(ref) == false)
		{
			return redirect(routes.Servicepack.index());
		}

		final File file = fileFromPathComponents(archive);
		if(file.exists())
		{
			final String url = routes.Ccm.internal(archive).url();

			response().setHeader("X-Accel-Redirect", url);
			response().setHeader("Content-Disposition", "attachment; filename=" + archive);
		}

		return ok();
	}

	public static Result internal(final String file)
	{
		return ok();
	}

	private static File fileFromPathComponents(final String... files)
	{
		File dir = Settings.SERVICEPACKS_CCM_DIR;

		for(final String file : files)
		{
			dir = new File(dir, file);
		}

		return dir;
	}

	private static String getCcmInfo()
	{
		return Utilities.getInfo(Settings.SERVICEPACKS_CCM_DIR);
	}
}
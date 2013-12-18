package controllers;

import helpers.Helpers;
import helpers.SessionHelper;

import java.util.Set;

import misc.Utilities;
import models.CodaSdsValidate;
import models.SdsValidate;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import security.Secured;
import data.AbstractNode;
import data.LanguageMap;
import data.LanguageNode;
import data.LanguageProduct;
import data.ProductVersion;

@Security.Authenticated(Secured.class)
public class Language extends Controller
{
	public static Result index()
	{
		final int accountId = SessionHelper.getAccountId(session());
		final Set<ProductVersion> allowedProducts = CodaSdsValidate.getProductVersionMap2(accountId);
		final Set<Integer> allowedSubProducts = SdsValidate.INSTANCE.getSubProducts(accountId);

		final AbstractNode languages = LanguageMap.getLanguageMap();

		getlanguageHierarchy(allowedProducts, allowedSubProducts, languages);

		final Double ieVersion = Utilities.getInternetExplorerVersion(request());

		return ok(views.html.language.index.render(languages, ieVersion));
	}

	private static void getlanguageHierarchy(final Set<ProductVersion> allowedProducts, final Set<Integer> allowedSubProducts, final AbstractNode languages)
	{
		for(final AbstractNode level1 : languages.getChildren())
		{
			for(final AbstractNode level2 : level1.getChildren())
			{
				for(final AbstractNode level3 : level2.getChildren())
				{
					for(final AbstractNode level4 : level3.getChildren())
					{
						for(final AbstractNode level5 : level4.getChildren())
						{
							level5.intersect(allowedProducts);
							if(level5.isEmpty())
								level4.removeChild(level5);
							if(level5.isNotAllowed(allowedSubProducts))
								level4.removeChild(level5);
						}
						level3.removeIfNoChildren(level4);
					}
					level2.removeIfNoChildren(level3);
				}
				level1.removeIfNoChildren(level2);
			}
			languages.removeIfNoChildren(level1);
		}
	}

	public static Result download(final String level1, final String level2, final String level3, final String lang, final String filename)
	{
		if(!SessionHelper.hasDownloadAccess(session()))
			return index();

		final int accountId = SessionHelper.getAccountId(session());

		final Set<ProductVersion> allowedProducts = CodaSdsValidate.getProductVersionMap2(accountId, true);
		final Set<Integer> allowedSubProducts = SdsValidate.INSTANCE.getSubProducts(accountId);

		final AbstractNode languages = LanguageMap.getLanguageMap();
		final LanguageNode language = languages.get(level1, level2, level3, lang, filename);
		final LanguageProduct languageList = language.getLanguage();

		final Set<ProductVersion> products2 = languageList.getProducts();
		products2.retainAll(allowedProducts);

		if(products2.size() > 0 && allowedSubProducts.contains(languageList.languageId))
		{
			final int accountContactId = SessionHelper.getAccountContactId(session());

			String ref = "";
			if(SessionHelper.hasUpdateAccess(session()))
				ref = Helpers.updateShippingOrderLanguagePack(accountContactId, languageList, filename);

			return redirect(routes.Language.redirect(level1, level2, level3, filename, ref));
		}
		else
		{
			return internalServerError(filename);
		}
	}

	public static Result redirect(final String level1, final String level2, final String level3, final String filename, final String ref)
	{
		if(Complete.testRef(ref) == false)
		{
			return redirect(routes.Language.index());
		}

		final String url = routes.Language.internal(level1, level2, level3, filename).url();

		response().setHeader("X-Accel-Redirect", url);
		response().setHeader("Content-Disposition", "attachment; filename=" + filename);

		return ok();
	}

	public static Result install(final String level1, final String level2, final String filename)
	{
		final String url = routes.Language.installer(level1, level2, filename).url();

		response().setHeader("X-Accel-Redirect", url);
		response().setHeader("Content-Disposition", "attachment; filename=" + filename);

		return ok();
	}

	public static Result installer(final String level1, final String level2, final String level3)
	{
		return ok();
	}

	public static Result internal(final String level1, final String level2, final String level3, final String level4)
	{
		return ok();
	}
}
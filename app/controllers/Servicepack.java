package controllers;

import finders.ServicePackProductFinder;
import helpers.Helpers;
import helpers.SessionHelper;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import misc.ArchiveGenerator;
import misc.Constants;
import misc.Constants.Products;
import misc.Download;
import misc.Settings;
import misc.Utilities;
import misc.Versions;
import models.AccountAddon;
import models.CodaSdsValidate;

import org.eclipse.jdt.annotation.Nullable;

import play.Logger;
import play.data.Form;
import play.libs.Akka;
import play.libs.F;
import play.libs.F.Function;
import play.libs.F.Tuple;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import security.Secured;
import tags.Lists;
import data.ProductVersion;

@Security.Authenticated(Secured.class)
public class Servicepack extends Controller
{
	public static Result index()
	{
		final int accountId = SessionHelper.INSTANCE.getAccountId(session());
		if(Logger.isDebugEnabled())
			Logger.of("servicepack").debug("account_id = " + accountId);

		final Map<Integer, Set<Integer>> allowedProducts = CodaSdsValidate.getProductVersionMap(accountId);
		final Set<Integer> spProducts = ServicePackProductFinder.getServicePackProducts();

		allowedProducts.keySet().retainAll(spProducts);

		final boolean ccm = allowedProducts.containsKey(Constants.ProductId.CCM);
		allowedProducts.remove(Constants.ProductId.CCM);

		final Set<Integer> versions = new LinkedHashSet<Integer>();
		for(final Integer key : allowedProducts.keySet())
			versions.addAll(allowedProducts.get(key));

		final boolean fin112 = versions.contains(Constants.ProductId.FINANCE_112); // 11.2
		final boolean fin113 = versions.contains(Constants.ProductId.FINANCE_113); // 11.3
		final boolean fin120 = versions.contains(Constants.ProductId.FINANCE_120); // 12.0

		return ok(views.html.servicepack.index.render(fin112, fin113, fin120, ccm));
	}

	public static Result finance(final String version)
	{
		final Integer versionId = Versions.get(version);

		final int accountId = SessionHelper.INSTANCE.getAccountId(session());
		if(Logger.isDebugEnabled())
			Logger.of("servicepack").debug("account_id = " + accountId);

		final Set<Integer> allowedProducts = CodaSdsValidate.getProductVersionMap(accountId, versionId).keySet();
		if(allowedProducts == null)
			return internalServerError("Allowed Products is empty");

		final Set<Integer> spProducts = ServicePackProductFinder.getServicePackProducts();
		spProducts.remove(Constants.ProductId.CCM);
		allowedProducts.retainAll(spProducts);

		if(allowedProducts.size() == 0)
			return badRequest();

		final List<Integer> servicepacks = Lists.getVersions(version);

		if(Logger.isDebugEnabled())
			Logger.of("servicepack").debug("servicepacks=" + servicepacks);

		final AccountAddon addon = Helpers.getAccountAddon(SessionHelper.INSTANCE.getAccountId(session()), version);

		return ok(views.html.servicepack.finance.render(getInfo(), version, servicepacks, allowedProducts, addon));
	}

	public static Result download()
	{
		if(!SessionHelper.INSTANCE.hasDownloadAccess(session()))
			return index();

		final Form<Download> filledForm = new Form<Download>(Download.class).bindFromRequest();

		if(filledForm.hasErrors())
			return badRequest();

		final Download df = filledForm.get();

		final String version = Settings.VERSION_FORMAT.format(df.getVersion());
		final Integer versionId = Versions.get(version);

		final String path = version + "-SP" + df.getServicepack();
		final String pdfName = "CODA V" + path + ".pdf";

		if(filledForm.data().containsKey("doc"))
		{
			final String url = routes.Servicepack.doc(path, pdfName).url();

			response().setHeader("X-Accel-Redirect", url);
			response().setHeader("Content-Disposition", "attachment; filename=" + pdfName);

			return ok();
		}

		final File root = new File(Settings.SERVICEPACKS_FINANCE_DIR, version + "-SP" + df.getServicepack());
		final File[] jars = root.listFiles(new FilenameFilter()
			{
				@Override
				public boolean accept(final @Nullable File arg0, final @Nullable String arg1)
				{
					if(arg1 == null)
						return false;
					else
						return arg1.endsWith(".jar");
				}
			});

		if(jars == null || jars.length == 0)
			return internalServerError("No jar files found (" + root + ")");

		final File[] zips = root.listFiles(new FilenameFilter()
			{
				@Override
				public boolean accept(final @Nullable File arg0, final @Nullable String arg1)
				{
					return (arg1 == null) ? false : arg1.endsWith(".zip");
				}
			});

		final List<File> productList = new ArrayList<File>();
		final Set<ProductVersion> products = new LinkedHashSet<ProductVersion>();

		final File doc = new File(new File(Settings.SERVICEPACKS_FINANCE_DIR, path), pdfName);
		if(doc.exists())
			productList.add(doc);

		for(final File zip : zips)
			productList.add(zip);

		final String name = jars[0].getName();
		final String build = name.substring(name.length() - 15, name.length() - 4);

		final boolean java = df.isAssets() | df.isBilling() | df.isFinance() | df.isPim() | df.isPop();
		final boolean redhat = df.getPlatform().equals("RED");

		long identifier = 0;

		if(!redhat && java)
		{
			identifier |= Products.FRAMEWORK_APP;
			final File product = makeProductFile(root, "framework-app", build);

			if(product.exists() == false)
				return internalServerError(product.getName());

			productList.add(product);
		}

		if(java)
		{
			identifier |= Products.FRAMEWORK_WEB;
			final File product = makeProductFile(root, "framework-web", build);

			if(product.exists() == false)
				return internalServerError(product.getName());

			productList.add(product);
			products.add(new ProductVersion(20, versionId));
		}

		if(df.isAssets())
		{
			if(!redhat)
			{
				identifier |= Products.ASSETS_APP;
				productList.add(new File(root, "assets-app-" + build + ".jar"));
			}
			identifier |= Products.ASSETS_WEB;
			productList.add(new File(root, "assets-web-" + build + ".jar"));
			products.add(new ProductVersion(2, versionId));
		}
		if(df.isBilling())
		{
			if(!redhat)
			{
				identifier |= Products.BILLING_APP;
				productList.add(new File(root, "billing-app-" + build + ".jar"));
				productList.add(new File(root, "billingmasters-app-" + build + ".jar"));
			}

			identifier |= Products.BILLING_WEB;
			productList.add(new File(root, "billing-web-" + build + ".jar"));
			productList.add(new File(root, "billingmasters-web-" + build + ".jar"));
			products.add(new ProductVersion(123, versionId));
		}
		if(!redhat && df.isCustomiser())
		{
			identifier |= Products.CUSTOMISER;
			productList.add(new File(root, "customiser-app-" + build + ".jar"));
			products.add(new ProductVersion(11, versionId));
		}
		if(df.isFinance())
		{
			identifier |= Products.FINANCE_WEB;
			productList.add(new File(root, "finance-web-" + build + ".jar"));
			products.add(new ProductVersion(12, versionId));
		}
		if(df.isPim())
		{
			if(!redhat)
			{
				identifier |= Products.PIM_APP;
				productList.add(new File(root, "invoicematching-app-" + build + ".jar"));
			}

			identifier |= Products.PIM_WEB;
			productList.add(new File(root, "invoicematching-web-" + build + ".jar"));
			products.add(new ProductVersion(22, versionId));
		}
		if(df.isPop())
		{
			if(!redhat)
			{
				identifier |= Products.POP_APP;
				productList.add(new File(root, "purchasing-app-" + build + ".jar"));
			}

			identifier |= Products.POP_WEB;
			productList.add(new File(root, "purchasing-web-" + build + ".jar"));
			products.add(new ProductVersion(127, versionId));
		}
		if(!redhat && df.isFin())
		{
			identifier |= Products.FINANCIALS;
			final File finance = makeFinanceAppName(root.getName(), build, df);

			if(finance.exists() == false)
			{
				Logger.error("Invalid platform, Database, Encoding combination: " + finance);
				flash("select.value", "Invalid platform, Database, Encoding combination.");
				return finance(version);
			}

			productList.add(finance);
			products.add(new ProductVersion(19, versionId));

			String encoding = df.getEncoding();
			if("CP285".equals(df.getEncoding()) || "CP37".equals(df.getEncoding()))
				encoding = "ASC";

			identifier |= Products.CORE_CLIENT;
			final File cc = makeCoreClientName(root.getName(), build, encoding);

			if(cc.exists() == false)
			{
				Logger.error("Invalid encoding: " + cc);
				flash("select.value", "Invalid encoding.");
				return finance(version);
			}

			productList.add(cc);
		}

		if(productList.size() == 0)
		{
			flash("select.value", "Please select at least one product");
			return finance(version);
		}

		final String archive = version + "-SP" + df.getServicepack() + "-" + df.getPlatform() + "-" + df.getDatabase() + "-" + df.getEncoding() + "-" + identifier + ".zip";

		createArchive(archive, productList);

		final String size = calculateArchiveSize(productList);

		final int accountContactId = SessionHelper.INSTANCE.getAccountContactId(session());

		String ref = "";
		if(SessionHelper.INSTANCE.hasUpdateAccess(session()))
			ref = Helpers.updateShippingOrderServicePacks(accountContactId, products, df, archive);

		return redirect(routes.Servicepack.stream(archive, ref, size));
	}

	public static Result stream(final String archive, final String ref, final String totalSize)
	{
		if(Complete.testRef(ref) == false)
			return redirect(routes.Servicepack.index());

		final File file = new File(Settings.DOWNLOAD_DIR, archive);
		if(file.exists())
		{
			final String url = routes.Servicepack.streaming(archive, ref).url();

			return ok(views.html.servicepack.streaming.render("0; url=" + url, url));
		}
		else
		{
			final long currentSize = new File(Settings.TEMPORARY_DIR, archive).length();

			final String url = routes.Servicepack.stream(archive, ref, totalSize).url();

			final long percentage = currentSize * 100 / Long.parseLong(totalSize);

			return ok(views.html.servicepack.stream.render("2; url=" + url, url, percentage));
		}
	}

	public static Result streaming(final String archive, final String ref)
	{
		if(Complete.testRef(ref) == false)
			return redirect(routes.Servicepack.index());

		final File file = new File(Settings.DOWNLOAD_DIR, archive);
		if(file.exists())
		{
			final String url = routes.Servicepack.internal(archive).url();

			response().setHeader("X-Accel-Redirect", url);
			response().setHeader("Content-Disposition", "attachment; filename=" + archive);

			return ok();
		}
		else
		{
			return ok(views.html.servicepack.stream.render("2; url=" + archive, archive, 100L));
		}
	}

	public static Result internal(final String archive)
	{
		return ok();
	}

	public static Result doc(final String path, final String filename)
	{
		return ok();
	}

	private static void createArchive(final String archive, final List<File> files)
	{
		final F.Promise<Tuple<String, List<File>>> promiseOfFile = Akka.future(new Callable<Tuple<String, List<File>>>()
			{
				@Override
				public Tuple<String, List<File>> call()
				{
					return new Tuple<String, List<File>>(archive, files);
				}
			});

		async(promiseOfFile.map(new Function<Tuple<String, List<File>>, Result>()
			{
				@Override
				public Result apply(final @Nullable Tuple<String, List<File>> tuple)
				{
					long maxRetain = Settings.ARCHIVE_MAX_RETAIN_PERIOD;
					long minRetain = Settings.ARCHIVE_MIN_RETAIN_PERIOD;
					long minFreeSpace = Settings.ARCHIVE_MIN_FREE_SPACE;

					if(tuple != null)
					{
						final ArchiveGenerator ag = new ArchiveGenerator(tuple._1, tuple._2, maxRetain, minRetain, minFreeSpace);
						ag.run();
					}

					return ok();
				}
			}));
	}

	private static String calculateArchiveSize(final List<File> files)
	{
		long size = 0;

		for(final File file : files)
			size += file.length();

		return new Long(size).toString();
	}

	private static File makeProductFile(final File root, final String name, final String build)
	{
		return new File(root, name + "-" + build + ".jar");
	}

	private static File makeCoreClientName(final String root, final String build, final String encoding)
	{
		final String filename = "core-client-" + encoding + "-" + build + ".exe";

		return new File(new File(new File(Settings.SERVICEPACKS_FINANCE_DIR, root), "core-client"), filename);
	}

	private static File makeFinanceAppName(final String root, final String build, final Download form)
	{
		final String platform = form.getPlatform();
		final String database = form.getDatabase();
		final String encoding = form.getEncoding();

		String filename = platform + "-" + database + "-" + encoding + "-" + build;

		if("WIN".equals(platform))
			filename += ".exe";
		else if("IBM".equals(platform))
		{
			filename = "V" + root.substring(0, 2) + root.substring(3, 4) + "OASSP";
			if("CP37".equals(form.getEncoding()))
				filename = filename + "7";
			else if("UNI".equals(form.getEncoding()))
				filename = filename + "U";
		}
		else
			filename += ".tar.Z";

		return new File(new File(new File(Settings.SERVICEPACKS_FINANCE_DIR, root), "finance-app"), filename);
	}

	private static String getInfo()
	{
		return Utilities.getInfo(Settings.SERVICEPACKS_FINANCE_DIR);
	}
}
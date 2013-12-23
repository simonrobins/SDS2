package helpers;

import java.io.File;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import misc.Download;
import misc.Settings;
import models.AccountAddon;
import models.AccountContact;
import models.Build;
import models.CompanyDownload;
import models.Counter;
import models.Product;
import models.ServicePack;
import models.ShippingOrder;
import models.ShippingOrderProduct;
import models.Version;

import org.eclipse.jdt.annotation.Nullable;

import play.Logger;
import play.db.ebean.Transactional;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

import data.LanguageProduct;
import data.ProductVersion;
import finders.AccountContactFinder;
import finders.BuildFinder;
import finders.CounterFinder;

public class Helpers
{
	@Transactional
	public static String updateShippingOrderLanguagePack(final int accountContactId, final LanguageProduct languageList, final String filename)
	{
		final AccountContact contact = AccountContactFinder.find(accountContactId);

		final String downloadRef = updateCompanyDownload(languageList.getLanguage(), languageList.getProducts(), contact, filename);

		int orderId = updateShippingOrder(contact, downloadRef);

		int productOrderId = ShippingOrderProduct.getNextOne(languageList.getProducts().size());

		final Product language = Product.find(languageList.languageId);

		for (final ProductVersion ids : languageList.getProducts())
		{
			final Product product = Product.find(ids.productId);
			final Version version = Version.find(ids.versionId);
			final ShippingOrderProduct sop = new ShippingOrderProduct();

			sop.setId(productOrderId++);
			sop.setOrderId(orderId);
			sop.setAccountContact(contact);
			sop.setProduct(product);
			sop.setVersion(version);
			sop.setChangeUid(Settings.USER_IDENTIFIER);

			sop.setShippingOptions(4);

			sop.setType(language.getProduct().trim() + " - " + product.getProduct().trim());

			Logger.debug("SOP=" + sop);

			Ebean.save(sop);
		}

		return downloadRef;
	}

	public static String updateShippingOrderReleases(final int accountContactId, final Set<ProductVersion> products, final File file)
	{
		final AccountContact contact = AccountContactFinder.find(accountContactId);

		final String downloadRef = updateCompanyDownload(products, contact, file);

		final int orderId = updateShippingOrder(contact, downloadRef);

		int productOrderId = ShippingOrderProduct.getNextOne(products.size());

		for (final ProductVersion ids : products)
		{
			final Product product = Product.find(ids.productId);
			final Version version = Version.find(ids.versionId);
			final Build build = BuildFinder.find(ids.buildId);
			final ShippingOrderProduct sop = new ShippingOrderProduct();

			sop.setId(productOrderId++);
			sop.setOrderId(orderId);
			sop.setAccountContact(contact);
			sop.setProduct(product);
			sop.setVersion(version);
			sop.setBuild(build);
			sop.setChangeUid(Settings.USER_IDENTIFIER);

			sop.setShippingOptions(1);

			String type = product.getProduct().trim() + " - " + version.getVersion().trim();
			if (build != null && build.getBuild() != null)
				type += " - " + build.getBuild().trim();
			sop.setType(type);

			Logger.debug(downloadRef + " " + product + " - " + version);

			Ebean.save(sop);
		}

		return downloadRef;
	}

	public static String updateShippingOrderServicePacks(final int accountContactId, final ServicePack servicepack, final String filename)
	{
		final Set<ProductVersion> products = new LinkedHashSet<ProductVersion>();
		products.add(new ProductVersion(servicepack.getProduct().getId(), servicepack.getVersion().getId()));

		final Download df = new Download();
		df.setServicepack(servicepack.getServicePackNumber());

		return updateShippingOrderServicePacks(accountContactId, products, df, filename);
	}

	@Transactional
	public static String updateShippingOrderServicePacks(final int accountContactId, final Set<ProductVersion> products, final Download df, final String filename)
	{
		final AccountContact contact = AccountContactFinder.find(accountContactId);

		final String downloadRef = updateCompanyDownload(products, contact, df, filename);

		final int orderId = updateShippingOrder(contact, downloadRef);

		int productOrderId = ShippingOrderProduct.getNextOne(products.size());

		for (final ProductVersion ids : products)
		{
			final Product product = Product.find(ids.productId);
			final Version version = Version.find(ids.versionId);
			final ShippingOrderProduct sop = new ShippingOrderProduct();

			sop.setId(productOrderId++);
			sop.setOrderId(orderId);
			sop.setAccountContact(contact);
			sop.setProduct(product);
			sop.setVersion(version);
			sop.setChangeUid(Settings.USER_IDENTIFIER);

			sop.setShippingOptions(5);

			final ServicePack sp = getServicePack(product, version, df.getServicepack());

			sop.setType(product.getProduct().trim() + " " + version.getVersion().trim());
			sop.setServicePack(sp);
			sop.setType(sop.getType() + " SP" + sp.getServicePackNumber());

			Ebean.save(sop);
		}

		return downloadRef;
	}

	private static int updateShippingOrder(final AccountContact contact, final String downloadRef)
	{
		final int orderId = ShippingOrder.getNextOne(1);

		final ShippingOrder so = new ShippingOrder();
		so.setId(orderId);
		so.setAccount(contact.getAccount());
		so.setContact(contact);
		so.setOrderNo(downloadRef);
		so.setChangeUid(Settings.USER_IDENTIFIER);
		Ebean.save(so);

		return orderId;
	}

	private static String updateCompanyDownload(final Set<ProductVersion> products, final AccountContact contact, final File file)
	{
		return updateCompanyDownload(null, products, contact, null);
	}

	private static String updateCompanyDownload(final @Nullable Product language, final Set<ProductVersion> products, final AccountContact contact, final @Nullable String filename)
	{
		return _updateCompanyDownload(language, products, contact, null, null);
	}

	private static String updateCompanyDownload(final Set<ProductVersion> products, final AccountContact contact, final Download df, final String filename)
	{
		return _updateCompanyDownload(null, products, contact, df, filename);
	}

	private static String _updateCompanyDownload(final @Nullable Product language, final Set<ProductVersion> products, final AccountContact contact, final @Nullable Download df, final @Nullable String filename)
	{
		int downloadId = CompanyDownload.getNextOne(products.size());
		final String downloadRef = "SDS" + Settings.REFERENCE_FORMAT.format(downloadId);

		for (final ProductVersion ids : products)
		{
			final Product product = Product.find(ids.productId);
			final Version version = Version.find(ids.versionId);
			final CompanyDownload cd = new CompanyDownload();

			cd.setId(downloadId++);
			cd.setProduct(product);
			if (language != null)
				cd.setSubProductId(language.getId());
			if (df != null)
			{
				cd.setServerOs(df.getPlatform());
				cd.setDatabaseType(df.getDatabase());
				cd.setEncoding(df.getEncoding());
			}
			cd.setVersion(version);
			cd.setAccount(contact.getAccount());
			cd.setAccountContact(contact);
			cd.setStartDate(new Date());
			cd.setDownloadRef(downloadRef);
			cd.setType(filename);

			if (df != null)
			{
				final ServicePack sp = getServicePack(product, version, df.getServicepack());
				cd.setServicePack(sp);
			}

			Ebean.save(cd);
		}

		return downloadRef;
	}

	@Transactional
	public static int getNextOne(final String table, final String id, int reserve)
	{
		String item = table + "." + id;
		Counter counter = CounterFinder.find(item);

		int lastOne = counter.getLastOne();
		counter.setLastOne(lastOne + reserve);

		Ebean.save(counter);

		return lastOne + 1;
	}

	public static AccountAddon getAccountAddon(final int accountId, final String version)
	{
		final String sql = "SELECT TOP 1 database_type,server_os,encoding FROM coda_company_download cd join coda_service_pack_product spp on spp.product_id = cd.product_id join coda_version v on v.version_id = cd.version_id where account_id = :accountId and version = :version order by download_dt desc";

		final SqlRow addon = Ebean.createSqlQuery(sql).setParameter("accountId", accountId).setParameter("version", version).findUnique();

		if (addon == null)
			return new AccountAddon("", "", "");
		else
			return new AccountAddon(addon.getString("server_os"), addon.getString("database_type"), addon.getString("encoding"));
	}

	private static ServicePack getServicePack(final Product product, final Version version, final int servicePackNumber)
	{
		ServicePack sp = Ebean.find(ServicePack.class).where().eq("product_id", product.getId()).eq("version_id", version.getId()).eq("servicePackNumber", servicePackNumber).findUnique();

		return sp;
	}
}

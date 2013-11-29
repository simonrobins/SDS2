package helpers;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import models.AccountAddon;
import models.ServicePack;

import org.fest.assertions.Assertions;
import org.junit.Test;

import controllers.AbstractTest;
import controllers.Databases;
import data.LanguageProduct;
import data.ProductVersion;
import finders.ServicePackFinder;

public class HelpersTest extends AbstractTest
{
	@Test
	public void testUpdateShippingOrderLanguagePack()
	{
		deleteCounter();
		deleteCompanyDownload();

		insert("counter", "item, last_one", "'coda_company_download.download_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order.order_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order_product.product_order_id', 0");

		Set<ProductVersion> set = new HashSet<ProductVersion>();
		set.add(new ProductVersion(163, 39));
		set.add(new ProductVersion(180, 39));
		LanguageProduct lp = new LanguageProduct("german", 131, set);
		String result = Helpers.updateShippingOrderLanguagePack(41724, lp, "abcd.txt");
		Assertions.assertThat(result).isEqualTo("SDS00000001");
		Assertions.assertThat(count(Databases.COUNTER)).isEqualTo(3);
		Assertions.assertThat(count(Databases.COMPANY_DOWNLOAD)).isEqualTo(2);
		Assertions.assertThat(count(Databases.SHIPPING_ORDER)).isEqualTo(1);
		Assertions.assertThat(count(Databases.SHIPPING_ORDER_PRODUCT)).isEqualTo(2);

		assertCount(Databases.COUNTER, "ITEM = 'coda_company_download.download_id' AND LAST_ONE = 2");
		assertCount(Databases.COUNTER, "ITEM = 'coda_shipping_order.order_id' AND LAST_ONE = 1");
		assertCount(Databases.COUNTER, "ITEM = 'coda_shipping_order_product.product_order_id' AND LAST_ONE = 2");

		assertCount(Databases.COMPANY_DOWNLOAD, "DOWNLOAD_ID  = 1 and TYPE IS NULL AND PRODUCT_ID = 163 AND SUB_PRODUCT_ID = 131 AND VERSION_ID = 39 AND ACCOUNT_ID = 999999 AND ACCOUNT_CONTACT_ID = 41724 AND DATABASE_TYPE IS NULL AND SERVER_OS IS NULL AND ENCODING IS NULL AND SUCCESS IS NULL AND DOWNLOAD_DT IS NOT NULL AND END_DT IS NULL AND BYTES_SENT = 0 AND DOWNLOAD_REF = 'SDS00000001' AND SERVICE_PACK_ID  IS NULL");
		assertCount(Databases.COMPANY_DOWNLOAD, "DOWNLOAD_ID  = 2 and TYPE IS NULL AND PRODUCT_ID = 180 AND SUB_PRODUCT_ID = 131 AND VERSION_ID = 39 AND ACCOUNT_ID = 999999 AND ACCOUNT_CONTACT_ID = 41724 AND DATABASE_TYPE IS NULL AND SERVER_OS IS NULL AND ENCODING IS NULL AND SUCCESS IS NULL AND DOWNLOAD_DT IS NOT NULL AND END_DT IS NULL AND BYTES_SENT = 0 AND DOWNLOAD_REF = 'SDS00000001' AND SERVICE_PACK_ID  IS NULL");

		assertCount(Databases.SHIPPING_ORDER, "order_id = 1 AND account_id = 999999 AND account_contact_id = 41724 AND order_no = 'SDS00000001' AND change_uid = 'DOWNLOAD'");

		assertCount(Databases.SHIPPING_ORDER_PRODUCT, "PRODUCT_ORDER_ID = 1 AND ORDER_ID = 1 AND ACCOUNT_CONTACT_ID = 41724 AND BUILD_ID IS NULL AND SERVICE_PACK_ID IS NULL AND CHANGE_DT IS NOT NULL AND CHANGE_UID = 'DOWNLOAD' AND SHIPPING_OPTIONS = 4");
		assertCount(Databases.SHIPPING_ORDER_PRODUCT, "PRODUCT_ORDER_ID = 2 AND ORDER_ID = 1 AND ACCOUNT_CONTACT_ID = 41724 AND BUILD_ID IS NULL AND SERVICE_PACK_ID IS NULL AND CHANGE_DT IS NOT NULL AND CHANGE_UID = 'DOWNLOAD' AND SHIPPING_OPTIONS = 4");
	}

	@Test
	public void testUpdateShippingOrderReleases()
	{
		deleteCounter();
		deleteCompanyDownload();

		insert("counter", "item, last_one", "'coda_company_download.download_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order.order_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order_product.product_order_id', 0");

		Set<ProductVersion> products = new HashSet<ProductVersion>();
		products.add(new ProductVersion(163, 39));
		products.add(new ProductVersion(180, 39));
		String result = Helpers.updateShippingOrderReleases(41724, products, new File("text.txt"));
		Assertions.assertThat(result).isEqualTo("SDS00000001");
		Assertions.assertThat(count(Databases.COUNTER)).isEqualTo(3);
		Assertions.assertThat(count(Databases.COMPANY_DOWNLOAD)).isEqualTo(2);
		Assertions.assertThat(count(Databases.SHIPPING_ORDER)).isEqualTo(1);
		Assertions.assertThat(count(Databases.SHIPPING_ORDER_PRODUCT)).isEqualTo(2);
	}

	@Test
	public void testUpdateShippingOrderReleasesWithBuildNumber()
	{
		deleteCounter();
		deleteCompanyDownload();

		insert("counter", "item, last_one", "'coda_company_download.download_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order.order_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order_product.product_order_id', 0");

		Set<ProductVersion> products = new HashSet<ProductVersion>();
		products.add(new ProductVersion(163, 39, 939));
		products.add(new ProductVersion(180, 39, 940));
		String result = Helpers.updateShippingOrderReleases(41724, products, new File("text.txt"));
		Assertions.assertThat(result).isEqualTo("SDS00000001");
		Assertions.assertThat(count(Databases.COUNTER)).isEqualTo(3);
		Assertions.assertThat(count(Databases.COMPANY_DOWNLOAD)).isEqualTo(2);
		Assertions.assertThat(count(Databases.SHIPPING_ORDER)).isEqualTo(1);
		Assertions.assertThat(count(Databases.SHIPPING_ORDER_PRODUCT)).isEqualTo(2);
	}

	@Test
	public void testUpdateShippingOrderReleasesWithInvalidBuildNumber()
	{
		deleteCounter();
		deleteCompanyDownload();

		insert("counter", "item, last_one", "'coda_company_download.download_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order.order_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order_product.product_order_id', 0");

		Set<ProductVersion> products = new HashSet<ProductVersion>();
		products.add(new ProductVersion(163, 39, 941));
		products.add(new ProductVersion(180, 39, 942));
		String result = Helpers.updateShippingOrderReleases(41724, products, new File("text.txt"));
		Assertions.assertThat(result).isEqualTo("SDS00000001");
		Assertions.assertThat(count(Databases.COUNTER)).isEqualTo(3);
		Assertions.assertThat(count(Databases.COMPANY_DOWNLOAD)).isEqualTo(2);
		Assertions.assertThat(count(Databases.SHIPPING_ORDER)).isEqualTo(1);
		Assertions.assertThat(count(Databases.SHIPPING_ORDER_PRODUCT)).isEqualTo(2);
	}

	@Test
	public void testUpdateShippingOrderServicePacks()
	{
		deleteCounter();
		deleteCompanyDownload();

		insert("counter", "item, last_one", "'coda_company_download.download_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order.order_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order_product.product_order_id', 0");

		ServicePack sp = ServicePackFinder.find(14);
		String result = Helpers.updateShippingOrderServicePacks(41724, sp, "text.txt");
		Assertions.assertThat(result).isEqualTo("SDS00000001");
		Assertions.assertThat(count(Databases.COUNTER)).isEqualTo(3);
		Assertions.assertThat(count(Databases.COMPANY_DOWNLOAD)).isEqualTo(1);
		Assertions.assertThat(count(Databases.SHIPPING_ORDER)).isEqualTo(1);
		Assertions.assertThat(count(Databases.SHIPPING_ORDER_PRODUCT)).isEqualTo(1);
	}

	@Test(expected = NullPointerException.class)
	public void testGetLastOneWithMissingTableAndId()
	{
		Helpers.getNextOne("missing", "missing", 0);
	}

	@Test
	public void testGetNextOne()
	{
		insert("counter", "item, last_one", "'coda_product.product_id', 0");

		int nextOne = Helpers.getNextOne("coda_product", "product_id", 1);
		Assertions.assertThat(nextOne).isEqualTo(1);

		assertCount("counter", "item = 'coda_product.product_id' AND last_one = 1");
	}

	@Test
	public void testGetNextOneWithIncrementOfTen()
	{
		insert("counter", "item, last_one", "'coda_product.product_id', 0");

		int nextOne = Helpers.getNextOne("coda_product", "product_id", 10);
		Assertions.assertThat(nextOne).isEqualTo(1);

		assertCount("counter", "item = 'coda_product.product_id' AND last_one = 10");
	}

	@Test
	public void testGetAccountAddonNotFound()
	{
		AccountAddon addon = Helpers.getAccountAddon(41724, "");
		Assertions.assertThat(addon.databaseType).isEqualTo("");
		Assertions.assertThat(addon.encoding).isEqualTo("");
		Assertions.assertThat(addon.serverOs).isEqualTo("");
	}

	@Test
	public void testGetAccountAddon()
	{
		insert("coda_company_download", "download_id, account_id, product_id, version_id, database_type, server_os, encoding", "1, 1, 12, 325, 'database', 'server', 'encoding'");

		AccountAddon addon = Helpers.getAccountAddon(1, "12.000          ");
		Assertions.assertThat(addon.databaseType).isEqualTo("database");
		Assertions.assertThat(addon.encoding).isEqualTo("encoding");
		Assertions.assertThat(addon.serverOs).isEqualTo("server");
	}

	private void assertCount(String table, String sql)
	{
		assertCount(table, sql, 1L);
	}

	private void assertCount(String table, String sql, Long count)
	{
		Assertions.assertThat(count(table, sql)).isEqualTo(count);
	}
}

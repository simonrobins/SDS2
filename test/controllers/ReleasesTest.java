package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.status;

import org.junit.Test;

import play.mvc.Result;

public class ReleasesTest extends AbstractTest
{
	@Test
	public void testIndex()
	{
		Result result = get("/releases");
		assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testDownload()
	{
		deleteCounter();
		deleteCompanyDownload();
		deleteShippingOrder();
		deleteShippingOrderProduct();

		insert("counter", "item, last_one", "'coda_company_download.download_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order.order_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order_product.product_order_id', 0");

		Result result = get("/releases/Coda_Financials/12.000/DestinationControl_DVD1_12.000.670.iso");
		assertThat(status(result)).isEqualTo(SEE_OTHER);

		assertCount("counter", "item = 'coda_shipping_order.order_id' AND last_one = 1");
		assertCount("counter", "item = 'coda_shipping_order_product.product_order_id' AND last_one = 20");
		assertCount("counter", "item = 'coda_company_download.download_id' AND last_one = 20");

		assertCount("coda_shipping_order", 1L);
		assertCount("coda_shipping_order_product", 20L);
		assertCount("coda_company_download", 20L);
	}

	@Test
	public void testRedirectNoDownloadRef()
	{
		deleteCounter();
		deleteCompanyDownload();
		deleteShippingOrder();
		deleteShippingOrderProduct();

		Result result = get("/releases/redirect/Coda%20Financials/12.000/DestinationControl_DVD1_12.000.670.iso/SDS00000001");
		assertThat(status(result)).isEqualTo(SEE_OTHER);

		assertCount("counter", "item = 'coda_shipping_order.order_id'", 0L);
		assertCount("counter", "item = 'coda_shipping_order_product.product_order_id'", 0L);
		assertCount("counter", "item = 'coda_company_download.download_id'", 0L);

		assertCount("coda_shipping_order", 0L);
		assertCount("coda_shipping_order_product", 0L);
		assertCount("coda_company_download", 0L);
	}
}

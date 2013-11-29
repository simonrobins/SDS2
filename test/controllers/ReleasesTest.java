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

		insert("counter", "item, last_one", "'coda_company_download.download_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order.order_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order_product.product_order_id', 0");

		Result result = get("/releases/Coda%20Financials/12.000/DestinationControl_DVD1_12.000.670.iso");
		assertThat(status(result)).isEqualTo(SEE_OTHER);
	}

	@Test
	public void testRedirectNoDownloadRef()
	{
		Result result = get("/releases/redirect/Coda%20Financials/12.000/DestinationControl_DVD1_12.000.670.iso/SDS00000001");
		assertThat(status(result)).isEqualTo(SEE_OTHER);
	}
}

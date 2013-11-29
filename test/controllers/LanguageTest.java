package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.status;

import org.junit.Test;

import play.mvc.Result;

public class LanguageTest extends AbstractTest
{
	@Test
	public void testIndex()
	{
		Result result = get("/language");
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

		Result result = get("/language/Coda%20Financials/11.300/Base%20Release/German/AdministrationConsole_V11300304_GermanLanguage.zip");
		assertThat(status(result)).isEqualTo(SEE_OTHER);
	}

	@Test
	public void testRedirect()
	{
		Result result = get("/language/redirect/Coda%20Financials/11.300/Base%20Release/German/AdministrationConsole_V11300304_GermanLanguage.zip");
		assertThat(status(result)).isEqualTo(SEE_OTHER);
	}
}

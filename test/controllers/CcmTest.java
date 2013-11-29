package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.header;
import static play.test.Helpers.status;

import org.junit.Test;

import play.mvc.Result;

public class CcmTest extends AbstractTest
{
	@Test
	public void testCcm()
	{
		Result result = get("/ccm");
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

		Result result = get("/ccm/CCM5.0-SP5.zip");
		assertThat(status(result)).isEqualTo(SEE_OTHER);
	}

	@Test
	public void testDownloadMissingFile()
	{
		Result result = get("/ccm/CCM5.0-SP4.zip");
		assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testStreamNoRefFound()
	{
		Result result = get("/ccm/stream/CCM5.0-SP4.zip/SDS00000001");
		assertThat(status(result)).isEqualTo(SEE_OTHER);
		assertThat(header("X-Accel-Redirect", result)).isNull();
		assertThat(header("Content-Disposition", result)).isNull();
	}

	@Test
	public void testStreamRefFoundNoFile()
	{
		insert(Databases.COMPANY_DOWNLOAD, "download_id, account_id, download_ref, bytes_sent", "1, 999999, 'SDS00000001', 0");
		Result result = get("/ccm/stream/CCM5.0-SP4.zip/SDS00000001");
		assertThat(status(result)).isEqualTo(OK);
		assertThat(header("X-Accel-Redirect", result)).isNull();
		assertThat(header("Content-Disposition", result)).isNull();
	}

	@Test
	public void testStreamRefFoundFileFound()
	{
		insert(Databases.COMPANY_DOWNLOAD, "download_id, account_id, download_ref, bytes_sent", "1, 999999, 'SDS00000001', 0");
		Result result = get("/ccm/stream/CCM5.0-SP5.zip/SDS00000001");
		assertThat(status(result)).isEqualTo(OK);
		assertThat(header("X-Accel-Redirect", result)).isEqualTo("/ccm/internal/CCM5.0-SP5.zip");
		assertThat(header("Content-Disposition", result)).isEqualTo("attachment; filename=CCM5.0-SP5.zip");
	}

	@Test
	public void testInternal()
	{
		Result result = get("/ccm/internal/CCM5.0-SP4.zip");
		assertThat(status(result)).isEqualTo(OK);
	}
}

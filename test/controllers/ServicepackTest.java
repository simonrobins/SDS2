package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.redirectLocation;
import static play.test.Helpers.status;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import play.mvc.Result;

public class ServicepackTest extends AbstractTest
{
	@Test
	public void testIndex()
	{
		Result result = get("/servicepack");
		assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testFinance()
	{
		Result result = get("/servicepack/12.000");
		assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testWin()
	{
		deleteCounter();
		deleteCompanyDownload();

		insert("counter", "item, last_one", "'coda_company_download.download_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order.order_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order_product.product_order_id', 0");

		final Map<String, String> data = new LinkedHashMap<String, String>();
		data.put("version", "12.000");
		data.put("servicepack", "7");
data.put("platform", "WIN");
		data.put("database", "ORA");
		data.put("encoding", "ASC");
		data.put("assets", "true");
		data.put("billing", "true");
		data.put("customiser", "true");
		data.put("finance", "true");
		data.put("pim", "true");
		data.put("pop", "true");
		data.put("fin", "true");

		final Result result = post("/servicepack/download", data);
		assertThat(status(result)).isEqualTo(SEE_OTHER);
		assertThat(redirectLocation(result)).isEqualTo("/servicepack/stream/12.000-SP7-WIN-ORA-ASC-16383.zip/SDS00000001/477");
		assertThat(count(Databases.COMPANY_DOWNLOAD)).isEqualTo(8);
	}

	@Test
	public void testIbm1()
	{
		deleteCounter();
		deleteCompanyDownload();

		insert("counter", "item, last_one", "'coda_company_download.download_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order.order_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order_product.product_order_id', 0");

		final Map<String, String> data = new LinkedHashMap<String, String>();
		data.put("version", "12.000");
		data.put("servicepack", "7");
		data.put("platform", "IBM");
		data.put("database", "ORA");
		data.put("encoding", "ASC");
		data.put("assets", "true");
		data.put("billing", "true");
		data.put("customiser", "true");
		data.put("finance", "true");
		data.put("pim", "true");
		data.put("pop", "true");
		data.put("fin", "true");

		final Result result = post("/servicepack/download", data);
		assertThat(status(result)).isEqualTo(SEE_OTHER);
		assertThat(redirectLocation(result)).startsWith("/servicepack/stream/12.000-SP7-IBM-ORA-ASC-16383.zip");
		assertThat(count(Databases.COMPANY_DOWNLOAD)).isEqualTo(8);
	}

	@Test
	public void testIbm2()
	{
		deleteCounter();
		deleteCompanyDownload();

		insert("counter", "item, last_one", "'coda_company_download.download_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order.order_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order_product.product_order_id', 0");

		final Map<String, String> data = new LinkedHashMap<String, String>();
		data.put("version", "12.000");
		data.put("servicepack", "7");
		data.put("platform", "IBM");
		data.put("database", "ORA");
		data.put("encoding", "UNI");
		data.put("assets", "true");
		data.put("billing", "true");
		data.put("customiser", "true");
		data.put("finance", "true");
		data.put("pim", "true");
		data.put("pop", "true");
		data.put("fin", "true");

		final Result result = post("/servicepack/download", data);
		assertThat(status(result)).isEqualTo(SEE_OTHER);
		assertThat(redirectLocation(result)).startsWith("/servicepack/stream/12.000-SP7-IBM-ORA-UNI-16383.zip");
		assertThat(count(Databases.COMPANY_DOWNLOAD)).isEqualTo(8);
	}

	@Test
	public void testIbm3()
	{
		deleteCounter();
		deleteCompanyDownload();

		insert("counter", "item, last_one", "'coda_company_download.download_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order.order_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order_product.product_order_id', 0");

		final Map<String, String> data = new LinkedHashMap<String, String>();
		data.put("version", "11.300");
		data.put("servicepack", "6");
		data.put("platform", "IBM");
		data.put("database", "ORA");
		data.put("encoding", "CP37");
		data.put("assets", "true");
		data.put("billing", "true");
		data.put("customiser", "false");
		data.put("finance", "true");
		data.put("pim", "true");
		data.put("pop", "true");
		data.put("fin", "true");

		final Result result = post("/servicepack/download", data);
		assertThat(status(result)).isEqualTo(SEE_OTHER);
		assertThat(redirectLocation(result)).isEqualTo("/servicepack/stream/11.300-SP6-IBM-ORA-CP37-16319.zip/SDS00000001/430");
		assertThat(count(Databases.COMPANY_DOWNLOAD)).isEqualTo(7);
	}

	@Test
	public void testHp()
	{
		deleteCounter();
		deleteCompanyDownload();

		insert("counter", "item, last_one", "'coda_company_download.download_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order.order_id', 0");
		insert("counter", "item, last_one", "'coda_shipping_order_product.product_order_id', 0");

		final Map<String, String> data = new LinkedHashMap<String, String>();
		data.put("version", "12.000");
		data.put("servicepack", "7");
		data.put("platform", "HP");
		data.put("database", "ORA");
		data.put("encoding", "ASC");
		data.put("assets", "true");
		data.put("billing", "true");
		data.put("customiser", "true");
		data.put("finance", "true");
		data.put("pim", "true");
		data.put("pop", "true");
		data.put("fin", "true");

		final Result result = post("/servicepack/download", data);
		assertThat(status(result)).isEqualTo(SEE_OTHER);
		assertThat(redirectLocation(result)).startsWith("/servicepack/stream/12.000-SP7-HP-ORA-ASC-16383.zip");
		assertThat(count(Databases.COMPANY_DOWNLOAD)).isEqualTo(8);
	}

	@Test
	public void testDocumentDownload()
	{
		final Map<String, String> data = new LinkedHashMap<String, String>();
		data.put("doc", "");
		data.put("version", "11.300");

		final Result result = post("/servicepack/download", data);
		assertThat(status(result)).isEqualTo(OK);
		assertThat(count(Databases.COMPANY_DOWNLOAD)).isEqualTo(0);
	}

	@Test
	public void testStream()
	{
		insert("coda_company_download", "download_id, type, product_id, version_id, account_id, account_contact_id, database_type, server_os, encoding, bytes_sent, download_ref, service_pack_id", "1, '12.000-SP5-WIN-ORA-ASC-16383.zip', 20, 325, 999999, 41724, 'ORA', 'WIN', 'ASC', 0, 'SDS00000001', 411");
		Result result = get("/servicepack/stream/12.000-SP5-WIN-ORA-ASC-16383.zip/SDS00000001/920113214");
		assertThat(status(result)).isEqualTo(OK);
		assertThat(count(Databases.COMPANY_DOWNLOAD)).isEqualTo(1);
	}

	@Test
	public void testStreaming()
	{
		insert("coda_company_download", "download_id, type, product_id, version_id, account_id, account_contact_id, database_type, server_os, encoding, bytes_sent, download_ref, service_pack_id", "1, '12.000-SP5-WIN-ORA-ASC-16383.zip', 20, 325, 999999, 41724, 'ORA', 'WIN', 'ASC', 0, 'SDS00000001', 411");
		Result result = get("/servicepack/streaming/archive.zip/SDS00000001");
		assertThat(status(result)).isEqualTo(OK);
		assertThat(count(Databases.COMPANY_DOWNLOAD)).isEqualTo(1);
	}

	@Test
	public void testStreamingWithMissingRef()
	{
		Result result = get("/servicepack/streaming/12.000-SP5-WIN-ORA-ASC-16383.zip/SDS00000002");
		assertThat(status(result)).isEqualTo(SEE_OTHER);
		assertThat(count(Databases.COMPANY_DOWNLOAD)).isEqualTo(0);
	}

	@Test
	public void testStreamingWithMissingFile()
	{
		insert("coda_company_download", "download_id, type, product_id, version_id, account_id, account_contact_id, database_type, server_os, encoding, bytes_sent, download_ref, service_pack_id", "1, '12.000-SP5-WIN-ORA-ASC-16383.zip', 20, 325, 999999, 41724, 'ORA', 'WIN', 'ASC', 0, 'SDS00000001', 411");
		Result result = get("/servicepack/streaming/12.000-SP5-WIN-ORA-ASC-16383.zip/SDS00000001");
		assertThat(status(result)).isEqualTo(OK);
		assertThat(count(Databases.COMPANY_DOWNLOAD)).isEqualTo(1);
	}

	@Test
	public void testInternal()
	{
		Result result = get("/servicepack/internal/12.000-SP5-WIN-ORA-ASC-16383.zip");
		assertThat(status(result)).isEqualTo(OK);
		assertThat(count(Databases.COMPANY_DOWNLOAD)).isEqualTo(0);
	}

	@Test
	public void testDoc()
	{
		Result result = get("/servicepack/doc/somewhere/12.000-SP5-WIN-ORA-ASC-16383.zip");
		assertThat(status(result)).isEqualTo(OK);
		assertThat(count(Databases.COMPANY_DOWNLOAD)).isEqualTo(0);
	}
}

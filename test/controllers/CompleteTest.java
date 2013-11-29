package controllers;

import static play.mvc.Http.Status.OK;
import static play.test.Helpers.status;

import java.util.List;
import java.util.Map;

import org.fest.assertions.Assertions;
import org.junit.Test;

import play.mvc.Result;

public class CompleteTest extends AbstractTest
{
	@Test
	public void testComplete()
	{
		insert(Databases.COMPANY_DOWNLOAD, "download_id, account_id, download_ref, bytes_sent", "1, 999999, 'SDS00000001', 0");

		Result result = get("/complete", "/servicepack/streaming/12.000-SP5-WIN-ORA-ASC-16383.zip/SDS00000001");
		Assertions.assertThat(status(result)).isEqualTo(OK);

		List<Map<String, Object>> rows = select(Databases.COMPANY_DOWNLOAD);
		Assertions.assertThat(rows.size()).isEqualTo(1);
		Assertions.assertThat(rows.get(0).get("DOWNLOAD_ID")).isEqualTo(1);
		Assertions.assertThat(rows.get(0).get("SUCCESS")).isEqualTo("Y");
		Assertions.assertThat(rows.get(0).get("ACCOUNT_ID")).isEqualTo(999999);
		Assertions.assertThat(rows.get(0).get("DOWNLOAD_REF")).isEqualTo("SDS00000001");
		Assertions.assertThat(rows.get(0).get("BYTES_SENT")).isEqualTo(9999L);
		Assertions.assertThat(rows.get(0).get("TYPE")).isEqualTo("12.000-SP5-WIN-ORA-ASC-16383.zip");
	}

	@Test
	public void testCompleteReload()
	{
		insert(Databases.COMPANY_DOWNLOAD, "download_id, account_id, download_ref, bytes_sent", "1, 999999, 'SDS00000001', 0");

		Result result = get("/complete", "/servicepack/streaming/12.000-SP5-WIN-ORA-ASC-16383.zip/SDS00000001");
		Assertions.assertThat(status(result)).isEqualTo(OK);

		result = get("/complete", "/servicepack/streaming/12.000-SP5-WIN-ORA-ASC-16383.zip/SDS00000001");
		Assertions.assertThat(status(result)).isEqualTo(OK);

		// List<Map<String, Object>> rows = select(Databases.COMPANY_DOWNLOAD);
		// Assertions.assertThat(rows.size()).isEqualTo(1);
		// Assertions.assertThat(rows.get(0).get("DOWNLOAD_ID")).isEqualTo(1);
		// Assertions.assertThat(rows.get(0).get("SUCCESS")).isEqualTo("Y");
		// Assertions.assertThat(rows.get(0).get("ACCOUNT_ID")).isEqualTo(999999);
		// Assertions.assertThat(rows.get(0).get("DOWNLOAD_REF")).isEqualTo("SDS00000001");
		// Assertions.assertThat(rows.get(0).get("BYTES_SENT")).isEqualTo(9999L);
		// Assertions.assertThat(rows.get(0).get("TYPE")).isEqualTo("12.000-SP5-WIN-ORA-ASC-16383.zip");
	}

	@Test
	public void testCompleteInvalid()
	{
		insert(Databases.COMPANY_DOWNLOAD, "download_id, account_id, download_ref, bytes_sent", "1, 999999, 'SDS00000001', 0");

		Result result = get("/complete", "/servicepack/streaming/12.000-SP5-WIN-ORA-ASC-16383.zip/SDS00000001");
		Assertions.assertThat(status(result)).isEqualTo(OK);

		List<Map<String, Object>> rows = select(Databases.COMPANY_DOWNLOAD);
		Assertions.assertThat(rows.size()).isEqualTo(1);
		Assertions.assertThat(rows.get(0).get("DOWNLOAD_ID")).isEqualTo(1);
		Assertions.assertThat(rows.get(0).get("SUCCESS")).isEqualTo("Y");
		Assertions.assertThat(rows.get(0).get("ACCOUNT_ID")).isEqualTo(999999);
		Assertions.assertThat(rows.get(0).get("DOWNLOAD_REF")).isEqualTo("SDS00000001");
		Assertions.assertThat(rows.get(0).get("BYTES_SENT")).isEqualTo(9999L);
		Assertions.assertThat(rows.get(0).get("TYPE")).isEqualTo("12.000-SP5-WIN-ORA-ASC-16383.zip");
	}
}

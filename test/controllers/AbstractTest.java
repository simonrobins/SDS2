package controllers;

import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.header;
import static play.test.Helpers.route;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import play.db.DB;
import play.mvc.Http.HeaderNames;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.FakeRequest;

public class AbstractTest
{
	protected static FakeApplication	app;
	protected static String				cookies;
	protected static QueryRunner		run;

	public static FakeApplication startApp(Map<String, String> configuration)
	{
		FakeApplication app = fakeApplication(configuration);
		start(app);

		return app;
	}

	@BeforeClass
	public static void startApp()
	{
		app = startApp(new HashMap<String, String>());

		run = new QueryRunner(DB.getDataSource());

		final Result result = callAction(controllers.routes.ref.Secure.restrictedAccess("41724", "1678382192", "3", "3f5f5c105462d19791978cb9a4bd6782"));
		cookies = header(HeaderNames.SET_COOKIE, result);
	}

	@AfterClass
	public static void stopApp()
	{
		stop(app);
	}

	@Before
	public void before()
	{
		deleteCounter();
		deleteCompanyDownload();
		deleteShippingOrder();
		deleteShippingOrderProduct();
	}

	@After
	public void after()
	{
		Assertions.assertThat(count("account")).isEqualTo(1L);
		Assertions.assertThat(count("account_contact")).isEqualTo(1L);
		Assertions.assertThat(count("coda_build")).isEqualTo(896L);
		Assertions.assertThat(count("coda_product")).isEqualTo(196L);
		Assertions.assertThat(count("coda_sds_validate")).isEqualTo(20162L);
		Assertions.assertThat(count("coda_service_pack")).isEqualTo(477L);
		Assertions.assertThat(count("coda_service_pack_product")).isEqualTo(9L);
		Assertions.assertThat(count("coda_sub_product")).isEqualTo(24L);
		Assertions.assertThat(count("coda_version")).isEqualTo(372L);
	}

	protected Result get(final String url)
	{
		return route(fakeRequest("GET", url).withHeader(HeaderNames.COOKIE, cookies).withHeader("USER-AGENT", "MSIE 9.0;"));
	}

	protected Result get(final String url, String requestUri)
	{
		FakeRequest request = fakeRequest("GET", url);
		request.withHeader(HeaderNames.COOKIE, cookies);
		request.withHeader("USER-AGENT", "MSIE 9.0;");
		request.withHeader("X-REQUEST-URI", requestUri);
		request.withHeader("X-REQUEST-COMPLETION", "OK");
		request.withHeader("X-BODY-BYTES-SENT", "9999");
		return route(request);
	}

	protected Result post(final String url, final Map<String, String> data)
	{
		return route(fakeRequest("POST", url).withHeader("USER-AGENT", "MSIE 9.0;").withHeader(HeaderNames.COOKIE, cookies).withFormUrlEncodedBody(data));
	}

	protected Result post(final String url, final String body)
	{
		FakeRequest request = fakeRequest("POST", url).withHeader("USER-AGENT", "MSIE 9.0;").withHeader(HeaderNames.COOKIE, cookies);
		Result result = route(request, body.getBytes());
		contentAsString(result);
		return result;
	}

	protected Long countCounter()
	{
		return count(Databases.COUNTER);
	}

	protected Long countCompanyDownload()
	{
		return count(Databases.COMPANY_DOWNLOAD);
	}

	protected Long countShippingOrder()
	{
		return count(Databases.SHIPPING_ORDER);
	}

	protected Long countShippingOrderProduct()
	{
		return count(Databases.SHIPPING_ORDER_PRODUCT);
	}

	protected Long count(String table)
	{
		return count(table, "1=1");
	}

	protected Long count(String table, String where)
	{
		try
		{
			return run.query("SELECT COUNT(*) FROM " + table + " WHERE " + where, new ScalarHandler<Long>());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	protected List<Map<String, Object>> select(String table)
	{
		try
		{
			return run.query("SELECT * FROM " + table, new MapListHandler());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	protected List<Map<String, Object>> select(String table, String where)
	{
		try
		{
			return run.query("SELECT * FROM " + table + " WHERE " + where, new MapListHandler());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	protected void insert(String table, String fields, String values)
	{
		try
		{
			run.update("INSERT INTO " + table + "(" + fields + ")" + " VALUES (" + values + ")");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	protected void deleteCounter()
	{
		deleteAll(Databases.COUNTER);
	}

	protected void deleteCompanyDownload()
	{
		deleteAll(Databases.COMPANY_DOWNLOAD);
	}

	protected void deleteShippingOrder()
	{
		deleteAll(Databases.SHIPPING_ORDER);
	}

	protected void deleteShippingOrderProduct()
	{
		deleteAll(Databases.SHIPPING_ORDER_PRODUCT);
	}

	private void deleteAll(String table)
	{
		try
		{
			run.update("DELETE FROM " + table);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}

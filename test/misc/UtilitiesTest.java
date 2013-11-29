package misc;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class UtilitiesTest
{
	interface MyCloseable extends Closeable
	{
		boolean getCalled();
	}

	private static FakeRequest request = new FakeRequest();

	@Test
	public void testQuietCloseWithNullStream()
	{
		Utilities.quietClose(null);
		Assertions.assertThat(true).isEqualTo(true);
	}

	@Test
	public void testQuietClose()
	{
		MyCloseable closeable = new MyCloseable()
			{
				private boolean called = false;

				@Override
				public void close()
					throws IOException
				{
					called = true;
				}

				@Override
				public boolean getCalled()
				{
					return called;
				}
			};

		Utilities.quietClose(closeable);
		Assertions.assertThat(closeable.getCalled()).isEqualTo(true);
	}

	@Test
	public void testQuietCloseIOException()
	{
		MyCloseable closeable = new MyCloseable()
			{
				private boolean called = false;

				@Override
				public void close()
					throws IOException
				{
					called = true;
					throw new IOException();
				}

				@Override
				public boolean getCalled()
				{
					return called;
				}
			};

		Utilities.quietClose(closeable);
		Assertions.assertThat(closeable.getCalled()).isEqualTo(true);
	}

	@Test
	public void testHeaderIsNull()
	{
		request.setHeaders(new HashMap<String, String[]>());
		Double version = Utilities.getInternetExplorerVersion(request);
		Assertions.assertThat(version).isEqualTo(0.0);
	}

	@Test
	public void testUserAgentHeaderIsEmpty()
	{
		Map<String, String[]> ua = new HashMap<String, String[]>();
		ua.put("User-Agent", new String[0]);
		request.setHeaders(ua);
		Double version = Utilities.getInternetExplorerVersion(request);
		Assertions.assertThat(version).isEqualTo(0.0);
	}

	@Test
	public void testUserAgentHeaderIsNotEmpty()
	{
		Map<String, String[]> ua = new HashMap<String, String[]>();
		ua.put("User-Agent", new String[]
			{
				"HELLO"
			});
		request.setHeaders(ua);
		Double version = Utilities.getInternetExplorerVersion(request);
		Assertions.assertThat(version).isEqualTo(0.0);
	}

	@Test
	public void testUserAgentHeaderContainsMSIE()
	{
		Map<String, String[]> ua = new HashMap<String, String[]>();
		ua.put("User-Agent", new String[]
			{
				"MSIE 99.99;"
			});
		request.setHeaders(ua);
		Double version = Utilities.getInternetExplorerVersion(request);
		Assertions.assertThat(version).isEqualTo(99.99);
	}
}

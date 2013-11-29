package tags;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import controllers.AbstractTest;

public class ListsTest extends AbstractTest
{
	@Test
	public void testInit()
	{
		assertThat(new Lists()).isNotNull();
	}

	@Test
	public void testGetVersions()
	{
		Map<String, List<Integer>> versions = Lists.getVersions();
		assertThat(versions).isNotNull();
	}

	@Test
	public void testGetVersion112()
	{
		List<Integer> versions = Lists.getVersions("11.200");
		assertThat(versions).isNotNull();
	}

	@Test
	public void testGetVersion113()
	{
		List<Integer> versions = Lists.getVersions("11.300");
		assertThat(versions).isNotNull();
	}

	@Test
	public void testGetVersion120()
	{
		List<Integer> versions = Lists.getVersions("12.000");
		assertThat(versions).isNotNull();
	}

	@Test
	public void testGetMissingVersion()
	{
		List<Integer> versions = Lists.getVersions("10.000");
		assertThat(versions).isNotNull();
	}
}

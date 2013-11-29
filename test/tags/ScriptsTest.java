package tags;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import controllers.AbstractTest;

public class ScriptsTest extends AbstractTest
{
	@Test
	public void testInit()
	{
		assertThat(new Scripts()).isNotNull();
	}

	@Test
	public void testServicepacks()
	{
		String result = Scripts.servicepacks();
		assertThat(result).isNotNull();
	}

	@Test
	public void testPlatforms()
	{
		String result = Scripts.platforms();
		assertThat(result).isNotNull();
	}

	@Test
	public void testDatabases()
	{
		String result = Scripts.databases();
		assertThat(result).isNotNull();
	}

	@Test
	public void testEncodings()
	{
		String result = Scripts.encodings();
		assertThat(result).isNotNull();
	}
}

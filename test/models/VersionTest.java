package models;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class VersionTest
{
	@Test
	public void testToString()
	{
		Version v = new Version();
		Assertions.assertThat(v.toString()).isEqualTo("ERROR[0]");
	}
}

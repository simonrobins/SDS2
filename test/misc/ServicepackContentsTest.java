package misc;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class ServicepackContentsTest
{
	@Test
	public void testNewSettings()
	{
		Assertions.assertThat(new ServicepackContents()).isNotNull();
	}
}

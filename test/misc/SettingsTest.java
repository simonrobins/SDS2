package misc;

import org.fest.assertions.Assertions;
import org.junit.Test;

import controllers.AbstractTest;

public class SettingsTest  extends AbstractTest
{
	@Test
	public void testNewSettings()
	{
		Assertions.assertThat(new Settings()).isNotNull();
	}
}

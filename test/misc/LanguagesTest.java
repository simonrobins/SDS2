package misc;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class LanguagesTest
{
	@Test
	public void testNewSettings()
	{
		Assertions.assertThat(new Languages()).isNotNull();
	}
}

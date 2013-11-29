package data;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class LanguageMapTest
{
	@Test
	public void testNewLanguageMap()
	{
		start(fakeApplication());

		Assertions.assertThat(new LanguageMap()).isNotNull();
	}

	@Test
	public void testGetLanguageMap()
	{
		start(fakeApplication());

		LanguageNode node = LanguageMap.getLanguageMap();
		Assertions.assertThat(node).isNotNull();
	}
}

package models;

import org.fest.assertions.Assertions;
import org.junit.Test;

import controllers.AbstractTest;

public class SubProductTest extends AbstractTest
{
	@Test
	public void testIdIsNull()
	{
		SubProduct sp = new SubProduct();

		Assertions.assertThat(sp).isNotNull();
		Assertions.assertThat(sp.getId()).isNull();
	}

	@Test
	public void testIdIsNotNull()
	{
		SubProduct sp = new SubProduct();
		sp.setId(99);

		Assertions.assertThat(sp).isNotNull();
		Assertions.assertThat(sp.getId()).isNotNull();
		Assertions.assertThat(sp.getId()).isEqualTo(99);
	}
}

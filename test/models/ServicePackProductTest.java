package models;

import org.fest.assertions.Assertions;
import org.junit.Test;

import controllers.AbstractTest;

public class ServicePackProductTest extends AbstractTest
{
	@Test
	public void testIdIsNull()
	{
		ServicePackProduct spp = new ServicePackProduct();

		Assertions.assertThat(spp).isNotNull();
		Assertions.assertThat(spp.getId()).isEqualTo(0);
	}

	@Test
	public void testIdIsNotNull()
	{
		ServicePackProduct spp = new ServicePackProduct();
		spp.setId(99);

		Assertions.assertThat(spp).isNotNull();
		Assertions.assertThat(spp.getId()).isNotNull();
		Assertions.assertThat(spp.getId()).isEqualTo(99);
	}
}

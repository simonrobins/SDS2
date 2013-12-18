package models;

import java.util.Date;

import org.fest.assertions.Assertions;
import org.junit.Test;

import controllers.AbstractTest;

public class ShippingOrderProductTest extends AbstractTest
{
	@Test
	public void testNull()
	{
		ShippingOrderProduct sop = new ShippingOrderProduct();

		Assertions.assertThat(sop).isNotNull();
		Assertions.assertThat(sop.getUnicode()).isEqualTo('\0');
		Assertions.assertThat(sop.getChangeDt()).isNotNull();
	}

	@Test
	public void testUnicodeIsNotNull()
	{
		ShippingOrderProduct sop = new ShippingOrderProduct();
		sop.setUnicode('1');

		Assertions.assertThat(sop).isNotNull();
		Assertions.assertThat(sop.getUnicode()).isEqualTo('1');
	}

	@Test
	public void testChangeDtIsNotNull()
	{
		ShippingOrderProduct sop = new ShippingOrderProduct();
		sop.setChangeDt(new Date());

		Assertions.assertThat(sop).isNotNull();
		Assertions.assertThat(sop.getChangeDt()).isNotNull();
	}
}

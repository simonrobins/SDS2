package models;

import org.fest.assertions.Assertions;
import org.junit.Test;

import controllers.AbstractTest;

;

public class ProductTest extends AbstractTest
{
	@Test
	public void testFind()
	{
		Assertions.assertThat(Product.find(1)).isNotNull();
	}

	@Test
	public void testFindNegativeId()
	{
		Assertions.assertThat(Product.find(-1)).isNotNull();
	}

	@Test
	public void testToString()
	{
		Product product = Product.find(1);
		Assertions.assertThat(product).isNotNull();
		Assertions.assertThat(product.toString()).isEqualTo("OCS-ADD ONS[1]");
	}

	@Test
	public void testToStringWithNullProduct()
	{
		Product product = Product.find(203);
		Assertions.assertThat(product).isNotNull();
		Assertions.assertThat(product.toString()).isEqualTo("ERROR[203]");
	}

	@Test
	public void testGetId()
	{
		Product product = Product.find(1);
		Assertions.assertThat(product).isNotNull();
		Assertions.assertThat(product.getId()).isEqualTo(1);
	}
}

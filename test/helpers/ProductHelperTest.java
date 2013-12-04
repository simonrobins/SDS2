package helpers;

import java.util.HashSet;
import java.util.Set;

import org.fest.assertions.Assertions;
import org.junit.Test;

import data.ProductVersion;

public class ProductHelperTest
{
	@Test
	public void testNewSettings()
	{
		Assertions.assertThat(new ProductHelper()).isNotNull();
	}

	@Test
	public void testEmptyValidateProductIds()
	{
		Set<ProductVersion> products = new HashSet<ProductVersion>();
		
		Assertions.assertThat(ProductHelper.validateProductIds(products)).isNotNull();
	}
}

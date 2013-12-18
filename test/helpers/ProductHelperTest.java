package helpers;

import java.util.HashSet;
import java.util.Set;

import org.fest.assertions.Assertions;
import org.junit.Test;

import controllers.AbstractTest;
import data.ProductVersion;

public class ProductHelperTest extends AbstractTest
{
	@Test
	public void testNewSettings()
	{
		Assertions.assertThat(new ProductHelper()).isNotNull();
	}

	@Test
	public void testEmptyProductIds()
	{
		Set<ProductVersion> products = new HashSet<ProductVersion>();
		Assertions.assertThat(ProductHelper.validateProductIds(products)).isTrue();
	}

	@Test
	public void testValidProductId()
	{
		Set<ProductVersion> products = new HashSet<ProductVersion>();
		products.add(new ProductVersion(1, 1));

		Assertions.assertThat(ProductHelper.validateProductIds(products)).isTrue();
	}

	@Test
	public void testValidProductIds()
	{
		Set<ProductVersion> products = new HashSet<ProductVersion>();
		products.add(new ProductVersion(1, 1));
		products.add(new ProductVersion(2, 2));
		products.add(new ProductVersion(3, 3));
		products.add(new ProductVersion(4, 4));
		products.add(new ProductVersion(5, 5));

		Assertions.assertThat(ProductHelper.validateProductIds(products)).isTrue();
	}

	@Test
	public void testInvalidProductId()
	{
		Set<ProductVersion> products = new HashSet<ProductVersion>();
		products.add(new ProductVersion(0, 0));

		Assertions.assertThat(ProductHelper.validateProductIds(products)).isFalse();
	}

	@Test
	public void testInvalidProductIds()
	{
		Set<ProductVersion> products = new HashSet<ProductVersion>();
		products.add(new ProductVersion(1, 1));
		products.add(new ProductVersion(2, 2));
		products.add(new ProductVersion(0, 0));
		products.add(new ProductVersion(3, 3));
		products.add(new ProductVersion(4, 4));

		Assertions.assertThat(ProductHelper.validateProductIds(products)).isFalse();
	}
}

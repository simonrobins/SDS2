package data;

import static play.test.Helpers.stop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import models.Product;

import org.fest.assertions.Assertions;
import org.junit.Test;

import play.test.FakeApplication;
import controllers.AbstractTest;

public class LanguageProductTest
{
	@Test
	public void testLanguageProduct()
	{
		LanguageProduct lp = new LanguageProduct(null, 0, null);
		Assertions.assertThat(lp).isNotNull();
	}

	@Test
	public void testGetProducts()
	{
		Set<ProductVersion> pv = new HashSet<ProductVersion>();
		LanguageProduct lp = new LanguageProduct(null, 0, pv);
		Assertions.assertThat(lp).isNotNull();
		Assertions.assertThat(lp.getProducts()).isNotNull();
		Assertions.assertThat(lp.getProducts()).isNotSameAs(pv);
		Assertions.assertThat(lp.getProducts()).isEqualTo(pv);
	}

	@Test
	public void testGetLanguage()
	{
		FakeApplication app = AbstractTest.startApp(new HashMap<String, String>());

		Product p = new Product();
		p.setId(1);
		p.setProduct("OCS-ADD ONS");

		LanguageProduct lp = new LanguageProduct(null, 1, null);
		Assertions.assertThat(lp).isNotNull();
		Assertions.assertThat(lp.getLanguage().toString()).isEqualTo(p.toString());

		stop(app);
	}

	@Test
	public void testGetInvalidLanguage()
	{
		FakeApplication app = AbstractTest.startApp(new HashMap<String, String>());

		LanguageProduct lp = new LanguageProduct(null, 0, null);
		Assertions.assertThat(lp).isNotNull();
		Assertions.assertThat(lp.getLanguage()).isNull();

		stop(app);
	}
}

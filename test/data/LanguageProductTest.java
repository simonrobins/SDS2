package data;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import models.Product;

import org.fest.assertions.Assertions;
import org.junit.Test;

import play.test.FakeApplication;

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
		Map<String, String> map = new HashMap<String, String>();

		FakeApplication app = fakeApplication(map);
		start(app);

		Product p = new Product();
		p.setId(1);
		p.setProduct("OCS-ADD ONS");

		LanguageProduct lp = new LanguageProduct(null, 1, null);
		Assertions.assertThat(lp).isNotNull();
		Assertions.assertThat(lp.getLanguage().toString()).isEqualTo(p.toString());
	}

	@Test
	public void testGetInvalidLanguage()
	{
		Map<String, String> map = new HashMap<String, String>();

		FakeApplication app = fakeApplication(map);
		start(app);

		LanguageProduct lp = new LanguageProduct(null, 0, null);
		Assertions.assertThat(lp).isNotNull();
		Assertions.assertThat(lp.getLanguage()).isNull();
	}
}

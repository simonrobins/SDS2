package data;

import static org.junit.Assert.fail;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;

import play.test.FakeApplication;

public class LanguageNodeTest
{
	private LanguageNode node;

	@Before
	public void before()
	{
		Map<String, String> map = new HashMap<String, String>();
		FakeApplication app = fakeApplication(map);
		start(app);

		node = LanguageMap.getLanguageMap().get("Coda Financials", "11.200", "SP17", "Dutch", "FinancialsBE_V11200SP17_DutchLanguage.zip");
	}

	@Test
	public void testIntersect()
	{
		List<ProductVersion> list = new ArrayList<ProductVersion>();
		node.intersect(list);
		Assertions.assertThat(node.getProducts()).isNotNull();
		Assertions.assertThat(node.getProducts().isEmpty()).isFalse();
		Assertions.assertThat(list.isEmpty()).isTrue();
	}

	@Test
	public void testIsNotAllowed()
	{
		Set<Integer> set = new HashSet<Integer>();
		Assertions.assertThat(node.isNotAllowed(set)).isTrue();
	}

	@Test
	public void testIsEmpty()
	{
		Assertions.assertThat(node.isEmpty()).isFalse();
	}

	@Test
	public void testLanguageNodeFile()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testLanguageNodeString()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testLanguageNodeFileBoolean()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testLanguageNodeInt()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testLanguageNodeFileLanguageProduct()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetLanguage()
	{
		LanguageNode node = new LanguageNode(new File("."));
		Assertions.assertThat(node).isNotNull();
		Assertions.assertThat(node.getLanguage()).isNull();
	}

	@Test
	public void testGetProducts()
	{
		fail("Not yet implemented");
	}
}

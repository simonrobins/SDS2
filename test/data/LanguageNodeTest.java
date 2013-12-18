package data;

import static play.test.Helpers.stop;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fest.assertions.Assertions;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.test.FakeApplication;
import controllers.AbstractTest;

public class LanguageNodeTest
{
	private static FakeApplication	app;
	private LanguageNode			node;

	@BeforeClass
	public static void startApp()
	{
		app = AbstractTest.startApp(new HashMap<String, String>());
	}

	@AfterClass
	public static void stopApp()
	{
		stop(app);
	}

	@Before
	public void before()
	{
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
	public void testGetLanguage()
	{
		LanguageNode node = new LanguageNode(new File("."));
		Assertions.assertThat(node).isNotNull();
		Assertions.assertThat(node.getLanguage()).isNull();
	}
}

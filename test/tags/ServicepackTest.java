package tags;

import static org.fest.assertions.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import controllers.AbstractTest;

public class ServicepackTest extends AbstractTest
{
	@Test
	public void testInit()
	{
		assertThat(new Servicepack()).isNotNull();
	}

	@Test
	public void testChecked()
	{
		new Servicepack();

		Set<Integer> products = new HashSet<Integer>();
		products.add(1);
		String result = Servicepack.checked(1, products);
		assertThat("checked=\"checked\"".equals(result));
	}

	@Test
	public void testNotChecked()
	{
		Set<Integer> products = new HashSet<Integer>();
		products.add(2);
		String result = Servicepack.checked(1, products);
		assertThat("".equals(result));
	}
}

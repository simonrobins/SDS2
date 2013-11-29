package data;

import org.fest.assertions.Assertions;
import org.junit.Test;

import controllers.AbstractTest;

public class ProductVersionTest extends AbstractTest
{
	@Test
	public void testHashCode()
	{
		ProductVersion pv = new ProductVersion(1, 2);
		Assertions.assertThat(pv.hashCode()).isEqualTo(23312);
	}

	@Test
	public void testValid()
	{
		ProductVersion pv1 = new ProductVersion(1, 2);

		Assertions.assertThat(pv1.valid()).isTrue();
	}

	@Test
	public void testInvalid()
	{
		ProductVersion pv1 = new ProductVersion(-1, 2);

		Assertions.assertThat(pv1.valid()).isTrue();
	}

	@Test
	public void testEquals()
	{
		ProductVersion pv1 = new ProductVersion(1, 2);
		ProductVersion pv2 = new ProductVersion(1, 2);

		Assertions.assertThat(pv1.equals(pv1)).isTrue();
		Assertions.assertThat(pv1.equals(pv2)).isTrue();
		Assertions.assertThat(pv2.equals(pv2)).isTrue();
	}

	@Test
	public void testNotEquals()
	{
		ProductVersion pv1 = new ProductVersion(1, 2);

		ProductVersion pv2 = new ProductVersion(2, 1);
		ProductVersion pv3 = new ProductVersion(2, 2);
		ProductVersion pv4 = new ProductVersion(1, 1);

		Assertions.assertThat(pv1.equals(pv2)).isFalse();
		Assertions.assertThat(pv1.equals(pv3)).isFalse();
		Assertions.assertThat(pv1.equals(pv4)).isFalse();
	}
}

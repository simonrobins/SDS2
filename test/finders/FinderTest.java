package finders;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class FinderTest
{
	@Test
	public void testAccountContactFinder()
	{
		Assertions.assertThat(new AccountContactFinder()).isNotNull();
	}

	@Test
	public void testBuilderFinder()
	{
		Assertions.assertThat(new BuildFinder()).isNotNull();
	}

	@Test
	public void testCounterFinder()
	{
		Assertions.assertThat(new CounterFinder()).isNotNull();
	}

	@Test
	public void testServicePackFinder()
	{
		Assertions.assertThat(new ServicePackFinder()).isNotNull();
	}

	@Test
	public void testServicePackProductFinder()
	{
		Assertions.assertThat(new ServicePackProductFinder()).isNotNull();
	}
}

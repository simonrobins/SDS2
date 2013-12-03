package finders;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class CompanyDownloadFinderTest
{
	@Test
	public void test()
	{
		Assertions.assertThat(new CompanyDownloadFinder()).isNotNull();
	}
}

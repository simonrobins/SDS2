package models;

import org.fest.assertions.Assertions;
import org.junit.Test;

import controllers.AbstractTest;

public class AccountTest extends AbstractTest
{
	@Test
	public void testIdIsNull()
	{
		Account a = new Account();

		Assertions.assertThat(a).isNotNull();
		Assertions.assertThat(a.getDownload()).isNull();
	}

	@Test
	public void testIdIsNotNull()
	{
		Account a = new Account();
		a.setDownload("DOWNLOAD");

		Assertions.assertThat(a).isNotNull();
		Assertions.assertThat(a.getDownload()).isNotNull();
		Assertions.assertThat(a.getDownload()).isEqualTo("DOWNLOAD");
	}
}

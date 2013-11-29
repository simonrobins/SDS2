package security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import misc.Settings;

import org.fest.assertions.Assertions;
import org.junit.Test;

import controllers.AbstractTest;

public class AutoLoginTest extends AbstractTest
{
	@Test
	public void testValidateMd5Default()
	{
		assertTrue(AutoLogin.getInstance().validateMd5(Settings.SECURE_PASSWORD, "41724", "1678382192", "c47a4d56d0f92858c57d008e9fe9232d"));
	}

	@Test
	public void testValidateMd5()
	{
		assertTrue(AutoLogin.getInstance().validateMd5(Settings.SECURE_PASSWORD, "41724", "1678382192", "3", "3f5f5c105462d19791978cb9a4bd6782"));
	}

	@Test
	public void testValidateMd5Expired()
	{
		assertFalse(AutoLogin.getInstance().validateMd5(Settings.SECURE_PASSWORD, "41724", "0", "3", "4ec73fb421bc2299ad9c5c71c5a378e7"));
	}

	@Test
	public void testValidateMd5InvalidMD5()
	{
		assertFalse(AutoLogin.getInstance().validateMd5(Settings.SECURE_PASSWORD, "41724", "1678382192", "3", "3f5f5c105462d19791978cb9a4bd6783"));
		assertFalse(AutoLogin.getInstance().validateMd5(Settings.SECURE_PASSWORD, "41724", "1678382192", "2", "3f5f5c105462d19791978cb9a4bd6782"));
		assertFalse(AutoLogin.getInstance().validateMd5(Settings.SECURE_PASSWORD, "41725", "1678382194", "3", "3f5f5c105462d19791978cb9a4bd6782"));
		assertFalse(AutoLogin.getInstance().validateMd5(Settings.SECURE_PASSWORD, "41725", "1678382192", "3", "3f5f5c105462d19791978cb9a4bd6782"));
	}

	@Test
	public void testValidateMd5Cached()
	{
		assertTrue(AutoLogin.getInstance().validateMd5(Settings.SECURE_PASSWORD, "41724", "1678382193", null, "577c405644338d60dd4e3f252b307e91"));
		assertFalse(AutoLogin.getInstance().validateMd5(Settings.SECURE_PASSWORD, "41724", "1678382193", null, "577c405644338d60dd4e3f252b307e91"));
	}

	@Test
	public void testWithValidAlgorithm()
	{
		Assertions.assertThat(AutoLogin.getInstance().digest("MD5")).isNotNull();
	}

	@Test(expected = RuntimeException.class)
	public void testWithInvalidAlgorithm()
	{
		Assertions.assertThat(AutoLogin.getInstance().digest("BLOB")).isNotNull();
	}
}

package security;

import static org.junit.Assert.fail;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class CipherTest
{
	public void testAesEncoderHandlesNullString()
	{
		try
		{
			String string = Cipher.aesEncoder("", "");
			Assertions.assertThat(string).isEqualTo(null);
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testAesEncoder()
	{
		try
		{
			String string = Cipher.aesEncoder("0123456789ABCDEF", "0123456789ABCDEF");
			Assertions.assertThat(string).isEqualTo("jYNdDP>YutWF>otClMQhiDSvM2yfAxs1VnOMY<YeovY=");
		}
		catch (Exception e)
		{
			fail("testAesEncoder");
		}
	}

	@Test
	public void testAesEncoderWithNothing()
	{
		try
		{
			String string = Cipher.aesEncoder("", "0123456789ABCDEF");
			Assertions.assertThat(string).isEqualTo("NK8zbJ8DGzVWc4xj5h6i9g==");
		}
		catch (Exception e)
		{
			fail("testAesEncoder");
		}
	}

	@Test
	public void testAesDecoder()
	{
		try
		{
			String string = Cipher.aesDecoder("jYNdDP>YutWF>otClMQhiDSvM2yfAxs1VnOMY<YeovY=", "0123456789ABCDEF");
			Assertions.assertThat(string).isEqualTo("0123456789ABCDEF");
		}
		catch (Exception e)
		{
			fail("testAesDecoder");
		}
	}
}

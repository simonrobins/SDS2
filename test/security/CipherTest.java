package security;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class CipherTest
{
	@Test
	public void testConstructor()
	{
		Assertions.assertThat(new Cipher()).isNotNull();
	}

	@Test(expected = RuntimeException.class)
	public void testAesEncoderHandlesNullString()
	{
		Cipher.aesEncoder("", "");
	}

	@Test
	public void testAesEncoder()
	{
		String string = Cipher.aesEncoder("0123456789ABCDEF", "0123456789ABCDEF");
		Assertions.assertThat(string).isEqualTo("jYNdDP>YutWF>otClMQhiDSvM2yfAxs1VnOMY<YeovY=");
	}

	@Test
	public void testAesEncoderWithNothing()
	{
		String string = Cipher.aesEncoder("", "0123456789ABCDEF");
		Assertions.assertThat(string).isEqualTo("NK8zbJ8DGzVWc4xj5h6i9g==");
	}

	@Test
	public void testAesDecoder()
	{
		String string = Cipher.aesDecoder("jYNdDP>YutWF>otClMQhiDSvM2yfAxs1VnOMY<YeovY=", "0123456789ABCDEF");
		Assertions.assertThat(string).isEqualTo("0123456789ABCDEF");
	}

	@Test
	public void testAesDecoderWithNothing()
	{
		String string = Cipher.aesDecoder("", "0123456789ABCDEF");
		Assertions.assertThat(string).isEqualTo("");
	}
}

package security;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class Base64Test
{
	private final String toEncode = "0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCD";
	private final String toDecode = "MDEyMzQ1Njc4OUFCQ0RFRjAxMjM0NTY3ODlBQkNERUYwMTIzNDU2Nzg5QUJDREVGMDEyMzQ1Njc4OUFCQ0RFRjAxMjM0NTY3ODlBQkNERUYwMTIzNDU2Nzg5QUJDRA==";

	private final String toEncode1 = "0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDE";
	private final String toDecode1 = "MDEyMzQ1Njc4OUFCQ0RFRjAxMjM0NTY3ODlBQkNERUYwMTIzNDU2Nzg5QUJDREVGMDEyMzQ1Njc4OUFCQ0RFRjAxMjM0NTY3ODlBQkNERUYwMTIzNDU2Nzg5QUJDREU=";

	private final String toEncode2 = "0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
	private final String toDecode2 = "MDEyMzQ1Njc4OUFCQ0RFRjAxMjM0NTY3ODlBQkNERUYwMTIzNDU2Nzg5QUJDREVGMDEyMzQ1Njc4OUFCQ0RFRjAxMjM0NTY3ODlBQkNERUYwMTIzNDU2Nzg5QUJDREVG";

	@Test
	public void testEncodeToStringWithEmptyString()
	{
		String result = new String(Base64.getInstance().encode("".getBytes()));
		Assertions.assertThat(result).isEqualTo("");
	}

	@Test
	public void testEncodeToString()
	{
		String result = new String(Base64.getInstance().encode(toEncode1.getBytes()));
		Assertions.assertThat(result).isEqualTo(toDecode1);

		result = new String(Base64.getInstance().encode(toEncode2.getBytes()));
		Assertions.assertThat(result).isEqualTo(toDecode2);

		result = new String(Base64.getInstance().encode(toEncode.getBytes()));
		Assertions.assertThat(result).isEqualTo(toDecode);
	}

	@Test
	public void testDecodeFastToStringWithEmptyString()
	{
		String result = new String(Base64.getInstance().decode(""));
		Assertions.assertThat(result).isEqualTo("");
	}

	@Test
	public void testDecodeFastToString()
	{
		String result = new String(Base64.getInstance().decode(toDecode1));
		Assertions.assertThat(result).isEqualTo(toEncode1);

		result = new String(Base64.getInstance().decode(toDecode2));
		Assertions.assertThat(result).isEqualTo(toEncode2);

		result = new String(Base64.getInstance().decode(toDecode));
		Assertions.assertThat(result).isEqualTo(toEncode);
	}
}
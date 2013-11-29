package security;

import javax.crypto.spec.SecretKeySpec;

import org.eclipse.jdt.annotation.Nullable;

public class Cipher
{
	private static final Cipher INSTANCE = new Cipher();

	public static Cipher getInstance()
	{
		return INSTANCE;
	}

	private Cipher()
	{
	}

	@Nullable
	public String aesEncoder(final String string, final String secret)
		throws Exception
	{
		final SecretKeySpec secretKey = new SecretKeySpec(secret.substring(0, 16).getBytes(), "AES");
		final javax.crypto.Cipher aesCipher = javax.crypto.Cipher.getInstance("AES");
		aesCipher.init(javax.crypto.Cipher.ENCRYPT_MODE, secretKey);

		final byte[] cleartext = string.getBytes();
		final byte[] ciphertext = aesCipher.doFinal(cleartext);

		return new String(Base64.getInstance().encode(ciphertext));
	}

	public String aesDecoder(final String codedpassword, final String secret)
		throws Exception
	{
		final SecretKeySpec secretKey = new SecretKeySpec(secret.substring(0, 16).getBytes(), "AES");
		final javax.crypto.Cipher aesCipher = javax.crypto.Cipher.getInstance("AES");
		aesCipher.init(javax.crypto.Cipher.DECRYPT_MODE, secretKey);

		final byte[] ciphertext = Base64.getInstance().decode(codedpassword);
		final byte[] cleartext = aesCipher.doFinal(ciphertext);
		return new String(cleartext);
	}
}

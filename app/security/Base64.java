package security;

import java.util.Arrays;

/**
 * A very fast and memory efficient class to encode and decode to and from
 * BASE64 in full accordance with RFC 2045.<br>
 * <br>
 * On Windows XP sp1 with 1.4.2_04 and later ;), this encoder and decoder is
 * about 10 times faster on small arrays (10 - 1000 bytes) and 2-3 times as fast
 * on larger arrays (10000 - 1000000 bytes) compared to
 * <code>sun.misc.Encoder()/Decoder()</code>.<br>
 * <br>
 * On byte arrays the encoder is about 20% faster than Jakarta Commons Base64
 * Codec for encode and about 50% faster for decoding large arrays. This
 * implementation is about twice as fast on very small arrays (&lt 30 bytes). If
 * source/destination is a <code>String</code> this version is about three times
 * as fast due to the fact that the Commons Codec result has to be recoded to a
 * <code>String</code> from <code>byte[]</code>, which is very expensive.<br>
 * <br>
 * This encode/decode algorithm doesn't create any temporary arrays as many
 * other codecs do, it only allocates the resulting array. This produces less
 * garbage and it is possible to handle arrays twice as large as algorithms that
 * create a temporary array. (E.g. Jakarta Commons Codec). It is unknown whether
 * Sun's <code>sun.misc.Encoder()/Decoder()</code> produce temporary arrays but
 * since performance is quite low it probably does.<br>
 * <br>
 * The encoder produces the same output as the Sun one except that the Sun's
 * encoder appends a trailing line separator if the last character isn't a pad.
 * Unclear why but it only adds to the length and is probably a side effect.
 * Both are in conformance with RFC 2045 though.<br>
 * Commons codec seem to always att a trailing line separator.<br>
 * <br>
 * <b>Note!</b> The encode/decode method pairs (types) come in three versions
 * with the <b>exact</b> same algorithm and thus a lot of code redundancy. This
 * is to not create any temporary arrays for transcoding to/from different
 * format types. The methods not used can simply be commented out.<br>
 * <br>
 * There is also a "fast" version of all decode methods that works the same way
 * as the normal ones, but har a few demands on the decoded input. Normally
 * though, these fast verions should be used if the source if the input is known
 * and it hasn't bee tampered with.<br>
 * <br>
 * If you find the code useful or you find a bug, please send me a note at
 * base64 @ miginfocom . com. Licence (BSD): ============== Copyright (c) 2004,
 * Mikael Grev, MiG InfoCom AB. (base64 @ miginfocom . com) All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. Neither the name of the MiG InfoCom AB nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission. THIS SOFTWARE IS
 * PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * @version 2.2
 * @author Mikael Grev Date: 2004-aug-02 Time: 11:31:11
 */

public class Base64
{
	private static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789<>".toCharArray();
	private static final int[] IA = new int[256];
	static
	{
		Arrays.fill(IA, -1);
		for(int i = 0, iS = CA.length; i < iS; i++)
			IA[CA[i]] = i;
		IA['='] = 0;
	}

	private static final Base64 INSTANCE = new Base64();

	public static Base64 getInstance()
	{
		return INSTANCE;
	}

	private Base64()
	{
	}

	// ****************************************************************************************
	// * char[] version
	// ****************************************************************************************

	/**
	 * Encodes a raw byte array into a BASE64 <code>char[]</code> representation
	 * i accordance with RFC 2045.
	 * 
	 * @param sArr The bytes to convert. If <code>null</code> or length 0 an
	 *            empty array will be returned.
	 * @return A BASE64 encoded array. Never <code>null</code>.
	 */
	public final char[] encode(final byte[] sArr)
	{
		// Check special case
		final int sLen = sArr.length;
		if(sLen == 0)
			return new char[0];

		final int eLen = (sLen / 3) * 3; // Length of even 24-bits.
		final int cCnt = ((sLen - 1) / 3 + 1) << 2; // Returned character count
		final int dLen = cCnt;
		final char[] dArr = new char[dLen];

		// Encode even 24-bits
		for(int s = 0, d = 0; s < eLen;)
		{
			// Copy next three bytes into lower 24 bits of integer, paying
			// attention to sign.
			final int i = (sArr[s++] & 0xff) << 16 | (sArr[s++] & 0xff) << 8 | (sArr[s++] & 0xff);

			// Encode the int into four chars
			dArr[d++] = CA[(i >>> 18) & 0x3f];
			dArr[d++] = CA[(i >>> 12) & 0x3f];
			dArr[d++] = CA[(i >>> 6) & 0x3f];
			dArr[d++] = CA[i & 0x3f];
		}

		// Pad and encode last bits if source isn't even 24 bits.
		final int left = sLen - eLen; // 0 - 2.
		if(left > 0)
		{
			// Prepare the int
			final int i = ((sArr[eLen] & 0xff) << 10) | (left == 2 ? ((sArr[sLen - 1] & 0xff) << 2) : 0);

			// Set last four chars
			dArr[dLen - 4] = CA[i >> 12];
			dArr[dLen - 3] = CA[(i >>> 6) & 0x3f];
			dArr[dLen - 2] = left == 2 ? CA[i & 0x3f] : '=';
			dArr[dLen - 1] = '=';
		}
		return dArr;
	}

	/**
	 * Decodes a BASE64 encoded string that is known to be resonably well
	 * formatted. The method is about twice as fast as {@link #decode(String)}.
	 * The preconditions are:<br>
	 * + The array must have a line length of 76 chars OR no line separators at
	 * all (one line).<br>
	 * + Line separator must be "\r\n", as specified in RFC 2045 + The array
	 * must not contain illegal characters within the encoded string<br>
	 * + The array CAN have illegal characters at the beginning and end, those
	 * will be dealt with appropriately.<br>
	 * 
	 * @param s The source string. Length 0 will return an empty array.
	 *            <code>null</code> will throw an exception.
	 * @return The decoded array of bytes. May be of length 0.
	 */
	public final byte[] decode(final String s)
	{
		// Check special case
		final int sLen = s.length();
		if(sLen == 0)
			return new byte[0];

		int sIx = 0, eIx = sLen - 1; // Start and end index after trimming.

		// get the padding count (=) (0, 1 or 2)
		final int pad = s.charAt(eIx) == '=' ? (s.charAt(eIx - 1) == '=' ? 2 : 1) : 0; // Count
																						// '='
																						// at
																						// end.
		final int cCnt = eIx - sIx + 1; // Content count including possible
										// separators

		final int len = (cCnt * 6 >> 3) - pad; // The number of decoded bytes
		final byte[] dArr = new byte[len]; // Preallocate byte[] of exact length

		// Decode all but the last 0 - 2 bytes.
		int d = 0;
		for(int eLen = (len / 3) * 3; d < eLen;)
		{
			// Assemble three bytes into an int from four "valid" characters.
			final int i = IA[s.charAt(sIx++)] << 18 | IA[s.charAt(sIx++)] << 12 | IA[s.charAt(sIx++)] << 6 | IA[s.charAt(sIx++)];

			// Add the bytes
			dArr[d++] = (byte) (i >> 16);
			dArr[d++] = (byte) (i >> 8);
			dArr[d++] = (byte) i;
		}

		if(d < len)
		{
			// Decode last 1-3 bytes (incl '=') into 1-3 bytes
			int i = 0;
			for(int j = 0; sIx <= eIx - pad; j++)
				i |= IA[s.charAt(sIx++)] << (18 - j * 6);

			for(int r = 16; d < len; r -= 8)
				dArr[d++] = (byte) (i >> r);
		}

		return dArr;
	}
}
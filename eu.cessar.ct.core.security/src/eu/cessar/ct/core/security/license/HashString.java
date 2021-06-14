package eu.cessar.ct.core.security.license;

// //////////////////////////////////////////////////////////////////////////////
//
// md4-like string hashing
//
// HashString.hashString(String) returns a Vector of three ints which are
// used as our three socket port numbers converted from our appname.
//
// Original C code from C.S., reused here with kind permission.
//
// //////////////////////////////////////////////////////////////////////////////

public class HashString
{
	public static int[] hashString(String aString)
	{

		int[] values = {0, 0, 0};
		long ctx0[] = {0xeca22e3c, 0xd1e336aa, 0x38f338cf, 0xf379b53f};
		long ctx1[] = {0x36aa38f3, 0x2e3cf379, 0xb53feca2, 0x38cfd1e3};
		long ctx2[] = {0x3e1df3b5, 0x3f83c3e2, 0x973faa63, 0x2acefc83};
		char buffer[] = new char[64];
		int i, len, add;

		len = (aString != null) ? aString.length() : 0;
		if (len > 64)
			len = 64;
		add = 64 - len;

		for (i = 0; i < len; i++)
			buffer[i] = aString.charAt(i);

		if (add > 0)
		{
			String add_string = "}jzfI|%obL^&lw9Jcm6B]W$h1MsaekH2#ivV!R[8g@Z0KuUDGy43ESrOCtx7T5Yn";
			for (i = 0; i < add; i++)
				buffer[len + i] = add_string.charAt(i);
		}

		values[0] = hashBufferWithContext(buffer, ctx0) + 15000;

		if (add > 0)
		{
			String add_string = "@dco4Fz8^|Y{&TKeDsC[$BxUWJyXlkhRrp#u}Oa]%L3btEg9wGSMPNvjq!AQm2iZ";
			for (i = 0; i < add; i++)
				buffer[len + i] = add_string.charAt(i);
		}

		values[1] = hashBufferWithContext(buffer, ctx1) + 30000;
		if (values[1] == values[0])
			values[1] += 13;

		if (add > 0)
		{
			String add_string = "gQomh{%H8[sYXau56y$ZBR#SJL]!T@1czjKidvFqkMp^G4EI}&AwxV70frP3UC|O";
			for (i = 0; i < add; i++)
				buffer[len + i] = add_string.charAt(i);
		}

		values[2] = hashBufferWithContext(buffer, ctx2) + 45000;
		while ((values[2] == values[0]) || (values[2] == values[1]))
			values[2] += 13;

		return values;
	}

	private static int hashBufferWithContext(char buffer[/* 64 */], long context[/* 4 */])
	{

		long data[] = new long[16];
		int i;

		for (i = 0; i < 16; i++)
			data[i] = 0;

		for (i = 0; i < 16; i++)
		{
			int j = i * 4;
			data[i] = ((long) (buffer[j]) & 0xFF) + ((((long) (buffer[j + 1])) & 0xFF) << 8)
				+ ((((long) (buffer[j + 2])) & 0xFF) << 16)
				+ ((((long) (buffer[j + 3])) & 0xFF) << 24);
		}

		doHash(context, data);

		return (int) ((context[0] + context[1] + context[2] + context[3]) % 15000);
	}

	private static long md4_f(long x, long y, long z)
	{

		return (((x & y) | (((~x) & 0xFFFFFFFFL) & z)) & 0xFFFFFFFFL);
	}

	private static long md4_g(long x, long y, long z)
	{

		return (((x & y) | (x & z) | (y & z)) & 0xFFFFFFFFL);
	}

	private static long md4_h(long x, long y, long z)
	{

		return ((x ^ y ^ z) & 0xFFFFFFFFL);
	}

	private static long md4_rot(long x, int bits)
	{

		return ((((x << bits) & 0xFFFFFFFFL) | ((x & 0xFFFFFFFFL) >>> (32 - bits))) & 0xFFFFFFFFL);
	}

	private static int dindex[/* 316 */] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
		0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15, 0, 8, 4, 12, 2, 10, 6, 14, 1, 9, 5,
		13, 3, 11, 7, 15,};

	private static int shift[/* 34 */] = {3, 7, 11, 19, 3, 9, 11, 15, 3, 5, 9, 13,};

	private static long addConst[/* 3 */] = {0, 0x4f452609, 0x74ec8b0b};

	private static void doHash(long[] context/* 4 */, long[] data/* 16 */)
	{

		long ctx[] = new long[4];
		int i, j;

		for (i = 0; i < 4; i++)
			ctx[i] = context[i];

		for (j = 0; j < 16; j++)
		{
			long x = ctx[j % 4] + md4_f(ctx[(j + 1) % 4], ctx[(j + 2) % 4], ctx[(j + 3) % 4]);
			ctx[j % 4] = md4_rot(x + data[dindex[0 * 16 + j]] + addConst[0], shift[0 * 4 + (j % 4)]);
		}
		for (j = 0; j < 16; j++)
		{
			long x = ctx[j % 4] + md4_g(ctx[(j + 1) % 4], ctx[(j + 2) % 4], ctx[(j + 3) % 4]);
			ctx[j % 4] = md4_rot(x + data[dindex[1 * 16 + j]] + addConst[1], shift[1 * 4 + (j % 4)]);
		}
		for (j = 0; j < 16; j++)
		{
			long x = ctx[j % 4] + md4_h(ctx[(j + 1) % 4], ctx[(j + 2) % 4], ctx[(j + 3) % 4]);
			ctx[j % 4] = md4_rot(x + data[dindex[2 * 16 + j]] + addConst[2], shift[2 * 4 + (j % 4)]);
		}

		for (i = 0; i < 4; i++)
			context[i] += ctx[i];
	}
}

package eu.cessar.ct.core.security.license;

class RandomGenerator
{
	/* ------ original C code from C.S., reused here with kind permission ------ */

	/*
	 * ---------------------------------------------------------------------------
	 * random-number-generator:
	 * --------------------------------------------------------------------------- struct
	 * random_status random_status status of the random-generator (internally used, may however be
	 * stored to generate reproducible results) random_get(void) returns a random-number in the
	 * range [0,1] random_init(int na1, int na2, int na3, int nb1) initializes the random-generator.
	 * Must be called once before random_get() is used. The numbers na1...na3, nb1 are some
	 * integer-numbers used to set the seed. Standard-values are (12, 34, 56, 78).
	 */

	/* ------------------------------------------------------------------------- */

	private int random_status_i;
	private double[] random_status_u = new double[97];
	private double random_status_c;

	RandomGenerator()
	{

		long msec_now = System.currentTimeMillis();
		long sec_now = msec_now / 1000;

		init((int) (sec_now / 17), (int) (sec_now % 1017), (int) (msec_now / 23),
			(int) (msec_now % 2023));
	}

	RandomGenerator(int na1, int na2, int na3, int nb1)
	{

		init(na1, na2, na3, nb1);
	}

	public void init(int na1, int na2, int na3, int nb1)
	{

		int i, j, mat;
		double s, t;

		random_status_i = 96;
		for (j = 0; j < 97; j++)
		{
			s = 0;
			t = 0.5;
			for (i = 0; i < 24; i++)
			{
				mat = (((na1 * na2) % 179) * na3) % 179;
				na1 = na2;
				na2 = na3;
				na3 = mat;
				nb1 = (53 * nb1 + 1) % 169;
				if ((nb1 * mat % 64) >= 32)
					s += t;
				t /= 2;

			}
			random_status_u[j] = s;
		}
		random_status_c = 362436. / 16777216.;
	}

	public double get()
	{

		double r;

		r = random_status_u[random_status_i] - random_status_u[(random_status_i + 33) % 97];
		if (r < 0)
			r += 1;
		random_status_u[random_status_i--] = r;
		if (random_status_i < 0)
			random_status_i = 96;
		random_status_c = random_status_c - 7654321. / 16777216.;
		if (random_status_c < 0)
			random_status_c += 16777213. / 16777216.;
		r -= random_status_c;
		if (r < 0)
			r += 1;
		return r;
	}
}

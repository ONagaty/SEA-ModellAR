package eu.cessar.ct.compat.tests.ant;

import junit.framework.Test;
import junit.framework.TestSuite;

public class SUITE_AntTasks
{

	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for eu.cessar.ct.compat.ant");

		// test compat ant task
		suite.addTestSuite(CompatConvertPathTaskTests.class);
		suite.addTestSuite(CompatGetVariantTaskTests.class);
		suite.addTestSuite(CompatGenerateJetTaskTests.class);
		suite.addTestSuite(CompatRunPlugetTaskTests.class);
		return suite;
	}

}

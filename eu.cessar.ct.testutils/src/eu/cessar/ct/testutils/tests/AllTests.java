package eu.cessar.ct.testutils.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests
{

	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for eu.cessar.ct.testutils.tests");
		// $JUnit-BEGIN$
		suite.addTestSuite(TestCaseSample.class);
		suite.addTestSuite(HelpDocumentationTests.class);
		// $JUnit-END$
		return suite;
	}

}

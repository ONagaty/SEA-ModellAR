package eu.cessar.ct.testutils.tests;

/**
 * TODO: Please comment this class
 * 
 * @author uidu8153
 * 
 *         %created_by: uidu8153 %
 * 
 *         %date_created: Wed Feb  4 05:56:31 2015 %
 * 
 *         %version: 2 %
 */

import junit.framework.Test;
import eu.cessar.ct.testutils.CessarTestSuite;

public class AllLibraryTests
{
	public static Test suite()
	{
		CessarTestSuite suite = new CessarTestSuite("Test for eu.cessar.ct.testutils.tests");

		// $JUnit-BEGIN$

		suite.addTestSuite(TestForLibraryJarFile.class);

		// $JUnit-END$
		return suite;
	}
}

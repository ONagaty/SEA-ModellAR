package eu.cessar.ct.compat.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import eu.cessar.ct.compat.tests.classpath.TestClassPath3x;
import eu.cessar.ct.compat.tests.internal.PlugetTaskManagerFactoryTest;

public class SUITE_Compat
{

	public static Test suite()
	{
		TestSuite suite = new TestSuite(SUITE_Compat.class.getName());
		// $JUnit-BEGIN$
		suite.addTestSuite(TestClassPath3x.class);
		suite.addTestSuite(PlugetTaskManagerFactoryTest.class);
		// $JUnit-END$
		return suite;
	}

}

package eu.cessar.ct.testutils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.apache.tools.ant.taskdefs.optional.junit.JUnitResultFormatter;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.osgi.util.NLS;
import org.junit.runner.Describable;
import org.junit.runner.Description;

import eu.cessar.ct.test.annotations.ExpectedFailure;
import eu.cessar.ct.test.annotations.TestDetails;

public class CessarTestCase extends TestCase implements Describable
{

	protected boolean dumpJobs = false;
	protected boolean dumpThreads = false;
	protected boolean ignoreExpectedFailures = false;

	/**
	 * Holds the potential expected failure annotation instance.
	 */
	private ExpectedFailure expectedFailure = null;

	/**
	 * Holds the current test output formatter (usually CustomXMLJUnitResultFormatter).
	 */
	private static JUnitResultFormatter formatter;

	/**
	 * Holds the current test output directory (assumed identical for all test suites).
	 */
	private static String testDirectory;

	static
	{
		// Initial setup of the formatter for expected failures.
		// They are marked as passed but logged as failed in separate .efxml files.
		try
		{
			setUpFormatter();
		}
		catch (SecurityException se)
		{
			CessarPluginActivator.getDefault().logError(se);
		}
	}

	protected boolean dumpJobsOnFailure()
	{
		return dumpJobs;
	}

	protected boolean dumpThreadsOnFailure()
	{
		return dumpThreads;
	}

	/**
	 * @param expected
	 */
	public void setExpectedFailure(ExpectedFailure expected)
	{
		expectedFailure = expected;
	}

	/**
	 * Initializes the expected failure-related members for the current test case, if any.
	 */
	private void setUpExpectedFailure()
	{
		if (expectedFailure == null)
		{
			// the failure might come from outside via setExpectedFailure
			String testName = getName();
			Method method;
			try
			{
				method = this.getClass().getMethod(testName);
				expectedFailure = method.getAnnotation(ExpectedFailure.class);
			}
			catch (Throwable t)
			{
				// It should not happen to fail to get the method
				CessarPluginActivator.getDefault().logError(t);
			}
		}
	}

	/**
	 * @throws Exception
	 */
	private void setUpCessar() throws Exception
	{
		dumpJobs = Boolean.valueOf(System.getProperty("B.dumpJobsOnFailure", "false"));
		dumpThreads = Boolean.valueOf(System.getProperty("B.dumpThreadsOnFailure", "false"));
		ignoreExpectedFailures = Boolean.valueOf(System.getProperty("B.ignoreExpectedFailures", "false"));
		setUpExpectedFailure();
	}

	/**
	 * Creates the expected failure file.
	 *
	 * @return the formatter
	 */
	private FileOutputStream createExpectedFailureFileOutputStream()
	{
		File efFile = new File(testDirectory + "/" + testName() + System.currentTimeMillis() + ".efxml");

		try
		{
			return new FileOutputStream(efFile, false);
		}
		catch (FileNotFoundException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		return null;
	}

	/**
	 * Decides whether the test failure is an expected one.
	 *
	 * @param t
	 *        the Throwable that resulted from the test.
	 * @return indicates if the Throwable matches the expected one.
	 */
	private boolean isExpectedFailure(final Throwable t)
	{
		if (null != expectedFailure)
		{
			// A failure was expected.
			if (t.getClass().equals(expectedFailure.expected()))
			{
				if (expectedFailure.message().isEmpty())
				{
					return (null == t.getMessage());
				}
				else
				{
					return (null != t.getMessage()) && t.getMessage().contains(expectedFailure.message());
				}
			}
		}
		return false;
	}

	/**
	 * Create the test formatter for expected failures.
	 *
	 * @param formatterName
	 *        the formatter name
	 * @return the JUnit test result formatter object
	 */
	private static JUnitResultFormatter createFormatter(String formatterName)
	{
		JUnitResultFormatter fmt = null;
		try
		{
			fmt = (JUnitResultFormatter) Class.forName(formatterName).newInstance();
		}
		catch (Exception e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		return fmt;
	}

	/**
	 * Sets up the formatter (same for all tests/suites).
	 *
	 * @throws SecurityException
	 * @throws IOException
	 */
	private static void setUpFormatter()
	{
		String formatterLine = getFormatterLine();
		if (null != formatterLine)
		{
			String[] fmtArgs = formatterLine.split(",");

			String formatterName = fmtArgs[0];
			formatter = createFormatter(formatterName);

			Path p = new Path(fmtArgs[1].split(" ")[0]);
			testDirectory = p.uptoSegment(p.segmentCount() - 1).toString();
		}
	}

	/**
	 * Retrieves the "formatter=" line from the command line arguments.
	 *
	 * @return the "formatter=" line if found or null
	 */
	private static String getFormatterLine()
	{
		String[] args = Platform.getCommandLineArgs();
		String needle = "formatter=";

		for (int i = 0; i < args.length; i++)
		{
			if (args[i].startsWith(needle))
			{
				return args[i].substring(needle.length());
			}
		}
		return null;
	}

	/**
	 * Log an expected failure.
	 *
	 * @param t
	 *        the expected failure
	 * @throws Throwable
	 */
	private void logExpectedFailure(final Throwable t) throws Throwable
	{
		System.out.println("== Failure of test '" + testName() + "' was expected (" + t.getClass().getName() + ": "
			+ t.getMessage() + "). CR info: " + expectedFailure.CRInfo());

		if (ignoreExpectedFailures)
		{
			// just ignore them
			return;
		}
		if (null == formatter)
		{
			// No expected failure formatter, let it fail.
			throw t;
		}

		FileOutputStream expectedFailureOutputStream = createExpectedFailureFileOutputStream();
		if (null == expectedFailureOutputStream)
		{
			// No expected failure file, let it fail.
			throw t;
		}

		try
		{
			// Log it separately in an ad hoc "suite".
			formatter.setOutput(expectedFailureOutputStream);

			JUnitTest jUnitTest = new JUnitTest(getClass().getName());
			formatter.startTestSuite(jUnitTest);
			formatter.startTest(this);

			formatter.addError(this, t);

			// There is always a single test failure in this ad hoc suite.
			jUnitTest.setCounts(1, 1, 0);

			formatter.endTestSuite(jUnitTest);
		}
		finally
		{
			expectedFailureOutputStream.close();
		}
	}

	/**
	 * The current test name.
	 *
	 * @return the fully qualified test name.
	 */
	private String testName()
	{
		return getClass().getName() + "." + getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#runBare()
	 */
	@Override
	public void runBare() throws Throwable
	{
		Throwable exception = null;
		setUpCessar();
		try
		{
			super.runBare();
		}
		catch (Throwable running)
		{
			exception = running;
		}
		finally
		{
			try
			{
				tearDownCessar();
			}
			catch (Throwable tearingDown)
			{
				if (exception == null)
				{
					exception = tearingDown;
				}
			}
		}
		if (exception != null)
		{
			throw exception;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#runTest()
	 */
	@Override
	protected void runTest() throws Throwable
	{
		long time = System.currentTimeMillis();
		Throwable failure = null;
		try
		{
			System.out.println("== Executing test: " + testName());
			super.runTest();
		}
		catch (Throwable t)
		{
			failure = t;
			// the test failed, let's see if we need to provide some infos.
			try
			{
				DebugUtils.dumpDebugInformation("Debug report of test: " + testName(), System.err, dumpJobsOnFailure(),
					dumpThreadsOnFailure());
			}
			catch (Throwable t2)
			{
				// Do not let it throw so just print it
				fail(t2);
			}
			if (isExpectedFailure(t))
			{
				System.out.println("== Failure of test: " + testName() + " was expected.");
				// Expected failure, attempt to log it separately.
				logExpectedFailure(t);
			}
			else
			{
				// Unexpected failure, test failed, propagate it.
				throw t;
			}
		}
		finally
		{
			time = System.currentTimeMillis() - time;
			System.out.println("== Execution of test: " + testName() + " performed in " + time + " ms");
		}

		if ((null != expectedFailure) && (null == failure))
		{
			// A failure was expected but did not occur, this is itself a failure.
			System.out.println("== Expected failure of test: " + testName() + " did not occur!");
			throw new ExpectedFailureAbsentException("Expected Throwable <" + expectedFailure.expected()
				+ "> did not occur for " + testName() + "!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.junit.runner.Describable#getDescription()
	 */
	public Description getDescription()
	{
		String testName = getName();
		Method method;
		Annotation testDetailAnn = null;
		Class<?> clz = this.getClass();
		try
		{
			method = clz.getMethod(testName);
			testDetailAnn = method.getAnnotation(TestDetails.class);
		}
		catch (Throwable t)
		{
			// It should not happen to fail to get the method
			CessarPluginActivator.getDefault().logError(t);
		}
		if (null == testDetailAnn)
		{
			testDetailAnn = clz.getAnnotation(TestDetails.class);
		}
		return Description.createTestDescription(this.getClass(), testName, testDetailAnn == null ? new Annotation[0]
			: new Annotation[] {testDetailAnn});
	}

	/**
	 * Fail a test with a particular Throwable.
	 *
	 * @param t
	 */
	public static final void fail(Throwable t)
	{
		if (t == null)
		{
			fail();
		}
		else
		{
			String message = t.getMessage();
			if (message == null)
			{
				message = t.getClass().getCanonicalName();
			}
			AssertionFailedError error = new AssertionFailedError(message);
			error.initCause(t);
			throw error;
		}
	}

	/**
	 * Assert that a status is not null and have OK severity
	 *
	 * @param status
	 */
	public static final void assertStatusOK(IStatus status)
	{
		assertStatus(status, IStatus.OK);
	}

	/**
	 * Assert that the status is either OK, or not OK.
	 *
	 * @param status
	 * @param isOK
	 */
	public static final void assertStatusOK(IStatus status, boolean isOK)
	{
		if (isOK)
		{
			assertStatus(status, IStatus.OK);
		}
		else
		{
			assertStatus(status, IStatus.ERROR | IStatus.WARNING | IStatus.CANCEL | IStatus.INFO);
		}
	}

	/**
	 * @param status
	 * @param expectedSeverities
	 */
	public static final void assertStatus(IStatus status, int expectedSeverities)
	{
		assertNotNull(status);
		if ((expectedSeverities & status.getSeverity()) != status.getSeverity())
		{
			AssertionFailedError err = new AssertionFailedError(status.getMessage());
			if (status.getException() != null)
			{
				err.initCause(status.getException());
			}
			throw err;
		}
	}

	private Set<IProject> projectsToCleanup;

	/**
	 * @param projectToDelete
	 */
	protected void addToCleanup(IProject projectToDelete)
	{
		if (projectsToCleanup == null)
		{
			projectsToCleanup = new HashSet<>();
		}
		projectsToCleanup.add(projectToDelete);
	}

	/**
	 *
	 */
	private void cleanUpProjects()
	{
		if (projectsToCleanup != null)
		{
			Iterator<IProject> it = projectsToCleanup.iterator();
			try
			{
				while (it.hasNext())
				{
					try
					{
						TestUtilities.deleteProject(it.next());
					}
					catch (CoreException e)
					{
						fail(e);
					}
				}
			}
			finally
			{
				projectsToCleanup.clear();
			}
		}
	}

	/**
	 * @see TestUtilities#importProject(String, IPath, boolean)
	 * @param pluginID
	 * @param projectArchive
	 * @param override
	 * @param autoClean
	 * @return
	 */
	public IProject importProject(String pluginID, IPath projectArchive, boolean override, boolean autoClean)
	{
		IProject project;
		try
		{
			project = TestUtilities.importProject(pluginID, projectArchive, override);
			if (autoClean)
			{
				addToCleanup(project);
			}
			return project;
		}
		catch (CoreException e)
		{
			fail(e);
			return null; //
		}
	}

	public IProject importProject(IPath projectArchive, boolean override, boolean autoClean)
	{
		IProject project;
		try
		{
			project = TestUtilities.importProject(projectArchive, override);
			if (autoClean)
			{
				addToCleanup(project);
			}
			return project;
		}
		catch (CoreException e)
		{
			fail(e);
			return null; //
		}

	}

	/**
	 * @see TestUtilities#importProject(String, IPath, String, boolean)
	 * @param pluginID
	 * @param projectArchive
	 * @param override
	 * @param autoClean
	 * @return
	 */
	public IProject importProject(String pluginID, IPath projectArchive, String projectName, boolean override,
		boolean autoClean)
	{
		IProject project;
		try
		{
			project = TestUtilities.importProject(pluginID, projectArchive, projectName, override);
			if (autoClean)
			{
				addToCleanup(project);
			}
			return project;
		}
		catch (CoreException e)
		{
			fail(e);
			return null; //
		}
	}

	/**
	 * @throws Exception
	 */
	private void tearDownCessar() throws Exception
	{
		cleanUpProjects();
	}

	/**
	 * Assert that <code>searchInto</code> contains the <code>searchFor</code> String
	 *
	 * @param searchInto
	 * @param searchFor
	 */
	public static void assertContains(String searchInto, String searchFor)
	{
		assertNotNull("Search into cannot be null", searchInto);
		assertNotNull("Search for cannot be null", searchFor);
		assert (searchInto.indexOf(searchFor) > -1);
	}

	public void assertEquals(EObject obj1, EObject obj2)
	{
		EcoreEqualityAssert ecoreEqualityAssert = new EcoreEqualityAssert();
		ecoreEqualityAssert.assertEquals(obj1, obj2);
	}

	public void assertEquals(EObject obj1, EObject obj2, boolean ignoreParent)
	{
		EcoreEqualityAssert ecoreEqualityAssert = new EcoreEqualityAssert(ignoreParent);
		ecoreEqualityAssert.assertEquals(obj1, obj2);
	}

	public void assertEquals(EObject obj1, EObject obj2, boolean ignoreParent, String... featureToBeIgnored)
	{
		EcoreEqualityAssert ecoreEqualityAssert = new EcoreEqualityAssert(ignoreParent, featureToBeIgnored);
		ecoreEqualityAssert.assertEquals(obj1, obj2);
	}

	/**
	 * Check that an object is of a particular instance
	 *
	 * @param object
	 * @param clazz
	 */
	public static void assertInstance(Object object, Class<?> clazz)
	{
		assertInstance(NLS.bind("Object {0} is not an instance of {1}", object, clazz), object, clazz);
	}

	/**
	 * Check that an object is of a particular instance
	 *
	 * @param message
	 * @param object
	 * @param clazz
	 */
	public static void assertInstance(String message, Object object, Class<?> clazz)
	{
		assertNotNull(message);
		assertNotNull(clazz);
		assertTrue(message, clazz.isInstance(object));

	}

	/**
	 * Check expected vs actual value with a custom message.
	 *
	 * @param msg
	 *        the custom message
	 * @param expected
	 *        the expected {@code int} value
	 * @param actual
	 *        the actual {@code int} value
	 */
	public void assertEqualInts(String msg, int expected, int actual)
	{
		assertEquals(String.format(msg, expected, actual), expected, actual);
	}
}

/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu2337<br/>
 * Sep 16, 2013 4:50:27 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.testutils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import eu.cessar.ct.test.annotations.MethodOrder;

/**
 * Custom test suite class allowing ordering of test methods.
 * 
 * @author uidu2337
 * 
 *         %created_by: uidu2337 %
 * 
 *         %date_created: Tue Sep 24 17:50:03 2013 %
 * 
 *         %version: 4 %
 */
public class CessarTestSuite extends TestSuite
{
	/**
	 * Constructs a {@code CessarTestSuite} from a test class.
	 * 
	 * The test methods inside the class are _sorted_ according to {@code MethodOrder} annotations as first criterion,
	 * and alphabetically as second criterion.
	 * 
	 * @param theClass
	 *        the class containing the test methods
	 */
	public CessarTestSuite(final Class<?> theClass)
	{
		// Let the superclass do its magic...
		super(theClass);
		try
		{
			// ... but afterwards sort the private Vector field {@code TestSuite#fTests} containing the methods,
			// according
			// to MethodOrder and alphabetically.
			Class<?> testSuiteClass = TestSuite.class;
			Field testsField = testSuiteClass.getDeclaredField("fTests");
			testsField.setAccessible(true);
			Vector<Test> theFTests = (Vector<Test>) testsField.get(this);
			Collections.sort(theFTests, new TestComparator(theClass));
			testsField.setAccessible(false);
		}
		// If the method sorting results in a {@code Throwable}, store it as a dummy failed test, so that the class
		// doesn't have to throw exceptions further.
		catch (NoSuchFieldException e)
		{
			addTest(warning(e));
		}
		catch (SecurityException e)
		{
			addTest(warning(e));
		}
		catch (IllegalArgumentException e)
		{
			addTest(warning(e));
		}
		catch (IllegalAccessException e)
		{
			addTest(warning(e));
		}
	}

	/**
	 * Constructs a {@code CessarTestSuite} from the given array of classes.
	 * 
	 * The test methods inside the classes are _sorted_ according to {@code MethodOrder} annotations as first criterion,
	 * and alphabetically as second criterion.
	 * 
	 * @param classes
	 *        the classes containing the test methods
	 */
	public CessarTestSuite(Class<?>[] classes) throws SecurityException, IllegalArgumentException
	{
		for (Class<?> clazz: classes)
		{
			addOrderedTestSuite(clazz);
		}
	}

	/**
	 * No change in the {@code String}-based constructor.
	 * 
	 * @param string
	 *        the suite name
	 */
	public CessarTestSuite(String string)
	{
		super(string);
	}

	class TestComparator implements Comparator<Test>
	{
		Map<String, Integer> fMethodOrder;

		TestComparator(Class<?> theClass) throws SecurityException
		{
			fMethodOrder = new HashMap<String, Integer>();
			Class<?> superClass = theClass;
			while (Test.class.isAssignableFrom(superClass))
			{
				for (Method method: superClass.getDeclaredMethods())
				{
					MethodOrder mo = method.getAnnotation(MethodOrder.class);
					int order = (null != mo) ? mo.order() : MethodOrder.DEFAULT_ORDER;
					fMethodOrder.put(method.getName(), order);
				}
				superClass = superClass.getSuperclass();
			}
		}

		private String methodName(Test o1)
		{
			String method1FullName = o1.toString();
			return method1FullName.substring(0, method1FullName.indexOf('('));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Test o1, Test o2)
		{
			String methodName1 = methodName(o1);
			Integer order1 = fMethodOrder.get(methodName1);
			String methodName2 = methodName(o2);
			Integer order2 = fMethodOrder.get(methodName2);
			// MethodOrder is the first sorting criterion, alphabetical is the second one.
			return (order1.equals(order2)) ? methodName1.compareTo(methodName2) : order1.compareTo(order2);
		}
	}

	/**
	 * Returns a test which will fail and log a warning message.
	 */
	public static Test warning(final Throwable t)
	{
		return new TestCase("warning")
		{
			@Override
			protected void runTest() throws Throwable
			{
				throw t;
			}
		};
	}

	/**
	 * Adds the tests from the given class to the suite in an ordered manner.
	 */
	public void addOrderedTestSuite(Class<?> testClass)
	{
		addTest(new CessarTestSuite(testClass));
	}
}

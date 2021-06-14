/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu2337<br/>
 * Aug 9, 2013 8:47:56 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.testutils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.test.annotations.InitEObject;

/**
 * TestCase class that performs auto-initialization of involved model elements.
 * 
 * @author uidu2337
 * 
 *         %created_by: uidu2337 %
 * 
 *         %date_created: Wed Aug 21 14:21:36 2013 %
 * 
 *         %version: 4 %
 */
public abstract class AnnoTestCase extends CessarTestCase
{
	/*
	 * The options below should probably be in a class of their own, but for now they are kept like this for
	 * convenience.
	 */

	/**
	 * The conventional prefix of the fields that should be auto-initialized.
	 */
	protected static String TESTED_OBJECT_PREFIX = "ar_";

	/**
	 * The class of the fields to be auto-initialized.
	 */
	protected static Class<?> TESTED_CLASS = EObject.class;

	/**
	 * The class that can initialize the fields.
	 */
	protected static Class<?> TESTED_OBJECT_FACTORY = AnnoTestCase.class;

	/**
	 * The method that can initialize the fields. As String, because we have no easily specifiable function/method
	 * objects yet.
	 */
	protected static String TESTED_OBJECT_FACTORY_METHOD = "getEObjectWithQName";

	/**
	 * The fake separator used in field names instead of the '/' model path separator. Thanks RoKi!
	 */
	protected static char FAKE_URI_SEPARATOR = '$';

	/**
	 * The default model path separator.
	 */
	protected static char URI_SEPARATOR = '/';

	/**
	 * The factory method for the "TESTED_OBJECTS".
	 */
	private static Method TOP_METHOD;

	/**
	 * Used to perform a one-time initialization.
	 */
	private static volatile boolean loadedObjects = false;

	/**
	 * Default method to retrieve an EObject from a loaded project.
	 * 
	 * @param project
	 *        the project
	 * @param URI
	 *        the FQN corresponding to the EObject
	 * @return the EObject if successful
	 */
	public static EObject getEObjectWithQName(IProject project, String URI)
	{
		List<EObject> objects = EObjectLookupUtils.getEObjectsWithQName(project, URI);
		if (objects.size() > 1)
		{
			CessarPluginActivator.getDefault().logWarning(
				"Auto-initialization: multiple EObjects were found for qualified name: " + URI + "!");
		}
		return objects.get(0);
	}

	/**
	 * Set a private static field to a value.
	 * 
	 * @param field
	 *        the field
	 * @param value
	 *        the value
	 * @param <T>
	 *        the class of the value
	 */
	private <T> void setPrivateStaticField(Field field, T value)
	{
		try
		{
			field.set(null, value);
		}
		catch (Exception e)
		{
			CessarPluginActivator.getDefault().logError(
				"Auto-initialization: field " + field.getName() + " could not be initialized!");
		}
	}

	/**
	 * Invoke a static method with given {@code args}.
	 * 
	 * @param meth
	 *        the method
	 * @param args
	 *        the arguments
	 * @param <T>
	 *        the return type of the method
	 * @return the return value of the method or null if unsuccessful invocation
	 */
	private <T> T invokeStaticMethod(Method meth, Object... args)
	{
		try
		{
			return (T) meth.invoke(null, args);
		}
		catch (Exception e)
		{
			CessarPluginActivator.getDefault().logError(
				"Auto-initialization: method " + meth.getDeclaringClass() + "." + meth.getName()
					+ " could not be invoked!");
		}
		return null;
	}

	/**
	 * This attempts a one-time initialization of all the test objects.
	 * 
	 * @param project
	 *        the project
	 * @throws NoSuchMethodException
	 *         in case the method is not found
	 */
	protected void loadObjects(IProject project) throws NoSuchMethodException
	{
		if (!loadedObjects)
		{
			synchronized (AnnoTestCase.class)
			{
				if (!loadedObjects)
				{
					if ((null == TESTED_OBJECT_FACTORY) || (null == TESTED_OBJECT_FACTORY_METHOD))
					{
						return;
					}
					for (Field field: this.getClass().getDeclaredFields())
					{
						String fieldName = field.getName();
						if (fieldName.startsWith(TESTED_OBJECT_PREFIX))
						{
							if (null == TOP_METHOD)
							{
								TOP_METHOD = TESTED_OBJECT_FACTORY.getMethod(TESTED_OBJECT_FACTORY_METHOD,
									IProject.class, String.class);
							}

							Object value;

							field.setAccessible(true);

							InitEObject lObject = field.getAnnotation(InitEObject.class);
							if (null != lObject)
							{
								// Use the annotation with priority if it exists,
								value = invokeStaticMethod(TOP_METHOD, project, lObject.URI());
							}
							else
							{
								// otherwise attempt to use the field's name with / substituted for $ as path.
								String potentialInit = fieldName.substring(TESTED_OBJECT_PREFIX.length());
								value = invokeStaticMethod(TOP_METHOD, project,
									potentialInit.replace(FAKE_URI_SEPARATOR, URI_SEPARATOR));
							}
							setPrivateStaticField(field, value);
						}
					}
					loadedObjects = true;
				}
			}
		}
	}
}

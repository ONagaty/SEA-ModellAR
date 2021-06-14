/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 30, 2009 3:19:04 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.emf.ecore.EClass;

import eu.cessar.ct.core.platform.util.StringUtils;

/**
 * 
 */
public class PMClassLoaderUtils
{

	/**
	 * @param cl
	 * @param eClassQName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	public static final EClass locateEClass(ClassLoader cl, String eClassQName)
		throws ClassNotFoundException, SecurityException, NoSuchFieldException,
		IllegalArgumentException, IllegalAccessException, NoSuchMethodException,
		InvocationTargetException
	{
		int index = eClassQName.lastIndexOf('.');

		String eClassSimpleName = eClassQName.substring(index + 1);
		String packQName = eClassQName.substring(0, index);
		String lastPackName;
		index = packQName.lastIndexOf('.');
		if (index == -1)
		{
			lastPackName = packQName;
		}
		else
		{
			lastPackName = packQName.substring(index + 1);
		}
		// locate the packageClass
		String emfPackClassName = packQName + "." + StringUtils.toTitleCase(lastPackName)
			+ "Package";
		Class<?> emfPackClass = cl.loadClass(emfPackClassName);
		Field instanceField = emfPackClass.getField("eINSTANCE");
		Object instance = instanceField.get(null);
		Method method = emfPackClass.getMethod("get" + eClassSimpleName);
		EClass result = (EClass) method.invoke(instance);
		return result;
	}
}

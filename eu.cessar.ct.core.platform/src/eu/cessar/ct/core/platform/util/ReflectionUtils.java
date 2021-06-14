/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.sphinx.platform.util.ReflectUtil;

import eu.cessar.ct.core.internal.platform.CessarPluginActivator;

/**
 * Contains reflection utilities methods.
 * 
 */
public final class ReflectionUtils
{
	private static Map<Class<? extends Object>, Class<? extends Object>> primitiveClassToWrapperClass;
	private static Field modifiersField;

	static
	{
		primitiveClassToWrapperClass = new HashMap<Class<? extends Object>, Class<? extends Object>>();
		primitiveClassToWrapperClass.put(Boolean.TYPE, Boolean.class);
		primitiveClassToWrapperClass.put(Character.TYPE, Character.class);
		primitiveClassToWrapperClass.put(Byte.TYPE, Byte.class);
		primitiveClassToWrapperClass.put(Short.TYPE, Short.class);
		primitiveClassToWrapperClass.put(Integer.TYPE, Integer.class);
		primitiveClassToWrapperClass.put(Long.TYPE, Long.class);
		primitiveClassToWrapperClass.put(Float.TYPE, Float.class);
		primitiveClassToWrapperClass.put(Double.TYPE, Double.class);
		primitiveClassToWrapperClass.put(Void.TYPE, Void.class);
		try
		{
			modifiersField = Field.class.getDeclaredField("modifiers"); //$NON-NLS-1$
			modifiersField.setAccessible(true);
		}
		catch (SecurityException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		catch (NoSuchFieldException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	private ReflectionUtils()
	{
		// private constructor
	}

	/**
	 * Returns the wrapper class corresponding to the given primitive. For example, for the <code>boolean</code>
	 * primitive class, it will return the <code>java.lang.Boolean</code> class.
	 * 
	 * @param wrapperClass
	 * @return the wrapper class
	 */
	public static Class<? extends Object> getPrimitiveClass(Class<? extends Object> wrapperClass)
	{
		Set<Class<? extends Object>> keySet = primitiveClassToWrapperClass.keySet();
		for (Class<? extends Object> primitiveClass: keySet)
		{
			if (primitiveClassToWrapperClass.get(primitiveClass).equals(wrapperClass))
			{
				return primitiveClass;
			}
		}
		return null;
	}

	/**
	 * Returns the primitive class corresponding to the given wrapper class. For example, for the
	 * <code>java.lang.Boolean</code> class, it will return the <code>boolean</code> primitive class.
	 * 
	 * @param primitiveClass
	 * @return the primitive class
	 */
	public static Class<? extends Object> getWrapperClass(Class<? extends Object> primitiveClass)
	{
		return primitiveClassToWrapperClass.get(primitiveClass);
	}

	/**
	 * Set the value of a private or protected field to a particular value by using Java Reflection. The method is
	 * capable to change even final fields both static and non-static
	 * 
	 * @param clazz
	 *        the class that define the field
	 * @param obj
	 *        the object that hold the field. Shall be null for static fields
	 * @param fieldName
	 *        the name of the field
	 * @param value
	 *        the new value of the field, can be null
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static void setFieldValue(Class<? extends Object> clazz, Object obj, String fieldName, Object value)
		throws NoSuchFieldException, IllegalAccessException
	{
		Field field = ReflectUtil.findField(clazz, fieldName);
		if (field == null)
		{
			throw new NoSuchFieldException(fieldName);
		}
		boolean accessible = field.isAccessible();
		try
		{
			field.setAccessible(true);
			int modifiers = field.getModifiers();
			if (Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers()))
			{
				// we need to mark the field as non final first
				modifiersField.setInt(field, modifiers & ~Modifier.FINAL);
			}
			field.set(obj, value);
			if (Modifier.isFinal(modifiers) && Modifier.isStatic(field.getModifiers()))
			{
				// make the field final again
				modifiersField.setInt(field, modifiers);
			}
		}
		finally
		{
			field.setAccessible(accessible);
		}
	}

	/**
	 * Get the value of a private or protected field using Java Reflection
	 * 
	 * @param clazz
	 *        the class that define the field
	 * @param obj
	 *        the object that hold the field. Shall be null for static fields
	 * @param fieldName
	 *        the name of the field
	 * @return field value
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static Object getFieldValue(Class<? extends Object> clazz, Object obj, String fieldName)
		throws NoSuchFieldException, IllegalAccessException
	{
		Field field = ReflectUtil.findField(clazz, fieldName);
		Object fieldObject;
		if (field == null)
		{
			throw new NoSuchFieldException(fieldName);
		}
		boolean accessible = field.isAccessible();
		try
		{
			field.setAccessible(true);
			fieldObject = field.get(obj);
		}
		finally
		{
			field.setAccessible(accessible);
		}
		return fieldObject;
	}
}

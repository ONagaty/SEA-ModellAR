/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jan 25, 2010 10:53:36 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm.asm;

/**
 *
 */
/* package */abstract class AbstractPMBinaryClassWrapper<T> implements IPMBinaryClassWrapper
{

	private final T element;
	private String className;
	private String classFileName;
	private byte[] binary;
	private boolean haveImplInBinary;
	private final boolean implClass;
	private final IPMBinaryGeneratorSettings settings;

	public AbstractPMBinaryClassWrapper(IPMBinaryGeneratorSettings settings, T element, boolean implClass)
	{
		this.settings = settings;
		this.element = element;
		this.implClass = implClass;
	}

	protected abstract byte[] generateBinary(T elem, boolean genImplCode);

	protected abstract String generateClassFileName(T elem);

	protected abstract String generateClassName(T elem);

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.pm.asm.IPMBinaryClassWrapper#getBinary()
	 */
	public byte[] getBinary(boolean genImplCode)
	{
		if (binary == null || haveImplInBinary != genImplCode)
		{
			binary = generateBinary(element, genImplCode);
			haveImplInBinary = genImplCode;
		}
		return binary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.pm.asm.IPMBinaryClassWrapper#isImplClass()
	 */
	public boolean isImplClass()
	{
		return implClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.pm.asm.IPMBinaryClassWrapper#clearBinary()
	 */
	public void clearBinary()
	{
		binary = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.pm.asm.IPMBinaryClassWrapper#getClassFileName()
	 */
	public String getClassFileName()
	{
		if (classFileName == null)
		{
			classFileName = generateClassFileName(element);
		}
		return classFileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.pm.asm.IPMBinaryClassWrapper#getClassName()
	 */
	public String getClassName()
	{
		if (className == null)
		{
			className = generateClassName(element);
		}
		return className;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.pm.asm.IPMBinaryClassWrapper#getSettings()
	 */
	public IPMBinaryGeneratorSettings getSettings()
	{
		return settings;
	}

}

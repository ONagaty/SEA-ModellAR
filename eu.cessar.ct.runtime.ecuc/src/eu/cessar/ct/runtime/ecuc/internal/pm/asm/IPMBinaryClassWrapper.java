/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jan 25, 2010 10:43:16 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm.asm;


/**
 *
 */
public interface IPMBinaryClassWrapper
{

	public static final Object eINSTANCE = null;

	/**
	 * Return the binary class names. It will use "." as separator
	 *
	 * @return
	 */
	public String getClassName();

	/**
	 * Return the name of the file that should be used to store the class. It will use "/" as separator and will add
	 * also the ".class" extension
	 *
	 * @return
	 */
	public String getClassFileName();

	/**
	 * Get the binary stream used for this class
	 *
	 * @return
	 */
	public byte[] getBinary(boolean createImplCode);

	/**
	 *
	 */
	public void clearBinary();

	/**
	 * Return true if the binary is part of the implementation, false otherwise
	 *
	 * @return
	 */
	public boolean isImplClass();

	/**
	 * Return the settings that should be used while generating this class
	 *
	 * @return
	 */
	public IPMBinaryGeneratorSettings getSettings();

}

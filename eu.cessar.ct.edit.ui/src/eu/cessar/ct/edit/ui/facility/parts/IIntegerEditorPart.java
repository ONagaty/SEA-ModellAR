/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Mar 25, 2010 12:12:13 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import eu.cessar.ct.core.platform.util.ERadix;

/**
 * @author uidl7321
 * 
 */
public interface IIntegerEditorPart extends IEditorPart
{
	/**
	 * 
	 * @param radix
	 */
	public void setRadix(ERadix radix);

	/**
	 * 
	 * @return
	 */
	public ERadix getRadix();

}

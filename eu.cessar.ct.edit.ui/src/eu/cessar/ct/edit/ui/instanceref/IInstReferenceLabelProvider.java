/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jun 10, 2010 6:31:29 PM </copyright>
 */
package eu.cessar.ct.edit.ui.instanceref;

/**
 * @author uidl6870
 * @param <V>
 * 
 */
public interface IInstReferenceLabelProvider<V> extends IReferenceLabelProvider<V>
{

	/**
	 * @return
	 */
	String getTargetCaptionTooltip();

	/**
	 * @param pureMM
	 * @return
	 */
	String getContextCaptionTooltip(boolean pureMM);
}

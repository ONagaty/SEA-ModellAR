/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Aug 29, 2011 2:01:46 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.composition;

import org.eclipse.emf.ecore.EClass;

import eu.cessar.ct.edit.ui.facility.composition.ECompositionType;
import eu.cessar.ct.edit.ui.facility.composition.ISystemCompostion;
import eu.cessar.ct.edit.ui.facility.composition.SystemCategory;

/**
 * @author uidt2045
 * 
 */
public class SystemComposition extends AbstractEditorComposition<SystemCategory> implements
	ISystemCompostion
{

	private SystemCategory sysCategory;

	public SystemComposition(SystemCategory sysCategory, IEditorCompositionProvider provider)
	{
		super(provider);
		this.sysCategory = sysCategory;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.IEditorComposition#getType()
	 */
	public ECompositionType getType()
	{
		return ECompositionType.SYSTEM;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.IEditorComposition#getCategory()
	 */
	public SystemCategory getCategory()
	{
		return sysCategory;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.IEditorComposition#setCategory(eu.cessar.ct.edit.ui.ICompositionCategory)
	 */
	public void setCategory(SystemCategory systemCategory)
	{
		this.sysCategory = systemCategory;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();

		sb.append(super.toString());

		sb.append("\nInput type: "); //$NON-NLS-1$

		EClass clz = getCategory().getInput();
		sb.append(clz.getEPackage().getName());
		sb.append("."); //$NON-NLS-1$
		sb.append(clz.getName());

		return sb.toString();
	}

}

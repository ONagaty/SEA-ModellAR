/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Sep 1, 2011 10:05:17 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.composition;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import eu.cessar.ct.core.platform.util.StringUtils;

/**
 * @author uidt2045
 * 
 */
public class SystemCategory implements ICompositionCategory<EClass>
{

	private EClass eclass;
	private EReference ref;

	public SystemCategory(EClass cls, EReference ref)
	{
		this.eclass = cls;
		this.ref = ref;
	}

	public String getName()
	{
		return eclass.getName();
	}

	public String getFullName()
	{
		StringBuffer sb = new StringBuffer();

		String featNameHumanized = StringUtils.humaniseCamelCase(ref.getName());

		String featNameHumanizedToTitleCase = StringUtils.toTitleCase(featNameHumanized);
		sb.append(featNameHumanizedToTitleCase);
		sb.append("->"); //$NON-NLS-1$
		sb.append(eclass.getName());

		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.ICompositionCategory#getInput()
	 */
	public EClass getInput()
	{
		return eclass;
	}

	/**
	 * 
	 * @return
	 */
	public EReference getReference()
	{
		return ref;
	}

}

/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Oct 14, 2009 4:48:23 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gautosar.ggenericstructure.ginfrastructure.GARPackage;

/**
 * 
 */
public class GPackageEcucEntry extends EcucEntry
{

	private final Set<GARPackage> packages = new HashSet<GARPackage>();

	private final String packageName;

	public GPackageEcucEntry(GARPackage gPackage)
	{
		packageName = gPackage.gGetShortName();
		packages.add(gPackage);
	}

	public String getPackageName()
	{
		return packageName;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.internal.model.EcucEntry#getName()
	 */
	@Override
	public String getName()
	{
		return getPackageName();
	}

	/**
	 * @param ownerPackage
	 */
	public void addPackage(GARPackage ownerPackage)
	{
		// add the package to the list
		packages.add(ownerPackage);
	}

	/**
	 * @return
	 */
	public List<GARPackage> getPackages()
	{
		return Collections.unmodifiableList(new ArrayList<GARPackage>(packages));
	}

}

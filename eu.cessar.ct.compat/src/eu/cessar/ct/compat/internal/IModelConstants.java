/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 22, 2010 11:07:31 AM </copyright>
 */
package eu.cessar.ct.compat.internal;

import org.artop.aal.gautosar.services.IMetaModelService;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;

/**
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public interface IModelConstants extends IMetaModelService
{

	public String getCNameModuleDef();

	public EClass getEClassIContainerDef();

	public Class<?> getMasterARObjectClass();

	public EClass getEREFEClass();

	public Class<?> getEREFClass();

	public EClass getMissingContainerEClass();

	public EClass getSlaveEClass(EClass masterEClass);

	public EClass getMasterEClass(String name);

	public EPackage getMasterRootPackage();

	public EFactory getMasterRootFactory();

	public int getAutosarReleaseOrdinal();

	public String getSlaveMMURI();

}

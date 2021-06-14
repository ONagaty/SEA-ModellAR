/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 13, 2010 2:14:21 PM </copyright>
 */
package eu.cessar.ct.compat.internal.pm.adapt;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.internal.pm.asm.DelegatedEPackageImpl;
import eu.cessar.ct.runtime.ecuc.pm.AbstractPMInfo;
import eu.cessar.ct.runtime.ecuc.pm.EMFPMUtils;
import eu.cessar.ct.runtime.ecuc.pm.IPMCompatElementInfo;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class RootNodeCompatPMInfo extends AbstractPMInfo<IProject> implements
	IPMCompatElementInfo<IProject>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.AbstractPMElementInfo#create()
	 */
	@Override
	protected void create(IEcucPresentationModel ecucPM)
	{
		IProject project = ecucPM.getProject();
		parentPackage = new DelegatedEPackageImpl(project, ecucPM.getProxyEngine());
		subPackage = parentPackage;
		parentPackage.setName(getPackageName());
		parentPackage.setNsPrefix(getNSPrefix());
		parentPackage.setNsURI(getNsURI(project));
		createClassifier();
	}

	/**
	 * @return
	 */
	protected String getNSPrefix()
	{
		return "ecuc"; //$NON-NLS-1$
	}

	/**
	 * @return
	 */
	protected String getPackageName()
	{
		return "ecuc"; //$NON-NLS-1$
	}

	/**
	 * @param ecucPM
	 */
	protected void createClassifier()
	{
		EClass clz = ecoreFactory.createEClass();
		classifier = clz;
		clz.getESuperTypes().addAll(getClassifierSuperclasses());
		clz.setName(getClassifierName());
		EMFPMUtils.setProxyAnnotation(clz, "TYPE", "RootNode"); //$NON-NLS-1$//$NON-NLS-2$
		parentPackage.getEClassifiers().add(classifier);

		// another classifier for the IModule need to be created
		clz = ecoreFactory.createEClass();
		clz.setName("IModule"); //$NON-NLS-1$
		clz.setAbstract(true);
		clz.setInterface(true);
		EMFPMUtils.setProxyAnnotation(clz, "TYPE", "IModule"); //$NON-NLS-1$//$NON-NLS-2$
		parentPackage.getEClassifiers().add(clz);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo#getSubPackage()
	 */
	public EPackage getSubPackage(IEcucPresentationModel ecucPM, boolean create)
	{
		return subPackage;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.internal.pm.adapt.RootNodePMInfo#getNsURI(org.eclipse.core.resources.IProject)
	 */
	protected String getNsURI(IProject project)
	{
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(project);
		String uri = mmService.getEcucMMService().getPresentationModelURI(project, true);
		return uri;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.internal.pm.adapt.RootNodePMInfo#getClassifierName()
	 */
	protected String getClassifierName()
	{
		return "RootNode"; //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.internal.pm.adapt.RootNodePMInfo#getClassifierSuperclasses()
	 */
	protected List<EClass> getClassifierSuperclasses()
	{
		return Collections.emptyList();
	}
}

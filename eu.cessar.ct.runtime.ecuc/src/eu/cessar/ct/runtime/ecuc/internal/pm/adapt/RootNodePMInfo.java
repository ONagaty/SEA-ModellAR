/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 13, 2010 2:14:21 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm.adapt;

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
import eu.cessar.ct.sdk.pm.pmPackage;

/**
 * 
 */
public class RootNodePMInfo extends AbstractPMInfo<IProject>
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
		createClassifier(ecucPM);
	}

	/**
	 * @param project
	 * @return
	 */
	protected String getNsURI(IProject project)
	{
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(project);
		String uri = mmService.getEcucMMService().getPresentationModelURI(project, false);
		return uri;
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
	protected void createClassifier(IEcucPresentationModel ecucPM)
	{
		EClass clz = ecoreFactory.createEClass();
		classifier = clz;
		clz.getESuperTypes().addAll(getClassifierSuperclasses());
		clz.setName(getClassifierName());
		updateClassifierAnnotations(clz);
		parentPackage.getEClassifiers().add(classifier);
	}

	/**
	 * @param clz
	 */
	protected void updateClassifierAnnotations(EClass clz)
	{
		EMFPMUtils.setProxyAnnotation(clz, "TYPE", "RootNode");
	}

	protected String getClassifierName()
	{
		return "PresentationModel"; //$NON-NLS-1$
	}

	protected List<EClass> getClassifierSuperclasses()
	{
		return Collections.singletonList(pmPackage.Literals.IPRESENTATION_MODEL);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo#getSubPackage()
	 */
	public EPackage getSubPackage(IEcucPresentationModel ecucPM, boolean create)
	{
		return subPackage;
	}
}

/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 29, 2010 2:26:46 PM </copyright>
 */
package eu.cessar.ct.jet.core.compiler;

import org.eclipse.jdt.core.IJavaProject;

import eu.cessar.ct.jet.core.compliance.EJetComplianceLevel;

/**
 * @author uidl6458
 * 
 */
public abstract class AbstractJetContentProvider implements IJETContentProvider
{

	private EJetComplianceLevel complianceLevel;
	private String className;
	private IJavaProject javaProject;

	/**
	 * @param complianceLevel
	 */
	public AbstractJetContentProvider(IJavaProject javaProject, EJetComplianceLevel complianceLevel)
	{
		this.javaProject = javaProject;
		this.complianceLevel = complianceLevel;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.jet.core.compiler.IJETContentProvider#getJetComplianceLevel()
	 */
	public EJetComplianceLevel getJetComplianceLevel()
	{
		// TODO Auto-generated method stub
		return complianceLevel;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.jet.core.compiler.IJETContentProvider#getOutputClass()
	 */
	public String getOutputClass()
	{
		return className;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.jet.core.compiler.IJETContentProvider#setOutputClass(java.lang.String)
	 */
	public void setOutputClass(String ocName)
	{
		className = ocName;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.jet.core.compiler.IJETContentProvider#getJavaProject()
	 */
	public IJavaProject getJavaProject()
	{
		return javaProject;
	}

}

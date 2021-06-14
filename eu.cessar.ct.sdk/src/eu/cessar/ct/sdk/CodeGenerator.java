/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 12, 2010 10:55:51 AM </copyright>
 */
package eu.cessar.ct.sdk;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;

import eu.cessar.ct.sdk.logging.ILogger;
import eu.cessar.ct.sdk.logging.LoggerFactory;

/**
 * The abstract base class all code generator will extend
 * 
 */
public abstract class CodeGenerator implements ICodeGenerator
{

	private final IProject project;
	private final IFolder outputFolder;
	private String outputFileName;

	public CodeGenerator(IProject project, IFolder outputFolder, String outputFileName)
	{
		this.project = project;
		this.outputFolder = outputFolder;
		this.outputFileName = outputFileName;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.ICodeGenerator#getOutputFileName()
	 */
	public String getOutputFileName()
	{
		return outputFileName;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.ICodeGenerator#getProject()
	 */
	public IProject getProject()
	{
		return project;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.ICodeGenerator#getOutputFolder()
	 */
	public IFolder getOutputFolder()
	{
		return outputFolder;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.ICodeGenerator#setOutputFileName(java.lang.String)
	 */
	public void setOutputFileName(String fileName)
	{
		outputFileName = fileName;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.ICodeGenerator#getLogger()
	 */
	public ILogger getLogger()
	{
		return LoggerFactory.getLogger();
	}

}

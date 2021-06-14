/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321<br/>
 * Feb 24, 2014 2:33:33 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace.internal.consistencycheck.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.artop.aal.common.metamodel.AutosarMetaModelVersionData;
import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;
import org.osgi.framework.Version;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eu.cessar.ct.core.mms.FileExtensionPreferenceAccessor;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.sdk.logging.LoggerFactory;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.ct.sdk.utils.ProjectUtils;
import eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType;
import eu.cessar.ct.workspace.consistencycheck.ESeverity;
import eu.cessar.ct.workspace.consistencycheck.IConsistencyCheckResult;
import eu.cessar.ct.workspace.consistencycheck.IProjectCheckInconsistency;
import eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyChecker;
import eu.cessar.ct.workspace.internal.Messages;
import eu.cessar.req.Requirement;

/**
 * Checks if there are files with a wrong metamodel (wrong major or minor revisions) in a project and returns a result
 * with additional details.
 * 
 * @author uidl7321
 * 
 *         %created_by: uidw8762 %
 * 
 *         %date_created: Thu May 29 11:45:25 2014 %
 * 
 *         %version: 10 %
 */
@Requirement(
	reqID = "REQ_CHECK#5")
public class CheckForWrongMetamodel implements IProjectConsistencyChecker
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.workspace.internal.consistencycheck.IProjectConsistencyChecker#performConsistencyCheck(org.eclipse
	 * .core.resources.IProject)
	 */
	@Override
	public List<IConsistencyCheckResult<IProjectCheckInconsistency>> performConsistencyCheck(IProject project)
	{
		List<IConsistencyCheckResult<IProjectCheckInconsistency>> resultList = new ArrayList<IConsistencyCheckResult<IProjectCheckInconsistency>>();

		List<IProjectCheckInconsistency> projectCheckList = getAllProjectFilesVersionCheckResults(project);

		IConsistencyCheckResult<IProjectCheckInconsistency> checkResult = new ProjectConsistencyCheckResult();
		checkResult.setInconsistencies(projectCheckList);
		checkResult.setStatus(Status.OK_STATUS);
		resultList.add(checkResult);

		return resultList;
	}

	/**
	 * Gets the project version.
	 * 
	 * @param project
	 *        the project
	 * @return the project version
	 */
	private Version getProjectVersion(IProject project)
	{
		AutosarReleaseDescriptor autosarProjectReleaseDescriptor = MetaModelUtils.getAutosarRelease(project);
		AutosarMetaModelVersionData autosarProjectMetaModelVersionData = autosarProjectReleaseDescriptor.getAutosarVersionData();
		Version nativeAUTOSARReleaseVersion = ModelUtils.getNativeAUTOSARRelease(autosarProjectMetaModelVersionData.getMajor());

		return nativeAUTOSARReleaseVersion;
	}

	/**
	 * Gets the all project files version check results.
	 * 
	 * Compare versions based on the specified criteria The applied rule is that AUTOSAR files having a different major
	 * version than the project version must be treated as errors and the ones having a different minor/patch version
	 * must be treated as warnings.
	 * 
	 * @param project
	 *        the project
	 * @return the all project files version check results
	 */
	private List<IProjectCheckInconsistency> getAllProjectFilesVersionCheckResults(IProject project)
	{
		Version projectVersion = getProjectVersion(project);

		IContainer projectFolder = project.getFolder(project.getFullPath());

		String[] filterExtensions = FileExtensionPreferenceAccessor.getExtensions(project);
		List<IFile> files = new ArrayList<IFile>();

		try
		{
			for (int i = 0; i < filterExtensions.length; i++)
			{
				filterExtensions[i] = "*." + filterExtensions[i]; //$NON-NLS-1$  
				files.addAll(ProjectUtils.getMembers(projectFolder.getParent(), filterExtensions[i], true));
			}
		}
		catch (CoreException e)
		{
			LoggerFactory.getLogger().error(e.getMessage());
		}

		List<IProjectCheckInconsistency> checkList = new ArrayList<IProjectCheckInconsistency>();
		boolean isVersionInconsistency = false;
		List<IFile> wrongVersionFiles = new ArrayList<IFile>();
		ProjectCheckInconsistency projectCheckInconsistency = null;

		Version fileVersion = null;

		for (IFile file: files)
		{
			fileVersion = getXPathFileVersion(file);

			if (fileVersion == null)
			{
				LoggerFactory.getLogger().error(NLS.bind(Messages.error_invalidVersion, file.getFullPath()));
				continue;
			}

			isVersionInconsistency = false;

			if (projectCheckInconsistency != null)
			{
				projectCheckInconsistency = null;
			}

			projectCheckInconsistency = new ProjectCheckInconsistency();
			isVersionInconsistency = false;

			if (fileVersion.getMajor() != projectVersion.getMajor())
			{
				projectCheckInconsistency.setSeverity(ESeverity.ERROR);
				isVersionInconsistency = true;
			}
			else
			{
				if (fileVersion.getMinor() != projectVersion.getMinor())
				{
					projectCheckInconsistency.setSeverity(ESeverity.WARNING);
					isVersionInconsistency = true;
				}
			}

			if (isVersionInconsistency)
			{
				wrongVersionFiles.clear();
				wrongVersionFiles.add(file);

				projectCheckInconsistency.addFiles(wrongVersionFiles);
				projectCheckInconsistency.setInconsistencyType(EProjectInconsistencyType.WRONG_MODEL);
				projectCheckInconsistency.setMessage(NLS.bind(Messages.wrongModel_inconsistency,
					fileVersion.toString(), projectVersion.toString()));
				checkList.add(projectCheckInconsistency);
			}
		}

		return checkList;
	}

	/**
	 * Gets the x path file version.
	 * 
	 * @param xmlFile
	 *        the xml file
	 * @return the x path file version
	 */
	private Version getXPathFileVersion(IFile xmlFile)
	{
		Version version = null;
		String xmlFilePath = xmlFile.getLocation().toOSString();
		XPath xpath = XPathFactory.newInstance().newXPath();
		String xpathExpression = "/AUTOSAR"; //$NON-NLS-1$

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document xmlDocument = null;
		NodeList nodes = null;
		try
		{
			builder = builderFactory.newDocumentBuilder();
			xmlDocument = builder.parse(new File(xmlFilePath));
			nodes = (NodeList) xpath.compile(xpathExpression).evaluate(xmlDocument, XPathConstants.NODESET);
		}
		catch (ParserConfigurationException e)
		{
			LoggerFactory.getLogger().error(e.getMessage());
		}
		catch (SAXException e)
		{
			LoggerFactory.getLogger().error(e.getMessage());
		}
		catch (IOException e)
		{
			LoggerFactory.getLogger().error(e.getMessage());
		}
		catch (XPathExpressionException e)
		{
			LoggerFactory.getLogger().error(e.getMessage());
		}

		NamedNodeMap attributes = null;
		Node nodeSchemaLocation = null;

		if (nodes.getLength() > 0)
		{
			// Read AUTOSAR schema location
			attributes = nodes.item(0).getAttributes();
			nodeSchemaLocation = attributes.getNamedItem(MetaModelUtils.SCHEMA_LOCATION_ATTR);

			if (nodeSchemaLocation != null)
			{

				String versionValue = nodeSchemaLocation.getNodeValue().toUpperCase();
				boolean isValidVersion = false;

				String schemaLocationVersion = versionValue;

				// Verify schema location
				int schemaLocationVersionIndex = -1;
				int schemaLocationVersionLength = -1;

				if (schemaLocationVersion.indexOf("AUTOSAR.ORG/SCHEMA/R") > -1) //$NON-NLS-1$
				{
					schemaLocationVersionIndex = schemaLocationVersion.indexOf("AUTOSAR.ORG/SCHEMA/R"); //$NON-NLS-1$
					schemaLocationVersionLength = 20;
				}
				if (schemaLocationVersionIndex == -1)
				{
					schemaLocationVersionIndex = schemaLocationVersion.indexOf("AUTOSAR.ORG/"); //$NON-NLS-1$
					schemaLocationVersionLength = 12;
				}

				if (schemaLocationVersionIndex > -1)
				{
					schemaLocationVersion = schemaLocationVersion.substring(schemaLocationVersionIndex
						+ schemaLocationVersionLength);
					schemaLocationVersion = schemaLocationVersion.replace(" ", ""); //$NON-NLS-1$ //$NON-NLS-2$
					schemaLocationVersion = schemaLocationVersion.trim();

					if (!schemaLocationVersion.isEmpty())
					{
						schemaLocationVersion = schemaLocationVersion.substring(schemaLocationVersion.lastIndexOf("AUTOSAR_") + 8); //$NON-NLS-1$
						schemaLocationVersion = schemaLocationVersion.substring(0,
							schemaLocationVersion.indexOf(".XSD")); //$NON-NLS-1$
						schemaLocationVersion = schemaLocationVersion.replace("-", "."); //$NON-NLS-1$ //$NON-NLS-2$
						schemaLocationVersion = schemaLocationVersion.replace(" ", ""); //$NON-NLS-1$ //$NON-NLS-2$			
					}
				}
				// Verify schema location attribute
				String schemaLocationAttributeVersion = versionValue;

				if (schemaLocationAttributeVersion.indexOf("AUTOSAR_") > -1) //$NON-NLS-1$
				{
					schemaLocationAttributeVersion = schemaLocationAttributeVersion.substring(schemaLocationAttributeVersion.indexOf("AUTOSAR_") + 8); //$NON-NLS-1$

					if (!schemaLocationAttributeVersion.isEmpty())
					{
						schemaLocationAttributeVersion = schemaLocationAttributeVersion.substring(0,
							schemaLocationAttributeVersion.indexOf(".XSD")); //$NON-NLS-1$
						schemaLocationAttributeVersion = schemaLocationAttributeVersion.replace("-", "."); //$NON-NLS-1$ //$NON-NLS-2$
						schemaLocationAttributeVersion = schemaLocationAttributeVersion.replace(" ", ""); //$NON-NLS-1$ //$NON-NLS-2$

						isValidVersion = schemaLocationVersion.substring(0, 5).equals(
							schemaLocationAttributeVersion.substring(0, 5));
					}
				}
				if (isValidVersion)
				{
					version = new Version(schemaLocationVersion);
				}
			}
		}
		return version;
	}
}

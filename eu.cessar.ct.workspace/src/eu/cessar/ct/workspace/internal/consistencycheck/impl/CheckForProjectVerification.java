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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Status;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eu.cessar.ct.core.platform.PlatformConstants;
import eu.cessar.ct.sdk.logging.LoggerFactory;
import eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType;
import eu.cessar.ct.workspace.consistencycheck.ESeverity;
import eu.cessar.ct.workspace.consistencycheck.IConsistencyCheckResult;
import eu.cessar.ct.workspace.consistencycheck.IProjectCheckInconsistency;
import eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyChecker;
import eu.cessar.ct.workspace.internal.Messages;

/**
 * Checks if .project file to have the required natures and build commands inside the project
 * 
 * @author uidu8153
 * 
 *         %created_by: uidu8153 %
 * 
 *         %date_created: Thu Jun 25 08:53:33 2015 %
 * 
 *         %version: 2 %
 */
public class CheckForProjectVerification implements IProjectConsistencyChecker
{

	/**
	 * Java builder identifier
	 */
	public static final String JAVA_BUILDER = "org.eclipse.jdt.core.javabuilder"; //$NON-NLS-1$

	@Override
	public List<IConsistencyCheckResult<IProjectCheckInconsistency>> performConsistencyCheck(IProject project)
	{
		List<IConsistencyCheckResult<IProjectCheckInconsistency>> resultList = new ArrayList<IConsistencyCheckResult<IProjectCheckInconsistency>>();

		List<IProjectCheckInconsistency> projectCheckList = getProjectVerificationCheckResults(project);

		IConsistencyCheckResult<IProjectCheckInconsistency> checkResult = new ProjectConsistencyCheckResult();
		checkResult.setInconsistencies(projectCheckList);
		checkResult.setStatus(Status.OK_STATUS);
		resultList.add(checkResult);

		return resultList;
	}

	/**
	 * Collects the Project Verification results
	 * 
	 * @param project
	 * @return in a List
	 */
	private List<IProjectCheckInconsistency> getProjectVerificationCheckResults(IProject project)
	{

		String fileName = Messages.CheckForProjectVerification_project_FolderName;
		List<IProjectCheckInconsistency> checkList = new ArrayList<IProjectCheckInconsistency>();

		IFile file = project.getFile(fileName);
		if (file.exists())
		{

			String xmlFilePath = file.getLocation().toOSString();
			checkProjectVerificationForBuildCommand(xmlFilePath, checkList, file);

			checkProjectVerificationForNatures(xmlFilePath, checkList, file);

		}

		return checkList;

	}

	/**
	 * Creates an Inconsistency for Project Verification.
	 * 
	 * @param checkList
	 * @param projectCheckInconsistency
	 * @param file
	 * @param message
	 */
	private void addInconsistencyForProject(List<IProjectCheckInconsistency> checkList,
		ProjectCheckInconsistency projectCheckInconsistency, IFile file, String message)
	{
		projectCheckInconsistency.addFile(file);
		projectCheckInconsistency.setInconsistencyType(EProjectInconsistencyType.PROJECT_PROBLEMS);
		projectCheckInconsistency.setMessage(message);
		checkList.add(projectCheckInconsistency);
	}

	/**
	 * checks if .project has Build command set.
	 * 
	 * @param xmlFilePath
	 * @param checkList
	 * @param file
	 * @return
	 */
	private List<IProjectCheckInconsistency> checkProjectVerificationForBuildCommand(String xmlFilePath,
		List<IProjectCheckInconsistency> checkList, IFile file)
	{

		ProjectCheckInconsistency projectCheckInconsistency = null;
		NodeList childNodes = getChildNodeList(xmlFilePath, Messages.CheckForProjectVerification_buildCommand_nodePath);
		Node item;
		String nodeName;

		for (int cidx = 0; cidx < childNodes.getLength(); cidx++)
		{
			item = childNodes.item(cidx);

			nodeName = item.getNodeName();

			if (nodeName.equals("name")) //$NON-NLS-1$
			{

				String nodeContent = item.getTextContent();
				if (!nodeContent.equals(JAVA_BUILDER))
				{
					projectCheckInconsistency = new ProjectCheckInconsistency();
					projectCheckInconsistency.setSeverity(ESeverity.WARNING);

					addInconsistencyForProject(checkList, projectCheckInconsistency, file,
						Messages.CheckForProjectVerification_error_BuildCommands);

				}
			}

		}
		return checkList;
	}

	/**
	 * checks if .project has required natures set.
	 * 
	 * @param xmlFilePath
	 * @param checkList
	 * @param file
	 * @return
	 */
	private List<IProjectCheckInconsistency> checkProjectVerificationForNatures(String xmlFilePath,
		List<IProjectCheckInconsistency> checkList, IFile file)
	{

		ProjectCheckInconsistency projectCheckInconsistency = null;
		ArrayList<String> nodeContent = new ArrayList<String>();
		NodeList childNodes = getChildNodeList(xmlFilePath, Messages.CheckForProjectVerification_natures_nodePath);
		Node item;
		String nodeName;
		for (int cidx = 0; cidx < childNodes.getLength(); cidx++)
		{
			item = childNodes.item(cidx);
			nodeName = item.getNodeName();

			if (nodeName.equals("nature")) //$NON-NLS-1$
			{
				String nature = item.getTextContent();
				nodeContent.add(nature);
			}

		}

		if (!nodeContent.contains(PlatformConstants.CESSAR_NATURE))
		{
			projectCheckInconsistency = new ProjectCheckInconsistency();
			projectCheckInconsistency.setSeverity(ESeverity.WARNING);

			addInconsistencyForProject(checkList, projectCheckInconsistency, file,
				Messages.CheckForProjectVerification_error_projectNatures);

		}

		return checkList;
	}

	/**
	 * checks if either build command and natures is set.
	 * 
	 * @param xmlFilePath
	 * @param xNodePath
	 * @return
	 */
	private NodeList getChildNodeList(String xmlFilePath, String xNodePath)
	{
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document xmlDocument = null;

		NodeList childNodes = null;
		XPath xpath = XPathFactory.newInstance().newXPath();

		try
		{
			builder = builderFactory.newDocumentBuilder();
			xmlDocument = builder.parse(new File(xmlFilePath));
			if (xmlDocument != null)
			{
				NodeList nodes = (NodeList) xpath.compile(xNodePath).evaluate(xmlDocument, XPathConstants.NODESET);
				Node item;
				for (int idx = 0; idx < nodes.getLength(); idx++)
				{
					item = nodes.item(idx);

					childNodes = item.getChildNodes();

				}
			}
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
		return childNodes;
	}
}
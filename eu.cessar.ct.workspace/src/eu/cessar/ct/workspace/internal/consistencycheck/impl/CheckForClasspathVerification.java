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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.sdk.logging.LoggerFactory;
import eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType;
import eu.cessar.ct.workspace.consistencycheck.ESeverity;
import eu.cessar.ct.workspace.consistencycheck.IConsistencyCheckResult;
import eu.cessar.ct.workspace.consistencycheck.IProjectCheckInconsistency;
import eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyChecker;
import eu.cessar.ct.workspace.internal.Messages;

/**
 * Checks if .classpath to have the required classpath libraries inside the project
 * 
 * @author uidu8153
 * 
 *         %created_by: uidu8153 %
 * 
 *         %date_created: Thu Jun 25 08:53:23 2015 %
 * 
 *         %version: 2 %
 */
public class CheckForClasspathVerification implements IProjectConsistencyChecker
{

	@Override
	public List<IConsistencyCheckResult<IProjectCheckInconsistency>> performConsistencyCheck(IProject project)
	{
		List<IConsistencyCheckResult<IProjectCheckInconsistency>> resultList = new ArrayList<IConsistencyCheckResult<IProjectCheckInconsistency>>();

		List<IProjectCheckInconsistency> classpathCheckList = getClasspathVerificationCheckResults(project);

		IConsistencyCheckResult<IProjectCheckInconsistency> checkResult = new ProjectConsistencyCheckResult();
		checkResult.setInconsistencies(classpathCheckList);
		checkResult.setStatus(Status.OK_STATUS);
		resultList.add(checkResult);

		return resultList;
	}

	/**
	 * Collects the classpath Verification results
	 * 
	 * @param project
	 * @return in a List
	 */
	private List<IProjectCheckInconsistency> getClasspathVerificationCheckResults(IProject project)
	{

		String fileName = Messages.CheckForClasspathVerification_classpath;
		List<IProjectCheckInconsistency> checkList = new ArrayList<IProjectCheckInconsistency>();

		IFile file = project.getFile(fileName);
		if (file.exists())
		{

			String xmlFilePath = file.getLocation().toOSString();
			checkProjectVerificationForClasspathEntry(xmlFilePath, checkList, file);
		}

		return checkList;

	}

	/**
	 * Creates an Inconsistency for Classpath Verification.
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
		projectCheckInconsistency.setInconsistencyType(EProjectInconsistencyType.CLASSPATH_PROBLEMS);
		projectCheckInconsistency.setMessage(message);
		checkList.add(projectCheckInconsistency);
	}

	/**
	 * checks if .classpath has classpath libraries set.
	 * 
	 * @param xmlFilePath
	 * @param checkList
	 * @param file
	 * @return
	 */
	private List<IProjectCheckInconsistency> checkProjectVerificationForClasspathEntry(String xmlFilePath,
		List<IProjectCheckInconsistency> checkList, IFile file)
	{

		ProjectCheckInconsistency projectCheckInconsistency = null;
		ArrayList<String> pathItemList = new ArrayList<String>();
		ArrayList<String> classPathEntries = getClassPathEntries();
		NodeList childNodes = getChildNodeList(xmlFilePath, Messages.CheckForClasspathVerification_classpath_Node);

		Node item;
		String nodeName;
		for (int cidx = 0; cidx < childNodes.getLength(); cidx++)
		{
			item = childNodes.item(cidx);
			nodeName = item.getNodeName();
			if (nodeName.equals("classpathentry")) //$NON-NLS-1$
			{
				NamedNodeMap attributes = item.getAttributes();

				Node pathItem = attributes.getNamedItem(Messages.CheckForClasspathVerification_path_Node);

				pathItemList.add(pathItem.getTextContent());
			}
		}
		if ((!checkIfClasspathEntryHasLibraries(classPathEntries, pathItemList)))
		{
			projectCheckInconsistency = new ProjectCheckInconsistency();
			projectCheckInconsistency.setSeverity(ESeverity.WARNING);

			addInconsistencyForProject(checkList, projectCheckInconsistency, file,
				Messages.CheckForClasspathVerification_error_classpathLibraries);

		}

		return checkList;
	}

	/**
	 * @return list of classpath entries
	 */
	private ArrayList<String> getClassPathEntries()
	{
		ArrayList<String> classPathEntries = new ArrayList<String>();
		classPathEntries.add(CessarRuntime.CONTAINER_ID_MODEL);
		classPathEntries.add(CessarRuntime.CONTAINER_ID_GENERIC);
		classPathEntries.add(CessarRuntime.CONTAINER_ID_PROJECT);
		return classPathEntries;
	}

	/**
	 * checks if the classpath libraries exists inside the list
	 * 
	 * @param classPathEntries
	 * @param nodes
	 * @return
	 */
	private boolean checkIfClasspathEntryHasLibraries(ArrayList<String> classPathEntries, ArrayList<String> nodes)
	{
		boolean isfound = false;

		if (nodes.containsAll(classPathEntries))
		{

			isfound = true;
		}

		return isfound;

	}

	/**
	 * checks if either required libraries is set.
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
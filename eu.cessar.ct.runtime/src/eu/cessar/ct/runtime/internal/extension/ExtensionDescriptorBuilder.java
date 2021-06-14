/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Nov 9, 2010 2:45:08 PM </copyright>
 */
package eu.cessar.ct.runtime.internal.extension;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.cessar.ct.runtime.extension.IExtensionManager;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;

/**
 * @author uidl6870
 * 
 */
public final class ExtensionDescriptorBuilder
{

	public static final ExtensionDescriptorBuilder INSTANCE = new ExtensionDescriptorBuilder();

	private static final String CED_TAG = "ced"; //$NON-NLS-1$
	private static final String SOURCE_TAG = "source"; //$NON-NLS-1$
	private static final String EXTENSION_TAG = "extension"; //$NON-NLS-1$

	private static final String UUID_ATTR = "UUID"; //$NON-NLS-1$
	private static final String PATH_ATTR = "path"; //$NON-NLS-1$
	private static final String POINT_ATTR = "point"; //$NON-NLS-1$

	private ExtensionDescriptorBuilder()
	{
		// hide
	}

	/**
	 * Creates an {@link ExtensionDescriptor} from a given {@link IFile}
	 * 
	 * @param iFile
	 *        the given IFile
	 * @return the extension descriptor or null if could not be created
	 */
	public ExtensionDescriptor createExtensionDesc(IFile iFile)
	{
		if (!iFile.exists())
		{
			return null;
		}
		ExtensionDescriptor extDesc = null;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document doc = builder.parse(iFile.getLocation().toFile());
			NodeList nodeList = doc.getElementsByTagName("*"); //$NON-NLS-1$

			// ced, source, extension
			if (nodeList.getLength() >= 3)
			{
				Node node0 = nodeList.item(0);
				Node node1 = nodeList.item(1);
				Node node2 = nodeList.item(2);

				// if valid descriptor
				if (CED_TAG.equals(node0.getNodeName()) && SOURCE_TAG.equals(node1.getNodeName())
					&& EXTENSION_TAG.equals(node2.getNodeName()))
				{

					extDesc = new ExtensionDescriptor();

					processNode0(node0, extDesc);

					processNode1(iFile, node1, extDesc);

					processNode2(extDesc, node2);
				}
			}
		}
		catch (Exception e) // SUPPRESS CHECKSTYLE change in future
		{
			CessarPluginActivator.getDefault().logError(e);
			extDesc = null;
		}
		return extDesc;
	}

	/**
	 * Processes the provided Node, node0 and set the UUID attribute of the
	 * {@link ExtensionDescriptor} with it.
	 * 
	 * @param node0
	 *        the {@link eu.cessar.ct.core.platform.util.Node} to process
	 * @param extDesc
	 *        the ExtensionDescriptor
	 */
	private void processNode0(Node node0, ExtensionDescriptor extDesc)
	{
		String attrValue = getAttrValue(node0, UUID_ATTR);
		if (attrValue != null)
		{
			extDesc.setUUID(attrValue);
		}
		else
		{ // SUPPRESS CHECKSTYLE for doc
			// invalid/missing UUID
		}
	}

	/**
	 * Processes the Node, node1 and the given IFile, and sets the source folder
	 * attribute of the provided ExtensionDescriptor
	 * 
	 * @param iFile
	 *        the IFile to process
	 * @param node1
	 *        the Node to process
	 * @param extDesc
	 *        the ExtensionDescriptor to set
	 */
	private void processNode1(IFile iFile, Node node1, ExtensionDescriptor extDesc)
	{
		String attrValue = getAttrValue(node1, PATH_ATTR);
		if (attrValue != null)
		{

			IProject project = iFile.getProject();
			IFolder folder = project.getFolder(attrValue);
			if (folder.exists())
			{
				extDesc.setSourceFolder(folder);
			}
			else
			{ // SUPPRESS CHECKSTYLE for doc
				// invalid source path
			}

		}
	}

	/**
	 * Processes the Node, node2 and sets the provided ExtensionDescriptor's
	 * extension and extension point attributes.
	 * 
	 * @param extDesc
	 *        the {@link ExtensionDescriptor} to set
	 * @param node2
	 *        the Node to process
	 */
	private void processNode2(ExtensionDescriptor extDesc, Node node2)
	{
		String attrValue = getAttrValue(node2, POINT_ATTR);
		if (attrValue != null)
		{

			boolean found = false;
			String[] extensionIds = IExtensionManager.EXTENSION_IDS;
			for (String id: extensionIds)
			{
				if (id.equals(attrValue))
				{
					found = true;
					break;
				}
			}

			if (found)
			{
				extDesc.setExtensionPointID(attrValue);

				Extension extension = createExtension(node2);
				extDesc.setExtension(extension);
			}
			else
			{ // SUPPRESS CHECKSTYLE for doc
				// invalid extension point
			}
		}
	}

	/**
	 * Create an extension
	 * 
	 * @param node2
	 * @return
	 */
	private Extension createExtension(Node node2)
	{
		Extension extension = new Extension();

		// node2 corresponds to the <extension ..> tag
		createElementsFor(node2, extension);

		return extension;
	}

	/**
	 * For each child node of the given <code>node</code> create a corresponding
	 * Element, set it as child of the given <code>extension</code> Extension
	 * and call createElementsForNode(child, elem);
	 * 
	 * @param node
	 * @param extension
	 */
	private void createElementsFor(Node node, Extension extension)
	{
		if (node.hasChildNodes())
		{
			NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++)
			{
				Node child = childNodes.item(i);
				if (child.getNodeType() == Node.TEXT_NODE)
				{
					continue;
				}
				NamedNodeMap attributes = child.getAttributes();

				// create elements that are children of the extension
				Element elem = new Element(child.getNodeName());
				if (attributes != null)
				{
					for (int j = 0; j < attributes.getLength(); j++)
					{
						// populate
						Node attr = attributes.item(j);
						elem.addAttribute(attr.getNodeName(), attr.getNodeValue());
					}
				}
				extension.addElement(elem);

				createElementsForNode(child, elem);
			}
		}
	}

	/**
	 * For each child node of the given <code>node</code> create a corresponding
	 * Element , set it up as child of the given <code>parent</code> Element and
	 * call recursively createElementsForNode(child, elem)
	 * 
	 * @param node
	 * @param parent
	 */
	private void createElementsForNode(Node node, Element parent)
	{
		if (node.hasChildNodes())
		{
			NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++)
			{
				Node child = childNodes.item(i);
				if (child.getNodeType() == Node.TEXT_NODE)
				{
					continue;
				}
				NamedNodeMap attributes = child.getAttributes();

				// create elements that are children of other elements

				Element elem = new Element(child.getNodeName());
				if (attributes != null)
				{
					for (int j = 0; j < attributes.getLength(); j++)
					{
						// populate
						Node attr = attributes.item(j);
						elem.addAttribute(attr.getNodeName(), attr.getNodeValue());
					}
				}

				parent.addChild(elem);

				createElementsForNode(child, elem);
			}
		}

	}

	private String getAttrValue(Node node, String attrName)
	{
		NamedNodeMap attributes = node.getAttributes();
		if (attributes != null)
		{
			Node attr = attributes.getNamedItem(attrName);
			if (attr != null)
			{
				return attr.getNodeValue();
			}
		}
		return null;
	}
}

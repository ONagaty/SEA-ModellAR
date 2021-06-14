package eu.cessar.ct.jet.core.internal.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.jdom.IDOMField;
import org.eclipse.jdt.core.jdom.IDOMMethod;
import org.eclipse.jdt.core.jdom.IDOMNode;

import eu.cessar.ct.jet.core.JETCoreConstants;
import eu.cessar.ct.jet.core.internal.CessarPluginActivator;

/**
 * Custom {@link JETSkeleton} implementation that makes all JET classes to inherit from {@link IJetGenerator} interface.
 */
@SuppressWarnings({"deprecation", "nls"})
class JETSkeletonExtended extends JETSkeleton
{
	private boolean parallel = true;
	private final String STRING_FORMATTER_RETURN = "return stringFormatter.toString();" + NL;
	private final String DEFAULT_INDENTATION = "\t\t";

	JETSkeletonExtended()
	{
		compilationUnit = jdomFactory.createCompilationUnit(getJETTEmplate(), JETCoreConstants.CCLASS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.codegen.jet.JETSkeleton#setClassName(java.lang.String)
	 */
	@Override
	public void setClassName(String className)
	{
		super.setClassName(className);
		// set also the constructor
		for (IDOMNode node = compilationUnit.getFirstChild(); node != null; node = node.getNextNode())
		{
			if (node.getNodeType() == IDOMNode.TYPE)
			{
				// JETCoreConstants.OUT_FILE_MEMBER
				for (IDOMNode cNode = node.getFirstChild(); cNode != null; cNode = cNode.getNextNode())
				{
					if (cNode.getNodeType() == IDOMNode.METHOD && ((IDOMMethod) cNode).isConstructor())
					{
						cNode.setName(className);
					}
				}
			}
		}
	}

	@Override
	public void setConstants(List<String> constants)
	{
		for (IDOMNode node = compilationUnit.getFirstChild(); node != null; node = node.getNextNode())
		{
			if (node.getNodeType() == IDOMNode.TYPE)
			{
				IDOMNode insertionNode = node.getFirstChild();
				insertionNode.insertSibling(jdomFactory.createField(STATIC_NL_DECLARATION));
				insertionNode.insertSibling(jdomFactory.createField(NL_DECLARATION + getNLString()
					+ NL_DECLARATION_TAIL));
				for (Iterator<String> i = constants.iterator(); i.hasNext();)
				{
					String constant = "  " + i.next() + NL; //$NON-NLS-1$
					if (!i.hasNext())
					{
						constant += NL;
					}
					insertionNode.insertSibling(jdomFactory.createField(constant));
				}
				break;
			}
		}
	}

	/**
	 * @param fileName
	 */
	public void setOutputFileName(final String fileName)
	{
		for (IDOMNode node = compilationUnit.getFirstChild(); node != null; node = node.getNextNode())
		{
			if (node.getNodeType() == IDOMNode.TYPE)
			{
				IDOMField field = (IDOMField) node.getChild(JETCoreConstants.DEFAULT_OUTPUT_FILE_NAME);
				field.setInitializer(JETCoreConstants.DQUOTE + fileName + JETCoreConstants.DQUOTE);
			}
		}
	}

	private String getJETTEmplate()
	{
		InputStream inputStream;
		try
		{
			inputStream = FileLocator.openStream(CessarPluginActivator.getDefault().getBundle(), new Path(
				JETCoreConstants.SKELETON_TEMPLATE), true);
			byte[] fileData = new byte[inputStream.available()];

			inputStream.read(fileData);
			inputStream.close();

			return new String(fileData);
		}
		catch (IOException e)
		{
			CessarPluginActivator.getDefault().logError(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.codegen.jet.JETSkeleton#setBody(java.util.List)
	 */
	@Override
	public void setBody(List<String> lines)
	{
		IDOMMethod method = getLastMethod();
		if (method != null)
		{
			StringBuilder body = new StringBuilder();
			body.append(NL + "  	{" + NL);
			for (String line: lines)
			{
				body.append(convertLineFeed(line));
				body.append(NL);
			}

			// In case a preference page should be used:
			// Preferences prefs = InstanceScope.INSTANCE.getNode("eu.cessar.ct.workspace.ui");
			// Boolean useFormatting = prefs.getBoolean("prettyprint", true);

			body.append(DEFAULT_INDENTATION + STRING_FORMATTER_RETURN);
			body.append("	}" + NL);

			method.setBody(body.toString());
		}
	}

	public void setParallel(String value)
	{
		parallel = Boolean.valueOf(value);
	}

	/**
	 * @return
	 */
	public boolean isParallel()
	{
		return parallel;
	}
}

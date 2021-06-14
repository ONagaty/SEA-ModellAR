/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jul 24, 2012 11:45:48 AM </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.model.documentation.wizard;

import eu.cessar.ct.workspace.model.documentation.generator.ModelDocumentationGenerator;
import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;
import gautosar.gecucdescription.GModuleConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.osgi.framework.Bundle;

/**
 * @author uidt2045
 * 
 */
public class ModelDocumentationWizard extends Wizard
{
	private static final String XSL_FILE = "xslt/ModelDocumentation.xsl"; //$NON-NLS-1$

	private ModelDocumentationPage page;

	/**
	 * @param selection
	 */
	public ModelDocumentationWizard(IStructuredSelection selection)
	{
		setWindowTitle(ModelDocumentationConstants.MODEL_DOCUMENTATION_WIZARD_TITLE);
		page = new ModelDocumentationPage(
			ModelDocumentationConstants.MODEL_DOCUMENTATION_WIZARD_TITLE, selection);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish()
	{

		File outputFile = page.getOutputFile();
		File outputXMLFile = getXMLFile(outputFile);
		Set<GModuleConfiguration> listOfSelectedMC = page.getListOfSelectedMC();

		// RUN THE GENERATOR
		// and output the file at the specified location
		ModelDocumentationGenerator.generateDocumentation(outputXMLFile, listOfSelectedMC);

		// apply the style sheet
		Source xmlInput = new StreamSource(outputXMLFile);

		Bundle bundle = CessarPluginActivator.getDefault().getBundle();
		URL url = FileLocator.find(bundle, new Path(XSL_FILE), null);
		try
		{
			URL resolve = FileLocator.resolve(url);
			InputStream xslStream = resolve.openStream();
			try
			{
				Source xslSource = new StreamSource(xslStream);
				Result xmlOutput = new StreamResult(outputFile);

				Transformer transformer = TransformerFactory.newInstance().newTransformer(xslSource);
				transformer.transform(xmlInput, xmlOutput);
			}
			finally
			{
				xslStream.close();
			}
		}
		catch (TransformerException e)
		{
			CessarPluginActivator.getDefault().logInfo(e);
		}
		catch (IOException e1)
		{
			CessarPluginActivator.getDefault().logInfo(e1);
		}
		finally
		{
			outputXMLFile.delete();
		}
		return true;
	}

	/**
	 * @param outputFile
	 * @return
	 */
	private File getXMLFile(File outputFile)
	{
		try
		{
			String canonicalPath = outputFile.getCanonicalPath();
			return new File(canonicalPath + ".xml"); //$NON-NLS-1$
		}
		catch (IOException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages()
	{
		addPage(page);
	}

	@Override
	public boolean needsPreviousAndNextButtons()
	{
		return false;
	}
}

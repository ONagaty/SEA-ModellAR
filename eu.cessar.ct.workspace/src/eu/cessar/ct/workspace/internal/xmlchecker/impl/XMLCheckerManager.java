/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 26.07.2012 11:07:41 </copyright>
 */
package eu.cessar.ct.workspace.internal.xmlchecker.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sphinx.emf.metamodel.IMetaModelDescriptor;
import org.eclipse.sphinx.emf.metamodel.MetaModelDescriptorRegistry;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.util.EcoreResourceUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.ResourceBuilder;
import eu.cessar.ct.core.platform.PlatformConstants;
import eu.cessar.ct.workspace.consistencycheck.IConsistencyCheckResult;
import eu.cessar.ct.workspace.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.internal.domain.CessarWorkspaceEditingDomainFactory;
import eu.cessar.ct.workspace.internal.xmlchecker.IXMLConsistencyChecker;
import eu.cessar.ct.workspace.xmlchecker.IXMLCheckerInconsistency;
import eu.cessar.ct.workspace.xmlchecker.IXMLCheckerManager;
import eu.cessar.req.Requirement;

/**
 * Cycle for each received file: <br>
 * Precondition: the file has been previously loaded. The manager makes a copy of the corresponding EMF resource and
 * persists it into a separate, temporary resource set. Next, it passes the initial file and the generated one to a
 * {@link IXMLConsistencyChecker} that will return a report with the identified inconsistencies, if any.
 * 
 * @author uidl6870
 * 
 */
@Requirement(
	reqID = "REQ_CHECK#3")
public class XMLCheckerManager implements IXMLCheckerManager
{
	private static final String COPY_FILE_NAME = "copy.arxml";//$NON-NLS-1$

	private Map<File, Integer> fileToLineNumberMap;

	/**
	 * 
	 */
	public XMLCheckerManager()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.xmlchecker.IXMLCheckerManager#performConsistencyCheck(java.util.List,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	public List<IConsistencyCheckResult<IXMLCheckerInconsistency>> performConsistencyCheck(List<File> files,
		IMetaModelDescriptor metaModelDescriptor, IProgressMonitor monitor)
	{
		if (files.size() == 0)
		{
			return Collections.emptyList();
		}

		List<IConsistencyCheckResult<IXMLCheckerInconsistency>> reports = new ArrayList<IConsistencyCheckResult<IXMLCheckerInconsistency>>();

		IMetaModelService service = MMSRegistry.INSTANCE.getMMService(metaModelDescriptor);
		if (service != null)
		{
			try
			{
				fileToLineNumberMap = getFileToLinesNumberMap(files);

				int totalTicks = 0;
				for (File f: fileToLineNumberMap.keySet())
				{
					totalTicks += fileToLineNumberMap.get(f);
				}

				monitor.beginTask("", totalTicks); //$NON-NLS-1$
				monitor.setTaskName("Consistency check launched..."); //$NON-NLS-1$

				Collection<String> values = service.getIgnorableFeatureToXMLNameMapping().values();
				List<File> filesWithInconsistencies = new ArrayList<File>();

				for (File file: files)
				{
					if (monitor.isCanceled())
					{
						break;
					}

					IConsistencyCheckResult<IXMLCheckerInconsistency> report = doPerformCheck(file,
						new HashSet<String>(values), new SubProgressMonitor(monitor, fileToLineNumberMap.get(file)),
						metaModelDescriptor);
					reports.add(report);

					if (report.getInconsistencies().size() > 0)
					{
						filesWithInconsistencies.add(file);
					}

					monitor.setTaskName(NLS.bind("{0} file(s) with inconsistencies found", //$NON-NLS-1$
						new Object[] {filesWithInconsistencies.size()}));

				}
			}
			finally
			{
				service.discardIgnorableFeatureToXMLNameMapping();
				monitor.done();
			}
		}

		return reports;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.xmlchecker.IXMLCheckerManager#performConsistencyCheck(java.util.List,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	public List<IConsistencyCheckResult<IXMLCheckerInconsistency>> performConsistencyCheck(List<IFile> iFiles,
		IProgressMonitor monitor)
	{
		if (iFiles.size() == 0)
		{
			return Collections.emptyList();
		}

		List<File> files = new ArrayList<File>();
		for (IFile iFile: iFiles)
		{
			File file = new File(iFile.getLocation().toOSString());
			files.add(file);
		}

		IMetaModelDescriptor metaModelDescriptor = MetaModelDescriptorRegistry.INSTANCE.getDescriptor(iFiles.get(0));

		List<IConsistencyCheckResult<IXMLCheckerInconsistency>> reports = performConsistencyCheck(files,
			metaModelDescriptor, monitor);

		return reports;
	}

	/**
	 * @param Files
	 *        to be checked
	 * @return a mapping between the files and the number of lines of each file
	 * @throws CoreException
	 */
	private static Map<File, Integer> getFileToLinesNumberMap(List<File> files)
	{
		Map<File, Integer> map = new HashMap<File, Integer>();

		for (File file: files)
		{
			int numberOfLines = 0;
			Scanner scanner = null;
			try
			{
				scanner = new Scanner(file);
				while (scanner.hasNextLine())
				{
					scanner.nextLine();
					numberOfLines++;
				}
			}
			catch (FileNotFoundException e1)
			{
				CessarPluginActivator.getDefault().logError(e1);
			}
			finally
			{
				if (scanner != null)
				{
					scanner.close();
				}
			}

			map.put(file, numberOfLines);
		}

		return map;
	}

	/**
	 * @param originalIFile
	 * @param ignorableXMLNames
	 * @param subMonitor
	 * @return
	 * @throws CoreException
	 */
	private IConsistencyCheckResult<IXMLCheckerInconsistency> doPerformCheck(File originalFile,
		Set<String> ignorableXMLNames, IProgressMonitor subMonitor, IMetaModelDescriptor superDescriptor)
	{
		IConsistencyCheckResult<IXMLCheckerInconsistency> report = new XMLConsistencyCheckResult();
		report.setStatus(Status.OK_STATUS);

		Resource copyResource = null;
		try
		{
			subMonitor.beginTask("", fileToLineNumberMap.get(originalFile)); //$NON-NLS-1$
			subMonitor.subTask("Checking " + originalFile.getName() + " file"); //$NON-NLS-1$ //$NON-NLS-2$

			IMetaModelDescriptor descriptor = superDescriptor;
			URI originalUri = EcorePlatformUtil.createURI(new Path(originalFile.getPath()));

			Resource resource = EcorePlatformUtil.getResource(originalUri);

			// TODO: Find a better solution to assure that the files are loaded
			if (resource == null)
			{
				CessarWorkspaceEditingDomainFactory factory = new CessarWorkspaceEditingDomainFactory();
				TransactionalEditingDomain editingDomain = factory.createEditingDomain(Collections.singletonList(superDescriptor));
				ResourceSet resourceSet = editingDomain.getResourceSet();
				resource = resourceSet.getResource(originalUri, true);
			}

			if (resource != null)
			{
				final EObject copy = EcoreUtil.copy(resource.getContents().get(0));
				try
				{
					String filePathName = getCopyFilePathName();
					Path path = new Path(filePathName);

					URI tempURI = EcorePlatformUtil.createURI(path);
					IContentTypeManager contentTypeManager = Platform.getContentTypeManager();

					InputStream is = new FileInputStream(originalFile);
					IContentDescription description = contentTypeManager.getDescriptionFor(is, originalFile.getName(),
						IContentDescription.ALL);
					IContentType contentType = description.getContentType();
					// IContentType contentType = ResourceUtils.getContentType(originalFile);

					File copyFile;

					if (contentType != null)
					{
						copyResource = ResourceBuilder.INSTANCE.create(descriptor, tempURI, contentType.getId(), copy);
						copyFile = new File(tempURI.toFileString());
						runSaveResource(copyResource, report);

						if (report.getStatus().isOK())
						{
							IXMLConsistencyChecker checker = new XMLConsistencyChecker(ignorableXMLNames);
							report = checker.compare(originalFile, copyFile, subMonitor);
						}
					}

				}
				catch (FileNotFoundException e)
				{
					report.setStatus(new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, e.getMessage(), e));
				}
				catch (IOException e1)
				{
					report.setStatus(new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, e1.getMessage(), e1));
				}
				catch (ExecutionException e2)
				{
					report.setStatus(new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, e2.getMessage(), e2));
				}
			}
			else
			{
				// may happen if the file is not well-formed
				report.setStatus(new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, NLS.bind(
					"No model object found for {0} file", //$NON-NLS-1$
					originalFile.getAbsolutePath())));
			}
		}

		finally
		{
			if (copyResource != null)
			{
				copyResource.unload();
			}

			subMonitor.done();
		}

		return report;
	}

	private static void runSaveResource(final Resource resource,
		final IConsistencyCheckResult<IXMLCheckerInconsistency> report) throws ExecutionException
	{
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(resource);
		WorkspaceTransactionUtil.executeInWriteTransaction(editingDomain, new Runnable()
		{

			@Override
			public void run()
			{
				Map<?, ?> defaultSaveOptions = EcoreResourceUtil.getDefaultSaveOptions();
				try
				{
					resource.save(defaultSaveOptions);
				}
				catch (IOException e)
				{
					report.setStatus(new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, e.getMessage(), e));
				}

			}
		}, ""); //$NON-NLS-1$
	}

	private static String getCopyFilePathName()
	{
		StringBuffer buffer = new StringBuffer();

		URL url = Platform.getConfigurationLocation().getURL();
		buffer.append(url.getPath());
		buffer.append(CessarPluginActivator.PLUGIN_ID);

		buffer.append(PlatformConstants.PATH_SEPARATOR);
		buffer.append(COPY_FILE_NAME);

		return buffer.toString();
	}

}

package eu.cessar.ct.ecuc.workspace.jobs;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.util.EcoreResourceUtil;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;

import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.ecuc.commands.NewARPackageCommand;
import eu.cessar.ct.core.mms.ecuc.commands.parameters.NewARPackageCommandParameter;
import eu.cessar.ct.ecuc.workspace.internal.CessarPluginActivator;
import eu.cessar.ct.ecuc.workspace.internal.Messages;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

abstract public class AbstractCreateModuleFileJob extends Job
{
	protected IProject project;
	protected IFile outputFile;
	boolean overwriteFile;
	boolean appendToFile;

	protected String destinationPackageQName;

	protected AutosarReleaseDescriptor autosarRelease;
	protected TransactionalEditingDomain editingDomain;

	protected IAdaptable uiInfoAdaptable;

	@Override
	abstract protected IStatus run(IProgressMonitor monitor);

	/**
	 * Create and return the proper scheduling rule for this job.
	 */
	abstract protected ISchedulingRule getJobRule();

	/**
	 * Checks all assumptions. Throws CoreException if assumptions not validated.
	 * 
	 * @throws CoreException
	 */
	abstract protected void assertValid() throws CoreException;

	protected AbstractCreateModuleFileJob(String name, IProject project, IFile outputFile, boolean overwriteFile,
		boolean appendToFile)
	{
		super(name);

		this.project = project;
		this.outputFile = outputFile;
		this.overwriteFile = overwriteFile;
		this.appendToFile = appendToFile;

		// Set job's rule
		setRule(getJobRule());

		// Setup job priority
		setPriority(Job.BUILD);
	}

	/**
	 * @return Project in which the new file will be added
	 */
	protected final IProject getProject()
	{
		return project;
	}

	/**
	 * @return File to be created/saved
	 */
	protected final IFile getOutputFile()
	{
		return outputFile;
	}

	/**
	 * @return AutosarReleaseDescriptor associated with the project
	 */
	protected AutosarReleaseDescriptor getAutosarRelease()
	{
		// Lazy initialize the release descriptor associated with the project
		if (null == autosarRelease)
		{
			autosarRelease = MetaModelUtils.getAutosarRelease(project);
		}
		Assert.isNotNull(autosarRelease);
		return autosarRelease;
	}

	/**
	 * @return TransactionalEditingDomain of the project
	 */
	protected final TransactionalEditingDomain getEditingDomain()
	{
		if (null == editingDomain)
		{
			editingDomain = WorkspaceEditingDomainUtil.getEditingDomain(getProject(), getAutosarRelease());
		}
		return editingDomain;
	}

	/**
	 * @return Model root, either obtained from the output file, if the file exists, or as a newly created AUTOSAR
	 *         object.
	 * @throws CoreException
	 */
	protected final Object getModelRoot() throws CoreException
	{
		EObject rootObject = null;
		IFile outputFile = getOutputFile();

		if (appendToFile)
		{
			Assert.isTrue(outputFile.exists());
			URI fileUri = EcorePlatformUtil.createURI(outputFile.getFullPath());
			Resource resource = getEditingDomain().loadResource(fileUri.toString());
			rootObject = resource.getContents().get(0);
		}
		else
		{
			// Delete the file - we'll create a new one from scratch
			if (overwriteFile)
			{
				outputFile.delete(false, null);
			}

			// Create the root (AUTOSAR) object
			IMetaModelService service = MMSRegistry.INSTANCE.getMMService(getAutosarRelease());
			IGenericFactory gFactory = service.getGenericFactory();
			rootObject = gFactory.createAUTOSAR();
		}
		Assert.isNotNull(rootObject);
		return rootObject;
	}

	/**
	 * Attempts to saves the model rooted in rootObject in the output file
	 * 
	 * @param rootObject
	 *        Model root
	 * @param monitor
	 *        Monitor used to report progress
	 * @throws CoreException
	 */
	protected void persistModel(EObject rootObject, IProgressMonitor monitor) throws CoreException
	{
		try
		{
			// Get the path where the file is going to be saved
			IPath outputPath = getOutputFile().getFullPath();

			// Set options
			Map<String, Object> options = new HashMap<String, Object>();
			options.put(XMLResource.OPTION_ENCODING, "UTF-8");
			// options.put(XMLResource.OPTION_EXTENDED_META_DATA, Boolean.TRUE);
			options.put(XMLResource.OPTION_KEEP_DEFAULT_CONTENT, Boolean.TRUE);

			// Save the file using the editing domain
			if (outputFile.exists())
			{
				// EResourceUtils.saveResources(Collections.singletonList(rootObject.eResource()), monitor, false);
				// EcorePlatformUtil.saveModel(rootObject, options, false, monitor);
				EcoreResourceUtil.saveModelResource(rootObject.eResource(), null);
			}
			else
			{
				EcorePlatformUtil.saveNewModelResource(getEditingDomain(), outputPath,
					autosarRelease.getContentTypeIds().get(0), rootObject, options, false, monitor);
			}
		}
		catch (Exception e)
		{
			throw new CoreException(CessarPluginActivator.getDefault().createStatus(e));
		}
	}

	/**
	 * Create a new object in the model.
	 * 
	 * @param project
	 *        Project into which the new object will be created
	 * @param typeName
	 *        Type of object to create (EClassifier string)
	 * @param parent
	 *        Parent to be of the new object - must not be null
	 * @param feature
	 *        Name of parent feature that will be affected.
	 * @return Newly created object (already inserted in the model)
	 * @throws ExecutionException
	 * @throws OperationCanceledException
	 */
	protected final EObject createObject(String typeName, EObject parent, String featureName)
		throws OperationCanceledException, ExecutionException
	{
		// Get the Autosar factory and package
		AutosarReleaseDescriptor autosarRelease = getAutosarRelease();
		EFactory autosarFactory = autosarRelease.getEFactory();
		EPackage autosarPackage = autosarRelease.getEPackage();

		// Create the object
		Assert.isNotNull(typeName);
		EClass type = (EClass) autosarPackage.getEClassifier(typeName);
		Assert.isNotNull(type);
		EObject eObject = autosarFactory.create(type);
		Assert.isNotNull(eObject);

		// Get the parent feature that will be affected
		Assert.isNotNull(parent);
		EClass typeParent = (EClass) autosarPackage.getEClassifier(parent.getClass().getInterfaces()[0].getSimpleName());
		Assert.isNotNull(typeParent);
		EStructuralFeature feature = typeParent.getEStructuralFeature(featureName);
		Assert.isNotNull(feature);

		// Execute command to set the newly created object into proper parent
		// feature
		final Command command = new CreateChildCommand(getEditingDomain(), parent, feature, eObject,
			Collections.singletonList(parent));
		Assert.isTrue(command.canExecute());

		String transactionLabel = NLS.bind(Messages.AbstractCreateJob_CreateChildCommand_Transaction,
			eObject.getClass().getInterfaces()[0].getSimpleName(),
			(parent instanceof GIdentifiable ? ((GIdentifiable) parent).gGetShortName()
				: parent.getClass().getInterfaces()[0].getSimpleName()));
		WorkspaceTransactionUtil.executeInWriteTransaction(getEditingDomain(), new Runnable()
		{
			public void run()
			{
				command.execute();
			}

		}, transactionLabel);

		// Return command result
		Collection<?> result = command.getResult();
		Assert.isTrue(null != result && 0 != result.size());
		return (EObject) result.iterator().next();
	}

	public EObject getCommandResult(Command command)
	{
		// Return command result
		Collection<?> result = command.getResult();
		Assert.isTrue(null != result && 0 != result.size());
		return (EObject) result.iterator().next();
	}

	public void setDestinationPackageQName(String destinationPackageQName)
	{
		this.destinationPackageQName = destinationPackageQName;
	}

	/**
	 * Obtains or creates the destination package, that will hold the new module object.
	 * 
	 * @param project
	 *        Autosar object root of the model.
	 * @return Destination package
	 * @throws CoreException
	 */
	protected GARPackage getDestinationPackage(EObject rootObject) throws CoreException
	{
		GARPackage arPackage = null;
		try
		{
			// Normalize name
			setDestinationPackageQName(MetaModelUtils.normalizeQualifiedName(destinationPackageQName));

			// Try to find a package with this name, or as close/deep as
			// possible along the QName path
			arPackage = findLastExistingPackage(outputFile, destinationPackageQName);
			if (destinationPackageQName != MetaModelUtils.getAbsoluteQualifiedName(arPackage))
			{
				// Destination package does not exist, arPackage points to the
				// last existing package along the QName path (null means we
				// will start with a new top-level package).
				String existingQName = MetaModelUtils.getAbsoluteQualifiedName(arPackage);
				String remainingQName = destinationPackageQName.substring(existingQName.length());
				String[] nameParts = MetaModelUtils.splitQualifiedName(remainingQName);
				for (int index = 0; index < nameParts.length; ++index)
				{
					EObject parent = (null == arPackage ? rootObject : arPackage);
					arPackage = createPackage(parent, nameParts[index]);
				}
			}

			Assert.isNotNull(arPackage, NLS.bind(Messages.CessarModuleConfigurationJob_InvalidPackage,
				destinationPackageQName, outputFile.getFullPath().toString()));
		}
		catch (Exception e)
		{
			throw new CoreException(CessarPluginActivator.getDefault().createStatus(e));
		}
		return arPackage;
	}

	/**
	 * This method browses the list of objects with the given qualified name in the resource and returns the package
	 * with the largest number of matching path segments.
	 * 
	 * If there is no such resource in the project, returns null. If there is no package matching any of the path
	 * segments, returns null.
	 * 
	 * @param packageQName
	 *        Qualified name of the searched package.
	 * @return
	 */
	protected final GARPackage findLastExistingPackage(IFile resourceFile, String packageQName)
	{
		Resource resource = null;
		if (resourceFile.exists())
		{
			URI fileUri = EcorePlatformUtil.createURI(resourceFile.getFullPath());
			resource = getEditingDomain().loadResource(fileUri.toString());
			if (null == resource)
			{
				return null;
			}
		}

		String existingQName = packageQName;
		while (0 != existingQName.length())
		{
			List<EObject> objs = EObjectLookupUtils.getEObjectsWithQName(resource, existingQName);
			for (EObject obj: objs)
			{
				if (GARPackage.class.isAssignableFrom(obj.getClass()))
				{
					return (GARPackage) obj;
				}
			}

			existingQName = existingQName.substring(0, existingQName.lastIndexOf(MetaModelUtils.QNAME_SEPARATOR_STR));
		}
		return null;
	}

	/**
	 * Creates and a new package as child of parent and sets its short name.
	 * 
	 * @param parent
	 *        Parent of the new package (can be AUTOSAR or ARPackage).
	 * @param packageName
	 *        Short name for the new package.
	 * @return Newly created package.
	 * @throws OperationCanceledException
	 * @throws ExecutionException
	 */
	protected GARPackage createPackage(EObject parent, final String packageName) throws CoreException
	{
		try
		{
			// Execute command to create new Module Configuration and install it
			// into parent package's elements.
			final NewARPackageCommand command = new NewARPackageCommand(new NewARPackageCommandParameter(parent,
				packageName), null);
			Assert.isTrue(command.canExecute());
			command.execute();

			// Check result
			Object commandResult = command.getResult().iterator().next();
			Assert.isNotNull(commandResult);
			return (GARPackage) commandResult;
		}
		catch (Exception e)
		{
			throw new CoreException(CessarPluginActivator.getDefault().createStatus(e));
		}
	}

	/**
	 * Set an adaptable to be used by
	 * {@link IOperationHistory#execute(org.eclipse.core.commands.operations.IUndoableOperation, IProgressMonitor, IAdaptable)}
	 * <br/>
	 * At a minimum the adaptable should be able to adapt to org.eclipse.swt.widgets.Shell.<br/>
	 * Having a shell, such an adaptable can be obtained by <code>WorkspaceUndoUtil.getUIInfoAdapter(shell)</code><br/>
	 * If null, a default shell will be created to ask the user for confirmation.
	 * 
	 * @param uiInfoAdaptable
	 *        the uiInfoAdaptable to set
	 */
	public void setUiInfoAdaptable(IAdaptable uiInfoAdaptable)
	{
		this.uiInfoAdaptable = uiInfoAdaptable;
	}

	/**
	 * @return the uiInfoAdaptable
	 */
	public IAdaptable getUiInfoAdaptable()
	{
		return uiInfoAdaptable;
	}
}

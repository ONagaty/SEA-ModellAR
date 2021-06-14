/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Oct 9, 2009 1:19:38 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.sphinx.emf.workspace.syncing.ModelSynchronizer;

import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.adapter.CessarAdapterFactoryFactory;
import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.ECompatibilityMode;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.emfproxy.EMFProxyRegistry;
import eu.cessar.ct.emfproxy.IEMFProxyConstants;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.ecuc.internal.Messages;
import eu.cessar.ct.runtime.ecuc.internal.pm.asm.DelegatedEPackageImpl;
import eu.cessar.ct.runtime.ecuc.internal.pm.asm.IPMBinaryClassWrapper;
import eu.cessar.ct.runtime.ecuc.internal.pm.asm.PMBinaryClassWrapperFactory;
import eu.cessar.ct.runtime.ecuc.pm.IPMCompatElementInfo;
import eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo;
import eu.cessar.ct.runtime.execution.IExecutionLoader;
import eu.cessar.ct.runtime.execution.IExecutionSupportListener;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;
import eu.cessar.ct.sdk.runtime.ExecutionService;
import eu.cessar.ct.sdk.runtime.ICessarTaskManager;
import eu.cessar.req.Requirement;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GCommonConfigurationAttributes;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 *
 */
public class EcucPresentationModelImpl implements IEcucPresentationModel, IExecutionSupportListener
{

	private AdapterFactory adapterFactory;

	/**
	 * The PM information for the root node
	 */
	private IPMElementInfo<Object> rootNodeInfo;

	/**
	 * Map between the qualified name of an identifiable and its IPMELementInfo
	 */
	private Map<String, IPMElementInfo<Object>> identToPmElementMap = new Hashtable<>();

	/**
	 * PM packages cache. The key is the URI of a PM package, the EPackage is the generated EPackage
	 */
	private Map<String, EPackage> pmPackageRegistry = new Hashtable<>();

	/**
	 * A map between class names and binary data.
	 */
	private Map<String, IPMBinaryClassWrapper> pmBinaryClasses = new HashMap<>();

	private final IProject project;

	private IEcucModel ecucModel;

	private boolean loading;

	private IEMFProxyEngine proxyEngine;

	private boolean needModelClearing;

	private ECompatibilityMode compatMode;

	private RuntimeResourceSetListener unloadListener;

	/**
	 * @param project
	 */
	public EcucPresentationModelImpl(IProject project)
	{
		this.project = project;
		CessarRuntime.getExecutionSupport().addListener(this);
	}

	/**
	 * @return
	 */
	private synchronized AdapterFactory getAdapterFactory()
	{
		if (adapterFactory == null)
		{
			adapterFactory = CessarAdapterFactoryFactory.eINSTANCE.getAdapterFactory(project);
		}
		return adapterFactory;
	}

	/**
	 * @return
	 */
	private IEcucModel getEcucModel()
	{
		if (ecucModel == null)
		{
			ecucModel = IEcucCore.INSTANCE.getEcucModel(project);
		}
		return ecucModel;
	}

	/**
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IPMElementInfo<Object> newElementInfo(Object element)
	{
		IPMElementInfo<Object> result;
		if (compatMode != null && compatMode.haveCompatibilityAPI())
		{
			result = (IPMElementInfo<Object>) getAdapterFactory().adapt(element, IPMCompatElementInfo.class);
		}
		else
		{
			result = (IPMElementInfo<Object>) getAdapterFactory().adapt(element, IPMElementInfo.class);
		}
		//		Assert.isNotNull(result, "No PM adaptor found for " + element); //$NON-NLS-1$
		return result;
	}

	/**
	 * @param nodeInfo
	 */
	private void updatePMCache(EPackage pack)
	{
		if (pack != null)
		{
			pmPackageRegistry.put(pack.getNsURI(), pack);
			PMBinaryClassWrapperFactory factory = new PMBinaryClassWrapperFactory();
			initializeFactory(factory);
			IPMBinaryClassWrapper wrapper;

			// EPackage classes
			wrapper = factory.createEPackageImplBinWrapper(pack);
			pmBinaryClasses.put(wrapper.getClassName(), wrapper);
			wrapper = factory.createEPackageBinWrapper(pack);
			pmBinaryClasses.put(wrapper.getClassName(), wrapper);
			wrapper = factory.createEPackageLiteralsBinWrapper(pack);
			pmBinaryClasses.put(wrapper.getClassName(), wrapper);

			// EFactory classes
			wrapper = factory.createEFactoryImplBinWrapper(pack);
			pmBinaryClasses.put(wrapper.getClassName(), wrapper);
			wrapper = factory.createEFactoryBinWrapper(pack);
			pmBinaryClasses.put(wrapper.getClassName(), wrapper);

			for (EClassifier cls: pack.getEClassifiers())
			{
				if (cls instanceof EClass)
				{
					EClass clz = (EClass) cls;
					wrapper = factory.createEClassBinWrapper(clz);
					pmBinaryClasses.put(wrapper.getClassName(), wrapper);
					if (!clz.isInterface())
					{
						wrapper = factory.createEClassImplBinWrapper(clz);
						pmBinaryClasses.put(wrapper.getClassName(), wrapper);
					}
				}
				else if (cls instanceof EEnum)
				{
					EEnum en = (EEnum) cls;
					wrapper = factory.createEEnumImplBinWrapper(en);
					pmBinaryClasses.put(wrapper.getClassName(), wrapper);
				}
			}
		}
	}

	private void initializeFactory(PMBinaryClassWrapperFactory factory)
	{
		if (compatMode.haveCompatibilityAPI())
		{
			factory.setUsingGenerics(false);
			factory.setUsingNewEnum(false);
			factory.setEDataTypeClass(boolean.class, "Z"); //$NON-NLS-1$
			factory.setEDataTypeClass(Boolean.class, "Z"); //$NON-NLS-1$
			factory.setEDataTypeClass(double.class, "D"); //$NON-NLS-1$
			factory.setEDataTypeClass(Double.class, "D"); //$NON-NLS-1$
			factory.setUsingTitleCaseFeatureNames(true);
		}
		else
		{
			factory.setUsingGenerics(true);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.execution.IBinaryClassResolver#resolveClass(java.lang.String)
	 */
	public byte[] resolveClass(String className)
	{
		initModel();
		IPMBinaryClassWrapper wrapper = pmBinaryClasses.get(className);
		if (wrapper == null)
		{
			return null;
		}
		else
		{
			return wrapper.getBinary(true);
		}
	}

	/**
	 * Initialize the model if necessary
	 */
	private void initModel()
	{
		if (rootNodeInfo == null)
		{
			synchronized (this)
			{
				loading = true;
				try
				{
					if (rootNodeInfo == null)
					{
						runInitModel();
					}
				}
				finally
				{
					loading = false;
				}
			}
		}
	}

	/**
	 * Full initialize the PM model
	 */
	private void runInitModel()
	{
		clearModel();
		PlatformUtils.waitForModelLoading(project, null);
		initCompatMode();
		IPMElementInfo<Object> adapted = newElementInfo(project);
		if (adapted != null)
		{
			adapted.initialize(this, project, null);
			Map<String, List<GARPackage>> arPackages = getEcucModel().getRootArPackagesWithModuleDefs();
			initializePackages(adapted, arPackages);
			updatePMCache(adapted.getParentPackage(this));
			updatePMCache(adapted.getSubPackage(this, false));
			rootNodeInfo = adapted;
		}
	}

	/**
	 * Init the compatibility mode flag
	 */
	private void initCompatMode()
	{
		compatMode = CESSARPreferencesAccessor.getCompatibilityMode(project);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.IEcucPresentationModel#getProxyEngine()
	 */
	public IEMFProxyEngine getProxyEngine()
	{
		if (proxyEngine == null)
		{
			initCompatMode();
			AutosarReleaseDescriptor release = MetaModelUtils.getAutosarRelease(project);
			String proxyMasterURI = release.getNamespace();
			String proxySlaveURI = MMSRegistry.INSTANCE.getMMService(project).getEcucMMService().getPresentationModelURI(
				project, compatMode == ECompatibilityMode.FULL);
			proxyEngine = EMFProxyRegistry.eINSTANCE.getEMFProxyEngine(proxyMasterURI, proxySlaveURI, project, false);

			unloadListener = new RuntimeResourceSetListener(proxyEngine);
			TransactionalEditingDomain domain = MetaModelUtils.getEditingDomain(project);
			domain.addResourceSetListener(unloadListener);
		}
		return proxyEngine;
	}

	/**
	 *
	 */
	private void cleanupProxyEngine()
	{
		if (proxyEngine != null)
		{
			proxyEngine.clear();
		}
	}

	/**
	 *
	 */
	private void disposeProxyEngine()
	{
		if (proxyEngine != null)
		{
			TransactionalEditingDomain domain = MetaModelUtils.getEditingDomain(project);
			domain.removeResourceSetListener(unloadListener);
			proxyEngine.dispose();
			proxyEngine = null;
			unloadListener = null;
		}
	}

	/**
	 *
	 */
	private void clearModel()
	{
		if (!loading && rootNodeInfo != null)
		{
			synchronized (this)
			{
				if (!loading && rootNodeInfo != null)
				{
					rootNodeInfo = null;
					identToPmElementMap.clear();
					pmPackageRegistry.clear();
					pmBinaryClasses.clear();
					disposeProxyEngine();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.IEcucJavaModel#clearCache()
	 */
	public void modelChanged()
	{
		// ignore model change messages during user code execution
		if (!ExecutionService.isRunning(project))
		{
			clearModel();
		}
		else
		{
			needModelClearing = true;
		}
	}

	/**
	 * @param arPackages
	 */
	private void initializePackages(IPMElementInfo<?> parentInfo, Map<String, List<GARPackage>> arPackages)
	{
		for (String packName: arPackages.keySet())
		{
			List<GARPackage> packages = arPackages.get(packName);
			if (packages.size() > 0)
			{
				GARPackage firstPack = packages.get(0);
				String packQName = MetaModelUtils.getAbsoluteQualifiedName(firstPack);
				IPMElementInfo<Object> packInfo = newElementInfo(firstPack);
				if (packInfo == null)
				{
					continue;
				}
				packInfo.initialize(this, firstPack, parentInfo);
				identToPmElementMap.put(packQName, packInfo);
				initializePackages(packInfo, getEcucModel().getArPackagesWithModuleDefs(packQName));
				List<GModuleDef> moduleDefs = getEcucModel().getModuleDefsFromPackage(packQName);
				for (GModuleDef gModuleDef: moduleDefs)
				{
					initializeGModuleDef(packInfo, gModuleDef);
				}
				updatePMCache(packInfo.getParentPackage(this));
				updatePMCache(packInfo.getSubPackage(this, false));
			}
		}
	}

	/**
	 * @param moduleDefInfos
	 * @param packInfo
	 * @param gModuleDef
	 */
	private void initializeGModuleDef(IPMElementInfo<Object> packInfo, GModuleDef gModuleDef)
	{
		IPMElementInfo<Object> info = newElementInfo(gModuleDef);
		if (info != null)
		{
			String qName = MetaModelUtils.getAbsoluteQualifiedName(gModuleDef);
			info.initialize(this, gModuleDef, packInfo);
			identToPmElementMap.put(qName, info);
			initializeContainerList(info, gModuleDef.gGetContainers());
			updatePMCache(info.getParentPackage(this));
			updatePMCache(info.getSubPackage(this, false));
		}
	}

	/**
	 * @param moduleDefInfos
	 * @param info
	 * @param containers
	 */
	private void initializeContainerList(IPMElementInfo<Object> parentInfo, EList<? extends GContainerDef> containers)
	{
		for (GContainerDef contDef: containers)
		{
			if (contDef instanceof GParamConfContainerDef)
			{
				initializeGParamConfContainerDef(parentInfo, (GParamConfContainerDef) contDef);
			}
			else if (contDef instanceof GChoiceContainerDef)
			{
				initializeGChoiceContainerDef(parentInfo, (GChoiceContainerDef) contDef);
			}
			else
			{
				// error
			}
		}
	}

	/**
	 * @param moduleDefInfos
	 * @param info
	 * @param contDef
	 */
	private void initializeGChoiceContainerDef(IPMElementInfo<Object> parentInfo, GChoiceContainerDef contDef)
	{
		IPMElementInfo<Object> info = newElementInfo(contDef);
		if (info != null)
		{
			String qName = MetaModelUtils.getAbsoluteQualifiedName(contDef);
			info.initialize(this, contDef, parentInfo);
			identToPmElementMap.put(qName, info);
			initializeContainerList(info, contDef.gGetChoices());
			updatePMCache(info.getParentPackage(this));
			updatePMCache(info.getSubPackage(this, false));
		}
	}

	/**
	 * @param moduleDefInfos
	 * @param info
	 * @param contDef
	 */
	private void initializeGParamConfContainerDef(IPMElementInfo<Object> parentInfo, GParamConfContainerDef contDef)
	{
		IPMElementInfo<Object> info = newElementInfo(contDef);
		if (info != null)
		{
			String qName = MetaModelUtils.getAbsoluteQualifiedName(contDef);
			info.initialize(this, contDef, parentInfo);
			identToPmElementMap.put(qName, info);
			initializeContainerList(info, contDef.gGetSubContainers());
			EList<? extends GConfigParameter> parameters = contDef.gGetParameters();
			for (GConfigParameter gConfigParameter: parameters)
			{
				initializeParameterDef(info, gConfigParameter);
			}
			EList<? extends GConfigReference> references = contDef.gGetReferences();
			for (GConfigReference gConfigReference: references)
			{
				initializeParameterDef(info, gConfigReference);
			}
			updatePMCache(info.getParentPackage(this));
			updatePMCache(info.getSubPackage(this, false));
		}
	}

	/**
	 * @param moduleDefInfos
	 * @param info
	 * @param gConfigParameter
	 */
	private void initializeParameterDef(IPMElementInfo<Object> parentInfo,
		GCommonConfigurationAttributes gConfigElementDef)
	{
		IPMElementInfo<Object> info = newElementInfo(gConfigElementDef);
		if (info != null)
		{
			String qName = MetaModelUtils.getAbsoluteQualifiedName(gConfigElementDef);
			info.initialize(this, gConfigElementDef, parentInfo);
			identToPmElementMap.put(qName, info);
			updatePMCache(info.getSubPackage(this, false));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.IEcucPresentationModel#getProject()
	 */
	public IProject getProject()
	{
		return project;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.IEcucPresentationModel#clearBinaryCache()
	 */
	/**
	 *
	 */
	public void clearBinaryCache()
	{
		if (pmPackageRegistry != null)
		{
			for (EPackage pack: pmPackageRegistry.values())
			{
				if (pack instanceof DelegatedEPackageImpl)
				{
					DelegatedEPackageImpl dPack = (DelegatedEPackageImpl) pack;
					dPack.clearBinaryCache();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.IEcucPresentationModel#getPMModelRoot()
	 */
	public EObject getPMModelRoot()
	{
		initModel();
		IEMFProxyObject rootObject = getProxyEngine().getSlaveObject(IEMFProxyConstants.DEFAULT_CONTEXT, project);
		return rootObject;
	}

	/**
	 * @param output
	 * @throws IOException
	 */
	private void dumpPMEcore(OutputStream output) throws IOException
	{
		initModel();
		Resource res = new XMLResourceImpl(URI.createURI("#")); //$NON-NLS-1$
		res.getContents().add(rootNodeInfo.getParentPackage(this));
		res.save(output, new HashMap<>());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.IEcucPresentationModel#getRootPMEClass()
	 */
	public EClass getRootPMEClass()
	{
		initModel();
		return (EClass) rootNodeInfo.getClassifier();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.IEcucPresentationModel#getPMClassifier(gautosar.ggenericstructure.ginfrastructure.
	 * GIdentifiable)
	 */
	public EClassifier getPMClassifier(GIdentifiable ident)
	{
		Assert.isNotNull(ident);
		initModel();
		String qName = MetaModelUtils.getAbsoluteQualifiedName(ident);
		IPMElementInfo<Object> info = identToPmElementMap.get(qName);
		if (info == null)
		{
			return null;
		}
		else
		{
			return info.getClassifier();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.IEcucPresentationModel#getEPackage(java.lang.String)
	 */
	public EPackage getEPackage(String pmURI)
	{
		initModel();
		return pmPackageRegistry.get(pmURI);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.execution.IClassLoaderListener#classLoaderAquired(org.eclipse.core.resources.IProject,
	 * eu.cessar.ct.runtime.execution.CompositeClassLoader)
	 */
	public void executionSupportAquired(IProject mProject, ICessarTaskManager<?> manager,
		IExecutionLoader executionLoader)
	{
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.execution.IClassLoaderListener#classLoaderReleased(org.eclipse.core.resources.IProject,
	 * eu.cessar.ct.runtime.execution.CompositeClassLoader)
	 */
	public void executionSupportReleased(IProject mProject, ICessarTaskManager<?> manager,
		IExecutionLoader executionLoader)
	{
		if (mProject == project && executionLoader == null)
		{
			if (needModelClearing)
			{
				clearModel();
				needModelClearing = false;
			}
			clearBinaryCache();
			cleanupProxyEngine();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.IEcucPresentationModel#getInitStatus()
	 */
	@Override
	public IStatus getInitStatus()
	{
		return ((null != ecucModel) ? ecucModel.getInitStatus() : null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.IEcucPresentationModel#dumpPresentationModel(boolean, String, IProgressMonitor)
	 */
	public IStatus dumpPresentationModel(final boolean dumpPMClasses, final String dumpFolder,
		final IProgressMonitor monitor)
	{
		initModel();
		WorkspaceJob dumpModel = new WorkspaceJob(Messages.message_dump)
		{
			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor1) throws CoreException
			{

				ModelSynchronizer.INSTANCE.stop();
				try
				{
					return doDumpPM(dumpPMClasses, dumpFolder, monitor);
				}
				finally
				{
					ModelSynchronizer.INSTANCE.start();
				}
			}

			// Add the job to a specific JobFamily in order to later be able to start/stop/cancel the job
			@Override
			public boolean belongsTo(Object family)
			{
				return family == DUMP_PM_FAMILY_JOB;
			}
		};

		dumpModel.setUser(true);
		IWorkspace workspace = project.getWorkspace();

		IWorkspaceRoot root = workspace.getRoot();
		IProject project2 = root.getProject(".org.eclipse.jdt.core.external.folders"); //$NON-NLS-1$
		ISchedulingRule combine = MultiRule.combine(project, project2);
		dumpModel.setRule(combine);
		dumpModel.schedule();

		try
		{
			dumpModel.join();
		}
		catch (InterruptedException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return dumpModel.getResult();
	}

	@Requirement(
		reqID = "137964")
	/**
	 * Method doing the actual dump and adds the PM to the classpath.
	 *
	 * @param dumpPMClasses
	 *        whether to dump the implementation classes
	 * @param dumpFolder
	 *        the pmbin folder name
	 * @param monitor
	 *        the progress monitor
	 * @return {@link IStatus} whether the dump succeeded
	 * @throws JavaModelException
	 * @throws CoreException
	 * @throws IOException
	 */
	private IStatus doDumpPM(final boolean dumpPMClasses, final String dumpFolder, final IProgressMonitor monitor)
	{
		IStatus status = Status.OK_STATUS;
		PMPackageManager manager = new PMPackageManager(project);

		final IFolder outputFolder = project.getFolder(dumpFolder);

		try
		{
			modelChanged();

			if (outputFolder.exists())
			{
				outputFolder.delete(true, false, monitor);
			}
			outputFolder.create(true, true, monitor);

			createEcoreFile(monitor, outputFolder);

			status = manager.packPMToJar(outputFolder, monitor, dumpPMClasses, pmBinaryClasses);
			if (!status.isOK())
			{
				return status;
			}

			status = cleanProjectClasspath(outputFolder);

			return status;
		}
		catch (JavaModelException e)
		{
			return CessarPluginActivator.getDefault().createStatus(e);
		}
		catch (CoreException e)
		{
			return CessarPluginActivator.getDefault().createStatus(e);
		}
	}

	/**
	 * Method for creating the .ecore file belonging to the AUTOSAR model in the project
	 *
	 * @param monitor
	 *        the progress monitor
	 * @param outputFolder
	 *        the pmbin folder
	 * @throws CoreException
	 * @throws IOException
	 */
	private void createEcoreFile(final IProgressMonitor monitor, final IFolder outputFolder)
	{
		IFile ecoreFile = outputFolder.getFile(PMJavaConstants.ECORE_FILE);
		try
		{
			if (ecoreFile.exists())
			{
				ecoreFile.delete(true, monitor);
			}
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			dumpPMEcore(bOut);

			ecoreFile.create(new ByteArrayInputStream(bOut.toByteArray()), true, monitor);
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		catch (IOException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/**
	 * Because of hystorical reasons, the pmbin folder is added to the classpath as class folder. This can cause
	 * problems if a jar of the folder is also added to the classpath as in certain situations, it is not being loaded
	 * correctly. The method removes the pmbin folder from the classpath after the .jar was generated.
	 *
	 * @param outputFolder
	 * @return
	 */
	private IStatus cleanProjectClasspath(IFolder outputFolder)
	{
		IJavaProject javaProject = JavaCore.create(project);
		try
		{
			IClasspathEntry[] rawClasspath = javaProject.getRawClasspath();
			Set<IClasspathEntry> entries = new LinkedHashSet<>();
			if (rawClasspath.length > 0)
			{
				entries.addAll(Arrays.asList(rawClasspath));
			}

			Iterator<IClasspathEntry> it = entries.iterator();
			while (it.hasNext())
			{
				IClasspathEntry ice = it.next();
				if (ice == null)
				{
					it.remove();
				}
				else if (outputFolder.getLocation() != null
					&& outputFolder.getLocation().toString().endsWith(ice.getPath().toString()))
				{
					it.remove();
				}
			}
			javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), new NullProgressMonitor());
		}
		catch (JavaModelException e)
		{
			return CessarPluginActivator.getDefault().createStatus(e);
		}

		return Status.OK_STATUS;
	}
}
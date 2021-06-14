package eu.cessar.ct.validation.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sphinx.emf.validation.diagnostic.ExtendedDiagnostician;
import org.eclipse.sphinx.emf.validation.stats.ValidationPerformanceStats;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.sdk.pm.IPMContainer;
import eu.cessar.ct.sdk.pm.IPMElement;
import eu.cessar.ct.sdk.pm.IPMModuleConfiguration;
import eu.cessar.ct.sdk.pm.IPresentationModel;
import eu.cessar.ct.sdk.utils.PMUtils;
import eu.cessar.ct.sdk.utils.ValidationUtils;
import eu.cessar.ct.validation.ValidationUtilsCommon;
import eu.cessar.req.Requirement;

/**
 * @author uidu2337
 *
 */
@Requirement(
	reqID = "REQ_API_VAL#1")
public final class SDKValidationUtils implements ValidationUtils.Service
{

	/**
	 * The singleton.
	 */
	public static final SDKValidationUtils eINSTANCE = new SDKValidationUtils();

	/**
	 * The private constructor.
	 */
	private SDKValidationUtils()
	{
		// hidden constructor
	}

	/**
	 * Validate a single {@code EObject}.
	 *
	 * @param target
	 *        the {@code EObject} to validate
	 * @return the validation result as a {@code Diagnostic}
	 */
	public static Diagnostic diagValidate(EObject target)
	{
		Diagnostic diagnostic = null;
		ExtendedDiagnostician diagnostician = new ExtendedDiagnostician();
		diagnostic = diagnostician.validate(target);

		return diagnostic;
	}

	/**
	 * Get the collection of {@code EObject}(s) corresponding to a Presentation Model object.
	 *
	 * @param object
	 *        the presentation model object
	 * @return the non-null, possibly empty, collection of {@code EObject}(s) corresponding to the presentation model
	 *         object
	 */
	private static List<EObject> getEObjects(IPMElement object)
	{
		List<EObject> objects = new ArrayList<>();
		if (object instanceof IPresentationModel)
		{
			EMFProxyObjectImpl impl = (EMFProxyObjectImpl) object;
			IEMFProxyEngine proxyEngine = impl.eGetProxyEngine();
			List<Object> masterObjects = proxyEngine.getMasterObjects(impl);
			if (masterObjects != null && !masterObjects.isEmpty())
			{
				objects.addAll(ValidationUtilsCommon.getMergedModelObjects(masterObjects));
			}
		}

		if (object instanceof IPMModuleConfiguration)
		{
			objects.addAll(PMUtils.getModuleConfigurations((IPMModuleConfiguration) object));
		}
		else if (object instanceof IPMContainer)
		{
			objects.addAll(PMUtils.getContainers((IPMContainer) object));
		}

		return objects;
	}

	/**
	 * Start an event to update the problem markers based on the list of {@code Diagnostic} objects.
	 *
	 * @param selectedModelObjects
	 *        the validated model objects
	 * @param diagnostics
	 *        the result of validation as a diagnostics list
	 */
	private static void handleDiagnosticMulti(List<EObject> selectedModelObjects, final List<Diagnostic> diagnostics)
	{
		Assert.isNotNull(diagnostics);

		ValidationPerformanceStats.INSTANCE.startNewEvent(
			ValidationPerformanceStats.ValidationEvent.EVENT_UPDATE_PROBLEM_MARKERS, "UpdateMarkers"); //$NON-NLS-1$

		ValidationUtilsCommon.updateProblemMarkers(selectedModelObjects, diagnostics);

		ValidationPerformanceStats.INSTANCE.endEvent(
			ValidationPerformanceStats.ValidationEvent.EVENT_UPDATE_PROBLEM_MARKERS, "UpdateMarkers"); //$NON-NLS-1$
	}

	/**
	 * Perform validation on a collection of {@code EObjects}.
	 *
	 * @param targets
	 *        the collection of {@code EObjects} to validate
	 * @return the validation IStatus
	 */
	private static IStatus multiValidate(List<EObject> targets)
	{
		List<Diagnostic> diagnostics = new ArrayList<>();

		if (ValidationUtilsCommon.shouldValidate(targets))
		{
			// validate each EObject in scope
			for (EObject frag: targets)
			{
				if (frag == null)
				{
					continue;
				}

				Diagnostic diag = diagValidate(frag);
				if (null != diag)
				{
					diagnostics.add(diag);
				}
			}

			if (!diagnostics.isEmpty())
			{
				handleDiagnosticMulti(targets, diagnostics);
			}
		}

		// necessary if all objects in the given list are null
		for (EObject target: targets)
		{
			if (target == null)
			{
				BasicDiagnostic bd = new BasicDiagnostic();
				bd.add(new BasicDiagnostic(Diagnostic.ERROR, Messages.API_validation_source, 0x31337,
					Messages.API_validation_null_arguments, targets.toArray()));
				diagnostics.add(bd);
				break;
			}
		}

		return BasicDiagnostic.toIStatus(ValidationUtilsCommon.combinedDiagnostic(diagnostics));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ValidationUtils.Service#validate(java.util.Collection)
	 */
	@Override
	public IStatus validate(Collection<? extends IPMElement> objects)
	{
		List<EObject> eObjects = new ArrayList<>(objects.size());

		for (IPMElement object: objects)
		{
			if (null != object)
			{
				eObjects.addAll(getEObjects(object));
			}
			else
			{
				eObjects.add(null);
			}
		}

		return multiValidate(eObjects);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ValidationUtils.Service#validate(eu.cessar.ct.sdk.pm.IPMElement)
	 */
	@Override
	public IStatus validate(IPMElement object)
	{
		return validate(Collections.singletonList(object));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ValidationUtils.Service#validate(org.eclipse.core.resources.IProject)
	 */
	@Override
	public IStatus validate(IProject project)
	{
		List<Object> selection = new ArrayList<>();
		selection.add(project);

		return multiValidate(ValidationUtilsCommon.getModelObjects(selection));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ValidationUtils.Service#validate(org.eclipse.core.resources.IFile)
	 */
	@Override
	public IStatus validate(IFile file)
	{
		return multiValidate(ValidationUtilsCommon.getModelObjects(Collections.singleton((Object) file)));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ValidationUtils.Service#validate(org.eclipse.core.resources.IFile, boolean)
	 */
	@Override
	public IStatus validate(IFile file, boolean merged)
	{
		if (merged)
		{
			return validate(file.getProject(), true);
		}
		else
		{
			return multiValidate(ValidationUtilsCommon.getModelObjects(Collections.singleton((Object) file)));
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ValidationUtils.Service#validate(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public IStatus validate(EObject object)
	{
		return multiValidate(Collections.singletonList(object));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ValidationUtils.Service#validateFiles(org.eclipse.core.resources.IFile)
	 */
	@Override
	public IStatus validateFiles(Collection<IFile> files)
	{
		Collection<Object> objects = new HashSet<>(files.size());
		objects.addAll(files);

		return multiValidate(ValidationUtilsCommon.getModelObjects(objects));

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ValidationUtils.Service#validateFiles(java.util.Collection, boolean)
	 */
	@Override
	public IStatus validateFiles(Collection<IFile> files, boolean merged)
	{

		Collection<Object> objects = new HashSet<>(files.size());
		objects.addAll(files);

		if (merged)
		{
			return multiValidate(ValidationUtilsCommon.getMergedModelObjects(objects));
		}
		else
		{
			return multiValidate(ValidationUtilsCommon.getModelObjects(objects));
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ValidationUtils.Service#validate(org.eclipse.core.resources.IFolder)
	 */
	@Override
	public IStatus validate(IFolder folder)
	{
		return multiValidate(ValidationUtilsCommon.getModelObjects(Collections.singleton((Object) folder)));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ValidationUtils.Service#validate(org.eclipse.core.resources.IFolder, boolean)
	 */
	@Override
	public IStatus validate(IFolder folder, boolean merged)
	{
		if (merged)
		{
			return multiValidate(ValidationUtilsCommon.getMergedModelObjects(Collections.singleton((Object) folder)));
		}
		else
		{
			return multiValidate(ValidationUtilsCommon.getModelObjects(Collections.singleton((Object) folder)));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.ValidationUtils.Service#validate(org.eclipse.core.resources.IProject, boolean)
	 */
	@Override
	public IStatus validate(IProject project, boolean merged)
	{
		List<Object> selection = new ArrayList<>();
		selection.add(project);

		if (merged)
		{
			return multiValidate(ValidationUtilsCommon.getMergedModelObjects(selection));

		}
		else
		{
			return multiValidate(ValidationUtilsCommon.getModelObjects(selection));
		}
	}
}

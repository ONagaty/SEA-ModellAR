/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu2337<br/>
 * Jun 17, 2014 1:53:51 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.input.ReaderInputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sphinx.emf.util.EObjectUtil;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.validation.Activator;
import org.eclipse.sphinx.emf.validation.diagnostic.ExtendedDiagnostic;
import org.eclipse.sphinx.emf.validation.markers.IValidationMarker;
import org.eclipse.sphinx.emf.validation.markers.IValidationProblemMarkersChangeListener;
import org.eclipse.sphinx.emf.validation.markers.ValidationMarkerManager;
import org.eclipse.sphinx.emf.validation.markers.util.FeatureAttUtil;
import org.eclipse.sphinx.emf.validation.preferences.IValidationPreferences;
import org.eclipse.sphinx.emf.validation.util.Messages;
import org.eclipse.sphinx.emf.validation.util.ValidationUtil;
import org.eclipse.sphinx.platform.util.PlatformLogUtil;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.util.ReflectionUtils;
import eu.cessar.ct.sdk.logging.LoggerFactory;

/**
 * Class that places validation markers on a hidden dummy resource.
 *
 * @see MergedValidationMarkerManager#CT_DUMMY_MARKERS_FILE
 *
 * @author uidu2337
 *
 *         %created_by: uidl6458 %
 *
 *         %date_created: Thu Jul  3 16:11:50 2014 %
 *
 *         %version: 2 %
 */
public final class MergedValidationMarkerManager
{

	/**
	 * Proxy URI integrity problem marker type.
	 * <p>
	 * !! Important Note !! Don't use Activator.getPlugin().getSymbolicName() instead of hard-coded plug-in name because
	 * this would prevent this class from being loaded in Java stand-alone applications.
	 * </p>
	 *
	 * @see IMarker#getType()
	 */
	public static final String PROXY_URI_INTEGRITY_PROBLEM = "org.eclipse.sphinx.emf.proxyuriintegrityproblemmarker"; //$NON-NLS-1$

	/**
	 * The singleton instance of {@link org.eclipse.sphinx.emf.validation.markers.ValidationMarkerManager}.
	 */
	private static final ValidationMarkerManager MARKER_MANAGER = ValidationMarkerManager.getInstance();

	private final static String VALIDATION_PROBLEM_MARKERS_CHANGE_LISTENER_LIST_FIELD_NAME = "validationProblemMarkersChangeListenerList"; //$NON-NLS-1$

	/**
	 * Retrieved from {@link org.eclipse.sphinx.emf.validation.markers.ValidationMarkerManager} via reflection.
	 */
	private static ListenerList validationProblemMarkersChangeListenerList;

	/**
	 * Name of the hidden dummy resource in the project on which merged validation problem markers are placed.
	 */
	private static final String CT_DUMMY_MARKERS_FILE = ".AUTOSAR_merged_model"; //$NON-NLS-1$

	/**
	 * Separator for the model (resource-relative) URI of a model element from the resource part. E.g.:
	 * platform:/resource/FourZeroThree/CESSAR/myDet.ecuconfig#/CESSAR/myDet/DetGeneral
	 */
	private static final String OBJECT_URI_SEPARATOR = "#"; //$NON-NLS-1$

	/**
	 * Content of the dummy markers file on which merged validation errors are placed.
	 */
	private static final String DUMMY_MARKERS_FILE_CONTENT = "This is a problem-marker file used in the scope of validating a splittable configuration!"; //$NON-NLS-1$

	/**
	 * Map storing the dummy marker files per project.
	 */
	private static final Map<IProject, IFile> PROJECT_DUMMY_MARKER_FILES = new HashMap<>();

	private MergedValidationMarkerManager()
	{
	}

	/**
	 * Return true if the file is the special marker file, false otherwise
	 *
	 * @param file
	 * @return true if the file is the special marker file
	 */
	public static boolean isMergedMarkerSourceFile(IFile file)
	{
		return file.getParent() instanceof IProject && CT_DUMMY_MARKERS_FILE.equals(file.getName());
	}

	/**
	 * Returns the dummy marker file in the given {@link project} on which merged validation markers are placed.
	 *
	 * @param project
	 *        the project
	 * @return the merged validation marker file
	 */
	public static IFile getMarkerSourceFile(final IProject project)
	{
		try
		{
			ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable()
			{
				public void run(IProgressMonitor monitor) throws CoreException
				{
					IFile ctDummyMarkersFile = project.getFile(CT_DUMMY_MARKERS_FILE);
					if (!ctDummyMarkersFile.exists())
					{
						InputStream in = null;
						try
						{
							in = new ReaderInputStream(new StringReader(DUMMY_MARKERS_FILE_CONTENT));
							ctDummyMarkersFile.create(in, true, null);
						}
						finally
						{
							if (in != null)
							{
								try
								{
									in.close();
								}
								catch (IOException e)
								{
									LoggerFactory.getLogger().log(e);
								}
							}
						}
					}
					// This is intentionally overwritten in the hash to make sure it's not an old cached value.
					PROJECT_DUMMY_MARKER_FILES.put(project, ctDummyMarkersFile);
				}
			}, project, IWorkspace.AVOID_UPDATE, null);
		}
		catch (CoreException e)
		{
			LoggerFactory.getLogger().log(e);

		}
		return PROJECT_DUMMY_MARKER_FILES.get(project);
	}

	/**
	 * Translate diagnostic in markers on the {@link Resource} resource
	 *
	 * @param resource
	 *        the target resource
	 * @param diagnostic
	 *        the connected {@link Diagnostic}
	 * @throws CoreException
	 */
	public static void addMarkers(IResource resource, Diagnostic diagnostic) throws CoreException
	{
		Assert.isNotNull(resource);
		Assert.isNotNull(diagnostic);

		IResource iResource = EcorePlatformUtil.getFile(resource);
		if (iResource == null || !iResource.exists())
		{
			return;
		}

		// Let's check if the diagnostic data are ok
		List<?> diagnosticData = diagnostic.getData();
		if (diagnosticData == null || diagnosticData.size() == 0)
		{
			return;
		}

		// The framework is too slow when there are too many markers. Hence we limitate the number of
		// created markers
		int max_err = Platform.getPreferencesService().getInt(org.eclipse.sphinx.emf.validation.Activator.PLUGIN_ID,
			IValidationPreferences.PREF_MAX_NUMBER_OF_ERRORS, IValidationPreferences.PREF_MAX_NUMBER_OF_ERRORS_DEFAULT,
			null);
		IMarker[] markers = iResource.getWorkspace().getRoot().findMarkers(IValidationMarker.MODEL_VALIDATION_PROBLEM,
			true, IResource.DEPTH_INFINITE);
		int nb_err = markers.length;

		// compute nb markers to be added
		int to_be_added = 0;
		for (Diagnostic childDiagnostic: diagnostic.getChildren())
		{
			if (!childDiagnostic.getData().isEmpty())
			{
				++to_be_added;
			}
		}

		// if we are about to overflow, remove oldest markers to ensure that some of the new markers will be added
		if (max_err > 0 && nb_err + to_be_added > max_err)
		{
			int nb_removed = 0;
			Arrays.sort(markers, new Comparator<IMarker>()
			{
				@Override
				public int compare(IMarker o1, IMarker o2)
				{
					try
					{
						return Long.valueOf(o1.getCreationTime()).compareTo(Long.valueOf(o2.getCreationTime()));
					}
					catch (CoreException ex)
					{
						return 0;
					}
				}
			});
			// do not remove more than half of max_err or total number of err
			int remove_threshold = (nb_err > max_err ? nb_err : max_err) / 2;
			for (int i = 0; i < markers.length && nb_err + to_be_added > max_err; ++i)
			{
				markers[i].delete();
				--nb_err;
				++nb_removed;
				if (nb_removed >= remove_threshold)
				{
					break;
				}
			}
		}

		EObject validatedObject = (EObject) diagnosticData.get(0);

		try
		{
			// Add new markers
			int markerSeverity = 0;

			for (Diagnostic childDiagnostic: diagnostic.getChildren())
			{
				if (max_err > 0 && nb_err > max_err)
				{
					break;
				}

				if (!childDiagnostic.getData().isEmpty())
				{

					Map<String, Object> attributes = new HashMap<>();

					IMarker marker = iResource.createMarker(IValidationMarker.MODEL_VALIDATION_PROBLEM);
					++nb_err;

					EObject tgtObject = (EObject) childDiagnostic.getData().get(0);

					attributes.put(EValidator.URI_ATTRIBUTE, EcoreUtil.getURI(tgtObject).toString());
					attributes.put(IValidationMarker.HASH_ATTRIBUTE, tgtObject.hashCode());

					int severity = childDiagnostic.getSeverity();
					if (severity < Diagnostic.WARNING)
					{
						markerSeverity = IMarker.SEVERITY_INFO;
					}
					else if (severity < Diagnostic.ERROR)
					{
						markerSeverity = IMarker.SEVERITY_WARNING;
					}
					else
					{
						markerSeverity = IMarker.SEVERITY_ERROR;
					}

					attributes.put(IMarker.SEVERITY, markerSeverity);

					String message = childDiagnostic.getMessage();
					if (message == null)
					{
						message = Messages.noMessageAvailableForThisMarker;
					}

					attributes.put(IMarker.MESSAGE, message);

					if (childDiagnostic instanceof ExtendedDiagnostic)
					{
						String ruleId = ((ExtendedDiagnostic) childDiagnostic).getConstraintId();
						// The features attribute.
						Set<String> features = FeatureAttUtil.getRulesFeaturesForEObj(ruleId, tgtObject);
						String packedFeaturesStr = FeatureAttUtil.packFeaturesAsString(marker, features);

						attributes.put(IValidationMarker.FEATURES_ATTRIBUTE, packedFeaturesStr);
						// The rule id attribute.
						attributes.put(IValidationMarker.RULE_ID_ATTRIBUTE, ruleId);
					}

					// Let's set all attributes
					marker.setAttributes(attributes);
				}
			}

			fireValidationProblemMarkersChanged(validatedObject);
		}
		catch (CoreException ex)
		{
			PlatformLogUtil.logAsWarning(Activator.getDefault(),
				NLS.bind(Messages.warningProblemWithMarkerOperationOnResource, iResource.getLocationURI().toString()));
		}
	}

	/**
	 * @param object
	 */
	protected static void fireValidationProblemMarkersChanged(EObject object)
	{
		try
		{
			validationProblemMarkersChangeListenerList = (ListenerList) ReflectionUtils.getFieldValue(
				ValidationMarkerManager.class, MARKER_MANAGER,
				VALIDATION_PROBLEM_MARKERS_CHANGE_LISTENER_LIST_FIELD_NAME);
		}
		catch (NoSuchFieldException | IllegalAccessException e)
		{
			PlatformLogUtil.logAsError(Activator.getDefault(), e.getMessage());
		}

		for (Object listener: validationProblemMarkersChangeListenerList.getListeners())
		{
			if (listener instanceof IValidationProblemMarkersChangeListener)
			{
				((IValidationProblemMarkersChangeListener) listener).validationProblemMarkersChanged(new EventObject(
					object));
			}
		}
	}

	private static String createProblemMarkerMessage(Exception exception)
	{
		Assert.isNotNull(exception);

		StringBuilder msg = new StringBuilder();
		msg.append(exception.getLocalizedMessage());
		Throwable cause = exception.getCause();
		if (cause != null)
		{
			String causeMsg = cause.getLocalizedMessage();
			if (causeMsg != null && causeMsg.length() > 0 && !msg.toString().contains(causeMsg))
			{
				msg.append(": "); //$NON-NLS-1$
				msg.append(causeMsg);
			}
		}
		return msg.toString();
	}

	/**
	 * @param format
	 * @param exception
	 * @return
	 */
	protected static String createProblemMarkerMessage(String format, Exception exception)
	{
		Assert.isNotNull(exception);

		String msg = createProblemMarkerMessage(exception);
		if (format == null)
		{
			return msg;
		}

		if (format.contains("{0}")) { //$NON-NLS-1$
			return NLS.bind(format, msg);
		}
		else
		{
			if (!format.endsWith(" ")) { //$NON-NLS-1$
				format.concat(" "); //$NON-NLS-1$
			}
			return format.concat(msg);
		}
	}

	/**
	 * @param diagnostic
	 */
	public static void handleDiagnostic(Diagnostic diagnostic)
	{
		handleDiagnostic(diagnostic, EObjectUtil.DEPTH_INFINITE);
	}

	/**
	 * Handles the given diagnostic and update markers on the concerned {@link EObject}.
	 *
	 * @param diagnostic
	 *        The validation diagnostic to handle.
	 * @param depth
	 *        depth of the diagnostic, see {@link EObjectUtil}
	 */
	public static void handleDiagnostic(Diagnostic diagnostic, int depth)
	{
		Assert.isNotNull(diagnostic);

		if (diagnostic.getData() == null)
		{
			return;
		}

		List<?> diagnosticData = diagnostic.getData();
		if (diagnosticData.isEmpty() || !(diagnosticData.get(0) instanceof EObject))
		{
			return;
		}

		/* The top level object validated. */
		EObject eObject = (EObject) diagnosticData.get(0);

		if (diagnostic.getSeverity() == Diagnostic.OK)
		{
			// Everything is OK; no error nor warning raised during validation. Let's clean old markers if any.
			try
			{
				removeMarkers(eObject, depth, IMarker.PROBLEM);
			}
			catch (CoreException ex)
			{
				PlatformLogUtil.logAsWarning(Activator.getDefault(), ex);
			}
		}
		else
		{
			final IProject project = MetaModelUtils.getProject(eObject);

			/*
			 * At least one error or warning has been found during validation. Existing markers must be updated or
			 * removed and new ones must be created.
			 */
			IFile markerSourceFile = getMarkerSourceFile(project);
			if (null != markerSourceFile)
			{
				try
				{
					removeMarkers(eObject, depth, IMarker.PROBLEM);
					addMarkers(markerSourceFile, diagnostic);
				}
				catch (CoreException ex)
				{
					PlatformLogUtil.logAsWarning(Activator.getDefault(), ex);
				}
			}
		}
	}

	/**
	 * Remove all markers directly attached to this eObject (depth set to IValidationMarkerManager.DEPTH_ZERO), to its
	 * direct children (depth set to IValidationMarkerManager.DEPTH_ONE), or to itself and all its children (depth set
	 * to IValidationMarkerManager.DEPTH_INFINITE)
	 *
	 * @param eObject
	 *        the target eObject
	 * @param depth
	 *        see {@link IValidationMarkerManager}
	 * @param markerType
	 *        type of marker to remove
	 * @throws CoreException
	 */
	public static void removeMarkers(EObject eObject, int depth, String markerType) throws CoreException
	{
		IMarker[] markers = getValidationMarkersList(eObject, depth, markerType);
		if (markers == null || markers.length == 0)
		{
			return;
		}

		for (IMarker marker: markers)
		{
			marker.delete();
		}

		// Notify IValidationProblemMarkerChangedListeners
		fireValidationProblemMarkersChanged(eObject);
	}

	/**
	 * Computes the model (resource-relative) URI based on the full URI including the resource.
	 *
	 * E.g., for the full URI "platform:/resource/FourZeroThree/CESSAR/myDet.ecuconfig#/CESSAR/myDet/DetGeneral", it
	 * returns "/CESSAR/myDet/DetGeneral".
	 *
	 * @param fullURI
	 *        the full URI of a model element.
	 *
	 * @return the model (resource-relative) URI of the element.
	 */
	private static String getObjectModelURI(String fullURI)
	{
		String result = fullURI;
		int hashIdx = fullURI.indexOf(OBJECT_URI_SEPARATOR);
		if (hashIdx != -1)
		{
			result = fullURI.substring(hashIdx + 1);
		}
		return result;
	}

	/**
	 * Decides whether {@link markerURI} refers to either the {@code EObject} pointed to by {@link eObjURI} or to a
	 * child of it.
	 *
	 * @param markerURI
	 * @param eObjURI
	 * @return <code>true</code> <b>iff</b> {@link markerURI} refers to either the {@code EObject} pointed to by
	 *         {@link eObjURI} or to a child of it.
	 */
	private static boolean isChild(String markerURI, String eObjURI)
	{
		if (eObjURI.isEmpty())
		{
			return true;
		}
		int lastCharIdx = eObjURI.length() - 1;
		String eParentURI = eObjURI;
		if (eObjURI.charAt(lastCharIdx) == '/')
		{
			eParentURI = eObjURI.substring(0, lastCharIdx);
		}

		return markerURI.equals(eParentURI) || markerURI.startsWith(eParentURI + '/');
	}

	/**
	 * Return an array of {IMarker}. This one is composed with markers of Type
	 * {@link IValidationMarker#MODEL_VALIDATION_PROBLEM} directly attached to this eObject, from its direct children
	 * only, or itself and also to its children, according to the depth value (respectively
	 * IValidationMarkerManager.DEPTH_ZERO, IValidationMarkerManager.DEPTH_ONE and
	 * IValidationMarkerManager.DET_INFINITE.)
	 *
	 * @param eObject
	 * @param depth
	 *        see {@link IValidationMarkerManager}
	 * @return an array of {@link IMarker}
	 * @throws CoreException
	 */
	public static IMarker[] getValidationMarkersList(EObject eObject, int depth) throws CoreException
	{

		return getValidationMarkersList(eObject, depth, IValidationMarker.MODEL_VALIDATION_PROBLEM);
	}

	/**
	 * Return an array of {IMarker}. This one is composed with markers directly attached to this eObject, from its
	 * direct children only, or itself and also to its children, according to the depth value (respectively
	 * IValidationMarkerManager.DEPTH_ZERO, IValidationMarkerManager.DEPTH_ONE and
	 * IValidationMarkerManager.DET_INFINITE.)
	 *
	 * @param eObject
	 * @param depth
	 *        see {@link IValidationMarkerManager}
	 * @param markerType
	 *        type of marker
	 * @return an array of {@link IMarker}
	 * @throws CoreException
	 */
	public static IMarker[] getValidationMarkersList(EObject eObject, int depth, String markerType)
		throws CoreException
	{
		IProject project = MetaModelUtils.getProject(eObject);
		IResource resource = getMarkerSourceFile(project);
		if (resource == null || !resource.exists())
		{
			return new IMarker[0];
		}

		// All the Markers connected with this resource
		IMarker[] allMarkers = resource.findMarkers(markerType, true, IResource.DEPTH_INFINITE);

		// URI of eObj
		String[] tmp = ValidationUtil.splitURI(eObject);
		if (tmp.length < 2)
		{
			return new IMarker[0];
		}
		String eObjURI = getObjectModelURI(tmp[0]);
		String eObjType = tmp[1];

		String markerURI;
		String markerEObjType;
		List<IMarker> result = new ArrayList<>();
		for (IMarker current: allMarkers)
		{
			if (current != null && current.exists())
			{
				tmp = ValidationUtil.splitURI((String) current.getAttribute(EValidator.URI_ATTRIBUTE));
				if (tmp != null && tmp.length == 2)
				{
					markerURI = getObjectModelURI(tmp[0]);
					markerEObjType = tmp[1];
					Object hash = current.getAttribute(IValidationMarker.HASH_ATTRIBUTE);
					switch (depth)
					{
						case EObjectUtil.DEPTH_ZERO:
							if (markerURI.equals(eObjURI) && eObjType.equals(markerEObjType)
								&& (Integer) hash == eObject.hashCode())
							{
								result.add(current);
							}
							break;
						case EObjectUtil.DEPTH_ONE: // same treatment, let's modify it later if necessary
						case EObjectUtil.DEPTH_INFINITE:
							if (isChild(markerURI, eObjURI))
							{
								result.add(current);
							}
							break;
						default:
							// Unexpected, do nothing
							break;
					}
				}
			}
			else
			{
				String msg = NLS.bind(Messages.warningNoSuchMarker, current == null ? "???" : current.getId()); //$NON-NLS-1$
				PlatformLogUtil.logAsWarning(Activator.getDefault(), msg);
			}
		}

		return result.toArray(new IMarker[result.size()]);
	}
}

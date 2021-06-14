/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Sep 2, 2010 2:50:07 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sphinx.emf.util.EObjectUtil;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import eu.cessar.ct.core.platform.concurrent.ICallback;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.splitable.ISplitableContextEditingStrategy;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;
import eu.cessar.ct.validation.EMFValidationMessages;
import eu.cessar.ct.validation.IValidationManager;
import eu.cessar.ct.validation.ValidationUtilsCommon;
import eu.cessar.req.Requirement;

/**
 * The Class AbstractEMFValidationPart.
 *
 * @author uidl6870
 */
@Requirement(
	reqID = "REQ_EDIT_PROP#4")
public abstract class AbstractEMFValidationPart extends SimpleValidationPart
{
	/** The Constant OK. */
	private static final String OK = "OK"; //$NON-NLS-1$

	/** Diagnostic object for the current processing element. */
	protected Diagnostic diagnostic;

	/** The call back. */
	private ICallback<Diagnostic> callBack;

	/**
	 * The Class EMFValidationRequestData.
	 */
	public static class EMFValidationRequestData
	{

		/** The object. */
		private EObject object;

		/** The validatation depth. */
		private int validatationDepth;

		/**
		 * Create a new {@link EMFValidationRequestData} with a depth of {@link EObjectUtil#DEPTH_ZERO}.
		 *
		 * @param object
		 *        the object
		 */
		public EMFValidationRequestData(EObject object)
		{
			this(object, EObjectUtil.DEPTH_ZERO);
		}

		/**
		 * Instantiates a new eMF validation request data.
		 *
		 * @param object
		 *        the object
		 * @param depth
		 *        the depth
		 */
		public EMFValidationRequestData(EObject object, int depth)
		{
			this.object = object;
			validatationDepth = depth;
		}

		/**
		 * Gets the object.
		 *
		 * @return the object
		 */
		public EObject getObject()
		{
			return object;
		}

		/**
		 * Gets the validation depth.
		 *
		 * @return the validation depth
		 */
		public int getValidationDepth()
		{
			return validatationDepth;
		}
	}

	/**
	 * Instantiates a new abstract emf validation part.
	 *
	 * @param editor
	 *        the editor
	 */
	public AbstractEMFValidationPart(IModelFragmentEditor editor)
	{
		super(editor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#refresh()
	 */
	@Override
	public void refresh()
	{
		Label label = getLabel();
		if (label != null && !label.isDisposed())
		{
			Image waitImage = CessarPluginActivator.getDefault().getImage(CessarPluginActivator.WAIT_ICON_ID);
			label.setImage(waitImage);
			if (callBack != null)
			{
				IValidationManager.INSTANCE.forgetWork(callBack);
			}
			callBack = new ICallback<Diagnostic>()
				{

				public void workDone(final Diagnostic diag)
				{
					callBack = null;
					Display display = Display.getDefault();
					display.asyncExec(new Runnable()
					{
						public void run()
						{
							updateValidationInfo(diag);
						}
					});
				}
				};

				IValidationManager.INSTANCE.performWork(getInputObject(), callBack);
		}
	}

	/**
	 * Return a list {@link EMFValidationRequestData} that have to be validated.
	 *
	 * @return the validation request data
	 */
	protected abstract List<EMFValidationRequestData> getValidationRequestData();

	/**
	 * Gets the values list.
	 *
	 * @return the values list
	 */
	protected abstract boolean hasMissingProxyReferences();

	/**
	 * Update validation info.
	 *
	 * @param diag
	 *        the diag
	 */
	protected void updateValidationInfo(Diagnostic diag)
	{
		diagnostic = diag;

		Label label = getLabel();
		if (label == null || label.isDisposed())
		{
			return;
		}

		String doc = getDocumentation();
		int status = getStatus();
		Image validationImage = null;
		boolean isProxyReference = hasMissingProxyReferences();

		if (isProxyReference)
		{
			boolean areEMFIntrinsicConstraintsPrefEnabled = ValidationUtilsCommon.areEMFIntrinsicConstraintsEnabled();
			String validationStatusMessage = areEMFIntrinsicConstraintsPrefEnabled ? EMFValidationMessages.EMFIntrinsicConstraintsStatusEnabled
				: EMFValidationMessages.EMFIntrinsicConstraintsStatusDisabled;
			validationStatusMessage = validationStatusMessage.concat("\n"); //$NON-NLS-1$
			doc = validationStatusMessage.concat(doc);
			if (status < IStatus.WARNING)
			{
				status = IStatus.WARNING;
			}
		}

		switch (status)
		{
			case IStatus.ERROR:
				validationImage = CessarPluginActivator.getDefault().getImage(CessarPluginActivator.ERROR_ICON_ID);
				break;

			case IStatus.WARNING:
				validationImage = CessarPluginActivator.getDefault().getImage(CessarPluginActivator.WARN_ICON_ID);
				break;

			case IStatus.INFO:
				validationImage = CessarPluginActivator.getDefault().getImage(CessarPluginActivator.INFO_ICON_ID);
				break;

			case IStatus.OK:
				if (doc != null && !OK.equals(doc) && !"".equals(doc)) //$NON-NLS-1$
				{
					validationImage = CessarPluginActivator.getDefault().getImage(CessarPluginActivator.INFO_ICON_ID);
				}
				break;
			default:
				// do nothing
		}

		label.setToolTipText(doc);
		label.setImage(validationImage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#dispose()
	 */
	@Override
	public void dispose()
	{
		if (callBack != null)
		{
			IValidationManager.INSTANCE.forgetWork(callBack);
			callBack = null;
		}
		diagnostic = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.SimpleValidationPart#getStatus()
	 */
	@Override
	protected int getStatus()
	{
		IStatus validationStatus = getValidationStatus();
		return validationStatus.getSeverity();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.SimpleValidationPart#getValidationStatus()
	 */
	@Override
	public IStatus getValidationStatus()
	{
		IStatus editableStatus = super.getValidationStatus();

		if (diagnostic == null)
		{
			return editableStatus;
		}
		else
		{
			MultiStatus multiStatus = new MultiStatus(CessarPluginActivator.PLUGIN_ID, 0, "OK", null); //$NON-NLS-1$

			if (!editableStatus.isOK())
			{
				multiStatus.add(editableStatus);
			}
			for (Diagnostic diag: diagnostic.getChildren())
			{
				if (isValidDiagnostic(diag))
				{
					IStatus stat = diagnosticToStatus(diag);
					updateMultiStatus(multiStatus, stat);
				}
			}

			return multiStatus;
		}
	}

	/**
	 * Gets the status in splitable context.
	 *
	 * @param strategy
	 *        editing strategy based o which to decide the status
	 * @return the validation status in the splittable context
	 */
	protected static IStatus getStatusInSplitableContext(ISplitableContextEditingStrategy strategy)
	{
		IStatus spliStatus = Status.OK_STATUS;

		boolean hasSplittableSupport = strategy.isSplittingAllowed();

		if (!hasSplittableSupport)
		{
			if (!strategy.areValuesConsistent())
			{
				spliStatus = new Status(IStatus.WARNING, CessarPluginActivator.PLUGIN_ID,
					"Different values set in different inputs"); //$NON-NLS-1$

			}
			else
			{
				List<EObject> fragmentsWithValue = strategy.getFragmentsWithValue();
				List<EObject> allFragments = strategy.getAllFragments();

				int size = fragmentsWithValue.size();
				int allSize = allFragments.size();
				if (size > 0 && size != allSize)
				{
					spliStatus = new Status(IStatus.WARNING, CessarPluginActivator.PLUGIN_ID,
						"The value is not set in all the resources in which the parent is defined"); //$NON-NLS-1$
				}
			}

		}
		return spliStatus;
	}

	/**
	 * Gets the updated status.
	 *
	 * @param currentStatus
	 *        the current status
	 * @param partialStatus
	 *        the status to be added to the current one, if its severity is different from {@link IStatus#OK}
	 * @return a status representing the result of merging the 2 given statuses
	 */
	protected static IStatus getUpdatedStatus(IStatus currentStatus, IStatus partialStatus)
	{
		// just return the current status
		if (partialStatus.isOK())
		{
			return currentStatus;
		}

		IStatus finalStatus = null;
		if (currentStatus.isOK())
		{
			finalStatus = partialStatus;
		}
		else if (currentStatus.isMultiStatus())
		{
			finalStatus = currentStatus;
			updateMultiStatus((MultiStatus) currentStatus, partialStatus);
		}
		else
		{
			finalStatus = new MultiStatus(CessarPluginActivator.PLUGIN_ID, IStatus.OK, "", null); //$NON-NLS-1$
			((MultiStatus) finalStatus).add(currentStatus);

			updateMultiStatus((MultiStatus) finalStatus, partialStatus);
		}

		return finalStatus;
	}

	/**
	 * Update multi status.
	 *
	 * @param multiStatus
	 *        the multi status
	 * @param toBeAdded
	 *        the to be added
	 */
	protected static void updateMultiStatus(MultiStatus multiStatus, IStatus toBeAdded)
	{
		// needn't to update
		if (toBeAdded.isOK())
		{
			return;
		}

		if (toBeAdded.isMultiStatus())
		{
			multiStatus.addAll(toBeAdded);
		}
		else
		{
			multiStatus.add(toBeAdded);
		}
	}

	/**
	 * Diagnostic to status.
	 *
	 * @param diagnostic
	 *        the diagnostic
	 * @return the i status
	 */
	private IStatus diagnosticToStatus(Diagnostic diag)
	{
		IStatus status = new Status(diag.getSeverity(), CessarPluginActivator.PLUGIN_ID, diag.getMessage());
		return status;
	}

	/**
	 * Checks if is valid diagnostic.
	 *
	 * @param diag
	 *        the diag
	 * @return true, if is valid diagnostic
	 */
	protected abstract boolean isValidDiagnostic(Diagnostic diag);
}

package eu.cessar.ct.edit.ui.facility;

import java.util.ArrayList;
import java.util.List;

import org.artop.aal.gautosar.services.splitting.Splitable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.ui.events.IFocusEventListener;
import eu.cessar.ct.core.platform.ui.util.CessarFormToolkit;
import eu.cessar.ct.edit.ui.facility.parts.DropDownActionPart;
import eu.cessar.ct.edit.ui.facility.parts.EEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.IActionPart;
import eu.cessar.ct.edit.ui.facility.parts.ICaptionPart;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.IResourcesPart;
import eu.cessar.ct.edit.ui.facility.parts.IValidationPart;
import eu.cessar.ct.edit.ui.facility.parts.NullResourcesPart;
import eu.cessar.ct.edit.ui.facility.splitable.ISplitableContextEditingManager;
import eu.cessar.ct.edit.ui.facility.splitable.ISplitableContextEditingStrategy;
import eu.cessar.ct.edit.ui.facility.splitable.NullSplitableContextEditingManager;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;

/**
 * 
 * Base implementation of a model fragment editor
 * 
 * @author uidl6870
 * 
 *         %created_by: uidu8153 %
 * 
 *         %date_created: Fri Nov 14 09:08:35 2014 %
 * 
 *         %version: RAUTOSAR~20 %
 */
public abstract class AbstractModelEditor implements IModelFragmentEditor
{
	private boolean initialized;
	private EObject inputObject;
	private IModelFragmentEditorProvider editorProvider;
	private CessarFormToolkit formToolkit;

	private ICaptionPart captionPart;
	private IResourcesPart resourcesPart;
	private IValidationPart validationPart;
	private IEditorPart editorPart;
	private IActionPart actionPart;

	private IFocusEventListener eventListener;
	private boolean deliverFocusLost = true;

	private boolean enabled = true;
	private List<IModelEditorChangeListener> modelEditorChangeListeners = new ArrayList<IModelEditorChangeListener>();

	private ISplitableContextEditingManager splitableContextEditingManager;

	public String getTypeId()
	{
		return editorProvider.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#getEditorPart(eu.cessar.ct.edit.ui.EEditorPart)
	 */
	public IModelFragmentEditorPart getPart(EEditorPart part)
	{
		IModelFragmentEditorPart result = null;
		switch (part)
		{
			case CAPTION:
				if (captionPart == null)
				{
					captionPart = createCaptionPart();
				}
				result = captionPart;
				break;

			case ACTION:
				if (actionPart == null)
				{
					actionPart = createActionPart();
				}

				result = actionPart;
				break;

			case VALIDATION:
				if (validationPart == null)
				{
					validationPart = createValidationPart();
				}
				result = validationPart;
				break;

			case RESOURCES:

				if (resourcesPart == null)
				{
					resourcesPart = createResourcesPart();
				}
				result = resourcesPart;
				break;

			case EDITING_AREA:
				if (editorPart == null)
				{
					editorPart = createEditorPart();
				}
				result = editorPart;
				break;

			default:// do nothing
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#getCaptionPart()
	 */

	public final ICaptionPart getCaptionPart()
	{
		return (ICaptionPart) getPart(EEditorPart.CAPTION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#getValidationPart()
	 */
	public final IValidationPart getValidationPart()
	{
		return (IValidationPart) getPart(EEditorPart.VALIDATION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#getEditorPart()
	 */
	public final IEditorPart getEditorPart()
	{
		return (IEditorPart) getPart(EEditorPart.EDITING_AREA);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#getActionPart()
	 */
	public final IActionPart getActionPart()
	{
		return (IActionPart) getPart(EEditorPart.ACTION);
	}

	/**
	 * @return the part corresponding to the label area of the editor
	 */
	protected abstract ICaptionPart createCaptionPart();

	/**
	 * @return the part providing validation feedback
	 */
	protected abstract IValidationPart createValidationPart();

	/**
	 * @return the part indicating the resources in which the editor's values are actually stored
	 */
	protected IResourcesPart createResourcesPart()
	{
		return new NullResourcesPart(this);
	}

	/**
	 * @return the part representing the editing area
	 */
	protected abstract IEditorPart createEditorPart();

	/**
	 * 
	 * @return the action part
	 */
	protected IActionPart createActionPart()
	{
		return new DropDownActionPart(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.IModelFragmentEditor#setEditorProvider(eu.cessar.ct.edit.ui.IModelFragmentEditorProvider)
	 */
	public void setEditorProvider(IModelFragmentEditorProvider editorProvider)
	{
		this.editorProvider = editorProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IModelFragmentEditor#getEditorProvider()
	 */
	public IModelFragmentEditorProvider getEditorProvider()
	{

		return editorProvider;
	}

	/**
	 * called from wrapper
	 */
	public void setInput(EObject object)
	{
		inputObject = object;

	}

	/**
	 * @return
	 */
	protected TransactionalEditingDomain getEditingDomain()
	{
		return MetaModelUtils.getEditingDomain(inputObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#refresh()
	 */
	public void refresh()
	{
		checkInit();

		if (isInputSplitable())
		{
			splitableContextEditingManager = createSplitableContextEditingManager();
		}

		getPart(EEditorPart.CAPTION).refresh();
		getPart(EEditorPart.RESOURCES).refresh();
		getPart(EEditorPart.VALIDATION).refresh();
		getPart(EEditorPart.EDITING_AREA).refresh();
		getPart(EEditorPart.ACTION).refresh();

	}

	/**
	 * 
	 * @return the created manager
	 */
	protected abstract ISplitableContextEditingManager createSplitableContextEditingManager();

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#isInformative()
	 */
	public boolean isInformative()
	{
		// by default, not informative, editors in general are meant to "edit"
		// a piece of the model
		return false;
	}

	/**
	 * 
	 */
	public void refreshWithNotifications()
	{
		refresh();
		ModelEditorChangeEvent changeEvent = new ModelEditorChangeEvent(this);

		for (IModelEditorChangeListener listener: modelEditorChangeListeners)
		{
			listener.modelEditorChanged(changeEvent);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#addModelEditorChangeListener(eu.cessar.ct.edit.ui.facility
	 * .IModelEditorChangeListener)
	 */
	public void addModelEditorChangeListener(IModelEditorChangeListener listener)
	{
		if (listener != null)
		{
			modelEditorChangeListeners.add(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#addEventListener(eu.cessar.ct.edit.ui.facility.IEventListener)
	 */
	public void setEventListener(IFocusEventListener listener)
	{
		eventListener = listener;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#setFocusLostEnablement(boolean)
	 */
	public void deliverFocusLost(boolean enablement)
	{
		deliverFocusLost = enablement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#shouldDeliverFocusLost()
	 */
	public boolean shouldDeliverFocusLost()
	{
		return deliverFocusLost;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#getEventListener()
	 */
	public IFocusEventListener getEventListener()
	{
		return eventListener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#removeModelEditorChangeListener(eu.cessar.ct.edit.ui.facility
	 * .IModelEditorChangeListener)
	 */
	public void removeModelEditorChangeListener(IModelEditorChangeListener listener)
	{
		if (listener != null)
		{
			modelEditorChangeListeners.remove(listener);
		}
	}

	/**
	 * 
	 */
	private void checkInit()
	{
		if (!initialized)
		{
			try
			{
				doInitialize();
			}
			finally
			{
				initialized = true;
			}
		}
	}

	/**
	 * This method will be executed during the first refresh
	 */
	protected abstract void doInitialize();

	/**
	 * called from wrapper
	 */
	public void dispose()
	{
		getPart(EEditorPart.CAPTION).dispose();
		getPart(EEditorPart.RESOURCES).dispose();
		getPart(EEditorPart.VALIDATION).dispose();
		getPart(EEditorPart.EDITING_AREA).dispose();
		getPart(EEditorPart.ACTION).dispose();

		if (modelEditorChangeListeners != null)
		{
			modelEditorChangeListeners.clear();
		}
	}

	/**
	 * @return the eObject
	 */
	public EObject getInput()
	{
		return inputObject;
	}

	/**
	 * @param formToolkit
	 *        the widgetFactory to set
	 */
	public void setFormToolkit(CessarFormToolkit formToolkit)
	{
		this.formToolkit = formToolkit;
	}

	/**
	 * @return the widgetFactory
	 */
	public CessarFormToolkit getFormToolkit()
	{
		return formToolkit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#isReadOnly()
	 */
	public IStatus getEditableStatus()
	{
		EObject input = getInput();
		if (input == null)
		{
			return new Status(IStatus.INFO, CessarPluginActivator.PLUGIN_ID, "No input set"); //$NON-NLS-1$
		}

		// default
		IStatus status = Status.OK_STATUS;

		if (isInputSplitable())
		{
			if (isMultiValueEditor())
			{
				IStatus noSupportStatus = new Status(IStatus.INFO, CessarPluginActivator.PLUGIN_ID,
					"Editing is not currently supported"); //$NON-NLS-1$
				status = noSupportStatus;
			}
			else
			{
				ISplitableContextEditingManager editorManager = getSplitableContextEditingManager();
				if (!(editorManager instanceof NullSplitableContextEditingManager))
				{
					status = getEditableStatusInSplitableContext();

				}
			}
		}

		return status;
	}

	/**
	 * The implementation of the different types of editors should override this.
	 * 
	 * @return the editable status in the context of splitable input
	 */
	@SuppressWarnings("static-method")
	protected IStatus getEditableStatusInSplitableContext()
	{
		// default
		return Status.OK_STATUS;
	}

	/**
	 * Called by the different types of editors
	 * 
	 * @param strategy
	 *        editing strategy for a particular editor
	 * @return the editable status in the context of splitable input, based on the logic
	 */
	protected static final IStatus doGetEditableStatusInSplitableContext(ISplitableContextEditingStrategy strategy)
	{
		IStatus status = Status.OK_STATUS;

		if (strategy != null)
		{
			boolean editingAllowed = strategy.isEditingAllowed();
			if (!editingAllowed)
			{
				status = new Status(IStatus.INFO, CessarPluginActivator.PLUGIN_ID,
					"Cannot edit because of splittable constraints"); //$NON-NLS-1$
			}
		}
		return status;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;

		getPart(EEditorPart.CAPTION).setEnabled(enabled);
		getPart(EEditorPart.RESOURCES).setEnabled(enabled);
		getPart(EEditorPart.VALIDATION).setEnabled(enabled);
		getPart(EEditorPart.EDITING_AREA).setEnabled(enabled);
		getPart(EEditorPart.ACTION).setEnabled(enabled);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#getEnabled()
	 */
	public boolean isEnabled()
	{
		return enabled;
	}

	/**
	 * @return whether the editor's input is a wrapper over a splitted object
	 */
	public boolean isInputSplitable()
	{
		return getInput() instanceof Splitable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#getEditingAreaContentVisibility()
	 */
	@Override
	public IStatus getEditingAreaContentVisibility()
	{
		// default behavior
		return Status.OK_STATUS;
	}

	/**
	 * @param strategy
	 *        the editing strategy to be used
	 * @return the status indicating the visibility status of the editing part in the splitable context
	 */
	protected static final IStatus getEditingAreaContentVisibilityInSplitableContext(
		ISplitableContextEditingStrategy strategy)
	{
		// always display editing area's content if input non-splitable
		if (strategy == null)
		{
			return Status.OK_STATUS;
		}

		boolean consistent = strategy.areValuesConsistent();
		if (consistent)
		{
			return Status.OK_STATUS;
		}
		else
		{
			return new Status(IStatus.INFO, CessarPluginActivator.PLUGIN_ID,
				"Content not visible because of splitable-related inconsistencies"); //$NON-NLS-1$
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#getSplitableContextEditorManager()
	 */
	@Override
	public ISplitableContextEditingManager getSplitableContextEditingManager()
	{
		if (!isInputSplitable())
		{
			return null;
		}
		return splitableContextEditingManager;
	}

}

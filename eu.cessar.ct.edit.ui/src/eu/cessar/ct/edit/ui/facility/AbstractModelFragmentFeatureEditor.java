package eu.cessar.ct.edit.ui.facility;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.util.StringUtils;
import eu.cessar.ct.edit.ui.facility.parts.FeatureValidationPart;
import eu.cessar.ct.edit.ui.facility.parts.IActionPart;
import eu.cessar.ct.edit.ui.facility.parts.ICaptionPart;
import eu.cessar.ct.edit.ui.facility.parts.IResourcesPart;
import eu.cessar.ct.edit.ui.facility.parts.IValidationPart;
import eu.cessar.ct.edit.ui.facility.parts.SingleFeatureCaptionPart;
import eu.cessar.ct.edit.ui.facility.parts.SingleFeatureResourcesPart;
import eu.cessar.ct.edit.ui.facility.splitable.ISplitableContextFeatureEditingStrategy;
import eu.cessar.ct.edit.ui.facility.splitable.ISplitableContextSingleFeatureEditingManager;
import eu.cessar.ct.edit.ui.facility.splitable.SplitableContextSingleFeatureEditingManager;
import eu.cessar.ct.edit.ui.internal.facility.FacilityConstants;
import eu.cessar.ct.edit.ui.internal.facility.actions.RemoveFeatureValueAction;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;

/**
 * Base implementation of a single feature editor.
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Oct 29 08:39:54 2012 %
 * 
 *         %version: 14 %
 */
public abstract class AbstractModelFragmentFeatureEditor extends AbstractModelEditor implements
	IModelFragmentFeatureEditor
{
	private EClass inputClass;
	private EStructuralFeature inputFeature;
	private ResourceSetListener resourceSetListener;

	private ISplitableContextSingleFeatureEditingManager manager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#getInstanceId()
	 */
	public String getInstanceId()
	{
		String inputClassName;
		if (getInputClass() == null)
		{
			inputClassName = "." + FacilityConstants.WARNING_CLASS_MISSING; //$NON-NLS-1$
		}
		else
		{
			inputClassName = "." + getInputClass().getName(); //$NON-NLS-1$
		}

		// in case the editor is for an object from the ECUC side
		EObject input = getInput();
		if (input instanceof GModuleConfiguration)
		{
			GModuleDef definition = ((GModuleConfiguration) input).gGetDefinition();
			inputClassName = MetaModelUtils.getAbsoluteQualifiedName(definition);
		}
		else
		{
			if (input instanceof GContainer)
			{
				GContainerDef definition = ((GContainer) input).gGetDefinition();
				inputClassName = MetaModelUtils.getAbsoluteQualifiedName(definition);
			}
		}
		String featureName = FacilityConstants.WARNING_FEATURE_MISSING;
		if (inputFeature != null)
		{
			featureName = inputFeature.getName();
		}
		return getTypeId() + "#" + featureName + inputClassName; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createCaptionPart()
	 */
	@Override
	protected ICaptionPart createCaptionPart()
	{
		String caption = ""; //$NON-NLS-1$
		String featureName = getInputFeature().getName();
		if (featureName != null)
		{
			caption = StringUtils.toTitleCase(featureName);
		}
		return new SingleFeatureCaptionPart(this, caption);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createResourcesPart()
	 */
	@Override
	protected IResourcesPart createResourcesPart()
	{
		return new SingleFeatureResourcesPart(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createValidationPart()
	 */
	@Override
	protected IValidationPart createValidationPart()
	{
		return new FeatureValidationPart(this);
	}

	/**
	 * @param part
	 */
	public void populateActionPart(IActionPart part)
	{
		Action removeFeatureValueAction = new RemoveFeatureValueAction(this);
		part.getMenuManager().add(removeFeatureValueAction);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ui.properties.IDynamicSection#setInputClass(org.eclipse.emf.ecore.EClass)
	 */
	public void setInputClass(EClass eClass)
	{
		inputClass = eClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ui.properties.IDynamicSection#getInputClass()
	 */
	public EClass getInputClass()
	{
		return inputClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ui.properties.IDynamicSection#setInputFeature(org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public void setInputFeature(EStructuralFeature feature)
	{
		inputFeature = feature;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ui.properties.IDynamicSection#getInputFeature()
	 */
	public EStructuralFeature getInputFeature()
	{
		return inputFeature;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#doInitialize()
	 */
	@Override
	protected void doInitialize()
	{
		// put a resource set listener on the domain
		TransactionalEditingDomain editingDomain = getEditingDomain();
		if (editingDomain != null)
		{
			resourceSetListener = createResourceSetListener();
			editingDomain.addResourceSetListener(resourceSetListener);
		}
	}

	/**
	 * @return
	 */
	private ResourceSetListener createResourceSetListener()
	{
		return new ResourceSetListenerImpl(NotificationFilter.createFeatureFilter(getInputFeature()))
		{
			/*
			 * @see org.eclipse.emf.transaction.ResourceSetListenerImpl#resourceSetChanged(ResourceSetChangeEvent)
			 */
			@Override
			public void resourceSetChanged(ResourceSetChangeEvent event)
			{
				Display display = PlatformUI.getWorkbench().getDisplay();
				display.asyncExec(new Runnable()
				{

					public void run()
					{
						refreshWithNotifications();
					}
				});
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#dispose()
	 */
	@Override
	public void dispose()
	{
		super.dispose();
		// remove the resource set listener
		if (resourceSetListener != null)
		{
			TransactionalEditingDomain editingDomain = getEditingDomain();
			if (editingDomain != null)
			{
				editingDomain.removeResourceSetListener(resourceSetListener);
			}
		}
	}

	/**
	 * @return the isMultiValue
	 */
	public boolean isMultiValueEditor()
	{
		return getInputFeature().isMany();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#isValueMandatory()
	 */
	public boolean isValueMandatory()
	{
		return getInputFeature().getLowerBound() > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#isValueSet()
	 */
	public boolean isValueSet()
	{
		return getInput() != null && getInput().eIsSet(getInputFeature());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#getEditableStatusInSplitableContext()
	 */
	@Override
	protected IStatus getEditableStatusInSplitableContext()
	{
		ISplitableContextFeatureEditingStrategy strategy = getSplitableContextEditingManager().getStrategy();
		return doGetEditableStatusInSplitableContext(strategy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createSplitableContextEditorManager()
	 */
	@Override
	protected ISplitableContextSingleFeatureEditingManager createSplitableContextEditingManager()
	{
		manager = new SplitableContextSingleFeatureEditingManager(this);
		return manager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#getSplitableContextEditorManager()
	 */
	@Override
	public ISplitableContextSingleFeatureEditingManager getSplitableContextEditingManager()
	{
		return manager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#getEditingAreaContentVisibility()
	 */
	@Override
	public IStatus getEditingAreaContentVisibility()
	{
		// always display editing area's content if input non-splitable
		if (!isInputSplitable())
		{
			return Status.OK_STATUS;
		}

		return getEditingAreaContentVisibilityInSplitableContext(getSplitableContextEditingManager().getStrategy());
	}
}

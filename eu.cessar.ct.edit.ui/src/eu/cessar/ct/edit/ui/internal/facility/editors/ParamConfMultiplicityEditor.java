package eu.cessar.ct.edit.ui.internal.facility.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.edit.ui.facility.AbstractModelFragmentMultiFeatureEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;
import eu.cessar.ct.edit.ui.facility.RemoveEditedFeaturesAction;
import eu.cessar.ct.edit.ui.facility.parts.IActionPart;
import eu.cessar.ct.edit.ui.facility.parts.ICaptionPart;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.IValidationPart;
import eu.cessar.ct.edit.ui.facility.parts.SimpleValidationPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.EditedFeaturesCaptionPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.ParamConfMultiplicityEditorPart;
import eu.cessar.ct.edit.ui.internal.facility.FacilityConstants;

/**
 * Editor for editing of lower/upper multiplicity for MM 2.x/3.x
 * 
 */
public class ParamConfMultiplicityEditor extends AbstractModelFragmentMultiFeatureEditor
{

	private EStructuralFeature lowerMultiplicityFeature;
	private EStructuralFeature upperMultiplicityFeature;

	private ResourceSetListener resourceSetListenerLower;
	private ResourceSetListener resourceSetListenerUpper;
	private EClass inputClass;

	/**
	 * 
	 * @param provider
	 */
	public ParamConfMultiplicityEditor(IModelFragmentEditorProvider provider)
	{
		setEditorProvider(provider);
		lowerMultiplicityFeature = getEditorProvider().getEditedFeatures().get(0);
		upperMultiplicityFeature = getEditorProvider().getEditedFeatures().get(1);
	}

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
			inputClassName = FacilityConstants.WARNING_CLASS_MISSING;
		}
		else
		{
			inputClassName = getInputClass().getName();
		}
		StringBuffer featureName = new StringBuffer();
		List<EStructuralFeature> editedFeatures = getEditorProvider().getEditedFeatures();
		for (EStructuralFeature feature: editedFeatures)
		{
			featureName.append(feature.getName());
			featureName.append("."); //$NON-NLS-1$
		}
		return getTypeId() + "#" + featureName.toString() + inputClassName;//$NON-NLS-1$
		//		return getTypeId() + "#" + ModelUtils.getAbsoluteQualifiedName((getInput()));//$NON-NLS-1$
	}

	/**
	 * @return
	 */
	protected EClass getInputClass()
	{
		return inputClass;
	}

	public void setInputClass(EClass clz)
	{
		inputClass = clz;
	}

	public EStructuralFeature getLowerMultiplicityFeature()
	{
		return lowerMultiplicityFeature;
	}

	public EStructuralFeature getUpperMultiplicityFeature()
	{
		return upperMultiplicityFeature;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createCaptionPart()
	 */
	@Override
	protected ICaptionPart createCaptionPart()
	{
		return new EditedFeaturesCaptionPart(this, "Multiplicity"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createEditorPart()
	 */
	@Override
	protected IEditorPart createEditorPart()
	{
		return new ParamConfMultiplicityEditorPart(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createValidationPart()
	 */
	@Override
	protected IValidationPart createValidationPart()
	{
		return new SimpleValidationPart(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#doInitialize()
	 */
	@Override
	protected void doInitialize()
	{
		TransactionalEditingDomain editingDomain = getEditingDomain();
		if (editingDomain != null)
		{
			resourceSetListenerLower = createResourceSetListener(lowerMultiplicityFeature);
			editingDomain.addResourceSetListener(resourceSetListenerLower);

			resourceSetListenerUpper = createResourceSetListener(upperMultiplicityFeature);
			editingDomain.addResourceSetListener(resourceSetListenerUpper);
		}

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

		removeListener(resourceSetListenerLower);
		removeListener(resourceSetListenerUpper);
	}

	private void removeListener(ResourceSetListener listener)
	{
		if (listener != null)
		{
			TransactionalEditingDomain editingDomain = getEditingDomain();
			if (editingDomain != null)
			{
				editingDomain.removeResourceSetListener(listener);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#isMultiValueEditor()
	 */
	public boolean isMultiValueEditor()
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#populateActionPart(eu.cessar.ct.edit.ui.facility.parts.IActionPart
	 * )
	 */
	public void populateActionPart(IActionPart part)
	{
		part.getMenuManager().add(new RemoveEditedFeaturesAction(this));
	}

	/**
	 * @return
	 */
	private ResourceSetListener createResourceSetListener(EStructuralFeature feature)
	{
		return new ResourceSetListenerImpl(NotificationFilter.createFeatureFilter(feature))
		{
			/*
			 * @seeorg.eclipse.emf.transaction.ResourceSetListenerImpl# resourceSetChanged(ResourceSetChangeEvent)
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
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#isValueMandatory()
	 */
	public boolean isValueMandatory()
	{
		return lowerMultiplicityFeature.getLowerBound() + upperMultiplicityFeature.getLowerBound() > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#isValueSet()
	 */
	public boolean isValueSet()
	{
		EObject input = getInput();
		if (input == null)
		{
			return false;
		}
		return input.eIsSet(lowerMultiplicityFeature) || input.eIsSet(upperMultiplicityFeature);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentMultiFeatureEditor#getInputFeatures()
	 */
	public List<EStructuralFeature> getInputFeatures()
	{
		List<EStructuralFeature> features = new ArrayList<EStructuralFeature>();
		features.add(lowerMultiplicityFeature);
		features.add(upperMultiplicityFeature);

		return features;
	}
}

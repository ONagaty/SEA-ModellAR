package eu.cessar.ct.edit.ui.facility.expansion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.platform.ui.util.CessarFormToolkit;
import eu.cessar.ct.edit.ui.IEditingFacility;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;

/**
 * @author uidl7321
 * 
 */
public abstract class AbstractModelFragmentEditorExpansion implements IModelFragmentEditorExpansion
{
	private CessarFormToolkit toolkit;
	private IModelFragmentEditor masterEditor;
	private List<String> featureNamesForExpansion;

	private List<IModelFragmentEditorProvider> editorProviders;
	private List<IModelFragmentEditor> upperLevelEditors;

	private boolean isExpanded;

	private EExpansionType relationType;

	public AbstractModelFragmentEditorExpansion(IModelFragmentEditor masterEditor,
		List<String> featureNamesForExpansion)
	{
		this.masterEditor = masterEditor;
		this.featureNamesForExpansion = featureNamesForExpansion;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansion#getEditorProviders(org.eclipse.emf.ecore.EObject, java.util.List)
	 */
	public List<IModelFragmentEditorProvider> getEditorProviders(EObject input,
		List<IModelFragmentEditor> upperLevelEditors)
	{
		if (input == null)
		{
			// if (masterEditor != null)
			// {
			// EObject realInput = masterEditor.getInput();
			// if (realInput == null)
			// {
			return Collections.emptyList();
			// }
			// else
			// {
			// input = realInput;
			// }
			// }
		}

		// compute the expansion editors only if necessary
		if (editorProviders == null || editorProviders.size() == 0)
		{
			editorProviders = IEditingFacility.eINSTANCE.getSimpleEditorsProviders(input,
				input.eClass(), computeFeaturesForExpansion(input), IEditingFacility.EDITOR_ALL);

			this.upperLevelEditors = upperLevelEditors;
			editorProviders = computeUniqueEditorProviders(upperLevelEditors);
		}

		else
		{
			// eliminate the duplicate editors from the upperLevelEditors only
			// if necessary
			if (!this.upperLevelEditors.containsAll(upperLevelEditors))
			{
				this.upperLevelEditors = upperLevelEditors;
				editorProviders = computeUniqueEditorProviders(upperLevelEditors);
			}
		}

		return editorProviders;
	}

	/**
	 * 
	 * @param input2
	 * @return
	 */
	private List<EStructuralFeature> computeFeaturesForExpansion(EObject input)
	{
		List<EStructuralFeature> featuresForExpansion = new ArrayList<EStructuralFeature>();
		if (input != null)
		{
			EClass inputEClass = input.eClass();
			for (String featureName: featureNamesForExpansion)
			{
				EStructuralFeature feature = inputEClass.getEStructuralFeature(featureName);
				if (feature == null)
				{
					CessarPluginActivator.getDefault().logError(
						"Could not find the feature {0}  on the class  {1}" //$NON-NLS-1$
						, new Object[] {featureName, inputEClass.getName()});
				}
				else
				{
					featuresForExpansion.add(feature);
				}
			}
		}

		return featuresForExpansion;
	}

	/**
	 * 
	 * @param upperLevelEditors
	 * @return
	 */
	private List<IModelFragmentEditorProvider> computeUniqueEditorProviders(
		List<IModelFragmentEditor> upperLevelEditors)
	{
		List<EStructuralFeature> editedFeatures = new ArrayList<EStructuralFeature>();

		for (IModelFragmentEditor upperLevelEditor: upperLevelEditors)
		{
			EObject upperLevelEditorInput = upperLevelEditor.getInput();
			if (upperLevelEditorInput != null
				&& upperLevelEditorInput.equals(getMasterEditor().getInput()))
			{
				editedFeatures.addAll(upperLevelEditor.getEditorProvider().getEditedFeatures());
			}

		}

		List<IModelFragmentEditorProvider> uniqueEditorProviders = new ArrayList<IModelFragmentEditorProvider>();

		for (IModelFragmentEditorProvider provider: editorProviders)
		{
			if (!editedFeatures.containsAll(provider.getEditedFeatures()))
			{
				uniqueEditorProviders.add(provider);
			}
		}

		return uniqueEditorProviders;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansion#canExpand()
	 */
	public abstract boolean canExpand();

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansion#isMultiValueEditor()
	 */
	public abstract boolean isMultiValueEditor();

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansion#dispose()
	 */
	public void dispose()
	{

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansion#setToolkit(eu.cessar.ct.core.platform.ui.util.CessarFormToolkit)
	 */
	public void setToolkit(CessarFormToolkit toolkit)
	{
		this.toolkit = toolkit;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansion#getMasterEditor()
	 */
	public IModelFragmentEditor getMasterEditor()
	{
		return masterEditor;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansion#isExpanded()
	 */
	public boolean isExpanded()
	{
		return isExpanded;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansion#setExpanded(boolean)
	 */
	public void setExpanded(boolean expanded)
	{
		this.isExpanded = expanded;
		doSetExpanded(expanded);

	}

	/**
	 * @param expanded
	 */
	protected abstract void doSetExpanded(boolean expanded);
}

package eu.cessar.ct.edit.ui.internal.facility.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.sphinx.emf.model.IModelDescriptor;
import org.eclipse.sphinx.emf.model.ModelDescriptorRegistry;
import org.eclipse.sphinx.emf.util.EObjectUtil;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentReferenceEditor;
import eu.cessar.ct.edit.ui.facility.parts.IActionPart;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.MultiReferenceEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.SingleReferenceEditorPart;
import eu.cessar.ct.edit.ui.instanceref.IReferenceLabelProvider;
import eu.cessar.ct.edit.ui.internal.facility.actions.GoToReferenceContributionItem;
import eu.cessar.ct.sdk.utils.ModelUtils;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

public class DefaultReferenceFeatureEditor extends AbstractModelFragmentFeatureEditor implements
	IModelFragmentReferenceEditor
{

	private class ReferenceLabelProvider extends LabelProvider implements
		IReferenceLabelProvider<EObject>
	{

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
		 */
		@Override
		public Image getImage(Object element)
		{
			return super.getImage(element);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
		 */
		@Override
		public String getText(Object element)
		{
			if (element instanceof GIdentifiable)
			{
				return MetaModelUtils.getAbsoluteQualifiedName((GIdentifiable) element);

			}
			if (element instanceof EObject)
			{
				return ((EObject) element).eClass().getName();
			}
			return ""; //$NON-NLS-1$
		}

		/* (non-Javadoc)
		 * @see eu.cessar.ct.edit.ui.instanceref.IReferenceLabelProvider#getTooltip(java.util.List)
		 */
		public String getTooltip(List<EObject> elem)
		{
			String emptyString = ""; //$NON-NLS-1$
			String toolTip = emptyString;
			// There should be only one element in the list
			if (elem != null && !elem.isEmpty())
			{
				for (EObject identifiable: elem)
				{
					toolTip = toolTip + (toolTip.length() == 0 ? emptyString : " /n "); //$NON-NLS-1$
					toolTip = toolTip + ModelUtils.getAbsoluteQualifiedName(identifiable)
						+ OF_TYPE_CONSTANT + identifiable.eClass().getName();
				}
			}
			return toolTip;
		}
	}

	private ILabelProvider labelProvider;

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createEditorPart()
	 */
	@Override
	protected IEditorPart createEditorPart()
	{
		if (!isMultiValueEditor())
		{
			return new SingleReferenceEditorPart(this);
		}
		else
		{
			return new MultiReferenceEditorPart(this);
		}
	}

	/**
	 * Return a list of candidates or an empty list if there are no candidates
	 * 
	 * @return
	 */
	public List<Object> getCandidates()
	{
		List<Object> result = new ArrayList<Object>();
		if (getInput() != null && getInputFeature() != null)
		{
			IModelDescriptor descriptor = ModelDescriptorRegistry.INSTANCE.getModel(EcorePlatformUtil.getFile(getInput()));
			if (descriptor != null)
			{
				result.addAll(EObjectUtil.getAllInstancesOf(descriptor,
					((EReference) getInputFeature()).getEReferenceType().getInstanceClass(), false));
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentReferenceEditor#getLabelProvider()
	 */
	public ILabelProvider getLabelProvider()
	{
		if (labelProvider == null)
		{
			labelProvider = new ReferenceLabelProvider();
		}
		return labelProvider;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor#populateActionPart(eu.cessar.ct.edit.ui.facility.parts.IActionPart)
	 */
	@Override
	public void populateActionPart(IActionPart part)
	{
		super.populateActionPart(part);
		part.getMenuManager().add(new GoToReferenceContributionItem(this));
	}
}

package eu.cessar.ct.edit.ui.internal.facility.editors;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.edit.ui.facility.AbstractEditorProvider;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorDelegate;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GAUTOSAR;

public class ReferencedByEditorDelegate implements IModelFragmentEditorDelegate
{

	private static class ReferencedByEditorProvider extends AbstractEditorProvider
	{

		private static final IModelFragmentEditorProvider eINSTANCE = new ReferencedByEditorProvider();

		/* (non-Javadoc)
		 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#createEditor()
		 */
		public IModelFragmentEditor createEditor()
		{
			// TODO Auto-generated method stub
			IModelFragmentEditor result = new ReferencedByEditor();
			result.setEditorProvider(this);
			return result;
		}

		/* (non-Javadoc)
		 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#getCategories()
		 */
		public String[] getCategories()
		{
			return new String[] {"referenced"}; //$NON-NLS-1$
		}

		/* (non-Javadoc)
		 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#getEditedFeatures()
		 */
		public List<EStructuralFeature> getEditedFeatures()
		{
			return Collections.emptyList();
		}

		/* (non-Javadoc)
		 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#getId()
		 */
		public String getId()
		{
			return "GARObject.referencedBy"; //$NON-NLS-1$
		}

		/* (non-Javadoc)
		 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#getPriority()
		 */
		public int getPriority()
		{
			return 0;
		}

		/* (non-Javadoc)
		 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#isMetaEditor()
		 */
		public boolean isMetaEditor()
		{
			return true;
		}

	}

	public List<IModelFragmentEditorProvider> getEditors(EObject object, EClass clz)
	{
		if (!(object instanceof GAUTOSAR) && !(object instanceof GARPackage))
		{
			return Collections.singletonList(ReferencedByEditorProvider.eINSTANCE);
		}
		else
		{
			return Collections.emptyList();
		}
	}
}

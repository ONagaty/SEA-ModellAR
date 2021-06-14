package eu.cessar.ct.edit.ui.internal.facility.composition;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.edit.ui.facility.composition.ECompositionType;
import eu.cessar.ct.edit.ui.facility.composition.IEditorComposition;

public interface IEditorCompositionProvider
{
	List<? extends IEditorComposition> getEditorCompositions(EObject object);

	List<? extends IEditorComposition> getEditorCompositions(EClass clz);

	ECompositionType getType();

	void setType(ECompositionType type);

	/**
	 * Invoked by the editor compositions, on the first call of
	 * {@link IEditorComposition#getEditorProviders()} , in order to postpone
	 * the computation of the editor providers as much as possible
	 * 
	 * @param composition
	 */
	void doComputeEditors(IEditorComposition<?> composition);

}

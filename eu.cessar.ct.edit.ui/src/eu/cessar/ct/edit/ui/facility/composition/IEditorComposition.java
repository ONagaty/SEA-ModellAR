package eu.cessar.ct.edit.ui.facility.composition;

import java.util.List;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;

/**
 * 
 * Entity which groups {@link IModelFragmentEditorProvider}s. There are 3 types
 * of compositions: simple, system and ECUC.
 * 
 * @author uidl6870
 * 
 * @param <T>
 *        category, see {@link ICompositionCategory}
 */
public interface IEditorComposition<T extends ICompositionCategory<?>>
{
	/**
	 * Returns the list of editor providers that belong to the composition,
	 * never <code>null</code>
	 * 
	 * @return
	 */
	public List<IModelFragmentEditorProvider> getEditorProviders();

	/**
	 * Called by the composition provider, must NOT be called by consumers
	 * 
	 * @param provider
	 */
	public void addEditorProvider(IModelFragmentEditorProvider provider);

	/**
	 * Return the type of composition
	 * 
	 * @return
	 */
	public ECompositionType getType();

	/**
	 * Returns the category, by which the composition is defined
	 * 
	 * @return
	 */
	public T getCategory();

	/**
	 * Sets the category for the composition Called by the composition provider,
	 * must NOT be called by consumers
	 * 
	 * @param category
	 */
	public void setCategory(T category);
}

/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 2, 2010 7:14:54 PM </copyright>
 */
package eu.cessar.ct.edit.ui;

import java.util.List;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.edit.ui.facility.EEditorCategory;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;
import eu.cessar.ct.edit.ui.facility.composition.ECompositionType;
import eu.cessar.ct.edit.ui.facility.composition.IEcucCompostion;
import eu.cessar.ct.edit.ui.facility.composition.IEditorComposition;
import eu.cessar.ct.edit.ui.facility.composition.ISimpleComposition;
import eu.cessar.ct.edit.ui.facility.composition.ISystemCompostion;
import eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansion;
import eu.cessar.ct.edit.ui.facility.preferences.ICacheEditingPreference;
import eu.cessar.ct.edit.ui.facility.preferences.IEditingPreferences;
import eu.cessar.ct.edit.ui.internal.facility.EditingFacilityImpl;
import eu.cessar.ct.edit.ui.newobjfactories.INewEObjectComposition;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GParamConfContainerDef;

/**
 * @author uidl6458
 */
public interface IEditingFacility
{

	public static final IEditingFacility eINSTANCE = new EditingFacilityImpl();

	public static final int EDITOR_CLASSIFIER = 1;
	public static final int EDITOR_FEATURE = 2;
	public static final int EDITOR_CLASS = 4;
	public static final int EDITOR_ALL = EDITOR_CLASSIFIER | EDITOR_FEATURE | EDITOR_CLASS;

	/**
	 * Returns the expansion for the given <code>editor</code>
	 *
	 * @param editor
	 * @return the expansion or <code>null</code>, if the editor does not have an expansion
	 */
	public IModelFragmentEditorExpansion getEditorExpansion(IModelFragmentEditor editor);

	/**
	 * Returns a list of editor providers for a container having <code>containerDef</code> as definition.
	 *
	 * @param containerDef
	 * @return
	 */
	public List<IModelFragmentEditorProvider> getEditorProvidersForDefinition(GParamConfContainerDef containerDef);

	/**
	 * Returns the editor providers for <code>object</code>
	 *
	 * @param object
	 * @return
	 */
	public List<IModelFragmentEditorProvider> getSimpleEditorProviders(EObject object);

	/**
	 *
	 * @param eClass
	 * @return
	 */
	public List<IModelFragmentEditorProvider> getSimpleEditorsProviders(EClass eClass);

	/**
	 *
	 * @param object
	 * @param eClass
	 * @param features
	 * @param editorTypes
	 * @return
	 */
	public List<IModelFragmentEditorProvider> getSimpleEditorsProviders(EObject object, EClass eClass,
		List<EStructuralFeature> features, int editorTypes);

	/**
	 *
	 * @param eClass
	 * @param features
	 * @param editorTypes
	 * @return
	 */
	public List<IModelFragmentEditorProvider> getSimpleEditorProviders(EClass eClass,
		List<EStructuralFeature> features, int editorTypes);

	/**
	 * Returns the list of simple compositions for the given <code>object</code> . One such composition groups the
	 * editors from the same {@link EEditorCategory}
	 *
	 * @param object
	 * @return
	 */
	public List<ISimpleComposition> getAllSimpleEditorCompositions(EObject object);

	/**
	 *
	 * @param eClass
	 * @return
	 */
	public List<ISimpleComposition> getAllSimpleEditorCompositions(EClass eClass);

	/**
	 * Returns a composition made up from editors that belong to the <code>category</code>, for the given
	 * <code>object</code>
	 *
	 * @param object
	 * @param category
	 * @return
	 */
	public ISimpleComposition getSimpleEditorComposition(EObject object, EEditorCategory category);

	/**
	 *
	 * @param eClass
	 * @param category
	 * @return
	 */
	public ISimpleComposition getSimpleEditorComposition(EClass eClass, EEditorCategory category);

	/**
	 * @param object
	 * @return
	 */
	// public Map<EEditorCategory, ISimpleComposition>
	// getAllSimpleEditorCompositions(EObject object);

	/**
	 * Returns a list with all editor compositions for the given <code>object</code>. <br>
	 *
	 * @param object
	 *        EObject for which the editor compositions are requested
	 * @return
	 */
	public List<IEditorComposition> getAllEditorCompositions(EObject object);

	/**
	 *
	 * @param eClass
	 * @return
	 */
	public List<IEditorComposition> getAllEditorCompositions(EClass eClass);

	/**
	 * Returns a list of compositions of the specified <code>type</code>. <br>
	 * <li>
	 * The call {@link #getEditorCompositions(EObject, ECompositionType.SIMPLE)} is equivalent to
	 * {@link #getSimpleEditorProviders(EObject)}.</li><br>
	 * <li>The call {@link #getEditorCompositions(EObject, ECompositionType.ECUC)} is suitable for an EObject of ECUC
	 * type ({@link GContainer} or {@link GModuleConfiguration}); it will return a list with {@link IEcucCompostion}s,
	 * one such composition grouping editors for each child container corresponding to the <code>object</code>
	 * {@link GContainer} or {@link GModuleConfiguration}</li> <br>
	 * <li>The call {@link #getEditorCompositions(EObject, ECompositionType.SYSTEM)} returns a list with
	 * {@link ISystemCompostion} compositions, one such composition grouping editors for a "child" of the
	 * <code>object</code>. "Children" are obtained from the <code>object</code>'s containment references</li>
	 *
	 * @param <T>
	 *        the concrete type of the composition, one of {@link ISimpleComposition}, {@link ISystemCompostion} or
	 *        {@link IEcucCompostion}
	 * @param object
	 *        the EObject for which the editor compositions are requested
	 * @param type
	 *        the desired composition type
	 * @return
	 */
	public <T extends IEditorComposition> List<T> getEditorCompositions(EObject object, ECompositionType type);

	/**
	 *
	 * @param eClass
	 * @param type
	 * @return
	 */
	public <T extends IEditorComposition> List<T> getEditorCompositions(EClass eClass, ECompositionType type);

	/**
	 *
	 * @param tabId
	 * @return
	 */
	public String getCategoryFromTabId(String tabId);

	/**
	 * @param autosarRelease
	 * @return
	 */
	@Deprecated
	public IEditingPreferences getDefaultEditingPreferences(AutosarReleaseDescriptor autosarRelease);

	/**
	 * @param autosarRelease
	 * @param namespace
	 * @return the default {@link IEditingPreferences}
	 */
	public IEditingPreferences getDefaultEditingPreferences(AutosarReleaseDescriptor autosarRelease, String namespace);

	/**
	 * Use {@link #getWorkspaceEditingPreferences(AutosarReleaseDescriptor, String)} instead.
	 *
	 * @param autosarRelease
	 * @return the {@link IEditingPreferences} at workspace level
	 */
	@Deprecated
	public IEditingPreferences getWorkspaceEditingPreferences(AutosarReleaseDescriptor autosarRelease);

	/**
	 * @param autosarRelease
	 * @param namespace
	 *
	 * @return the {@link IEditingPreferences} at workspace level
	 */
	public IEditingPreferences getWorkspaceEditingPreferences(AutosarReleaseDescriptor autosarRelease, String namespace);

	/**
	 * Use {@link #getProjectEditingPreferences(IProject, String)} instead.
	 *
	 * @param project
	 * @return the {@link IEditingPreferences} at project level
	 */
	@Deprecated
	public IEditingPreferences getProjectEditingPreferences(IProject project);

	/**
	 * @param project
	 * @param projNamespace
	 * @param wsNamespace
	 *
	 * @return the {@link IEditingPreferences} at project level
	 */
	public IEditingPreferences getProjectEditingPreferences(IProject project, String projNamespace, String wsNamespace);

	/**
	 * This preference will serialize only on commit
	 *
	 * @param parentPref
	 * @return the {ICacheEditingPreference} instance
	 */
	public ICacheEditingPreference getCacheEditingPreference(IEditingPreferences parentPref);

	/**
	 * Lookup and return the composition capable for handling object of type children. If there is no factory that can
	 * create a composition, null will be returned
	 *
	 * @param owner
	 * @param feature
	 * @param children
	 * @return a composition for the UI, could return null.
	 */
	public INewEObjectComposition getNewEObjectUIFactory(EObject owner, EStructuralFeature feature, EObject children);

}

/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 11.04.2013 10:04:34
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.newobjfactories;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * An instance of this class will be created by the {@link INewEObjectUIFactory} and is responsible to create the UI for
 * a new EMF Object.</p> The life cycle of an INewEObjectComposition is as follow:
 * <ol>
 * <li>A new instance is obtained by the caller by calling the
 * {@link INewEObjectUIFactory#accept(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature, org.eclipse.emf.ecore.EObject)
 * IEMFUIFactory.accept} method. The returned class will have already the owner, feature and newChild stored in some
 * private data members. Note that the
 * <li>The {@link #init(IUIFeedback) init} method shall be called by the caller and should provide an implementation of
 * {@link IUIFeedback}
 * <li>execute {@link #createControls(Composite)} method that will create the necessary UI into the parent
 * <li>The caller should then enter the read & dispatch messages cycle. Usually that will mean to open the dialog where
 * the UI is created or a similar method necessary to wait for the user to fill the UI with data.
 * <li>During editing, the listeners that will be attached to the widgets created by the
 * {@link #createControls(Composite)} method could call various methods from the {@link IUIFeedback} method to signal
 * the state of the UI.
 * <li>At the end of the editing the caller shall call either {@link #finish()} or {@link #cancel()}.
 * <li>Last, {@link #dispose()} will be called.
 * </ol>
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Tue Apr 16 19:14:58 2013 %
 * 
 *         %version: 1 %
 */
public interface INewEObjectComposition
{

	/**
	 * Return the owner as it was offered by the {@link INewEObjectUIFactory}
	 * 
	 * @return the owner, never null
	 */
	public EObject getOwner();

	/**
	 * Return the feature as it was offered by the {@link INewEObjectUIFactory}
	 * 
	 * @return the feature, never null
	 */
	public EStructuralFeature getChildrenFeature();

	/**
	 * Return the children as it was offered by the {@link INewEObjectUIFactory}
	 * 
	 * @return the children, never null
	 */
	public EObject getChildren();

	/**
	 * @param feedback
	 */
	public void init(IUIFeedback feedback);

	/**
	 * Create a {@link Control} into the provided {@link Composite} showing the necessary UI to create a new EMF object
	 * 
	 * @param parent
	 */
	public void createControls(Composite parent);

	/**
	 * The user accepted the current information and he requested to finish the UI. Any data that still have to be taken
	 * from UI widgets and put into the children have to be performed at this point.
	 * <p/>
	 * If, in addition to changes to the children, objects or attributes needed to be created in locations that does not
	 * have the children as they parent the necessary commands shall be returned.
	 * <p/>
	 * If for whatever reasons, the operations cannot be performed the methoud could still use the {@link IUIFeedback}
	 * methods to notify and stop the finish process but it shall not throw errors.
	 * 
	 * @return a list of additional commands, could be null or empty
	 */
	public List<Command> finish();

	/**
	 * The user requested to cancel the UI.
	 */
	public void cancel();

	/**
	 * The UI is disposed. Resource deallocation or listener removal should go here. There is no need to dispose the ui
	 * itself, this will be done anyway when the parent received by the {@link #createControls(Composite)} method will
	 * be disposed.
	 */
	public void dispose();

}

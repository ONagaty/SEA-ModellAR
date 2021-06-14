/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jun 17, 2010 11:01:10 AM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.actions;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.utils.EditUtils;
import eu.cessar.req.Requirement;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * @author uidl6458
 * 
 */
@Requirement(
	reqID = "REQ_EDIT_PROP#GOTO#1")
public class GoToReferenceContributionItem extends ContributionItem
{

	private final IModelFragmentFeatureEditor editor;

	public GoToReferenceContributionItem(IModelFragmentFeatureEditor editor)
	{
		this.editor = editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.ContributionItem#isEnabled()
	 */
	@Override
	public boolean isEnabled()
	{
		// return true if there is something set into the editor
		EObject input = editor.getInput();
		EStructuralFeature feature = editor.getInputFeature();
		if (input == null || feature == null)
		{
			return false;
		}
		return input.eIsSet(feature);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.ContributionItem#fill(org.eclipse.swt.widgets.Menu, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void fill(Menu menu, int index)
	{
		EObject input = editor.getInput();
		EStructuralFeature feature = editor.getInputFeature();
		boolean isMany = false;
		EList<EObject> refered = null;
		if (input == null || feature == null)
		{
			isMany = true;
			refered = ECollections.emptyEList();
		}
		else
		{
			isMany = feature.isMany();
			Object value = input.eGet(feature);
			if (isMany)
			{
				refered = (EList<EObject>) value;
			}
			else
			{
				refered = new BasicEList<EObject>();
				if (value != null && !((EObject) value).eIsProxy())
				{
					refered.add((EObject) value);
				}
			}
		}
		if (isMany)
		{
			MenuItem cascadeMenu = new MenuItem(menu, SWT.CASCADE);
			cascadeMenu.setText("Go to"); //$NON-NLS-1$
			cascadeMenu.setEnabled(refered.size() > 0);

			Menu subMenu = new Menu(menu);
			cascadeMenu.setMenu(subMenu);
			menu = subMenu; // SUPPRESS CHECKSTYLE OK here
		}
		constructMenu(menu, feature, isMany, refered);
	}

	private void constructMenu(Menu menu, EStructuralFeature feature, boolean isMany, EList<EObject> refered)
	{
		for (int i = 0; i < refered.size(); i++)
		{
			final EObject eObject = refered.get(i);
			if (!eObject.eIsProxy())
			{
				MenuItem goToMenu = new MenuItem(menu, SWT.PUSH);
				String name = null;
				if (eObject instanceof GIdentifiable)
				{
					name = ((GIdentifiable) eObject).gGetShortName();
				}
				if (name == null)
				{
					// use the index and the name of the feature from where is
					// refered
					if (isMany)
					{
						name = String.valueOf(i) + " - "; //$NON-NLS-1$
					}
					else
					{
						name = ""; //$NON-NLS-1$
					}
					name = name + feature.getName();
				}
				if (!isMany)
				{
					name = "GoTo " + name; //$NON-NLS-1$
				}
				goToMenu.setText(name);
				goToMenu.setEnabled(!eObject.eIsProxy());
				goToMenu.addSelectionListener(new SelectionAdapter()
				{
					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
					 */
					@Override
					public void widgetSelected(SelectionEvent e)
					{
						EditUtils.openEditor(eObject, true);
					}
				});
			}
		}
	}
}

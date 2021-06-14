/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Nov 30, 2011 12:59:08 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ArmEvent;
import org.eclipse.swt.events.ArmListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;

import eu.cessar.ct.edit.ui.internal.facility.editors.SystemIRefEditor;
import eu.cessar.ct.edit.ui.utils.EditUtils;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.req.Requirement;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * @author uidt2045
 * 
 */
@Requirement(
	reqID = "REQ_EDIT_PROP#3")
public class GoToInstanceRefContributionItem extends ContributionItem
{

	private final SystemIRefEditor editor;

	/**
	 * Instantiates a new go to instance ref contribution item.
	 * 
	 * @param editor
	 *        the editor
	 */
	public GoToInstanceRefContributionItem(SystemIRefEditor editor)
	{
		this.editor = editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.ContributionItem#fill(org.eclipse.swt.widgets.Menu, int)
	 */
	@Override
	public void fill(Menu menu, int index)
	{
		if (editor.getInput() == null)
		{
			return;
		}

		List<EObject> targets = new ArrayList<EObject>();
		List<EObject> contexts = new ArrayList<EObject>();

		contexts = getContextObjects();
		EObject targetFeature = editor.getTargetElement();
		if (targetFeature != null)
		{
			targets.add(targetFeature);
			fillReferences(menu, targets, "target"); //$NON-NLS-1$
			fillReferences(menu, contexts, "context"); //$NON-NLS-1$
		}
	}

	/**
	 * @param menu
	 * @param values
	 * @param string
	 */
	private void fillReferences(Menu menu, List<EObject> values, String suffix)
	{
		if (values == null)
		{
			return;
		}

		if (values.size() > 1)
		{
			MenuItem cascadeMenu = new MenuItem(menu, SWT.CASCADE);
			cascadeMenu.setText("Go to " + suffix); //$NON-NLS-1$

			Menu subMenu = new Menu(menu);
			cascadeMenu.setMenu(subMenu);
			menu = subMenu; // SUPPRESS CHECKSTYLE OK here
		}

		for (int i = 0; i < values.size(); i++)
		{
			final GIdentifiable systemReference = (GIdentifiable) values.get(i);

			String name = null;

			name = systemReference.gIsSetShortName() ? systemReference.gGetShortName()
				: ModelUtils.getAbsoluteQualifiedName(systemReference);

			if (name == null)
			{
				name = "<<Unnamed>>"; //$NON-NLS-1$
			}

			if (!name.isEmpty())
			{
				if (values.size() == 1)
				{
					name = "Goto " + suffix + " - " + name; //$NON-NLS-1$//$NON-NLS-2$
				}

				MenuItem goToMenu = new MenuItem(menu, SWT.PUSH);
				goToMenu.setText(name);
				goToMenu.setEnabled(!systemReference.eIsProxy());
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
						EditUtils.openEditor(systemReference, true);
					}
				});

				final int toolTipCoordX = 0;
				final int toolTipCoordY = menu.getShell().getBounds().height;

				final Shell menuShell = goToMenu.getDisplay().getActiveShell();

				goToMenu.addArmListener(new ArmListener()
				{

					@Override
					public void widgetArmed(ArmEvent e)
					{
						ToolTip tooltipMenuTem = new ToolTip(menuShell, SWT.ICON_INFORMATION);
						tooltipMenuTem.setText(ModelUtils.getAbsoluteQualifiedName(systemReference));
						tooltipMenuTem.setVisible(systemReference.gIsSetShortName());
						tooltipMenuTem.setLocation(new Point(toolTipCoordX, toolTipCoordY));
					}
				});
			}
		}
	}

	/**
	 * @return
	 */
	private List<EObject> getContextObjects()
	{
		List<EReference> contextFeatures = editor.getContextFeatures();
		EObject input = editor.getInput();
		List<EObject> result = new ArrayList<EObject>();
		for (EReference eReference: contextFeatures)
		{
			if (!eReference.isTransient() && !eReference.isVolatile() && !eReference.isContainment()
				&& eReference.getEOpposite() == null)
			{
				Object object = input.eGet(eReference);
				if (object != null)
				{
					if (object instanceof Collection)
					{
						Collection<?> col = (Collection<?>) object;
						Iterator<?> iterator = col.iterator();
						while (iterator.hasNext())
						{
							result.add((EObject) iterator.next());
						}
					}
					else
					{
						result.add((EObject) object);
					}
				}
			}
		}
		return result;
	}

}

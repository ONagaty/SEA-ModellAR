/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Mar 25, 2010 5:02:45 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.actions;

import java.util.List;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import eu.cessar.ct.core.platform.util.ERadix;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.IRadixChangeListener;
import eu.cessar.ct.edit.ui.facility.RadixChangeEvent;
import eu.cessar.ct.edit.ui.facility.RadixChangeListenerProvider;
import eu.cessar.ct.edit.ui.facility.parts.EEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.IIntegerEditorPart;

/**
 * Contribution item that adds a "Change radix" menu with 3 radio sub-menu items:"Decimal","Hexa","Binary" and changes
 * the integer radix accordingly with the selection.
 * 
 */
public class ChangeIntegerRadixContributionItem extends ContributionItem
{
	protected IModelFragmentEditor editor;

	/**
	 * 
	 * @param editor
	 */
	public ChangeIntegerRadixContributionItem(IModelFragmentEditor editor)
	{
		this.editor = editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled()
	{
		return editor.getPart(EEditorPart.EDITING_AREA) instanceof IIntegerEditorPart;
	}

/*
 * (non-Javadoc)
 * 
 * @see org.eclipse.jface.action.ContributionItem#fill(org.eclipse.swt.widgets.Menu, int)
 */
	@Override
	public void fill(Menu parentMenu, int index)
	{
		ERadix setting = getRadix();

		MenuItem cascadeMenu = new MenuItem(parentMenu, SWT.CASCADE);
		cascadeMenu.setText("Change radix"); //$NON-NLS-1$

		Menu subMenu = new Menu(parentMenu);
		cascadeMenu.setMenu(subMenu);

		for (final ERadix radix: ERadix.values())
		{
			MenuItem menuItem = new MenuItem(subMenu, SWT.RADIO);
			menuItem.setText(radix.getLiteral());
			menuItem.setSelection(radix == setting);
			menuItem.addSelectionListener(new SelectionAdapter()
			{
				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
				 */
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					setRadix(radix);

					// Notify subTable
					List<IRadixChangeListener> listeners = RadixChangeListenerProvider.getInstance().getRadixListeners();
					if (listeners != null)
					{
						for (IRadixChangeListener listener: listeners)
						{
							listener.radixChanged(new RadixChangeEvent(radix));
						}
					}
				}
			});

		}
	}

	/**
	 * Sets the integer radix for the associated editor part.
	 * 
	 * @param radix
	 *        the new radix
	 */
	protected void setRadix(ERadix radix)
	{
		IEditorPart editorPart = getEditorPart();
		((IIntegerEditorPart) editorPart).setRadix(radix);
	}

	/**
	 * 
	 */
	protected ERadix getRadix()
	{
		IEditorPart editorPart = getEditorPart();
		return ((IIntegerEditorPart) editorPart).getRadix();
	}

	private IEditorPart getEditorPart()
	{
		return (IEditorPart) editor.getPart(EEditorPart.EDITING_AREA);
	}
}

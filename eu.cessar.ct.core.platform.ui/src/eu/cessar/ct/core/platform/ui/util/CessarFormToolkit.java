/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 3, 2010 1:05:39 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import eu.cessar.ct.core.platform.ui.widgets.ExtendedCCombo;

/**
 * The CESSAR form toolkit that shall be used to create UI artifacts in order to
 * share a common look
 * 
 * @author uidl6458
 * 
 * @Review uidl6458 - 19.04.2012
 */
public class CessarFormToolkit extends FormToolkit
{

	/**
	 * The default instance
	 */
	public static final CessarFormToolkit DEFAULT = new CessarFormToolkit(
		PlatformUI.getWorkbench().getDisplay());

	/**
	 * These horizontal margin around the composite. Each section should use a
	 * margin of 0, 0.
	 */
	public static final int HMARGIN = 6;

	/**
	 * These horizontal margin around the composite.
	 */
	public static final int VMARGIN = 6;

	/**
	 * Horizontal space to leave between related widgets. Each section should
	 * use these values for spacing its widgets. For example, you can use +/-
	 * HSPACE as the offset of a left or right FlatFormAttachment.
	 * 
	 * The tabbed property composite also inserts VSPACE pixels between section
	 * composites if more than one section is displayed.
	 */
	public static final int HSPACE = 5;

	/**
	 * Horizontal space to leave between related widgets.
	 */
	public static final int VSPACE = 4;

	/**
	 * Space to leave between the center of the property tab and the closest
	 * widget to the left or right. I.e. for a property tab whose widgets are
	 * logically divided into two halves, the total space between the halves
	 * should be 2*CENTER_SPACE.
	 */
	public static final int CENTER_SPACE = 10;

	/**
	 * @param display
	 */
	public CessarFormToolkit(Display display)
	{
		super(display);
	}

	/**
	 * Creates the tab folder as a part of the form.
	 * 
	 * @param parent
	 *        the composite parent.
	 * @param style
	 *        the tab folder style.
	 * @return the tab folder
	 */
	public CTabFolder createTabFolder(Composite parent, int style)
	{
		CTabFolder tabFolder = new CTabFolder(parent, style);
		return tabFolder;
	}

	/**
	 * Creates the tab item as a part of the tab folder.
	 * 
	 * @param tabFolder
	 *        the parent.
	 * @param style
	 *        the tab folder style.
	 * @return the tab item.
	 */
	public CTabItem createTabItem(CTabFolder tabFolder, int style)
	{
		CTabItem tabItem = new CTabItem(tabFolder, style);
		return tabItem;
	}

	/**
	 * Creates the list as a part of the form.
	 * 
	 * @param parent
	 *        the composite parent.
	 * @param style
	 *        the list style.
	 * @return the list.
	 */
	public List createList(Composite parent, int style)
	{
		List list = new org.eclipse.swt.widgets.List(parent, style);
		return list;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.widgets.FormToolkit#createComposite(org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	public Composite createComposite(Composite parent, int style)
	{
		Composite c = super.createComposite(parent, style);
		paintBordersFor(c);
		return c;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.widgets.FormToolkit#createComposite(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Composite createComposite(Composite parent)
	{
		Composite c = createComposite(parent, SWT.NONE);
		return c;
	}

	/**
	 * Creates a plain composite as a part of the form.
	 * 
	 * @param parent
	 *        the composite parent.
	 * @param style
	 *        the composite style.
	 * @return the composite.
	 */
	public Composite createPlainComposite(Composite parent, int style)
	{
		Composite c = super.createComposite(parent, style);
		c.setBackground(parent.getBackground());
		paintBordersFor(c);
		return c;
	}

	/**
	 * Creates a scrolled composite as a part of the form.
	 * 
	 * @param parent
	 *        the composite parent.
	 * @param style
	 *        the composite style.
	 * @return the composite.
	 */
	public ScrolledComposite createScrolledComposite(Composite parent, int style)
	{
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, style);
		return scrolledComposite;
	}

	/**
	 * Creates a combo box as a part of the form.
	 * 
	 * @param parent
	 *        the combo box parent.
	 * @param comboStyle
	 *        the combo box style.
	 * @return the combo box.
	 */
	public ExtendedCCombo createCCombo(Composite parent, int comboStyle)
	{
		ExtendedCCombo combo = new ExtendedCCombo(parent, comboStyle);
		adapt(combo, true, false);
		// Bugzilla 145837 - workaround for no borders on Windows XP
		if (getBorderStyle() == SWT.BORDER)
		{
			combo.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		}
		return combo;
	}

	/**
	 * Creates a combo box as a part of the form.
	 * 
	 * @param parent
	 *        the combo box parent.
	 * @return the combo box.
	 */
	public ExtendedCCombo createCCombo(Composite parent)
	{
		return createCCombo(parent, SWT.FLAT | SWT.READ_ONLY);
	}

	/**
	 * Creates a group as a part of the form.
	 * 
	 * @param parent
	 *        the group parent.
	 * @param text
	 *        the group title.
	 * @return the composite.
	 */
	public Group createGroup(Composite parent, String text)
	{
		Group group = new Group(parent, SWT.SHADOW_NONE);
		group.setText(text);
		group.setBackground(getColors().getBackground());
		group.setForeground(getColors().getForeground());
		return group;
	}

	/**
	 * Creates a flat form composite as a part of the form.
	 * 
	 * @param parent
	 *        the composite parent.
	 * @return the composite.
	 */
	public Composite createFlatFormComposite(Composite parent)
	{
		Composite composite = createComposite(parent);
		FormLayout layout = new FormLayout();
		layout.marginWidth = HSPACE + 2;
		layout.marginHeight = VSPACE;
		layout.spacing = VMARGIN + 1;
		composite.setLayout(layout);
		return composite;
	}

	/**
	 * Creates a label as a part of the form.
	 * 
	 * @param parent
	 *        the label parent.
	 * @param text
	 *        the label text.
	 * @return the label.
	 */
	public CLabel createCLabel(Composite parent, String text)
	{
		return createCLabel(parent, text, SWT.NONE);
	}

	/**
	 * Creates a label as a part of the form.
	 * 
	 * @param parent
	 *        the label parent.
	 * @param text
	 *        the label text.
	 * @param style
	 *        the label style.
	 * @return the label.
	 */
	public CLabel createCLabel(Composite parent, String text, int style)
	{
		final CLabel label = new CLabel(parent, style);
		label.setBackground(parent.getBackground());
		label.setText(text);
		return label;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.widgets.FormToolkit#paintBordersFor(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void paintBordersFor(Composite parent)
	{
		return;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.widgets.FormToolkit#dispose()
	 */
	@Override
	public void dispose()
	{
		if (getColors() != null)
		{
			super.dispose();
		}
	}
}

/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Aug 9, 2010 10:44:02 AM </copyright>
 */
package eu.cessar.ct.core.internal.platform.ui.prefandprop;

/**
 * @author uidt2045
 * 
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import eu.cessar.ct.core.platform.ui.prefandprop.DialogField;

public class LayoutUtil
{

	/**
	 * Calculates the number of columns needed by field editors
	 */
	public static int getNumberOfColumns(DialogField[] editors)
	{
		int nCulumns = 0;
		for (int i = 0; i < editors.length; i++)
		{
			nCulumns = Math.max(editors[i].getNumberOfControls(), nCulumns);
		}
		return nCulumns;
	}

	/**
	 * Creates a composite and fills in the given editors.
	 * 
	 * @param labelOnTop
	 *        Defines if the label of all fields should be on top of the fields
	 */
	public static void doDefaultLayout(Composite parent, DialogField[] editors, boolean labelOnTop)
	{
		doDefaultLayout(parent, editors, labelOnTop, 0, 0);
	}

	/**
	 * Creates a composite and fills in the given editors.
	 * 
	 * @param labelOnTop
	 *        Defines if the label of all fields should be on top of the fields
	 * @param marginWidth
	 *        The margin width to be used by the composite
	 * @param marginHeight
	 *        The margin height to be used by the composite
	 */
	public static void doDefaultLayout(Composite parent, DialogField[] editors, boolean labelOnTop,
		int marginWidth, int marginHeight)
	{
		int nCulumns = getNumberOfColumns(editors);
		Control[][] controls = new Control[editors.length][];
		for (int i = 0; i < editors.length; i++)
		{
			controls[i] = editors[i].doFillIntoGrid(parent, nCulumns);
		}
		if (labelOnTop)
		{
			nCulumns--;
			modifyLabelSpans(controls, nCulumns);
		}
		GridLayout layout = null;
		if (parent.getLayout() instanceof GridLayout)
		{
			layout = (GridLayout) parent.getLayout();
		}
		else
		{
			layout = new GridLayout();
		}
		if (marginWidth != SWT.DEFAULT)
		{
			layout.marginWidth = marginWidth;
		}
		if (marginHeight != SWT.DEFAULT)
		{
			layout.marginHeight = marginHeight;
		}
		layout.numColumns = nCulumns;
		parent.setLayout(layout);
	}

	private static void modifyLabelSpans(Control[][] controls, int nCulumns)
	{
		for (int i = 0; i < controls.length; i++)
		{
			setHorizontalSpan(controls[i][0], nCulumns);
		}
	}

	/**
	 * Sets the span of a control. Assumes that GridData is used.
	 */
	public static void setHorizontalSpan(Control control, int span)
	{
		Object ld = control.getLayoutData();
		if (ld instanceof GridData)
		{
			((GridData) ld).horizontalSpan = span;
		}
		else if (span != 1)
		{
			GridData gd = new GridData();
			gd.horizontalSpan = span;
			control.setLayoutData(gd);
		}
	}

	/**
	 * Sets the width hint of a control. Assumes that GridData is used.
	 */
	public static void setWidthHint(Control control, int widthHint)
	{
		Object ld = control.getLayoutData();
		if (ld instanceof GridData)
		{
			((GridData) ld).widthHint = widthHint;
		}
	}

	/**
	 * Sets the heightHint hint of a control. Assumes that GridData is used.
	 */
	public static void setHeightHint(Control control, int heightHint)
	{
		Object ld = control.getLayoutData();
		if (ld instanceof GridData)
		{
			((GridData) ld).heightHint = heightHint;
		}
	}

	/**
	 * Sets the horizontal indent of a control. Assumes that GridData is used.
	 */
	public static void setHorizontalIndent(Control control, int horizontalIndent)
	{
		Object ld = control.getLayoutData();
		if (ld instanceof GridData)
		{
			((GridData) ld).horizontalIndent = horizontalIndent;
		}
	}

	/**
	 * Sets the horizontal grabbing of a control to true. Assumes that GridData
	 * is used.
	 */
	public static void setHorizontalGrabbing(Control control)
	{
		Object ld = control.getLayoutData();
		if (ld instanceof GridData)
		{
			((GridData) ld).grabExcessHorizontalSpace = true;
		}
	}

	/**
	 * Sets the vertical grabbing of a control to true. Assumes that GridData is
	 * used.
	 * 
	 * @since 3.6
	 */
	public static void setVerticalGrabbing(Control control)
	{
		Object ld = control.getLayoutData();
		if (ld instanceof GridData)
		{
			GridData gd = ((GridData) ld);
			gd.grabExcessVerticalSpace = true;
			gd.verticalAlignment = SWT.FILL;
		}
	}

}

/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uid95856<br/>
 * 10.11.2014 11:41:56
 *
 * </copyright>
 */
package eu.cessar.ct.core.platform.ui.dialogs;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;

/**
 * Filter on a {@linkplain StructuredViewer}.
 *
 * @author uid95856
 *
 *         %created_by: uidu4748 %
 *
 *         %date_created: Thu Jul 2 10:20:44 2015 %
 *
 *         %version: RAUTOSAR~14 %
 */
public class ViewerFiltering
{
	private Text textFilter;

	private Button buttonCaseSensitive;
	private Button buttonAutoFilter;
	private Button buttonHidden;
	private boolean autoFilter = true;

	private StructuredViewer viewer;
	private AbstractRegexViewerFilter filter;

	TableViewer tableViewer;

	/**
	 * @param filter
	 */
	public ViewerFiltering(AbstractRegexViewerFilter filter)
	{
		this.filter = filter;
	}

	/**
	 * Create the filter ui elements - label, textobox, checkbox
	 *
	 * @param parent
	 * @param gridColumnFilterDialog
	 *
	 */
	public void createFilterArea(Composite parent)
	{
		if (parent != null && !parent.isDisposed())
		{
			Label label = new Label(parent, SWT.NONE);
			label.setText(eu.cessar.ct.core.internal.platform.ui.Messages.LabelFilter);

			textFilter = new Text(parent, SWT.BORDER);
			textFilter.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

			buttonCaseSensitive = new Button(parent, SWT.CHECK);
			buttonCaseSensitive.setText(eu.cessar.ct.core.internal.platform.ui.Messages.ButtonCaseSensitive);

			GridData gd = new GridData();
			gd.horizontalSpan = 2;
			buttonCaseSensitive.setLayoutData(gd);

			buttonAutoFilter = new Button(parent, SWT.CHECK);
			buttonAutoFilter.setText(eu.cessar.ct.core.internal.platform.ui.Messages.ButtonAutoFilter);
			buttonAutoFilter.setSelection(autoFilter);
			gd = new GridData();
			gd.horizontalSpan = 2;
			buttonAutoFilter.setLayoutData(gd);

			buttonHidden = new Button(parent, SWT.NONE);
			buttonHidden.setVisible(false); // the button is not visible
			buttonHidden.setEnabled(false);

			gd = new GridData();
			gd.horizontalSpan = 2;
			gd.heightHint = 0;
			buttonHidden.setLayoutData(gd);

			parent.getShell().setDefaultButton(buttonHidden);

			addListeners();

		}
	}

	/**
	 * Checks the size of the input inside the treeViewer in order to set the autoFilter to on or off
	 */
	private void setAutoFilter()
	{
		if (viewer != null)
		{
			Object input = viewer.getInput();
			computeAutoFilterStateIfInputIsList(input);
			if (buttonAutoFilter != null)
			{
				buttonAutoFilter.setSelection(autoFilter);
			}
		}
	}

	private void computeAutoFilterStateIfInputIsList(Object input)
	{
		if (input instanceof List<?>)
		{
			if (!((List<?>) input).isEmpty())
			{
				Object object = ((List<?>) input).get(0);
				if (object == null)
				{
					return;
				}
				IProject project = MetaModelUtils.getProject(object);
				int size = ((List<?>) input).size();
				boolean enablementOfFilterOnKeyPressed = CESSARPreferencesAccessor.getEnablementOfFilterOnKeyPressed(project);
				if (enablementOfFilterOnKeyPressed)
				{
					String filterOnKeyPressedLimitValue = CESSARPreferencesAccessor.getFilterOnKeyPressedLimitValue(project);
					if (size > Integer.valueOf(filterOnKeyPressedLimitValue))
					{
						autoFilter = false;
					}
					else
					{
						autoFilter = true;
					}

				}
			}
		}
	}

	/**
	 * Performs the search and refresh the viewer
	 */
	private void updateTreeViewer()
	{
		viewer.getControl().setRedraw(false);

		viewer.refresh();
		filter.setSearchString(textFilter.getText());
		if (viewer != null)
		{
			viewer.refresh();
			if (viewer instanceof AbstractTreeViewer)
			{
				((AbstractTreeViewer) viewer).expandAll();
			}
		}
		viewer.getControl().setRedraw(true);
	}

	/**
	 * Add the listeners to the ui elements
	 */
	private void addListeners()
	{

		if (textFilter == null || buttonCaseSensitive == null || buttonAutoFilter == null)
		{
			return;
		}

		textFilter.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				if (autoFilter)
				{
					updateTreeViewer();
				}
			}
		});

		textFilter.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				updateTreeViewer();
			}
		});

		buttonCaseSensitive.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				filter.setCaseSensitive(buttonCaseSensitive.getSelection());
				updateTreeViewer();
			}
		});

		buttonAutoFilter.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				autoFilter = buttonAutoFilter.getSelection();
			}
		});
	}

	/**
	 * Sets the viewer and adds the filters
	 *
	 * @param structuredViewer
	 */
	public void setInput(StructuredViewer structuredViewer)
	{
		viewer = structuredViewer;

		if (viewer != null)
		{
			viewer.addFilter(filter);
		}

		setAutoFilter();

	}

	/**
	 * @return the textFilter
	 */
	public Text getTextFilter()
	{
		return textFilter;
	}

}

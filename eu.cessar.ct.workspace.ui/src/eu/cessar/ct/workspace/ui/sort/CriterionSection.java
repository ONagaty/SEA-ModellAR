/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidt2045 Jul 12, 2011 3:42:29 PM </copyright>
 */
package eu.cessar.ct.workspace.ui.sort;

import java.util.List;

import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import eu.cessar.ct.workspace.sort.ESortDirection;
import eu.cessar.ct.workspace.sort.IDirectionalSortCriterion;
import eu.cessar.ct.workspace.sort.ISortCriterion;
import eu.cessar.ct.workspace.sort.ISortTarget;

/**
 * @author uidt2045
 *
 */

public class CriterionSection
{

	private final SelectionListener removeSectionListener;
	private Label lblFeature;
	private ComboViewer comboCriterionViewer;
	private Label lblDirection;
	private Combo comboDirection;
	private Label lblSpacer;
	private Button btnLess;

	private ISortCriterion currentCriterion;
	private ESortDirection currentDirection = ESortDirection.ASCENDING;
	private boolean isFeature = false;

	private class SortCriterionLabelProvider extends LabelProvider
	{
		@Override
		public Image getImage(final Object element)
		{
			Object image = ((ISortCriterion) element).getImage();
			if (image != null)
			{
				return ExtendedImageRegistry.INSTANCE.getImage(image);
			}
			return null;
		}

		@Override
		public String getText(final Object element)
		{
			return ((ISortCriterion) element).getLabel();
		}
	}

	private class SortCriterionContentProvider implements IStructuredContentProvider
	{
		public Object[] getElements(final Object inputElement)
		{
			if (inputElement instanceof ISortTarget)
			{
				return ((ISortTarget) inputElement).getAvailableSortCriteria().toArray();
			}
			else
			{
				return new Object[0];
			}
		}

		public void dispose()
		{
		}

		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
		{
		}
	}

	/**
	 * @param removeSectionListener
	 * @param isFeature
	 */
	public CriterionSection(final SelectionListener removeSectionListener, boolean isFeature)
	{
		this.removeSectionListener = removeSectionListener;
		this.isFeature = isFeature;
	}

	/**
	 * @param parent
	 */
	public void createContents(final Composite parent, final boolean addRemoveButton)
	{

		if (isFeature)
		{
			lblFeature = new Label(parent, SWT.NONE);
			lblFeature.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblFeature.setText("Feature"); //$NON-NLS-1$
		}

		comboCriterionViewer = new ComboViewer(parent, SWT.READ_ONLY);
		Combo combo = comboCriterionViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboCriterionViewer.setLabelProvider(new SortCriterionLabelProvider());
		comboCriterionViewer.setContentProvider(new SortCriterionContentProvider());
		comboCriterionViewer.setSorter(new ViewerSorter());
		comboCriterionViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			public void selectionChanged(final SelectionChangedEvent event)
			{
				currentCriterion = (ISortCriterion) ((IStructuredSelection) event.getSelection()).getFirstElement();
			}

		});

		lblDirection = new Label(parent, SWT.NONE);
		lblDirection.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDirection.setText("Direction:");

		comboDirection = new Combo(parent, SWT.READ_ONLY);
		comboDirection.setItems(new String[] {ESortDirection.ASCENDING.name(), ESortDirection.DESCENDING.name()});
		comboDirection.select(0);
		comboDirection.addSelectionListener(new SelectionAdapter()
		{

			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				currentDirection = ESortDirection.valueOf(comboDirection.getText());
			}

		});

		if (addRemoveButton)
		{
			btnLess = new Button(parent, SWT.NONE);
			btnLess.setText("Less");
			if (removeSectionListener != null)
			{
				btnLess.addSelectionListener(new SelectionAdapter()
				{
					/*
					 * (non-Javadoc)
					 *
					 * @see
					 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
					 */
					@Override
					public void widgetSelected(final SelectionEvent e)
					{
						e.data = CriterionSection.this;
						removeSectionListener.widgetSelected(e);
					}
				});
			}
		}
		else
		{
			lblSpacer = new Label(parent, SWT.NONE);
		}

	}

	/**
	 *
	 */
	public void disposeContents()
	{
		if (lblFeature != null && !lblFeature.isDisposed())
		{
			lblFeature.dispose();
			comboCriterionViewer.getCombo().dispose();
			lblDirection.dispose();
			comboDirection.dispose();
			if (btnLess != null)
			{
				btnLess.dispose();
			}
			else
			{
				lblSpacer.dispose();
			}
		}
	}

	/**
	 * @param sortTarget
	 */
	public void setSortTarget(final ISortTarget sortTarget)
	{
		comboCriterionViewer.setSelection(StructuredSelection.EMPTY);
		comboCriterionViewer.setInput(sortTarget);
		currentCriterion = null;
	}

	public ISortTarget getSortTarget()
	{
		return (ISortTarget) comboCriterionViewer.getInput();
	}

	/**
	 * @return
	 */
	public IDirectionalSortCriterion getSortCriterion()
	{
		if (currentCriterion == null || currentDirection == null)
		{
			return null;
		}
		else
		{
			return currentCriterion.createDirectionalSortCriterion(currentDirection);
		}
	}

	/**
	 * @param defaultCriterion
	 * @return
	 */
	public void setSortCriterion(ISortCriterion defaultCriterion)
	{
		currentCriterion = defaultCriterion;
		ISortTarget target = getSortTarget();
		if (target != null)
		{
			List<ISortCriterion> sortCriteria = target.getAvailableSortCriteria();
			for (ISortCriterion criteria: sortCriteria)
			{
				if (criteria.getLabel().equals(defaultCriterion.getLabel()))
				{
					comboCriterionViewer.setSelection(new StructuredSelection(criteria));
					break;
				}
			}
		}
	}
}

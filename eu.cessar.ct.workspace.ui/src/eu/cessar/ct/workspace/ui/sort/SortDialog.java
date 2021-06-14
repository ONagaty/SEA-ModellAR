/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidt2045 Jul 12, 2011 3:39:53 PM </copyright>
 */
package eu.cessar.ct.workspace.ui.sort;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.dialogs.TitleAreaDialog;
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import eu.cessar.ct.workspace.sort.IDirectionalSortCriterion;
import eu.cessar.ct.workspace.sort.ISortCriterion;
import eu.cessar.ct.workspace.sort.ISortProvider;
import eu.cessar.ct.workspace.sort.ISortTarget;
import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;

/**
 * @author uidt2045
 *
 */

public class SortDialog extends TitleAreaDialog
{
	/** */
	private static final String DO_SORT_GROUPING = "DO_SORT_GROUPING"; //$NON-NLS-1$

	/** */
	protected ISortTarget currentSortTarget;

	private ISortProvider sortProvider;
	private ComboViewer featureComboViewer;

	private Button groupCheckBox;
	private boolean shouldGroup;

	private List<CriterionSection> criteriaSections;

	private boolean populateWithDefault;
	private ISortTarget defaultTarget;
	private ISortCriterion defaultCriterion;

	private class SortTargetLabelProvider extends LabelProvider
	{
		@Override
		public Image getImage(final Object element)
		{
			Object image = ((ISortTarget) element).getImage();
			if (image != null)
			{
				return ExtendedImageRegistry.INSTANCE.getImage(image);
			}
			return null;
		}

		@Override
		public String getText(final Object element)
		{
			return ((ISortTarget) element).getLabel();
		}
	}

	private class SortTargetContentProvider implements IStructuredContentProvider
	{
		public Object[] getElements(final Object inputElement)
		{
			return ((ISortProvider) inputElement).getSortTargets().toArray();
		}

		public void dispose()
		{
			// do nothing
		}

		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
		{
			// do nothing
		}
	}

	private SelectionListener criterionRemoveListener = new SelectionAdapter()
	{
		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		@Override
		public void widgetSelected(final SelectionEvent e)
		{
			removeCriterion((CriterionSection) e.data);
		}
	};
	private Composite cmpSelectHow;

	private boolean isModelEditor;

	private Button ascendingRadio;

	private boolean isAscending;

	private Button descendingRadio;

	private boolean isDescending;

	/**
	 * @return the isAscending
	 */
	public boolean isAscending()
	{
		return isAscending;
	}

	/**
	 * @return the isDecessending
	 */
	public boolean isDecessending()
	{
		return isDescending;
	}

	/**
	 * Create the dialog.
	 *
	 * @param parentShell
	 * @param sortProvider
	 */
	public SortDialog(final Shell parentShell, final ISortProvider sortProvider)
	{
		super(parentShell);
		this.sortProvider = sortProvider;
		setHelpAvailable(false);
		criteriaSections = new ArrayList<CriterionSection>();
	}

	/**
	 * @param parentShell
	 * @param sortProvider
	 * @param populateWithDefault
	 */
	public SortDialog(final Shell parentShell, final ISortProvider sortProvider, boolean populateWithDefault)
	{
		super(parentShell);
		this.sortProvider = sortProvider;
		setHelpAvailable(false);
		criteriaSections = new ArrayList<CriterionSection>();
		isModelEditor = populateWithDefault;
	}

	/**
	 * Create contents of the dialog.
	 *
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(final Composite parent)
	{
		getShell().setText("Sort"); //$NON-NLS-1$

		setTitle("Sort children"); //$NON-NLS-1$
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		if (isModelEditor)
		{
			setMessage("Select how the children of the selected item will be sorted.\r"); //$NON-NLS-1$
			createModelEditorSelect(container);
		}
		else
		{
			setMessage("Select how the children of the selected item will be sorted.\r\nWARNING: This will effectively change the file."); //$NON-NLS-1$
			createGroupSelectWhat(container);
			createGroupSelectHow(container);
			populateDialog();

		}

		return area;
	}

	/**
	 * @param container
	 */
	private void createModelEditorSelect(Composite container)
	{
		Group grpSelectWhat = new Group(container, SWT.NONE);
		grpSelectWhat.setText("Sorting Options"); //$NON-NLS-1$
		grpSelectWhat.setLayout(new GridLayout(2, false));
		grpSelectWhat.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		ascendingRadio = new Button(grpSelectWhat, SWT.RADIO);
		ascendingRadio.setLayoutData(new GridData(SWT.LEFT_TO_RIGHT, SWT.CENTER, true, false, 1, 1));
		ascendingRadio.setText("ASCENDING"); //$NON-NLS-1$
		ascendingRadio.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				super.widgetSelected(e);
				isAscending = ascendingRadio.getSelection();
			}
		});

		descendingRadio = new Button(grpSelectWhat, SWT.RADIO);
		descendingRadio.setLayoutData(new GridData(SWT.LEFT_TO_RIGHT, SWT.CENTER, true, false, 2, 1));
		descendingRadio.setText("DESCENDING"); //$NON-NLS-1$
		isDescending = descendingRadio.getSelection();
		descendingRadio.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				super.widgetSelected(e);
				isDescending = descendingRadio.getSelection();
			}
		});

		groupCheckBox = new Button(grpSelectWhat, SWT.CHECK);
		groupCheckBox.setLayoutData(new GridData(SWT.LEFT_TO_RIGHT, SWT.CENTER, true, false, 2, 1));
		groupCheckBox.setSelection(CessarPluginActivator.getDefault().getDialogSettings().getBoolean(DO_SORT_GROUPING));
		groupCheckBox.setText("If checked, the sorted elements will be grouped(based on Class Type)."); //$NON-NLS-1$
		shouldGroup = groupCheckBox.getSelection();
		groupCheckBox.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				super.widgetSelected(e);
				shouldGroup = groupCheckBox.getSelection();
			}
		});
	}

	/**
	 * @param container
	 *        the parent Composite
	 */
	@SuppressWarnings("nls")
	private void createGroupSelectWhat(Composite container)
	{
		Group grpSelectWhat = new Group(container, SWT.NONE);
		grpSelectWhat.setText("1. Select what to sort");
		grpSelectWhat.setLayout(new GridLayout(2, false));
		grpSelectWhat.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		Label lblFeature = new Label(grpSelectWhat, SWT.NONE);
		lblFeature.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFeature.setText("Children of type:");

		// featureComboViewer = new ComboViewer(grpSelectWhat, SWT.READ_ONLY);

		featureComboViewer = new ComboViewer(grpSelectWhat, SWT.READ_ONLY);

		Combo combo = featureComboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		featureComboViewer.setLabelProvider(new SortTargetLabelProvider());
		featureComboViewer.setContentProvider(new SortTargetContentProvider());
		featureComboViewer.setSorter(new ViewerSorter());
		featureComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.
			 * SelectionChangedEvent)
			 */
			public void selectionChanged(final SelectionChangedEvent event)
			{
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				currentSortTarget = (ISortTarget) selection.getFirstElement();
				for (CriterionSection section: criteriaSections)
				{
					section.setSortTarget(currentSortTarget);
				}
			}
		});
		if (!isModelEditor)
		{
			groupCheckBox = new Button(grpSelectWhat, SWT.CHECK);
			groupCheckBox.setLayoutData(new GridData(SWT.LEFT_TO_RIGHT, SWT.CENTER, true, false, 2, 1));
			groupCheckBox.setSelection(CessarPluginActivator.getDefault().getDialogSettings().getBoolean(
				DO_SORT_GROUPING));
			groupCheckBox.setText("If checked, the sorted elements will be grouped.");
			shouldGroup = groupCheckBox.getSelection();
			groupCheckBox.addSelectionListener(new SelectionAdapter()
			{
				/*
				 * (non-Javadoc)
				 *
				 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
				 */
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					super.widgetSelected(e);
					shouldGroup = groupCheckBox.getSelection();
				}
			});
		}
	}

	/**
	 * @param container
	 *        the parent Composite
	 */
	@SuppressWarnings("nls")
	private void createGroupSelectHow(Composite container)
	{
		Group grpSelectHow = new Group(container, SWT.NONE);
		grpSelectHow.setLayout(new GridLayout(1, false));
		grpSelectHow.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpSelectHow.setText("2. Select how to sort");

		cmpSelectHow = new Composite(grpSelectHow, SWT.NONE);
		GridLayout gl_cmpSelectHow = new GridLayout(5, false);
		gl_cmpSelectHow.marginHeight = 0;
		gl_cmpSelectHow.marginWidth = 0;
		cmpSelectHow.setLayout(gl_cmpSelectHow);
		cmpSelectHow.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		CriterionSection section = new CriterionSection(criterionRemoveListener, true);
		section.createContents(cmpSelectHow, false);
		criteriaSections.add(section);

		Composite cmpAddCriteria = new Composite(grpSelectHow, SWT.NONE);
		cmpAddCriteria.setLayout(new GridLayout(1, false));
		cmpAddCriteria.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnAdd = new Button(cmpAddCriteria, SWT.NONE);
		btnAdd.setText("Add criteria");
		btnAdd.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				addCriterion(true);
			}
		});
	}

	/**
	 * @param data
	 */
	private void removeCriterion(final CriterionSection section)
	{
		section.disposeContents();
		criteriaSections.remove(section);
		resizeVertically(getShell());
	}

	/**
	 * @param addRemoveButton
	 */
	protected void addCriterion(final boolean addRemoveButton)
	{
		CriterionSection section = new CriterionSection(criterionRemoveListener, true);
		section.createContents(cmpSelectHow, addRemoveButton);
		criteriaSections.add(section);
		section.setSortTarget(criteriaSections.get(0).getSortTarget());

		resizeVertically(getShell());
	}

	/**
	 * Causes the <code>control</code> to be resized to its preferred height, its width being preserved.
	 *
	 * @param control
	 */
	private static void resizeVertically(Control control)
	{
		Point ctrlPoint = control.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		control.setSize(control.getSize().x, ctrlPoint.y);
	}

	/**
	 * @return the current sort target
	 */
	public ISortTarget getSortTarget()
	{
		return currentSortTarget;
	}

	/**
	 * @return whether elements should be grouped
	 */
	public boolean getGroupedFlag()
	{
		return shouldGroup;
	}

	/**
	 * @return list with {@link IDirectionalSortCriterion}
	 */
	public List<IDirectionalSortCriterion> getSortCriteria()
	{
		List<IDirectionalSortCriterion> result = new ArrayList<IDirectionalSortCriterion>();
		for (CriterionSection section: criteriaSections)
		{
			IDirectionalSortCriterion criterion = section.getSortCriterion();
			if (criterion != null)
			{
				result.add(criterion);
			}
		}
		return result;
	}

	/**
	 *
	 */
	private void populateDialog()
	{
		featureComboViewer.setInput(sortProvider);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed()
	{
		if (!isModelEditor)
		{

			if (currentSortTarget == null)
			{
				setErrorMessage("Please select a type to be sorted"); //$NON-NLS-1$
				return;
			}
			if (getSortCriteria().size() == 0)
			{
				setErrorMessage("Please select at least one sort criteria"); //$NON-NLS-1$
				return;
			}
			CessarPluginActivator.getDefault().getDialogSettings().put(DO_SORT_GROUPING, groupCheckBox.getSelection());
		}
		super.okPressed();
	}

	@SuppressWarnings("unused")
	private void populateDialogWithDefaults()
	{
		List<ISortTarget> targets = sortProvider.getSortTargets();
		if (defaultTarget == null)
		{
			defaultTarget = targets.get(0);
		}
		for (ISortTarget target: targets)
		{
			if (target.getLabel().equals(defaultTarget.getLabel()))
			{
				featureComboViewer.setSelection(new StructuredSelection(target));
				currentSortTarget = target;
				for (CriterionSection section: criteriaSections)
				{
					section.setSortTarget(currentSortTarget);
				}
			}
		}
		if (defaultCriterion != null)
		{
			CriterionSection criterionSection = criteriaSections.get(0);
			criterionSection.setSortCriterion(defaultCriterion);
		}
	}

	/**
	 * @param defaultTarget
	 */
	public void setDefaultTarget(ISortTarget defaultTarget)
	{
		this.defaultTarget = defaultTarget;
	}

	/**
	 * @param flag
	 */
	public void setPopulateWithDefault(boolean flag)
	{
		populateWithDefault = flag;
	}

	/**
	 * @param criterion
	 */
	public void setDefaultCriterion(ISortCriterion criterion)
	{
		defaultCriterion = criterion;
	}

}

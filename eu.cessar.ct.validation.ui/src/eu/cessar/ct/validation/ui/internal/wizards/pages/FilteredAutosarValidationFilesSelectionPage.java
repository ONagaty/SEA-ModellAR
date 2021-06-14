/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4020<br/>
 * Oct 10, 2014 10:44:20 AM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.internal.wizards.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.ui.filetypesViewer.CheckStateListener;
import eu.cessar.ct.core.platform.ui.filetypesViewer.CheckStateProvider;
import eu.cessar.ct.core.platform.ui.filetypesViewer.FileTypesContentProvider;
import eu.cessar.ct.core.platform.ui.filetypesViewer.FileTypesLabelProvider;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.validation.ui.internal.wizards.FilteredSelectionUtils;
import eu.cessar.ct.validation.ui.internal.wizards.Messages;

/**
 * Page created for selection of files to be validated
 *
 * @author uidg4020
 *
 *         %created_by: uidw6834 %
 *
 *         %date_created: Wed May 27 13:40:10 2015 %
 *
 *         %version: 6 %
 */
public class FilteredAutosarValidationFilesSelectionPage extends WizardPage
{
	/** Selected objects to perform validation for */
	protected List<Object> selectedObjects = new ArrayList<>();
	/** Current selection */
	protected IStructuredSelection selection;
	/** Current project */
	protected IProject project;
	/** Tree viewer for resources */
	protected CheckboxTreeViewer resourceViewer;
	private Button btnDoNotDisplayAgain;
	private List<Object> grayedObjects = new ArrayList<>();

	/**
	 * @param structuredSelection
	 * @param project
	 * @param pageName
	 */
	public FilteredAutosarValidationFilesSelectionPage(IStructuredSelection structuredSelection, IProject project,
		String pageName)
	{
		super(pageName);
		selection = structuredSelection;
		this.project = project;
		setTitle(Messages.filteredAutosarValidationPageTitle);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(1, false);
		container.setLayout(layout);
		container.setLayoutData(new GridData(SWT.FILL, SWT.None, true, false));
		setControl(container);

		// create resource viewer
		resourceViewer = new CheckboxTreeViewer(container);
		resourceViewer.addCheckStateListener(new CheckStateListener(resourceViewer));
		List<IFile> checkedFiles = new ArrayList<>();
		resourceViewer.setCheckStateProvider(new CheckStateProvider(resourceViewer, checkedFiles));

		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		Tree tree = resourceViewer.getTree();
		tree.setLayoutData(gridData);

		List<String> acceptedFileTypes = getAcceptedFileTypes();

		// get the content types for each of the file types
		List<IContentType> contentTypes = PlatformUIUtils.getContentTypesForFilesTypes(acceptedFileTypes);

		// get the allowed file extensions for the given content types
		List<String> allowedExtensions = PlatformUIUtils.getFileExtensionsForContentTypes(contentTypes);

		FileTypesContentProvider contentProvider = new FileTypesContentProvider(allowedExtensions);
		resourceViewer.setContentProvider(contentProvider);
		resourceViewer.setLabelProvider(new FileTypesLabelProvider(false));

		TreeColumn tc = new TreeColumn(tree, SWT.NONE);
		tc.setWidth(450);
		tree.pack();
		resourceViewer.setInput(Collections.singletonList(project));

		resourceViewer.expandAll();
		// initially check elements
		checkElements();

		createButtonsComposite(container);
		btnDoNotDisplayAgain = new Button(container, SWT.CHECK);
		btnDoNotDisplayAgain.setLayoutData(new GridData());
		btnDoNotDisplayAgain.setText(Messages.hideWizardText);

	}

	/**
	 * @param container
	 */
	private void createButtonsComposite(Composite container)
	{
		Composite buttonsContainer = new Composite(container, SWT.NULL);
		GridLayout buttonLayout = new GridLayout(4, false);
		buttonsContainer.setLayout(buttonLayout);
		GridData buttonGridData = new GridData(SWT.LEFT, SWT.None, true, false);
		buttonsContainer.setLayoutData(buttonGridData);

		createChangeStateButton(buttonsContainer, true, Messages.selectAllButtonText);
		createChangeStateButton(buttonsContainer, false, Messages.deselectAllButtonText);
	}

	private void createChangeStateButton(Composite buttonsContainer, boolean state, String buttonText)
	{
		final Button selectAllButton = new Button(buttonsContainer, SWT.NONE);
		selectAllButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
		selectAllButton.setText(buttonText);
		selectAllButton.addSelectionListener(createTreeStateListener(state));
	}

	/**
	 * Create a listener to set all nodes from the {@code resourceViewer} to the given state
	 *
	 * @param state
	 * @return
	 */
	private SelectionAdapter createTreeStateListener(final boolean state)
	{

		SelectionAdapter adapter = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				CheckboxTreeViewer viewer = resourceViewer;
				viewer.setSubtreeChecked(project, state);

			}
		};
		return adapter;
	}

	/**
	 * Set all nodes from the {@code resourceViewer} to the appropriate state. If history available, state from history
	 * is restored. If not, all elements are checked
	 */
	private void checkElements()
	{
		List<Object> selectionsHistory = FilteredSelectionUtils.getSelectionsHistory(project);
		List<Object> grayedElements = FilteredSelectionUtils.getGrayedElements(project);
		Object[] grayedObjectsVector = transformToVector(grayedElements);
		List<Object> allSelectionsList = new ArrayList<>();
		// if no selections history, all files are selected
		if (selectionsHistory == null)
		{
			resourceViewer.setSubtreeChecked(project, true);
			return;
		}
		else
		{
			allSelectionsList.addAll(selectionsHistory);
		}
		if (grayedElements != null)
		{
			allSelectionsList.addAll(grayedElements);
		}
		Object[] allSelectionsVector = transformToVector(allSelectionsList);
		if (allSelectionsVector != null)
		{
			resourceViewer.setCheckedElements(allSelectionsVector);
			if (grayedObjectsVector != null)
			{
				resourceViewer.setGrayedElements(grayedObjectsVector);
			}
		}

	}

	/**
	 * @return accepted file types
	 */
	protected List<String> getAcceptedFileTypes()
	{
		AutosarReleaseDescriptor autosarRelease = MetaModelUtils.getAutosarRelease(project);
		return autosarRelease.getContentTypeIds();
	}

	/**
	 * @return selected objects
	 */
	public List<Object> getSelectedObjects()
	{
		if (resourceViewer != null)
		{
			CheckboxTreeViewer tree = resourceViewer;
			Object[] checkedElements = tree.getCheckedElements();
			Object[] grayedElements = tree.getGrayedElements();
			selectedObjects = new ArrayList<>();
			selectedObjects.addAll(Arrays.asList(checkedElements));
			grayedObjects = new ArrayList<>();
			grayedObjects.addAll(Arrays.asList(grayedElements));
			selectedObjects.removeAll(grayedObjects);

		}
		return selectedObjects;
	}

	/**
	 * These method should be called when the wizard finishes
	 */
	public void saveSelectionHistory()
	{
		FilteredSelectionUtils.addSelectedObjects(project, selectedObjects);
		FilteredSelectionUtils.addGrayedObjects(project, grayedObjects);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	@Override
	public boolean canFlipToNextPage()
	{
		return true;
	}

	/**
	 * @return true if wizard should not be displayed in the future
	 */
	public boolean doNotDisplayWizardInTheFuture()
	{
		boolean hide = btnDoNotDisplayAgain.getSelection();
		return hide;
	}

	private Object[] transformToVector(List<Object> selectionsList)
	{
		if (selectionsList == null)
		{
			return null;
		}
		int size = selectionsList.size();
		Object[] selections = new Object[size];
		int index = 0;
		for (Object object: selectionsList)
		{
			selections[index] = object;
			index++;
		}
		return selections;

	}
}

/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 17.08.2012 13:25:44 </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.xmlchecker;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.ui.internal.Messages;
import eu.cessar.ct.workspace.xmlchecker.IXMLCheckerInconsistency;
import eu.cessar.req.Requirement;

/**
 * 
 * Troubleshooting page shown before the final report, where the user is made aware that there are several possible
 * causes of the identified loading/serializing inconsistencies.
 * 
 * @author uidl6870
 * 
 */
@Requirement(
	reqID = "REQ_CHECK#2")
public class TroubleshootingPage extends WizardPage
{

	private final List<IXMLCheckerInconsistency> inconsistencies;

	private boolean nextPressed;

	private static final String[] COLUMN_TITLES = new String[] {"File", "Namespace"}; //$NON-NLS-1$ //$NON-NLS-2$
	private static final int[] COLUMN_WIDTHS = {300, 300};
	private static final int TABLE_HIGH_HINT = 50;

	private static final String CONTACT = "https://workspace1.conti.de/content/00000222/Lists/Announcements/DispForm.aspx?ID=6&Source=https%3A%2F%2Fworkspace1%2Econti%2Ede%2Fcontent%2F00000222%2FLists%2FAnnouncements%2FAllItems%2Easpx"; //$NON-NLS-1$

	private boolean validateWithXsd;

	private final IProject project;

	/**
	 * @param pageName
	 * @param project
	 * @param inconsistencies
	 */
	public TroubleshootingPage(String pageName, IProject project, List<IXMLCheckerInconsistency> inconsistencies)
	{
		super(pageName);
		this.project = project;
		this.inconsistencies = inconsistencies;
		setTitle(pageName);
		setMessage("Troubleshooting methods"); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#getNextPage()
	 */
	@Override
	public IWizardPage getNextPage()
	{
		nextPressed = true;
		return super.getNextPage();
	}

	/**
	 * Check whether next was pressed
	 * 
	 * @return
	 */
	public boolean nextPressed()
	{
		return nextPressed;
	}

	public boolean isValidateWithXsdSelected()
	{
		return validateWithXsd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	@Override
	public boolean isPageComplete()
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(final Composite parent)
	{
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		setControl(container);

		final Map<File, List<IXMLCheckerInconsistency>> fileToInconsistenciesMap = getFileToInconsistenciesMap(inconsistencies);

		Label msg1 = new Label(container, SWT.NONE);
		msg1.setText(NLS.bind(Messages.xmlChecker_troubleshooting_msg_1, fileToInconsistenciesMap.size()));
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		msg1.setLayoutData(gridData);

		Label note = new Label(container, SWT.NONE);
		note.setText(Messages.xmlChecker_wizard_troubleshooting_note);
		setStyle(note, SWT.ITALIC);

		createTableViewer(container, fileToInconsistenciesMap);

		Label msg2 = new Label(container, SWT.NONE);
		msg2.setText(Messages.xmlChecker_troubleshooting_msg_2);

		createTroubleShootingMethod1(container);

		createTroubleShootingMethod2(container);

		createtroubleShootingMethod3(container);

		createContactLink(container);
	}

	/**
	 * @param container
	 */
	private void createtroubleShootingMethod3(Composite container)
	{
		Label hint3 = new Label(container, SWT.NONE);
		hint3.setText(Messages.xmlChecker_wizard_troubleshooting_hint3);
		setStyle(hint3, SWT.BOLD);

		Label detail3 = new Label(container, SWT.NONE);
		detail3.setText(Messages.xmlChecker_wizard_troubleshooting_detail3);
	}

	/**
	 * @param container
	 */
	private void createTroubleShootingMethod1(Composite container)
	{
		String autosarRelease = MetaModelUtils.getAutosarRelease(project).getName();
		Label hint1 = new Label(container, SWT.NONE);
		hint1.setText(NLS.bind(Messages.xmlChecker_wizard_troubleshooting_hint1, new Object[] {autosarRelease}));
		setStyle(hint1, SWT.BOLD);

		Label detail1 = new Label(container, SWT.WRAP);
		detail1.setText(Messages.xmlChecker_wizard_troubleshooting_detail1);

		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		detail1.setLayoutData(gridData);

	}

	/**
	 * @param container
	 */
	private void createTroubleShootingMethod2(Composite container)
	{
		Label hint2 = new Label(container, SWT.NONE);
		hint2.setText(Messages.xmlChecker_wizard_troubleshooting_hint2);
		setStyle(hint2, SWT.BOLD);

		final Button btn = new Button(container, SWT.CHECK);
		btn.setText("Validate against XSD schema"); //$NON-NLS-1$

		final Label lblWarning = new Label(container, SWT.NONE);
		lblWarning.setText("Note: this is a long-running operation that triggers a reload of the project."); //$NON-NLS-1$
		setStyle(lblWarning, SWT.ITALIC);

		Label hint = new Label(container, SWT.NONE);
		hint.setText(Messages.xmlChecker_wizard_troubleshooting_detail2);

		btn.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				validateWithXsd = btn.getSelection();
			}
		});

	}

	/**
	 * @param container
	 */
	private void createContactLink(Composite container)
	{
		Link link = new Link(container, SWT.NONE);

		link.setText(NLS.bind("<a href=\"{0}\">{1}</a>", //$NON-NLS-1$
			new Object[] {CONTACT, " Help Desk for Cessar CT"})); //$NON-NLS-1$
		link.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				try
				{
					// Open default external browser
					PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new URL(e.text));
				}
				catch (PartInitException ex)
				{
					CessarPluginActivator.getDefault().logError(ex);
				}
				catch (MalformedURLException ex)
				{
					CessarPluginActivator.getDefault().logError(ex);
				}
			}
		});

	}

	/**
	 * @param container
	 * @param fileToInconsistenciesMap
	 */
	private void createTableViewer(Composite container, Map<File, List<IXMLCheckerInconsistency>> fileToInconsistenciesMap)
	{
		TableViewer tableViewer = new TableViewer(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
			| SWT.FULL_SELECTION);

		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);

		GridData tableViewerGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tableViewerGridData.heightHint = TABLE_HIGH_HINT;
		tableViewer.getTable().setLayoutData(tableViewerGridData);

		createTableViewerColumns(tableViewer);

		tableViewer.setLabelProvider(new FilesWithInconsistenciesLabelProvider(fileToInconsistenciesMap));
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(fileToInconsistenciesMap.keySet());
	}

	private void createTableViewerColumns(TableViewer tableViewer)
	{
		for (int i = 0; i < COLUMN_TITLES.length; i++)
		{
			TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			TableColumn column = viewerColumn.getColumn();
			column.setText(COLUMN_TITLES[i]);
			column.setWidth(COLUMN_WIDTHS[i]);
			column.setResizable(true);
			column.setMoveable(true);
		}
	}

	private Map<File, List<IXMLCheckerInconsistency>> getFileToInconsistenciesMap(List<IXMLCheckerInconsistency> list)
	{
		Map<File, List<IXMLCheckerInconsistency>> map = new HashMap<File, List<IXMLCheckerInconsistency>>();

		for (IXMLCheckerInconsistency inconsistency: list)
		{
			File file = inconsistency.getDetailFromOriginalFile().getFile();
			if (!map.containsKey(file))
			{
				map.put(file, new ArrayList<IXMLCheckerInconsistency>());
			}
			List<IXMLCheckerInconsistency> l = map.get(file);
			l.add(inconsistency);
		}

		return map;
	}

	private void setStyle(Label label, int style)
	{
		Font font = label.getFont();
		FontData[] fontData = font.getFontData();
		fontData[0].setStyle(style);

		label.setFont(new Font(label.getDisplay(), fontData[0]));
	}
}

package eu.cessar.ct.pluget.debug.ui.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import eu.cessar.ct.pluget.debug.PlugetDebugConstants;
import eu.cessar.ct.runtime.CessarRuntimeUtils;
import eu.cessar.ct.runtime.ui.execution.AbstractCessarLaunchConfigurationTab;

public class SelectPlugetTab extends AbstractCessarLaunchConfigurationTab
{

	private boolean isFileLauncher = true;

	private final static String PLUGET_GROUP_NAME = "&Pluget"; //$NON-NLS-1$
	private final static String SELECT_PLUGET_DIALOG_TITLE = "Pluget selection"; //$NON-NLS-1$
	private final static String SELECT_PLUGET_DIALOG_MSG = "Select a pluget"; //$NON-NLS-1$

	// validation status object for select PLUGET dialog
	private static final IStatus SELECT_PLUGET_DIALOG_ERR_STATUS = new Status(IStatus.ERROR,
		CessarPluginActivator.PLUGIN_ID, "A pluget has to be selected"); //$NON-NLS-1$
	
	private Button fFileRadioButton;
	private Text fSelectFileText;
	private Button fSelectFileButton;
	
	private Button fClassRadioButton;
	private Text fSelectClassText;
	private Button fSelectClassButton;

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchConfigurationTab#getGroupName()
	 */
	@Override
	protected String getGroupName()
	{
		return PLUGET_GROUP_NAME;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchConfigurationTab#getSelectFileDialogErrStatus()
	 */
	@Override
	protected IStatus getSelectFileDialogErrStatus()
	{
		return SELECT_PLUGET_DIALOG_ERR_STATUS;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchConfigurationTab#getSelectFileDialogMsg()
	 */
	@Override
	protected String getSelectFileDialogMsg()
	{
		return SELECT_PLUGET_DIALOG_MSG;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchConfigurationTab#getSelectFileDialogTitle()
	 */
	@Override
	protected String getSelectFileDialogTitle()
	{
		return SELECT_PLUGET_DIALOG_TITLE;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchConfigurationTab#getFileAttribute()
	 */
	@Override
	protected String getFileAttribute()
	{
		return PlugetDebugConstants.PLUGET_DEBUG_PLUGET;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchConfigurationTab#getProjectAttribute()
	 */
	@Override
	protected String getProjectAttribute()
	{
		return PlugetDebugConstants.PLUGET_DEBUG_PROJECT;
	}
	
	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchConfigurationTab#addContentsToFileGroupEditor(org.eclipse.swt.widgets.Group)
	 */
	@Override
	protected void addContentsToFileGroupEditor(Group group) {
		WidgetListener fListener = getListener();
		
		//add controls for selecting a pluget file
		fFileRadioButton = createRadioButton(group, "Pluget file");
		fFileRadioButton.addSelectionListener(fListener);
		fFileRadioButton.setSelection(isFileLauncher);
		
		Composite composite = new Composite(group, SWT.None);
		GridLayout gridLayot = new GridLayout(2, false);
		gridLayot.verticalSpacing = 0;
		composite.setLayout(gridLayot);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gridData);
		fSelectFileText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		fSelectFileText.setLayoutData(gridData);
		fSelectFileText.setFont(composite.getFont());
		fSelectFileText.addModifyListener(fListener);
		fSelectFileButton = createPushButton(composite, getSelectButtonText(), null);
		fSelectFileButton.addSelectionListener(fListener);
		
		//add controls for selecting a class file
		fClassRadioButton = createRadioButton(group, "Pluget class");
		fClassRadioButton.addSelectionListener(fListener);
		fClassRadioButton.setSelection(!isFileLauncher);
		
		composite = new Composite(group, SWT.None);
		gridLayot = new GridLayout(2, false);
		gridLayot.verticalSpacing = 0;
		composite.setLayout(gridLayot);
		GridData gd = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gd);
		fSelectClassText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		fSelectClassText.setLayoutData(gridData);
		fSelectClassText.setFont(composite.getFont());
		fSelectClassText.addModifyListener(fListener);
		fSelectClassButton = createPushButton(composite, getSelectButtonText(), null);
		fSelectClassButton.addSelectionListener(fListener);	
	}
	
	private void updateEnabledState()
	{
		fFileRadioButton.setSelection(isFileLauncher);
		fSelectFileText.setEnabled(isFileLauncher);
		fSelectFileButton.setEnabled(isFileLauncher);
		fClassRadioButton.setSelection(!isFileLauncher);
		fSelectClassText.setEnabled(!isFileLauncher);
		fSelectClassButton.setEnabled(!isFileLauncher);
	}
	
	@Override
	protected String doGetFileEditorText() {
		if(!fSelectFileText.isEnabled() || !isFileLauncher)
		{
			return null;
		}
		return fSelectFileText.getText().trim();
	}
	
	@Override
	protected void doSetFileEditorText(String text) {
		if(text != null)
		{
		fSelectFileText.setText(text);
		}
	}
	
	@Override
	protected void doWidgetSelected(SelectionEvent e) {
		super.doWidgetSelected(e);
		Object source = e.getSource();
		if (source == fSelectFileButton)
		{
			handleFileButtonSelected();
		}
		else if(source == fSelectClassButton)
		{
			handleClassButtonSelected();
		}
		else 
		{
			handleRadioButtonSelected(source);
		}
	}
	/**
	 * @param source
	 */
	private void handleRadioButtonSelected(Object source) {
		if(source == fFileRadioButton)
		{
			fSelectFileText.setEnabled(true);
			fSelectFileButton.setEnabled(true);
			fSelectClassText.setEnabled(false);
			fSelectClassButton.setEnabled(false);
			isFileLauncher = true;
		}
		else if(source == fClassRadioButton)
		{
			fSelectFileText.setEnabled(false);
			fSelectFileButton.setEnabled(false);
			fSelectClassText.setEnabled(true);
			fSelectClassButton.setEnabled(true);
			isFileLauncher = false;
		}
		
	}

	/**
	 * 
	 */
	private void handleClassButtonSelected()
	{
		IType type = chooseClass();
		if(type == null)
		{
			return;
		}
		String className = type.getFullyQualifiedName('.');
		fSelectClassText.setText(className);
		
	}

	/**
	 * @return
	 */
	private IType chooseClass() {
		JavaElementLabelProvider labelProvider = new JavaElementLabelProvider();
		labelProvider.turnOn(JavaElementLabelProvider.SHOW_QUALIFIED);
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(),labelProvider
				);
			dialog.setTitle(SELECT_PLUGET_DIALOG_TITLE);
			dialog.setMessage(SELECT_PLUGET_DIALOG_MSG);
			dialog.setElements(collectPlugetClasses());

			// get the selection
			if (dialog.open() == Window.OK)
			{
				return (IType) dialog.getFirstResult();
			}
			else
			{
				return null;
			}
	}

	/**
	 * @return
	 */
	private IType[] collectPlugetClasses() 
	{
		String projectName = getProjectEditorText();
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		return CessarRuntimeUtils.collectPlugetClasses(project);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchConfigurationTab#initializeFrom(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			isFileLauncher = configuration.getAttribute(PlugetDebugConstants.PLUGET_DEBUG_IS_FILE_LAUNCHER, true);
			updateEnabledState();
			super.initializeFrom(configuration);
			String classAttribute = configuration.getAttribute(PlugetDebugConstants.PLUGET_DEBUG_CLASS, "");
			if(classAttribute != null && !classAttribute.equals("") && fSelectClassText.isEnabled())
			{
				fSelectClassText.setText(classAttribute);
			}
		} catch (CoreException e) {
			CessarPluginActivator.getDefault().logError(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		super.setDefaults(configuration);
		configuration.setAttribute(PlugetDebugConstants.PLUGET_DEBUG_CLASS, "");
	}
	
	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchConfigurationTab#performApply(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		if(fSelectClassText.isEnabled())
		{
			configuration.setAttribute(PlugetDebugConstants.PLUGET_DEBUG_CLASS, fSelectClassText.getText().trim());
			isFileLauncher = false;
		}	
		else
		{
			isFileLauncher = true;
		}
		configuration.setAttribute(PlugetDebugConstants.PLUGET_DEBUG_IS_FILE_LAUNCHER, isFileLauncher);
		super.performApply(configuration);
	}
	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchConfigurationTab#collectFiles()
	 */
	@Override
	protected void collectFiles(List<IFile> files, IProject cessarProject)
	{
		if (files == null)
		{
			files = new ArrayList<IFile>();
		}
		CessarRuntimeUtils.collectPlugetFiles(cessarProject, (List<IFile>) files);
	}
}

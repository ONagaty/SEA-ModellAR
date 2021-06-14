package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.common;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.providers.PackagesTreeContentProvider;
import eu.cessar.ct.ecuc.workspace.ui.internal.providers.PackagesTreeLabelProvider;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.AbstractWizard;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import eu.cessar.ct.sdk.utils.ModelUtils;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (autosar).
 */

public class ModuleInitializationPage extends AbstractWizardPage
{
	private IProject project;

	private TreeViewer treeExistingPackages;

	private PackagesTreeContentProvider contentProvider;

	private PackagesTreeLabelProvider labelProvider;

	private Text txtModuleName;

	private String moduleName;

	private Text txtPackageName;

	private String packageName;

	private GModuleDef moduleDefinition;

	/**
	 * Constructor for ModuleInitializationPage.
	 * 
	 * @param selection
	 */
	public ModuleInitializationPage()
	{
		super("ModuleInitializationPage"); //$NON-NLS-1$
	}

	public ModuleInitializationPage(IStructuredSelection selection)
	{
		this();
		this.selection = selection;
	}

	/**
	 * @return the txtModuleName
	 */
	public Text getTxtModuleName()
	{
		return txtModuleName;
	}

	/**
	 * @see AbstractWizardPage#createControl(Composite)
	 * @see AbstractWizardPage#doCreateControl(Composite)
	 */
	@Override
	protected void doCreateControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(getGridLayout(1, false));
		setControl(container);

		Label ModuleName = new Label(container, SWT.NONE);
		ModuleName.setData(AbstractWizardPage.CONTROL_ID, "ModuleName"); // $NON-NLS$ //$NON-NLS-1$
		ModuleName.setLayoutData(new GridData(SWT.FILL));

		txtModuleName = new Text(container, SWT.BORDER);
		txtModuleName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		txtModuleName.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				moduleName = txtModuleName.getText();
				setPageComplete(isPageComplete());
				getContainer().updateMessage();
			}
		});
		Label ExistingPackagesName = new Label(container, SWT.NONE);
		ExistingPackagesName.setData(AbstractWizardPage.CONTROL_ID, "ExistingPackagesName"); //$NON-NLS-1$
		ExistingPackagesName.setLayoutData(new GridData(SWT.FILL));

		treeExistingPackages = new TreeViewer(container, SWT.BORDER);
		treeExistingPackages.getControl().setLayoutData(
			new GridData(SWT.FILL, SWT.FILL, true, true));
		treeExistingPackages.addSelectionChangedListener(new ISelectionChangedListener()
		{
			public void selectionChanged(SelectionChangedEvent event)
			{
				handlePackageSelection((IStructuredSelection) event.getSelection());
			}
		});
		treeExistingPackages.addDoubleClickListener(new IDoubleClickListener()
		{
			public void doubleClick(DoubleClickEvent event)
			{
				handlePackageSelection((IStructuredSelection) event.getSelection());
				if (isPageComplete())
				{
					getWizard().getContainer().showPage(getNextPage());
				}
			}
		});

		contentProvider = new PackagesTreeContentProvider(project);
		labelProvider = new PackagesTreeLabelProvider();
		treeExistingPackages.setContentProvider(contentProvider);
		treeExistingPackages.setLabelProvider(labelProvider);
		treeExistingPackages.setInput(ResourcesPlugin.getWorkspace().getRoot());

		Tree tree = treeExistingPackages.getTree();
		TreeColumn tc = new TreeColumn(tree, SWT.NONE);
		tc.setWidth(500);

		Label createNewPackage = new Label(container, SWT.NONE);
		createNewPackage.setData(AbstractWizardPage.CONTROL_ID, "createNewPackage"); //$NON-NLS-1$
		createNewPackage.setLayoutData(new GridData(SWT.FILL));

		txtPackageName = new Text(container, SWT.BORDER);
		txtPackageName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		txtPackageName.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				packageName = txtPackageName.getText();
				setPageComplete(isPageComplete());
				getContainer().updateMessage();
			}
		});
		//
		// Label labelDescription = new Label(container, SWT.WRAP);
		// labelDescription.setData(AbstractWizardPage.CONTROL_ID,
		// "labelDescription");
		// GridData gridData = new GridData(SWT.FILL, SWT.NONE, false, true);
		// gridData.widthHint = 300;
		// labelDescription.setLayoutData(gridData);
	}

	protected void handlePackageSelection(IStructuredSelection selection)
	{
		if (null != selection && selection.getFirstElement() instanceof GARPackage)
		{
			setPackageName((GARPackage) selection.getFirstElement());
		}
		else
		{
			setPackageName(null);
		}
		setPageComplete(isPageComplete());
		getContainer().updateMessage();

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	@Override
	public boolean isPageComplete()
	{
		String arPackageName = getPackageName();
		return (null != arPackageName && 0 != arPackageName.length() && null != moduleName && checkForDuplicateModules());
	}

	public boolean checkForDuplicateModules()
	{
		if (txtModuleName.getText().length() == 0 || txtPackageName.getText().length() == 0)
		{
			return false;
		}

		List<EObject> objectsWithQualifiedName = ModelUtils.getEObjectsWithQualifiedName(
			getProject(), txtPackageName.getText() + "/" + txtModuleName.getText()); //$NON-NLS-1$
		if (objectsWithQualifiedName.isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public String getErrorMessage()
	{
		if (null == getPackageName())
		{
			return Messages.ModuleConfigurationInitializationPage_Err_No_Package;
		}
		if (null == getModuleName())
		{
			return Messages.ModuleConfigurationInitializationPage_Err_No_ModuleConfName;
		}

		if (!isPageComplete())
		{
			return Messages.DuplicateQName;
		}

		return super.getErrorMessage();
	}

	@Override
	public void setVisible(final boolean visible)
	{
		if (visible)
		{
			setNextPageDefault(this);
			AbstractWizard wizard = (AbstractWizard) getWizard();
			setProject(wizard.getProject());
			setModuleDefinition(wizard.getModuleDefinition());

			// Update tree of packages
			contentProvider.setProject(getProject());
			treeExistingPackages.refresh();
			treeExistingPackages.expandAll();

			// Update the module name
			txtModuleName.setText(wizard.getDefaultDestinationModuleName());

			// Update the package name
			txtPackageName.setText(wizard.getDefaultDestinationPackageName());
		}
		super.setVisible(visible);
	}

	public IProject getProject()
	{
		return project;
	}

	public void setProject(IProject project)
	{
		this.project = project;
	}

	public GModuleDef getModuleDefinition()
	{
		return moduleDefinition;
	}

	public void setModuleDefinition(GModuleDef moduleDefinition)
	{
		this.moduleDefinition = moduleDefinition;
	}

	public String getModuleName()
	{
		return moduleName;
	}

	public String getPackageName()
	{
		return packageName;
	}

	public void setPackageName(GARPackage selectedPackage)
	{
		this.packageName = (null == selectedPackage ? ((AbstractWizard) getWizard()).getDefaultDestinationPackageName()
			: MetaModelUtils.getAbsoluteQualifiedName(selectedPackage));

		// Update the control reflecting the package name
		txtPackageName.setText(getPackageName());
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage#performDefault()
	 */
	@Override
	public void performDefault()
	{
		AbstractWizard wizard = (AbstractWizard) getWizard();
		txtModuleName.setText(wizard.getDefaultDestinationModuleName());
		txtPackageName.setText(wizard.getDefaultDestinationPackageName());
	}

}

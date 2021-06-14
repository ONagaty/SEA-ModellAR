/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uid95513<br/>
 * Apr 28, 2015 11:26:03 AM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ui.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import eu.cessar.ct.cid.IArtifactParameter;
import eu.cessar.ct.cid.bindings.PlugetBinding;
import eu.cessar.ct.cid.extension.IDatatype;
import eu.cessar.ct.cid.ui.CIDUIRegistry;
import eu.cessar.ct.cid.ui.IDatatypeEditor;
import eu.cessar.req.Requirement;

/**
 * Wizard page created dynamically with all pluget inputs described in the pluget's .cid file.
 *
 * @author uid95513
 *
 *         %created_by: uid95513 %
 *
 *         %date_created: Wed Jun 17 09:56:41 2015 %
 *
 *         %version: 9 %
 */
@Requirement(
	reqID = "47213")
public class PlugetAutomationWizardPage extends WizardPage
{

	private PlugetBinding plugetBinding;
	private List<IArtifactParameter<?>> artifactParameters = new ArrayList<IArtifactParameter<?>>();
	private List<IDatatypeEditor<?>> datatypeEditors = new ArrayList<IDatatypeEditor<?>>();

	/**
	 * @param pageName
	 * @param title
	 * @param titleImage
	 * @param plugetBinding
	 */
	public PlugetAutomationWizardPage(String pageName, String title, ImageDescriptor titleImage,
		PlugetBinding plugetBinding)
	{
		super(pageName, title, titleImage);
		this.plugetBinding = plugetBinding;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent)
	{
		// create a scrolled composite
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		GridLayout layout = new GridLayout(1, false);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		scrolledComposite.setLayout(layout);
		scrolledComposite.setLayoutData(data);

		// create a composite
		Composite composite = new Composite(scrolledComposite, SWT.FILL);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// a control needs to be added on the wizard page corresponding to each parameter described in the .cid
		List<IArtifactParameter<?>> cidArtifactParameters = plugetBinding.getParameters();
		for (IArtifactParameter<?> parameter: cidArtifactParameters)
		{
			// get the editor class configured in plugin.xml as the editor for this parameter type
			IDatatype<?> parameterDatatype = parameter.getType();
			IDatatypeEditor<?> datatypeEditor = CIDUIRegistry.INSTANCE.getEditor(parameterDatatype);

			if (datatypeEditor != null)
			{
				// add the control that corresponds to this input type
				datatypeEditor.createControlForParameter(composite, parameter);

				// register a listener for each selection update
				Listener listener = new Listener()
				{
					public void handleEvent(Event event)
					{
						getContainer().updateButtons();
					}
				};
				datatypeEditor.add(listener);

				// save the parameters and their associated datatypeEditors for further use
				artifactParameters.add(parameter);
				datatypeEditors.add(datatypeEditor);
			}
			else
			{
				// plugin.xml does not specify the class that implements this input type
				CessarPluginActivator.getDefault().logError(Messages.editorNotImplemented + parameterDatatype);
			}

		}

		// set the content of the scrolled composite to the "child" composite
		scrolledComposite.setContent(composite);

		// update the layout of the scrolled composite
		AutomationUtils.updateScrolledCompositeLayout(scrolledComposite);

		setControl(parent);
		parent.setFocus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	@Override
	public boolean isPageComplete()
	{
		Boolean isComplete = true;

		// check if all mandatory parameters have been filled
		int i = 0;
		while (i < artifactParameters.size())
		{
			IArtifactParameter<?> parameter = artifactParameters.get(i);
			if (parameter.isMandatory())
			{
				// get the corresponding editor object
				IDatatypeEditor<?> editor = datatypeEditors.get(i);
				if ((editor.getValue() == null) || (editor.getValue().isEmpty()))
				{
					isComplete = false;
				}
			}
			i++;
		}
		return isComplete;
	}

	/**
	 * @return a list of string arguments for running the pluget
	 */
	public String[] getArguments()
	{
		List<String> v = new ArrayList<String>();
		for (IDatatypeEditor<?> editor: datatypeEditors)
		{

			IArtifactParameter<?> parameterForEditor = editor.getParameterForEditor();

			// add the value taken from the editor
			String value = editor.getValue();
			if ((value != null) && !value.isEmpty())
			{

				// add the pluget input command it exists
				String plugetInputCommand = parameterForEditor.getArtifactInputCommand();
				if (plugetInputCommand != null && plugetInputCommand != AutomationConstants.EMPTY_STRING)
				{
					v.add(plugetInputCommand);
				}
				v.add(value);
			}
		}

		String[] arguments = new String[v.size()];
		v.toArray(arguments);

		return arguments;
	}
}

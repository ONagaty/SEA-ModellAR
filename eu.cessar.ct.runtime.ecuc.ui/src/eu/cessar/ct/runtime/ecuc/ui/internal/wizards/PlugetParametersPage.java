/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidg4020<br/>
 * Mar 3, 2014 3:58:33 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.ui.internal.wizards;

import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

import eu.cessar.ct.runtime.ecuc.ui.internal.menu.PlugetParametersComposite;
import eu.cessar.ct.runtime.ecuc.ui.internal.menu.PlugetParametersComposite.IPlugetParametersCompositeChangedListener;

/**
 * Uses the {@link PlugetParametersComposite} to create a page for selection of the parameters to be used when running a
 * pluget.To obtain the selected parameters the {@code PlugetParametersPage.getIntroducedValues()} method should be used
 * 
 * @author uidg4020
 * 
 *         %created_by: uidg4098 %
 * 
 *         %date_created: Mon Aug 11 10:03:34 2014 %
 * 
 *         %version: RAUTOSAR~4 %
 */
public class PlugetParametersPage extends WizardPage implements IPlugetParametersCompositeChangedListener
{
	private PlugetParametersComposite parametersComposite;
	private boolean controlsEnabled;
	private final String description = "Select a Parameter"; //$NON-NLS-1$
	/**
	 * This flag is enabled if the page will display history for the parameters to run plugets.
	 */
	private boolean isHistory;

	/**
	 * 
	 * @param pageName
	 */
	@SuppressWarnings("nls")
	public PlugetParametersPage(String pageName)
	{
		super(pageName);
		setTitle("Parameters");
		setDescription(description);
		parametersComposite = new PlugetParametersComposite(this);
		setPageComplete(false);
	}

	/**
	 * must be implemented to return the parsed parameters
	 * 
	 * @return parsed parameters
	 */
	public String getIntroducedValues()
	{
		String introducedText = parametersComposite.getIntroducedText();
		return introducedText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent)
	{
		Composite createParametersArea = parametersComposite.createParametersArea(parent);
		setControl(createParametersArea);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.ui.internal.menu.PlugetParametersComposite.IPlugetParametersCompositeChangedListener
	 * #setRunPlugetControlsEnabled(boolean)
	 */
	@Override
	public void setRunPlugetControlsEnabled(boolean isValid)
	{
		controlsEnabled = isValid;
		// Is no need to update buttons if this is a call corresponding to the setHistory call
		if (isHistory)
		{
			isHistory = false;
			return;
		}

		IWizardContainer container = getContainer();
		container.updateButtons();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.ui.internal.menu.PlugetParametersComposite.IPlugetParametersCompositeChangedListener
	 * #setErrorCode(java.lang.String)
	 */
	@Override
	public void setErrorCode(String error)
	{
		// no code needed
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.ui.internal.menu.PlugetParametersComposite.IPlugetParametersCompositeChangedListener
	 * #handleControl()
	 */
	@Override
	public void handleControl()
	{
		getContainer().updateButtons();

	}

/*
 * (non-Javadoc)
 * 
 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
 */
	@Override
	public boolean isPageComplete()
	{

		if (!controlsEnabled)
		{
			return false;
		}
		return true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.ui.internal.menu.PlugetParametersComposite.IPlugetParametersCompositeChangedListener
	 * #setHistory(java.lang.String[])
	 */
	@Override
	public void setHistory(String[] history)
	{ // set the flag to true, as the first call to thesetRunPlugetControlsEnabled() will be a consequence of this
		// method.
		isHistory = true;
		parametersComposite.setHistory(history);
	}

	/**
	 * Adds the name of the pluget to the page description.
	 * 
	 * @param plugetName
	 *        name of the pluget selected in the Pluget Selection Page
	 */
	protected void updateDescription(String plugetName)
	{
		setDescription(description + " for pluget \"" + plugetName + "\""); //$NON-NLS-1$ //$NON-NLS-2$
	}
}

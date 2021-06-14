/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by aurel_avramescu Aug 17, 2010 4:46:45 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;
import org.eclipse.swt.widgets.MessageBox;

import eu.cessar.ct.ecuc.workspace.cleanup.IEcucCleaningAction;
import eu.cessar.ct.ecuc.workspace.cleanup.ModuleConfigurationCleaner;
import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import gautosar.gecucdescription.GModuleConfiguration;

/**
 * @author aurel_avramescu
 * 
 */
public class CleanMultipleAction extends AbstractSyncCleanAction
{

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action)
	{
		try
		{
			if (!isValidSelection)
			{
				MessageBox message = new MessageBox(targetPart.getSite().getShell());
				message.setMessage(Messages.CleanUp_SelectionError);
				message.open();
				return;
			}
			switch (isFileOrProject)
			{
				case 1:
					extractModulesFromFile();
					break;

				case 2:
					extractModulesFromProject();
					break;
			}
			if (configurations == null || configurations.isEmpty())
			{
				MessageBox message = new MessageBox(targetPart.getSite().getShell());
				message.setMessage(Messages.CleanUp_NoModuleConf);
				message.open();
			}
			else
			{
				WorkspaceTransactionUtil.executeInWriteTransaction(
					TransactionUtil.getEditingDomain(EcorePlatformUtil.getResource(file)),

					new Runnable()
					{
						public void run()
						{
							try
							{
								List<IEcucCleaningAction> actions = new ArrayList<IEcucCleaningAction>(
									10);
								for (GModuleConfiguration configuration: configurations)
								{
									ModuleConfigurationCleaner cleaner = new ModuleConfigurationCleaner(
										project, configuration);
									cleaner.performSync();
									if (!actions.containsAll(cleaner.getCleaningActions()))
									{
										actions.addAll(cleaner.getCleaningActions());
									}
								}
								if (actions.isEmpty())
								{
									MessageBox message = new MessageBox(
										targetPart.getSite().getShell());
									message.setMessage(Messages.CleanSyncAction_NoAction);
									message.open();
								}
								else
								{
									CleanerWizard wizard = new CleanerWizard(actions, file,
										Messages.CleanUp_JobName);
									WizardDialog dialog = new WizardDialog(
										targetPart.getSite().getShell(), wizard);
									dialog.create();
									dialog.open();
								}
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					}, Messages.CleanUp_StartJob);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();

		}

	}

}

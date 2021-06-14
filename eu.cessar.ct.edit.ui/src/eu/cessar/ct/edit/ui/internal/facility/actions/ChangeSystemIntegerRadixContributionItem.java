/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 Jul 4, 2011 1:50:14 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.actions;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.platform.util.ERadix;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;

/**
 * @author uidu0944
 * 
 */
public class ChangeSystemIntegerRadixContributionItem extends ChangeIntegerRadixContributionItem
{

	/**
	 * @param editor
	 */
	public ChangeSystemIntegerRadixContributionItem(IModelFragmentFeatureEditor editor)
	{
		super(editor);
		// TODO Auto-generated constructor stub
	}

	private IModelFragmentFeatureEditor getEditor()
	{
		return (IModelFragmentFeatureEditor) editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.internal.facility.actions.ChangeIntegerRadixContributionItem#getRadix()
	 */
	@Override
	protected ERadix getRadix()
	{
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(getEditor().getInputClass());
		if (mmService != null
			&& mmService.canStoreRadixInformation(getEditor().getInput(), (EAttribute) getEditor().getInputFeature()))
		{
			return mmService.getRadix(getEditor().getInput(), (EAttribute) getEditor().getInputFeature(), -1);
		}
		else
		{
			return super.getRadix();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.internal.facility.actions.ChangeIntegerRadixContributionItem#setRadix(eu.cessar.ct.core.
	 * platform.util.ERadix)
	 */
	@Override
	protected void setRadix(ERadix radix)
	{
		final IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(getEditor().getInputClass());
		final ERadix lRadix = radix;
		if (mmService != null
			&& mmService.canStoreRadixInformation(getEditor().getInput(), (EAttribute) getEditor().getInputFeature()))
		{
			updateModel(new Runnable()
			{
				public void run()
				{
					mmService.setRadix(getEditor().getInput(), (EAttribute) getEditor().getInputFeature(), lRadix, -1);
				}
			}, getEditor().getInput(), "Change radix");
		}
		// call super anyway because we need to update the UI
		super.setRadix(radix);
	}

	private void updateModel(Runnable runnable, EObject object, String label)
	{
		try
		{
			WorkspaceTransactionUtil.executeInWriteTransaction(WorkspaceEditingDomainUtil.getEditingDomain(object),
				runnable, label);
		}
		catch (OperationCanceledException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		catch (ExecutionException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

}

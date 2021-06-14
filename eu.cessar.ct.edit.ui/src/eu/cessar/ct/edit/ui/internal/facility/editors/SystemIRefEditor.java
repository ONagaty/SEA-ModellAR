/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jun 9, 2010 1:33:15 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.editors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.action.Action;
import org.eclipse.sphinx.emf.model.IModelDescriptor;
import org.eclipse.sphinx.emf.model.ModelDescriptorRegistry;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;
import org.eclipse.swt.widgets.Event;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.instanceref.IContextType;
import eu.cessar.ct.core.mms.instanceref.ISystemInstanceReferenceHelper;
import eu.cessar.ct.core.mms.instanceref.InstanceReferenceUtils;
import eu.cessar.ct.core.mms.internal.instanceref.InstanceRefConfigurationException;
import eu.cessar.ct.core.platform.util.StringUtils;
import eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentReferenceEditor;
import eu.cessar.ct.edit.ui.facility.InstanceReferenceEditor;
import eu.cessar.ct.edit.ui.facility.parts.IActionPart;
import eu.cessar.ct.edit.ui.facility.parts.ICaptionPart;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.SingleFeatureCaptionPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.SystemIRefEditorPart;
import eu.cessar.ct.edit.ui.instanceref.IInstReferenceLabelProvider;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;
import eu.cessar.ct.edit.ui.internal.facility.actions.GoToInstanceRefContributionItem;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Editor for System instance references.
 * 
 */
/**
 * @author uidl6870
 * 
 */
public class SystemIRefEditor extends AbstractModelFragmentFeatureEditor implements IModelFragmentReferenceEditor,
	InstanceReferenceEditor
{
	private EReference targetFeature;
	private List<EReference> contextFeatures;

	private IInstReferenceLabelProvider lp;
	public final static String TARGET = "target"; //$NON-NLS-1$

	private ISystemInstanceReferenceHelper helper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createEditorPart()
	 */
	@Override
	public IEditorPart createEditorPart()
	{
		return new SystemIRefEditorPart(this);
	}

	public EObject getTargetElement()
	{
		EObject input = getInput();
		Object object = input.eGet(targetFeature);
		return (GIdentifiable) object;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createCaptionPart()
	 */
	@Override
	protected ICaptionPart createCaptionPart()
	{
		String caption = ""; //$NON-NLS-1$
		String featureName = getInputFeature().getName();
		if (featureName != null)
		{
			// remove "target" prefix
			if (featureName.length() != TARGET.length() && featureName.startsWith(TARGET))
			{
				featureName = featureName.substring(TARGET.length());
			}
			caption = StringUtils.toTitleCase(featureName);
		}
		return new SingleFeatureCaptionPart(this, caption);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditor#populateActionPart
	 * (eu.cessar.ct.edit.ui.facility.parts.IActionPart)
	 */
	@Override
	public void populateActionPart(IActionPart part)
	{
		part.getMenuManager().add(new RemoveEditedFeaturesAction(this));
		part.getMenuManager().add(new GoToInstanceRefContributionItem(this));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentReferenceEditor#getCandidates()
	 */
	public List<Object> getCandidates()
	{
		List<Object> candidates = new ArrayList<Object>();
		candidates.addAll(helper.getCandidatesMap().keySet());

		return candidates;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.InstanceReferenceEditor#hasCandidatesForCompleteConfig()
	 */
	public boolean hasCandidatesForCompleteConfig()
	{
		return helper.hasCandidatesForCompleteConfig();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.InstanceReferenceEditor#hasCandidatesForIncompleteConfig()
	 */
	public boolean hasCandidatesForIncompleteConfig()
	{
		return helper.hasCandidatesForIncompleteConfig();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.InstanceReferenceEditor#computeCandidates()
	 */
	public void computeCandidates() throws InstanceRefConfigurationException
	{

		helper.computeCandidates();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.InstanceReferenceEditor#getCandidatesMap()
	 */
	public Map<GIdentifiable, List<List<GIdentifiable>>> getCandidatesMap()
	{
		return helper.getCandidatesMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentReferenceEditor#getLabelProvider()
	 */
	public IInstReferenceLabelProvider getLabelProvider()
	{
		if (lp == null)
		{
			lp = new InstanceRefLabelProvider(this);
		}
		return lp;
	}

	@Override
	public void setInput(EObject object)
	{
		super.setInput(object);
		if (object != null)
		{
			IMetaModelService mmService = getMMService();
			if (targetFeature == null)
			{

				targetFeature = mmService.getTargetFeature(getInputClass());
				contextFeatures = mmService.getContextFeatures(getInputClass());
			}

			helper = InstanceReferenceUtils.getInstanceReferenceHelper(getModelDescriptor());

			try
			{
				helper.init(getInput().eClass());
			}
			catch (InstanceRefConfigurationException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}
	}

	private IMetaModelService getMMService()
	{
		return MMSRegistry.INSTANCE.getMMService(getInput().eClass());
	}

	private IModelDescriptor getModelDescriptor()
	{
		return ModelDescriptorRegistry.INSTANCE.getModel(getInput().eResource());
	}

	/**
	 * 
	 * @return
	 */
	public List<EReference> getContextFeatures()
	{
		return contextFeatures;
	}

	/**
	 * @return
	 */
	public EReference getTargetFeature()
	{
		return targetFeature;
	}

	/**
	 * Action for removing all the features edited by this editor.
	 * 
	 */
	private class RemoveEditedFeaturesAction extends Action
	{
		private final IModelFragmentEditor editor;

		/**
		 * 
		 * @param editor
		 */
		public RemoveEditedFeaturesAction(IModelFragmentEditor editor)
		{
			this.editor = editor;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.action.Action#isEnabled()
		 */
		@Override
		public boolean isEnabled()
		{
			boolean readOnly = !editor.getEditableStatus().isOK();
			if (readOnly)
			{
				return false;
			}
			return editor.getInput().eIsSet(getInputFeature());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.action.Action#getText()
		 */
		@Override
		public String getText()
		{
			return "Remove value"; //$NON-NLS-1$
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.action.Action#runWithEvent(org.eclipse.swt.widgets .Event)
		 */
		@Override
		public void runWithEvent(Event event)
		{
			final EObject input = editor.getInput();
			if (input != null)
			{
				final List<EReference> setFeatures = new ArrayList<EReference>();
				// collect features that are set
				for (final EReference feature: getContextFeatures())
				{
					if (input.eIsSet(feature))
					{
						setFeatures.add(feature);
					}
				}

				if (input.eIsSet(getInputFeature()))
				{
					setFeatures.add((EReference) getInputFeature());
				}

				if (setFeatures.size() > 0)
				{
					remove(new Runnable()
					{
						public void run()
						{
							for (EReference ref: setFeatures)
							{
								input.eUnset(ref);
							}
						}
					}, "Remove context value"); //$NON-NLS-1$

					editor.refresh();
				}
			}
		}

		/**
		 * 
		 * @param runnable
		 * @param label
		 */
		private void remove(Runnable runnable, String label)
		{
			try
			{
				WorkspaceTransactionUtil.executeInWriteTransaction(
					WorkspaceEditingDomainUtil.getEditingDomain(editor.getInput()), runnable, label);
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

	public List<IContextType> getContextTypes()
	{
		return helper.getContextTypes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.InstanceReferenceEditor#getTargetType()
	 */
	public EClass getTargetType()
	{
		return helper.getTargetType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor#dispose()
	 */
	@Override
	public void dispose()
	{
		super.dispose();

		helper.reset();
	}
}

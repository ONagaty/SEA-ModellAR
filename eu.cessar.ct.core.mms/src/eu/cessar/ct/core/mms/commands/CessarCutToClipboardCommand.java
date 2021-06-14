/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidt6343 Mar 16, 2011 12:01:46 PM </copyright>
 */
package eu.cessar.ct.core.mms.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CopyToClipboardCommand;
import org.eclipse.emf.edit.command.CutToClipboardCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.FeatureMapEntryWrapperItemProvider;

import eu.cessar.ct.sdk.utils.ModelUtils;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * A Cessar-specific implementation of {@link CutToClipboardCommand}.
 *
 */
public class CessarCutToClipboardCommand extends CutToClipboardCommand
{

	/**
	 * @param domain
	 * @param command
	 */
	public CessarCutToClipboardCommand(EditingDomain domain, Command command)
	{
		super(domain, command);
	}

	/**
	 * This creates a command to remove an object and set it to the clipboard.
	 */
	public static Command create(EditingDomain domain, Object value)
	{
		if (domain == null)
		{
			return new CessarCutToClipboardCommand(domain, RemoveCommand.create(domain, value));
		}
		else
		{
			CommandParameter parameter = new CommandParameter(null, null, Collections.singleton(value));
			return new CessarCutToClipboardCommand(domain,
				RemoveCommand.create(domain, parameter.getOwner(), parameter.getFeature(), parameter.getCollection()));
		}
	}

	/**
	 * This creates a command to remove a particular value from the specified feature of the owner and set it to the
	 * clipboard.
	 */
	public static Command create(EditingDomain domain, Object owner, Object feature, Object value)
	{
		if (domain == null)
		{
			return new CessarCutToClipboardCommand(domain, RemoveCommand.create(domain, owner, feature, value));
		}
		else
		{
			CommandParameter parameter = new CommandParameter(owner, feature, Collections.singleton(value));
			return new CessarCutToClipboardCommand(domain,
				RemoveCommand.create(domain, parameter.getOwner(), parameter.getFeature(), parameter.getCollection()));
		}
	}

	/**
	 * This creates a command to remove multiple objects and set it to the clipboard.
	 */
	public static Command create(EditingDomain domain, Collection<?> collection)
	{
		if (domain == null)
		{
			return new CessarCutToClipboardCommand(domain, RemoveCommand.create(domain, collection));
		}
		else
		{
			CommandParameter parameter = new CommandParameter(null, null, collection);
			return new CessarCutToClipboardCommand(domain,
				RemoveCommand.create(domain, parameter.getOwner(), parameter.getFeature(), parameter.getCollection()));
		}
	}

	/**
	 * This creates a command to remove a collection of values from the specified feature of the owner and set it to the
	 * clipboard.
	 */
	public static Command create(EditingDomain domain, Object owner, Object feature, Collection<?> collection)
	{
		if (domain == null)
		{
			return new CessarCutToClipboardCommand(domain, RemoveCommand.create(domain, owner, feature, collection));
		}
		else
		{
			CommandParameter parameter = new CommandParameter(owner, feature, collection);
			return new CessarCutToClipboardCommand(domain,
				RemoveCommand.create(domain, parameter.getOwner(), parameter.getFeature(), parameter.getCollection()));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.emf.edit.command.CutToClipboardCommand#execute()
	 */
	@Override
	public void execute()
	{
		// redundant execute & undo is called to avoid SWT exception caused by
		// org.eclipse.sphinx.emf.explorer.ExtendedCommonNavigator$6$2.run(ExtendedCommonNavigator.java:723)
		// - sets field affectedObjects to a non-null value
		// if (command != null)
		// {
		// command.execute();
		// }
		// undo();

		// Added for avoiding nullPointerException on Cut and Undo action.
		super.execute();

		// delegate command to be executed after the paste, add current thread
		// information
		if (domain != null)
		{
			oldClipboard = domain.getClipboard();
			// create a copy command because if working with splittable the copy
			// command creates the entire element from all resources
			Command copy = CopyToClipboardCommand.create(domain, command.getResult());
			if (copy.canExecute())
			{
				copy.execute();
			}
			Collection<?> result = copy.getResult();

			handleReferencedItems();

			domain.setClipboard(new ArrayList<Object>(result));
			CessarPasteFromClipboardCommand.setDelegateCommand(null);
			CessarPasteFromClipboardCommand.saveFeatures(domain.getClipboard(), result);
		}
	}

	/*
	 * If the object is referred, propagate the original object (instead of the copy). The original object is being
	 * picked up from the initial Remove command This fix preserves the existing logic and only affects the references
	 */
	private void handleReferencedItems()
	{
		Object[] resultObjectList = command.getResult().toArray();
		if (resultObjectList != null && resultObjectList.length > 0)
		{
			boolean endCondition = false;
			for (Object object: resultObjectList)
			{
				if (object instanceof EObject)
				{
					handleEObjectReferencedItem((EObject) object);
				}
				else if (object instanceof FeatureMapEntryWrapperItemProvider)
				// the result is an ARpackage/FeatureMapEntryWrapperItemProvider
				{
					endCondition = handleFeatureEntryWrapperItemProviderRefItem(resultObjectList,
						(FeatureMapEntryWrapperItemProvider) object);
				}
				if (endCondition)
				{
					break;
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private boolean handleFeatureEntryWrapperItemProviderRefItem(Object[] resultObjectList,
		FeatureMapEntryWrapperItemProvider itemProvider)
	{
		boolean endCondition = false;

		FeatureMap.Entry entry = (FeatureMap.Entry) itemProvider.getValue();
		Object value = entry.getValue();

		if (value instanceof GIdentifiable)
		{
			if (isReferrenced((EObject) value) || resultObjectList.length > 1)
			{
				CessarPasteFromClipboardCommand.setOriginalClipboard((Collection<Object>) command.getResult());
				CessarPasteFromClipboardCommand.setIsRefferencedFlag(true);
				endCondition = true;
			}
		}
		return endCondition;
	}

	@SuppressWarnings("unchecked")
	private void handleEObjectReferencedItem(EObject eObj)
	{
		Map<EObject, List<EReference>> referencedBy = ModelUtils.getReferencedBy(eObj);

		if (referencedBy != null && !referencedBy.isEmpty())
		{
			CessarPasteFromClipboardCommand.setOriginalClipboard((Collection<Object>) command.getResult());
			CessarPasteFromClipboardCommand.setIsRefferencedFlag(true);
		}
	}

	private boolean isReferrenced(EObject node)
	{
		EList<EObject> eContents = node.eContents();
		for (EObject eObject: eContents)
		{
			Map<EObject, List<EReference>> referencedBy = ModelUtils.getReferencedBy(eObject);
			if (referencedBy != null && !referencedBy.isEmpty())
			{
				return true;
			}
			isReferrenced(eObject);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.emf.common.command.CommandWrapper#getAffectedObjects()
	 */
	@Override
	public Collection<?> getAffectedObjects()
	{
		return getResult();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.emf.edit.command.CutToClipboardCommand#undo()
	 */
	@Override
	public void undo()
	{
		// TODO Auto-generated method stub
		super.undo();
	}
}

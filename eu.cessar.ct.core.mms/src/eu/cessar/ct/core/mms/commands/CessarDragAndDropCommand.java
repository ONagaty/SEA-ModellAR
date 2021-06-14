/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt6343 Mar 16, 2011 5:37:49 PM </copyright>
 */
package eu.cessar.ct.core.mms.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CopyCommand;
import org.eclipse.emf.edit.command.DragAndDropCommand;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.sdk.logging.LoggerFactory;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * A Cessar-specific implementation of {@link DragAndDropCommand}.
 * 
 */
public class CessarDragAndDropCommand extends DragAndDropCommand
{
	private Map<EStructuralFeature, List<Object>> featureMap;

	// private Object dropOwner = null;

	/**
	 * Creates a {@link CessarDragAndDropCommand} using the provided parameters.
	 * 
	 * @param domain
	 *        the {@link EditingDomain} to which the command will be associated
	 * @param owner
	 *        the owner {@link EObject} of the command - the one that triggered the command
	 * @param location
	 *        the relative vertical location of the drag operation, where 0.0 is at the top and 1.0 is at the bottom
	 * @param operations
	 *        a bitwise mask of the DROP_* values.
	 * @param operation
	 *        the desired operation as specified by a DROP_* value
	 * @param collection
	 *        the source objects being dragged
	 */
	public CessarDragAndDropCommand(EditingDomain domain, Object owner, float location, int operations, int operation,
		Collection<?> collection)
	{
		// The location is transformed to either 0.1 (up) or 0.9 (down) to
		// remove the SWT Exception that occurs because the tree editor
		// coordinates are not accurate in comparison to the mouse pointer
		super(domain, owner, location < 0.5f ? 0.1f : 0.9f, operations, operation, collection);
	}

	/**
	 * Creates a {@link CessarDragAndDropCommand} using the provided parameters.
	 * 
	 * @param domain
	 *        the {@link EditingDomain} to which the command will be associated
	 * @param owner
	 *        the owner of the command - the one that triggered the command
	 * @param location
	 *        the relative vertical location of the drag operation, where 0.0 is at the top and 1.0 is at the bottom
	 * @param operations
	 *        a bitwise mask of the DROP_* values.
	 * @param operation
	 *        the desired operation as specified by a DROP_* value
	 * @param collection
	 *        the source objects being dragged
	 * @return the newly created command
	 */
	public CessarDragAndDropCommand(EditingDomain domain, Object owner, float location, int operations, int operation,
		Collection<?> collection, boolean optimize)
	{
		super(domain, owner, location < 0.5f ? 0.1f : 0.9f, operations, operation, collection, optimize);
	}

	/**
	 * This creates a command to perform a drag and drop operation upon the owner. See {@link DragAndDropCommand
	 * DragAndDropCommand} for a description of the arguments.
	 */
	public static Command create(EditingDomain domain, Object owner, float location, int operations, int operation,
		Collection<?> collection)
	{
		return new CessarDragAndDropCommand(domain, owner, location, operations, operation, collection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.edit.command.DragAndDropCommand#prepare()
	 */
	@Override
	protected boolean prepare()
	{
		// reset the drop owner
		return super.prepare();
	}

	/**
	 * This attempts to prepare a drop insert operation.
	 */
	@Override
	protected boolean prepareDropInsert()
	{
		// This will be the default return value.
		//
		boolean result = false;

		// The feedback is set based on which half we are in.
		// If the command isn't executable, these values won't be used.
		//
		feedback = location < 0.5 ? FEEDBACK_INSERT_BEFORE : FEEDBACK_INSERT_AFTER;

		// If we can't determine the parent.
		//
		Object parent = getParent(owner);
		if (parent == null)
		{
			dragCommand = UnexecutableCommand.INSTANCE;
			dropCommand = UnexecutableCommand.INSTANCE;
		}
		else
		{
			// Iterate over the children to find the owner.
			//
			Collection<?> children = getChildren(parent);
			int i = findOwnerLocation(children);

			// Try to create a specific command based on the current desired
			// operation.
			//
			result = prepareDropInsertCommandBasedOnOperation(parent, children, i);

			// If there isn't an executable command we should maybe try a copy
			// operation, but only if we're allowed and not doing a link.
			//
			result = tryDropCopyInsertOperation(result, parent, children, i);

			// If there isn't an executable command we should maybe try a link
			// operation, but only if we're allowed and not doing a link.
			//
			result = tryDropLinkInsertOperation(result, parent, children, i);
		}

		return result;
	}

	/**
	 * Iterate over the children to find the owner.
	 * 
	 * @param children
	 *        the children
	 * @return the owner location in the given Collection
	 */
	private int findOwnerLocation(Collection<?> children)
	{
		int i = 0;

		for (Object child: children)
		{
			// When we match the owner, we're done.
			//
			if (child == owner)
			{
				break;
			}
			++i;
		}

		// If the location indicates after, add one more.
		//
		if (location >= 0.5)
		{
			++i;
		}
		return i;
	}

	private boolean prepareDropInsertCommandBasedOnOperation(Object parent, Collection<?> children, int i)
	{
		boolean result;

		switch (operation)
		{
			case DROP_MOVE:
			{
				result = prepareDropMoveInsert(parent, children, i);
				break;
			}
			case DROP_COPY:
			{
				result = prepareDropCopyInsert(parent, children, i);
				break;
			}
			case DROP_LINK:
			{
				result = prepareDropLinkInsert(parent, children, i);
				break;
			}
			default:
				result = false;
		}
		return result;
	}

	/**
	 * If there isn't an executable command we should maybe try a copy operation, but only if we're allowed and not
	 * doing a link.
	 * 
	 * @param intermediateResult
	 * @param parent
	 * @param children
	 * @param i
	 * @return
	 */
	private boolean tryDropCopyInsertOperation(boolean intermediateResult, Object parent, Collection<?> children, int i)
	{
		boolean result = intermediateResult;

		boolean validCoperation = operation != DROP_COPY && operation != DROP_LINK && (operations & DROP_COPY) != 0;
		if (!intermediateResult && validCoperation)
		{
			// Try again.
			reset();
			result = prepareDropCopyInsert(parent, children, i);

			if (result)
			{
				// We've switch the operation!
				operation = DROP_COPY;
			}
		}
		return result;
	}

	/**
	 * If there isn't an executable command we should maybe try a link operation, but only if we're allowed and not
	 * doing a link.
	 * 
	 * @param intermediateResult
	 * @param parent
	 * @param children
	 * @param i
	 * @return
	 */
	private boolean tryDropLinkInsertOperation(boolean intermediateResult, Object parent, Collection<?> children, int i)
	{
		boolean result = intermediateResult;

		if (!intermediateResult && operation != DROP_LINK && (operations & DROP_LINK) != 0)
		{
			// Try again.
			reset();
			result = prepareDropLinkInsert(parent, children, i);
			if (result)
			{
				// We've switch the operation!
				operation = DROP_LINK;
			}
		}
		return result;
	}

	/**
	 * This attempts to prepare a drop move insert operation.
	 */
	@Override
	protected boolean prepareDropMoveInsert(Object parent, Collection<?> children, int index)
	{
		// We don't want to move insert an object before or after itself...
		//
		if (collection.contains(owner))
		{
			dragCommand = IdentityCommand.INSTANCE;
			dropCommand = UnexecutableCommand.INSTANCE;
		}
		// If the dragged objects share a parent...
		//
		else if (children.containsAll(collection))
		{
			initDragAndDropCommandsIfDraggedObjectsShareAParent(parent, children, index);
		}
		else if (isCrossDomain())
		{
			dragCommand = IdentityCommand.INSTANCE;
			dropCommand = UnexecutableCommand.INSTANCE;
		}
		else
		{
			// Just remove the objects and add them.
			dropCommand = AddCommand.create(domain, parent, null, collection, index);
			if (analyzeForNonContainment(dropCommand))
			{
				dropCommand.dispose();
				dropCommand = UnexecutableCommand.INSTANCE;
				dragCommand = IdentityCommand.INSTANCE;
			}
			else
			{
				dragCommand = RemoveCommand.create(domain, collection);
			}
		}

		boolean result = dragCommand.canExecute() && dropCommand.canExecute();
		return result;
	}

	private void initDragAndDropCommandsIfDraggedObjectsShareAParent(Object parent, Collection<?> children, int index)
	{
		dragCommand = IdentityCommand.INSTANCE;

		// Create move commands for all the objects in the collection.
		//
		CompoundCommand compoundCommand = new CompoundCommand();
		List<Object> before = new ArrayList<Object>();
		List<Object> after = new ArrayList<Object>();

		int j = 0;
		for (Object object: children)
		{
			if (collection.contains(object))
			{
				if (j < index)
				{
					before.add(object);
				}
				else if (j > index)
				{
					after.add(object);
				}
			}
			++j;
		}

		for (Object object: before)
		{
			compoundCommand.append(MoveCommand.create(domain, parent, null, object, index - 1));
		}

		for (ListIterator<Object> objects = after.listIterator(after.size()); objects.hasPrevious();)
		{
			Object object = objects.previous();
			compoundCommand.append(MoveCommand.create(domain, parent, null, object, index));
		}

		if (compoundCommand.getCommandList().size() == 0)
		{
			dropCommand = IdentityCommand.INSTANCE;
		}
		else
		{
			dropCommand = compoundCommand;
		}
	}

	/**
	 * This attempts to prepare a drop copy insert operation.
	 */
	@Override
	protected boolean prepareDropCopyInsert(final Object parent, Collection<?> children, final int index)
	{
		boolean result;

		// We don't want to copy insert an object before or after itself...
		//
		if (collection.contains(owner))
		{
			dragCommand = IdentityCommand.INSTANCE;
			dropCommand = UnexecutableCommand.INSTANCE;
			result = false;
		}
		else
		{
			// Copy the collection
			//
			dragCommand = CopyCommand.create(domain, collection);

			if (optimize)
			{
				result = optimizedCanExecute();
				if (result)
				{
					optimizedDropCommandOwner = parent;
				}
			}
			else
			{
				if (dragCommand.canExecute() && dragCommand.canUndo())
				{
					dragCommand.execute();
					saveFeatures(collection, dragCommand.getResult());
					isDragCommandExecuted = true;

					// And add the copy.
					//
					dropCommand = AddCommand.create(domain, parent, null, dragCommand.getResult(), index);
					if (analyzeForNonContainment(dropCommand))
					{
						dropCommand.dispose();
						dropCommand = UnexecutableCommand.INSTANCE;

						dragCommand.undo();
						dragCommand.dispose();
						isDragCommandExecuted = false;
						dragCommand = IdentityCommand.INSTANCE;
					}
					result = dropCommand.canExecute();
				}
				else
				{
					dropCommand = UnexecutableCommand.INSTANCE;
					result = false;
				}
			} // if optimize
		} // if collection

		return result;
	}

	/**
	 * This attempts to prepare a drop link insert operation.
	 */
	@Override
	protected boolean prepareDropLinkInsert(Object parent, Collection<?> children, int index)
	{
		boolean result;

		// We don't want to insert an object before or after itself...
		//
		if (collection.contains(owner))
		{
			dragCommand = IdentityCommand.INSTANCE;
			dropCommand = UnexecutableCommand.INSTANCE;
			result = false;
		}
		else
		{
			dragCommand = IdentityCommand.INSTANCE;

			// Add the collection
			//
			dropCommand = AddCommand.create(domain, parent, null, collection, index);
			if (!analyzeForNonContainment(dropCommand))
			{
				dropCommand.dispose();
				dropCommand = UnexecutableCommand.INSTANCE;
			}
			result = dropCommand.canExecute();
		}

		return result;
	}

	/**
	 * This attempts to prepare a drop on operation.
	 */
	@Override
	protected boolean prepareDropOn()
	{
		// This is the default return value.
		//
		boolean result = false;

		// This is the feedback we use to indicate drop on; it will only be used
		// if the command is executable.
		//
		feedback = FEEDBACK_SELECT;

		// Prepare the right type of operation.
		//
		result = prepareDropOnCommandBasedOnOperation();

		// If there isn't an executable command we should maybe try a copy
		// operation, but only if we're allowed and not doing a link.
		//
		result = tryDropCopyOperation(result);

		// If there isn't an executable command we should maybe try a link
		// operation, but only if we're allowed and not doing a link.
		//
		result = tryDropLinkOperation(result);

		return result;
	}

	private boolean prepareDropOnCommandBasedOnOperation()
	{
		boolean result;

		switch (operation)
		{
			case DROP_MOVE:
			{
				result = prepareDropMoveOn();
				break;
			}
			case DROP_COPY:
			{
				result = prepareDropCopyOn();
				break;
			}
			case DROP_LINK:
			{
				result = prepareDropLinkOn();
				break;
			}
			default:
				result = false;
		}
		return result;
	}

	private boolean tryDropCopyOperation(boolean intermediateResult)
	{
		boolean result = intermediateResult;

		boolean validOperation = operation != DROP_COPY && operation != DROP_LINK && (operations & DROP_COPY) != 0;
		if (!intermediateResult && validOperation)
		{
			reset();
			result = prepareDropCopyOn();
			if (result)
			{
				operation = DROP_COPY;
			}
		}
		return result;
	}

	private boolean tryDropLinkOperation(boolean intermediateResult)
	{
		boolean result = intermediateResult;

		if (!intermediateResult && operation != DROP_LINK && (operations & DROP_LINK) != 0)
		{
			reset();
			result = prepareDropLinkOn();
			if (result)
			{
				operation = DROP_LINK;
			}
		}
		return result;
	}

	/**
	 * This attempts to prepare a drop move on operation.
	 */
	@Override
	protected boolean prepareDropMoveOn()
	{
		if (isCrossDomain())
		{
			dragCommand = IdentityCommand.INSTANCE;
			dropCommand = UnexecutableCommand.INSTANCE;
		}
		else
		{
			dropCommand = AddCommand.create(domain, owner, null, collection);
			if (analyzeForNonContainment(dropCommand))
			{
				dropCommand.dispose();
				dropCommand = UnexecutableCommand.INSTANCE;
				dragCommand = IdentityCommand.INSTANCE;
			}
			else
			{
				dragCommand = RemoveCommand.create(domain, collection);
			}
		}

		boolean result = dragCommand.canExecute() && dropCommand.canExecute();
		return result;
	}

	/**
	 * This attempts to prepare a drop copy on operation.
	 */
	@Override
	protected boolean prepareDropCopyOn()
	{
		boolean result;

		dragCommand = CopyCommand.create(domain, collection);

		if (optimize)
		{
			result = optimizedCanExecute();
			if (result)
			{
				optimizedDropCommandOwner = owner;
			}
		}
		else
		{
			if (dragCommand.canExecute() && dragCommand.canUndo())
			{
				dragCommand.execute();
				saveFeatures(collection, dragCommand.getResult());
				isDragCommandExecuted = true;
				dropCommand = AddCommand.create(domain, owner, null, dragCommand.getResult());
				if (analyzeForNonContainment(dropCommand))
				{
					dropCommand.dispose();
					dropCommand = UnexecutableCommand.INSTANCE;

					dragCommand.undo();
					dragCommand.dispose();
					isDragCommandExecuted = false;
					dragCommand = IdentityCommand.INSTANCE;
				}
			}
			else
			{
				dropCommand = UnexecutableCommand.INSTANCE;
			}

			result = dragCommand.canExecute() && dropCommand.canExecute();
		}
		return result;
	}

	/**
	 * This attempts to prepare a drop link on operation.
	 */
	@Override
	protected boolean prepareDropLinkOn()
	{
		dragCommand = IdentityCommand.INSTANCE;
		EStructuralFeature feature = ((EObject) collection.iterator().next()).eContainingFeature();
		dropCommand = SetCommand.create(domain, owner, feature, collection);

		// If we can't set the collection, try setting use the single value of
		// the collection.
		//
		// if (!dropCommand.canExecute() && collection.size() == 1)
		// {
		// dropCommand.dispose();
		// dropCommand = SetCommand.create(domain, owner, feature,
		// collection.iterator().next());
		// }

		if (!dropCommand.canExecute() || !analyzeForNonContainment(dropCommand))
		{
			dropCommand.dispose();
			dropCommand = AddCommand.create(domain, owner, null, collection);
			if (!analyzeForNonContainment(dropCommand))
			{
				dropCommand.dispose();
				dropCommand = UnexecutableCommand.INSTANCE;
			}
		}

		boolean result = dropCommand.canExecute();
		return result;
	}

	@Override
	protected boolean optimizedCanExecute()
	{
		// We'll assume that the copy command can execute and that adding a copy
		// of the clipboard
		// is the same test as adding the clipboard contents itself.
		//
		Command addCommand = AddCommand.create(domain, owner, null, collection);
		boolean result = addCommand.canExecute() && !analyzeForNonContainment(addCommand);
		addCommand.dispose();
		return result;
	}

	@Override
	public void execute()
	{
		executeDragCommand();

		createDropCommand();

		// dropCommand = AddCommand.create(domain,
		// optimizedDropCommandOwner, null,
		// dragCommand.getResult());

		executeDropCommand();

	}

	private void executeDragCommand()
	{
		if (dragCommand != null && !isDragCommandExecuted)
		{
			// special case in optimized mode, the drag command wasn't executed,
			// so drag command was not yet created
			// if ((optimizedDropCommandOwner != null) && (dropCommand == null))
			// {
			if (optimizedDropCommandOwner != null)
			{
				if (dragCommand.canExecute())
				{
					dragCommand.execute();
				}
				else
				{
					LoggerFactory.getLogger().warn("Could not execute Drag Command"); //$NON-NLS-1$
					// Thread.dumpStack();
				}
			}
			else
			{
				dragCommand.execute();
			}
		}
	}

	private void createDropCommand()
	{
		featureMap = createFeatureMap(dragCommand.getResult());

		Object trueOwner = owner;

		if (featureMap.size() == 1)
		{
			createSimpleDropCommand(trueOwner);
		}
		else if (featureMap.size() > 1)
		{
			createCompoundDropCommand(trueOwner);
		}
	}

	private void createSimpleDropCommand(Object trueOwner)
	{
		EStructuralFeature feature = featureMap.keySet().iterator().next();
		if (feature == null || feature.getUpperBound() == -1 || feature.getUpperBound() > 1)
		{
			List<Object> vector = featureMap.get(feature);
			for (Object obj: vector)
			{
				if (obj instanceof GIdentifiable && trueOwner instanceof EObject)
				{
					GIdentifiable gObj = (GIdentifiable) obj;
					String shortName = MetaModelUtils.computeUniqueChildShortName((EObject) trueOwner,
						gObj.gGetShortName());
					gObj.gSetShortName(shortName);
				}
			}
			Object lastOwner = trueOwner;
			if (dropCommand instanceof AddCommand)
			{
				if (feature == null)
				{
					feature = ((AddCommand) dropCommand).getFeature();
				}

				if (trueOwner instanceof EObject)
				{
					EClass eClass = ((EObject) trueOwner).eClass();
					EList<EStructuralFeature> eStructuralFeatures = eClass.getEAllStructuralFeatures();
					if (!eStructuralFeatures.contains(feature))
					{

						trueOwner = ((EObject) trueOwner).eContainer();
					}
				}
				int index = ((AddCommand) dropCommand).getIndex();
				dropCommand = AddCommand.create(domain, trueOwner, feature, dragCommand.getResult(), index);
			}
			else
			{
				dropCommand = AddCommand.create(domain, lastOwner, feature, dragCommand.getResult());
			}

		}
		else
		{
			dropCommand = SetCommand.create(domain, trueOwner, feature, featureMap.get(feature).get(0));
		}
	}

	/**
	 * Multiple features need multiple add commands that contains one or more objects with the same feature
	 * 
	 * @param trueOwner
	 */
	private void createCompoundDropCommand(Object trueOwner)
	{
		CompoundCommand compoundCommand = new CompoundCommand();

		for (EStructuralFeature key: featureMap.keySet())
		{
			if (key.getUpperBound() == -1 || key.getUpperBound() > 1)
			{
				compoundCommand.append(AddCommand.create(domain, trueOwner, key, featureMap.get(key)));
			}
			else
			{
				compoundCommand.append(SetCommand.create(domain, trueOwner, key, featureMap.get(key).get(0)));
			}
		}
		dropCommand = compoundCommand;
	}

	private void executeDropCommand()
	{
		if (dropCommand != null)
		{
			if (dropCommand.canExecute())
			{
				dropCommand.execute();
			}
			else
			{
				LoggerFactory.getLogger().warn("Could not execute Drop Command"); //$NON-NLS-1$
				dragCommand.undo();
				// Thread.dumpStack();
			}
		}
	}

	private Map<EStructuralFeature, List<Object>> createFeatureMap(Collection<?> result)
	{
		if (featureMap != null && featureMap.size() > 0)
		{
			return featureMap;
		}
		else
		{
			featureMap = new HashMap<EStructuralFeature, List<Object>>();
		}

		Iterator<?> iterator = result.iterator();
		while (iterator.hasNext())
		{
			EObject eobject = (EObject) iterator.next();
			EStructuralFeature key = eobject.eContainingFeature();
			List<Object> values = featureMap.get(key);
			if (values == null)
			{
				values = new Vector<Object>();
			}
			values.add(eobject);

			featureMap.put(key, values);
		}

		return featureMap;
	}

	private void saveFeatures(Collection<?> original, Collection<?> copy)
	{
		Map<EStructuralFeature, List<Object>> fMap = new HashMap<EStructuralFeature, List<Object>>();

		Iterator<?> oIterator = original.iterator();
		Iterator<?> cIterator = copy.iterator();

		while (oIterator.hasNext() && cIterator.hasNext())
		{
			EObject eOriginal = (EObject) oIterator.next();
			EObject eCopy = (EObject) cIterator.next();

			EStructuralFeature key = eOriginal.eContainingFeature();
			List<Object> values = fMap.get(key);
			if (values == null)
			{
				values = new Vector<Object>();
			}

			values.add(eCopy);

			fMap.put(key, values);
		}

		featureMap = fMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.edit.command.DragAndDropCommand#getAffectedObjects()
	 */
	@Override
	public Collection<?> getAffectedObjects()
	{
		// TODO Auto-generated method stub
		return getResult();
	}
}

/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidt6343 Mar 15, 2011 12:40:35 PM </copyright>
 */
package eu.cessar.ct.core.mms.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandWrapper;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.StrictCompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CopyCommand;
import org.eclipse.emf.edit.command.PasteFromClipboardCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.FeatureMapEntryWrapperItemProvider;

import eu.cessar.ct.core.mms.EcucMetaModelUtils;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.EProjectVariant;
import gautosar.gecucdescription.GContainer;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * A Cessar-specific implementation of the {@link PasteFromClipboardCommand}.
 *
 */
public class CessarPasteFromClipboardCommand extends PasteFromClipboardCommand
{
	private static Command delegateCommand;

	private static boolean isRefferenced = false;
	private static boolean commandTypeIsCut = false;
	private static Collection<Object> originalClipboard;

	private List<Object> clipboard;

	static boolean isValidPastableContent;

	/**
	 * @author uidu3379
	 *
	 */
	private final class PasteCommand extends CommandWrapper
	{
		private final Command copyCommand;
		private Collection<Object> original;
		private Collection<Object> copy;

		/**
		 * @param copyCommand
		 */
		private PasteCommand(Command copyCommand)
		{
			this.copyCommand = copyCommand;
		}

		@Override
		protected Command createCommand()
		{
			// featureMap = new HashMap<EStructuralFeature,
			// Vector<Object>>();

			original = domain.getClipboard();

			Object localOwner = getOwner();
			// use the result of the copy command, unless a referenced
			// object is involved
			Collection<?> result = isRefferenced ? originalClipboard : copyCommand.getResult();
			Iterator<?> itResult = result.iterator();
			while (itResult.hasNext())
			{
				Object resObj = itResult.next();
				GIdentifiable gIdentifObj = getGIdentifiable(resObj);

				if (localOwner instanceof EObject && gIdentifObj != null)
				{
					if (!commandTypeIsCut)
					{
						String shortName = MetaModelUtils.computeUniqueChildShortName((EObject) localOwner,
							gIdentifObj.gGetShortName());
						gIdentifObj.gSetShortName(shortName);
					}
				}
			}

			copy = new ArrayList<Object>(result);
			// Use the original to do the add, but only if it's of the same
			// type as the copy.
			// This ensures that if there is conversion being done as part
			// of the copy,
			// as would be the case for a cross domain copy in the mapping
			// framework,
			// that we do actually use the converted instance.
			//
			if (shouldResetOriginal())
			{
				original = null;
			}

			if (featureMap == null)
			{
				return null;
			}

			// use the result of the copy command, unless a referenced
			// object is involved
			Command newCommand = getAddCommand(isRefferenced ? new ArrayList<Object>(originalClipboard) : copy);
			if (isRefferenced)
			{
				isRefferenced = false;
				originalClipboard = null;
			}

			return newCommand;
		}

		private boolean shouldResetOriginal()
		{
			boolean resetOriginal = false;
			if (original.size() == copy.size())
			{
				for (Iterator<Object> i = original.iterator(), j = copy.iterator(); i.hasNext();)
				{
					Object originalObject = i.next();
					Object copyObject = j.next();
					if (originalObject.getClass() != copyObject.getClass())
					{
						resetOriginal = true;
						break;
					}
				}
			}
			return resetOriginal;
		}

		private GIdentifiable getGIdentifiable(Object resObj)
		{
			GIdentifiable gIdentifObj = null;

			if (resObj instanceof GIdentifiable)
			{
				gIdentifObj = (GIdentifiable) resObj;
			}
			else
			// the result is an
			// ARpackage/FeatureMapEntryWrapperItemProvider
			{
				Object entryValue = getEntryValue(resObj);
				if (entryValue instanceof GIdentifiable)
				{
					gIdentifObj = (GIdentifiable) entryValue;
				}
			}
			return gIdentifObj;
		}

		private Object getEntryValue(Object obj)
		{
			Object entryValue = null;

			if (obj instanceof FeatureMapEntryWrapperItemProvider)
			{
				FeatureMap.Entry entry = (FeatureMap.Entry) ((FeatureMapEntryWrapperItemProvider) obj).getValue();
				if (entry != null)
				{
					entryValue = entry.getValue();
				}
			}

			return entryValue;
		}

		@Override
		public void execute()
		{
			if (original != null)
			{
				setCreationPhase();
				domain.setClipboard(copy);
				domain.setClipboard(original);
			}
			super.execute();
		}

		/**
		 * sets the creation phase of a GContainer to reflect it for later usage
		 *
		 */
		private void setCreationPhase()
		{
			EProjectVariant projectVariant = CESSARPreferencesAccessor.getProjectVariant(
				MetaModelUtils.getProject(owner));

			for (Object newContainer: copy)
			{
				if (newContainer instanceof GContainer)
				{
					// set the project phase for the created container
					EcucMetaModelUtils.setContainerCreationPhase((GContainer) newContainer, projectVariant);
				}
			}

		}

		@Override
		public void undo()
		{
			super.undo();
			if (original != null)
			{
				domain.setClipboard(original);
			}
		}

		@Override
		public void redo()
		{
			if (original != null)
			{
				domain.setClipboard(copy);
			}
			super.redo();
		}
	}

	private static Map<EStructuralFeature, List<Object>> featureMap;

	/**
	 * Creates a {@link CessarPasteFromClipboardCommand} using the given parameters.
	 *
	 * @param domain
	 *        the {@link EditingDomain} in which the command will be created
	 * @param owner
	 *        the object where the clipboard copy is pasted
	 * @param feature
	 *        the feature of the owner where the clipboard copy is pasted
	 * @param index
	 *        the index in the feature of the owner where the clipboard copy is pasted
	 */
	public CessarPasteFromClipboardCommand(EditingDomain domain, Object owner, Object feature, int index)
	{
		super(domain, owner, feature, index, true);
	}

	/**
	 * Creates a {@link CessarPasteFromClipboardCommand} using the given parameters.
	 *
	 * @param domain
	 *        the {@link EditingDomain} in which the command will be created
	 * @param owner
	 *        the object where the clipboard copy is pasted
	 * @param feature
	 *        the feature of the owner where the clipboard copy is pasted
	 * @param index
	 *        the index in the feature of the owner where the clipboard copy is pasted
	 * @param optimize
	 *        controls whether or not to optimize the canExecute (prepare)
	 * @param list
	 */
	public CessarPasteFromClipboardCommand(EditingDomain domain, Object owner, Object feature, int index,
		boolean optimize, List<Object> list)
	{
		super(domain, owner, feature, index, optimize);
		clipboard = list;
	}

	/**
	 * Creates a {@link CessarPasteFromClipboardCommand} using the given parameters.
	 *
	 * @param domain
	 *        the {@link EditingDomain} in which the command will be created
	 * @param owner
	 *        the object where the clipboard copy is pasted
	 * @param feature
	 *        the feature of the owner where the clipboard copy is pasted
	 * @param index
	 *        the index in the feature of the owner where the clipboard copy is pasted
	 * @param optimize
	 *        controls whether or not to optimize the canExecute (prepare)
	 */
	public CessarPasteFromClipboardCommand(EditingDomain domain, Object owner, Object feature, int index,
		boolean optimize)
	{
		super(domain, owner, feature, index, optimize);
	}

	/**
	 * Creates a {@link CessarPasteFromClipboardCommand} using the given parameters, having the optional positional
	 * index indicator is unspecified.
	 *
	 * @param domain
	 *        the {@link EditingDomain} in which the command will be created
	 * @param owner
	 *        the object where the clipboard copy is pasted
	 * @param feature
	 *        the feature of the owner where the clipboard copy is pasted
	 * @param list
	 * @param isValidPastableContent
	 * @return the newly created command
	 */
	@SuppressWarnings("hiding")
	public static Command create(EditingDomain domain, Object owner, Object feature, List<Object> list,
		boolean isValidPastableContent)
	{
		CessarPasteFromClipboardCommand.isValidPastableContent = isValidPastableContent;
		return create(domain, owner, feature, CommandParameter.NO_INDEX, list);
	}

	/**
	 * @param domain
	 * @param owner
	 * @param feature
	 * @return the newly created command
	 */
	public static Command create(EditingDomain domain, Object owner, Object feature)
	{
		return create(domain, owner, feature, CommandParameter.NO_INDEX);
	}

	/**
	 * Creates a {@link CessarPasteFromClipboardCommand} using the given parameters.
	 *
	 * @param domain
	 *        the {@link EditingDomain} in which the command will be created
	 * @param owner
	 *        the object where the clipboard copy is pasted
	 * @param feature
	 *        the feature of the owner where the clipboard copy is pasted
	 * @param index
	 *        the index in the feature of the owner where the clipboard copy is pasted
	 * @param list
	 * @return the newly created command
	 */
	public static Command create(EditingDomain domain, Object owner, Object feature, int index, List<Object> list)
	{
		if (domain == null)
		{
			return new CessarPasteFromClipboardCommand(domain, owner, feature, index, true, list);
		}
		else
		{
			// Command command =
			// domain.createCommand(CessarPasteFromClipboardCommand.class,
			// new CommandParameter(owner, feature, Collections.emptyList(),
			// index));
			// return command;
			CommandParameter param = new CommandParameter(owner, feature, Collections.emptyList(), index);
			return new CessarPasteFromClipboardCommand(domain, param.getOwner(), param.getFeature(), param.getIndex(),
				domain.getOptimizeCopy(), list);

		}
	}

	/**
	 * Creates a {@link CessarPasteFromClipboardCommand} using the given parameters.
	 *
	 * @param domain
	 *        the {@link EditingDomain} in which the command will be created
	 * @param owner
	 *        the object where the clipboard copy is pasted
	 * @param feature
	 *        the feature of the owner where the clipboard copy is pasted
	 * @param index
	 *        the index in the feature of the owner where the clipboard copy is pasted
	 * @return the newly created command
	 */
	public static Command create(EditingDomain domain, Object owner, Object feature, int index)
	{
		if (domain == null)
		{
			return new CessarPasteFromClipboardCommand(domain, owner, feature, index, true);
		}
		else
		{
			// Command command =
			// domain.createCommand(CessarPasteFromClipboardCommand.class,
			// new CommandParameter(owner, feature, Collections.emptyList(),
			// index));
			// return command;
			CommandParameter param = new CommandParameter(owner, feature, Collections.emptyList(), index);
			return new CessarPasteFromClipboardCommand(domain, param.getOwner(), param.getFeature(), param.getIndex(),
				domain.getOptimizeCopy());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.emf.edit.command.PasteFromClipboardCommand#prepare()
	 */
	@Override
	protected boolean prepare()
	{
		// Create a strict compound command to do a copy and then add the result
		//
		command = new StrictCompoundCommand();

		if (domain.getClipboard() == null && clipboard != null)
		{
			domain.setClipboard(clipboard);
		}
		final Command copyCommand = CopyCommand.create(domain, domain.getClipboard());
		command.append(copyCommand);

		// Create a proxy that will create an add command.
		//

		command.append(new PasteCommand(copyCommand));

		boolean result;
		if (optimize)
		{
			// This will determine canExecute as efficiently as possible.
			//
			result = optimizedCanExecute();
		}
		else
		{
			// This will actually execute the copy command in order to check if
			// the add can execute.
			//
			result = command.canExecute();
		}

		return result;
	}

	private Command getAddCommand(Collection<Object> copy)
	{
		// check if the copy is an ARpackage/FeatureMapEntryWrapperItemProvider,
		// if so transform it into a collection of EObject
		copy = addaptClipboard(copy); // SUPPRESS CHECKSTYLE it's used outside

		Command addCommand = null;
		// only one command needed : one object or multiple objects with
		// same feature
		if (featureMap.size() == 1)
		{
			EStructuralFeature localFeature = featureMap.keySet().iterator().next();
			if (localFeature == null || localFeature.getUpperBound() == -1 || localFeature.getUpperBound() > 1)
			{
				if (owner instanceof EObject)
				{
					// if (feature != null && ((EObject) owner).eClass().getEAllStructuralFeatures().contains(feature))
					// {
					// addCommand = AddCommand.create(domain, owner, feature, copy, index);
					// }
					addCommand = AddCommand.create(domain, owner, localFeature, copy, index);
					if (!addCommand.canExecute())
					// else
					{
						if (!((EObject) owner).eClass().getEAllStructuralFeatures().contains(localFeature))
						{
							EList<EStructuralFeature> eAllStructuralFeatures = ((EObject) owner).eClass().getEAllStructuralFeatures();
							for (int i = 0; i < eAllStructuralFeatures.size(); i++)
							{
								EStructuralFeature eStructuralFeature = eAllStructuralFeatures.get(i);
								if (eStructuralFeature instanceof EReference)
								{
									EClassifier eType2 = eStructuralFeature.getEType();

									Iterator<Object> iterator = copy.iterator();
									while (iterator.hasNext())
									{
										Object next = iterator.next();
										EClass eClass2 = ((EObject) next).eClass();
										if (eType2.equals(eClass2))
										{
											addCommand = AddCommand.create(domain, owner, eStructuralFeature, copy,
												index);
											break;

										}
										else
										{
											if (((EClass) eType2).isSuperTypeOf(eClass2))
											{
												addCommand = AddCommand.create(domain, owner, eStructuralFeature, copy,
													index);
												break;
											}
										}
									}
								}
							}
						}
					}
				}
			}
			else
			{
				addCommand = SetCommand.create(domain, owner, localFeature, copy.iterator().next(), index);
			}
		}
		// multiple features need multiple add commands that contains
		// one or more objects with the same feature
		else
		{
			CompoundCommand compoundCommand = new CompoundCommand();

			for (EStructuralFeature key: featureMap.keySet())
			{
				if (key.getUpperBound() == -1 || key.getUpperBound() > 1)
				{
					compoundCommand.append(AddCommand.create(domain, owner, key, featureMap.get(key), index));
				}
				else
				{
					compoundCommand.append(
						SetCommand.create(domain, owner, key, featureMap.get(key).iterator().next(), index));
				}
			}
			addCommand = compoundCommand;
		}

		callDelegate();

		return addCommand;
	}

	/**
	 * Passes a command to the <code>CessarPasteFromClipboardCommand</code> to be executed after the actual pasting has
	 * been done.<br>
	 * The delegator (the place from where the method is called) must ensure the command does not execute before. If it
	 * must be /is executed before, an undo should be called.<br>
	 * <br>
	 * <i>21.03.2011 : used only for <b>cut-paste</b></i>
	 *
	 * @param command
	 *        - The command to be executed later
	 */
	protected static synchronized void setDelegateCommand(Command command)
	{
		delegateCommand = command;
		if (delegateCommand != null)
		{
			commandTypeIsCut = true;
		}
		else
		{
			commandTypeIsCut = false;
		}
	}

	/**
	 * Calls the delayed command, if any is specified. The command is disposed (made null) after a first call.
	 */
	private synchronized void callDelegate()
	{
		if (delegateCommand != null)
		{
			delegateCommand.execute();
		}
		delegateCommand = null;
	}

	/**
	 * Saves a map of {@link EStructuralFeature} and Vector key-value map, where the keys are computed from features of
	 * the {@link EObject}s in the original collection and the values from the copy collection.
	 *
	 * @param original
	 * @param copy
	 */
	public static void saveFeatures(Collection<?> original, Collection<?> copy)
	{
		Map<EStructuralFeature, List<Object>> fMap = new HashMap<EStructuralFeature, List<Object>>();

		Iterator<?> oIterator = original.iterator();
		Iterator<?> cIterator = copy.iterator();

		while (oIterator.hasNext() && cIterator.hasNext())
		{
			Object objOriginal = oIterator.next();
			if (objOriginal instanceof EObject)
			{
				EObject eOriginal = (EObject) objOriginal;
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
			else
			// the object is an ARpackage/FeatureMapEntryWrapperItemProvider
			{
				if (objOriginal instanceof FeatureMapEntryWrapperItemProvider)
				{
					FeatureMapEntryWrapperItemProvider eCopy = (FeatureMapEntryWrapperItemProvider) cIterator.next();

					FeatureMap.Entry entry = (FeatureMap.Entry) ((FeatureMapEntryWrapperItemProvider) objOriginal).getValue();

					EStructuralFeature key = ((EObject) entry.getValue()).eContainingFeature();
					List<Object> values = fMap.get(key);
					if (values == null)
					{
						values = new Vector<Object>();
					}

					values.add(eCopy.getValue());

					fMap.put(key, values);
				}
			}
		}

		featureMap = fMap;
	}

	/**
	 * @param originalClipboardValue
	 * @param originalClipboard
	 *        the originalClipboard to set
	 */
	protected static synchronized void setOriginalClipboard(Collection<Object> originalClipboardValue)
	{
		originalClipboard = /* addaptClipboard(originalClipboardValue) */originalClipboardValue;
	}

	/**
	 * @param isRefference
	 */
	protected static synchronized void setIsRefferencedFlag(boolean isRefference)
	{
		isRefferenced = isRefference;
	}

	/**
	 * Override this method in order to handle FeatureMapEntryWrapperItemProvider object types
	 */
	@Override
	protected boolean optimizedCanExecute()
	{
		Collection<Object> adaptedClipboard = addaptClipboard(domain.getClipboard());
		Command addCommand = AddCommand.create(domain, owner, feature, adaptedClipboard);
		boolean result = addCommand.canExecute();
		addCommand.dispose();
		if (!result && CessarPasteFromClipboardCommand.isValidPastableContent)
		{
			result = CessarPasteFromClipboardCommand.isValidPastableContent;
		}
		return result;
	}

	private static synchronized Collection<Object> addaptClipboard(Collection<Object> clipboard)
	{
		Collection<Object> addaptedClipboard = new ArrayList<>();
		if (clipboard != null && clipboard.size() > 0)
		{
			for (Object object: clipboard)
			{
				if (object instanceof FeatureMapEntryWrapperItemProvider)
				{
					FeatureMap.Entry entry = (FeatureMap.Entry) ((FeatureMapEntryWrapperItemProvider) object).getValue();
					addaptedClipboard.add(entry.getValue());
				}
				else
				{
					addaptedClipboard.add(object);
				}
			}
		}
		return addaptedClipboard;
	}

}

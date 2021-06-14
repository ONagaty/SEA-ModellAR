package mms.internal.gautosar.gecucdescription.commands;

import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * An implementation of {@link AddCommand} that adds {@link GContainer}s in a given module.
 * 
 */
public class AddGContainerInModuleCommand extends AddCommand
{

	/**
	 * Creates an instance of {@link AddGContainerInModuleCommand} using the provided parameters.
	 * 
	 * @param domain
	 *        the {@link EditingDomain} in which the command operates
	 * @param owner
	 *        the owner object into which the values will be added
	 * @param reference
	 *        the many-valued feature of the owner into which the values are added
	 * @param collection
	 *        the values to add into the specified many-valued feature of the owner
	 * @param index
	 *        the position at which the objects will be inserted
	 */
	public AddGContainerInModuleCommand(EditingDomain domain, EObject owner, EReference reference,
		Collection<?> collection, Integer index)
	{
		super(domain, owner, reference, collection, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.edit.command.AbstractOverrideableCommand#doCanExecute()
	 */
	@Override
	public boolean doCanExecute()
	{
		boolean result = super.doCanExecute();

		if (!result || !(owner instanceof GModuleConfiguration))
		{
			return false;
		}

		GModuleConfiguration gOwner = (GModuleConfiguration) owner;
		GModuleDef gOwnerDef = gOwner.gGetDefinition();

		// if gOwnerDef = null is a strange scenario -> accept for now
		if (gOwnerDef != null)
		{
			// assume not OK
			result = false;
			if (collection != null && !collection.isEmpty())
			{
				for (Object subCntObj: collection)
				{
					if (subCntObj instanceof GContainer)
					{
						result = doesContainerDefContainModuleDef(gOwnerDef, (GContainer) subCntObj, result);
					}
					if (!result)
					{
						// stop as soon as we found something that is wrong
						break;
					}
				}
			}
		}

		return result;
	}

	/**
	 * @param targetModuleDef
	 * @param sourceContainer
	 * @param resultInitValue
	 * @return
	 */
	private static boolean doesContainerDefContainModuleDef(GModuleDef targetModuleDef, GContainer sourceContainer,
		boolean resultInitValue)
	{
		boolean result = resultInitValue;
		GContainerDef subCntDef = sourceContainer.gGetDefinition();
		if (subCntDef != null && subCntDef.eContainer() != null)
		{
			if (subCntDef.eContainer() instanceof GChoiceContainerDef)
			{
				result = subCntDef.eContainer().eContainer().equals(targetModuleDef);
			}
			else
			{
				result = subCntDef.eContainer().equals(targetModuleDef);
			}
		}
		return result;
	}
}

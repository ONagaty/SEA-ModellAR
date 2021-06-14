package mms.internal.gautosar.gecucdescription;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;

import eu.cessar.ct.core.mms.ecuc.commands.parameters.NewEcucContainerCommandParameter;
import eu.cessar.ct.core.mms.providers.DelegateEditingDomainItemProvider;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Class that compute new children descriptor for a GModuleConfiguration. The
 * children descriptors are created based on the definitions from corresponding
 * sMODULE DEF.
 */
public class GModuleConfigurationEditingDomainItemProvider extends
	DelegateEditingDomainItemProvider
{
	/**
	 * Default constructor
	 * 
	 * @param parent
	 *        a parent {@link IEditingDomainItemProvider} implementation
	 */
	public GModuleConfigurationEditingDomainItemProvider(IEditingDomainItemProvider parent)
	{
		super(parent);
	}

	/**
	 * The method that create children descriptors based on the datas from
	 * Module Def.
	 */
	@Override
	public Collection<?> getNewChildDescriptors(Object object, EditingDomain editingDomain,
		Object sibling)
	{
		// if selected object is a Module Configuration, extract the children
		// according with its definition
		GModuleConfiguration moduleConfig = null;
		if (object instanceof GModuleConfiguration)
		{
			moduleConfig = (GModuleConfiguration) object;
		}
		else if (sibling instanceof GModuleConfiguration)
		{
			moduleConfig = (GModuleConfiguration) sibling;
		}
		if (moduleConfig != null)
		{
			return getModuleConfigurationChildDescriptors(moduleConfig);
		}

		// unknown selection, so retrieve default children descriptors
		return super.getNewChildDescriptors(object, editingDomain, sibling);
	}

	/**
	 * Compute children descriptors when selection is a Module configuration
	 * 
	 * @param moduleConfig
	 *        selected module configuration
	 * @return A children descriptors collection
	 */
	protected Collection<?> getModuleConfigurationChildDescriptors(GModuleConfiguration moduleConfig)
	{
		GModuleDef moduleDef = moduleConfig.gGetDefinition();
		if (moduleDef != null)
		{
			EList<? extends GContainerDef> childContainerDefs = moduleDef.gGetContainers();
			return collectSubcontainerDescriptors(childContainerDefs, moduleConfig);
		}
		return Collections.emptyList();
	}

	/**
	 * Collect children descriptors for the subcontainers of currently selected
	 * container.
	 */
	private List<CommandParameter> collectSubcontainerDescriptors(
		EList<? extends GContainerDef> childContainerDefs, GIdentifiable owner)
	{
		List<CommandParameter> result = new ArrayList<CommandParameter>();
		if ((childContainerDefs != null) && !childContainerDefs.isEmpty())
		{
			for (GContainerDef gContainerDef: childContainerDefs)
			{
				if (gContainerDef instanceof GChoiceContainerDef)
				{
					// for choice container, create choices child descriptors
					EList<? extends GParamConfContainerDef> choices = ((GChoiceContainerDef) gContainerDef).gGetChoices();
					for (GParamConfContainerDef choiceDef: choices)
					{
						NewEcucContainerCommandParameter commandParameter = new NewEcucContainerCommandParameter(
							owner, choiceDef);
						commandParameter.setChoiceContainerDefinition((GChoiceContainerDef) gContainerDef);
						result.add(commandParameter);
					}
				}
				else if (gContainerDef instanceof GParamConfContainerDef)
				{
					result.add(new NewEcucContainerCommandParameter(owner, gContainerDef));
				}
				else
				{
					throw new ClassCastException(
						"Should not enter on this branch. Must investigate"); //$NON-NLS-1$
				}
			}
		}
		return result;
	}
}

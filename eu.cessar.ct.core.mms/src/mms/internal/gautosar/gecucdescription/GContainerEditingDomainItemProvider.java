package mms.internal.gautosar.gecucdescription;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;

import eu.cessar.ct.core.mms.ecuc.commands.parameters.NewEcucContainerCommandParameter;
import eu.cessar.ct.core.mms.providers.DelegateEditingDomainItemProvider;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Class that compute new children descriptor for a GContainer. The children
 * descriptors are created based on the definitions from corresponding sMODULE
 * DEF.
 */
public class GContainerEditingDomainItemProvider extends DelegateEditingDomainItemProvider
{
	/**
	 * Default constructor
	 * 
	 * @param parent
	 *        a parent {@link IEditingDomainItemProvider} implementation
	 */
	public GContainerEditingDomainItemProvider(IEditingDomainItemProvider parent)
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

		// if selected object is an ECUC container, extract the children
		// according with its definition
		GContainer container = null;
		if (object instanceof GContainer)
		{
			container = (GContainer) object;
		}
		else if (sibling instanceof GContainer)
		{
			container = (GContainer) sibling;
		}
		if (container != null)
		{
			Collection<CommandParameter> result = getContainerChildDescriptors(container);
			Collection<?> others = super.getNewChildDescriptors(object, editingDomain, sibling);
			for (Object descr: others)
			{
				if (descr instanceof CommandParameter)
				{
					// just skip commands that creates parameter, references or
					// containers
					CommandParameter cmd = (CommandParameter) descr;
					if (!(cmd.value instanceof GContainer)
						&& !(cmd.value instanceof GParameterValue)
						&& !(cmd.value instanceof GConfigReferenceValue))
					{
						result.add(cmd);
					}
				}
			}
			return result;
		}

		// unknown selection, so retrieve default children descriptors
		return super.getNewChildDescriptors(object, editingDomain, sibling);
	}

	/**
	 * Compute children descriptors when selection is a container or choice
	 * container instance
	 * 
	 * @param containerConfig
	 *        selected container instance
	 * @return A children descriptors collection
	 */
	protected Collection<CommandParameter> getContainerChildDescriptors(GContainer containerConfig)
	{
		GContainerDef containerDef = containerConfig.gGetDefinition();
		List<CommandParameter> result = new ArrayList<CommandParameter>();
		if (containerDef instanceof GParamConfContainerDef)
		{
			EList<? extends GContainerDef> childContainerDefs = ((GParamConfContainerDef) containerDef).gGetSubContainers();
			result.addAll(collectSubcontainerDescriptors(childContainerDefs, containerConfig));
		}
		else if (containerDef instanceof GChoiceContainerDef)
		{
			EList<? extends GContainerDef> childContainerDefs = ((GChoiceContainerDef) containerDef).gGetChoices();
			result.addAll(collectSubcontainerDescriptors(childContainerDefs, containerConfig));
		}

		return result;
	}

	/**
	 * Collect children descriptors for the subcontainers of currently selected
	 * container.
	 */
	protected List<CommandParameter> collectSubcontainerDescriptors(
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

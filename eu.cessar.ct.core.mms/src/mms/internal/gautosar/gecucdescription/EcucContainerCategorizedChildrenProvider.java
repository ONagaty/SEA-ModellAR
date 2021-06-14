package mms.internal.gautosar.gecucdescription;

import java.util.ArrayList;
import java.util.List;

import eu.cessar.ct.core.mms.ecuc.commands.parameters.NewEcucContainerCommandParameter;
import eu.cessar.ct.core.mms.providers.GenericCategorizedChildrenProvider;
import eu.cessar.ct.core.mms.providers.ICategorizedChildrenProvider;

/**
 * A {@link ICategorizedChildrenProvider} implementation applicable for ECUC
 * containers. It leave regular container descriptors un-grouped and categorize
 * the choice containers.
 */
public class EcucContainerCategorizedChildrenProvider extends GenericCategorizedChildrenProvider
{
	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.mms.providers.generic.GenericCategorizedChildrenProvider#getPathForChildDescriptor(java.lang.Object)
	 */
	@Override
	public List<String> getPathForChildDescriptor(Object descriptor)
	{
		List<String> result = new ArrayList<String>();

		if (descriptor instanceof NewEcucContainerCommandParameter)
		{
			NewEcucContainerCommandParameter containerCmdParameter = (NewEcucContainerCommandParameter) descriptor;
			if (containerCmdParameter.isChoice())
			{
				result.add(containerCmdParameter.getChoiceContainerDefinition().gGetShortName());
			}
		}
		return result;
	}
}

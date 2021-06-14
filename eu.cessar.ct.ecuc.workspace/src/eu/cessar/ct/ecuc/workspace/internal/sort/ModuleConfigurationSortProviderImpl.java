/**
 * 
 */
package eu.cessar.ct.ecuc.workspace.internal.sort;

import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

/**
 * @author uidl6458
 * 
 */
public class ModuleConfigurationSortProviderImpl extends AbstractEcuSortProviderImpl
{

	private final GModuleDef definition;

	/**
	 * @param domain
	 * @param parentObject
	 */
	public ModuleConfigurationSortProviderImpl(final TransactionalEditingDomain domain,
		final GModuleConfiguration parentObject, final GModuleDef definition)
	{
		super(domain, parentObject);
		this.definition = definition;
	}

	/* (non-Javadoc)
	 * @see org.autosartools.ecuconfig.genericeditor.core.internal.sort.EcuSortProviderImpl#getContainerDefinitions()
	 */
	@Override
	protected List<GParamConfContainerDef> getContainerDefinitions()
	{
		List<GParamConfContainerDef> result = new ArrayList<GParamConfContainerDef>();
		for (EObject obj: definition.eContents())
		{
			if (obj instanceof GParamConfContainerDef)
			{
				result.add((GParamConfContainerDef) obj);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.autosartools.ecuconfig.genericeditor.core.internal.sort.EcuSortProviderImpl#getChoiceContainerDefinitions()
	 */
	@Override
	protected List<GChoiceContainerDef> getChoiceContainerDefinitions()
	{
		List<GChoiceContainerDef> result = new ArrayList<GChoiceContainerDef>();
		for (EObject obj: definition.eContents())
		{
			if (obj instanceof GChoiceContainerDef)
			{
				result.add((GChoiceContainerDef) obj);
			}
		}
		return result;
	}
}

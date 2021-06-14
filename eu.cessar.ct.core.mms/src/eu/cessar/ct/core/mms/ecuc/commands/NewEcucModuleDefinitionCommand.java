package eu.cessar.ct.core.mms.ecuc.commands;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;

import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.ecuc.commands.parameters.NewEcucModuleDefinitionCommandParameter;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;
import gautosar.ggenericstructure.ginfrastructure.GPackageableElement;

/**
 * Generic implementation of the command that create an ECUC Container, based on
 * the data passed through the parameter.
 */
public class NewEcucModuleDefinitionCommand extends AbstractCessarCommand
{
	private NewEcucModuleDefinitionCommandParameter cmdParameter;

	/**
	 * Creates a new instance of {@link NewEcucModuleDefinitionCommand}.
	 * 
	 * @param parameter
	 *        an instance of Ecuc module definition command parameter
	 * @param image
	 *        an image object to be displayed as command icon
	 */
	public NewEcucModuleDefinitionCommand(NewEcucModuleDefinitionCommandParameter parameter,
		Object image)
	{
		super(image);
		Assert.isNotNull(parameter);
		this.cmdParameter = parameter;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
	 */
	@Override
	public boolean canExecute()
	{
		return (cmdParameter.getOwner() instanceof GARPackage);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.command.CommandActionDelegate#getText()
	 */
	@Override
	public String getText()
	{
		return GModuleDef.class.getInterfaces()[0].getSimpleName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.command.Command#execute()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void execute()
	{
		GIdentifiable owner = (GIdentifiable) cmdParameter.getOwner();
		EList<? extends GPackageableElement> childrenList = null;
		childrenList = ((GARPackage) owner).gGetElements();

		if (childrenList != null)
		{
			EList tmpList = childrenList;
			IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(owner);
			IGenericFactory genericFactory = mmService.getGenericFactory();
			if (genericFactory != null)
			{
				GModuleDef newModuleDef = genericFactory.createModuleDef();
				newModuleDef.gSetShortName(cmdParameter.getShortName());

				GModuleDef definition = cmdParameter.getDefinition();
				if (null != definition)
				{
					newModuleDef.gSetRefinedModuleDef(definition);
				}
				tmpList.add(newModuleDef);

				result.add(newModuleDef);
			}
		}
	}

}

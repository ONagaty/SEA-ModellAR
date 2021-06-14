package eu.cessar.ct.core.mms.ecuc.commands;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;

import eu.cessar.ct.core.mms.ecuc.commands.parameters.NewEcucModuleDefinitionCommandParameter;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;
import gautosar.ggenericstructure.ginfrastructure.GPackageableElement;

/**
 * Generic implementation of the command that create an ECUC Container, based on
 * the data passed through the parameter.
 */
public class CopyEcucModuleDefinitionCommand extends AbstractCessarCommand
{
	private NewEcucModuleDefinitionCommandParameter cmdParameter;

	/**
	 * Creates a new instance of {@link CopyEcucModuleDefinitionCommand} using
	 * the given parameters.
	 * 
	 * @param parameter
	 *        the {@link NewEcucModuleDefinitionCommandParameter} to use. Must
	 *        not be <code>null</code>.
	 * @param image
	 *        the image that will be used for the command
	 */
	public CopyEcucModuleDefinitionCommand(NewEcucModuleDefinitionCommandParameter parameter,
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
		return (cmdParameter.getOwner() instanceof GARPackage)
			&& null != cmdParameter.getDefinition();
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
	public void execute()
	{
		GIdentifiable owner = (GIdentifiable) cmdParameter.getOwner();
		// EList<GPackageableElement> childrenList = null;
		EList<GPackageableElement> childrenList = ((GARPackage) owner).gGetElements();

		if (childrenList != null)
		{
			childrenList.add(cmdParameter.getDefinition());
			result.add(cmdParameter.getDefinition());
		}
	}

}

package eu.cessar.ct.core.mms.ecuc.commands;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.ecuc.commands.parameters.NewARPackageCommandParameter;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GAUTOSAR;

/**
 * Generic implementation of the command that create an ECUC Container, based on
 * the data passed through the parameter.
 */
public class NewARPackageCommand extends AbstractCessarCommand
{
	private NewARPackageCommandParameter cmdParameter;

	/**
	 * Default command constructor
	 * 
	 * @param parameter
	 *        an instance of ARPackage command parameter
	 * @param image
	 *        an image object to be displayed as command icon
	 */
	public NewARPackageCommand(NewARPackageCommandParameter parameter, Object image)
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
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.command.CommandActionDelegate#getText()
	 */
	@Override
	public String getText()
	{
		return GARPackage.class.getInterfaces()[0].getSimpleName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.command.Command#execute()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void execute()
	{
		EObject owner = (EObject) cmdParameter.getOwner();
		EList<? extends GARPackage> childrenList = null;

		if (owner instanceof GAUTOSAR)
		{
			childrenList = ((GAUTOSAR) owner).gGetArPackages();
		}
		if (owner instanceof GARPackage)
		{
			childrenList = ((GARPackage) owner).gGetSubPackages();
		}

		if (childrenList != null)
		{
			EList tmpList = childrenList;
			IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(owner);
			IGenericFactory genericFactory = mmService.getGenericFactory();
			if (genericFactory != null)
			{
				GARPackage newARPackage = genericFactory.createARPackage();
				newARPackage.gSetShortName(cmdParameter.getShortName());
				tmpList.add(newARPackage);
				result.add(newARPackage);
			}
		}
	}

}

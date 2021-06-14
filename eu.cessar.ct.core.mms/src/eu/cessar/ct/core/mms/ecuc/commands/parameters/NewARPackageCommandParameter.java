package eu.cessar.ct.core.mms.ecuc.commands.parameters;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

import eu.cessar.ct.core.mms.commands.ICessarCommandParameter;
import eu.cessar.ct.core.mms.ecuc.commands.NewARPackageCommand;

/**
 * A generic command parameter implementation that must be passed to the command
 * that create a new ECUC container.
 */
public class NewARPackageCommandParameter extends CommandParameter implements
	ICessarCommandParameter
{
	private String shortName;

	/**
	 * Creates an instance of {@link NewARPackageCommandParameter} using the
	 * provided parameters.
	 * 
	 * @param owner
	 *        the owner object for the ARPackage to be created
	 * @param newShortName
	 *        the short name of ARPackage to be created by this command
	 */
	public NewARPackageCommandParameter(EObject owner, String newShortName)
	{
		super(owner);
		Assert.isNotNull(owner);

		shortName = newShortName;
	}

	/**
	 * Gets the short name stored inside the command parameter.
	 * 
	 * @return the shortName
	 */
	public String getShortName()
	{
		return shortName;
	}

	public Command createCommand(AdapterFactory adapterFactory)
	{
		Object image = null;
		if (adapterFactory != null)
		{
			Adapter adapter = (Adapter) adapterFactory.adapt(owner, IItemLabelProvider.class);
			if (adapter instanceof IItemLabelProvider)
			{
				image = ((IItemLabelProvider) adapter).getImage(owner);
			}
		}
		return new NewARPackageCommand(this, image);
	}
}

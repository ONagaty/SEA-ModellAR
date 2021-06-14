package eu.cessar.ct.core.mms.ecuc.commands;

import java.math.BigInteger;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.ecuc.commands.parameters.NewEcucParameterCommandParameter;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Generic implementation of the command that create an ECUC parameter, based on
 * the data passed through the command parameter.
 */
public class NewEcucParameterCommand extends AbstractCessarCommand
{
	private NewEcucParameterCommandParameter cmdParameter;

	/**
	 * Creates an instance of {@link NewEcucParameterCommand}.
	 * 
	 * @param parameter
	 *        an instance of {@link NewEcucParameterCommandParameter}
	 * @param image
	 *        an image object to be displayed as command icon
	 */
	public NewEcucParameterCommand(NewEcucParameterCommandParameter parameter, Object image)
	{
		super(image);
		Assert.isNotNull(parameter);
		this.cmdParameter = parameter;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.command.CommandActionDelegate#getText()
	 */
	@Override
	public String getText()
	{
		return "Parameter : " + cmdParameter.getDefinition().gGetShortName(); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
	 */
	@Override
	public boolean canExecute()
	{
		if (!(cmdParameter.getOwner() instanceof GContainer))
		{
			return false;
		}

		GIdentifiable owner = (GIdentifiable) cmdParameter.getOwner();
		IEcucMMService ecucMMService = MMSRegistry.INSTANCE.getMMService(owner).getEcucMMService();
		GConfigParameter gConfigParameter = cmdParameter.getDefinition();
		EList<? extends GParameterValue> values = ((GContainer) owner).gGetParameterValues();
		int instancesCount = 0;

		// count the parameter instances
		for (GParameterValue gParameterValue: values)
		{
			if (gParameterValue.gGetDefinition().equals(gConfigParameter))
			{
				instancesCount++;
			}
		}

		BigInteger upper = ecucMMService.getUpperMultiplicity(gConfigParameter, BigInteger.ONE,
			true);

		if (upper.equals(IEcucMMService.MULTIPLICITY_STAR))
		{
			return true;
		}
		else
		{
			long longUpper = upper.longValue();
			return longUpper > instancesCount;
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.mms.ecuc.commands.AbstractCessarCommand#execute()
	 */
	@Override
	public void execute()
	{
		GIdentifiable owner = (GIdentifiable) cmdParameter.getOwner();
		EList<GParameterValue> parameterValues = ((GContainer) owner).gGetParameterValues();
		IGenericFactory genericFactory = MMSRegistry.INSTANCE.getGenericFactory(owner);
		if (genericFactory != null)
		{
			GConfigParameter paramDef = cmdParameter.getDefinition();
			GParameterValue parameterValue = genericFactory.createParameterValue(paramDef);
			if (parameterValue != null)
			{
				parameterValues.add(parameterValue);
				result.add(parameterValue);
			}
		}
	}

}

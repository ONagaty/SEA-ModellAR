package eu.cessar.ct.core.mms.ecuc.commands;

import java.math.BigInteger;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.ecuc.commands.parameters.NewEcucReferenceCommandParameter;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Generic implementation of the command that create an ECUC reference, based on
 * the data passed through the command parameter.
 */
public class NewEcucReferenceCommand extends AbstractCessarCommand
{
	private NewEcucReferenceCommandParameter cmdParameter;

	/**
	 * Reference created by this command
	 */
	private GConfigReferenceValue referenceValue;

	/**
	 * Creates an instance of {@link NewEcucReferenceCommand}.
	 * 
	 * @param parameter
	 *        an instance of {@link NewEcucReferenceCommandParameter}
	 * @param image
	 *        an image object to be displayed as command icon
	 */
	public NewEcucReferenceCommand(NewEcucReferenceCommandParameter parameter, Object image)
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
		return "Reference : " + cmdParameter.getDefinition().gGetShortName(); //$NON-NLS-1$
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
		GConfigReference gConfigReference = cmdParameter.getDefinition();
		EList<? extends GConfigReferenceValue> values = ((GContainer) owner).gGetReferenceValues();
		int instancesCount = 0;

		// count the reference instances
		for (GConfigReferenceValue gReferenceValue: values)
		{
			if (gReferenceValue.gGetDefinition().equals(gConfigReference))
			{
				instancesCount++;
			}
		}

		BigInteger upper = ecucMMService.getUpperMultiplicity(gConfigReference, BigInteger.ONE,
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
		EList<GConfigReferenceValue> referenceValues = ((GContainer) owner).gGetReferenceValues();
		IGenericFactory genericFactory = MMSRegistry.INSTANCE.getGenericFactory(owner);

		GConfigReference refDef = cmdParameter.getDefinition();
		GConfigReferenceValue referenceValue = genericFactory.createReferenceValue(refDef);
		referenceValues.add(referenceValue);
		result.add(referenceValue);
	}

}

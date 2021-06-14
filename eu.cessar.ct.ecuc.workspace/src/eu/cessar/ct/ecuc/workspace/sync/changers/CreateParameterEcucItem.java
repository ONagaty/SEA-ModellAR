package eu.cessar.ct.ecuc.workspace.sync.changers;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.ecuc.workspace.internal.SynchronizeConstants;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.math.BigInteger;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

public class CreateParameterEcucItem extends CreateEcucItem
{
	private GIdentifiable owner;
	private GConfigParameter paramDef;

	public CreateParameterEcucItem(GIdentifiable owner, GConfigParameter paramDef)
	{
		this.owner = owner;
		this.paramDef = paramDef;
	}

	@Override
	public boolean canExecute()
	{
		if (!(owner instanceof GContainer))
		{
			return false;
		}

		IEcucMMService ecucMMService = MMSRegistry.INSTANCE.getMMService(owner).getEcucMMService();

		EList<? extends GParameterValue> values = ((GContainer) owner).gGetParameterValues();
		int instancesCount = 0;

		// count the parameter instances
		for (GParameterValue gParameterValue: values)
		{
			if (gParameterValue.gGetDefinition().equals(paramDef))
			{
				instancesCount++;
			}
		}

		BigInteger upper = ecucMMService.getUpperMultiplicity(paramDef, BigInteger.ONE, true);

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

	@Override
	public String getActionDescription()
	{
		return SynchronizeConstants.createParamItemDescr;
	}

	@Override
	protected void create()
	{
		EList<GParameterValue> parameterValues = ((GContainer) owner).gGetParameterValues();
		IGenericFactory genericFactory = MMSRegistry.INSTANCE.getGenericFactory(owner);

		if (genericFactory != null)
		{
			GParameterValue parameterValue;
			parameterValue = genericFactory.createParameterValueWithDefault(paramDef);
			if (parameterValue != null)
			{
				parameterValues.add(parameterValue);
			}
			else
			{
				parameterValue = genericFactory.createParameterValue(paramDef);
				parameterValues.add(parameterValue);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.synchronizer.internal.modulechanger.AbstractModuleChanger#getOwner()
	 */
	@Override
	public EObject getEcucItem()
	{
		return owner;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ecuc.workspace.sync.changers.AbstractEcucItemChanger#copy(java.lang.String)
	 */
	@Override
	public AbstractEcucItemChanger copy()
	{
		return this;
	}

}

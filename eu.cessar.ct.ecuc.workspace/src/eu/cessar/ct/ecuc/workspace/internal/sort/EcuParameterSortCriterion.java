/**
 * 
 */
package eu.cessar.ct.ecuc.workspace.internal.sort;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.workspace.sort.DirectionalSortCriterion;
import eu.cessar.ct.workspace.sort.ESortDirection;
import eu.cessar.ct.workspace.sort.IDirectionalSortCriterion;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GConfigParameter;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;

/**
 * @author uidl6458
 * 
 */
public class EcuParameterSortCriterion extends AbstractEcuSortCriterion
{

	private final GConfigParameter parameterDef;

	/**
	 * @param sortTarget
	 */
	public EcuParameterSortCriterion(final ContainerDefSortTarget sortTarget,
		final GConfigParameter parameterDef)
	{
		super(sortTarget);
		// TODO Auto-generated constructor stub
		this.parameterDef = parameterDef;
		setLabel(parameterDef.gGetShortName());
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.sort.ISortCriterion#createDirectionalSortCriterion(org.autosartools.general.core.sort.ESortDirection)
	 */
	public IDirectionalSortCriterion createDirectionalSortCriterion(final ESortDirection direction)
	{
		return new DirectionalSortCriterion(this, direction);
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.sort.ISortCriterion#getObjectToCompare(org.eclipse.emf.ecore.EObject)
	 */
	public Object getObjectToCompare(final EObject parent)
	{
		if (parent instanceof GContainer)
		{
			GContainer cnt = (GContainer) parent;
			Iterator<GParameterValue> iterator = cnt.gGetParameterValues().iterator();
			IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(parent);
			IEcucMMService ecucService = mmService.getEcucMMService();
			while (iterator.hasNext())
			{
				GParameterValue parameterValue = iterator.next();
				if (parameterDef.equals(parameterValue.gGetDefinition()))
				{
					return ecucService.getParameterValue(parameterValue);
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.workspace.sort.ISortCriterion#getTypeName()
	 */
	public String getTypeName()
	{
		if (parameterDef == null)
		{
			return "";
		}
		return parameterDef.gGetShortName();
	}

}

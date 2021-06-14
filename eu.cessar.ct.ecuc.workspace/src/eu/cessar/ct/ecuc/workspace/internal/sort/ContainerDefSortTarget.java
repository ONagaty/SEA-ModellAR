/**
 *
 */
package eu.cessar.ct.ecuc.workspace.internal.sort;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.IModelChangeMonitor;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.util.DelegatingSubEList;
import eu.cessar.ct.core.platform.util.IModelChangeStampProvider;
import eu.cessar.ct.workspace.sort.AbstractSortTarget;
import eu.cessar.ct.workspace.sort.DescriptionAttributeSortCriterion;
import eu.cessar.ct.workspace.sort.ISortCriterion;
import eu.cessar.ct.workspace.sort.LongNameAttributeSortCriterion;
import eu.cessar.ct.workspace.sort.ShortNameAttributeSortCriterion;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;

/**
 * @author uidl6458
 *
 */
public class ContainerDefSortTarget extends AbstractSortTarget
{
	private final GParamConfContainerDef cntDef;
	private final GChoiceContainerDef choice;

	private class ContainerSubEList extends DelegatingSubEList<GContainer>
	{

		private final String defQName;

		/**
		 * @param delegatedClass
		 * @param parentList
		 */
		public ContainerSubEList(final EList<? super GContainer> parentList, final String defQName,
			IModelChangeStampProvider changeProvider)
		{
			super(GContainer.class, parentList, changeProvider);
			this.defQName = defQName;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.autosartools.general.core.util.DelegatingSubEList#isDelegatedObject(java.lang.Object)
		 */
		@Override
		protected boolean isDelegatedObject(final Object parentElement)
		{
			if (parentElement instanceof GContainer)
			{
				GContainer cnt = (GContainer) parentElement;
				// TODO
				if (cnt.gGetDefinition() != null
					&& defQName.equals(MetaModelUtils.getAbsoluteQualifiedName(cnt.gGetDefinition())))
				{
					return true;
				}
			}
			return false;
		}
	}

	public ContainerDefSortTarget(final GParamConfContainerDef cntDef, final GChoiceContainerDef choice)
	{
		this.cntDef = cntDef;
		this.choice = choice;
		if (choice != null)
		{
			setLabel(choice.gGetShortName() + "->" + cntDef.gGetShortName());
		}
		else
		{
			setLabel(cntDef.gGetShortName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.autosartools.general.core.internal.sort.AbstractSortTarget#doGetSortCriteria()
	 */
	@Override
	protected List<ISortCriterion> doGetSortCriteria()
	{
		List<ISortCriterion> result = new ArrayList<ISortCriterion>();
		// first add the short name as possible sorting criteria
		IMetaModelService service = MMSRegistry.INSTANCE.getMMService(cntDef.eClass());
		result.add(new ShortNameAttributeSortCriterion(this));
		result.add(new LongNameAttributeSortCriterion(this));
		result.add(new DescriptionAttributeSortCriterion(this));
		Iterator<GConfigParameter> iterator = cntDef.gGetParameters().iterator();
		while (iterator.hasNext())
		{
			GConfigParameter parameter = iterator.next();
			BigInteger upperMultiplicity = service.getEcucMMService().getUpperMultiplicity(parameter, BigInteger.ONE,
				true);
			if (upperMultiplicity.compareTo(BigInteger.ONE) == 0)
			{
				result.add(new EcuParameterSortCriterion(this, parameter));
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.autosartools.general.core.sort.ISortTarget#getObjectsToSort(org.eclipse.emf.ecore.EObject)
	 */
	public EList<EObject> getObjectsToSort(final EObject parent)
	{
		String qName = MetaModelUtils.getAbsoluteQualifiedName(cntDef);
		EList<?> result = new ContainerSubEList(getAllSortableObjects(parent), qName,
			IModelChangeMonitor.INSTANCE.getChangeStampProvider(cntDef));
		return (EList<EObject>) result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.sort.ISortTarget#getAllSortableObjects(org.eclipse.emf.ecore.EObject)
	 */
	public EList<EObject> getAllSortableObjects(EObject parent)
	{
		EList<?> containers;
		if (parent instanceof GModuleConfiguration)
		{
			containers = ((GModuleConfiguration) parent).gGetContainers();
		}
		else
		{
			containers = ((GContainer) parent).gGetSubContainers();
		}
		return (EList<EObject>) containers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.sort.ISortTarget#getGroupName(org.eclipse.emf.ecore.EObject)
	 */
	public String getGroupName(EObject candidate)
	{
		if (candidate instanceof GContainer)
		{
			GContainerDef def = ((GContainer) candidate).gGetDefinition();
			if (def != null && !def.eIsProxy())
			{
				return "" + def.gGetShortName();
			}
		}
		return "null";
	}

}

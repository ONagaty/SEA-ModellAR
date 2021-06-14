/**
 *
 */
package eu.cessar.ct.ecuc.workspace.internal.sort;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import eu.cessar.ct.workspace.sort.AbstractSortProviderImpl;
import eu.cessar.ct.workspace.sort.IDirectionalSortCriterion;
import eu.cessar.ct.workspace.sort.ISortTarget;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;

/**
 * @author uidl6458
 *
 */
public abstract class AbstractEcuSortProviderImpl extends AbstractSortProviderImpl
{

	/**
	 * @param domain
	 * @param parentObject
	 */
	public AbstractEcuSortProviderImpl(final TransactionalEditingDomain domain, final EObject parentObject)
	{
		super(domain, parentObject);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.autosartools.general.core.internal.sort.AbstractSortProviderImpl#doGetSortTargets()
	 */
	@Override
	protected List<ISortTarget> doGetSortTargets()
	{
		List<ISortTarget> result = new ArrayList<>();
		for (GParamConfContainerDef def: getContainerDefinitions())
		{
			result.add(new ContainerDefSortTarget(def, null));
		}
		for (GChoiceContainerDef def: getChoiceContainerDefinitions())
		{
			EList<EObject> eContents = def.eContents();
			for (EObject childContDef: eContents)
			{
				if (childContDef instanceof GParamConfContainerDef)
				{
					result.add(new ContainerDefSortTarget((GParamConfContainerDef) childContDef, def));
				}
			}
		}
		return result;
	}

	/**
	 * @return
	 */
	protected abstract List<GParamConfContainerDef> getContainerDefinitions();

	/**
	 * @return
	 */
	protected abstract List<GChoiceContainerDef> getChoiceContainerDefinitions();

	/*
	 * (non-Javadoc)
	 *
	 * @see org.autosartools.general.core.sort.ISortProvider#performSort(org.autosartools.general.core.sort.ISortTarget,
	 * java.util.List, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void performSort(final ISortTarget target, final List<IDirectionalSortCriterion> criteria, boolean isGrouped,
		final IProgressMonitor monitor) throws OperationCanceledException, ExecutionException
	{
		super.performSort(target, criteria, isGrouped, monitor);
	}
}

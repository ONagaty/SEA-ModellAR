package eu.cessar.ct.ecuc.workspace.internal.sync.problems;

import eu.cessar.ct.ecuc.workspace.internal.SynchronizeConstants;
import eu.cessar.ct.ecuc.workspace.sync.changers.AbstractEcucItemChanger;
import eu.cessar.ct.ecuc.workspace.sync.changers.DeleteEcucItem;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

public class DeleteProblem extends AbstractProblem
{
	private GIdentifiable elemConf;

	/**
	 * @param elemDef
	 * @param elemConf
	 */
	public DeleteProblem(GIdentifiable elemConf)
	{
		this.elemConf = elemConf;
	}

	@Override
	public String getDescription()
	{
		return SynchronizeConstants.deleteItemDescr;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.synchronizer.internal.problems.AbstractProblem#getChangers()
	 */
	@Override
	public List<AbstractEcucItemChanger> getChangers()
	{
		List<AbstractEcucItemChanger> listOfChangers = new ArrayList<AbstractEcucItemChanger>();
		listOfChangers.add(new DeleteEcucItem(elemConf));
		return listOfChangers;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.synchronizer.internal.problems.AbstractProblem#getActionType()
	 */
	@Override
	public String getProblemType()
	{
		return SynchronizeConstants.problemDelType;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.synchronizer.internal.problems.AbstractProblem#getElement()
	 */
	@Override
	public EObject getElement()
	{
		return elemConf;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem#getAffectedElement()
	 */
	@Override
	public EObject getAffectedElement()
	{
		// TODO Auto-generated method stub
		return elemConf;
	}

}

package eu.cessar.ct.ecuc.workspace.internal.sync.problems;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;

import eu.cessar.ct.ecuc.workspace.sync.changers.AbstractEcucItemChanger;
import eu.cessar.ct.sdk.utils.ModelUtils;
import gautosar.gecucdescription.GParameterValue;

/**
 * @author uidt2045 <br>
 *         This is a wrapper for the AbstractModuleChangers
 * 
 */
public abstract class AbstractProblem
{
	/**
	 * @throws ExecutionException
	 * @throws OperationCanceledException
	 * 
	 */
	public void solve() throws OperationCanceledException, ExecutionException
	{
		List<AbstractEcucItemChanger> listOfChangers = getChangers();
		for (AbstractEcucItemChanger changer: listOfChangers)
		{
			TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(EcorePlatformUtil.getResource(ModelUtils.getDefiningFile(changer.getEcucItem())));
			WorkspaceTransactionUtil.executeInWriteTransaction(editingDomain, changer,
				changer.getActionDescription());
		}
	}

	/**
	 * Get a short description for the problem
	 * 
	 * @return
	 */
	public abstract String getDescription();

	/**
	 * @return
	 */
	public abstract List<AbstractEcucItemChanger> getChangers();

	/**
	 * @return returns the type of the problem
	 */
	public abstract String getProblemType();

	/**
	 * this method is for GUI usage
	 * 
	 * @return returns the element given to the problem. <br>
	 *         example: the delete problem returns the element to be deleted,
	 *         and when there is a multiplicity problem, which is solved by a
	 *         create action, it will return the definition of the element which
	 *         will be created.
	 * 
	 */
	public abstract EObject getElement();

	/**
	 * @return returns the element which will be affected by the problem
	 *         solving. So it is from the configuration
	 */
	public abstract EObject getAffectedElement();

	/* 
	 * A problem equals another problem if they have the same affected element, they return the same getElement(), 
	 * 
	 */
	public boolean equals(AbstractProblem obj)
	{
		if (this.getAffectedElement().equals(obj.getAffectedElement())
			&& (this.getElement().equals(obj.getElement())))
		{
			return true;
		}
		return false;
	}

	/**
	 * Format qualified name with parameter name
	 * 
	 * @return
	 */
	public String getQualifiedNameString()
	{
		EObject element = getElement();
		if (element instanceof GParameterValue)
		{
			String s = ModelUtils.getAbsoluteQualifiedName(element);
			String r = s.substring(0, s.lastIndexOf("/")) + "/"
				+ ((GParameterValue) element).gGetDefinition().gGetShortName();
			return r;
		}
		else
		{
			return ModelUtils.getAbsoluteQualifiedName(element);
		}
	}

}

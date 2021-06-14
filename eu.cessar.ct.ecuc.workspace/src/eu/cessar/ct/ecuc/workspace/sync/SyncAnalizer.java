package eu.cessar.ct.ecuc.workspace.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem;
import eu.cessar.ct.ecuc.workspace.internal.sync.problems.MultiplicityProblem;
import eu.cessar.ct.ecuc.workspace.internal.sync.rules.AbstractRule;
import eu.cessar.ct.ecuc.workspace.internal.sync.rules.DefinitionExistRule;
import eu.cessar.ct.ecuc.workspace.internal.sync.rules.EnumRule;
import eu.cessar.ct.ecuc.workspace.internal.sync.rules.MultiplicityRule;
import eu.cessar.ct.ecuc.workspace.internal.sync.rules.ValitationResult;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

public class SyncAnalizer
{
	private ArrayList<AbstractRule> listOfRules;
	private Map<String, Object> options;

	public SyncAnalizer()
	{
		listOfRules = new ArrayList<AbstractRule>();

		listOfRules.add(new DefinitionExistRule());
		listOfRules.add(new MultiplicityRule());
		listOfRules.add(new EnumRule());
	}

	/**
	 * It's main purpose is to 'fix' the problems given in the list. <br>
	 * Solution: Calls the method <code>solve</code> on each of the <code>Problem</code> objects given in the list.
	 *
	 * @throws ExecutionException
	 * @throws OperationCanceledException
	 */
	public void synchronize(List<AbstractProblem> initialList, List<AbstractProblem> list)
		throws OperationCanceledException, ExecutionException
	{
		List<GIdentifiable> listOfProblemObjects = new ArrayList<GIdentifiable>();
		for (AbstractProblem problem: list)
		{
			problem.solve();
			if (problem.getElement() instanceof GIdentifiable)
			{
				if (!listOfProblemObjects.contains(problem.getAffectedElement()))
				{
					listOfProblemObjects.add((GIdentifiable) problem.getAffectedElement());
				}
			}
		}
		makeRecursiveActions(initialList, list, listOfProblemObjects);
	}

	/**
	 * @param list
	 * @param listOfProblemObjects
	 * @throws ExecutionException
	 * @throws OperationCanceledException
	 */
	private void makeRecursiveActions(List<AbstractProblem> initialList, List<AbstractProblem> oldList,
		List<GIdentifiable> newListOfProblemObjects) throws OperationCanceledException, ExecutionException
	{
		List<AbstractProblem> newList = new ArrayList<AbstractProblem>();
		List<AbstractProblem> problemsToBeIgnored = new ArrayList<AbstractProblem>();
		for (GIdentifiable identif: newListOfProblemObjects)
		{
			analyze(identif, options, newList, problemsToBeIgnored);
		}

		// newList.removeAll(oldList);
		removeDuplicatesFromNewList(newList);
		removeFromNewListOldProblems(newList, oldList);
		removeFromNewListOldProblems(newList, initialList);
		if (newList.isEmpty())
		{
			// stop the recursiveness
		}
		else
		{
			synchronize(initialList, newList);
		}
	}

	/**
	 * @param newList
	 */
	private void removeDuplicatesFromNewList(List<AbstractProblem> newList)
	{
		for (int i = 0; i < newList.size(); i++)
		{
			for (int j = i + 1; j < newList.size(); j++)
			{
				if (newList.get(i).equals(newList.get(j)))
				{
					newList.remove(j);
					j--;
				}
			}
		}
	}

	/**
	 * @param newList
	 * @param oldList
	 */
	private void removeFromNewListOldProblems(List<AbstractProblem> newList, List<AbstractProblem> oldList)
	{
		for (AbstractProblem oldProblem: oldList)
		{
			for (int i = 0; i < newList.size(); i++)
			{
				if (newList.get(i).equals(oldProblem))
				{
					newList.remove(i);
				}
			}
		}
	}

	protected void addRule(AbstractRule rule)
	{
		if (listOfRules != null)
		{
			listOfRules.add(rule);
		}
	}

	protected void removeRule(AbstractRule rule)
	{
		listOfRules.remove(rule);
	}

	/**
	 * This method applies the rules from <code>listOfRules</code> to the given list of <code>GIdentifiables</code> and
	 * returns a ValidationResult object which contains a list of problems( <code>.getListOfProblems()</code>) if there
	 * are any or an empty list if the rule passed
	 *
	 * @return
	 */
	public void analyze(GIdentifiable confItem, Map<String, Object> options, List<AbstractProblem> problems,
		List<AbstractProblem> problemsToBeIgnored)
	{
		boolean goDeeper = true;
		this.options = options;

		for (AbstractRule rule: listOfRules)
		{

			ValitationResult validResult = rule.validate(confItem, options);
			if (validResult != null)
			{
				// this means that the rule did not pass
				// if (validResult.getListOfProblems() != null)
				if (!validResult.getListOfProblems().isEmpty())
				{

					List<AbstractProblem> listOfProblems = validResult.getListOfProblems();

					for (AbstractProblem abstractProblem: listOfProblems)
					{

						if (abstractProblem instanceof MultiplicityProblem)
						{
							if (abstractProblem.getElement() instanceof GIdentifiable)
							{
								EObject element = abstractProblem.getElement();

								if (element instanceof GConfigParameter)
								{

									GConfigParameter configParameter = (GConfigParameter) element;

									IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(
										configParameter.eClass());
									if (mmService != null)
									{
										IEcucMMService ecucMMService = mmService.getEcucMMService();
										Object defaultValue = ecucMMService.getDefaultValue(configParameter);

										if (ecucMMService.isSetDefaultValue(configParameter)
											&& (defaultValue != null && defaultValue != "")) //$NON-NLS-1$
										{

											problems.add(abstractProblem);
											if (!validResult.canGoDeeper())
											{
												goDeeper = false;
											}
											if (!validResult.shouldContinue())
											{
												// STOP CHECKING THE OTHER RULES
												break;
											}
										}
										else
										{

											// if no default value found inside the definition, then the
											// problem will be added to this list.

											problemsToBeIgnored.add(abstractProblem);

										}
									}
								}

							}
						}
						else
						{
							problems.add(abstractProblem);
							if (!validResult.canGoDeeper())
							{
								goDeeper = false;
							}
							if (!validResult.shouldContinue())
							{
								// STOP CHECKING THE OTHER RULES
								break;
							}
						}
					}

				}
			}
			{
				// this means that the rule is not applicable
			}
		}
		if (goDeeper)
		{
			// the rules should be applicable to the children
			ArrayList<GIdentifiable> instanceChildren = getChildren(confItem);
			for (GIdentifiable instanceChild: instanceChildren)
			{
				if (instanceChild instanceof GContainer)
				{
					analyze(instanceChild, options, problems, problemsToBeIgnored);
				}
			}
		}
	}

	/**
	 * @param confItem
	 * @return a list of the children of the config instance that is verified with the rules
	 */
	private ArrayList<GIdentifiable> getChildren(GIdentifiable item)
	{
		ArrayList<GIdentifiable> children = new ArrayList<GIdentifiable>();

		for (EObject child: item.eContents())
		{

			if (child instanceof GIdentifiable)
			{

				children.add((GIdentifiable) child);
			}
			if (child instanceof GParameterValue)
			{

				// need to find a solution when I need to check the
				// parameterValues if they have a correct definition
			}
		}
		return children;
	}

}

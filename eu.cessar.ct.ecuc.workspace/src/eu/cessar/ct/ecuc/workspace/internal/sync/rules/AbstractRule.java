package eu.cessar.ct.ecuc.workspace.internal.sync.rules;

import eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractRule
{

	/**
	 * Checks if the given object satisfies the conditions of this rule
	 * 
	 * @param object
	 * 
	 * @return
	 */
	public abstract boolean passesRule(GIdentifiable objConf, Map<String, Object> options);

	/**
	 * returns an short description of the purpose of this rule
	 * 
	 * @return
	 */
	public abstract String getRuleDescription();

	/**
	 * checks if this rule is applicable to the given object
	 * 
	 * @param object
	 * @return
	 */
	public abstract boolean isAplicableTo(GIdentifiable objConf);

	/**
	 * will return a ValidationResult object, (where you can call getListOfProblems that returns a list of problems) if
	 * the given parameters don't pass the rule.<br>
	 * Or an ValidationResult object, where when calling getListOfProblems an empty list is returned, if they pass the
	 * rule.<br>
	 * Or Null if the given parameters are not applicable <br>
	 * 
	 * @param objDef
	 * @param objConf
	 * @return
	 */
	public ValitationResult validate(GIdentifiable objConf, Map<String, Object> options)
	{
		if (isAplicableTo(objConf))
		{
			Map<String, Object> specificOptions = createSpecificOptions(options);
			if (!passesRule(objConf, specificOptions))
			{
				return new ValitationResult(objConf, getDeepFlag(), getBlockingType(), configureProblems(objConf,
					specificOptions));
			}
			return new ValitationResult(objConf, getDeepFlag(), getBlockingType(), new ArrayList<AbstractProblem>());
		}
		return null;
	}

	/**
	 * returns the flag that determines: if the rule did not pass... should you go deep?
	 */
	protected abstract Boolean getDeepFlag();

	/**
	 * returns the flag that determines: if the rule did not pass... should you continue with the other rules?
	 * 
	 */
	protected abstract Boolean getBlockingType();

	/**
	 * @param options
	 * @return
	 */
	protected abstract Map<String, Object> createSpecificOptions(Map<String, Object> options);

	/**
	 * this method will configure the problems with the correct parameters... and should be called from getProblems,
	 * only if there are problems(if passesRule() returns false)
	 */
	protected abstract List<AbstractProblem> configureProblems(GIdentifiable elemConf, Map<String, Object> options);

}

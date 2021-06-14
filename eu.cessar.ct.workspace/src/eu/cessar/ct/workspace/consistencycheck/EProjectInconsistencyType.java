/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 03.08.2012 11:05:09 </copyright>
 */
package eu.cessar.ct.workspace.consistencycheck;

/**
 * Enumeration for project inconsistency types
 * 
 * @author uidl6870
 * 
 */
public enum EProjectInconsistencyType
{
	/**
	 *
	 */
	NONE("No checkings"), //$NON-NLS-1$

	/**
	 *
	 */
	DUPLICATE_MODULE_DEF("Duplicate module definition"), //$NON-NLS-1$

	/**
	 *
	 */
	WRONG_MODEL("Wrong metamodel"), //$NON-NLS-1$

	/**
	 *
	 */
	JET_PROBLEMS("Jet Problems"), //$NON-NLS-1$

	/**
	 * 
	 */
	PROJECT_PROBLEMS(".project Problems"), //$NON-NLS-1$

	/**
	 * 
	 */
	CLASSPATH_PROBLEMS(".classpath Problems"), //$NON-NLS-1$

	/**
	 * 
	 */
	SETTINGS_PROBLEMS(".settings Problems"), //$NON-NLS-1$
	/**
	 * 
	 */
	PMBIN_PROBLEMS("pmbin Problems");//$NON-NLS-1$

	private final String name;

	EProjectInconsistencyType(String name)
	{
		this.name = name;
	}

	/**
	 * Returns the corresponding name of the literal.
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Gets the project consistency type from name.
	 * 
	 * @param name
	 *        the name
	 * @return the project consistency type from name
	 */
	public static EProjectInconsistencyType getProjectConsistencyTypeFromName(String name)
	{
		if (name == null)
		{
			return null;
		}
		if (name.equals(EProjectInconsistencyType.DUPLICATE_MODULE_DEF.getName()))
		{
			return EProjectInconsistencyType.DUPLICATE_MODULE_DEF;
		}
		if (name.equals(EProjectInconsistencyType.WRONG_MODEL.getName()))
		{
			return EProjectInconsistencyType.WRONG_MODEL;
		}
		if (name.equals(EProjectInconsistencyType.JET_PROBLEMS.getName()))
		{
			return EProjectInconsistencyType.JET_PROBLEMS;
		}
		if (name.equals(EProjectInconsistencyType.PROJECT_PROBLEMS.getName()))
		{
			return EProjectInconsistencyType.PROJECT_PROBLEMS;
		}
		if (name.equals(EProjectInconsistencyType.CLASSPATH_PROBLEMS.getName()))
		{
			return EProjectInconsistencyType.CLASSPATH_PROBLEMS;
		}
		if (name.equals(EProjectInconsistencyType.SETTINGS_PROBLEMS.getName()))
		{
			return EProjectInconsistencyType.SETTINGS_PROBLEMS;
		}
		if (name.equals(EProjectInconsistencyType.PMBIN_PROBLEMS.getName()))
		{
			return EProjectInconsistencyType.PMBIN_PROBLEMS;
		}
		if (name.equals(EProjectInconsistencyType.NONE.getName()))
		{
			return EProjectInconsistencyType.NONE;
		}
		return null;
	}
}

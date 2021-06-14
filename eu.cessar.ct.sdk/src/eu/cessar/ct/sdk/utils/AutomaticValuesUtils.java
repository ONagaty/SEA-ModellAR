/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Oct 14, 2009 4:56:45 PM </copyright>
 */
package eu.cessar.ct.sdk.utils;

import java.math.BigInteger;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;

import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.sdk.autofill.EAutomaticValuesFillType;
import eu.cessar.ct.sdk.autofill.IAlgorithm;
import eu.cessar.ct.sdk.autofill.IContainerFragmentFilter;
import eu.cessar.ct.sdk.pm.IPMContainer;

/**
 * 
 * Utilities for automatic fill of values.
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Tue Feb 11 16:00:46 2014 %
 * 
 *         %version: 3 %
 */
public final class AutomaticValuesUtils
{

	private static final Service SERVICE = PlatformUtils.getService(Service.class);

	private AutomaticValuesUtils()
	{
		// avoid instance
	}

	/**
	 * The Service part of the public SDK, do not use it directly, not part of the public API
	 * 
	 * @author uidw8762
	 * 
	 *         %created_by: uidl6870 %
	 * 
	 *         %date_created: Tue Feb 11 16:00:46 2014 %
	 * 
	 *         %version: 3 %
	 */
	public static interface Service
	{
		@SuppressWarnings("javadoc")
		public IStatus fillIntParameters(IProject project, String containerDefinitionQualifiedName,
			String parameterName, IAlgorithm<BigInteger> fillAlgorithm, EAutomaticValuesFillType automaticFillType);

		@SuppressWarnings("javadoc")
		public IStatus fillIntParameters(IProject project, String moduleConfigurationQualifiedName,
			String containerDefinitionQualifiedName, String parameterName, IAlgorithm<BigInteger> fillAlgorithm,
			EAutomaticValuesFillType automaticFillType, IContainerFragmentFilter elementFilter);

		@SuppressWarnings("javadoc")
		public IStatus pmFillIntParameters(EList<? extends IPMContainer> containerList, String parameterName,
			IAlgorithm<BigInteger> fillAlgorithm, EAutomaticValuesFillType automaticFillType);

		/** Float parameters */

		@SuppressWarnings("javadoc")
		public IStatus fillFloatParameters(IProject project, String containerDefinitionQualifiedName,
			String parameterName, IAlgorithm<Double> fillAlgorithm, EAutomaticValuesFillType automaticFillType);

		@SuppressWarnings("javadoc")
		public IStatus fillFloatParameters(IProject project, String moduleConfigurationQualifiedName,
			String containerDefinitionQualifiedName, String parameterName, IAlgorithm<Double> fillAlgorithm,
			EAutomaticValuesFillType automaticFillType, IContainerFragmentFilter elementFilter);

		@SuppressWarnings("javadoc")
		public IStatus pmFillFloatParameters(EList<? extends IPMContainer> containerList, String parameterName,
			IAlgorithm<Double> fillAlgorithm, EAutomaticValuesFillType automaticFillType);

		/** String parameters */

		@SuppressWarnings("javadoc")
		public IStatus fillStringParameters(IProject project, String containerDefinitionQualifiedName,
			String parameterName, IAlgorithm<String> fillAlgorithm, EAutomaticValuesFillType automaticFillType);

		@SuppressWarnings("javadoc")
		public IStatus fillStringParameters(IProject project, String moduleConfigurationQualifiedName,
			String containerDefinitionQualifiedName, String parameterName, IAlgorithm<String> fillAlgorithm,
			EAutomaticValuesFillType automaticFillType, IContainerFragmentFilter elementFilter);

		@SuppressWarnings("javadoc")
		public IStatus pmFillStringParameters(EList<? extends IPMContainer> containerList, String parameterName,
			IAlgorithm<String> fillAlgorithm, EAutomaticValuesFillType automaticFillType);

		/** Boolean parameters */

		@SuppressWarnings("javadoc")
		public IStatus fillBooleanParameters(IProject project, String containerDefinitionQualifiedName,
			String parameterName, IAlgorithm<Boolean> fillAlgorithm, EAutomaticValuesFillType automaticFillType);

		@SuppressWarnings("javadoc")
		public IStatus fillBooleanParameters(IProject project, String moduleConfigurationQualifiedName,
			String containerDefinitionQualifiedName, String parameterName, IAlgorithm<Boolean> fillAlgorithm,
			EAutomaticValuesFillType automaticFillType, IContainerFragmentFilter elementFilter);

		@SuppressWarnings("javadoc")
		public IStatus pmFillBooleanParameters(EList<? extends IPMContainer> containerList, String parameterName,
			IAlgorithm<Boolean> fillAlgorithm, EAutomaticValuesFillType automaticFillType);

	}

	/** Automatic values fill for the Integer types */

	/**
	 * Fills the parameter with the specified name using the given algorithm and automatic fill type strategy, in the
	 * containers that are instances of the definition given by its qualified name.
	 * <p>
	 * <br>
	 * <strong>NOTE</strong>: in case there is <b>no such container definition</b> or <b>no such parameter defined</b>,
	 * an unchecked exception will be raised.<br>
	 * 
	 * In case of multiple configurations for the module definition holding the container definition specified by its
	 * qualified name, the first first one in alphabetical order will be used.
	 * 
	 * In case of <b>split containers</b>, the parameter will be set in the first fragment in the alphabetical order
	 * considering the containing resource name. In order to change this behavior, simply use
	 * {@link #fillIntParameters(IProject, String, String, String, IAlgorithm, EAutomaticValuesFillType, IContainerFragmentFilter)}.
	 * 
	 * @param project
	 *        the CT project
	 * @param containerDefinitionQualifiedName
	 *        the qualified name of the container definition whose instances are candidates for auto-filling.
	 * @param parameterName
	 *        the short name of the parameter to be filled
	 * @param fillAlgorithm
	 *        the algorithm providing the values for auto-filling
	 * @param automaticFillType
	 *        the auto-fill type strategy to be used. The possible values are: <b>FILL_ALL</b>, <b>FILL_SET</b>,
	 *        <b>FILL_NOT_SET</b>.
	 * @return the status of the operation
	 */
	public static IStatus fillIntParameters(IProject project, String containerDefinitionQualifiedName,
		String parameterName, IAlgorithm<BigInteger> fillAlgorithm, EAutomaticValuesFillType automaticFillType)
	{
		return SERVICE.fillIntParameters(project, containerDefinitionQualifiedName, parameterName, fillAlgorithm,
			automaticFillType);
	}

	/**
	 * Fills the parameter with the specified name using the given algorithm and automatic fill type strategy, in the
	 * containers that are instances of the definition given by its qualified name and belong to the specified module
	 * configuration, and are accepted by the provided filter.
	 * <p>
	 * <br>
	 * <strong>NOTE</strong>: in case there is <b>no such container definition</b> or <b>no such parameter defined</b>,
	 * an unchecked exception will be raised.
	 * 
	 * @param project
	 *        the CT project
	 * @param moduleConfigurationQualifiedName
	 *        the qualified name of the module configuration
	 * @param containerDefinitionQualifiedName
	 *        the qualified name of the container definition whose instances are candidates for auto-filling.
	 * @param parameterName
	 *        the short name of the parameter to be filled
	 * @param fillAlgorithm
	 *        the algorithm providing the values for auto-filling
	 * @param automaticFillType
	 *        the auto-fill type strategy to be used<br>
	 * @param containerFilter
	 *        the user implemented filter for the instances of the given container definition that satisfy the chosen
	 *        auto-fill type strategy. In case of split containers, the filter allows choosing the container fragment to
	 *        be filled
	 * @return the status of the operation
	 */
	public static IStatus fillIntParameters(IProject project, String moduleConfigurationQualifiedName,
		String containerDefinitionQualifiedName, String parameterName, IAlgorithm<BigInteger> fillAlgorithm,
		EAutomaticValuesFillType automaticFillType, IContainerFragmentFilter containerFilter)
	{
		return SERVICE.fillIntParameters(project, moduleConfigurationQualifiedName, containerDefinitionQualifiedName,
			parameterName, fillAlgorithm, automaticFillType, containerFilter);
	}

	/**
	 * Works just like {@link #fillIntParameters(IProject, String, String, IAlgorithm, EAutomaticValuesFillType)}, the
	 * only difference being that it receives {@code containersList}, a list of <b>IPMContainer</b>s to be filled,
	 * instead of the project, the container definition qualified name and parameter name.
	 * 
	 * @see #fillIntParameters(IProject, String, String, IAlgorithm, EAutomaticValuesFillType)
	 * 
	 * @param containerList
	 *        list of {@link IPMContainer} containers that are targets for auto-filling
	 * 
	 */
	@SuppressWarnings("javadoc")
	public static IStatus pmFillIntParameters(EList<? extends IPMContainer> containerList, String parameterName,
		IAlgorithm<BigInteger> fillAlgorithm, EAutomaticValuesFillType automaticFillType)
	{
		return SERVICE.pmFillIntParameters(containerList, parameterName, fillAlgorithm, automaticFillType);
	}

	/** Automatic values fill for the Float types */

	/**
	 * Works just like {@link #fillIntParameters(IProject, String, String, IAlgorithm, EAutomaticValuesFillType)}, the
	 * only difference being that it operates on float parameters, and receives an <b>IAlgorithm&lt;Double&gt;.</b>
	 * 
	 * @see #fillIntParameters(IProject, String, String, IAlgorithm, EAutomaticValuesFillType)
	 * 
	 * @param fillAlgorithm
	 *        the algorithm providing the <b>Double</b> values for auto-filling
	 */
	@SuppressWarnings("javadoc")
	public static IStatus fillFloatParameters(IProject project, String containerDefinitionQualifiedName,
		String parameterName, IAlgorithm<Double> fillAlgorithm, EAutomaticValuesFillType automaticFillType)
	{
		return SERVICE.fillFloatParameters(project, containerDefinitionQualifiedName, parameterName, fillAlgorithm,
			automaticFillType);
	}

	/**
	 * Works just like
	 * {@link #fillIntParameters(IProject, String, String, String, IAlgorithm, EAutomaticValuesFillType, IContainerFragmentFilter)}
	 * , the only difference being that it operates on float parameters, and receives an
	 * <b>IAlgorithm&lt;Double&gt;.</b>
	 * 
	 * @see #fillIntParameters(IProject, String, String, String, IAlgorithm, EAutomaticValuesFillType,
	 *      IContainerFragmentFilter)
	 * 
	 * @param fillAlgorithm
	 *        the algorithm providing the <b>Double</b> values for auto-filling
	 */
	@SuppressWarnings("javadoc")
	public static IStatus fillFloatParameters(IProject project, String moduleConfigurationQualifiedName,
		String containerDefinitionQualifiedName, String parameterName, IAlgorithm<Double> fillAlgorithm,
		EAutomaticValuesFillType automaticFillType, IContainerFragmentFilter containerFilter)
	{
		return SERVICE.fillFloatParameters(project, moduleConfigurationQualifiedName, containerDefinitionQualifiedName,
			parameterName, fillAlgorithm, automaticFillType, containerFilter);
	}

	/**
	 * Works just like {@link #pmFillIntParameters(EList, String, IAlgorithm, EAutomaticValuesFillType)}, the only
	 * difference being that it operates on float parameters, and receives an <b>IAlgorithm&lt;Double&gt;.</b>
	 * 
	 * @see #pmFillIntParameters(EList, String, IAlgorithm, EAutomaticValuesFillType)
	 * 
	 * @param containerList
	 *        list of {@link IPMContainer} containers that are targets for auto-filling
	 * @param fillAlgorithm
	 *        the algorithm providing the <b>Double</b> values for auto-filling
	 */
	@SuppressWarnings("javadoc")
	public static IStatus pmFillFloatParameters(EList<? extends IPMContainer> containerList, String parameterName,
		IAlgorithm<Double> fillAlgorithm, EAutomaticValuesFillType automaticFillType)
	{
		return SERVICE.pmFillFloatParameters(containerList, parameterName, fillAlgorithm, automaticFillType);
	}

	/** Automatic values fill for the String types */

	/**
	 * Works just like {@link #fillIntParameters(IProject, String, String, IAlgorithm, EAutomaticValuesFillType)}, the
	 * only difference being that it operates on String parameters, and receives an <b>IAlgorithm&lt;String&gt;.</b>
	 * 
	 * @see #fillIntParameters(IProject, String, String, IAlgorithm, EAutomaticValuesFillType)
	 * 
	 * @param fillAlgorithm
	 *        the algorithm providing the <b>String</b> values for auto-filling
	 */
	@SuppressWarnings("javadoc")
	public static IStatus fillStringParameters(IProject project, String containerDefinitionQualifiedName,
		String parameterName, IAlgorithm<String> fillAlgorithm, EAutomaticValuesFillType automaticFillType)
	{
		return SERVICE.fillStringParameters(project, containerDefinitionQualifiedName, parameterName, fillAlgorithm,
			automaticFillType);
	}

	/**
	 * Works just like
	 * {@link #fillIntParameters(IProject, String, String, String, IAlgorithm, EAutomaticValuesFillType, IContainerFragmentFilter)}
	 * , the only difference being that it operates on String parameters, and receives an
	 * <b>IAlgorithm&lt;String&gt;.</b>
	 * 
	 * @see #fillIntParameters(IProject, String, String, String, IAlgorithm, EAutomaticValuesFillType,
	 *      IContainerFragmentFilter)
	 * 
	 * @param fillAlgorithm
	 *        the algorithm providing the <b>String</b> values for auto-filling
	 */
	@SuppressWarnings("javadoc")
	public static IStatus fillStringParameters(IProject project, String moduleConfigurationQualifiedName,
		String containerDefinitionQualifiedName, String parameterName, IAlgorithm<String> fillAlgorithm,
		EAutomaticValuesFillType automaticFillType, IContainerFragmentFilter containerFilter)
	{
		return SERVICE.fillStringParameters(project, moduleConfigurationQualifiedName,
			containerDefinitionQualifiedName, parameterName, fillAlgorithm, automaticFillType, containerFilter);
	}

	/**
	 * Works just like {@link #pmFillIntParameters(EList, String, IAlgorithm, EAutomaticValuesFillType)}, the only
	 * difference being that it operates on String parameters, and receives an <b>IAlgorithm&lt;String&gt;.</b>
	 * 
	 * @see #pmFillIntParameters(EList, String, IAlgorithm, EAutomaticValuesFillType)
	 * 
	 * @param containerList
	 *        list of {@link IPMContainer} containers that are targets for auto-filling
	 * @param fillAlgorithm
	 *        the algorithm providing the <b>String</b> values for auto-filling
	 */
	@SuppressWarnings("javadoc")
	public static IStatus pmFillStringParameters(EList<? extends IPMContainer> containerList, String parameterName,
		IAlgorithm<String> fillAlgorithm, EAutomaticValuesFillType automaticFillType)
	{
		return SERVICE.pmFillStringParameters(containerList, parameterName, fillAlgorithm, automaticFillType);
	}

	/** Automatic values fill for the Boolean type */

	/**
	 * Works just like {@link #fillIntParameters(IProject, String, String, IAlgorithm, EAutomaticValuesFillType)}, the
	 * only difference being that it operates on Boolean parameters, and receives an <b>IAlgorithm&lt;Boolean&gt;.</b>
	 * 
	 * @see #fillIntParameters(IProject, String, String, IAlgorithm, EAutomaticValuesFillType)
	 * 
	 * @param fillAlgorithm
	 *        the algorithm providing the <b>Boolean</b> values for auto-filling
	 */
	@SuppressWarnings("javadoc")
	public static IStatus fillBooleanParameters(IProject project, String containerDefinitionQualifiedName,
		String parameterName, IAlgorithm<Boolean> fillAlgorithm, EAutomaticValuesFillType automaticFillType)
	{
		return SERVICE.fillBooleanParameters(project, containerDefinitionQualifiedName, parameterName, fillAlgorithm,
			automaticFillType);
	}

	/**
	 * Works just like
	 * {@link #fillIntParameters(IProject, String, String, String, IAlgorithm, EAutomaticValuesFillType, IContainerFragmentFilter)}
	 * , the only difference being that it operates on Boolean parameters, and receives an
	 * <b>IAlgorithm&lt;Boolean&gt;.</b>
	 * 
	 * @see #fillIntParameters(IProject, String, String, String, IAlgorithm, EAutomaticValuesFillType,
	 *      IContainerFragmentFilter)
	 * 
	 * @param fillAlgorithm
	 *        the algorithm providing the <b>Boolean</b> values for auto-filling
	 */
	@SuppressWarnings("javadoc")
	public static IStatus fillBooleanParameters(IProject project, String moduleConfigurationQualifiedName,
		String containerDefinitionQualifiedName, String parameterName, IAlgorithm<Boolean> fillAlgorithm,
		EAutomaticValuesFillType automaticFillType, IContainerFragmentFilter containerFilter)
	{
		return SERVICE.fillBooleanParameters(project, moduleConfigurationQualifiedName,
			containerDefinitionQualifiedName, parameterName, fillAlgorithm, automaticFillType, containerFilter);
	}

	/**
	 * Works just like {@link #pmFillIntParameters(EList, String, IAlgorithm, EAutomaticValuesFillType)}, the only
	 * difference being that it operates on Boolean parameters, and receives an <b>IAlgorithm&lt;Boolean&gt;.</b>
	 * 
	 * @see #pmFillIntParameters(EList, String, IAlgorithm, EAutomaticValuesFillType)
	 * 
	 * @param containerList
	 *        list of {@link IPMContainer} containers that are targets for auto-filling
	 * @param fillAlgorithm
	 *        the algorithm providing the <b>Boolean</b> values for auto-filling
	 */
	@SuppressWarnings("javadoc")
	public static IStatus pmFillBooleanParameters(EList<? extends IPMContainer> containerList, String parameterName,
		IAlgorithm<Boolean> fillAlgorithm, EAutomaticValuesFillType automaticFillType)
	{
		return SERVICE.pmFillBooleanParameters(containerList, parameterName, fillAlgorithm, automaticFillType);
	}
}

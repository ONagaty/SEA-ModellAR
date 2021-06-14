/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870<br/>
 * Dec 10, 2013 10:30:10 AM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.autofill;

import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;

import eu.cessar.ct.runtime.ecuc.internal.CessarPluginActivator;
import eu.cessar.ct.sdk.autofill.EAutomaticValuesFillType;
import eu.cessar.ct.sdk.autofill.IAlgorithm;
import eu.cessar.ct.sdk.autofill.IContainerFragmentFilter;
import eu.cessar.ct.sdk.collections.Wrapper;
import eu.cessar.ct.sdk.pm.IPMContainer;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAInstanceReference;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucparameterdef.GAbstractStringParamDef;
import gautosar.gecucparameterdef.GEnumerationParamDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 * Implementation of the AutomaticValuesUtils from SDK
 *
 * @author uidl6870
 *
 *         %created_by: uidu2337 %
 *
 *         %date_created: Mon Feb 10 12:18:04 2014 %
 *
 *         %version: 15 %
 */
@Requirement(
	reqID = "REQ_API_AUTOFILL#1")
public final class SDKAutofillUtilsService implements eu.cessar.ct.sdk.utils.AutomaticValuesUtils.Service
{

	/** the singleton */
	public static final SDKAutofillUtilsService INSTANCE = new SDKAutofillUtilsService();

	private SDKAutofillUtilsService()
	{
		// hide
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.AutomaticValuesUtils.Service#fillIntParameters(org.eclipse.core.resources.IProject,
	 * java.lang.String, java.lang.String, eu.cessar.ct.sdk.utils.IAlgorithm,
	 * eu.cessar.ct.sdk.utils.EAutomaticValuesFillType)
	 */
	@Override
	public IStatus fillIntParameters(IProject project, String containerDefinitionQualifiedName, String parameterName,
		IAlgorithm<BigInteger> fillAlgorithm, EAutomaticValuesFillType automaticFillType)
	{
		return doFillParameters(project, null, containerDefinitionQualifiedName, parameterName, fillAlgorithm,
			automaticFillType, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.AutomaticValuesUtils.Service#fillIntParameters(org.eclipse.core.resources.IProject,
	 * java.lang.String, java.lang.String, eu.cessar.ct.sdk.utils.IElementFilter, eu.cessar.ct.sdk.utils.IAlgorithm,
	 * eu.cessar.ct.sdk.utils.EAutomaticValuesFillType)
	 */
	@Override
	public IStatus fillIntParameters(IProject project, String moduleConfigurationQualifiedName,
		String containerDefinitionQualifiedName, String parameterName, IAlgorithm<BigInteger> fillAlgorithm,
		EAutomaticValuesFillType automaticFillType, IContainerFragmentFilter containerFragmentFilter)
	{
		return doFillParameters(project, moduleConfigurationQualifiedName, containerDefinitionQualifiedName,
			parameterName, fillAlgorithm, automaticFillType, containerFragmentFilter);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.autofill.AutomaticValuesUtils.Service#fillFloatParameters(org.eclipse.core.resources.IProject,
	 * java.lang.String, java.lang.String, eu.cessar.ct.sdk.autofill.IAlgorithm,
	 * eu.cessar.ct.sdk.autofill.EAutomaticValuesFillType)
	 */
	@Override
	public IStatus fillFloatParameters(IProject project, String containerDefinitionQualifiedName, String parameterName,
		IAlgorithm<Double> fillAlgorithm, EAutomaticValuesFillType automaticFillType)
	{

		return doFillParameters(project, null, containerDefinitionQualifiedName, parameterName, fillAlgorithm,
			automaticFillType, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.autofill.AutomaticValuesUtils.Service#fillFloatParameters(org.eclipse.core.resources.IProject,
	 * java.lang.String, java.lang.String, eu.cessar.ct.sdk.autofill.IAlgorithm,
	 * eu.cessar.ct.sdk.autofill.EAutomaticValuesFillType, eu.cessar.ct.sdk.autofill.IContainerFragmentFilter)
	 */
	@Override
	public IStatus fillFloatParameters(IProject project, String moduleConfigurationQualifiedName,
		String containerDefinitionQualifiedName, String parameterName, IAlgorithm<Double> fillAlgorithm,
		EAutomaticValuesFillType automaticFillType, IContainerFragmentFilter elementFilter)
	{
		return doFillParameters(project, moduleConfigurationQualifiedName, containerDefinitionQualifiedName,
			parameterName, fillAlgorithm, automaticFillType, elementFilter);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.autofill.AutomaticValuesUtils.Service#fillStringParameters(org.eclipse.core.resources.IProject,
	 * java.lang.String, java.lang.String, eu.cessar.ct.sdk.autofill.IAlgorithm,
	 * eu.cessar.ct.sdk.autofill.EAutomaticValuesFillType)
	 */
	@Override
	public IStatus fillStringParameters(IProject project, String containerDefinitionQualifiedName, String parameterName,
		IAlgorithm<String> fillAlgorithm, EAutomaticValuesFillType automaticFillType)
	{
		return doFillParameters(project, null, containerDefinitionQualifiedName, parameterName, fillAlgorithm,
			automaticFillType, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.autofill.AutomaticValuesUtils.Service#fillStringParameters(org.eclipse.core.resources.IProject,
	 * java.lang.String, java.lang.String, eu.cessar.ct.sdk.autofill.IAlgorithm,
	 * eu.cessar.ct.sdk.autofill.EAutomaticValuesFillType, eu.cessar.ct.sdk.autofill.IContainerFragmentFilter)
	 */
	@Override
	public IStatus fillStringParameters(IProject project, String moduleConfigurationQualifiedName,
		String containerDefinitionQualifiedName, String parameterName, IAlgorithm<String> fillAlgorithm,
		EAutomaticValuesFillType automaticFillType, IContainerFragmentFilter elementFilter)
	{
		return doFillParameters(project, moduleConfigurationQualifiedName, containerDefinitionQualifiedName,
			parameterName, fillAlgorithm, automaticFillType, elementFilter);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.autofill.AutomaticValuesUtils.Service#fillBooleanParameters(org.eclipse.core.resources.IProject,
	 * java.lang.String, java.lang.String, eu.cessar.ct.sdk.autofill.IAlgorithm,
	 * eu.cessar.ct.sdk.autofill.EAutomaticValuesFillType)
	 */
	@Override
	public IStatus fillBooleanParameters(IProject project, String containerDefinitionQualifiedName,
		String parameterName, IAlgorithm<Boolean> fillAlgorithm, EAutomaticValuesFillType automaticFillType)
	{
		return doFillParameters(project, null, containerDefinitionQualifiedName, parameterName, fillAlgorithm,
			automaticFillType, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.autofill.AutomaticValuesUtils.Service#fillBooleanParameters(org.eclipse.core.resources.IProject,
	 * java.lang.String, java.lang.String, eu.cessar.ct.sdk.autofill.IAlgorithm,
	 * eu.cessar.ct.sdk.autofill.EAutomaticValuesFillType, eu.cessar.ct.sdk.autofill.IContainerFragmentFilter)
	 */
	@Override
	public IStatus fillBooleanParameters(IProject project, String moduleConfigurationQualifiedName,
		String containerDefinitionQualifiedName, String parameterName, IAlgorithm<Boolean> fillAlgorithm,
		EAutomaticValuesFillType automaticFillType, IContainerFragmentFilter elementFilter)
	{
		return doFillParameters(project, moduleConfigurationQualifiedName, containerDefinitionQualifiedName,
			parameterName, fillAlgorithm, automaticFillType, elementFilter);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.autofill.AutomaticValuesUtils.Service#pmFillFloatParameters(org.eclipse.emf.common.util.EList,
	 * java.lang.String, eu.cessar.ct.sdk.autofill.IAlgorithm, eu.cessar.ct.sdk.autofill.EAutomaticValuesFillType)
	 */
	@Override
	public IStatus pmFillFloatParameters(EList<? extends IPMContainer> containerList, String parameterName,
		IAlgorithm<Double> fillAlgorithm, EAutomaticValuesFillType automaticFillType)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.autofill.AutomaticValuesUtils.Service#pmFillStringParameters(org.eclipse.emf.common.util.EList,
	 * java.lang.String, eu.cessar.ct.sdk.autofill.IAlgorithm, eu.cessar.ct.sdk.autofill.EAutomaticValuesFillType)
	 */
	@Override
	public IStatus pmFillStringParameters(EList<? extends IPMContainer> containerList, String parameterName,
		IAlgorithm<String> fillAlgorithm, EAutomaticValuesFillType automaticFillType)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.autofill.AutomaticValuesUtils.Service#pmFillBooleanParameters(org.eclipse.emf.common.util.EList,
	 * java.lang.String, eu.cessar.ct.sdk.autofill.IAlgorithm, eu.cessar.ct.sdk.autofill.EAutomaticValuesFillType)
	 */
	@Override
	public IStatus pmFillBooleanParameters(EList<? extends IPMContainer> containerList, String parameterName,
		IAlgorithm<Boolean> fillAlgorithm, EAutomaticValuesFillType automaticFillType)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.AutomaticValuesUtils.Service#pmfillIntParameters(org.eclipse.emf.common.util.EList,
	 * java.lang.String, eu.cessar.ct.sdk.utils.IAlgorithm, eu.cessar.ct.sdk.utils.EAutomaticValuesFillType)
	 */
	@Override
	public IStatus pmFillIntParameters(EList<? extends IPMContainer> containerList, String parameterName,
		IAlgorithm<BigInteger> fillAlgorithm, EAutomaticValuesFillType automaticFillType)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @param project
	 *        the used project must not be <code>null</code>
	 * @param moduleConfigQualifiedName
	 *        qualified name of the module configuration to be used, could be <code>null</code>
	 * @param containerDefinitionQualifiedName
	 *        qualified name of the container definition
	 * @param parameterName
	 *        parameter short name, must not be <code>null</code>
	 * @param fillAlgorithm
	 *        the fill algorithm, must not be <code>null</code>
	 * @param automaticFillType
	 *        the fill type, must not be <code>null</code>
	 * @param filter
	 *        fragment filter, could be <code>null</code>
	 * @return the status of the operation
	 */
	public static <T> IStatus doFillParameters(IProject project, String moduleConfigQualifiedName,
		String containerDefinitionQualifiedName, final String parameterName, final IAlgorithm<T> fillAlgorithm,
		EAutomaticValuesFillType automaticFillType, IContainerFragmentFilter filter)
	{
		Assert.isNotNull(project, "project"); //$NON-NLS-1$
		Assert.isNotNull(containerDefinitionQualifiedName, "containerDefinitionQualifiedName"); //$NON-NLS-1$
		Assert.isNotNull(automaticFillType, "automaticFillType"); //$NON-NLS-1$
		Assert.isNotNull(parameterName, "parameterName"); //$NON-NLS-1$
		Assert.isNotNull(fillAlgorithm, "fillAlgorithm"); //$NON-NLS-1$

		IAutofillCandidatesProvider autofillCandidatesProvider = new AutofillParameterCandidatesProvider();

		IStatus status = autofillCandidatesProvider.computeCandidates(project, moduleConfigQualifiedName,
			containerDefinitionQualifiedName, parameterName, automaticFillType, filter);

		if (!status.isOK())
		{
			return status;
		}

		final Map<ISEAContainer, GContainer> result = autofillCandidatesProvider.getCandidates();

		return executeAutofill(result, project, autofillCandidatesProvider.getParameterDefinition(), fillAlgorithm);
	}

	/**
	 * @param result
	 * @param project
	 * @param parameterDef
	 * @param fillAlgorithm
	 * @param paramName
	 * @return the status of the operation
	 */
	public static <T> IStatus executeAutofill(final Map<ISEAContainer, GContainer> result, IProject project,
		final GReferrable parameterDef, final IAlgorithm<T> fillAlgorithm)
	{
		Exception ex = null;
		try
		{
			TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(parameterDef);
			WorkspaceTransactionUtil.executeInWriteTransaction(domain, new Runnable()
			{
				@Override
				public void run()
				{
					doExecuteAutofill(result, parameterDef, fillAlgorithm);
				}
			}, "Adding parameter value"); //$NON-NLS-1$

		}
		catch (OperationCanceledException e)
		{
			ex = e;
		}
		catch (ExecutionException e)
		{
			ex = e;
		}
		finally
		{
			fillAlgorithm.reset();
		}

		IStatus status = Status.OK_STATUS;
		if (ex != null)
		{
			status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, "Automatic fill operation has failed!", //$NON-NLS-1$
				ex);
		}

		return status;
	}

	/**
	 * @param result
	 * @param parameterDef
	 * @param fillAlgorithm
	 * @param parameterName
	 */
	@SuppressWarnings("unchecked")
	private static <T> void doExecuteAutofill(Map<ISEAContainer, GContainer> result, GReferrable parameterDef,
		IAlgorithm<T> fillAlgorithm)
	{
		Set<ISEAContainer> keySet = result.keySet();
		String parameterName = parameterDef.gGetShortName();

		for (ISEAContainer container: keySet)
		{
			T value = fillAlgorithm.getValue(result.get(container), parameterDef);
			GContainer fragment = result.get(container);

			if (value instanceof Boolean)
			{
				container.setBoolean(fragment, parameterName, (Boolean) value);
			}
			else if (value instanceof Double)
			{
				container.setFloat(fragment, parameterName, (Double) value);
			}
			else if (value instanceof BigInteger)
			{
				container.setInteger(fragment, parameterName, (BigInteger) value);
			}
			else if (value instanceof String)
			{
				if (parameterDef instanceof GAbstractStringParamDef)
				{
					container.setString(fragment, parameterName, (String) value);
				}
				else if (parameterDef instanceof GEnumerationParamDef)
				{
					container.setEnum(fragment, parameterName, (String) value);
				}
			}
			else if (value instanceof ISEAContainer)
			{

				container.setReference(fragment, parameterName, (ISEAContainer) value);
			}
			else if (value instanceof GIdentifiable)
			{

				container.setForeignReference(fragment, parameterName, (GIdentifiable) value);
			}
			else if (value instanceof Wrapper)
			{
				ISEAInstanceReference instanceReference = container.getInstanceReference(parameterName);
				if (null == instanceReference)
				{
					instanceReference = container.createInstanceReference(parameterName);
				}
				instanceReference.setTarget(((Wrapper<GIdentifiable>) value).getTarget());
				instanceReference.setContexts(((Wrapper<GIdentifiable>) value).getContext());
			}
		}
	}
}

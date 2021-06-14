/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 17, 2010 3:06:44 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.runtime.ecuc.util.DelegatingWithSourceMultiEList;
import eu.cessar.ct.runtime.ecuc.util.SplitPMUtils;
import eu.cessar.ct.sdk.pm.PMInvalidSplitException;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GConfigParameter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Wrapper for a single parameter value.
 * 
 * @param <V>
 * @param <D>
 * @param <T>
 */
public class SingleGParameterValueWrapper<V extends GParameterValue, D extends GConfigParameter, T> extends
	AbstractSingleMasterAttributeWrapper<T>
{
	private final EList<V> parameters;
	private final IParameterValueConvertor<V, D, T> convertor;
	private final D definition;
	private final boolean missingContainer;
	private String definitionName;

	/**
	 * 
	 * @param engine
	 * @param parameters
	 * @param definition
	 * @param convertor
	 * @param missingContainer
	 */
	public SingleGParameterValueWrapper(IEMFProxyEngine engine, EList<V> parameters, D definition,
		IParameterValueConvertor<V, D, T> convertor, boolean missingContainer)
	{
		super(engine);

		this.parameters = parameters;
		this.definition = definition;
		this.convertor = convertor;
		this.missingContainer = missingContainer;

		if (definition != null)
		{
			definitionName = definition.gGetShortName();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getFeatureClass()
	 */
	public Class<T> getFeatureClass()
	{
		return convertor.getValueClass();
	}

	/**
	 * Handles the cases of multiple values set for a split parameter with single multiplicity:<br>
	 * - diff values in the fragments from the same/several file(s)<br>
	 * - same value duplicated in all the fragments from the same/several file(s)<br>
	 * A {@link PMInvalidSplitException} is thrown.
	 */
	@Requirement(
		reqID = "REQ_RUNTIME#3")
	private void handleMultipleValues()
	{
		if (missingContainer)
		{
			return;
		}
		List<T> parameterValues = convertToValues(parameters);
		String parentInformation = getParentInformation(parameters);
		SplitPMUtils.handleMultipleValues(parameterValues, "Parameter with definition " + definitionName //$NON-NLS-1$
			+ parentInformation + " with upper multiplicity=1 has multiple values"); //$NON-NLS-1$
	}

	/**
	 * @return text information about the parent
	 */
	private String getParentInformation(EList<V> paramList)
	{
		String parentInformation = ""; //$NON-NLS-1$
		if (paramList.size() > 0)
		{

			V parameter = paramList.get(0);
			EObject eContainer = parameter.eContainer();

			if (eContainer instanceof GContainer)
			{
				GContainer container = (GContainer) eContainer;
				String parentShortName = container.gGetShortName();
				parentInformation = " from " + parentShortName; //$NON-NLS-1$
			}
		}
		return parentInformation;
	}

	private List<T> convertToValues(EList<V> params)
	{
		List<T> values = new ArrayList<T>();
		for (V v: params)
		{
			T value = convertor.getValue(v);
			values.add(value);
		}
		return values;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#getValue()
	 */
	@Requirement(
		reqID = "REQ_RUNTIME#3")
	public T getValue()
	{
		handleMultipleValues();

		if (!missingContainer && parameters != null && parameters.size() > 0)
		{
			return convertor.getValue(parameters.get(0));
		}
		else
		{
			return convertor.getValueForNull();
		}
	}

	/**
	 * Handle set value for split parameter (multiple sources).
	 * 
	 * @param newValue
	 *        the value to be set
	 */
	private void setMultiValues(final Object newValue)
	{

		DelegatingWithSourceMultiEList<V> splitParameters = (DelegatingWithSourceMultiEList<V>) parameters;
		EList<V> targetList = SplitPMUtils.findDefaultTargetListForSet(splitParameters, definition);
		unsetValue();
		@SuppressWarnings("unchecked")
		V createValue = convertor.createValue(definition, (T) newValue);
		targetList.add(createValue);
	}

	/**
	 * Handle set value to specified target for split parameter (multiple sources).
	 * 
	 * @param newValue
	 *        the value to be set in the target
	 * @param file
	 *        the target IFile
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Requirement(
		reqID = "29641")
	public void setValue(final Object newValue, Resource file)
	{
		if (parameters instanceof DelegatingWithSourceMultiEList)
		{
			DelegatingWithSourceMultiEList<V> splitParameters = (DelegatingWithSourceMultiEList<V>) parameters;

			@SuppressWarnings("rawtypes")
			final EList parentList = splitParameters.getParentList(file);

			final V createdValue = convertor.createValue(definition, (T) newValue);

			updateModel(new Runnable()
			{
				public void run()
				{
					parentList.add(createdValue);
				}
			});
		}
		else
		{ // if parameter exists in only one resource
			updateModel(new Runnable()
			{
				public void run()
				{
					setValueForNonMultiEList(newValue);
				}
			});
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#setValue(java.lang.Object)
	 */
	@Requirement(
		reqID = "REQ_RUNTIME#2")
	public void setValue(final Object newValue)
	{
		if (missingContainer)
		{
			throw new PMRuntimeException("Cannot change values within a missing container"); //$NON-NLS-1$
		}
		updateModel(new Runnable()
		{
			@SuppressWarnings("unchecked")
			public void run()
			{

				if (parameters instanceof DelegatingWithSourceMultiEList)
				{
					setMultiValues(newValue);
				}
				else
				{
					setValueForNonMultiEList(newValue);
				}
			}
		});
	}

	/**
	 * This method must be used only when the <code>parameters</code> are not instance of DelegatingWithSourceMultiEList
	 * (for example DelegatingWithSourceSubEList).
	 * 
	 * @param newValue
	 */
	@SuppressWarnings("unchecked")
	private void setValueForNonMultiEList(final Object newValue)
	{
		if (parameters.isEmpty())
		{
			parameters.add(convertor.createValue(definition, (T) newValue));
		}
		else
		{
			convertor.setValue(parameters.get(0), (T) newValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#isSetValue()
	 */
	@Requirement(
		reqID = "REQ_RUNTIME#3")
	public boolean isSetValue()
	{
		handleMultipleValues();

		if (!missingContainer && parameters != null && parameters.size() > 0)
		{
			return convertor.isSetValue(parameters.get(0));
		}
		else
		{
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#unsetValue()
	 */
	@Requirement(
		reqID = "REQ_RUNTIME#2")
	public void unsetValue()
	{
		if (missingContainer)
		{
			throw new PMRuntimeException("Cannot change values within a missing container"); //$NON-NLS-1$
		}
		handleMultipleValues();
		updateModel(new Runnable()
		{
			public void run()
			{
				SplitPMUtils.clearValues(parameters, definition);
			}
		});
	}
}

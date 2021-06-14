/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 15.04.2013 12:35:08
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.util;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import gautosar.gecucparameterdef.GCommonConfigurationAttributes;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;

/**
 * Base implementation of a list used by the Sea API to operate on the attributes (parameters or references) of a given
 * definition of a particular container.
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed Jun 17 18:43:52 2015 %
 * 
 *         %version: 6 %
 * @param <T>
 * @param <E>
 */
public abstract class AbstractAttributesEList<T, E> extends AbstractSeaSingleEList<E, T>
{
	private final GCommonConfigurationAttributes definition;
	private EList<T> attributeValues;

	/**
	 * @param parent
	 * @param attributeValues
	 * @param definition
	 *        definition of the attribute
	 * @param seaModel
	 * @param optionsHolder
	 *        store for Sea options
	 */
	public AbstractAttributesEList(ISEAContainer parent, ESplitableList<T> attributeValues,
		GCommonConfigurationAttributes definition, ISeaOptions optionsHolder)
	{
		super(attributeValues, parent, optionsHolder);
		this.attributeValues = attributeValues;
		this.definition = definition;
	}

	/**
	 * @return the modifiable list with parameter/reference values
	 */
	protected EList<T> getAttributeValues()
	{
		return attributeValues;
	}

	/**
	 * @return the definition
	 */
	protected GCommonConfigurationAttributes getDefinition()
	{
		return definition;
	}

	/**
	 * @param value
	 * @return whether <code>value</code> is accepted by the list
	 */
	protected abstract boolean accept(T value);

	/**
	 * @param index
	 * @return the index in the backing store list
	 */
	protected int getRealIndex(int index)
	{
		if (index >= size())
		{
			return -1;
		}

		int withValue = 0;
		int realIndex = -1;
		for (int i = 0; i < getRealSize(); i++)
		{
			if (accept(attributeValues.get(i)))
			{
				if (index == withValue)
				{
					realIndex = i;
					break;
				}

				withValue++;
			}

		}

		return realIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#doClear()
	 */
	@Override
	protected void doClear()
	{
		getAttributeValues().clear();
	}

	/**
	 * @return size of the backing store list
	 */
	protected int getRealSize()
	{
		return getAttributeValues().size();
	}

	/**
	 * @return the editing domain to be used
	 */
	@Override
	protected TransactionalEditingDomain getEditingDomain()
	{
		TransactionalEditingDomain domain = WorkspaceEditingDomainUtil.getEditingDomain(definition);
		return domain;
	}

	/**
	 * @return the meta-model service
	 */
	@Override
	protected IMetaModelService getMMService()
	{
		return MMSRegistry.INSTANCE.getMMService(definition.eClass());
	}
}

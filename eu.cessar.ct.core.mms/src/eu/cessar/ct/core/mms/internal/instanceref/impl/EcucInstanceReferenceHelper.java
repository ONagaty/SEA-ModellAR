/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 05.07.2013 11:22:57
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal.instanceref.impl;

import java.util.StringTokenizer;

import org.eclipse.sphinx.emf.model.IModelDescriptor;

import eu.cessar.ct.core.mms.ecuc.instanceref.IEcucInstanceReferenceHelper;
import eu.cessar.ct.core.mms.internal.instanceref.IContextToken;
import eu.cessar.ct.core.mms.internal.instanceref.InstanceRefConfigurationException;
import gautosar.gecucparameterdef.GInstanceReferenceDef;

/**
 *
 */
public class EcucInstanceReferenceHelper extends AbstractInstanceReferenceHelper implements
	IEcucInstanceReferenceHelper
{

	/**
	 * @param modelDescriptor
	 */
	public EcucInstanceReferenceHelper(IModelDescriptor modelDescriptor)
	{
		super(modelDescriptor);
	}

	/**
	 * @param input
	 * @throws InstanceRefConfigurationException
	 */
	public void init(GInstanceReferenceDef input) throws InstanceRefConfigurationException
	{

		String destinationType = input.gGetDestinationType();
		setTargetType(getEClass(destinationType));

		String destinationContext = input.gGetDestinationContext();

		String[] contextTokens = getContextTokens(destinationContext);
		init(contextTokens);

	}

	private static String[] getContextTokens(String context)
	{
		StringTokenizer tokenizer = new StringTokenizer(context, " "); //$NON-NLS-1$

		String[] tokens = new String[tokenizer.countTokens()];
		int i = 0;
		while (tokenizer.hasMoreTokens())
		{
			String token = tokenizer.nextToken();
			tokens[i] = token.trim();
			i++;
		}

		return tokens;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.core.mms.internal.instanceref.impl.AbstractInstanceReferenceHelper#createContextToken(java.lang.
	 * String)
	 */
	@Override
	protected IContextToken createContextToken(int index, String string)
	{
		return new EcucContextToken(string);
	}
}

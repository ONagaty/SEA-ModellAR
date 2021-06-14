/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870<br/>
 * Dec 10, 2013 11:16:40 AM
 *
 * </copyright>
 */
package eu.cessar.ct.sdk.autofill;

import java.math.BigInteger;

import eu.cessar.ct.sdk.autofill.algorithms.BooleanAlgorithm;
import eu.cessar.ct.sdk.autofill.algorithms.FloatIncrementalAlgorithm;
import eu.cessar.ct.sdk.autofill.algorithms.ForeignReferenceAlgorithm;
import eu.cessar.ct.sdk.autofill.algorithms.InstanceReferenceAlgorithm;
import eu.cessar.ct.sdk.autofill.algorithms.IntIncrementalAlgorithm;
import eu.cessar.ct.sdk.autofill.algorithms.ReferenceAlgorithm;
import eu.cessar.ct.sdk.autofill.algorithms.StringAlgorithm;
import eu.cessar.ct.sdk.collections.Wrapper;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Factory for algorithms<br>
 *
 * @author uidl6870
 *
 *         %created_by: uidl6870 %
 *
 *         %date_created: Mon Feb 10 09:11:26 2014 %
 *
 *         %version: 1 %
 */
public final class AlgorithmFactory
{
	/**
	 * the singleton
	 */
	public static final AlgorithmFactory INSTANCE = new AlgorithmFactory();

	private AlgorithmFactory()
	{
		// hide
	}

	/**
	 * @param initialValue
	 * @param increment
	 * @return an algorithm providing values based on the <code>initialValue</code> and the given <code>increment</code>
	 */
	public static IAlgorithm<BigInteger> newIntIncrementalAlgorithm(BigInteger initialValue, BigInteger increment)
	{
		return new IntIncrementalAlgorithm(initialValue, increment);
	}

	/**
	 *
	 * @param initialValue
	 * @param increment
	 * @return an algorithm providing values based on the <code>initialValue</code> and the given <code>increment</code>
	 */
	public static IAlgorithm<Double> newFloatIncrementalAlgorithm(Double initialValue, Double increment)
	{
		return new FloatIncrementalAlgorithm(initialValue, increment);
	}

	/**
	 * @param value
	 * @return an algorithm providing the given <code>value</code>
	 */
	public static IAlgorithm<Boolean> newBooleanAlgorithm(Boolean value)
	{
		return new BooleanAlgorithm(value);
	}

	/**
	 * @param value
	 * @return an algorithm providing the given <code>value</code>
	 */
	public static IAlgorithm<String> newStringAlgorithm(String value)
	{
		return new StringAlgorithm(value);
	}

	/**
	 * @param value
	 * @return an algorithm providing the given <code>value</code>
	 */
	public static IAlgorithm<ISEAContainer> newReferenceAlgorithm(ISEAContainer value)
	{
		return new ReferenceAlgorithm(value);
	}

	/**
	 * @param value
	 * @return an algorithm providing the given <code>value</code>
	 */
	public static IAlgorithm<GIdentifiable> newForeignReferenceAlgorithm(GIdentifiable value)
	{
		return new ForeignReferenceAlgorithm(value);
	}

	/**
	 * Provides Instance of InstanceReferenceAlgorithm
	 * 
	 * @param value
	 * @return an algorithm providing the given <code>value</code>
	 */
	public static IAlgorithm<Wrapper<GIdentifiable>> newInstanceReferenceAlgorithm(Wrapper<GIdentifiable> value)
	{
		return new InstanceReferenceAlgorithm(value);
	}
}

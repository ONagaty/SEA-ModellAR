/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Oct 24, 2012 1:08:54 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility.splitable;

import java.util.ArrayList;
import java.util.List;

import org.artop.aal.gautosar.services.splitting.Splitable;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Concrete implementation of a {@link ISplitableContextMultiFeatureManager}
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Fri Oct 26 10:47:11 2012 %
 * 
 *         %version: 1 %
 */
public class SplitableContextMultiFeatureEditingManager implements ISplitableContextMultiFeatureManager
{
	private List<ISplitableContextFeatureEditingStrategy> strategies;
	private final List<EStructuralFeature> features;
	private final Splitable input;

	/**
	 * @param input
	 * @param list
	 */
	public SplitableContextMultiFeatureEditingManager(Splitable input, List<EStructuralFeature> list)
	{
		this.input = input;
		features = list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.ISplitableContextMultiFeatureEditorManager#getStrategies()
	 */
	@Override
	public List<ISplitableContextFeatureEditingStrategy> getStrategies()
	{
		if (strategies == null)
		{
			strategies = new ArrayList<ISplitableContextFeatureEditingStrategy>();

			for (EStructuralFeature feature: features)
			{
				ISplitableContextFeatureEditingStrategy strategy = new SplitableContextFeatureEditingStrategy();
				strategy.setInput(input);
				strategy.setFeature(feature);

				strategies.add(strategy);

			}
		}
		return strategies;
	}

}

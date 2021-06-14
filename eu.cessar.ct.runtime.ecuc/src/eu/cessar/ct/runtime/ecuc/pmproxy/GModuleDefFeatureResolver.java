/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 15, 2010 11:34:11 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.MultiGModuleConfigurationReferenceWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.SingleGModuleConfigurationReferenceWrapper;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GModuleDef;

/**
 * 
 */
public class GModuleDefFeatureResolver extends AbstractEReferenceFeatureResolver
{
	private static final String SEPARATOR = ","; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.pmproxy.AbstractEReferenceFeatureResolver#getReferenceWrapper(eu.cessar.ct.emfproxy
	 * .IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, org.eclipse.emf.ecore.EReference)
	 */
	@Override
	protected IMasterFeatureWrapper<EObject> getReferenceWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EReference reference)
	{
		String uri = engine.getProxyElementAnnotation(reference.getEReferenceType(), ATTR_URI);
		InternalProxyConfigurationError.assertTrue(uri != null);

		IEcucModel model = getEcucModel(engine);
		GModuleDef def = model.getModuleDef(uri);
		// should not be null
		InternalProxyConfigurationError.assertTrue(def != null);

		Collection<List<GModuleConfiguration>> allValues = new ArrayList<List<GModuleConfiguration>>();

		Map<GModuleDef, Map<String, List<GModuleConfiguration>>> map = model.getSplitedModuleCfgsFromSameFamily(def);
		for (GModuleDef key: map.keySet())
		{
			Map<String, List<GModuleConfiguration>> map2 = map.get(key);
			Collection<List<GModuleConfiguration>> values = map2.values();
			allValues.addAll(values);
		}

		if (reference.isMany())
		{
			List<List<GModuleConfiguration>> cfgs = new ArrayList<List<GModuleConfiguration>>(allValues);
			return new MultiGModuleConfigurationReferenceWrapper(engine, cfgs, def);
		}
		else
		{
			if (allValues.size() > 1)
			{

				String msg = getErrorMessage(map);
				throw error(msg);
			}
			else
			{
				List<GModuleConfiguration> modules;
				if (allValues.size() == 1)
				{
					modules = allValues.iterator().next();
				}
				else
				{
					// no module configuration available
					modules = Collections.emptyList();
				}

				return new SingleGModuleConfigurationReferenceWrapper(engine, modules, def);
			}
		}
	}

	/**
	 * @param map
	 * @return
	 */
	private String getErrorMessage(Map<GModuleDef, Map<String, List<GModuleConfiguration>>> map)
	{
		StringBuffer buffer = new StringBuffer();

		buffer.append("Ambiguous configuration! Module configurations for both standard and vendor specific modules are present, while the multiplicity allows only one:\n"); //$NON-NLS-1$
		buffer.append("("); //$NON-NLS-1$
		for (GModuleDef moduleDef: map.keySet())
		{
			Map<String, List<GModuleConfiguration>> map2 = map.get(moduleDef);
			Set<String> keySet = map2.keySet();

			for (String string: keySet)
			{
				buffer.append(string);

				buffer.append(", "); //$NON-NLS-1$
			}
		}
		int i = buffer.lastIndexOf(SEPARATOR);
		if (i != -1)
		{
			buffer.delete(i, i + SEPARATOR.length());
		}
		buffer.append(")"); //$NON-NLS-1$

		return buffer.toString();
	}

}

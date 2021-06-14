/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 02.04.2013 11:07:19
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.core.platform.nature.CessarNature;
import eu.cessar.ct.runtime.ecuc.internal.Messages;
import eu.cessar.ct.runtime.ecuc.internal.sea.impl.SEAModelImpl;
import eu.cessar.ct.runtime.ecuc.internal.sea.impl.SeaOptions;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;

/**
 * Implementation of the SEAModelUtil from SDK
 * 
 * @author uidl6870
 * 
 *         %created_by: uidu2337 %
 * 
 *         %date_created: Tue Aug 26 13:41:39 2014 %
 * 
 *         %version: 9 %
 */
public final class SDKSeaModelUtilService implements eu.cessar.ct.sdk.sea.util.SEAModelUtil.Service
{
	/** the singleton */
	public static final SDKSeaModelUtilService INSTANCE = new SDKSeaModelUtilService();

	private Map<IProject, List<ISEAModel>> seaModelToProjectMap = new WeakHashMap<IProject, List<ISEAModel>>();

	private SDKSeaModelUtilService()
	{
		// hide
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.SEAModelUtil.Service#getSEAModel(org.eclipse.core.resources.IProject, java.util.Map)
	 */
	@Override
	public ISEAModel getSEAModel(IProject proj, Map<String, Object> params)
	{
		ISEAModel model = null;

		// check for null argument
		if (proj == null)
		{
			throw new IllegalArgumentException(NLS.bind(Messages.Project_not_valid, "<null>")); //$NON-NLS-1$
		}

		// check the passed argument
		if (!proj.isAccessible() || !CessarNature.haveNature(proj))
		{
			throw new IllegalArgumentException(NLS.bind(Messages.Project_not_valid, proj.getName()));
		}

		ISeaOptions newOptions = new SeaOptions(params);

		model = lookupModel(proj, newOptions);
		if (model == null)
		{
			synchronized (INSTANCE)
			{
				model = lookupModel(proj, newOptions);
				if (model == null)
				{
					model = new SEAModelImpl(proj, newOptions);

					if (!seaModelToProjectMap.containsKey(proj))
					{
						seaModelToProjectMap.put(proj, new ArrayList<ISEAModel>());
					}

					seaModelToProjectMap.get(proj).add(model);
				}
			}
		}

		return model;
	}

	/**
	 * @param project
	 *        the project for which the sea model using the <code>newOptions</code> is desired
	 * @param newOptions
	 * @return the associated sea model with the specified options if exists, null otherwise
	 */
	private ISEAModel lookupModel(IProject project, ISeaOptions newOptions)
	{
		ISEAModel seaModel = null;
		if (seaModelToProjectMap.containsKey(project))
		{
			List<ISEAModel> list = seaModelToProjectMap.get(project);
			seaModelToProjectMap.remove(project);
			seaModelToProjectMap.put(project, list);
			for (ISEAModel currentModel: list)
			{
				if (currentModel.getOptions().equals(newOptions))
				{
					seaModel = currentModel;
					break;
				}
			}
		}

		return seaModel;
	}

}

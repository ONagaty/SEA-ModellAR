/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 11.10.2012 14:45:28
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.edit.ui.facility.IModelFragmentMultiFeatureEditor;
import eu.cessar.ct.edit.ui.facility.splitable.ISplitableContextFeatureEditingStrategy;
import eu.cessar.ct.edit.ui.facility.splitable.ISplitableContextMultiFeatureManager;

/**
 * Implementation of a {@link IResourcesPart} to be used by {@link IModelFragmentMultiFeatureEditor}s
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Tue Oct 16 13:28:26 2012 %
 * 
 *         %version: 1 %
 */
public class MultiFeaturesResourcesPart extends AbstractResourcesPart
{
	private static final String COMMENT_MULTI = "Value of {0} present in {1} resource(s):"; //$NON-NLS-1$

	private static final String CR = "\n"; //$NON-NLS-1$

	private final IModelFragmentMultiFeatureEditor editor;
	private ISplitableContextMultiFeatureManager manager;

	/**
	 * @param editor
	 */
	public MultiFeaturesResourcesPart(IModelFragmentMultiFeatureEditor editor)
	{
		super(editor);
		this.editor = editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IResourcesPart#getResourcesNo()
	 */
	@Override
	public int getResourcesNo()
	{
		manager = editor.getSplitableContextEditingManager();

		int size = getResources().size();

		return size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.AbstractResourcesPart#getDocumentation()
	 */
	@Override
	public String getDocumentation()
	{
		StringBuffer buffer = new StringBuffer();

		List<ISplitableContextFeatureEditingStrategy> strategies = manager.getStrategies();
		for (ISplitableContextFeatureEditingStrategy strategy: strategies)
		{
			List<Resource> resourcesWithValue = strategy.getResourcesWithValue();
			if (resourcesWithValue.size() < 1)
			{
				continue;
			}

			buffer.append(NLS.bind(COMMENT_MULTI,
				new Object[] {strategy.getFeature().getName(), resourcesWithValue.size()}));
			buffer.append(getResourcesAsString(resourcesWithValue));
			buffer.append(CR);

		}
		return buffer.toString();
	}

	/**
	 * 
	 * @return the list with resources in which are stored fragments that have at least one of the editor's feature set
	 */
	private List<Resource> getResources()
	{
		List<Resource> resources = new ArrayList<Resource>();

		List<ISplitableContextFeatureEditingStrategy> strategies = manager.getStrategies();
		for (ISplitableContextFeatureEditingStrategy strategy: strategies)
		{
			List<Resource> relevantResources = strategy.getResourcesWithValue();

			for (Resource resource: relevantResources)
			{
				if (!resources.contains(resource))
				{
					resources.add(resource);
				}
			}
		}
		return resources;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.AbstractResourcesPart#getVisibility()
	 */
	@Override
	protected boolean getVisibility()
	{
		return getResources().size() > 0;
	}

}

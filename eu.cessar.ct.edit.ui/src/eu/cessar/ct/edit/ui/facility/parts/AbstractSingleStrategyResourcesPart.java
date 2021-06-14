/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 26.10.2012 12:49:04
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.splitable.ISplitableContextEditingStrategy;

/**
 * Basic implementation of {@link IResourcesPart} belonging to editors with a a single editing strategy
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Fri Oct 26 14:53:09 2012 %
 * 
 *         %version: 1 %
 */
public abstract class AbstractSingleStrategyResourcesPart extends AbstractResourcesPart
{
	private int resourcesNo;
	private static final String COMMENT = "Value present in {0} resource(s):"; //$NON-NLS-1$

	/**
	 * @param editor
	 */
	public AbstractSingleStrategyResourcesPart(IModelFragmentEditor editor)
	{
		super(editor);
	}

	/**
	 * 
	 * @return the editing strategy to be used
	 */
	protected abstract ISplitableContextEditingStrategy getStrategy();

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IResourcesPart#getResourcesNo()
	 */
	@Override
	public int getResourcesNo()
	{
		int no = computeResourcesNo();
		resourcesNo = no;
		return no;
	}

	private int computeResourcesNo()
	{
		ISplitableContextEditingStrategy strategy = getStrategy();
		int no = strategy.getFragmentsWithValue().size();

		return no;
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
		buffer.append(NLS.bind(COMMENT, resourcesNo));
		buffer.append(getResourcesAsString(getStrategy().getResourcesWithValue()));

		return buffer.toString();
	}

	/**
	 * @return the comment displayed as additional documentation
	 */
	protected static String getComment()
	{
		return COMMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.AbstractResourcesPart#getVisibility()
	 */
	@Override
	protected boolean getVisibility()
	{
		boolean displayResourcesNo = false;

		ISplitableContextEditingStrategy strategy = getStrategy();
		int size = strategy.getFragmentsWithValue().size();
		boolean oneResource = (size == 1);
		boolean moreThanOneResource = (size > 1);

		// one value in one resource
		if (oneResource)
		{
			displayResourcesNo = strategy.isSplittingAllowed();
		}
		else if (moreThanOneResource)
		{
			displayResourcesNo = true;
		}

		return displayResourcesNo;

	}

}

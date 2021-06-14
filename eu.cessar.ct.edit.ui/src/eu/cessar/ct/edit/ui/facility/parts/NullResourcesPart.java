/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Oct 21, 2012 5:39:25 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;

/**
 * Editors that do not support a resources part should use this implementation
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Fri Oct 26 10:47:07 2012 %
 * 
 *         %version: 1 %
 */
public class NullResourcesPart extends AbstractResourcesPart
{

	/**
	 * @param editor
	 */
	public NullResourcesPart(IModelFragmentEditor editor)
	{
		super(editor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#refresh()
	 */
	@Override
	public void refresh()
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.AbstractResourcesPart#getVisibility()
	 */
	@Override
	protected boolean getVisibility()
	{
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.AbstractResourcesPart#getDocumentation()
	 */
	@Override
	public String getDocumentation()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IResourcesPart#getResourcesNo()
	 */
	@Override
	public int getResourcesNo()
	{
		// TODO Auto-generated method stub
		return 0;
	}

}

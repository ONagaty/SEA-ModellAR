/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Mar 17, 2011 4:41:07 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.expansion;

import java.util.Map;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;

/**
 * @author uidl6870
 * 
 */
public interface IModelFragmentEditorExpansionProvider
{

	public IModelFragmentEditorExpansion createExpansion(IModelFragmentEditor editor,
		Map<String, String> properties);

}

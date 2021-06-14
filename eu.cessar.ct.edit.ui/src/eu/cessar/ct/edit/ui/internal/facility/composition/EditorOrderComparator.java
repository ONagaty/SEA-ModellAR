/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu3379<br/>
 * 18.12.2012 16:53:15
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.composition;

import java.util.Comparator;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;
import eu.cessar.ct.edit.ui.facility.parts.EEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.ICaptionPart;
import eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart;
import eu.cessar.req.Requirement;

/**
 * Provides a simple Comparator for displaying the editors according to a certain consistent order.
 * 
 * @author uidu3379
 * 
 *         %created_by: uidu3379 %
 * 
 *         %date_created: Thu Dec 20 15:32:12 2012 %
 * 
 *         %version: 1 %
 */

@Requirement(
	reqID = "REQ_EDIT_PROP#1")
public class EditorOrderComparator implements Comparator<IModelFragmentEditorProvider>
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(IModelFragmentEditorProvider o1, IModelFragmentEditorProvider o2)
	{
		// first compare the priority
		int result = o1.getPriority() - o2.getPriority();

		// if the same, compare by caption part
		if (result == 0)
		{
			IModelFragmentEditorPart captionPart1 = o1.createEditor().getPart(EEditorPart.CAPTION);
			IModelFragmentEditorPart captionPart2 = o2.createEditor().getPart(EEditorPart.CAPTION);

			if (captionPart1 instanceof ICaptionPart && captionPart2 instanceof ICaptionPart)
			{
				result = ((ICaptionPart) captionPart1).getCaption().compareTo(
					((ICaptionPart) captionPart2).getCaption());
			}

		}
		return result;
	}
}

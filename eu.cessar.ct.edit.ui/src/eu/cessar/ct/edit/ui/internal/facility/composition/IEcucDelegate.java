/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Aug 29, 2011 5:52:55 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.composition;

import java.util.List;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;
import gautosar.gecucparameterdef.GParamConfContainerDef;

/**
 * @author uidl6870
 * 
 */
public interface IEcucDelegate
{

	public List<IModelFragmentEditorProvider> getEditorsForDefinition(
		GParamConfContainerDef containerDef);
}

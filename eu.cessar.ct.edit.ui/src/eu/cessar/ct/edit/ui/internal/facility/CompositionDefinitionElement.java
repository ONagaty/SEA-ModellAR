/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Aug 22, 2011 5:51:51 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility;

import org.eclipse.core.runtime.IConfigurationElement;

import eu.cessar.ct.core.platform.util.ExtensionClassWrapper;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;
import eu.cessar.ct.edit.ui.internal.facility.composition.IEditorCompositionProvider;

/**
 * @author uidl6870
 * 
 */
public class CompositionDefinitionElement implements IDefinitionElement
{
	private ExtensionClassWrapper<IEditorCompositionProvider> wrapper;
	private String input;
	private String type;

	public CompositionDefinitionElement(IConfigurationElement element)
	{

		wrapper = new ExtensionClassWrapper<IEditorCompositionProvider>(element,
			FacilityConstants.ATT_PROVIDER);
		this.input = element.getAttribute("input"); //$NON-NLS-1$
		this.type = element.getAttribute("type"); //$NON-NLS-1$
	}

	/**
	 * @return the input
	 */
	public String getInput()
	{
		return input;
	}

	/**
	 * 
	 * @return the type of composition
	 */
	public String getType()
	{
		return type;
	}

	public IEditorCompositionProvider createEditorCompositionProvider()
	{

		try
		{
			return wrapper.getInstance();
		}
		catch (Throwable e) // SUPPRESS CHECKSTYLE change in future
		{
			CessarPluginActivator.getDefault().logError(e);
			return null;
		}
	}
}

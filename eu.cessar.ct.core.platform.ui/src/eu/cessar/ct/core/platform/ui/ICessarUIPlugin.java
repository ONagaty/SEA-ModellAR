/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uid94246 Jul 29, 2009 2:21:41 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import eu.cessar.ct.core.platform.ICessarPlugin;

/**
 * Interface that all activators from UI dependent plugins need to implement
 * 
 * @author uid94246
 * 
 * @Review uidl6458 - 19.04.2012
 */
public interface ICessarUIPlugin extends ICessarPlugin
{
	/**
	 * Lookup the {@link ImageRegistry} associated with the plugin for an
	 * {@link Image} associated with the <code>key</code> argument. The
	 * {@link ImageRegistry} should be initialized by the plugin by implementing
	 * the {@link AbstractUIPlugin#initializeImageRegistry(ImageRegistry)}
	 * 
	 * @param key
	 *        the image key
	 * @return the image assigned to the key or <code>null</code> if none
	 */
	public Image getImage(String key);
}

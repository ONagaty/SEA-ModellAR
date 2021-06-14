/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu3379<br/>
 * 25.01.2013 17:24:01
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets.expandShelf;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.nebula.widgets.pshelf.PShelfItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

/**
 * Label Provider used by the {@link ShelfViewer} to display the content of its items. Use {@link ShelfViewer} and
 * Implement this interface instead of using the widget directly.
 * 
 * @author uidu3379
 * 
 *         %created_by: uidl7321 %
 * 
 *         %date_created: Fri Apr 12 10:59:16 2013 %
 * 
 *         %version: 3 %
 */
public interface IShelfItemLabelProvider extends IBaseLabelProvider
{
	/**
	 * Gets the text to display on the header of the item which has the itemData associated data.
	 * 
	 * @param itemData
	 *        the model data behind the {@link PShelfItem}
	 * @return the textual representation of the header
	 */
	public String getItemHeaderText(Object itemData);

	/**
	 * Gets the Images to display on the header area of the item which with itemData.
	 * 
	 * @param itemData
	 *        the model data behind the {@link PShelfItem}
	 * @return an array of Images corresponding to this itemData model element
	 */
	public Image[] getItemHeaderImages(Object itemData);

	/**
	 * Constructs and returns a Composite that will be displayed inside the body area of the {@link PShelfItem} with the
	 * provided itemData. It is possible that the Composite is different, depending on the data to display inside the
	 * item.
	 * 
	 * @param itemData
	 *        the model data of the {@link PShelfItem} in which to construct the Composite
	 * @param parent
	 *        the parent of the Composite to construct (usually the body area of the item)
	 * @return a Composite that will be placed in the body of the item
	 */
	public Composite constructItemBodyComposite(Object itemData, Composite parent);

	/**
	 * Gets the text to display as a tooltip when the mouse is hovering the information icon from the header of the item
	 * which has the itemData associated data.
	 * 
	 * @param itemData
	 *        the model data behind the {@link PShelfItem}
	 * @return the text to display on mouse hover
	 */
	public String getItemHeaderHoverText(Object itemData);

}

/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu3379<br/>
 * 29.01.2013 14:08:49
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets.expandShelf;

import org.eclipse.nebula.widgets.pshelf.PShelf;
import org.eclipse.nebula.widgets.pshelf.PShelfItem;
import org.eclipse.nebula.widgets.pshelf.PaletteShelfRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Renderer for the {@link PShelf} widget that handles drawing {@link PShelfItem}s with more Images in their header. To
 * use this functionality, the {@link ShelfViewer} and the {@link IShelfItemLabelProvider} must be used instead of
 * PShelf directly.
 * 
 * @author uidu3379
 * 
 *         %created_by: uidl7321 %
 * 
 *         %date_created: Wed Apr 17 16:19:32 2013 %
 * 
 *         %version: 5 %
 */
public class ExtendedShelfRenderer extends PaletteShelfRenderer
{

	private IShelfItemLabelProvider labelProvider;

	private HoverShell hShell;

	/**
	 * Sets the internally used {@link IShelfItemLabelProvider}. If not set, the data associated with the items will be
	 * displayed by getting it directly from the underlying {@link CessarExpandItem}.
	 * 
	 * @param labelProvider
	 *        the associated {@link IShelfItemLabelProvider}
	 */
	void setLabelProvider(IShelfItemLabelProvider labelProvider)
	{
		this.labelProvider = labelProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.nebula.widgets.pshelf.PaletteShelfRenderer#computeSize(org.eclipse.swt.graphics.GC, int, int,
	 * java.lang.Object)
	 */
	@Override
	public Point computeSize(GC gc, int wHint, int hHint, Object value)
	{
		Point size = super.computeSize(gc, wHint, hHint, value);

		// get the max height among our own images

		PShelfItem item = (PShelfItem) value;
		int imageMaxHeight = 0;

		if (item.getImage() == null)
		{
			// get the image with the maximum height
			Image[] images = null;

			if (labelProvider != null)
			{
				images = labelProvider.getItemHeaderImages(item.getData());
			}

			if (images != null)
			{
				int maxHeightImageIndex = 0;
				for (int i = 1; i < images.length; i++)
				{
					if (images[i - 1].getBounds().height < images[i].getBounds().height)
					{
						maxHeightImageIndex = i;
					}
				}

				imageMaxHeight = images[maxHeightImageIndex].getBounds().height;
			}
		}

		return new Point(size.x, Math.max(size.y, imageMaxHeight));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.nebula.widgets.pshelf.PaletteShelfRenderer#paint(org.eclipse.swt.graphics.GC, java.lang.Object)
	 */
	@Override
	public void paint(GC gc, Object value)
	{
		// first let the default renderer paint the background and the text
		super.paint(gc, value);

		// then paint our own icons

		// this is taken from the parent
		int spacing = 4;

		PShelfItem item = (PShelfItem) value;

		// draw our own images
		gc.setForeground(getShadeColor());

		int x = 6;
		int y = -1;

		Image[] images = null;
		if (labelProvider != null)
		{
			images = labelProvider.getItemHeaderImages(item.getData());
		}
		if (images != null && images.length > 0)
		{
			for (Image image: images)
			{
				y = (getBounds().height - image.getBounds().height) / 2;
				if ((getBounds().height - image.getBounds().height) % 2 != 0)
				{
					y++;
				}

				gc.drawImage(image, x, getBounds().y + y);

				x += image.getBounds().width + spacing;
			}
		}

		Point iconLocation = new Point(0, 0);

		if (y != -1)
		{
			iconLocation = item.getBody().getParent().getParent().toDisplay(x, y);
			// System.err.println("Icon " + iconLocation);
		}

		handleHovering(item, iconLocation);
	}

	/**
	 * 
	 * Responsible for creating the a hover shell for a PShelfItem.
	 * 
	 * @author uidl7321
	 * 
	 *         %created_by: uidl7321 %
	 * 
	 *         %date_created: Wed Apr 17 16:19:32 2013 %
	 * 
	 *         %version: 5 %
	 */
	private class HoverShell
	{
		Shell hoverShell;
		Label label;
		PShelfItem item;

		public HoverShell(Shell shell, PShelfItem item, Point hoverLocation)
		{
			// System.err.println("Create!");

			hoverShell = new Shell(shell, SWT.ON_TOP | SWT.TOOL);
			hoverShell.setLayout(new FillLayout());
			label = new Label(hoverShell, SWT.NONE);
			this.item = item;

			Point size = hoverShell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			// System.err.println(item.getText() + " is expanded " + isExpanded());

			hoverShell.setBounds(hoverLocation.x, hoverLocation.y, size.x, size.y);

		}

		/**
		 * 
		 */
		public void setText()
		{
			Object data = item.getData();
			if (label != null)
			{
				label.setText(labelProvider.getItemHeaderHoverText(data));
			}

		}

		/**
		 * @param visible
		 */
		public void setVisible(boolean visible)
		{
			if (hoverShell != null)
			{
				hoverShell.setVisible(visible);
			}

			if (label != null)
			{
				label.setVisible(visible);
			}

		}

		public void dispose()
		{
			if (hoverShell != null)
			{
				hoverShell.dispose();
				hoverShell = null;
			}
			if (label != null)
			{

				label.dispose();
				label = null;
			}
		}
	}

	/**
	 * @param item
	 * @param iconLocation
	 */
	private void handleHovering(PShelfItem item, Point iconLocation)
	{
		Shell shell = item.getBody().getShell();

		if (isHover() && !isSelected())
		{
			Point cursorLocation = Display.getCurrent().getCursorLocation();
			// System.err.println("Cursor point " + cursorLocation);
			int diff = (iconLocation.x - cursorLocation.x);
			if (diff >= 0 && diff <= 15)
			{
				disposeHoverShell();
				Point hoverLocation = computeHoverLocation(item);
				hShell = new HoverShell(shell, item, hoverLocation);
				hShell.setText();
				hShell.hoverShell.pack();
				hShell.hoverShell.open();
			}
		}
		else
		{
			if (hShell != null && hShell.item == item)
			{
				disposeHoverShell();
			}
		}

	}

	/**
	 * Returns the point representing the location of the hover shell.
	 * 
	 * @return
	 */
	private static Point computeHoverLocation(PShelfItem item)
	{
		Composite itemBody = item.getBody();
		Rectangle rect = itemBody.getBounds();
		Point pt = itemBody.toDisplay(rect.x, rect.y);
		// System.err.println(pt.x + " " + pt.y);

		Composite parent = itemBody.getParent().getParent();
		PShelf pshelf = null;
		if (parent instanceof PShelf)
		{
			pshelf = (PShelf) parent;
		}

		// see the location of the currently selected PShelfItem compared to the hoverPShelfItem

		int indexOfSelected = -1;
		int indexOfHover = -1;
		if (pshelf != null)
		{
			PShelfItem selectedItem = pshelf.getSelection();

			PShelfItem[] items = pshelf.getItems();
			for (int i = 0; i < items.length; i++)
			{
				PShelfItem pShelfItem = items[i];
				if (pShelfItem == selectedItem)
				{
					indexOfSelected = i;
				}

				else if (pShelfItem == item)
				{
					indexOfHover = i;
				}

				if (indexOfHover != -1 && indexOfSelected != -1)
				{
					break;
				}
			}

		}

		int hoverY = pt.y + 10;
		if (indexOfHover != -1 && indexOfSelected != -1 && indexOfHover > indexOfSelected)
		{
			Rectangle pShelfBounds = pshelf.getBounds();
			if (hoverY < pShelfBounds.y)
			{
				hoverY += rect.height;
			}
			else
			{
				hoverY = pShelfBounds.height + 10 * (indexOfHover - indexOfSelected);
			}
		}

		return new Point(pt.x, hoverY);

	}

	private void disposeHoverShell()
	{
		if (hShell != null)
		{
			// System.err.println("Dispose!");
			hShell.setVisible(false);
			hShell.dispose();
			hShell = null;
		}
	}

}

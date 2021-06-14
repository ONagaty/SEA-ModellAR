package eu.cessar.ct.core.platform.ui.widgets.breadcrumb;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

/**
 * The drop-down part of the breadcrumb item.
 * 
 */
class BreadcrumbItemTreeExpand extends BreadcrumbItemExpand
{

	/**
	 * Create a new instance of this class with the given item parent and the
	 * composite parent.
	 * 
	 * @param parent
	 *        the parent item.
	 * @param parentContainer
	 *        the parent composite.
	 */
	protected BreadcrumbItemTreeExpand(final BreadcrumbItem parent, final Composite parentContainer)
	{
		super(parent, parentContainer);
	}

	/**
	 * Calculates a useful size for the given shell.
	 * 
	 * @param shell
	 *        the shell to calculate the size for.
	 */
	@Override
	protected void setShellBounds(final Shell shell)
	{
		final Rectangle rect = parentComposite.getBounds();
		final Rectangle toolbarBounds = imageElement.getBounds();

		shell.pack();
		final Point size = shell.getSize();
		final int height = Math.min(size.y, DROP_DOWN_HEIGHT);
		final int width = Math.max(Math.min(size.x, DROP_DOWN_WIDTH), 250);

		int imageBoundsX = 0;
		if (((TreeViewer) dropDownWindow.getDropDownViewer()).getTree().getItemCount() > 0)
		{
			final TreeItem item = ((TreeViewer) dropDownWindow.getDropDownViewer()).getTree().getItem(
				0);
			imageBoundsX = item.getImageBounds(0).x;
		}

		final Rectangle trim = shell.computeTrim(0, 0, width, height);
		int x = toolbarBounds.x + toolbarBounds.width + 2 + trim.x - imageBoundsX;
		if (!isLTR())
		{
			x += width;
		}

		Point pt = new Point(x, rect.y + rect.height);
		pt = parentComposite.toDisplay(pt);

		final Rectangle monitor = getClosestMonitor(shell.getDisplay(), pt).getClientArea();
		final int overlapX = pt.x + width - (monitor.x + monitor.width);
		if (overlapX > 0)
		{
			pt.x -= overlapX;
		}
		if (pt.x < monitor.x)
		{
			pt.x = monitor.x;
		}
		final int overlayY = pt.y + height - (monitor.y + monitor.height);
		if (overlayY > 0)
		{
			pt.y -= height;
			pt.y -= rect.height;
		}

		shell.setLocation(pt);
		shell.setSize(width, height);
	}

	/**
	 * Opens the drop down menu.
	 */
	@Override
	protected void openDropDownMenu()
	{

		if (!enabled || menuIsShown)
		{
			return;
		}

		if (!getParentItem().isDropdownOpen())
		{
			menuIsShown = true;
			imageElement.setImage(getArrowImage(true));
			dropDownWindow = (DropDownTreeWindow) getDropDownWindow();
			getParentItem().setDropdownOpen(true);

			BusyIndicator.showWhile(imageElement.getDisplay(), new Runnable()
			{
				public void run()
				{
					dropDownWindow.open();
					// dropDownWindow.getShell().setActive(); - a noticeable
					// flicker, could be annoying
				}
			});
		}
		else
		{
			getParentItem().setDropdownOpen(false);
		}
	}
}

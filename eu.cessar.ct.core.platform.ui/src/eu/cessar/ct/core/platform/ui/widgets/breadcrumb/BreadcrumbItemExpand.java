package eu.cessar.ct.core.platform.ui.widgets.breadcrumb;

import org.eclipse.jface.util.Geometry;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import eu.cessar.ct.core.internal.platform.ui.CessarPluginActivator;
import eu.cessar.ct.core.internal.platform.ui.Messages;
import eu.cessar.ct.core.platform.ui.PlatformUIConstants;

abstract public class BreadcrumbItemExpand
{

	protected static int DROP_DOWN_HEIGHT = 300;
	protected static int DROP_DOWN_WIDTH = 500;
	protected BreadcrumbItem parentItem;
	protected Composite parentComposite;
	protected Label imageElement;
	protected DropDownWindow dropDownWindow;
	protected boolean menuIsShown;
	protected boolean enabled;

	private StructuredViewer dropDownViewer;
	private Image rightImage;
	private Image leftImage;
	private Image downImage;
	private boolean leftToRight;
	public boolean dropDownOpen = false;

	public BreadcrumbItemExpand()
	{
		super();
	}

	/**
	 * Opens the drop down menu.
	 */
	abstract protected void openDropDownMenu();

	/**
	 * Calculates a useful size for the given shell.
	 * 
	 * @param shell
	 *        the shell to calculate the size for.
	 */
	abstract protected void setShellBounds(final Shell shell);

	/**
	 * @return the parentItem
	 */
	protected BreadcrumbItem getParentItem()
	{
		return parentItem;
	}

	/**
	 * Create a new instance of this class with the given item parent and the
	 * composite parent.
	 * 
	 * @param parent
	 *        the parent item.
	 * @param parentContainer
	 *        the parent composite.
	 */
	protected BreadcrumbItemExpand(final BreadcrumbItem parent, final Composite parentContainer)
	{
		this.parentItem = parent;
		enabled = true;
		menuIsShown = false;
		parentComposite = parentContainer;
		leftToRight = (parentContainer.getStyle() & SWT.RIGHT_TO_LEFT) == 0;

		// images
		rightImage = CessarPluginActivator.getDefault().getImage(
			PlatformUIConstants.KEY_ARROW_RIGHT);
		leftImage = CessarPluginActivator.getDefault().getImage(PlatformUIConstants.KEY_ARROW_LEFT);
		downImage = CessarPluginActivator.getDefault().getImage(PlatformUIConstants.KEY_ARROW_DOWN);

		imageElement = new Label(parentContainer, SWT.NONE);
		imageElement.setImage(getArrowImage(false));
		imageElement.setToolTipText(Messages.BreadcrumbItemDropDown_Action_ToolTip);
		imageElement.getAccessible().addAccessibleListener(new AccessibleAdapter()
		{
			@Override
			public void getName(final AccessibleEvent e)
			{
				e.result = Messages.BreadcrumbItemDropDown_Action_ToolTip;
			}
		});
		imageElement.addListener(SWT.FocusIn, new Listener()
		{
			public void handleEvent(final Event event)
			{
				parent.getItemDetail().setHasFocus(true);
			}
		});
		imageElement.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseUp(MouseEvent e)
			{
				getViewer().preSelectItem(parentItem);
			}

		});

		imageElement.addListener(SWT.KeyDown, new Listener()
		{
			public void handleEvent(final Event event)
			{
				final BreadcrumbViewer viewer = getViewer();
				int index = viewer.indexOf(parentItem);
				switch (event.keyCode)
				{
					case SWT.ARROW_RIGHT:
						index++;
						break;
					case SWT.ARROW_LEFT:
						index--;
						break;
					case SWT.HOME:
						index = viewer.isRootVisible() ? 0 : 1;
						break;
					case SWT.END:
						index = viewer.getItemCount() - 1;
						break;
					default:
						return;
				}
				if (index >= 0 && index < viewer.getItemCount())
				{
					viewer.selectItem(index);
				}
				event.doit = true;
			}
		});
	}

	/**
	 * Returns the monitor whose client area contains the given point. If no
	 * monitor contains the point, returns the monitor that is closest to the
	 * point.
	 * <p>
	 * Copied from
	 * <code>org.eclipse.jface.window.Window.getClosestMonitor(Display, Point)</code>
	 * .
	 * </p>
	 * 
	 * @param display
	 *        the display showing the monitors.
	 * @param point
	 *        point to find (display coordinates).
	 * @return the monitor closest to the given point.
	 */
	protected static Monitor getClosestMonitor(final Display display, final Point point)
	{
		int closest = Integer.MAX_VALUE;

		final Monitor[] monitors = display.getMonitors();
		Monitor result = monitors[0];

		for (final Monitor current: monitors)
		{
			final Rectangle clientArea = current.getClientArea();

			if (clientArea.contains(point))
			{
				return current;
			}

			final int distance = Geometry.distanceSquared(Geometry.centerPoint(clientArea), point);
			if (distance < closest)
			{
				closest = distance;
				result = current;
			}
		}

		return result;
	}

	/**
	 * Gets the current item width.
	 * 
	 * @return the current item width.
	 */
	public int getCurrentWidth()
	{
		if (!imageElement.isDisposed())
		{
			if (!imageElement.getVisible())
			{
				return 0;
			}
			return imageElement.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		}
		else
		{
			return 0;
		}
	}

	/**
	 * Gets the drop down selection provider.
	 * 
	 * @return the selection provider of the drop down or <code>null</code>.
	 */
	public ISelectionProvider getDropDownSelectionProvider()
	{
		if (!menuIsShown)
		{
			return null;
		}
		return dropDownViewer;
	}

	/**
	 * Gets the drop down window.
	 * 
	 * @return the drop down window if shown, <code>null</code> otherwise.
	 */
	public Window getDropDownWindow()
	{
		/*if (!isMenuShown())
		{
			return null;
		}*/
		if (dropDownWindow == null)
		{
			dropDownWindow = new DropDownTreeWindow(this, imageElement.getShell());
		}
		return dropDownWindow;
	}

	/**
	 * Returns <code>true</code> if the receiver has the user-interface focus,
	 * and <code>false</code> otherwise.
	 * 
	 * @return the receiver's focus state
	 * @exception SWTException
	 *            <ul>
	 *            <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *            <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *            thread that created the receiver</li>
	 *            </ul>
	 */
	public boolean isFocusControl()
	{
		return parentComposite.isFocusControl() || imageElement.isFocusControl();
	}

	/**
	 * Gets a value indicating if this element is expanded.
	 * 
	 * @return <code>true</code> if this item is expanded, <code>false</code>
	 *         otherwise.
	 */
	public boolean isMenuShown()
	{
		return menuIsShown;
	}

	/**
	 * Set whether the drop down menu is available.
	 * 
	 * @param enabled
	 *        true if available
	 */
	public void setEnabled(final boolean enabled)
	{
		this.enabled = enabled;
		imageElement.setVisible(enabled);
	}

	protected Image getArrowImage(final boolean down)
	{
		if (down)
		{
			return downImage;
		}
		if (isLTR())
		{
			return rightImage;
		}
		return leftImage;
	}

	protected BreadcrumbViewer getViewer()
	{
		return (BreadcrumbViewer) parentItem.getViewer();
	}

	/**
	 * Gets a value indicating if the breadcrumb is in left-to-right mode.
	 * 
	 * @return <code>true</code> if the breadcrumb is in left-to-right mode,
	 *         <code>false</code> otherwise.
	 */
	protected boolean isLTR()
	{
		return leftToRight;
	}

	/**
	 * Set the size of the given shell such that more content can be shown. The
	 * shell size does not exceed {@link #DROP_DOWN_HEIGHT} and
	 * {@link #DROP_DOWN_WIDTH}.
	 * 
	 * @param shell
	 *        the shell to resize.
	 */
	protected void resizeShell(final Shell shell)
	{
		final Point size = shell.getSize();
		final int currentWidth = size.x;
		final int currentHeight = size.y;

		if (currentHeight >= DROP_DOWN_HEIGHT && currentWidth >= DROP_DOWN_WIDTH)
		{
			return;
		}

		final Point preferedSize = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);

		int newWidth;
		if (currentWidth >= DROP_DOWN_WIDTH)
		{
			newWidth = currentWidth;
		}
		else
		{
			newWidth = Math.min(Math.max(preferedSize.x, currentWidth), DROP_DOWN_WIDTH);
		}
		int newHeight;
		if (currentHeight >= DROP_DOWN_HEIGHT)
		{
			newHeight = currentHeight;
		}
		else
		{
			newHeight = Math.min(Math.max(preferedSize.y, currentHeight), DROP_DOWN_HEIGHT);
		}

		if (newHeight != currentHeight || newWidth != currentWidth)
		{
			shell.setRedraw(false);
			try
			{
				shell.setSize(newWidth, newHeight);
				if (!isLTR())
				{
					final Point location = shell.getLocation();
					shell.setLocation(location.x - (newWidth - currentWidth), location.y);
				}
			}
			finally
			{
				shell.setRedraw(true);
			}
		}
	}

}
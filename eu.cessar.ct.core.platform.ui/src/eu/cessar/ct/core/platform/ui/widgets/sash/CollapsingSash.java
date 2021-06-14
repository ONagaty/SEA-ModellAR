package eu.cessar.ct.core.platform.ui.widgets.sash;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Sash;

import eu.cessar.ct.core.platform.ui.events.SashCollapseEvent;
import eu.cessar.ct.core.platform.ui.events.SashCollapseListener;

public class CollapsingSash
{
	private static final int NOT_INITIALIZED = Integer.MIN_VALUE;
	private Composite parent;
	private Sash sash;
	private Label button;

	private List<SashCollapseListener> collapseListeners = new ArrayList<SashCollapseListener>();
	private boolean stickToEdge = false;
	private int lastSashLocation = NOT_INITIALIZED;
	private int firstSashLocation = NOT_INITIALIZED;

	public CollapsingSash(final Composite parent)
	{
		this.parent = parent;
		// only vertical for now
		// TODO make a layout manager for different kinds of sashes
		sash = new Sash(parent, SWT.VERTICAL | SWT.BORDER_SOLID);
		sash.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
		button = new Label(parent, SWT.BORDER_SOLID);
		button.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		button.moveAbove(sash);

		sash.addControlListener(new ControlListener()
		{
			public void controlMoved(ControlEvent e)
			{
				repositionButton();
			}

			public void controlResized(ControlEvent e)
			{
				repositionButton();
			}
		});

		sash.addPaintListener(new PaintListener()
		{
			public void paintControl(PaintEvent e)
			{
				repositionButton();
				button.redraw();
			}
		});

		button.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseDown(MouseEvent e)
			{
				collapseSash();
			}
		});

		button.addMouseTrackListener(new MouseTrackListener()
		{
			public void mouseEnter(MouseEvent e)
			{
				button.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION));
			}

			public void mouseExit(MouseEvent e)
			{
				button.setBackground(parent.getDisplay().getSystemColor(
					SWT.COLOR_WIDGET_LIGHT_SHADOW));
			}

			public void mouseHover(MouseEvent e)
			{
				// TODO Auto-generated method stub
			}
		});

	}

	public Sash getSash()
	{
		return sash;
	}

	public void setLayoutData(FormData layoutData)
	{
		firstSashLocation = layoutData.left.numerator;
		sash.setLayoutData(layoutData);
	}

	public Object getLayoutData()
	{
		return sash.getLayoutData();
	}

	public void addSelectionListener(SelectionListener listener)
	{
		sash.addSelectionListener(listener);
	}

	public void addMouseListener(MouseListener listener)
	{
		button.addMouseListener(listener);
	}

	public void repositionButton()
	{
		button.setBounds(sash.getBounds().x, sash.getBounds().y + sash.getBounds().height / 2
			- button.getBounds().height / 2, sash.getBounds().width, 30);
		button.moveAbove(sash);
	}

	public int getSashLocation(Point eventPoint, Rectangle parentClientArea)
	{
		int sashLocation = 0;

		int relSashControlPos = sash.getBounds().x;
		int sashMovedDistance = eventPoint.x - relSashControlPos;
		int newSashCompositePos = Math.min(Math.max(sash.getBounds().x + sashMovedDistance, 0),
			parentClientArea.x + parentClientArea.width - sash.getBounds().width);
		sashLocation = newSashCompositePos - parentClientArea.x;

		return sashLocation;
	}

	public int getSashLocationPercentage(Point eventPoint, Rectangle parentClientArea)
	{
		int sashAttachPercent = 0;
		int sashAttachDistance = getSashLocation(eventPoint, parentClientArea);

		sashAttachPercent = (int) (sashAttachDistance * (double) 100 / parentClientArea.width);

		return sashAttachPercent;
	}

	public int getSashLocationWhenCollapsed(Rectangle parentClientArea)
	{
		return parentClientArea.width - sash.getBounds().width;
	}

	public int getSashPercentageWhenCollapsed(Rectangle parentClientArea)
	{
		return (int) (100 * (1 - ((double) sash.getBounds().width / parentClientArea.width)));
	}

	public void addSashCollapseListener(SashCollapseListener listener)
	{
		collapseListeners.add(listener);
	}

	public void removeSashCollapseListener(SashCollapseListener listener)
	{
		collapseListeners.remove(listener);
	}

	private void collapseSash()
	{
		int collapseType = stickToEdge ? SashCollapseEvent.COLLAPSE_LEFT
			: SashCollapseEvent.COLLAPSE_RIGHT;
		stickToEdge = !stickToEdge;

		for (SashCollapseListener listener: collapseListeners)
		{
			listener.handleSashCollapseEvent(new SashCollapseEvent(collapseType));
		}

		parent.layout();
		repositionButton();
	}

	public void sashDraggedToLocation(int sashLocation)
	{
		stickToEdge = false;
		setLastSashLocation(sashLocation);
	}

	public int getLastSashLocation()
	{
		if (lastSashLocation == NOT_INITIALIZED)
		{
			setLastSashLocation((int) (((double) firstSashLocation / 100)
				* parent.getClientArea().width + parent.getClientArea().x));
		}
		return lastSashLocation;
	}

	public void setLastSashLocation(int location)
	{
		lastSashLocation = location;
	}

	public boolean isCollapsedToRight()
	{
		return stickToEdge;
	}

	public void dispose()
	{
		sash.dispose();
		button.dispose();
	}

	public void setParent(Composite parent)
	{
		sash.setParent(parent);
		button.setParent(parent);
	}
}

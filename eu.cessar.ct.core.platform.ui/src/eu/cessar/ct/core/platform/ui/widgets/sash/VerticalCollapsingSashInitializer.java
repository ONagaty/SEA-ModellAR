/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu3379 Sep 26, 2011 5:16:07 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets.sash;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import eu.cessar.ct.core.platform.ui.events.SashCollapseEvent;
import eu.cessar.ct.core.platform.ui.events.SashCollapseListener;

/**
 * @author uidu3379
 * 
 */
public class VerticalCollapsingSashInitializer
{
	private static final int FULL_PERCENT = 100;
	private static final int CONETENT_MIN_WIDTH = 50;
	private static final int CONTENT_MIN_HEIGHT = 100;
	private static final int SASH_WIDTH = 5;

	public static CollapsingSash initializeVerticalSash(final Composite p,
		final Control leftContent, final Control rightContent, int attachmentPercent)
	{
		final FormLayout form = new FormLayout();
		p.setLayout(form);

		final CollapsingSash sash = new CollapsingSash(p);

		FormData leftContentData = new FormData();
		leftContentData.left = new FormAttachment(0, 0);
		leftContentData.right = new FormAttachment(sash.getSash(), 0);
		leftContentData.top = new FormAttachment(0, 0);
		leftContentData.bottom = new FormAttachment(FULL_PERCENT, 0);
		leftContentData.width = CONETENT_MIN_WIDTH;
		leftContentData.height = CONTENT_MIN_HEIGHT;
		leftContent.setLayoutData(leftContentData);

		final FormData sashData = new FormData();
		sashData.left = new FormAttachment(attachmentPercent, 0);
		sashData.top = new FormAttachment(0, 0);
		sashData.bottom = new FormAttachment(FULL_PERCENT, 0);
		sashData.width = SASH_WIDTH;
		sash.setLayoutData(sashData);

		final FormData rightContentData = new FormData();
		rightContentData.left = new FormAttachment(sash.getSash(), 0);
		rightContentData.right = new FormAttachment(FULL_PERCENT, 0);
		rightContentData.top = new FormAttachment(0, 0);
		rightContentData.bottom = new FormAttachment(FULL_PERCENT, 0);
		rightContentData.width = CONETENT_MIN_WIDTH;
		rightContentData.height = CONTENT_MIN_HEIGHT;
		rightContent.setLayoutData(rightContentData);

		sash.repositionButton();

		// add the sash event listeners:
		// 1. one for selection events: when we move the sash by dragging it
		// the sash must attach to a new location computed by the sash itself
		// using the
		// method sash.getSashLocation(Point, Rectangle)

		sash.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				int sashLocation = sash.getSashLocation(new Point(e.x, e.y), p.getClientArea());
				if (sashData.left.numerator != sashLocation)
				{
					sashData.left = new FormAttachment(sashLocation, p.getClientArea().width, 0);
					sash.sashDraggedToLocation(sashLocation);
				}
				p.layout();
				sash.repositionButton();
			}
		});

		// 2. one for collapse events: when the two collapse buttons on the sash
		// are clicked
		// the sash must attach itself to the left or to the right edge.
		// In order to the right edge attach position, we use the method
		// sash.getSashLocationWhenCollapsed(Rectangle)

		sash.addSashCollapseListener(new SashCollapseListener()
		{
			public void handleSashCollapseEvent(SashCollapseEvent event)
			{
				int type = event.getCollapseType();
				if (type == SashCollapseEvent.COLLAPSE_LEFT)
				{
					sashData.left = new FormAttachment(sash.getLastSashLocation(),
						p.getClientArea().width, 0);
				}
				else if (type == SashCollapseEvent.COLLAPSE_RIGHT)
				{
					sashData.left = new FormAttachment(
						sash.getSashLocationWhenCollapsed(p.getClientArea()),
						p.getClientArea().width, 0);
				}
				p.layout();
			}
		});

		// 3. one for tracking resize events
		// We need to do this in order to change the sash location when the sash
		// is attached to the
		// right edge, as a consequence of a collapse event. The location of the
		// sash must keep track
		// of the new size of the p client area where it belongs

		p.addControlListener(new ControlAdapter()
		{
			@Override
			public void controlResized(ControlEvent e)
			{
				if (sash.isCollapsedToRight())
				{
					sashData.left = new FormAttachment(
						sash.getSashLocationWhenCollapsed(p.getClientArea()),
						p.getClientArea().width, 0);
				}
			}

		});

		// 4. Add the dispose listener
		p.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e)
			{
				sash.dispose();
			}
		});

		sash.getSash().setFocus();

		return sash;
	}
}

package eu.cessar.ct.core.platform.ui.events;

import org.eclipse.swt.widgets.Event;

public class SashCollapseEvent extends Event
{
	public static final int COLLAPSE_RIGHT = 1;
	public static final int COLLAPSE_LEFT = 2;
	public static final int COLLAPSE_UP = 3;
	public static final int COLLAPSE_DOWN = 4;

	private int collapseType = 0;

	public SashCollapseEvent(int type)
	{
		super();
		collapseType = type;
	}

	public int getCollapseType()
	{
		return collapseType;
	}

	public void setCollapseType(int collapseType)
	{
		this.collapseType = collapseType;
	}

}

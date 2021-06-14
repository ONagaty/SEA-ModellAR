/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jun 24, 2010 4:59:30 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import org.eclipse.jface.text.AbstractHoverInformationControlManager;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author uidl6458
 * 
 */
class CaptionHoverInformationControlManager extends AbstractHoverInformationControlManager
{

	private final ICaptionPart captionPart;

	/**
	 * @param creator
	 */
	protected CaptionHoverInformationControlManager(ICaptionPart captionPart)
	{
		super(new IInformationControlCreator()
		{

			public IInformationControl createInformationControl(Shell parent)
			{
				return new DefaultInformationControl(parent, false);
			}
		});
		this.captionPart = captionPart;
		setSizeConstraints(140, 30, false, true);

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.AbstractInformationControlManager#computeInformation()
	 */
	@Override
	protected void computeInformation()
	{
		Control control = getSubjectControl();
		if (control != null && !control.isDisposed())
		{
			Point size = control.getSize();
			Rectangle r = new Rectangle(0, 0, size.x, size.y);
			setInformation(captionPart.getDocumentation().replaceAll("\\n", "<br/>"), r); //$NON-NLS-1$//$NON-NLS-2$
		}
	}
}

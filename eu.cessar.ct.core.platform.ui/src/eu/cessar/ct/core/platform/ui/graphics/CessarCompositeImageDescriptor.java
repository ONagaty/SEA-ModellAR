/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uid95513<br/>
 * Feb 27, 2015 5:18:05 PM
 *
 * </copyright>
 */
package eu.cessar.ct.core.platform.ui.graphics;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

/**
 * Class synthesizes an image from other two images, by overlaying a decorator image over a base one
 *
 * @author uid95513
 *
 */
public class CessarCompositeImageDescriptor extends CompositeImageDescriptor
{
	private Image base;
	private Image deco;
	private int corner;
	private Point imageSize;

	/**
	 * Creates a composite descriptor. The composite image is created on call to <code>getImage</code>
	 *
	 * @param baseImage
	 *        - the base image
	 * @param decoImage
	 *        - the decorator image
	 * @param corner
	 *        - place where the decorator should be added
	 */
	public CessarCompositeImageDescriptor(final Image baseImage, final Image decoImage, final int corner)
	{
		base = baseImage;
		deco = decoImage;
		this.corner = corner;
		imageSize = new Point(baseImage.getBounds().width, baseImage.getBounds().height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.resource.CompositeImageDescriptor#drawCompositeImage(int, int)
	 */
	@Override
	protected void drawCompositeImage(int width, int height)
	{
		// method receives as parameters the width and height of the base image provided by getSize()

		// To draw the composite image, the base image is drawn first (first layer) and then the overlay image (second
		// layer)
		drawImage(base.getImageData(), 0, 0);

		ImageData decoratorImage = deco.getImageData();
		int decoWidth = decoratorImage.width;
		int decoHeight = decoratorImage.height;

		switch (corner)
		{
			case IDecoration.TOP_LEFT:
			{
				// overlay in the top-left corner
				drawImage(decoratorImage, 0, 0);
				break;
			}
			case IDecoration.TOP_RIGHT:
			{
				// overlay in the top-right corner
				drawImage(decoratorImage, width - decoWidth, 0);
				break;
			}
			case IDecoration.BOTTOM_LEFT:
			{
				// overlay in the bottom-left corner
				drawImage(decoratorImage, 0, height - decoHeight);
				break;
			}
			case IDecoration.BOTTOM_RIGHT:
			{
				// overlay in the bottom-right corner
				drawImage(decoratorImage, width - decoWidth, height - decoHeight);
				break;
			}
			default:
				break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.resource.CompositeImageDescriptor#getSize()
	 */
	@Override
	protected Point getSize()
	{
		return imageSize;
	}

	/**
	 * Get the image formed by overlaying different images on the base image
	 *
	 * @return composite image
	 */
	public Image getImage()
	{
		return createImage();
	}
}

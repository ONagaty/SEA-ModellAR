package eu.cessar.ct.core.mms.providers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.osgi.framework.Bundle;

/**
 * 
 * Abstract implementation of a label text and label icon provider, for tree items.
 * 
 */
public abstract class AbstractItemLabelProvider extends AdapterImpl implements IItemLabelProvider
{
	private Adapter parentAdapter;
	private Map<String, Object> imagesMapping = new HashMap<String, Object>();
	private volatile Map<EClass, String> imagesPathMapping;

	public AbstractItemLabelProvider(Adapter parentAdapter)
	{
		this.parentAdapter = parentAdapter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.edit.provider.IItemLabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object object)
	{
		return ((IItemLabelProvider) parentAdapter).getText(object);
	}

	/**
	 * @return
	 */
	protected abstract Bundle getBundle();

	/**
	 * @return
	 */
	protected Map<EClass, String> getImagesPathMapping()
	{
		if (imagesPathMapping == null)
		{
			synchronized (this)
			{
				if (imagesPathMapping == null)
				{
					imagesPathMapping = new HashMap<EClass, String>();
					doInitImagesPathMapping(imagesPathMapping);
				}
			}
		}
		return imagesPathMapping;
	}

	/**
	 * @param pathMapping
	 */
	protected abstract void doInitImagesPathMapping(Map<EClass, String> pathMapping);

	// CHECKSTYLE:OFF
	// disabled checkstyles here because it was to risky to refactor this method.
	// If working in this area, please consider enabling checkstyles
	// and removing the errors.
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.edit.provider.IItemLabelProvider#getImage(java.lang.Object)
	 */
	public Object getImage(Object object)
	{
		URL imageURL;
		EClass eClass = ((EObject) object).eClass();
		String path = getImagesPathMapping().get(eClass);
		Object imageFromParent = ((IItemLabelProvider) parentAdapter).getImage(object);

		if (path == null)
		{
			return imageFromParent;
		}

		imageURL = (URL) imagesMapping.get(path);
		if (imageURL == null)
		{
			imageURL = FileLocator.find(getBundle(), new Path(path), null);
			if (imageURL == null)
			{
				return imageFromParent;
			}
			try
			{
				InputStream inputStream = imageURL.openStream();
				inputStream.close();
			}
			catch (IOException e)
			{
				return imageFromParent;
			}
			imagesMapping.put(path, imageURL);
		}

		return imageURL;
	}
	// CHECKSTYLE:ON

}

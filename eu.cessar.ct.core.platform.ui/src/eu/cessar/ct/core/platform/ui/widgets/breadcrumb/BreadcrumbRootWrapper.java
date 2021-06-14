package eu.cessar.ct.core.platform.ui.widgets.breadcrumb;

/**
 * @author uidl7321
 * 
 */
public class BreadcrumbRootWrapper
{
	private Object wrappedObject;

	/**
	 * @param wrappedObject
	 */
	public BreadcrumbRootWrapper(Object wrappedObject)
	{
		this.wrappedObject = wrappedObject;
	}

	/**
	 * @return
	 */
	public Object getWrappedObject()
	{
		return wrappedObject;
	}
}

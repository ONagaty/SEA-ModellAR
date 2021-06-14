/**
 * 
 */
package eu.cessar.ct.testutils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.Bundle;

public class ApplicationContextWrapper implements IApplicationContext
{
	private Map<String, String[]> argumentsMap;

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplicationContext#applicationRunning()
	 */
	public void applicationRunning()
	{
	}

	/**
	 * 
	 */
	public void setArguments(final String[] args)
	{
		argumentsMap = new HashMap<String, String[]>();
		argumentsMap.put(IApplicationContext.APPLICATION_ARGS, args);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplicationContext#getArguments()
	 */
	@SuppressWarnings("unchecked")
	public Map getArguments()
	{
		return argumentsMap;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplicationContext#getBrandingApplication()
	 */
	public String getBrandingApplication()
	{
		return "";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplicationContext#getBrandingBundle()
	 */
	public Bundle getBrandingBundle()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplicationContext#getBrandingDescription()
	 */
	public String getBrandingDescription()
	{
		return "";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplicationContext#getBrandingId()
	 */
	public String getBrandingId()
	{
		return "";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplicationContext#getBrandingName()
	 */
	public String getBrandingName()
	{
		return "";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplicationContext#getBrandingProperty(java.lang.String)
	 */
	public String getBrandingProperty(final String key)
	{
		return "";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplicationContext#setResult(java.lang.Object, org.eclipse.equinox.app.IApplication)
	 */
	public void setResult(final Object result, final IApplication application)
	{
		// do nothing
		// this method appear in Eclipse 3.6
	}
}
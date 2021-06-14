package eu.cessar.ct.core.platform;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;

/**
 * 
 * The interface that all cessar plugins need to implement. By convention, all
 * activators are named "CessarPluginActivator"
 * 
 * @author uidl6458
 * 
 * @Review uidl6458 - 12.04.2012
 * 
 */
public interface ICessarPlugin
{

	/**
	 * @return
	 */
	public String getPluginID();

	/**
	 * Return the debug option <code>debugOption</code> of the selected plugin.
	 * Please note that the string should not contain also the pluginID, this is
	 * filled automatically. For details see
	 * {@link Platform#getDebugOption(String)}
	 * 
	 * @param debugOption
	 * @return
	 */
	public String getDebugOption(String debugOption);

	/**
	 * @param message
	 * @param args
	 */
	public void logError(String message, Object... args);

	/**
	 * @param e
	 */
	public void logError(Throwable t);

	/**
	 * @param e
	 * @param message
	 * @param args
	 */
	public void logError(Throwable t, String message, Object... args);

	/**
	 * @param message
	 * @param args
	 */
	public void logWarning(String message, Object... args);

	/**
	 * @param e
	 */
	public void logWarning(Throwable t);

	/**
	 * @param e
	 * @param message
	 * @param args
	 */
	public void logWarning(Throwable t, String message, Object... args);

	/**
	 * @param message
	 * @param args
	 */
	public void logInfo(String message, Object... args);

	/**
	 * @param e
	 */
	public void logInfo(Throwable t);

	/**
	 * @param e
	 * @param message
	 * @param args
	 */
	public void logInfo(Throwable t, String message, Object... args);

	/**
	 * @param severity
	 * @param message
	 * @param args
	 */
	public void log(int severity, String message, Object... args);

	/**
	 * @param severity
	 * @param e
	 */
	public void log(int severity, Throwable t);

	/**
	 * @param severity
	 * @param e
	 * @param message
	 * @param args
	 */
	public void log(int severity, Throwable t, String message, Object... args);

	/**
	 * @param status
	 */
	public void log(IStatus status);

	/**
	 * Create a IStatus from a Throwable
	 * 
	 * @param t
	 * @return
	 */
	public IStatus createStatus(Throwable t);

	/**
	 * @param severity
	 * @param message
	 * @param args
	 * @return
	 */
	public IStatus createStatus(int severity, String message, Object... args);

	/**
	 * @return
	 */
	public boolean isLicenseValid();
}

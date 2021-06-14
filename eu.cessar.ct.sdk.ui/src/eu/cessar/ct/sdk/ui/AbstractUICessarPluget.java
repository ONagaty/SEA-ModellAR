package eu.cessar.ct.sdk.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;

import eu.cessar.ct.sdk.AbstractCessarPluget;

/**
 * Super class that any pluget that needs an UI shall extend
 * 
 * @author uidl6458
 * 
 */
public abstract class AbstractUICessarPluget extends AbstractCessarPluget
{

	/**
	 * Override the run method. A {@link Display} will be located and
	 * {@link #runUI(Display, IProgressMonitor, String[])} will be executed from UI thread
	 */
	@Override
	public final void run(final IProgressMonitor monitor, final String[] args)
	{
		// get the default display
		final Display display = Display.getDefault();

		// All code that change the UI need to be executed from UI thread.
		// also, we dont't want to finish the pluget until the UI is finished
		// so we will use syncExec instead of asyncExec
		display.syncExec(new Runnable()
		{

			public void run()
			{
				runUI(display, monitor, args);
			}
		});
	}

	/**
	 * The main method of any pluget with UI. The method will be executed from UI thread. Implementors should avoid
	 * executing long running operations directly from the method.
	 * 
	 * @param display
	 *        the SWT {@link Diplay} to use
	 * @param monitor
	 *        the progress monitor
	 * @param args
	 *        the arguments given by the user to the pluget
	 */
	public abstract void runUI(Display display, IProgressMonitor monitor, String[] args);

}

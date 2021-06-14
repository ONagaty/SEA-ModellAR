/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Dec 5, 2013 11:21:51 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.actions;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;

import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.ui.internal.Messages;

/**
 * Handler for the 'CESSAR FAQ' Help menu item
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Dec 12 17:29:10 2013 %
 * 
 *         %version: 1 %
 */
public class FAQHandler extends AbstractHandler
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		Exception ex = null;
		try
		{
			IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport().createBrowser(null);
			URL url = new URL(Messages.FAQ_url);

			browser.openURL(url);
		}
		catch (PartInitException e)
		{
			ex = e;
		}
		catch (MalformedURLException e)
		{
			ex = e;
		}

		if (ex != null)
		{
			CessarPluginActivator.getDefault().logError(ex);
			MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.error_openingFAQ_title,
				NLS.bind(Messages.error_openingFAQ_message, ex.getMessage()));
		}

		return null;
	}
}

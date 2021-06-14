/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4098<br/>
 * 17.07.2014 10:41:12
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.ui.internal.wizards;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.runtime.ecuc.ui.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.ecuc.ui.internal.Messages;

/**
 * Popup dialog displaying contextual information. Specialized to display title-text pairs and links to resources.
 *
 * @author uidg4098
 *
 *         %created_by: uid95856 %
 *
 *         %date_created: Fri Mar 13 13:33:28 2015 %
 *
 *         %version: TAUTOSAR~5 %
 */
public class MessagePopup extends PopupDialog
{

	private Map<String, String> textMap;
	private Composite window;
	private Rectangle region;
	private Map<String, File> files;

	/**
	 * Object of type {@link Rectangle}
	 */
	private Rectangle rectangle;

	/**
	 * Default constructor
	 *
	 * @param parent
	 *        of type {@link Shell}
	 * @param rectangle
	 *        of type {@link Rectangle}
	 * @param headerString
	 *        of type String indicating the header
	 * @param footerString
	 *        of type String indicating the footer
	 */
	@SuppressWarnings("deprecation")
	public MessagePopup(Shell parent, Rectangle rectangle, String headerString, String footerString)
	{
		super(parent, PopupDialog.INFOPOPUPRESIZE_SHELLSTYLE, true, false, true, false, headerString, footerString);
		this.rectangle = rectangle;
	}

	/**
	 * @param parent
	 * @param rectangle
	 * @param text
	 * @param files
	 *        map containing the user manual and release notes file with the message shown in the UI as key
	 */
	public MessagePopup(Shell parent, Rectangle rectangle, Map<String, String> text, Map<String, File> files)
	{
		this(parent, rectangle, "Plugin description", null); //$NON-NLS-1$
		textMap = text;
		this.files = files;
		region = rectangle;
	}

	@Override
	protected Control createDialogArea(Composite parent)
	{
		window = new Composite(parent, SWT.NONE);
		window.setLayout(new GridLayout(1, false));
		GridData gd;

		Label textLabel;

		for (String key: textMap.keySet())
		{
			if (!key.equals(Messages.PLUGET_EMPTY_KEY))
			{
				textLabel = new Label(window, SWT.WRAP);
				gd = new GridData();
				textLabel.setText(key + ":"); //$NON-NLS-1$
				textLabel.setFont(getBoldFont(textLabel));
				textLabel.setLayoutData(gd);
			}

			gd = new GridData();
			textLabel = new Label(window, SWT.WRAP);
			if (textMap.get(key).length() > 0)
			{
				textLabel.setText(textMap.get(key) + "\n\n"); //$NON-NLS-1$
			}
			else
			{
				textLabel.setFont(getBoldFont(textLabel));
				textLabel.setText("Not available\n\n"); //$NON-NLS-1$
			}
			gd.widthHint = region.width - 20;
			textLabel.setLayoutData(gd);
		}

		gd = new GridData();
		if (files.size() > 0)
		{
			gd = new GridData();
			Label linkTitle = new Label(window, SWT.NONE);
			linkTitle.setText("See also:"); //$NON-NLS-1$
			linkTitle.setFont(getBoldFont(linkTitle));
			linkTitle.setLayoutData(gd);

			for (String message: files.keySet())
			{
				createLinkLabel(parent, message, files.get(message));
			}
		}

		return parent;
	}

	private Font getBoldFont(Label l)
	{
		FontData fontData = l.getFont().getFontData()[0];
		Font font = new Font(l.getDisplay(), new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
		return font;
	}

	private Link createLinkLabel(Composite parent, String text, final File fileToOpen)
	{
		if (fileToOpen != null && fileToOpen.exists())
		{
			GridData gd = new GridData();
			Link l = new Link(window, SWT.NONE);
			l.setText("<a>" + text + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
			l.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
			l.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					openURL(fileToOpen);
				}
			});
			l.setLayoutData(gd);
			return l;
		}
		return null;
	}

	/**
	 * Opens the file in the default desktop application assigned to its extension. For HTML resources the Help Window
	 * is shown.
	 *
	 * @param fileToOpen
	 *        resource to be displayed
	 */
	protected void openURL(File fileToOpen)
	{
		if (fileToOpen.exists())
		{
			if (fileToOpen.getAbsolutePath().endsWith(".html")) //$NON-NLS-1$
			{
				try
				{
					String url = fileToOpen.toURI().toURL().toString();
					PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(url);
				}
				catch (MalformedURLException e)
				{
					CessarPluginActivator.getDefault().logError(e);
				}
			}
			else if (Desktop.isDesktopSupported())
			{
				try
				{
					Desktop.getDesktop().open(fileToOpen);
				}
				catch (IOException ex)
				{
					CessarPluginActivator.getDefault().logError(ex);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.PopupDialog#getFocusControl()
	 */
	@Override
	protected Control getFocusControl()
	{
		return window;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.window.Window#handleShellCloseEvent()
	 */
	@Override
	protected void handleShellCloseEvent()
	{
		super.handleShellCloseEvent();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.PopupDialog#createTitleMenuArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createTitleMenuArea(Composite arg0)
	{
		Control ctrl = super.createTitleMenuArea(arg0);
		Composite composite = (Composite) ctrl;
		Control[] ctrls = composite.getChildren();

		ToolBar toolBar = (ToolBar) ctrls[1];
		ToolItem[] toolItems = toolBar.getItems();
		toolItems[0].setImage(null);

		return ctrl;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.PopupDialog#fillDialogMenu(org.eclipse.jface.action.IMenuManager)
	 */
	@Override
	protected void fillDialogMenu(IMenuManager dialogMenu)
	{
		dialogMenu.addMenuListener(new IMenuListener()
		{
			public void menuAboutToShow(IMenuManager arg0)
			{
				handleShellCloseEvent();
			}
		});

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.PopupDialog#adjustBounds()
	 */
	@Override
	protected void adjustBounds()
	{
		Point pt = getShell().getDisplay().getCursorLocation();
		getShell().setBounds(pt.x + 10, pt.y + 10, rectangle.width, rectangle.height);
	}
}

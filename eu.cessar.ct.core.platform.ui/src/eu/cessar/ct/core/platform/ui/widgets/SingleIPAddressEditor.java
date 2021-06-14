/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944<br/>
 * 15.02.2013 16:49:57
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import eu.cessar.ct.core.internal.platform.ui.CessarPluginActivator;
import eu.cessar.ct.core.platform.ui.IdentificationUtils;
import eu.cessar.ct.core.platform.ui.events.EFocusEvent;
import eu.cessar.ct.core.platform.ui.events.IFocusEventListener;

/**
 * IP address generic editor implementation. Caters for both IPv4 and IPv6 formats
 * 
 * @author uidu0944
 * 
 *         %created_by: uidu0944 %
 * 
 *         %date_created: Fri Mar  8 11:23:56 2013 %
 * 
 *         %version: 2 %
 */
public class SingleIPAddressEditor extends AbstractSingleEditor<InetAddress>
{
	private Composite ipAddressComposite;
	private Text[] addressBlocks;
	private Label[] separators;

	private String separator;
	private String defaultAddressBlockValue;
	private int addressBlockNumber;
	private String addressBlockRegexp;

	/**
	 * Since the IP address editor can be used with either IPv4 or IPv6 addresses, several parameters are exposed
	 * 
	 * @param separator
	 *        "." for IPv4 and ":" for IPv6
	 * @param defaultAddressBlockValue
	 *        "0" for IPv4 and "0000" for IPv6
	 * @param addressBlockNumber
	 *        4 for IPV4 and 8 for IPv6
	 * @param addressBlockRegexp
	 *        according to Autosar document
	 */
	public SingleIPAddressEditor(String separator, String defaultAddressBlockValue, int addressBlockNumber,
		String addressBlockRegexp)
	{
		super(true);
		this.separator = separator;
		this.defaultAddressBlockValue = defaultAddressBlockValue;
		this.addressBlockNumber = addressBlockNumber;
		this.addressBlockRegexp = addressBlockRegexp;
	}

	/**
	 * @param acceptNull
	 */
	public SingleIPAddressEditor(boolean acceptNull)
	{
		super(acceptNull);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#createEditor(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createEditor(Composite parent)
	{
		ipAddressComposite = renderIPEditor(parent);
		ipAddressComposite.setData(IdentificationUtils.KEY_NAME, getIdentificationPrefix()
			+ "#" + IdentificationUtils.TEXT_ID); //$NON-NLS-1$
		return ipAddressComposite;
	}

	private Composite renderIPEditor(Composite parent)
	{
		Color white = parent.getDisplay().getSystemColor(SWT.COLOR_WHITE);
		ipAddressComposite = new Composite(parent, SWT.BORDER | SWT.RESIZE);
		ipAddressComposite.setBackground(white);
		ipAddressComposite.setLayout(new GridLayout(2 * addressBlockNumber - 1, false));
		ipAddressComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)/* new FormData() */);

		addressBlocks = new Text[addressBlockNumber];
		separators = new Label[addressBlockNumber - 1];

		GC gc = new GC(ipAddressComposite);
		String mask = "123"; //$NON-NLS-1$
		if (addressBlockNumber == 8)
		{
			mask = "1234"; //$NON-NLS-1$
		}
		Point size = gc.stringExtent(mask);
		int w = size.x;
		gc.dispose();

		for (int i = 0; i < (2 * addressBlockNumber - 1); i++)
		{
			if (i % 2 == 0)
			{
				Text t = new Text(ipAddressComposite, SWT.NONE);
				t.setBackground(white);
				t.addVerifyListener(new IPAddressBlockVerifyKeyListener());
				GridData gd = new GridData();

				gd.widthHint = w;
				t.setLayoutData(gd);
				addressBlocks[i / 2] = t;
			}
			else
			{
				Label l = new Label(ipAddressComposite, SWT.NONE);
				l.setBackground(white);
				l.setText(separator);
				separators[(i + 2) / 2 - 1] = l;
			}
		}

		// handle focus lost for last address block
		addFocusListener(addressBlocks[addressBlockNumber - 1]);

		return ipAddressComposite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#getDataFromUI()
	 */
	@Override
	protected InetAddress getDataFromUI()
	{
		InetAddress addr = null;
		try
		{
			addr = InetAddress.getByName(computeIPAddressStringFromUI(true));
		}
		catch (UnknownHostException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return addr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#isNull(java.lang.Object)
	 */
	@Override
	protected boolean isNull(InetAddress data)
	{
		return false;
	}

	/**
	 * Set UI address blocks with the attribute value in the autosar file
	 */
	@Override
	protected void setDataToUI(InetAddress data)
	{
		if (null != data)
		{
			String[] sAddressBlocks = data.getHostAddress().split("[" + separator + "]"); //$NON-NLS-1$ //$NON-NLS-2$
			if (sAddressBlocks.length == addressBlockNumber)
			{
				for (int j = 0; j < sAddressBlocks.length; j++)
				{
					// compensate the 0000 default value fo IPv6
					if (sAddressBlocks[j].equals("0")) //$NON-NLS-1$
					{
						addressBlocks[j].setText(defaultAddressBlockValue);
					}
					else
					{
						addressBlocks[j].setText(sAddressBlocks[j]);
					}
				}
			}
		}
		else
		{
			for (int j = 0; j < addressBlocks.length; j++)
			{
				addressBlocks[j].setText(""); //$NON-NLS-1$
			}
		}

	}

	/**
	 * 
	 * Verifies IPvX address blocks are valid (satisfy the regular expression)
	 * 
	 * @author uidu0944
	 * 
	 *         %created_by: uidu0944 %
	 * 
	 *         %date_created: Fri Mar  8 11:23:56 2013 %
	 * 
	 *         %version: 2 %
	 */
	protected final class IPAddressBlockVerifyKeyListener implements VerifyListener, VerifyKeyListener
	{

		final Pattern pattern = Pattern.compile(addressBlockRegexp);

		public void verifyText(VerifyEvent verifyevent)
		{
			verify(verifyevent);
		}

		public void verifyKey(VerifyEvent verifyevent)
		{
			verify(verifyevent);
		}

		private void verify(VerifyEvent e)
		{
			// String stringAddressFromUI = computeIPAddressStringFromUI(false);
			// do not verify against the regexp if IP address is empty
			// if (null != stringAddressFromUI)
			// {
			String string = e.text;
			char[] chars = new char[string.length()];
			string.getChars(0, chars.length, chars, 0);

			Text text = (Text) e.getSource();
			e.text = new String(chars);

			final String oldS = text.getText();
			String newS = oldS.substring(0, e.start) + e.text + oldS.substring(e.end);

			if (!newS.isEmpty())
			{
				Matcher matcher = pattern.matcher(newS);
				if (!matcher.matches())
				{
					e.doit = false;
					return;
				}
			}
			// }
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractEditor#setEventListener(eu.cessar.ct.core.platform.ui.events.
	 * IFocusEventListener)
	 */
	@Override
	public void setEventListener(IFocusEventListener listener)
	{
		IFocusEventListener fELis = new IFocusEventListener()
		{
			// fill up with zero(s) any address bloc(k) accidentally left empty
			@Override
			public void notify(EFocusEvent event)
			{
				try
				{
					if (null != computeIPAddressStringFromUI(false))
					{
						setDataToUI(InetAddress.getByName(computeIPAddressStringFromUI(true)));
					}
					notifyAcceptData(getOldInputData(), getInputData());
				}
				catch (UnknownHostException e)
				{
					CessarPluginActivator.getDefault().logError(e);
				}

			}
		};
		super.setEventListener(fELis);
	}

	/**
	 * Compute address string from UI
	 * 
	 * @param fillWithZero
	 *        flag indicates whether the empty address blocks will be filled up with default value 0
	 * @return
	 */
	private String computeIPAddressStringFromUI(boolean fillWithZero)
	{
		StringBuffer sB = new StringBuffer();

		for (int i = 0; i < (2 * addressBlockNumber - 1); i++)
		{
			if (i % 2 == 0)
			{
				if (!fillWithZero)
				{
					sB.append(addressBlocks[i / 2].getText());
				}
				else
				{
					if (addressBlocks[i / 2].getText().isEmpty())
					{
						sB.append(defaultAddressBlockValue);
					}
					else
					{
						sB.append(addressBlocks[i / 2].getText());
					}
				}
			}
			else
			{
				sB.append(separator);
			}
		}
		String addrStringNotSet = ""; //$NON-NLS-1$
		for (int i = 0; i < (addressBlockNumber - 1); i++)
		{
			addrStringNotSet += separator;
		}
		return addrStringNotSet.equals(sB.toString()) ? null : sB.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#setStatusMessage(org.eclipse.core.runtime.IStatus)
	 */
	@Override
	public void setStatusMessage(IStatus status)
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#getDefaultData()
	 */
	@Override
	protected Inet4Address getDefaultData()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#haveUI()
	 */
	@Override
	protected boolean haveUI()
	{
		return ipAddressComposite != null && !ipAddressComposite.isDisposed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#doSetReadOnly()
	 */
	@Override
	protected void doSetReadOnly()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#doSetEnabled(boolean)
	 */
	@Override
	protected void doSetEnabled(boolean enabled)
	{
	}

}

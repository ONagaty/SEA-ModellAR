package com.continental.license.internal.core;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
	private static final String BUNDLE_NAME = "com.continental.license.internal.core.messages"; //$NON-NLS-1$
	public static String licenseDialog_label_DistributedTo;

	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
		// do nothing
	}
}

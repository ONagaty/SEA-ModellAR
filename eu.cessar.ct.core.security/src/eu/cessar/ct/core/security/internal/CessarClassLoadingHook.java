/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6343 Feb 22, 2011 2:27:15 PM </copyright>
 */
package eu.cessar.ct.core.security.internal;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.eclipse.osgi.internal.hookregistry.ClassLoaderHook;
import org.eclipse.osgi.internal.loader.classpath.ClasspathEntry;
import org.eclipse.osgi.internal.loader.classpath.ClasspathManager;
import org.eclipse.osgi.storage.bundlefile.BundleEntry;

/**
 * Class loading hook that perform the decryption if the product is licensed
 *
 * @author uidl6458
 *
 *         %created_by: uidl7321 %
 *
 *         %date_created: Wed Jul  1 16:20:31 2015 %
 *
 *         %version: 5.1.4 %
 */
@SuppressWarnings("restriction")
public class CessarClassLoadingHook extends ClassLoaderHook
{

	private static final byte[] CES_SIGNATURE = {(byte) 0xCE, (byte) 0x55, (byte) 0xBA, (byte) 0xBE};

	private static final String ALGORITHM = "AES"; //$NON-NLS-1$

	private ThreadLocal<Cipher> cipher = new ThreadLocal<Cipher>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.security.internal.AbstractClassLoadingHook#processClass(java.lang.String, byte[],
	 * org.eclipse.osgi.baseadaptor.loader.ClasspathEntry, org.eclipse.osgi.baseadaptor.bundlefile.BundleEntry,
	 * org.eclipse.osgi.baseadaptor.loader.ClasspathManager)
	 */
	@SuppressWarnings("nls")
	@Override
	public byte[] processClass(String name, byte[] classbytes, ClasspathEntry classpathEntry, BundleEntry entry,
		ClasspathManager manager)
	{
		if (name != null
			&& name.startsWith("eu.cessar.ct")
			&& (isCessSignature(classbytes) || name.toLowerCase().contains(
				"eu.cessar.ct.core.internal.platform.cessarpluginactivator")))
		{
			if (SecurityUtils.haveLicense())
			{
				try
				{
					Cipher aCipher = getCipher();

					// For development use only. Forces license check
					if (name.toLowerCase().contains("eu.cessar.ct.core.internal.platform.cessarpluginactivator")
						&& !isCessSignature(classbytes))
					{
						return null;
					}

					return aCipher.doFinal(classbytes, CES_SIGNATURE.length, classbytes.length - CES_SIGNATURE.length);
				}
				// SUPPRESS CHECKSTYLE allow catching everything
				catch (Exception e)
				{
					// SUPPRESS CHECKSTYLE print on the console because we are in very early stage of execution
					e.printStackTrace();
					ClassFormatError error = new ClassFormatError("The class " + name + " has an invalid content"); //$NON-NLS-1$ //$NON-NLS-2$
					error.initCause(e);
					// put it on null so the next call will create a new one
					cipher.set(null);
					throw error;
				}
			}
			else
			{
				throw new SecurityViolationError("Cannot start without license");
			}
		}
		return null;
	}

	/**
	 * @param classbytes
	 * @return
	 */
	private boolean isCessSignature(byte[] classbytes)
	{
		if (classbytes != null && classbytes.length >= CES_SIGNATURE.length)
		{
			for (int i = 0; i < CES_SIGNATURE.length; i++)
			{
				if (classbytes[i] != CES_SIGNATURE[i])
				{
					return false;
				}
			}
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * @return
	 * @throws Exception
	 */
	private Cipher getCipher() throws Exception
	{
		Cipher result = cipher.get();
		if (result == null)
		{
			SecretKeySpec keySpec = new SecretKeySpec(SecurityUtils.getDecryptionKey(), ALGORITHM);
			result = Cipher.getInstance(ALGORITHM);
			result.init(Cipher.DECRYPT_MODE, keySpec);
			cipher.set(result);
		}
		return result;

	}

}

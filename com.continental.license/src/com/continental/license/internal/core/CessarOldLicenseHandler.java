package com.continental.license.internal.core;

import java.io.File;

import org.eclipse.core.runtime.Platform;

public class CessarOldLicenseHandler
{

	/**
	 * The name of the parameter that can be used to specify another Cessar license folder
	 * 
	 */
	protected static final String CESSAR_LICENSE_FOLDER = "CessarLicenseFolder"; //$NON-NLS-1$

	/** Default license folder */
	private static final String LICENSE_FOLDER = "/Application Data/Conti Engineering/CESSAR-CT"; //$NON-NLS-1$

	/**
	 * Return the message to display in the splash screen with the information on the license
	 * associated to the given product
	 * 
	 * @param productName
	 *        the product name
	 * @return the message or null if there is no license for the product
	 */
	public static String createLicenseSplashMessage(String productName)
	{

		String licMessage = null;

		// Get the installed license
		ILicense license = getLicense(productName);
		if (license != null)
		{
			// Initialization
			String firstName = null;
			String lastName = null;
			String company = null;

			// Get the license info
			LicenseInfo licInfo = license.getLicenseInfo();
			if (licInfo != null)
			{
				// Get the first name
				firstName = licInfo.getFirstName();
				// Get the last name
				lastName = licInfo.getLastName();
				// Get the company
				company = licInfo.getCompany();
			}

			// Create the license message
			StringBuffer buffer = new StringBuffer();
			if (firstName != null && firstName.length() > 0)
			{
				buffer.append(" ").append(firstName); //$NON-NLS-1$
			}
			if (lastName != null && lastName.length() > 0)
			{
				buffer.append(" ").append(lastName); //$NON-NLS-1$
			}
			buffer.append(", "); //$NON-NLS-1$
			if (company != null && company.length() > 0)
			{
				buffer.append(" ").append(company); //$NON-NLS-1$
			}
			if (buffer.length() == 0)
			{
				buffer.append(" Unknown"); //$NON-NLS-1$
			}
			licMessage = Messages.licenseDialog_label_DistributedTo + buffer.toString();
		}

		return licMessage;
	}

	/**
	 * Return the ILicense for the specified product
	 * 
	 * @param productName
	 *        the product name
	 * @return the ILicense if installed, otherwise null
	 */
	public static ILicense getLicense(String productName)
	{
		return LicenseHandler.getInstance().getLicense(productName);
	}

	/**
	 * Check the license for the given module of the given product.
	 * 
	 * @param productName
	 *        the product name
	 * @param moduleName
	 *        the module name
	 * @return true if the license is valid for the given module of the given product, otherwise
	 *         false
	 */
	public static boolean checkLicense(String productName, String moduleName)
	{
		LicenseHandler licenseHandler = LicenseHandler.getInstance();
		boolean valid = true;

		// Check if license installed
		ILicense license = CessarOldLicenseHandler.getLicense(productName);
		if (license == null)
		{
			try
			{
				// Set the license folder if needed
				_setLicenseFolderIfNeeded(productName);

				// Try to install an existing license for the product
				licenseHandler.installLicenseInFolder(productName);

				// Check the installed license
				valid = checkLicense(productName, moduleName);
			}
			catch (InvalidLicenseException e)
			{
				// License does not exist
				valid = false;
			}
		}
		else
		{
			// License exist
			// Check if module exists
			boolean moduleExist = licenseHandler.checkModuleExist(productName, moduleName);

			if (moduleExist)
			{
				// Check expiration date
				if (licenseHandler.moduleIsExpired(productName, moduleName))
				{
					// License is expired
					valid = false;
				}

				if (valid)
				{
					// Check permission
					Permission perm = licenseHandler.getModulePermission(productName, moduleName);
					valid = Permission.YES.equals(perm);
				}
			}
			else
			{
				// The module does not exist
				valid = false;
			}
		}
		return valid;
	}

	/**
	 * Set the license folder if it is not set.
	 * 
	 * @param productName
	 *        the product name
	 */
	public static void _setLicenseFolderIfNeeded(String productName)
	{
		LicenseHandler licenseHandler = LicenseHandler.getInstance();

		if (licenseHandler.getLicenseFolder() == null)
		{
			File licFolder = null;
			String cessarLicFolderStr = System.getProperty(CESSAR_LICENSE_FOLDER);
			if (cessarLicFolderStr != null)
			{
				licFolder = new File(cessarLicFolderStr);
				if (!licFolder.exists())
				{
					licFolder.mkdirs();
				}
				/*File cessarLicFolder = new File(cessarLicFolderStr);
				// Checks if a license exists in the folder
				if (licenseHandler.checkIfLicenseExistInFolder(cessarLicFolder, productName))
				{
					licFolder = cessarLicFolder;
				}*/
			}else{
			if (licFolder == null)
			{
				String parentFolder = System.getenv("ALLUSERSPROFILE");
				if (parentFolder == null)
				{
					// roll back to the install location
					parentFolder = Platform.getInstallLocation().getURL().getFile();
				}
				licFolder = new File(parentFolder, LICENSE_FOLDER);
				if (!licFolder.exists())
				{
					licFolder.mkdirs();
				}
			}
			}
			// Set the license folder
			LicenseHandler.getInstance().setLicenseFolder(licFolder);
		}
	}

}

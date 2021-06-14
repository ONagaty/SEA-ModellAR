package eu.cessar.ct.core.security.license;

import com.continental.license.internal.core.CessarOldLicenseHandler;

import eu.cessar.ct.license.internal.CessarNewLicenseHandler;
import eu.cessar.ct.license.internal.exceptions.LicenseExpiredException;
import eu.cessar.ct.license.internal.exceptions.LicenseIsValidException;
import eu.cessar.ct.license.internal.exceptions.LicenseNotDefinedException;
import eu.cessar.ct.license.internal.impl.LicenseSettings;

/**
 * @author uidt2045
 * 
 */
public class LicenseApi
{

	private static boolean isValidLicense = isLicenseValid();

	/**
	 * @return a message for the splash handler, which contains information about the user of this license and his
	 *         company
	 */
	public static String getProductMessage()
	{
		if (CessarOldLicenseHandler.checkLicense(CessarLicenseConstants.PRODUCT_CESSAR,
			CessarLicenseConstants.MODULE_CESSAR))
		{
			return CessarOldLicenseHandler.createLicenseSplashMessage(CessarLicenseConstants.PRODUCT_CESSAR);
		}

		else
		{
			return CessarNewLicenseHandler.createLicenseSplashMessage(CessarLicenseConstants.PRODUCT_CESSAR);
		}

		// return null;
	}

	/**
	 * Will not reload the license at every call
	 * 
	 * @return <b>true</b> if it has a valid license, either a new one or an old one,<br>
	 *         <b>false</b> otherwise
	 */
	public static boolean hasValidLicense()
	{
		if (!isValidLicense)
		{
			isValidLicense = isLicenseValid();
			return isValidLicense;
		}
		return isValidLicense;
	}

	/**
	 * Will <b>reload</b> the license
	 * 
	 * @return true if the current license is valid
	 */
	private static boolean isLicenseValid()
	{
		boolean checkLfLicense = CessarOldLicenseHandler.checkLicense(CessarLicenseConstants.PRODUCT_CESSAR,
			CessarLicenseConstants.MODULE_CESSAR);

		if (!checkLfLicense /* && LicenseSettings.usingLicenseProtector() || LicenseSettings.usingFloatingLicense() */)
		{
			try
			{
				LicenseSettings.initialCheck();
			}
			catch (LicenseIsValidException e)
			{
				return true;
			}
			catch (LicenseNotDefinedException e)
			{
				return false;
			}
			catch (LicenseExpiredException e)
			{
				return false;
			}
		}

		return checkLfLicense;
	}

}

package eu.cessar.ct.core.security.license;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LicenseHandler
{

	/** Singleton */
	private static LicenseHandler __instance = null;

	/** License folder */
	private static File _licenseFolder = null;

	/** License map */
	private Map<String, LicenseDef> licenseMap = null;

	/**
	 * Return the local full host ID
	 *
	 * @return the local full host ID
	 */
	public static String getLocalFullHostID()
	{

		return HostID.getFullID();
	}

	/**
	 * Return the singleton
	 *
	 * @return the singleton
	 */
	public static LicenseHandler getInstance()
	{

		if (__instance == null)
		{
			__instance = new LicenseHandler();
		}
		return __instance;
	}

	/**
	 * Default constructor
	 */
	public LicenseHandler()
	{
		licenseMap = new HashMap<String, LicenseDef>();
	}

	/**
	 * Get the license folder
	 *
	 * @return the license folder
	 */
	public File getLicenseFolder()
	{

		return _licenseFolder;
	}

	/**
	 * Set the license folder. When the license folder is set, all license files present in that folder will be read.
	 * Any license already processed from other folder will be discarded.
	 *
	 * @param folder
	 *        the license folder
	 * @throws InvalidLicenseException
	 */
	public void setLicenseFolder(File folder)
	{
		licenseMap.clear();
		_licenseFolder = folder;
		_readAllLicenses();
	}

	/**
	 * Search and install a license corresponding to a product in the license folder
	 *
	 * @param productName
	 *        the product name
	 * @throws InvalidLicenseException
	 *         if no valid license found
	 */
	public void installLicenseInFolder(String productName) throws InvalidLicenseException
	{

		_installLicenseInFolder(_licenseFolder, productName);
	}

	/**
	 * Search and install a license corresponding to a product in the specified folder
	 *
	 * @param folder
	 *        the folder where to search the license
	 * @param productName
	 *        the product name
	 * @throws InvalidLicenseException
	 *         if no valid license found
	 */
	private void _installLicenseInFolder(File folder, String productName) throws InvalidLicenseException
	{

		if (folder == null)
		{
			throw new IllegalArgumentException("folder is null");
		}

		if (folder != null && folder.exists() && !folder.isDirectory())
		{
			throw new IllegalArgumentException(folder + " is not a directory");
		}

		// Find the license file
		File licenseFile = _findLicenseInFolder(folder, productName);

		if (licenseFile == null)
		{
			// No license found
			throw new InvalidLicenseException("No license found for the product " + productName);
		}

		// Install the license for the product
		_installLicense(productName, licenseFile);
	}

	/**
	 * Find a license file corresponding to a product in the specified folder
	 *
	 * @param folder
	 *        the folder where to search the license
	 * @param productName
	 *        the product name
	 * @return the license file, <code>null</code> if none
	 */
	private File _findLicenseInFolder(File folder, String productName)
	{

		File licFile = null;
		if (folder != null && folder.isDirectory() && folder.exists())
		{
			// Get the default license file name
			String lfName = LicenseDef._createDefaultLicenseFileName(productName);

			if (lfName != null)
			{
				// Parse folder and search the license file corresponding to the product
				File[] files = folder.listFiles();
				for (int i = 0; i < files.length && licFile == null; i++)
				{
					File file = files[i];
					if (lfName.equals(file.getName()))
					{
						licFile = file;
					}
				}
			}
		}
		return licFile;
	}

	/**
	 * Find all license files in the specified folder
	 *
	 * @param folder
	 *        the folder where to search the license
	 * @return the license files, newer null
	 */
	private File[] _findAllLicensesInFolder(File folder)
	{

		List<File> licFiles = new ArrayList<File>();
		if (folder != null && folder.isDirectory() && folder.exists())
		{
			// Parse folder and search the license file corresponding to the product
			File[] files = folder.listFiles();
			for (int i = 0; i < files.length; i++)
			{
				File file = files[i];
				if (file.getName().endsWith(ILicense.LICENSE_FILE_EXT))
				{
					licFiles.add(file);
				}
			}
		}
		return licFiles.toArray(new File[licFiles.size()]);
	}

	/**
	 * Check if a license corresponding to a product exists in the specified folder. Please note that the productName
	 * might be null and in this case the method will return true if there is at lease on license file inside
	 *
	 * @param folder
	 *        the folder where to search the license
	 * @param productName
	 *        the product name, might be null
	 * @return true if a license exist, otherwise false
	 */
	public boolean checkIfLicenseExistInFolder(File folder, String productName)
	{
		if (productName != null)
		{
			return _findLicenseInFolder(folder, productName) != null;
		}
		else
		{
			return _findAllLicensesInFolder(folder).length > 0;
		}
	}

	/**
	 * Install license for a product in the license folder
	 *
	 * @param productName
	 *        the product name, might be null and in this case the informations from licFile should be used
	 * @param licFile
	 *        the file of the license to install
	 * @throws InvalidLicenseException
	 *         if the license is invalid for the product
	 */
	public void installLicense(String productName, File licFile) throws InvalidLicenseException
	{
		findLicenseFolder();
		_installLicense(_licenseFolder, productName, licFile);
	}

	public File findLicenseFolder()
	{
		String cessarLicFolderStr = System.getProperty(CessarOldLicenseHandler.CESSAR_LICENSE_FOLDER);
		if (cessarLicFolderStr != null)
		{
			_licenseFolder = new File(cessarLicFolderStr);
			if (!_licenseFolder.exists())
			{
				cessarLicFolderStr = System.getenv("ALLUSERSPROFILE")
					+ "\\Application Data\\Conti Engineering\\CESSAR-CT\\";
				_licenseFolder = new File(cessarLicFolderStr);
			}
		}
		else
		{
			cessarLicFolderStr = System.getenv("ALLUSERSPROFILE")
				+ "\\Application Data\\Conti Engineering\\CESSAR-CT\\";
			_licenseFolder = new File(cessarLicFolderStr);
		}
		_licenseFolder = new File(cessarLicFolderStr);
		return _licenseFolder;
	}

	/**
	 * Install license for a product in the specified folder
	 *
	 * @param folder
	 *        the folder where to install the license
	 * @param productName
	 *        the product name, might be null and in this case the informations from licFile should be used
	 * @param licFile
	 *        the file of the license to install
	 * @throws InvalidLicenseException
	 *         if the license is invalid for the product
	 */
	private void _installLicense(File folder, String productName, File licFile) throws InvalidLicenseException
	{

		if (folder != null && folder.exists() && !folder.isDirectory())
		{
			throw new IllegalArgumentException(folder + " is not a directory");
		}

		if (licFile == null)
		{
			throw new IllegalArgumentException("License file to install is null");
		}

		// Get current license for this productName

		LicenseDef currentLicenseDef = null;
		if (productName != null)
		{
			currentLicenseDef = _getLicenseDef(productName);
		}

		// Install the license for the product
		LicenseDef newLicense = _installLicense(productName, licFile);

		try
		{
			// Get the default license file name
			String lfName = LicenseDef._createDefaultLicenseFileName(newLicense.getProduct().getName());

			// Create folder if needed
			if (folder != null)
			{
				folder.mkdirs();
			}

			// Copy
			copyLicenseFile(licFile, new File(folder, lfName));
		}
		catch (IOException e)
		{
			// Keeping the current license
			if (currentLicenseDef != null)
			{
				licenseMap.put(productName, currentLicenseDef);
			}
			throw new InvalidLicenseException("Error during the copy of the license file into " + folder.getPath()
				+ " directory");
		}
	}

	/**
	 * Copy <code>srcFile</code> to <code>destFile</code>
	 *
	 * @param srcFile
	 *        the source file
	 * @param destFile
	 *        the destination file
	 * @throws IOException
	 *         if error during the copy
	 */
	public void copyLicenseFile(File srcFile, File destFile) throws IOException
	{

		FileInputStream fis = new FileInputStream(srcFile);
		if (!destFile.canWrite())
		{
			destFile.setWritable(true);
		}
		FileOutputStream fos = new FileOutputStream(destFile);

		try
		{
			FileChannel channelSrc = fis.getChannel();
			FileChannel channelDest = fos.getChannel();
			channelSrc.transferTo(0, channelSrc.size(), channelDest);
		}
		finally
		{
			try
			{
				fis.close();
			}
			catch (Exception e)
			{}
			try
			{
				fos.close();
			}
			catch (Exception e)
			{}
		}
	}

	/**
	 * Install license for a product
	 *
	 * @param productName
	 *        the product name, if null the informations from licFile should be used.
	 * @param licFile
	 *        the file of the license to install
	 * @return the installed license
	 * @throws InvalidLicenseException
	 *         if the license is invalid for the product
	 */
	private LicenseDef _installLicense(String productName, File licFile) throws InvalidLicenseException
	{

		// Read and check license
		LicenseDef license = _readLicenseDef(productName, licFile, false);

		if (productName == null)
		{
			productName = license.getProduct().getName();
		}
		// License is valid
		licenseMap.put(productName, license);
		return license;
	}

	/**
	 * Read and install all license available under the current licenseFolder
	 *
	 * @throws InvalidLicenseException
	 */
	private void _readAllLicenses()
	{
		File[] licFiles = _findAllLicensesInFolder(_licenseFolder);
		for (File licFile: licFiles)
		{
			String productName = licFile.getName();
			// remove the extension from productName
			productName = productName.substring(0, productName.length() - ILicense.LICENSE_FILE_EXT.length());
			try
			{
				_installLicense(productName, licFile);
			}
			catch (InvalidLicenseException e)
			{
				// just report the error and skip this entry
				InvalidLicenseException licexc = new InvalidLicenseException("Old license model not installed");
				licexc.initCause(e);
				// CessarPluginActivator.logWarning(licexc);
			}
		}
	}

	/**
	 * Read a license model for a given product
	 *
	 * @param productName
	 *        the product name
	 * @param file
	 *        the license model file to read
	 * @return the license model
	 * @throws InvalidLicenseException
	 *         if error during the reading
	 */
	public LicenseDef readLicenseModel(String productName, File file) throws InvalidLicenseException
	{

		// Read and check license model
		return _readLicenseDef(productName, file, true);
	}

	/**
	 * Read a license model for a given product
	 *
	 * @param productName
	 *        the product name
	 * @param is
	 *        the license model input stream to read
	 * @return the license model
	 * @throws InvalidLicenseException
	 *         if error during the reading
	 */
	public LicenseDef readLicenseModel(String productName, InputStream is) throws InvalidLicenseException
	{

		// Read and check license model
		return _readLicenseDef(productName, is, true);
	}

	/**
	 * Read a license for a given product
	 *
	 * @param productName
	 *        the product name
	 * @param file
	 *        the file to read
	 * @param modelFile
	 *        true if the file is a license model file, otherwise false
	 * @return the license
	 * @throws InvalidLicenseException
	 *         if error during the reading
	 */
	private LicenseDef _readLicenseDef(String productName, File file, boolean modelFile) throws InvalidLicenseException
	{

		LicenseDef license = null;

		try
		{
			// Read the license
			license = _readLicenseDef(productName, new FileInputStream(file), modelFile);
		}
		catch (IOException e)
		{
			throw new InvalidLicenseException(file + " doest not exist", e);
		}

		return license;
	}

	/**
	 * Read a license for a given product. If <code>modelFile</code> is <code>true</code>, the method returns a license
	 * model.
	 *
	 * @param productName
	 *        the product name, can be null
	 * @param is
	 *        the input stream to read
	 * @param modelFile
	 *        true if the file is a license model file, otherwise false
	 * @return the license
	 * @throws InvalidLicenseException
	 *         if error during the reading
	 */
	private LicenseDef _readLicenseDef(String productName, InputStream is, boolean modelFile)
		throws InvalidLicenseException
	{

		LicenseDef license = null;

		try
		{
			// Read license for the product
			license = new LicenseDef(is, !modelFile);

			// Check the license
			_checkLicense(productName, license);
		}
		catch (InvalidLicenseException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new InvalidLicenseException("The license is invalid for the product " + productName + " [Cause : + "
				+ e.getMessage() + "]", e);
		}

		return license;
	}

	/**
	 * Check the license
	 *
	 * @param license
	 *        the license
	 * @throws InvalidLicenseException
	 *         if the license is invalid
	 */
	private void _checkLicense(String productName, LicenseDef license) throws InvalidLicenseException
	{

		// Product name control
		if (productName != null && !productName.equals(license.getProduct().getName()))
		{
			throw new InvalidLicenseException("The license is invalid for the product " + productName);
		}

		// HostId Control.
		// Retrieve local host id based on the license file host id type :
		// New : BIOS ID
		// Old : Mac Address
		// Linux : always Mac Address
		int locHostID = license.isNewLicenseType() ? HostID.obtainHostID() : HostID.obtainOldHostID();
		int licHostID = license.getHostID();
		boolean hostIDBinded = license.getLicenseInfo().isHostIDBinded();
		// Check Host ID if needed
		if (licHostID != 0 && (licHostID != locHostID && hostIDBinded))
		{
			throw new InvalidLicenseException("The license for the product '" + productName
				+ "' is invalid for the current HostId");
		}
	}

	/**
	 * Uninstall the license corresponding to the product name.
	 *
	 * @param productName
	 *        the product name
	 */
	public void uninstallLicense(String productName)
	{

		licenseMap.remove(productName);
	}

	/**
	 * Return the ILicense for the specified product
	 *
	 * @param productName
	 *        the product name
	 * @return the ILicense if installed, otherwise null
	 */
	public ILicense getLicense(String productName)
	{

		return _getLicenseDef(productName);
	}

	/**
	 * Return the available licenses
	 *
	 * @return an array of available licenses, never null
	 */
	public ILicense[] getLicenses()
	{
		return licenseMap.values().toArray(new LicenseDef[0]);
	}

	/**
	 * Return the LicenseDef for the specified product
	 *
	 * @param productName
	 *        the product name
	 * @return the LicenseDef if installed, otherwise null
	 */
	private LicenseDef _getLicenseDef(String productName)
	{

		return licenseMap.get(productName);
	}

	/**
	 * Return the product corresponding to the product name.
	 *
	 * @param productName
	 *        the product name
	 * @return the product corresponding to the product name or null if no license installed for the product name
	 */
	public IProduct getLicenseProduct(String productName)
	{

		LicenseDef license = _getLicenseDef(productName);
		IProduct product = null;
		if (license != null)
		{
			product = license.getProduct();
		}
		return product;
	}

	/**
	 * Return the value of a module parameter corresponding to the product name.
	 *
	 * @param productName
	 *        the product name
	 * @param moduleName
	 *        the module name
	 * @param parameter
	 *        the parameter name
	 * @return the value of the parameter otherwise null
	 */
	public String getModuleParameterValue(String productName, String moduleName, String parameter)
	{

		LicenseDef licenseDef = _getLicenseDef(productName);
		return _getModuleParameterValue(licenseDef, moduleName, parameter);
	}

	/**
	 * Return the value of a module parameter corresponding to the license.
	 *
	 * @param licenseDef
	 *        the license
	 * @param moduleName
	 *        the module name
	 * @param parameter
	 *        the parameter name
	 * @return the value of the parameter otherwise null
	 */
	private String _getModuleParameterValue(LicenseDef licenseDef, String moduleName, String parameter)
	{

		String value = null;

		// Check module validity
		_checkModuleValidity(licenseDef, moduleName);

		if (licenseDef != null)
		{
			value = licenseDef.getParamValue(moduleName, parameter);
		}

		return value;
	}

	/**
	 * Get the expiration date for a given module of a given product name.
	 *
	 * @param productName
	 *        the product name
	 * @param moduleName
	 *        the module name
	 * @return the expiration date, otherwise null
	 */
	public ExpDate getModuleExpDate(String productName, String moduleName)
	{

		LicenseDef licenseDef = _getLicenseDef(productName);
		return _getModuleExpDate(licenseDef, moduleName);
	}

	/**
	 * Get the expiration date for a given module of a given license.
	 *
	 * @param licenseDef
	 *        the license
	 * @param moduleName
	 *        the module name
	 * @return the expiration date, otherwise null
	 */
	private ExpDate _getModuleExpDate(LicenseDef licenseDef, String moduleName)
	{

		ExpDate expDate = null;

		// Check module validity
		_checkModuleValidity(licenseDef, moduleName);

		if (licenseDef != null)
		{
			expDate = licenseDef.getExpDate(moduleName);
		}

		return expDate;
	}

	/**
	 * Check the permission for a given module of a given product name.
	 *
	 * @param productName
	 *        the product name
	 * @param moduleName
	 *        the module name
	 * @return the permission
	 */
	public Permission getModulePermission(String productName, String moduleName)
	{

		LicenseDef licenseDef = _getLicenseDef(productName);
		return _getModulePermission(licenseDef, moduleName);
	}

	/**
	 * Check the permission for a given module of a given license.
	 *
	 * @param licenseDef
	 *        the license
	 * @param moduleName
	 *        the module name
	 * @return the permission
	 */
	private Permission _getModulePermission(LicenseDef licenseDef, String moduleName)
	{

		Permission perm = Permission.NO;

		// Check module validity
		_checkModuleValidity(licenseDef, moduleName);

		if (licenseDef != null)
		{
			// Get the permission
			perm = licenseDef.getMode(moduleName);
		}

		return perm;
	}

	/**
	 * Check if the module is expired for a given license.
	 *
	 * @param licenseDef
	 *        the license
	 * @param moduleName
	 *        the module name
	 * @return true if the module is expired, otherwise false
	 */
	public boolean moduleIsExpired(String productName, String moduleName)
	{

		LicenseDef licenseDef = _getLicenseDef(productName);
		return _moduleIsExpired(licenseDef, moduleName);
	}

	/**
	 * Check if the module is expired for a given license.
	 *
	 * @param productName
	 *        the product name
	 * @param moduleName
	 *        the module name
	 * @return true if the module is expired, otherwise false
	 */
	private boolean _moduleIsExpired(LicenseDef licenseDef, String moduleName)
	{

		boolean res = false;

		// Check module validity
		_checkModuleValidity(licenseDef, moduleName);

		if (licenseDef != null)
		{
			ExpDate expDate = licenseDef.getExpDate(moduleName);
			if (expDate != null)
			{
				res = expDate.expired();
			}
		}

		return res;
	}

	/**
	 * Check if the specified module exists for the given product.
	 *
	 * @param productName
	 *        the product name
	 * @param moduleName
	 *        the module name
	 * @return true if the module is expired, otherwise false
	 */
	public boolean checkModuleExist(String productName, String moduleName)
	{
		boolean res = false;

		LicenseDef licenseDef = _getLicenseDef(productName);
		if (licenseDef != null)
		{
			try
			{
				// Check module validity
				_checkModuleValidity(licenseDef, moduleName);
				res = true;
			}
			catch (Exception e)
			{
				res = false;
			}
		}

		return res;
	}

	/**
	 * Check the validity of the given module for a given license
	 *
	 * @param licenseDef
	 *        the license
	 * @param moduleName
	 *        the module name
	 * @throws IllegalArgumentException
	 *         if the module does not exist for the product
	 */
	private void _checkModuleValidity(LicenseDef licenseDef, String moduleName)
	{

		if (licenseDef != null)
		{
			int moduleIndex = licenseDef.getModuleIndex(moduleName);
			int moduleCount = licenseDef._getModuleCount();
			if (moduleIndex < 0 || moduleIndex >= moduleCount)
			{
				throw new IllegalArgumentException(moduleName + " is not a module of the product "
					+ licenseDef.getProduct().getName());
			}
		}
	}

	/**
	 * Compare two versions
	 *
	 * @param version1
	 *        the first version
	 * @param version2
	 *        the second version
	 * @return a value <code>0</code> if equals, <br>
	 *         a value less than <code>0</code> if the first version is less than the second version<br>
	 *         a value greater than <code>0</code> if the first version is greater than the second version<br>
	 */
	public static int compareVersion(String version1, String version2)
	{

		return compareVersion(version1, version2, -1);
	}

	/**
	 * Compare two versions
	 *
	 * @param version1
	 *        the first version
	 * @param version2
	 *        the second version
	 * @param depthToCheck
	 *        the depth of the version to check. A value less than <code>0</code> to check the whole version
	 * @return a value <code>0</code> if equals, <br>
	 *         a value less than <code>0</code> if the first version is less than the second version<br>
	 *         a value greater than <code>0</code> if the first version is greater than the second version<br>
	 */
	public static int compareVersion(String version1, String version2, int depthToCheck)
	{

		int res = 0;
		if (version1 == version2)
		{
			res = 0;
		}
		else if (version1 == null)
		{
			res = -1;
		}
		else if (version2 == null)
		{
			res = 1;
		}
		else
		{
			String[] versionStr1 = version1.split("\\.");
			String[] versionStr2 = version2.split("\\.");

			if (depthToCheck >= 0)
			{
				if (versionStr1.length > depthToCheck)
				{
					String[] copyVersionStr = new String[depthToCheck];
					System.arraycopy(versionStr1, 0, copyVersionStr, 0, depthToCheck);
					versionStr1 = copyVersionStr;
				}
				if (versionStr2.length > depthToCheck)
				{
					String[] copyVersionStr = new String[depthToCheck];
					System.arraycopy(versionStr2, 0, copyVersionStr, 0, depthToCheck);
					versionStr2 = copyVersionStr;
				}
			}

			for (int i = 0; i < versionStr1.length && res == 0; i++)
			{
				String v1 = versionStr1[i];
				if (i < versionStr2.length)
				{
					String v2 = versionStr2[i];
					res = v1.compareTo(v2);
				}
				else
				{
					res = 1;
				}
			}
			if (res == 0)
			{
				if (versionStr1.length < versionStr2.length)
				{
					res = -1;
				}
			}
		}

		return res;
	}
}
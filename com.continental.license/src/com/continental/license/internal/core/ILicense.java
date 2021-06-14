package com.continental.license.internal.core;

public interface ILicense
{
	/** License file extension */
	public static final String LICENSE_FILE_EXT = ".lf";
	
	/**
	 * Return true if this LicenseDef is a LicenseModel.
	 * 
	 * @return true if this LicenseDef is a LicenseModel.
	 */
	public boolean isLicenseModel();

	/**
	 * Return the product
	 * 
	 * @return the product
	 */
	public IProduct getProduct();

	/**
	 * Return the license information
	 * 
	 * @return the license information
	 */
	public LicenseInfo getLicenseInfo();

	/**
	 * Return the permission for the specified module
	 * 
	 * @param moduleName
	 *        the name of the module
	 * @return the permission
	 */
	public Permission getMode(String moduleName);

	/**
	 * Return the expiration date for the specified module
	 * 
	 * @param moduleName
	 *        the name of the module
	 * @return the expiration date
	 */
	public ExpDate getExpDate(String moduleName);
	
	/**
	 * Return the value of a module parameter
	 * @param moduleName the name of the module
	 * @param paramName the name of the parameter
	 * @return the value
	 */
	public String getParamValue(String moduleName, String paramName);
}
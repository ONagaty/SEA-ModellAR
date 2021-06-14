package com.continental.license.internal.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class LicenseInfo
{
	/** Default separator */
	public static final String LICENSE_INFO_FILE_EXT = ".lif";

	/** Default separator */
	private static final String SEPARATOR = "+|+";

	/** No name */
	private static final String NONAME = "noname";

	// Attributes declaration
	private String _firstName;
	private String _lastName;
	private String _email;
	private String _department;
	private String _company;
	private String _address;
	private String _city;
	private String _zip;
	private String _country;
	private String _phone;
	private String _comment;
	private String _dateOfDemand;
	private String _productName;
	private String _fullID;
	private boolean _hostIDBinded;
	private String _dateOfGeneration;
	private boolean includeProduct = false;

	/**
	 * Converts LicenseInfo to String.
	 *
	 * @param licenseInfo
	 *        the license info.
	 * @return the String.
	 */
	public static String licenseInfoToString(LicenseInfo licenseInfo)
	{
		return _licenseInfoToString(licenseInfo, SEPARATOR);
	}

	/**
	 * Converts LicenseInfo to String.
	 *
	 * @param licenseInfo
	 *        the license info.
	 * @param separator
	 *        the separator.
	 * @return the String.
	 */
	private static String _licenseInfoToString(LicenseInfo licenseInfo, String separator)
	{
		StringBuffer buffer = new StringBuffer();
		if (licenseInfo != null)
		{
			if (separator == null)
			{
				separator = SEPARATOR;
			}
			buffer.append(licenseInfo.getFirstName()).append(separator);
			buffer.append(licenseInfo.getLastName()).append(separator);
			buffer.append(licenseInfo.getEmail()).append(separator);
			buffer.append(licenseInfo.getDepartment()).append(separator);
			buffer.append(licenseInfo.getCompany()).append(separator);
			buffer.append(licenseInfo.getAddress()).append(separator);
			buffer.append(licenseInfo.getCity()).append(separator);
			buffer.append(licenseInfo.getZip()).append(separator);
			buffer.append(licenseInfo.getCountry()).append(separator);
			buffer.append(licenseInfo.getPhone()).append(separator);
			buffer.append(licenseInfo.getComment()).append(separator);
			buffer.append(licenseInfo.getDateOfDemand()).append(separator);
			if(licenseInfo.isIncludeProduct())
			{
				buffer.append(licenseInfo.getProductName()).append(separator);
			}
			buffer.append(licenseInfo.getFullID()).append(separator);
			buffer.append(licenseInfo.isHostIDBinded()).append(separator);
			buffer.append(licenseInfo.getDateOfGeneration()).append(separator);
		}
		return buffer.toString();
	}

	/**
	 * Converts String to LicenseInfo.
	 *
	 * @param str
	 *        the String.
	 * @return the LicenseInfo.
	 */
	public static LicenseInfo stringToLicenseInfo(String str)
	{
		return _stringToLicenseInfo(str, SEPARATOR);
	}

	/**
	 * Converts String to LicenseInfo.
	 *
	 * @param str
	 *        the String.
	 * @return the LicenseInfo.
	 */
	private static LicenseInfo _stringToLicenseInfo(String str, String separator)
	{
		if (separator == null)
		{
			separator = SEPARATOR;
		}
		LicenseInfo licenseInfo = null;
		List<String> paramList = new ArrayList<String>();
		int idx;
		while ((idx = str.indexOf(separator)) != -1)
		{
			String segment = str.substring(0, idx);
			paramList.add(segment);
			str = str.substring(idx + separator.length());
		}
		switch (paramList.size())
		{
			case 15:
				// old license file without product was received
				licenseInfo = new LicenseInfo(paramList.get(0), paramList.get(1), paramList.get(2),
					paramList.get(3), paramList.get(4), paramList.get(5), paramList.get(6),
					paramList.get(7), paramList.get(8), paramList.get(9), paramList.get(10),
					paramList.get(11), null, paramList.get(12), Boolean.valueOf(paramList.get(13)),
					paramList.get(14));
				break;
			case 16:
				// new license file with product included was received
				licenseInfo = new LicenseInfo(paramList.get(0), paramList.get(1), paramList.get(2),
					paramList.get(3), paramList.get(4), paramList.get(5), paramList.get(6),
					paramList.get(7), paramList.get(8), paramList.get(9), paramList.get(10),
					paramList.get(11), paramList.get(12), paramList.get(13),
					Boolean.valueOf(paramList.get(14)), paramList.get(15));
				break;
			default:
				// invalid, just return an empty license
				licenseInfo = new LicenseInfo();
				break;
		}
		return licenseInfo;
	}

	/**
	 * Writes a <code>LicenseInfo</code> to file.
	 *
	 * @param file
	 * @param licenseInfo
	 * @throws IOException
	 */
	public static File writeLicenseInfo(File folder, LicenseInfo licenseInfo) throws IOException
	{
		if (licenseInfo == null)
		{
			throw new IOException("licenseInfo is null");
		}

		// Creates folder if needed
		if (folder != null)
		{
			if (folder.exists())
			{
				if (!folder.isDirectory())
				{
					throw new IOException(folder.getPath() + " is not a directory");
				}
			}
			else
			{
				folder.mkdirs();
			}
		}

		// Converts LicenseInfo to a string
		String licenseInfoStr = _licenseInfoToString(licenseInfo, "\n");

		// Create the file name
		StringBuffer buffer = new StringBuffer();
		buffer.append(licenseInfo.getLastName());
		String firstName = licenseInfo.getFirstName();
		if (buffer.length() > 0 && firstName.length() > 0)
		{
			buffer.append("_");
		}
		buffer.append(firstName);
		if (buffer.length() == 0)
		{
			buffer.append(NONAME);
		}
		buffer.append(LICENSE_INFO_FILE_EXT);

		// Create the file
		File file = new File(folder, _replaceChar(buffer.toString()));

		// Writes licenseInfoStr
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		try
		{
			out.println(licenseInfoStr);
		}
		finally
		{
			try
			{
				out.close();
			}
			catch (Exception e)
			{}
		}
		return file;
	}

	/**
	 * Replace the characters with accent and remove the whitespace
	 *
	 * @param str
	 *        the string to change
	 * @return the new string
	 */
	private static String _replaceChar(String str)
	{
		String newStr = str;
		if (newStr != null)
		{
			newStr = newStr.toUpperCase();
			newStr = newStr.replaceAll(" ", "");
			newStr = newStr.replaceAll("Ý", "Y");
			newStr = newStr.replaceAll("Ù|Ú|Û|Ü", "U");
			newStr = newStr.replaceAll("Ò|Ó|Ô|Õ|Ö", "O");
			newStr = newStr.replaceAll("Ì|Í|Î|Ï", "I");
			newStr = newStr.replaceAll("È|É|Ê|Ë", "E");
			newStr = newStr.replaceAll("Ç", "C");
			newStr = newStr.replaceAll("À|Á|Â|Ã|Ä|Å|Æ", "A");
			newStr = newStr.toLowerCase();
		}
		return newStr;
	}

	/**
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static LicenseInfo readLicenseInfo(File file) throws IOException
	{
		if (file == null)
		{
			throw new IOException("file is null");
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		StringBuffer strBuf = new StringBuffer();

		// Read first line
		String line = in.readLine();
		// Read others lines
		while (line != null)
		{
			if (strBuf.length() > 0)
			{
				strBuf.append(SEPARATOR);
			}
			strBuf.append(line);
			// Next line
			line = in.readLine();
		}
		// Converts string to a LicenseInfo
		return stringToLicenseInfo(strBuf.toString());
	}

	/**
	 * Default constructor
	 */
	LicenseInfo()
	{
	}

	/**
	 * Constructor
	 *
	 * @param firstname
	 *        the first name
	 * @param lastname
	 *        the last name
	 * @param email
	 *        the email
	 * @param department
	 *        the department
	 * @param company
	 *        the company
	 * @param address
	 *        the address
	 * @param city
	 *        the city
	 * @param zip
	 *        the zip
	 * @param country
	 *        the country
	 * @param phone
	 *        the phone
	 * @param comment
	 *        the comment
	 * @param dateOfDemand
	 *        the date of demand
	 * @param productName
	 *        the product name
	 * @param fullID
	 *        the full ID
	 * @param hostIDBinded
	 *        true if the host ID is binded with the license, otherwise false
	 * @param dateOfGeneration
	 *        the date of the license generation
	 */
	public LicenseInfo(String firstname, String lastname, String email, String department,
		String company, String address, String city, String zip, String country, String phone,
		String comment, String dateOfDemand, String productName, String fullID,
		boolean hostIDBinded, String dateOfGeneration)
	{
		_firstName = (firstname == null ? "" : firstname);
		_lastName = (lastname == null ? "" : lastname);
		_email = (email == null ? "" : email);
		_department = (department == null ? "" : department);
		_company = (company == null ? "" : company);
		_address = (address == null ? "" : address);
		_city = (city == null ? "" : city);
		_zip = (zip == null ? "" : zip);
		_country = (country == null ? "" : country);
		_phone = (phone == null ? "" : phone);
		_comment = (comment == null ? "" : comment);
		_dateOfDemand = (dateOfDemand == null ? "" : dateOfDemand);
		_productName = (productName == null ? "" : productName);
		_fullID = (fullID == null ? "" : fullID);
		_hostIDBinded = hostIDBinded;
		_dateOfGeneration = (dateOfGeneration == null ? "" : dateOfGeneration);
	}

	/**
	 * Gets the first name
	 *
	 * @return the first name
	 */
	public String getFirstName()
	{
		return _firstName;
	}

	/**
	 * Gets the last name
	 *
	 * @return the last name
	 */
	public String getLastName()
	{
		return _lastName;
	}

	/**
	 * Gets the email
	 *
	 * @return the email
	 */
	public String getEmail()
	{
		return _email;
	}

	/**
	 * Gets the department
	 *
	 * @return the department
	 */
	public String getDepartment()
	{
		return _department;
	}

	/**
	 * Gets the company
	 *
	 * @return the company
	 */
	public String getCompany()
	{
		return _company;
	}

	/**
	 * Gets the address
	 *
	 * @return the address
	 */
	public String getAddress()
	{
		return _address;
	}

	/**
	 * Gets the city
	 *
	 * @return the city
	 */
	public String getCity()
	{
		return _city;
	}

	/**
	 * Gets the zip
	 *
	 * @return the zip
	 */
	public String getZip()
	{
		return _zip;
	}

	/**
	 * Gets the country
	 *
	 * @return the country
	 */
	public String getCountry()
	{
		return _country;
	}

	/**
	 * Gets the phone
	 *
	 * @return the phone
	 */
	public String getPhone()
	{
		return _phone;
	}

	/**
	 * Returns the comment
	 *
	 * @return the comment
	 */
	public String getComment()
	{
		return _comment;
	}

	/**
	 * Gets the date of the demand
	 *
	 * @return the date of the demand
	 */
	public String getDateOfDemand()
	{
		return _dateOfDemand;
	}

	/**
	 * Gets the name of the licensed product
	 *
	 * @return the product name
	 */
	public String getProductName()
	{
		return _productName;
	}

	public void setIncludeProduct(boolean addProduct)
	{
		includeProduct = addProduct;
	}

	public boolean isIncludeProduct()
	{
		return includeProduct;
	}

	/**
	 * Gets the full id (mac address, os.arch, os.name, os.version)
	 *
	 * @return the full id
	 */
	public String getFullID()
	{
		return _fullID;
	}

	/**
	 * Returns true if the host ID is binded with the license, otherwise false.
	 *
	 * @return true if the host ID is binded with the license, otherwise false.
	 */
	public boolean isHostIDBinded()
	{
		return _hostIDBinded;
	}

	/**
	 * Gets the date of the license generation
	 *
	 * @return the date of the license generation
	 */
	public String getDateOfGeneration()
	{
		return _dateOfGeneration;
	}
	
	@Override
	public String toString()
	{
		StringBuffer sB = new StringBuffer();
		if(_firstName!=null && !_firstName.equals(""))
			sB.append(_firstName);
		if(_lastName!=null && !_lastName.equals(""))
			sB.append(", " + _lastName);
		if(_company!=null && !_company.equals(""))
			sB.append(", " + _company);
		if(_department!=null && !_department.equals(""))
			sB.append(", " + _department);
		if(_email!=null && !_email.equals(""))
			sB.append(", " + _email);
		if(_phone!=null && !_phone.equals(""))
			sB.append(", " + _phone);
		if(sB.toString().equals(""))
			return "Temporary";
		return sB.toString();
	}
}

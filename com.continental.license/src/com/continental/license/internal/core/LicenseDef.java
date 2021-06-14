package com.continental.license.internal.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.zip.CRC32;

/**
 * the license definition class support. This class decodes license strings, and initialize
 * permissions values
 */
public class LicenseDef implements ILicense
{

	/** Separator for the parameters of the module */
	protected static final String MODULE_PARAMETER_SEPARATOR = "||";
	
	/** Product reference */
	private Product _product = null;

	/** Module parameters */
	protected LicenseParametersValues _licenseParamsValues = new LicenseParametersValues();

	/** License information */
	protected LicenseInfo _licenseInfo = new LicenseInfo();

	/** Arrays to perform license encoding/decoding */
	protected String[] _permArray = new String[32];
	protected int _permArrayChecksum;
	protected int _rollPermArray;
	protected int[] _hexArray = new int[32];

	class ModuleLicense
	{
		public int mode = Permission.NO.ordinal(); // one of ML_* values
		public int exp_date_index = -1; // 0..2 or -1 if forever
	}

	private ExpDate[] ed = {new ExpDate(), new ExpDate(), new ExpDate()};
	private ModuleLicense[] ml = null;

	private InternalLicenseDef[] _ild = null;
	private int _lastModuleGroupInit = -1;

	protected int version = 0; // 0..255
	private int aux_A = 0; // 0..255
	private int aux_B = 0; // 0..255
	private int aux_C = 0; // 0..7
	private int aux_D = 0; // 0..1

	private boolean _isLicenseModel = false;
	
	protected LicenseDef(IProduct product)
	{

		if (product instanceof Product) {
			_product = (Product)product;
			if (_product != null && _product.getName() == null)
			{
				_product = null;
			}
		}
		_isLicenseModel = true;
	}
	
	protected LicenseDef(String productName)
	{

		this(new Product(productName));
	}

	protected LicenseDef(InputStream is) throws Exception
	{

		this(is, false);
	}

	LicenseDef(InputStream is, boolean decodeKey) throws Exception
	{

		_readLicense(is, decodeKey);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		boolean res = false;
		if (obj instanceof LicenseDef)
		{
			LicenseDef lm = (LicenseDef) obj;
			// Compare _productName
			res = (_product == null && lm._product == null)
				|| (_product != null && _product.equals(lm._product));
		}
		return res;
	}

	/**
	 * Return true if this LicenseDef is a LicenseModel.
	 * @return true if this LicenseDef is a LicenseModel.
	 */
	public final boolean isLicenseModel()
	{
		return _isLicenseModel;
	}

	/**
	 * Return the product
	 * @return the product
	 */
	public final IProduct getProduct()
	{

		return _product;
	}

	/**
	 * Return the product name
	 * @return the product name
	 */
	protected final String _getProductName()
	{

		String name = null;
		if (_product != null) {
			name = _product.getName();
		}
		return name;
	}
	
	/**
	 * Return the number of module
	 * @return the number of module
	 */
	protected final int _getModuleCount() {
		
		int count = 0;
		if (_product != null) {
			count = _product.getModulesList().size();
		}
		return count;
	}

	/**
	 * Add a new module
	 * @param moduleName the name of the module to add
	 */
	protected void addModule(String moduleName)
	{
		
		if (_product != null)
		{
			_product.addModule(new Module(moduleName));
		}
	}

	/**
	 * Remove a new module
	 * @param moduleName the name of the module to remove
	 */
	protected void removeModule(String moduleName)
	{
		
		if (_product != null)
		{
			_product.removeModule(new Module(moduleName));
		}
	}

	/**
	 * Set the parameters for a module.
	 * @param moduleName the name of the module
	 * @param moduleParamsList the list of the new parameters
	 */
	protected void setModuleParameters(String moduleName, Collection<String> moduleParamsList)
	{

		// Get module
		Module module = null;

		if (_product != null)
		{
			module = (Module) _product.getModule(moduleName);
		}
		if (module != null)
		{
			// Clear old parameters
			module.removeAllParameters();
			if (moduleParamsList != null)
			{
				// Add new parameters
				for (Iterator<String> it = moduleParamsList.iterator(); it.hasNext();)
				{
					module.addParameter(it.next());
				}
			}
		}
	}

	/**
	 * Return the value of a module parameter
	 * @param moduleName the name of the module
	 * @param paramName the name of the parameter
	 * @return the value
	 */
	public final String getParamValue(String moduleName, String paramName)
	{

		return _licenseParamsValues.get(getModuleIndex(moduleName) + paramName);
	}

	/**
	 * Set the value of a module parameter
	 * @param moduleName the name of the module
	 * @param paramName the name of the parameter
	 * @param value the value of the parameter
	 */
	public final void setParamValue(String moduleName, String paramName, String value)
	{

		_licenseParamsValues.put(getModuleIndex(moduleName) + paramName, value);
	}

	/**
	 * Return the license information
	 * @return the license information
	 */
	public final LicenseInfo getLicenseInfo()
	{
		return _licenseInfo;
	}

	/**
	 * Set the license information
	 * @param licenseInfo the new license information
	 */
	public final void setLicenseInfo(LicenseInfo licenseInfo)
	{
		_licenseInfo = licenseInfo;
		aux_D = 0;
		if (_licenseInfo != null && _licenseInfo.isHostIDBinded()) {
			aux_D = 1;
		}
	}

	private void _checkAndInitArrays()
	{
		if (ml == null || ml.length != _getModuleCount())
		{
			_initArrays();
		}
	}
	
	private void _initArrays()
	{

		ml = new ModuleLicense[_getModuleCount()];
		for (int i = 0; i < ml.length; i++) {
			ml[i] = new ModuleLicense();
		}
		setMaxUsers(0);
	}

	public final Permission getMode(int index)
	{

		_checkAndInitArrays();
		_initModuleGroupData(index);
		return Permission.values()[ml[index].mode];
	}

	public final Permission getMode(String moduleName)
	{

		return getMode(getModuleIndex(moduleName));
	}

	public final void setMode(int index, Permission mode)
	{

		_checkAndInitArrays();
		_initModuleGroupData(index);

		ml[index].mode = mode.ordinal();
	}

	public final Permission[] getMode()
	{

		Permission[] mode = new Permission[ml.length];
		for (int i = 0; i < ml.length; i++)
		{
			mode[i] = Permission.values()[ml[i].mode];
		}
		return mode;
	}

	// Must be a valid module Name
	int getModuleIndex(String moduleName)
	{

		int result = -1;
		int i = 0;
		
		if (_product != null && moduleName != null) {
			Iterator<IModule> it = _product.getModulesList().iterator();
			for (; it.hasNext() && result < 0;)
			{
				IModule module = it.next();
				if (moduleName.equals(module.getName())) {
					result = i;
				}
				i++;
			}
		}

		return result;
	}

	public final int getExpDateIndex(int index)
	{

		_checkAndInitArrays();
		_initModuleGroupData(index);
		return ml[index].exp_date_index;
	}

	public final void setExpDateIndex(int index, int expDateIndex) throws NumberFormatException
	{

		_checkAndInitArrays();
		_initModuleGroupData(index);

		if ((expDateIndex < -1) || (expDateIndex > 2))
		{
			throw new NumberFormatException("illegal exp date index");
		}
		ml[index].exp_date_index = expDateIndex;
	}

	public final int[] getExpDateIndex()
	{

		int[] exp_date_index = new int[ml.length];
		for (int i = 0; i < ml.length; i++)
		{
			exp_date_index[i] = ml[i].exp_date_index;
		}
		return exp_date_index;
	}

	public final void setExpDateIndex(int expDateIndex[]) throws NumberFormatException
	{

		for (int i = 0; i < expDateIndex.length; i++)
		{
			if (i < ml.length)
			{
				if ((expDateIndex[i] < -1) || (expDateIndex[i] > 2))
				{
					throw new NumberFormatException("illegal exp date index");
				}
				ml[i].exp_date_index = expDateIndex[i];
			}
			else
			{
				ml[i].exp_date_index = -1;
			}
		}
	}

	public final ExpDate getExpDate(int index)
	{

		ExpDate expDate = null;
		if (index >= 0 && index < ed.length)
		{
			expDate = new ExpDate(ed[index].getCalendar());
		}
		return expDate;
	}

	public final ExpDate getExpDate(String moduleName)
	{

		int index = getExpDateIndex(getModuleIndex(moduleName));
		return getExpDate(index);
	}

	public final void setExpDate(int index, ExpDate expDate)
	{

		if (index >= 0 && index < ed.length)
		{
			ed[index] = new ExpDate(expDate.getCalendar());
		}
	}

	public final ExpDate[] getExpDate()
	{

		ExpDate[] reted = new ExpDate[ed.length];
		for (int i = 0; i < ed.length; i++)
		{
			reted[i] = new ExpDate(ed[i].getCalendar());
		}
		return reted;
	}

	public final void setExpDate(ExpDate expDate[])
	{

		for (int i = 0; i < ed.length; i++)
		{
			if (i < expDate.length)
			{
				ed[i] = new ExpDate(expDate[i].getCalendar());
			}
			else
			{
				ed[i] = new ExpDate();
			}
		}
	}

	public final int getMaxUsers() throws NumberFormatException
	// obtain maxUsers (0..101) from aux_A value
	{

		int maxUsers = (aux_A & 127) - 13;

		if ((maxUsers < 0) || (maxUsers > 101))
			throw new NumberFormatException("illegal maxUsers");

		return maxUsers;
	}

	public final void setMaxUsers(int maxUsers) throws NumberFormatException
	// stuff maxUsers (0..101) into aux_A value
	{

		if ((maxUsers < 0) || (maxUsers > 101))
			throw new NumberFormatException("illegal maxUsers");

		aux_A = maxUsers + 13; // store 13..114 to lower 7 bits

		// stuff random bit into aux_A's MSB

		int r = (int) (System.currentTimeMillis() / 1000);

		aux_A += ((r & 1) << 7) & 128;
	}

	/**
	 * Get the host ID (value between [0..1023]).
	 * 
	 * @return the host ID
	 * @throws NumberFormatException
	 *         if the host ID is invalid
	 */
	public final int getHostID() throws NumberFormatException
	{

		// obtain aHostID (0..1023) from aux_B/C values
		int aHostID = aux_B + ((aux_C & 3) << 8);
		if ((aHostID < 0) || (aHostID > 1023))
		{
			throw new NumberFormatException("illegal HostID");
		}

		return aHostID;
	}

	/**
	 * Set the host ID (value between [0..1023]).
	 * 
	 * @param aHostID
	 *        the host ID
	 * @throws NumberFormatException
	 *         if the host ID is invalid
	 */
	public final void setHostID(int aHostID) throws NumberFormatException
	{

		// stuff aHostID (0..1023) into aux_B/C values
		if ((aHostID < 0) || (aHostID > 1023))
		{
			throw new NumberFormatException("illegal HostID");
		}

		aux_B = aHostID & 255; // store lower 8 bits of aHostID
		aux_C = (aHostID >>> 8) & 3; // store upper 2 bits of aHostID

		// stuff random bits into aux_C's MSB
		int r = (int) (System.currentTimeMillis() / 1000);
		aux_C += ((r & 1) << 2) & 4;
	}

	/**
	 * Decode the encoded host ID and set the host ID.
	 * 
	 * @param encodedHostID
	 *        the encoded host ID
	 * @throws Exception
	 *         if error during the decoding
	 */
	public final void setEncodedHostId(String encodedHostID) throws Exception
	{

		try
		{
			int startIndex = 0;
			int endIndex = 6;
			// License based on the biosID are prefixed
			if (encodedHostID.startsWith(HostID.NEW_LICENSE_PREFIX))
			{
				startIndex=1;
				endIndex = 7;
			}
			setHostID(HostID.decodeIntValue(encodedHostID.substring(startIndex, endIndex)));
		}
		catch (Exception e)
		{
			throw new Exception("The Host ID '" + encodedHostID + "' is invalid");
		}
	}
	
	/**
	 * New license (Windows platform only) are prefixed.
	 * 
	 * @return boolean
	 */
	public boolean isNewLicenseType()
	{
		String fullID = getLicenseInfo().getFullID();
		return fullID.startsWith(HostID.NEW_LICENSE_PREFIX);
	}

	/**
	 * Initialize module group data
	 * @param moduleIndex the index of the module
	 */
	void _initModuleGroupData(int moduleIndex)
	{

		int g = moduleIndex / 6;
		if (g != _lastModuleGroupInit && _ild != null && _ild[g] != null)
		{
			_ild[g].convertToLicenseDef(this, g);
			_lastModuleGroupInit = g;
		}
	}

	/**
	 * Reset the content of the license
	 */
	protected void reset()
	{
		_ild = null;
		_lastModuleGroupInit = -1;
	}
	
	// obtain an InternalLicenseDef from given license string,
	// then convert that internal representation to 'this'
	private final void _decodeLicense(String aLicenseString) throws NumberFormatException
	{

		_ild = new InternalLicenseDef[((_getModuleCount() - 1) / 6) + 1];
		_initArrays();

		// JLK Standard decoding for 6 modules groups.
		// -------------------------------------------
		int nbGroup = ((_getModuleCount() - 1) / 6);
		int start = 0;
		for (int g = 0; g <= nbGroup; g++)
		{
			try
			{
				String sublic = aLicenseString.substring(start, start + 39);
				_ild[g] = new InternalLicenseDef(sublic);

				start += 40; // We added a '-' after each license bloc.
			}
			catch (NumberFormatException nfe)
			{
				throw nfe;
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		// Key
		String key = _replaceSpace(_getProductName());
		
		// Decoding License Info.
		// ----------------------
		String infoLengthStr = aLicenseString.substring(start, start + 4);
		int infoLength = Integer.parseInt(infoLengthStr, 16);

		// Jump the length or -0000-
		start += 5;

		// Get the string of license info, if exists.
		if (infoLength != 0)
		{
			String infosStr = aLicenseString.substring(start, start + infoLength);
			String infos = _decode(key, infosStr);
			_licenseInfo = LicenseInfo.stringToLicenseInfo(infos);
		}
		else {
			_licenseInfo = new LicenseInfo();
		}

		// Jump License Info
		start += infoLength + 1;

		// Decoding parameters.
		// --------------------
		String paramLengthStr = aLicenseString.substring(start, start + 4);
		int paramLength = Integer.parseInt(paramLengthStr, 16);

		// Jump the length or -0000-
		start += 5;

		// Get the string of parameter, if exists.
		if (paramLength != 0)
		{
			String paramStr = aLicenseString.substring(start, start + paramLength);
			String params = _decode(key, paramStr);
			_licenseParamsValues = LicenseParametersValues.stringToModuleParams(params);
		}
		else {
			_licenseParamsValues = new LicenseParametersValues();
		}

		// Can read and check crc value.
		// -----------------------------
		int licLength = start + paramLength;
		String crcValue = aLicenseString.substring(licLength);
		// This crc must be equals to crc of current read licence...
		// Generate a global CRC for key... and append it after parameters without any separator.
		CRC32 crc = new CRC32();
		String strForCrc = new String(aLicenseString.substring(0, licLength));
		crc.update(strForCrc.getBytes());
		String newCrc = "" + crc.getValue();
		if (!crcValue.equals(newCrc))
		{
			throw new NumberFormatException("Crc corrupted");
		}

		// Reinit content.
		_lastModuleGroupInit = -1;
		_initModuleGroupData(0);
	}

	/**
	 * Decode a String.
	 * 
	 * @param key
	 *        the key used to decode.
	 * @param cryptStr
	 *        the string to decode.
	 * @return the decoded string.
	 */
	private String _decode(String key, String cryptStr)
	{

		String value = null;

		try
		{
			DesDecrypter decrypt = new DesDecrypter(key);
			value = decrypt.decrypt(cryptStr);
		}
		catch (Exception ex)
		{}

		return value;
	}

	/**
	 * Read the license, and initialize all fields of this
	 * 
	 * @param is
	 *        the input stream of the license file
	 * @param decodeKey
	 *        true if the license key is decoded, otherwise false.
	 * @throws Exception
	 *         if error
	 */
	protected final void _readLicense(InputStream is, boolean decodeKey) throws Exception
	{

		BufferedReader bufReader = new BufferedReader(new InputStreamReader(is));
		
		try
		{
			// Read license model part
			_readLicenseModel(bufReader);

			if (decodeKey)
			{
				// Read license key
				_decodeLicense(_readLicenseKey(bufReader));
			}
			
			_isLicenseModel = !decodeKey;
		}
		finally
		{
			try
			{
				bufReader.close();
			}
			catch (Exception e)
			{}
		}
	}

	/**
	 * Read the license model, and initialize all fields of this
	 * 
	 * @param bufReader the buffered reader of the license model
	 * @throws Exception if error
	 */
	private void _readLicenseModel(BufferedReader bufReader) throws Exception
	{

		// First line, contains product name.
		Product tmpProduct = new Product(bufReader.readLine());
		
		// Second line, nb of modules to be read.
		int nbModules = Integer.parseInt(bufReader.readLine());

		// Must contain, plugin index on first line and plugin name.
		StringBuffer sum = new StringBuffer(tmpProduct.getName());
		sum.append("" + nbModules);

		// Read all modules names and params values
		for (int m = 0; m < nbModules; m++)
		{
			String moduleLine = bufReader.readLine();

			// Read module with parameters
			Module module = _readModule(moduleLine);
			tmpProduct.addModule(module);
			
			// Recompute check sum as in write method.
			sum.append(module.getName());
			for (Iterator<String> it = module.getParametersList().iterator(); it.hasNext();)
			{
				sum.append(it.next());
			}
		}

		// Check the validity of the product
		if (_product != null && !_product.equals(tmpProduct))
		{
			throw new InvalidLicenseException("The license file is invalid for the product "
				+ _product.getName());
		}
		_product = tmpProduct;
		
		// Init arrays
		_initArrays();
		
		// Check next encoded value, for checksum on modules and parameters names.
		int[] values = HashString.hashString(sum.toString());
		int valStr = ((values[0] + values[1] + values[2]) % 1023) + 1;
		if (!HostID.encodeIntValue(valStr).equals(bufReader.readLine()))
		{
			throw new IllegalArgumentException("Unable to read licence stream. Bad modules control");
		}

		// permArrays reading.
		for (int l = (_permArray.length - 1); l >= 0; l--) {
			_permArray[l] = bufReader.readLine();
		}

		// Write the per_array_checksums.
		_permArrayChecksum = Integer.parseInt(bufReader.readLine());

		// Then Roll perm arrays.
		_rollPermArray = Integer.parseInt(bufReader.readLine());

		// The all values in rool array.
		int v = _hexArray.length - 1;
		String harrayStr = bufReader.readLine();
		StringTokenizer stk = new StringTokenizer(harrayStr, " ");
		while (stk.hasMoreTokens())
		{
			String vs = stk.nextToken();
			_hexArray[v--] = Integer.parseInt(vs);
		}
	}

	/**
	 * Return the module corresponding to the string.
	 * @param str the string
	 * @return the module
	 */
	private Module _readModule(String str)
	{

		Module module = null;

		// Read module name
		int pos = str.indexOf(MODULE_PARAMETER_SEPARATOR);
		String moduleName = str;
		if (pos > 0) {
			moduleName = str.substring(0, pos);
		}
		module = new Module(moduleName);

		// Read parameters
		if (pos > 0) {
			String paramsLine = str.substring(pos + MODULE_PARAMETER_SEPARATOR.length(),
				str.length());
			if (paramsLine != null && paramsLine.length() > 0) {
				StringTokenizer stk = new StringTokenizer(paramsLine, MODULE_PARAMETER_SEPARATOR);
				while (stk.hasMoreTokens())
				{
					module.addParameter(stk.nextToken());
				}
			}
		}
		
		return module;
	}

	/**
	 * Read the license string from BufferedReader.
	 * 
	 * @param bufReader
	 *        the buffered reader of the license
	 * @throws IOException
	 *         if error during the reading
	 */
	private String _readLicenseKey(BufferedReader bufReader) throws IOException
	{

		// Must read all buffers until last line, but parameters may contain \n !!
		StringBuffer strBuf = new StringBuffer();
		// Read first line
		String line = bufReader.readLine();
		// Read others lines
		while (line != null)
		{
			strBuf.append(line);
			// Next line
			line = bufReader.readLine();
		}

		return strBuf.toString();
	}
	
	/**
	 * Create the default license file name.
	 * 
	 * @param productName
	 *        the product name.
	 * @return the default license file name.
	 */
	protected static String _createDefaultLicenseFileName(String productName)
	{

		return _createFileName(productName, 0);
	}

	/**
	 * Create the file name with the product name and the identifier.
	 * @param productName the product name
	 * @param id the identifier
	 * @return the filename
	 */
	protected static String _createFileName(String productName, int id) {
		
		StringBuffer buf = new StringBuffer();
		if (productName != null) {
			buf.append(_replaceSpace(productName));
		}
		if (id > 0) {
			buf.append("_").append(_intToString(id));
		}
		buf.append(LICENSE_FILE_EXT);
		
		return buf.toString();
	}
	
	/**
	 * Convert an integer to a string with a length of 4 characters minimum.
	 * @param value the integer
	 * @return the string
	 */
	private static String _intToString(int value)
	{
		StringBuffer buf = new StringBuffer();
		buf.append(value);
		while (buf.length() < 4)
		{
			buf.insert(0, "0");
		}
		return buf.toString();
	}
	
	/**
	 * Replace ' ' by '_'
	 * @param str the string to replace
	 * @return the new string
	 */
	protected static String _replaceSpace(String str) {
		
		String newStr = null;
		if (str != null) {
			newStr = str.replaceAll(" ", "_");
		}
		return newStr;
	}

	protected class InternalLicenseDef
	{
		protected class InternalExpDate
		{
			public int day; // 5b - 31..1 reversed, 0 error (random = forever)
			public int month; // 4b - 0..11, (13 = forever), else error
			public int year; // 5b - 0..31, (~day = forever)
			public int yearModulo; // 8b - 0..255, (~day = forever)
			public int check; // 6b - ((day << 1) ^ year) | ~month
		}

		protected class InternalModuleLicense
		{
			public int mode; // 2b - 00 no, 01 yes
			public int index; // 2b - 00 ed0, 01 ed1, 10 ed2, 11 forever
			public int nm; // 2b - ~mode
			public int ni; // 2b - ~index
			public int mxi; // 2b - mode ^ index
		}

		protected InternalExpDate[] ied = {new InternalExpDate(), new InternalExpDate(),
			new InternalExpDate()}; // 3 * 20 = 60b
		protected InternalModuleLicense[] iml = {new InternalModuleLicense(),
			new InternalModuleLicense(), new InternalModuleLicense(), new InternalModuleLicense(),
			new InternalModuleLicense(), new InternalModuleLicense()}; // 6 * 10 = 60b
		protected int serchar0; // 6b - 0..25 == 'A'..'Z', 38..63 == 'a'..'z'; 26..37 err
		protected int serchar1; // 6b - 0..25 == 'A'..'Z', 38..63 == 'a'..'z'; 26..37 err
		protected int sernum; // 14b - 1..9999 == number; 0, 10000..16383 err
		protected int vers; // 8b - 0..255
		protected int auxA; // 8b - 0..255
		protected int auxB; // 8b - 0..255
		protected int auxC; // 3b - 0..7
		protected int auxD; // 1b - 0..1
		protected int na; // 8b - ~auxA
		protected int nb; // 8b - ~auxB
		protected int nc; // 3b - ~auxC
		protected int nd; // 1b - ~auxD
		// total 192b

		transient int[] val = new int[32];

		protected InternalLicenseDef(LicenseDef ld, int group)
		{

			int i;
			int start = group * 6;

			for (i = 0; i < 3; i++)
			{
				if (ld.ed[i].isDefined())
				{
					ied[i].day = 31 - (ld.ed[i].day() - 1);
					ied[i].month = ld.ed[i].month();
					ied[i].year = ld.ed[i].year();
					ied[i].yearModulo = ld.ed[i].yearModulo();
				}
				else
				{
					int r = (int) (System.currentTimeMillis() / 1000);
					ied[i].day = r & 31;
					ied[i].month = 13;
					ied[i].year = (~ied[i].day) & 31;
					ied[i].yearModulo = 0;
				}
				ied[i].check = ((((ied[i].day) << 1) ^ ied[i].year) | ~ied[i].month) & 63;
			}

			for (i = 0; i < 6; i++)
			{
				if (ld.ml != null && (start + i) < ld.ml.length)
				{
					iml[i].mode = ld.ml[start + i].mode;
					if (ld.ml[start + i].exp_date_index == -1)
						iml[i].index = 3;
					else
						iml[i].index = ld.ml[start + i].exp_date_index;
				}
				else
				{
					iml[i].mode = Permission.NO.ordinal();
					iml[i].index = 3;
				}

				iml[i].nm = (~iml[i].mode) & 3;
				iml[i].ni = (~iml[i].index) & 3;
				iml[i].mxi = (iml[i].mode ^ iml[i].index) & 3;

			}

			vers = ld.version;

			auxA = ld.aux_A;
			auxB = ld.aux_B;
			auxC = ld.aux_C;
			auxD = ld.aux_D;

			na = (~auxA) & 255;
			nb = (~auxB) & 255;
			nc = (~auxC) & 7;
			nd = (~auxD) & 1;
		}

		int LOW3(int i)
		{

			return val[i] & 7;
		}

		int HIGH3(int i)
		{

			return ((val[i] & 56) >>> 3) & 7;
		}

		int LOW2(int i)
		{

			return val[i] & 3;
		}

		int MID2(int i)
		{

			return ((val[i] & 12) >>> 2) & 3;
		}

		int HIGH2(int i)
		{

			return ((val[i] & 48) >>> 4) & 3;
		}

		int LOW4(int i)
		{

			return val[i] & 15;
		}

		int MIDHIGH4(int i)
		{

			return ((val[i] & 60) >>> 2) & 15;
		}

		InternalLicenseDef(String aLicenseString) throws NumberFormatException
		{

			int sum = 0, i, si;
			char sc;

			// verify permArray[] checksum

			for (i = 0; i < _permArray.length; i++)
			{
				String perm = _permArray[i];
				for (int j = 0; j < 64; j++)
					sum += perm.charAt(j);
			}
			if (sum != _permArrayChecksum)
				throw new NumberFormatException("checksum error");

			// convert license string to 6-bit values

			if ((aLicenseString.length() != 39) || (aLicenseString.charAt(4) != '-')
				|| (aLicenseString.charAt(9) != '-') || (aLicenseString.charAt(14) != '-')
				|| (aLicenseString.charAt(19) != '-') || (aLicenseString.charAt(24) != '-')
				|| (aLicenseString.charAt(29) != '-') || (aLicenseString.charAt(34) != '-'))
				throw new NumberFormatException("malformed string");

			for (i = si = 0; i < _permArray.length; i++, si++)
			{
				String perm = _permArray[i];
				int pi;
				if ((sc = aLicenseString.charAt(si)) == '-')
					sc = aLicenseString.charAt(++si);
				if ((pi = perm.indexOf(sc)) == -1)
					throw new NumberFormatException("invalid string");
				val[_hexArray[i]] = (pi - _rollPermArray) % 64;
			}

			// extract fields from 6-bit values

			ied[0].day = HIGH3(11) + (HIGH2(4) << 3);
			ied[0].month = LOW2(1) + (HIGH2(18) << 2);
			ied[0].year = HIGH3(2) + (HIGH2(17) << 3);
			ied[0].yearModulo = MIDHIGH4(20) + (LOW4(29) << 4);
			ied[0].check = LOW3(12) + (LOW3(22) << 3);

			ied[1].day = HIGH3(12) + (MID2(16) << 3);
			ied[1].month = LOW2(8) + (MID2(6) << 2);
			ied[1].year = LOW3(11) + (MID2(19) << 3);
			ied[1].yearModulo = LOW3(27) + (LOW2(5) << 6) + (LOW3(2) << 3);
			ied[1].check = HIGH3(0) + (LOW3(23) << 3);

			ied[2].day = HIGH3(22) + (MID2(7) << 3);
			ied[2].month = HIGH2(14) + (LOW2(21) << 2);
			ied[2].year = HIGH3(9) + (MID2(13) << 3);
			ied[2].yearModulo = LOW3(9) + (val[15] & 24) + ((val[25] & 56) << 3);
			ied[2].check = HIGH3(23) + (LOW3(0) << 3);
			
			iml[0].mode = MID2(3);
			iml[0].index = LOW2(18);
			iml[0].nm = HIGH2(8);
			iml[0].ni = MID2(14);
			iml[0].mxi = LOW2(16);

			iml[1].mode = LOW2(13);
			iml[1].index = HIGH2(19);
			iml[1].nm = HIGH2(1);
			iml[1].ni = LOW2(7);
			iml[1].mxi = MID2(10);

			iml[2].mode = LOW2(4);
			iml[2].index = MID2(17);
			iml[2].nm = HIGH2(6);
			iml[2].ni = LOW2(15);
			iml[2].mxi = HIGH2(21);

			iml[3].mode = HIGH2(10);
			iml[3].index = HIGH2(3);
			iml[3].nm = LOW2(20);
			iml[3].ni = MID2(4);
			iml[3].mxi = LOW2(14);

			iml[4].mode = LOW2(6);
			iml[4].index = MID2(21);
			iml[4].nm = HIGH2(16);
			iml[4].ni = LOW2(10);
			iml[4].mxi = HIGH2(13);

			iml[5].mode = MID2(1);
			iml[5].index = HIGH2(5);
			iml[5].nm = LOW2(17);
			iml[5].ni = MID2(8);
			iml[5].mxi = MID2(18);

			vers = MID2(15) + (LOW2(3) << 2) + (HIGH2(7) << 4) + (LOW2(19) << 6);

			auxA = LOW2(26) + (LOW2(31) << 2) + (MIDHIGH4(24) << 4);
			auxB = MIDHIGH4(30) + (HIGH2(28) << 4) + (LOW2(24) << 6);
			auxC = HIGH3(27);
			auxD = (LOW4(5) >>> 3) & 1;

			na = LOW2(30) + (LOW4(28) << 2) + (HIGH2(29) << 6);
			nb = MIDHIGH4(26) + (MIDHIGH4(31) << 4);
			nc = LOW3(25);
			nd = (LOW4(5) >>> 2) & 1;
			
			// check ied fields
			boolean error = false;

			for (i = 0; i < 3; i++)
			{
				if (ied[i].month == 13)
				{
					if ((ied[i].year != (~ied[i].day & 31)) || (ied[i].yearModulo != 0))
					{
						error = true;
						break;
					}
				}
				else
				{
					if (ied[i].day == 0)
					{
						error = true;
						break;
					}
					if (ied[i].month > 11)
					{
						error = true;
						break;
					}
				}
				if (ied[i].check != ((((ied[i].day << 1) ^ ied[i].year) | ~(ied[i].month)) & 63))
				{
					error = true;
					break;
				}
			}
			if (error)
				throw new NumberFormatException("invalid exp date");

			// check iml fields

			for (i = 0; i < 6; i++)
			{
				if (iml[i].nm != (~iml[i].mode & 3))
				{
					error = true;
					break;
				}
				if (iml[i].ni != (~iml[i].index & 3))
				{
					error = true;
					break;
				}
				if (iml[i].mxi != ((iml[i].mode ^ iml[i].index) & 3))
				{
					error = true;
					break;
				}
			}
			if (error)
				throw new NumberFormatException("invalid module license");
			
			// check aux fields
			if (na != (~auxA & 255))
				error = true;
			else if (nb != (~auxB & 255))
				error = true;
			else if (nc != (~auxC & 7))
				error = true;
			else if (nd != (~auxD & 1))
				error = true;
			if (error)
				throw new NumberFormatException("invalid aux values");
		}

		void convertToLicenseDef(LicenseDef ld, int group)
		{

			int start = group * 6;
			int i;

			for (i = 0; i < 3; i++)
			{
				if (ied[i].month == 13)
					ld.ed[i] = new ExpDate();
				else
					ld.ed[i] = new ExpDate(ied[i].year, ied[i].month, (31 - ied[i].day) + 1,
						ied[i].yearModulo);
			}

			// On doit boucler soit jusqu'à 6 si size >= 6 ou alors jusquà size
			int bound = Math.min(ld.ml.length - start, 6);
			int index = start;
			for (i = 0; i < bound; i++)
			{
				ld.ml[index].mode = iml[i].mode;
				if (iml[i].index == 3)
					ld.ml[index].exp_date_index = -1;
				else
					ld.ml[index].exp_date_index = iml[i].index;
				index++;
			}

			ld.version = vers;

			ld.aux_A = auxA;
			ld.aux_B = auxB;
			ld.aux_C = auxC;
			ld.aux_D = auxD;
		}
	}
}

package eu.cessar.ct.core.security.license;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
	private static final String BUNDLE_NAME = "eu.cessar.ct.core.security.license.messages"; //$NON-NLS-1$
	public static String licenseDialog_label_Address;
	public static String licenseDialog_label_City;
	public static String licenseDialog_label_Company;
	public static String licenseDialog_label_Country;
	public static String licenseDialog_label_DateOfDemand;
	public static String licenseDialog_label_Department;
	public static String licenseDialog_label_Email;
	public static String licenseDialog_label_Expiration;
	public static String licenseDialog_label_FirstName;
	public static String licenseDialog_label_InfoAboutTheProductLicense;
	public static String licenseDialog_label_LastName;
	public static String licenseDialog_label_MailBody;
	public static String licenseDialog_label_MailSubject;
	public static String licenseDialog_label_Module;
	public static String licenseDialog_label_NoLicenseForProduct;
	public static String licenseDialog_label_Permission;
	public static String licenseDialog_label_Phone;
	public static String licenseDialog_label_User;
	public static String licenseDialog_label_Zip;
	public static String licenseDialog_NoRight;
	public static String licenseDialog_Valid;
	public static String licenseInfoCessar;
	public static String licenseInfoAdvanced;
	public static String licenseDialog_label_DistributedTo;

	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
	}
}

package eu.cessar.ct.core.security.license;

/**
 * Exception is used if a license is invalid. 
 */
public class InvalidLicenseException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2915048000704660318L;

	/**
	 * Default constructor
	 */
	public InvalidLicenseException()
	{
		super();
	}

	/**
     * Constructs a InvalidLicenseException with the specified detail message and cause.
	 * 
	 * @param message the message
	 */
	public InvalidLicenseException(String message)
	{
		super(message);
	}

	/**
     * Constructs a InvalidLicenseException with the specified detail message and cause.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
	public InvalidLicenseException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
     * Constructs a InvalidLicenseException with the specified detail message and cause.
	 * 
	 * @param cause the cause
	 */
	public InvalidLicenseException(Throwable cause)
	{
		super(cause);
	}
}

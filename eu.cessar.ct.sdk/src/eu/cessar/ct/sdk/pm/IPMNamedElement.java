/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package eu.cessar.ct.sdk.pm;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>IPM Named Element</b></em>'. <!-- end-user-doc -->
 *
 *
 * @see eu.cessar.ct.sdk.pm.pmPackage#getIPMNamedElement()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IPMNamedElement extends IPMElement
{
	/**
	 * Return the shortname of the element
	 * 
	 * @generated not
	 */
	String getShortName();

	/**
	 * Check if the shortname is set
	 * 
	 * @generated not
	 */
	boolean isSetShortName();
} // IPMNamedElement

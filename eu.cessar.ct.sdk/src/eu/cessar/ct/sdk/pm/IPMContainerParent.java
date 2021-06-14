/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.cessar.ct.sdk.pm;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IPM Container Parent</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see eu.cessar.ct.sdk.pm.pmPackage#getIPMContainerParent()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IPMContainerParent extends IPMNamedElement
{

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model annotation="EMF_PROXY TYPE='OP_isCompatibleWith'"
	 * @generated
	 */
	Boolean isCompatibleWith(String qualifiedName);
} // IPMContainerParent

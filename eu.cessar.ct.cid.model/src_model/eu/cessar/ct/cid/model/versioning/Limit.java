/**
 */
package eu.cessar.ct.cid.model.versioning;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Limit</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.cessar.ct.cid.model.versioning.Limit#isIncluding <em>Including</em>}</li>
 *   <li>{@link eu.cessar.ct.cid.model.versioning.Limit#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.cessar.ct.cid.model.versioning.VersioningPackage#getLimit()
 * @model
 * @generated
 */
public interface Limit extends EObject
{
	/**
	 * Returns the value of the '<em><b>Including</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Including</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Including</em>' attribute.
	 * @see #setIncluding(boolean)
	 * @see eu.cessar.ct.cid.model.versioning.VersioningPackage#getLimit_Including()
	 * @model
	 * @generated
	 */
	boolean isIncluding();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.cid.model.versioning.Limit#isIncluding <em>Including</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Including</em>' attribute.
	 * @see #isIncluding()
	 * @generated
	 */
	void setIncluding(boolean value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see eu.cessar.ct.cid.model.versioning.VersioningPackage#getLimit_Value()
	 * @model dataType="eu.cessar.ct.cid.model.datatypes.VersionLiteral"
	 * @generated
	 */
	String getValue();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.cid.model.versioning.Limit#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

} // Limit

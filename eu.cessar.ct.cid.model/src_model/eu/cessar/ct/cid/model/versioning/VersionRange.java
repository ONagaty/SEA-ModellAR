/**
 */
package eu.cessar.ct.cid.model.versioning;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Version Range</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.cessar.ct.cid.model.versioning.VersionRange#getFrom <em>From</em>}</li>
 *   <li>{@link eu.cessar.ct.cid.model.versioning.VersionRange#getTo <em>To</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.cessar.ct.cid.model.versioning.VersioningPackage#getVersionRange()
 * @model
 * @generated
 */
public interface VersionRange extends EObject
{
	/**
	 * Returns the value of the '<em><b>From</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>From</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>From</em>' containment reference.
	 * @see #setFrom(Limit)
	 * @see eu.cessar.ct.cid.model.versioning.VersioningPackage#getVersionRange_From()
	 * @model containment="true"
	 * @generated
	 */
	Limit getFrom();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.cid.model.versioning.VersionRange#getFrom <em>From</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>From</em>' containment reference.
	 * @see #getFrom()
	 * @generated
	 */
	void setFrom(Limit value);

	/**
	 * Returns the value of the '<em><b>To</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>To</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>To</em>' containment reference.
	 * @see #setTo(Limit)
	 * @see eu.cessar.ct.cid.model.versioning.VersioningPackage#getVersionRange_To()
	 * @model containment="true"
	 * @generated
	 */
	Limit getTo();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.cid.model.versioning.VersionRange#getTo <em>To</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To</em>' containment reference.
	 * @see #getTo()
	 * @generated
	 */
	void setTo(Limit value);

} // VersionRange

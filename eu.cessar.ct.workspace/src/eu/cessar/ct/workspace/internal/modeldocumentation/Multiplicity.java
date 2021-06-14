/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.cessar.ct.workspace.internal.modeldocumentation;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multiplicity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.Multiplicity#getLower <em>Lower</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.Multiplicity#getUpper <em>Upper</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.Multiplicity#getMultiplicity <em>Multiplicity</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getMultiplicity()
 * @model
 * @generated
 */
public interface Multiplicity extends EObject {
	/**
	 * Returns the value of the '<em><b>Lower</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lower</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lower</em>' attribute.
	 * @see #setLower(String)
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getMultiplicity_Lower()
	 * @model required="true"
	 * @generated
	 */
	String getLower();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.Multiplicity#getLower <em>Lower</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lower</em>' attribute.
	 * @see #getLower()
	 * @generated
	 */
	void setLower(String value);

	/**
	 * Returns the value of the '<em><b>Upper</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Upper</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Upper</em>' attribute.
	 * @see #setUpper(String)
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getMultiplicity_Upper()
	 * @model required="true"
	 * @generated
	 */
	String getUpper();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.Multiplicity#getUpper <em>Upper</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Upper</em>' attribute.
	 * @see #getUpper()
	 * @generated
	 */
	void setUpper(String value);

	/**
	 * Returns the value of the '<em><b>Multiplicity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multiplicity</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multiplicity</em>' reference.
	 * @see #setMultiplicity(AttributeDef)
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getMultiplicity_Multiplicity()
	 * @model
	 * @generated
	 */
	AttributeDef getMultiplicity();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.Multiplicity#getMultiplicity <em>Multiplicity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Multiplicity</em>' reference.
	 * @see #getMultiplicity()
	 * @generated
	 */
	void setMultiplicity(AttributeDef value);

} // Multiplicity

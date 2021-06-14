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
 * A representation of the model object '<em><b>Def Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.DefElement#getMultiplicity <em>Multiplicity</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.DefElement#getShortName <em>Short Name</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.DefElement#getDescription <em>Description</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getDefElement()
 * @model
 * @generated
 */
public interface DefElement extends EObject {
	/**
	 * Returns the value of the '<em><b>Multiplicity</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multiplicity</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multiplicity</em>' containment reference.
	 * @see #setMultiplicity(Multiplicity)
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getDefElement_Multiplicity()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Multiplicity getMultiplicity();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.DefElement#getMultiplicity <em>Multiplicity</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Multiplicity</em>' containment reference.
	 * @see #getMultiplicity()
	 * @generated
	 */
	void setMultiplicity(Multiplicity value);

	/**
	 * Returns the value of the '<em><b>Short Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Short Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Short Name</em>' attribute.
	 * @see #setShortName(String)
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getDefElement_ShortName()
	 * @model required="true"
	 * @generated
	 */
	String getShortName();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.DefElement#getShortName <em>Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Short Name</em>' attribute.
	 * @see #getShortName()
	 * @generated
	 */
	void setShortName(String value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getDefElement_Description()
	 * @model required="true"
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.DefElement#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

} // DefElement

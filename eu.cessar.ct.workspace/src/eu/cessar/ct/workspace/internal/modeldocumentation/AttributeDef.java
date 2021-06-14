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
 * A representation of the model object '<em><b>Attribute Def</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.AttributeDef#getName <em>Name</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.AttributeDef#getTypeOfValue <em>Type Of Value</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.AttributeDef#getMultiplicity <em>Multiplicity</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getAttributeDef()
 * @model
 * @generated
 */
public interface AttributeDef extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getAttributeDef_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.AttributeDef#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Type Of Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type Of Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type Of Value</em>' attribute.
	 * @see #setTypeOfValue(String)
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getAttributeDef_TypeOfValue()
	 * @model required="true"
	 * @generated
	 */
	String getTypeOfValue();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.AttributeDef#getTypeOfValue <em>Type Of Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type Of Value</em>' attribute.
	 * @see #getTypeOfValue()
	 * @generated
	 */
	void setTypeOfValue(String value);

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
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getAttributeDef_Multiplicity()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Multiplicity getMultiplicity();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.AttributeDef#getMultiplicity <em>Multiplicity</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Multiplicity</em>' containment reference.
	 * @see #getMultiplicity()
	 * @generated
	 */
	void setMultiplicity(Multiplicity value);

} // AttributeDef

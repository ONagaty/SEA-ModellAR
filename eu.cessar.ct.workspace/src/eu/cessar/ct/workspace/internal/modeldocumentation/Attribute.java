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
 * A representation of the model object '<em><b>Attribute</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.Attribute#getInstanceOf <em>Instance Of</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.Attribute#getName <em>Name</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.Attribute#getType <em>Type</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.Attribute#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getAttribute()
 * @model
 * @generated
 */
public interface Attribute extends EObject {
	/**
	 * Returns the value of the '<em><b>Instance Of</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Instance Of</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Instance Of</em>' containment reference.
	 * @see #setInstanceOf(AttributeDef)
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getAttribute_InstanceOf()
	 * @model containment="true" required="true"
	 * @generated
	 */
	AttributeDef getInstanceOf();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.Attribute#getInstanceOf <em>Instance Of</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Instance Of</em>' containment reference.
	 * @see #getInstanceOf()
	 * @generated
	 */
	void setInstanceOf(AttributeDef value);

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
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getAttribute_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.Attribute#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getAttribute_Type()
	 * @model required="true"
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.Attribute#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

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
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getAttribute_Value()
	 * @model
	 * @generated
	 */
	String getValue();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.Attribute#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

} // Attribute

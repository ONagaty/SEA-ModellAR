/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.cessar.ct.workspace.internal.modeldocumentation;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Doc Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#getInstanceOf <em>Instance Of</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#getAttributes <em>Attributes</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#getContains <em>Contains</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#getShortName <em>Short Name</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#getType <em>Type</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#isIsInstance <em>Is Instance</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getDocElement()
 * @model
 * @generated
 */
public interface DocElement extends EObject {
	/**
	 * Returns the value of the '<em><b>Instance Of</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Instance Of</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Instance Of</em>' containment reference.
	 * @see #setInstanceOf(DefElement)
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getDocElement_InstanceOf()
	 * @model containment="true" required="true"
	 * @generated
	 */
	DefElement getInstanceOf();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#getInstanceOf <em>Instance Of</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Instance Of</em>' containment reference.
	 * @see #getInstanceOf()
	 * @generated
	 */
	void setInstanceOf(DefElement value);

	/**
	 * Returns the value of the '<em><b>Attributes</b></em>' containment reference list.
	 * The list contents are of type {@link eu.cessar.ct.workspace.internal.modeldocumentation.Attribute}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attributes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attributes</em>' containment reference list.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getDocElement_Attributes()
	 * @model containment="true"
	 * @generated
	 */
	EList<Attribute> getAttributes();

	/**
	 * Returns the value of the '<em><b>Contains</b></em>' containment reference list.
	 * The list contents are of type {@link eu.cessar.ct.workspace.internal.modeldocumentation.DocElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contains</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contains</em>' containment reference list.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getDocElement_Contains()
	 * @model containment="true"
	 * @generated
	 */
	EList<DocElement> getContains();

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
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getDocElement_ShortName()
	 * @model required="true"
	 *        extendedMetaData="kind='attribute'"
	 * @generated
	 */
	String getShortName();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#getShortName <em>Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Short Name</em>' attribute.
	 * @see #getShortName()
	 * @generated
	 */
	void setShortName(String value);

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
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getDocElement_Type()
	 * @model required="true"
	 *        extendedMetaData="kind='attribute'"
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Is Instance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Instance</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Instance</em>' attribute.
	 * @see #setIsInstance(boolean)
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#getDocElement_IsInstance()
	 * @model required="true"
	 *        extendedMetaData="kind='attribute' namespace=''"
	 * @generated
	 */
	boolean isIsInstance();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#isIsInstance <em>Is Instance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Instance</em>' attribute.
	 * @see #isIsInstance()
	 * @generated
	 */
	void setIsInstance(boolean value);

} // DocElement

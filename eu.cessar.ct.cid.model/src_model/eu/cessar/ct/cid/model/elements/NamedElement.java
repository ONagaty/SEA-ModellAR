/**
 */
package eu.cessar.ct.cid.model.elements;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Named Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.cessar.ct.cid.model.elements.NamedElement#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.cessar.ct.cid.model.elements.ElementsPackage#getNamedElement()
 * @model abstract="true"
 * @generated
 */
public interface NamedElement extends EObject
{
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
	 * @see eu.cessar.ct.cid.model.elements.ElementsPackage#getNamedElement_Name()
	 * @model dataType="eu.cessar.ct.cid.model.datatypes.Identifier"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.cid.model.elements.NamedElement#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // NamedElement

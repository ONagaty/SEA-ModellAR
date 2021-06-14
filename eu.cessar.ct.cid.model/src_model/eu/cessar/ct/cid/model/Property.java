/**
 */
package eu.cessar.ct.cid.model;

import eu.cessar.ct.cid.model.elements.NamedElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.cessar.ct.cid.model.Property#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.cessar.ct.cid.model.ModelPackage#getProperty()
 * @model
 * @generated
 */
public interface Property extends NamedElement
{
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
	 * @see eu.cessar.ct.cid.model.ModelPackage#getProperty_Value()
	 * @model
	 * @generated
	 */
	String getValue();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.cid.model.Property#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

} // Property

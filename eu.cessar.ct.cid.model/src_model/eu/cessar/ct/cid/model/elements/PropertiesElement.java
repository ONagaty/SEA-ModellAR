/**
 */
package eu.cessar.ct.cid.model.elements;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.cid.model.Property;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Properties Element</b></em>'. <!-- end-user-doc
 * -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link eu.cessar.ct.cid.model.elements.PropertiesElement#getProperties <em>Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.cessar.ct.cid.model.elements.ElementsPackage#getPropertiesElement()
 * @model abstract="true"
 * @generated
 */
public interface PropertiesElement extends EObject
{
	/**
	 * Returns the value of the '<em><b>Properties</b></em>' containment reference list. The list contents are of type
	 * {@link eu.cessar.ct.cid.model.Property}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Properties</em>' containment reference list isn't clear, there really should be more
	 * of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Properties</em>' containment reference list.
	 * @see eu.cessar.ct.cid.model.elements.ElementsPackage#getPropertiesElement_Properties()
	 * @model containment="true" extendedMetaData="kind='element' name='property' namespace='http://www.cessar.eu/cid'"
	 * @generated
	 */
	List<Property> getProperties();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Return all Properties with the specified
	 * <code>name</code> defined under current Artifact
	 *
	 * @param name
	 *        Property name
	 * @return list of Properties having specified name <!-- end-model-doc -->
	 * @model dataType="eu.cessar.ct.cid.model.datatypes.PropertyList"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return
	 *        eu.cessar.ct.cid.model.internal.util.PropertiesElementServices.getProperties(this, name);\r\n'"
	 * @generated
	 */
	List<Property> getProperties(String name);

} // PropertiesElement

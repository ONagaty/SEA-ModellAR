/**
 */
package eu.cessar.ct.cid.model;

import eu.cessar.ct.cid.model.elements.PropertiesElement;
import eu.cessar.ct.cid.model.elements.TypedElement;

import eu.cessar.ct.cid.model.versioning.VersionRange;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dependency</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.cessar.ct.cid.model.Dependency#isMandatory <em>Mandatory</em>}</li>
 *   <li>{@link eu.cessar.ct.cid.model.Dependency#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.cessar.ct.cid.model.ModelPackage#getDependency()
 * @model
 * @generated
 */
public interface Dependency extends TypedElement, PropertiesElement
{
	/**
	 * Returns the value of the '<em><b>Mandatory</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mandatory</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mandatory</em>' attribute.
	 * @see #setMandatory(boolean)
	 * @see eu.cessar.ct.cid.model.ModelPackage#getDependency_Mandatory()
	 * @model default="true"
	 * @generated
	 */
	boolean isMandatory();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.cid.model.Dependency#isMandatory <em>Mandatory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mandatory</em>' attribute.
	 * @see #isMandatory()
	 * @generated
	 */
	void setMandatory(boolean value);

	/**
	 * Returns the value of the '<em><b>Version</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Version</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' containment reference.
	 * @see #setVersion(VersionRange)
	 * @see eu.cessar.ct.cid.model.ModelPackage#getDependency_Version()
	 * @model containment="true"
	 * @generated
	 */
	VersionRange getVersion();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.cid.model.Dependency#getVersion <em>Version</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' containment reference.
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(VersionRange value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Return the concrete binding associated with this dependency. If there is no such binding, null will be returned.
	 * 
	 * @return the concrete binding or null if there is no binding available
	 * <!-- end-model-doc -->
	 * @model kind="operation" dataType="eu.cessar.ct.cid.model.datatypes.IDependencyBinding"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='<%eu.cessar.ct.cid.model.IDependencyBinding%> binding = (<%eu.cessar.ct.cid.model.IDependencyBinding%>) getField(BINDING_FLAG);\r\nif (binding == null)\r\n{\r\n\tbinding = <%eu.cessar.ct.cid.model.internal.util.DependencyServices%>.getConcreteBinding(this);\r\n\tsetField(BINDING_FLAG, binding);\r\n}\r\nreturn binding;\r\n'"
	 * @generated
	 */
	IDependencyBinding getConcreteBinding();

} // Dependency

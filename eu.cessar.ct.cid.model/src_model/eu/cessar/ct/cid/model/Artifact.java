/**
 */
package eu.cessar.ct.cid.model;

import eu.cessar.ct.cid.model.elements.DependantElement;
import eu.cessar.ct.cid.model.elements.NamedElement;
import eu.cessar.ct.cid.model.elements.PropertiesElement;
import eu.cessar.ct.cid.model.elements.TypedElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Artifact</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.cessar.ct.cid.model.Artifact#getTitle <em>Title</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.cessar.ct.cid.model.ModelPackage#getArtifact()
 * @model
 * @generated
 */
public interface Artifact extends DependantElement, TypedElement, NamedElement, PropertiesElement
{
	/**
	 * Returns the value of the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Title</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Title</em>' attribute.
	 * @see #setTitle(String)
	 * @see eu.cessar.ct.cid.model.ModelPackage#getArtifact_Title()
	 * @model
	 * @generated
	 */
	String getTitle();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.cid.model.Artifact#getTitle <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' attribute.
	 * @see #getTitle()
	 * @generated
	 */
	void setTitle(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Return the concrete binding associated with this artifact. If there is no such binding, null will be returned.
	 * 
	 * @return the concrete binding or null if there is no binding available
	 * <!-- end-model-doc -->
	 * @model kind="operation" dataType="eu.cessar.ct.cid.model.datatypes.IArtifactBinding"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='<%eu.cessar.ct.cid.model.IArtifactBinding%> binding = (<%eu.cessar.ct.cid.model.IArtifactBinding%>) getField(BINDING_FLAG);\r\nif (binding == null)\r\n{\r\n\tbinding = <%eu.cessar.ct.cid.model.internal.util.ArtifactServices%>.getConcreteBinding(this);\r\n\tsetField(BINDING_FLAG, binding);\r\n}\r\nreturn binding;\r\n'"
	 * @generated
	 */
	IArtifactBinding getConcreteBinding();

} // Artifact

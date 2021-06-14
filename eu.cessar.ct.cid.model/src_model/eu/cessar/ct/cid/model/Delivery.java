/**
 */
package eu.cessar.ct.cid.model;

import eu.cessar.ct.cid.model.elements.DependantElement;
import eu.cessar.ct.cid.model.elements.NamedElement;
import eu.cessar.ct.cid.model.elements.PropertiesElement;

import java.util.List;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Delivery</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.cessar.ct.cid.model.Delivery#getArtifacts <em>Artifacts</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.cessar.ct.cid.model.ModelPackage#getDelivery()
 * @model
 * @generated
 */
public interface Delivery extends DependantElement, NamedElement, PropertiesElement
{
	/**
	 * Returns the value of the '<em><b>Artifacts</b></em>' containment reference list.
	 * The list contents are of type {@link eu.cessar.ct.cid.model.Artifact}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Artifacts</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Artifacts</em>' containment reference list.
	 * @see eu.cessar.ct.cid.model.ModelPackage#getDelivery_Artifacts()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='artifact' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<Artifact> getArtifacts();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Return all Artifacts of the specified <code>type</code> defined under current Delivery
	 * @param type Artifact type
	 * @return list of Artifacts having specified <code>type</code>
	 * <!-- end-model-doc -->
	 * @model dataType="eu.cessar.ct.cid.model.datatypes.ArtifactList"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return <%eu.cessar.ct.cid.model.internal.util.DeliveryServices%>.getArtifacts(this, type);\r\n'"
	 * @generated
	 */
	List<Artifact> getArtifacts(String type);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Return all Artifacts of the specified <code>type</code> and <code>name</code> defined under current Delivery
	 * @param type Artifact type
	 * @param name Artifact name
	 * @return list of Artifacts having specified <code>type</code>
	 * <!-- end-model-doc -->
	 * @model dataType="eu.cessar.ct.cid.model.datatypes.ArtifactList"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return <%eu.cessar.ct.cid.model.internal.util.DeliveryServices%>.getArtifacts(this, type, name);\r\n'"
	 * @generated
	 */
	List<Artifact> getArtifacts(String type, String name);

} // Delivery

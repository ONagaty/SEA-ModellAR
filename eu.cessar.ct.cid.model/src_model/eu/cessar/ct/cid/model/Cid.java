/**
 */
package eu.cessar.ct.cid.model;

import java.util.List;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cid</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.cessar.ct.cid.model.Cid#getDeliveries <em>Deliveries</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.cessar.ct.cid.model.ModelPackage#getCid()
 * @model
 * @generated
 */
public interface Cid extends EObject
{
	/**
	 * Returns the value of the '<em><b>Deliveries</b></em>' containment reference list.
	 * The list contents are of type {@link eu.cessar.ct.cid.model.Delivery}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Deliveries</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Deliveries</em>' containment reference list.
	 * @see eu.cessar.ct.cid.model.ModelPackage#getCid_Deliveries()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='delivery' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<Delivery> getDeliveries();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Return all Artifacts defined under current Cid 
	 * @return list of Artifacts defined under current Cid 
	 * <!-- end-model-doc -->
	 * @model kind="operation" dataType="eu.cessar.ct.cid.model.datatypes.ArtifactList"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return <%eu.cessar.ct.cid.model.internal.util.CidServices%>.getArtifacts(this);\r\n'"
	 * @generated
	 */
	List<Artifact> getArtifacts();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Return all Artifacts of the specified <code>type</code> defined under current Cid 
	 * @param type Artifact type
	 * @return list of Artifacts having specified <code>type</code>
	 * <!-- end-model-doc -->
	 * @model dataType="eu.cessar.ct.cid.model.datatypes.ArtifactList"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return <%eu.cessar.ct.cid.model.internal.util.CidServices%>.getArtifacts(this, type);\r\n'"
	 * @generated
	 */
	List<Artifact> getArtifacts(String type);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Return all Artifacts of the specified <code>type</code> and <code>name</code> defined under current Cid 
	 * @param type Artifact type
	 * @param name Artifact name
	 * @return list of Artifacts having specified <code>type</code>
	 * <!-- end-model-doc -->
	 * @model dataType="eu.cessar.ct.cid.model.datatypes.ArtifactList"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return <%eu.cessar.ct.cid.model.internal.util.CidServices%>.getArtifacts(this, type, name);\r\n'"
	 * @generated
	 */
	List<Artifact> getArtifacts(String type, String name);

} // Cid

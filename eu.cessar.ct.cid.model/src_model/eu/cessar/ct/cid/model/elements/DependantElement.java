/**
 */
package eu.cessar.ct.cid.model.elements;

import eu.cessar.ct.cid.model.Dependency;
import eu.cessar.ct.cid.model.Metadata;

import java.util.List;

import org.eclipse.core.runtime.IStatus;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dependant Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.cessar.ct.cid.model.elements.DependantElement#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link eu.cessar.ct.cid.model.elements.DependantElement#getDependencies <em>Dependencies</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.cessar.ct.cid.model.elements.ElementsPackage#getDependantElement()
 * @model abstract="true"
 * @generated
 */
public interface DependantElement extends EObject
{
	/**
	 * Returns the value of the '<em><b>Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Metadata</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Metadata</em>' containment reference.
	 * @see #setMetadata(Metadata)
	 * @see eu.cessar.ct.cid.model.elements.ElementsPackage#getDependantElement_Metadata()
	 * @model containment="true"
	 * @generated
	 */
	Metadata getMetadata();

	/**
	 * Sets the value of the '{@link eu.cessar.ct.cid.model.elements.DependantElement#getMetadata <em>Metadata</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Metadata</em>' containment reference.
	 * @see #getMetadata()
	 * @generated
	 */
	void setMetadata(Metadata value);

	/**
	 * Returns the value of the '<em><b>Dependencies</b></em>' containment reference list.
	 * The list contents are of type {@link eu.cessar.ct.cid.model.Dependency}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dependencies</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dependencies</em>' containment reference list.
	 * @see eu.cessar.ct.cid.model.elements.ElementsPackage#getDependantElement_Dependencies()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='dependency' namespace='http://www.cessar.eu/cid'"
	 * @generated
	 */
	EList<Dependency> getDependencies();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Evaluate all dependencies defined by the artifact plus those inherited from Delivery and not overrided.
	 * 
	 * @return the evaluation status.
	 * <!-- end-model-doc -->
	 * @model dataType="eu.cessar.ct.cid.model.datatypes.IStatus"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return <%eu.cessar.ct.cid.model.internal.util.DependantElementServices%>.evaluateDependencies(this);\r\n'"
	 * @generated
	 */
	IStatus evaluateDependencies();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Return all Dependencies of the specified <code>type</code> defined under current Artifact 
	 * @param type dependency type
	 * @return list of Dependencies having specified dependency type
	 * <!-- end-model-doc -->
	 * @model dataType="eu.cessar.ct.cid.model.datatypes.DependencyList"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return <%eu.cessar.ct.cid.model.internal.util.DependantElementServices%>.getDependencies(this, type);\r\n'"
	 * @generated
	 */
	List<Dependency> getDependencies(String type);

} // DependantElement

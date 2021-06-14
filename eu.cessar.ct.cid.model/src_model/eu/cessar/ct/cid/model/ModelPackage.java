/**
 */
package eu.cessar.ct.cid.model;

import eu.cessar.ct.cid.model.elements.ElementsPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see eu.cessar.ct.cid.model.ModelFactory
 * @model kind="package"
 * @generated
 */
public interface ModelPackage extends EPackage
{
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "model";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.cessar.eu/cid";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ModelPackage eINSTANCE = eu.cessar.ct.cid.model.impl.ModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link eu.cessar.ct.cid.model.impl.CidImpl <em>Cid</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.cid.model.impl.CidImpl
	 * @see eu.cessar.ct.cid.model.impl.ModelPackageImpl#getCid()
	 * @generated
	 */
	int CID = 0;

	/**
	 * The feature id for the '<em><b>Deliveries</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CID__DELIVERIES = 0;

	/**
	 * The number of structural features of the '<em>Cid</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CID_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.cid.model.impl.DeliveryImpl <em>Delivery</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.cid.model.impl.DeliveryImpl
	 * @see eu.cessar.ct.cid.model.impl.ModelPackageImpl#getDelivery()
	 * @generated
	 */
	int DELIVERY = 1;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELIVERY__METADATA = ElementsPackage.DEPENDANT_ELEMENT__METADATA;

	/**
	 * The feature id for the '<em><b>Dependencies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELIVERY__DEPENDENCIES = ElementsPackage.DEPENDANT_ELEMENT__DEPENDENCIES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELIVERY__NAME = ElementsPackage.DEPENDANT_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELIVERY__PROPERTIES = ElementsPackage.DEPENDANT_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Artifacts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELIVERY__ARTIFACTS = ElementsPackage.DEPENDANT_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Delivery</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELIVERY_FEATURE_COUNT = ElementsPackage.DEPENDANT_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.cid.model.impl.MetadataImpl <em>Metadata</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.cid.model.impl.MetadataImpl
	 * @see eu.cessar.ct.cid.model.impl.ModelPackageImpl#getMetadata()
	 * @generated
	 */
	int METADATA = 2;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA__PROPERTIES = ElementsPackage.PROPERTIES_ELEMENT__PROPERTIES;

	/**
	 * The number of structural features of the '<em>Metadata</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA_FEATURE_COUNT = ElementsPackage.PROPERTIES_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.cid.model.impl.PropertyImpl <em>Property</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.cid.model.impl.PropertyImpl
	 * @see eu.cessar.ct.cid.model.impl.ModelPackageImpl#getProperty()
	 * @generated
	 */
	int PROPERTY = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__NAME = ElementsPackage.NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__VALUE = ElementsPackage.NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Property</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_FEATURE_COUNT = ElementsPackage.NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.cid.model.impl.DependencyImpl <em>Dependency</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.cid.model.impl.DependencyImpl
	 * @see eu.cessar.ct.cid.model.impl.ModelPackageImpl#getDependency()
	 * @generated
	 */
	int DEPENDENCY = 4;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__TYPE = ElementsPackage.TYPED_ELEMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__PROPERTIES = ElementsPackage.TYPED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Mandatory</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__MANDATORY = ElementsPackage.TYPED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Version</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__VERSION = ElementsPackage.TYPED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Dependency</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY_FEATURE_COUNT = ElementsPackage.TYPED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.cid.model.impl.ArtifactImpl <em>Artifact</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.cid.model.impl.ArtifactImpl
	 * @see eu.cessar.ct.cid.model.impl.ModelPackageImpl#getArtifact()
	 * @generated
	 */
	int ARTIFACT = 5;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARTIFACT__METADATA = ElementsPackage.DEPENDANT_ELEMENT__METADATA;

	/**
	 * The feature id for the '<em><b>Dependencies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARTIFACT__DEPENDENCIES = ElementsPackage.DEPENDANT_ELEMENT__DEPENDENCIES;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARTIFACT__TYPE = ElementsPackage.DEPENDANT_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARTIFACT__NAME = ElementsPackage.DEPENDANT_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARTIFACT__PROPERTIES = ElementsPackage.DEPENDANT_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARTIFACT__TITLE = ElementsPackage.DEPENDANT_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Artifact</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARTIFACT_FEATURE_COUNT = ElementsPackage.DEPENDANT_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.cid.model.Cid <em>Cid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cid</em>'.
	 * @see eu.cessar.ct.cid.model.Cid
	 * @generated
	 */
	EClass getCid();

	/**
	 * Returns the meta object for the containment reference list '{@link eu.cessar.ct.cid.model.Cid#getDeliveries <em>Deliveries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Deliveries</em>'.
	 * @see eu.cessar.ct.cid.model.Cid#getDeliveries()
	 * @see #getCid()
	 * @generated
	 */
	EReference getCid_Deliveries();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.cid.model.Delivery <em>Delivery</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Delivery</em>'.
	 * @see eu.cessar.ct.cid.model.Delivery
	 * @generated
	 */
	EClass getDelivery();

	/**
	 * Returns the meta object for the containment reference list '{@link eu.cessar.ct.cid.model.Delivery#getArtifacts <em>Artifacts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Artifacts</em>'.
	 * @see eu.cessar.ct.cid.model.Delivery#getArtifacts()
	 * @see #getDelivery()
	 * @generated
	 */
	EReference getDelivery_Artifacts();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.cid.model.Metadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Metadata</em>'.
	 * @see eu.cessar.ct.cid.model.Metadata
	 * @generated
	 */
	EClass getMetadata();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.cid.model.Property <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Property</em>'.
	 * @see eu.cessar.ct.cid.model.Property
	 * @generated
	 */
	EClass getProperty();

	/**
	 * Returns the meta object for the attribute '{@link eu.cessar.ct.cid.model.Property#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see eu.cessar.ct.cid.model.Property#getValue()
	 * @see #getProperty()
	 * @generated
	 */
	EAttribute getProperty_Value();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.cid.model.Dependency <em>Dependency</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Dependency</em>'.
	 * @see eu.cessar.ct.cid.model.Dependency
	 * @generated
	 */
	EClass getDependency();

	/**
	 * Returns the meta object for the attribute '{@link eu.cessar.ct.cid.model.Dependency#isMandatory <em>Mandatory</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mandatory</em>'.
	 * @see eu.cessar.ct.cid.model.Dependency#isMandatory()
	 * @see #getDependency()
	 * @generated
	 */
	EAttribute getDependency_Mandatory();

	/**
	 * Returns the meta object for the containment reference '{@link eu.cessar.ct.cid.model.Dependency#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Version</em>'.
	 * @see eu.cessar.ct.cid.model.Dependency#getVersion()
	 * @see #getDependency()
	 * @generated
	 */
	EReference getDependency_Version();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.cid.model.Artifact <em>Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Artifact</em>'.
	 * @see eu.cessar.ct.cid.model.Artifact
	 * @generated
	 */
	EClass getArtifact();

	/**
	 * Returns the meta object for the attribute '{@link eu.cessar.ct.cid.model.Artifact#getTitle <em>Title</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Title</em>'.
	 * @see eu.cessar.ct.cid.model.Artifact#getTitle()
	 * @see #getArtifact()
	 * @generated
	 */
	EAttribute getArtifact_Title();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ModelFactory getModelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals
	{
		/**
		 * The meta object literal for the '{@link eu.cessar.ct.cid.model.impl.CidImpl <em>Cid</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.cid.model.impl.CidImpl
		 * @see eu.cessar.ct.cid.model.impl.ModelPackageImpl#getCid()
		 * @generated
		 */
		EClass CID = eINSTANCE.getCid();

		/**
		 * The meta object literal for the '<em><b>Deliveries</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CID__DELIVERIES = eINSTANCE.getCid_Deliveries();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.cid.model.impl.DeliveryImpl <em>Delivery</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.cid.model.impl.DeliveryImpl
		 * @see eu.cessar.ct.cid.model.impl.ModelPackageImpl#getDelivery()
		 * @generated
		 */
		EClass DELIVERY = eINSTANCE.getDelivery();

		/**
		 * The meta object literal for the '<em><b>Artifacts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DELIVERY__ARTIFACTS = eINSTANCE.getDelivery_Artifacts();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.cid.model.impl.MetadataImpl <em>Metadata</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.cid.model.impl.MetadataImpl
		 * @see eu.cessar.ct.cid.model.impl.ModelPackageImpl#getMetadata()
		 * @generated
		 */
		EClass METADATA = eINSTANCE.getMetadata();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.cid.model.impl.PropertyImpl <em>Property</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.cid.model.impl.PropertyImpl
		 * @see eu.cessar.ct.cid.model.impl.ModelPackageImpl#getProperty()
		 * @generated
		 */
		EClass PROPERTY = eINSTANCE.getProperty();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROPERTY__VALUE = eINSTANCE.getProperty_Value();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.cid.model.impl.DependencyImpl <em>Dependency</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.cid.model.impl.DependencyImpl
		 * @see eu.cessar.ct.cid.model.impl.ModelPackageImpl#getDependency()
		 * @generated
		 */
		EClass DEPENDENCY = eINSTANCE.getDependency();

		/**
		 * The meta object literal for the '<em><b>Mandatory</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DEPENDENCY__MANDATORY = eINSTANCE.getDependency_Mandatory();

		/**
		 * The meta object literal for the '<em><b>Version</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPENDENCY__VERSION = eINSTANCE.getDependency_Version();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.cid.model.impl.ArtifactImpl <em>Artifact</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.cid.model.impl.ArtifactImpl
		 * @see eu.cessar.ct.cid.model.impl.ModelPackageImpl#getArtifact()
		 * @generated
		 */
		EClass ARTIFACT = eINSTANCE.getArtifact();

		/**
		 * The meta object literal for the '<em><b>Title</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ARTIFACT__TITLE = eINSTANCE.getArtifact_Title();

	}

} //ModelPackage

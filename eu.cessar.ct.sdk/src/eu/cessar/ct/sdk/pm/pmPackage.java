/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.cessar.ct.sdk.pm;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

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
 * @see eu.cessar.ct.sdk.pm.pmFactory
 * @model kind="package"
 * @generated
 */
public interface pmPackage extends EPackage
{
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "pm";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://cessar.eu/PMBase";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "pm";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	pmPackage eINSTANCE = eu.cessar.ct.sdk.pm.impl.pmPackageImpl.init();

	/**
	 * The meta object id for the '{@link eu.cessar.ct.sdk.pm.IPMElement <em>IPM Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.sdk.pm.IPMElement
	 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getIPMElement()
	 * @generated
	 */
	int IPM_ELEMENT = 2;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.sdk.pm.IPresentationModel <em>IPresentation Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.sdk.pm.IPresentationModel
	 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getIPresentationModel()
	 * @generated
	 */
	int IPRESENTATION_MODEL = 0;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.sdk.pm.IPMNamedElement <em>IPM Named Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.sdk.pm.IPMNamedElement
	 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getIPMNamedElement()
	 * @generated
	 */
	int IPM_NAMED_ELEMENT = 3;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.sdk.pm.IPMPackage <em>IPM Package</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.sdk.pm.IPMPackage
	 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getIPMPackage()
	 * @generated
	 */
	int IPM_PACKAGE = 1;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.sdk.pm.IPMContainerParent <em>IPM Container Parent</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.sdk.pm.IPMContainerParent
	 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getIPMContainerParent()
	 * @generated
	 */
	int IPM_CONTAINER_PARENT = 4;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.sdk.pm.IPMModuleConfiguration <em>IPM Module Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.sdk.pm.IPMModuleConfiguration
	 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getIPMModuleConfiguration()
	 * @generated
	 */
	int IPM_MODULE_CONFIGURATION = 5;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.sdk.pm.IPMContainer <em>IPM Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.sdk.pm.IPMContainer
	 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getIPMContainer()
	 * @generated
	 */
	int IPM_CONTAINER = 6;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.sdk.pm.IPMChoiceContainer <em>IPM Choice Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.sdk.pm.IPMChoiceContainer
	 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getIPMChoiceContainer()
	 * @generated
	 */
	int IPM_CHOICE_CONTAINER = 7;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.sdk.pm.IPMInstanceRef <em>IPM Instance Ref</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.sdk.pm.IPMInstanceRef
	 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getIPMInstanceRef()
	 * @generated
	 */
	int IPM_INSTANCE_REF = 8;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.sdk.pm.impl.MissingContainerImpl <em>Missing Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.sdk.pm.impl.MissingContainerImpl
	 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getMissingContainer()
	 * @generated
	 */
	int MISSING_CONTAINER = 9;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.sdk.pm.IEMFProxyObject <em>IEMF Proxy Object</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.sdk.pm.IEMFProxyObject
	 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getIEMFProxyObject()
	 * @generated
	 */
	int IEMF_PROXY_OBJECT = 10;

	/**
	 * The number of structural features of the '<em>IEMF Proxy Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEMF_PROXY_OBJECT_FEATURE_COUNT = 0;

	/**
	 * The number of structural features of the '<em>IPM Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPM_ELEMENT_FEATURE_COUNT = IEMF_PROXY_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>IPresentation Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPRESENTATION_MODEL_FEATURE_COUNT = IPM_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>IPM Named Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPM_NAMED_ELEMENT_FEATURE_COUNT = IPM_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>IPM Package</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPM_PACKAGE_FEATURE_COUNT = IPM_NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>IPM Container Parent</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPM_CONTAINER_PARENT_FEATURE_COUNT = IPM_NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>IPM Module Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPM_MODULE_CONFIGURATION_FEATURE_COUNT = IPM_CONTAINER_PARENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>IPM Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPM_CONTAINER_FEATURE_COUNT = IPM_CONTAINER_PARENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>IPM Choice Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPM_CHOICE_CONTAINER_FEATURE_COUNT = IPM_CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>IPM Instance Ref</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPM_INSTANCE_REF_FEATURE_COUNT = IPM_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Missing Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MISSING_CONTAINER_FEATURE_COUNT = IPM_CONTAINER_FEATURE_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.sdk.pm.IPresentationModel <em>IPresentation Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IPresentation Model</em>'.
	 * @see eu.cessar.ct.sdk.pm.IPresentationModel
	 * @generated
	 */
	EClass getIPresentationModel();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.sdk.pm.IPMPackage <em>IPM Package</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IPM Package</em>'.
	 * @see eu.cessar.ct.sdk.pm.IPMPackage
	 * @generated
	 */
	EClass getIPMPackage();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.sdk.pm.IPMElement <em>IPM Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IPM Element</em>'.
	 * @see eu.cessar.ct.sdk.pm.IPMElement
	 * @generated
	 */
	EClass getIPMElement();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.sdk.pm.IPMNamedElement <em>IPM Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IPM Named Element</em>'.
	 * @see eu.cessar.ct.sdk.pm.IPMNamedElement
	 * @generated
	 */
	EClass getIPMNamedElement();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.sdk.pm.IPMContainerParent <em>IPM Container Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IPM Container Parent</em>'.
	 * @see eu.cessar.ct.sdk.pm.IPMContainerParent
	 * @generated
	 */
	EClass getIPMContainerParent();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.sdk.pm.IPMModuleConfiguration <em>IPM Module Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IPM Module Configuration</em>'.
	 * @see eu.cessar.ct.sdk.pm.IPMModuleConfiguration
	 * @generated
	 */
	EClass getIPMModuleConfiguration();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.sdk.pm.IPMContainer <em>IPM Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IPM Container</em>'.
	 * @see eu.cessar.ct.sdk.pm.IPMContainer
	 * @generated
	 */
	EClass getIPMContainer();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.sdk.pm.IPMChoiceContainer <em>IPM Choice Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IPM Choice Container</em>'.
	 * @see eu.cessar.ct.sdk.pm.IPMChoiceContainer
	 * @generated
	 */
	EClass getIPMChoiceContainer();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.sdk.pm.IPMInstanceRef <em>IPM Instance Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IPM Instance Ref</em>'.
	 * @see eu.cessar.ct.sdk.pm.IPMInstanceRef
	 * @generated
	 */
	EClass getIPMInstanceRef();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.sdk.pm.MissingContainer <em>Missing Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Missing Container</em>'.
	 * @see eu.cessar.ct.sdk.pm.MissingContainer
	 * @generated
	 */
	EClass getMissingContainer();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.sdk.pm.IEMFProxyObject <em>IEMF Proxy Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IEMF Proxy Object</em>'.
	 * @see eu.cessar.ct.sdk.pm.IEMFProxyObject
	 * @model instanceClass="eu.cessar.ct.sdk.pm.IEMFProxyObject"
	 * @generated
	 */
	EClass getIEMFProxyObject();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	pmFactory getpmFactory();

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
		 * The meta object literal for the '{@link eu.cessar.ct.sdk.pm.IPresentationModel <em>IPresentation Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.sdk.pm.IPresentationModel
		 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getIPresentationModel()
		 * @generated
		 */
		EClass IPRESENTATION_MODEL = eINSTANCE.getIPresentationModel();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.sdk.pm.IPMPackage <em>IPM Package</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.sdk.pm.IPMPackage
		 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getIPMPackage()
		 * @generated
		 */
		EClass IPM_PACKAGE = eINSTANCE.getIPMPackage();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.sdk.pm.IPMElement <em>IPM Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.sdk.pm.IPMElement
		 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getIPMElement()
		 * @generated
		 */
		EClass IPM_ELEMENT = eINSTANCE.getIPMElement();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.sdk.pm.IPMNamedElement <em>IPM Named Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.sdk.pm.IPMNamedElement
		 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getIPMNamedElement()
		 * @generated
		 */
		EClass IPM_NAMED_ELEMENT = eINSTANCE.getIPMNamedElement();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.sdk.pm.IPMContainerParent <em>IPM Container Parent</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.sdk.pm.IPMContainerParent
		 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getIPMContainerParent()
		 * @generated
		 */
		EClass IPM_CONTAINER_PARENT = eINSTANCE.getIPMContainerParent();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.sdk.pm.IPMModuleConfiguration <em>IPM Module Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.sdk.pm.IPMModuleConfiguration
		 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getIPMModuleConfiguration()
		 * @generated
		 */
		EClass IPM_MODULE_CONFIGURATION = eINSTANCE.getIPMModuleConfiguration();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.sdk.pm.IPMContainer <em>IPM Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.sdk.pm.IPMContainer
		 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getIPMContainer()
		 * @generated
		 */
		EClass IPM_CONTAINER = eINSTANCE.getIPMContainer();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.sdk.pm.IPMChoiceContainer <em>IPM Choice Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.sdk.pm.IPMChoiceContainer
		 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getIPMChoiceContainer()
		 * @generated
		 */
		EClass IPM_CHOICE_CONTAINER = eINSTANCE.getIPMChoiceContainer();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.sdk.pm.IPMInstanceRef <em>IPM Instance Ref</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.sdk.pm.IPMInstanceRef
		 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getIPMInstanceRef()
		 * @generated
		 */
		EClass IPM_INSTANCE_REF = eINSTANCE.getIPMInstanceRef();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.sdk.pm.impl.MissingContainerImpl <em>Missing Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.sdk.pm.impl.MissingContainerImpl
		 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getMissingContainer()
		 * @generated
		 */
		EClass MISSING_CONTAINER = eINSTANCE.getMissingContainer();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.sdk.pm.IEMFProxyObject <em>IEMF Proxy Object</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.sdk.pm.IEMFProxyObject
		 * @see eu.cessar.ct.sdk.pm.impl.pmPackageImpl#getIEMFProxyObject()
		 * @generated
		 */
		EClass IEMF_PROXY_OBJECT = eINSTANCE.getIEMFProxyObject();

	}

} //pmPackage

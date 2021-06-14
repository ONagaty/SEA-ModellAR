/**
 */
package eu.cessar.ct.cid.model.elements;

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
 * @see eu.cessar.ct.cid.model.elements.ElementsFactory
 * @model kind="package"
 * @generated
 */
public interface ElementsPackage extends EPackage
{
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "elements";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.cessar.eu/cid/elements";

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
	ElementsPackage eINSTANCE = eu.cessar.ct.cid.model.elements.impl.ElementsPackageImpl.init();

	/**
	 * The meta object id for the '{@link eu.cessar.ct.cid.model.elements.impl.NamedElementImpl <em>Named Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.cid.model.elements.impl.NamedElementImpl
	 * @see eu.cessar.ct.cid.model.elements.impl.ElementsPackageImpl#getNamedElement()
	 * @generated
	 */
	int NAMED_ELEMENT = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT__NAME = 0;

	/**
	 * The number of structural features of the '<em>Named Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.cid.model.elements.impl.TypedElementImpl <em>Typed Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.cid.model.elements.impl.TypedElementImpl
	 * @see eu.cessar.ct.cid.model.elements.impl.ElementsPackageImpl#getTypedElement()
	 * @generated
	 */
	int TYPED_ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT__TYPE = 0;

	/**
	 * The number of structural features of the '<em>Typed Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.cid.model.elements.impl.DependantElementImpl <em>Dependant Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.cid.model.elements.impl.DependantElementImpl
	 * @see eu.cessar.ct.cid.model.elements.impl.ElementsPackageImpl#getDependantElement()
	 * @generated
	 */
	int DEPENDANT_ELEMENT = 2;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDANT_ELEMENT__METADATA = 0;

	/**
	 * The feature id for the '<em><b>Dependencies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDANT_ELEMENT__DEPENDENCIES = 1;

	/**
	 * The number of structural features of the '<em>Dependant Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDANT_ELEMENT_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.cid.model.elements.impl.PropertiesElementImpl <em>Properties Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.cid.model.elements.impl.PropertiesElementImpl
	 * @see eu.cessar.ct.cid.model.elements.impl.ElementsPackageImpl#getPropertiesElement()
	 * @generated
	 */
	int PROPERTIES_ELEMENT = 3;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTIES_ELEMENT__PROPERTIES = 0;

	/**
	 * The number of structural features of the '<em>Properties Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTIES_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.cid.model.elements.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Element</em>'.
	 * @see eu.cessar.ct.cid.model.elements.NamedElement
	 * @generated
	 */
	EClass getNamedElement();

	/**
	 * Returns the meta object for the attribute '{@link eu.cessar.ct.cid.model.elements.NamedElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see eu.cessar.ct.cid.model.elements.NamedElement#getName()
	 * @see #getNamedElement()
	 * @generated
	 */
	EAttribute getNamedElement_Name();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.cid.model.elements.TypedElement <em>Typed Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Typed Element</em>'.
	 * @see eu.cessar.ct.cid.model.elements.TypedElement
	 * @generated
	 */
	EClass getTypedElement();

	/**
	 * Returns the meta object for the attribute '{@link eu.cessar.ct.cid.model.elements.TypedElement#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see eu.cessar.ct.cid.model.elements.TypedElement#getType()
	 * @see #getTypedElement()
	 * @generated
	 */
	EAttribute getTypedElement_Type();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.cid.model.elements.DependantElement <em>Dependant Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Dependant Element</em>'.
	 * @see eu.cessar.ct.cid.model.elements.DependantElement
	 * @generated
	 */
	EClass getDependantElement();

	/**
	 * Returns the meta object for the containment reference '{@link eu.cessar.ct.cid.model.elements.DependantElement#getMetadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Metadata</em>'.
	 * @see eu.cessar.ct.cid.model.elements.DependantElement#getMetadata()
	 * @see #getDependantElement()
	 * @generated
	 */
	EReference getDependantElement_Metadata();

	/**
	 * Returns the meta object for the containment reference list '{@link eu.cessar.ct.cid.model.elements.DependantElement#getDependencies <em>Dependencies</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Dependencies</em>'.
	 * @see eu.cessar.ct.cid.model.elements.DependantElement#getDependencies()
	 * @see #getDependantElement()
	 * @generated
	 */
	EReference getDependantElement_Dependencies();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.cid.model.elements.PropertiesElement <em>Properties Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Properties Element</em>'.
	 * @see eu.cessar.ct.cid.model.elements.PropertiesElement
	 * @generated
	 */
	EClass getPropertiesElement();

	/**
	 * Returns the meta object for the containment reference list '{@link eu.cessar.ct.cid.model.elements.PropertiesElement#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Properties</em>'.
	 * @see eu.cessar.ct.cid.model.elements.PropertiesElement#getProperties()
	 * @see #getPropertiesElement()
	 * @generated
	 */
	EReference getPropertiesElement_Properties();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ElementsFactory getElementsFactory();

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
		 * The meta object literal for the '{@link eu.cessar.ct.cid.model.elements.impl.NamedElementImpl <em>Named Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.cid.model.elements.impl.NamedElementImpl
		 * @see eu.cessar.ct.cid.model.elements.impl.ElementsPackageImpl#getNamedElement()
		 * @generated
		 */
		EClass NAMED_ELEMENT = eINSTANCE.getNamedElement();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMED_ELEMENT__NAME = eINSTANCE.getNamedElement_Name();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.cid.model.elements.impl.TypedElementImpl <em>Typed Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.cid.model.elements.impl.TypedElementImpl
		 * @see eu.cessar.ct.cid.model.elements.impl.ElementsPackageImpl#getTypedElement()
		 * @generated
		 */
		EClass TYPED_ELEMENT = eINSTANCE.getTypedElement();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TYPED_ELEMENT__TYPE = eINSTANCE.getTypedElement_Type();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.cid.model.elements.impl.DependantElementImpl <em>Dependant Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.cid.model.elements.impl.DependantElementImpl
		 * @see eu.cessar.ct.cid.model.elements.impl.ElementsPackageImpl#getDependantElement()
		 * @generated
		 */
		EClass DEPENDANT_ELEMENT = eINSTANCE.getDependantElement();

		/**
		 * The meta object literal for the '<em><b>Metadata</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPENDANT_ELEMENT__METADATA = eINSTANCE.getDependantElement_Metadata();

		/**
		 * The meta object literal for the '<em><b>Dependencies</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPENDANT_ELEMENT__DEPENDENCIES = eINSTANCE.getDependantElement_Dependencies();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.cid.model.elements.impl.PropertiesElementImpl <em>Properties Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.cid.model.elements.impl.PropertiesElementImpl
		 * @see eu.cessar.ct.cid.model.elements.impl.ElementsPackageImpl#getPropertiesElement()
		 * @generated
		 */
		EClass PROPERTIES_ELEMENT = eINSTANCE.getPropertiesElement();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROPERTIES_ELEMENT__PROPERTIES = eINSTANCE.getPropertiesElement_Properties();

	}

} //ElementsPackage

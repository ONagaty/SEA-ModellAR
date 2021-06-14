/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.cessar.ct.workspace.internal.modeldocumentation;

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
 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationFactory
 * @model kind="package"
 * @generated
 */
public interface ModeldocumentationPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "modeldocumentation";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.cessar.eu.ModelDocumentation";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "modeldocumentation";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ModeldocumentationPackage eINSTANCE = eu.cessar.ct.workspace.internal.modeldocumentation.impl.ModeldocumentationPackageImpl.init();

	/**
	 * The meta object id for the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.DocElementImpl <em>Doc Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.impl.DocElementImpl
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.impl.ModeldocumentationPackageImpl#getDocElement()
	 * @generated
	 */
	int DOC_ELEMENT = 0;

	/**
	 * The feature id for the '<em><b>Instance Of</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOC_ELEMENT__INSTANCE_OF = 0;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOC_ELEMENT__ATTRIBUTES = 1;

	/**
	 * The feature id for the '<em><b>Contains</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOC_ELEMENT__CONTAINS = 2;

	/**
	 * The feature id for the '<em><b>Short Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOC_ELEMENT__SHORT_NAME = 3;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOC_ELEMENT__TYPE = 4;

	/**
	 * The feature id for the '<em><b>Is Instance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOC_ELEMENT__IS_INSTANCE = 5;

	/**
	 * The number of structural features of the '<em>Doc Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOC_ELEMENT_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.DefElementImpl <em>Def Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.impl.DefElementImpl
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.impl.ModeldocumentationPackageImpl#getDefElement()
	 * @generated
	 */
	int DEF_ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Multiplicity</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEF_ELEMENT__MULTIPLICITY = 0;

	/**
	 * The feature id for the '<em><b>Short Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEF_ELEMENT__SHORT_NAME = 1;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEF_ELEMENT__DESCRIPTION = 2;

	/**
	 * The number of structural features of the '<em>Def Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEF_ELEMENT_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.MultiplicityImpl <em>Multiplicity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.impl.MultiplicityImpl
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.impl.ModeldocumentationPackageImpl#getMultiplicity()
	 * @generated
	 */
	int MULTIPLICITY = 2;

	/**
	 * The feature id for the '<em><b>Lower</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULTIPLICITY__LOWER = 0;

	/**
	 * The feature id for the '<em><b>Upper</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULTIPLICITY__UPPER = 1;

	/**
	 * The feature id for the '<em><b>Multiplicity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULTIPLICITY__MULTIPLICITY = 2;

	/**
	 * The number of structural features of the '<em>Multiplicity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULTIPLICITY_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.AttributeImpl <em>Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.impl.AttributeImpl
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.impl.ModeldocumentationPackageImpl#getAttribute()
	 * @generated
	 */
	int ATTRIBUTE = 3;

	/**
	 * The feature id for the '<em><b>Instance Of</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__INSTANCE_OF = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__NAME = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__TYPE = 2;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__VALUE = 3;

	/**
	 * The number of structural features of the '<em>Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.AttributeDefImpl <em>Attribute Def</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.impl.AttributeDefImpl
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.impl.ModeldocumentationPackageImpl#getAttributeDef()
	 * @generated
	 */
	int ATTRIBUTE_DEF = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_DEF__NAME = 0;

	/**
	 * The feature id for the '<em><b>Type Of Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_DEF__TYPE_OF_VALUE = 1;

	/**
	 * The feature id for the '<em><b>Multiplicity</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_DEF__MULTIPLICITY = 2;

	/**
	 * The number of structural features of the '<em>Attribute Def</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_DEF_FEATURE_COUNT = 3;


	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.workspace.internal.modeldocumentation.DocElement <em>Doc Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Doc Element</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.DocElement
	 * @generated
	 */
	EClass getDocElement();

	/**
	 * Returns the meta object for the containment reference '{@link eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#getInstanceOf <em>Instance Of</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Instance Of</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#getInstanceOf()
	 * @see #getDocElement()
	 * @generated
	 */
	EReference getDocElement_InstanceOf();

	/**
	 * Returns the meta object for the containment reference list '{@link eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#getAttributes <em>Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attributes</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#getAttributes()
	 * @see #getDocElement()
	 * @generated
	 */
	EReference getDocElement_Attributes();

	/**
	 * Returns the meta object for the containment reference list '{@link eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#getContains <em>Contains</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Contains</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#getContains()
	 * @see #getDocElement()
	 * @generated
	 */
	EReference getDocElement_Contains();

	/**
	 * Returns the meta object for the attribute '{@link eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#getShortName <em>Short Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Short Name</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#getShortName()
	 * @see #getDocElement()
	 * @generated
	 */
	EAttribute getDocElement_ShortName();

	/**
	 * Returns the meta object for the attribute '{@link eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#getType()
	 * @see #getDocElement()
	 * @generated
	 */
	EAttribute getDocElement_Type();

	/**
	 * Returns the meta object for the attribute '{@link eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#isIsInstance <em>Is Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Instance</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.DocElement#isIsInstance()
	 * @see #getDocElement()
	 * @generated
	 */
	EAttribute getDocElement_IsInstance();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.workspace.internal.modeldocumentation.DefElement <em>Def Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Def Element</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.DefElement
	 * @generated
	 */
	EClass getDefElement();

	/**
	 * Returns the meta object for the containment reference '{@link eu.cessar.ct.workspace.internal.modeldocumentation.DefElement#getMultiplicity <em>Multiplicity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Multiplicity</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.DefElement#getMultiplicity()
	 * @see #getDefElement()
	 * @generated
	 */
	EReference getDefElement_Multiplicity();

	/**
	 * Returns the meta object for the attribute '{@link eu.cessar.ct.workspace.internal.modeldocumentation.DefElement#getShortName <em>Short Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Short Name</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.DefElement#getShortName()
	 * @see #getDefElement()
	 * @generated
	 */
	EAttribute getDefElement_ShortName();

	/**
	 * Returns the meta object for the attribute '{@link eu.cessar.ct.workspace.internal.modeldocumentation.DefElement#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.DefElement#getDescription()
	 * @see #getDefElement()
	 * @generated
	 */
	EAttribute getDefElement_Description();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.workspace.internal.modeldocumentation.Multiplicity <em>Multiplicity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Multiplicity</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.Multiplicity
	 * @generated
	 */
	EClass getMultiplicity();

	/**
	 * Returns the meta object for the attribute '{@link eu.cessar.ct.workspace.internal.modeldocumentation.Multiplicity#getLower <em>Lower</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lower</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.Multiplicity#getLower()
	 * @see #getMultiplicity()
	 * @generated
	 */
	EAttribute getMultiplicity_Lower();

	/**
	 * Returns the meta object for the attribute '{@link eu.cessar.ct.workspace.internal.modeldocumentation.Multiplicity#getUpper <em>Upper</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Upper</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.Multiplicity#getUpper()
	 * @see #getMultiplicity()
	 * @generated
	 */
	EAttribute getMultiplicity_Upper();

	/**
	 * Returns the meta object for the reference '{@link eu.cessar.ct.workspace.internal.modeldocumentation.Multiplicity#getMultiplicity <em>Multiplicity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Multiplicity</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.Multiplicity#getMultiplicity()
	 * @see #getMultiplicity()
	 * @generated
	 */
	EReference getMultiplicity_Multiplicity();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.workspace.internal.modeldocumentation.Attribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.Attribute
	 * @generated
	 */
	EClass getAttribute();

	/**
	 * Returns the meta object for the containment reference '{@link eu.cessar.ct.workspace.internal.modeldocumentation.Attribute#getInstanceOf <em>Instance Of</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Instance Of</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.Attribute#getInstanceOf()
	 * @see #getAttribute()
	 * @generated
	 */
	EReference getAttribute_InstanceOf();

	/**
	 * Returns the meta object for the attribute '{@link eu.cessar.ct.workspace.internal.modeldocumentation.Attribute#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.Attribute#getName()
	 * @see #getAttribute()
	 * @generated
	 */
	EAttribute getAttribute_Name();

	/**
	 * Returns the meta object for the attribute '{@link eu.cessar.ct.workspace.internal.modeldocumentation.Attribute#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.Attribute#getType()
	 * @see #getAttribute()
	 * @generated
	 */
	EAttribute getAttribute_Type();

	/**
	 * Returns the meta object for the attribute '{@link eu.cessar.ct.workspace.internal.modeldocumentation.Attribute#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.Attribute#getValue()
	 * @see #getAttribute()
	 * @generated
	 */
	EAttribute getAttribute_Value();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.workspace.internal.modeldocumentation.AttributeDef <em>Attribute Def</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute Def</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.AttributeDef
	 * @generated
	 */
	EClass getAttributeDef();

	/**
	 * Returns the meta object for the attribute '{@link eu.cessar.ct.workspace.internal.modeldocumentation.AttributeDef#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.AttributeDef#getName()
	 * @see #getAttributeDef()
	 * @generated
	 */
	EAttribute getAttributeDef_Name();

	/**
	 * Returns the meta object for the attribute '{@link eu.cessar.ct.workspace.internal.modeldocumentation.AttributeDef#getTypeOfValue <em>Type Of Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type Of Value</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.AttributeDef#getTypeOfValue()
	 * @see #getAttributeDef()
	 * @generated
	 */
	EAttribute getAttributeDef_TypeOfValue();

	/**
	 * Returns the meta object for the containment reference '{@link eu.cessar.ct.workspace.internal.modeldocumentation.AttributeDef#getMultiplicity <em>Multiplicity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Multiplicity</em>'.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.AttributeDef#getMultiplicity()
	 * @see #getAttributeDef()
	 * @generated
	 */
	EReference getAttributeDef_Multiplicity();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ModeldocumentationFactory getModeldocumentationFactory();

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
	interface Literals {
		/**
		 * The meta object literal for the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.DocElementImpl <em>Doc Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.workspace.internal.modeldocumentation.impl.DocElementImpl
		 * @see eu.cessar.ct.workspace.internal.modeldocumentation.impl.ModeldocumentationPackageImpl#getDocElement()
		 * @generated
		 */
		EClass DOC_ELEMENT = eINSTANCE.getDocElement();

		/**
		 * The meta object literal for the '<em><b>Instance Of</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOC_ELEMENT__INSTANCE_OF = eINSTANCE.getDocElement_InstanceOf();

		/**
		 * The meta object literal for the '<em><b>Attributes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOC_ELEMENT__ATTRIBUTES = eINSTANCE.getDocElement_Attributes();

		/**
		 * The meta object literal for the '<em><b>Contains</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOC_ELEMENT__CONTAINS = eINSTANCE.getDocElement_Contains();

		/**
		 * The meta object literal for the '<em><b>Short Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOC_ELEMENT__SHORT_NAME = eINSTANCE.getDocElement_ShortName();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOC_ELEMENT__TYPE = eINSTANCE.getDocElement_Type();

		/**
		 * The meta object literal for the '<em><b>Is Instance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOC_ELEMENT__IS_INSTANCE = eINSTANCE.getDocElement_IsInstance();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.DefElementImpl <em>Def Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.workspace.internal.modeldocumentation.impl.DefElementImpl
		 * @see eu.cessar.ct.workspace.internal.modeldocumentation.impl.ModeldocumentationPackageImpl#getDefElement()
		 * @generated
		 */
		EClass DEF_ELEMENT = eINSTANCE.getDefElement();

		/**
		 * The meta object literal for the '<em><b>Multiplicity</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEF_ELEMENT__MULTIPLICITY = eINSTANCE.getDefElement_Multiplicity();

		/**
		 * The meta object literal for the '<em><b>Short Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DEF_ELEMENT__SHORT_NAME = eINSTANCE.getDefElement_ShortName();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DEF_ELEMENT__DESCRIPTION = eINSTANCE.getDefElement_Description();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.MultiplicityImpl <em>Multiplicity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.workspace.internal.modeldocumentation.impl.MultiplicityImpl
		 * @see eu.cessar.ct.workspace.internal.modeldocumentation.impl.ModeldocumentationPackageImpl#getMultiplicity()
		 * @generated
		 */
		EClass MULTIPLICITY = eINSTANCE.getMultiplicity();

		/**
		 * The meta object literal for the '<em><b>Lower</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MULTIPLICITY__LOWER = eINSTANCE.getMultiplicity_Lower();

		/**
		 * The meta object literal for the '<em><b>Upper</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MULTIPLICITY__UPPER = eINSTANCE.getMultiplicity_Upper();

		/**
		 * The meta object literal for the '<em><b>Multiplicity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MULTIPLICITY__MULTIPLICITY = eINSTANCE.getMultiplicity_Multiplicity();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.AttributeImpl <em>Attribute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.workspace.internal.modeldocumentation.impl.AttributeImpl
		 * @see eu.cessar.ct.workspace.internal.modeldocumentation.impl.ModeldocumentationPackageImpl#getAttribute()
		 * @generated
		 */
		EClass ATTRIBUTE = eINSTANCE.getAttribute();

		/**
		 * The meta object literal for the '<em><b>Instance Of</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ATTRIBUTE__INSTANCE_OF = eINSTANCE.getAttribute_InstanceOf();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE__NAME = eINSTANCE.getAttribute_Name();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE__TYPE = eINSTANCE.getAttribute_Type();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE__VALUE = eINSTANCE.getAttribute_Value();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.AttributeDefImpl <em>Attribute Def</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.workspace.internal.modeldocumentation.impl.AttributeDefImpl
		 * @see eu.cessar.ct.workspace.internal.modeldocumentation.impl.ModeldocumentationPackageImpl#getAttributeDef()
		 * @generated
		 */
		EClass ATTRIBUTE_DEF = eINSTANCE.getAttributeDef();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE_DEF__NAME = eINSTANCE.getAttributeDef_Name();

		/**
		 * The meta object literal for the '<em><b>Type Of Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE_DEF__TYPE_OF_VALUE = eINSTANCE.getAttributeDef_TypeOfValue();

		/**
		 * The meta object literal for the '<em><b>Multiplicity</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ATTRIBUTE_DEF__MULTIPLICITY = eINSTANCE.getAttributeDef_Multiplicity();

	}

} //ModeldocumentationPackage

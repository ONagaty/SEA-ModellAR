/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.cessar.ct.workspace.internal.modeldocumentation.impl;

import eu.cessar.ct.workspace.internal.modeldocumentation.Attribute;
import eu.cessar.ct.workspace.internal.modeldocumentation.AttributeDef;
import eu.cessar.ct.workspace.internal.modeldocumentation.DefElement;
import eu.cessar.ct.workspace.internal.modeldocumentation.DocElement;
import eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationFactory;
import eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage;
import eu.cessar.ct.workspace.internal.modeldocumentation.Multiplicity;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ModeldocumentationPackageImpl extends EPackageImpl implements ModeldocumentationPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass docElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass defElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass multiplicityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass attributeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass attributeDefEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ModeldocumentationPackageImpl() {
		super(eNS_URI, ModeldocumentationFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link ModeldocumentationPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ModeldocumentationPackage init() {
		if (isInited) return (ModeldocumentationPackage)EPackage.Registry.INSTANCE.getEPackage(ModeldocumentationPackage.eNS_URI);

		// Obtain or create and register package
		ModeldocumentationPackageImpl theModeldocumentationPackage = (ModeldocumentationPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ModeldocumentationPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ModeldocumentationPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theModeldocumentationPackage.createPackageContents();

		// Initialize created meta-data
		theModeldocumentationPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theModeldocumentationPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ModeldocumentationPackage.eNS_URI, theModeldocumentationPackage);
		return theModeldocumentationPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDocElement() {
		return docElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocElement_InstanceOf() {
		return (EReference)docElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocElement_Attributes() {
		return (EReference)docElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocElement_Contains() {
		return (EReference)docElementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocElement_ShortName() {
		return (EAttribute)docElementEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocElement_Type() {
		return (EAttribute)docElementEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocElement_IsInstance() {
		return (EAttribute)docElementEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDefElement() {
		return defElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDefElement_Multiplicity() {
		return (EReference)defElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDefElement_ShortName() {
		return (EAttribute)defElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDefElement_Description() {
		return (EAttribute)defElementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMultiplicity() {
		return multiplicityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMultiplicity_Lower() {
		return (EAttribute)multiplicityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMultiplicity_Upper() {
		return (EAttribute)multiplicityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMultiplicity_Multiplicity() {
		return (EReference)multiplicityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAttribute() {
		return attributeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAttribute_InstanceOf() {
		return (EReference)attributeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAttribute_Name() {
		return (EAttribute)attributeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAttribute_Type() {
		return (EAttribute)attributeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAttribute_Value() {
		return (EAttribute)attributeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAttributeDef() {
		return attributeDefEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAttributeDef_Name() {
		return (EAttribute)attributeDefEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAttributeDef_TypeOfValue() {
		return (EAttribute)attributeDefEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAttributeDef_Multiplicity() {
		return (EReference)attributeDefEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModeldocumentationFactory getModeldocumentationFactory() {
		return (ModeldocumentationFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		docElementEClass = createEClass(DOC_ELEMENT);
		createEReference(docElementEClass, DOC_ELEMENT__INSTANCE_OF);
		createEReference(docElementEClass, DOC_ELEMENT__ATTRIBUTES);
		createEReference(docElementEClass, DOC_ELEMENT__CONTAINS);
		createEAttribute(docElementEClass, DOC_ELEMENT__SHORT_NAME);
		createEAttribute(docElementEClass, DOC_ELEMENT__TYPE);
		createEAttribute(docElementEClass, DOC_ELEMENT__IS_INSTANCE);

		defElementEClass = createEClass(DEF_ELEMENT);
		createEReference(defElementEClass, DEF_ELEMENT__MULTIPLICITY);
		createEAttribute(defElementEClass, DEF_ELEMENT__SHORT_NAME);
		createEAttribute(defElementEClass, DEF_ELEMENT__DESCRIPTION);

		multiplicityEClass = createEClass(MULTIPLICITY);
		createEAttribute(multiplicityEClass, MULTIPLICITY__LOWER);
		createEAttribute(multiplicityEClass, MULTIPLICITY__UPPER);
		createEReference(multiplicityEClass, MULTIPLICITY__MULTIPLICITY);

		attributeEClass = createEClass(ATTRIBUTE);
		createEReference(attributeEClass, ATTRIBUTE__INSTANCE_OF);
		createEAttribute(attributeEClass, ATTRIBUTE__NAME);
		createEAttribute(attributeEClass, ATTRIBUTE__TYPE);
		createEAttribute(attributeEClass, ATTRIBUTE__VALUE);

		attributeDefEClass = createEClass(ATTRIBUTE_DEF);
		createEAttribute(attributeDefEClass, ATTRIBUTE_DEF__NAME);
		createEAttribute(attributeDefEClass, ATTRIBUTE_DEF__TYPE_OF_VALUE);
		createEReference(attributeDefEClass, ATTRIBUTE_DEF__MULTIPLICITY);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(docElementEClass, DocElement.class, "DocElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDocElement_InstanceOf(), this.getDefElement(), null, "instanceOf", null, 1, 1, DocElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocElement_Attributes(), this.getAttribute(), null, "attributes", null, 0, -1, DocElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocElement_Contains(), this.getDocElement(), null, "contains", null, 0, -1, DocElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocElement_ShortName(), ecorePackage.getEString(), "shortName", null, 1, 1, DocElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocElement_Type(), ecorePackage.getEString(), "type", null, 1, 1, DocElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocElement_IsInstance(), ecorePackage.getEBoolean(), "isInstance", null, 1, 1, DocElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(defElementEClass, DefElement.class, "DefElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDefElement_Multiplicity(), this.getMultiplicity(), null, "multiplicity", null, 1, 1, DefElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDefElement_ShortName(), ecorePackage.getEString(), "shortName", null, 1, 1, DefElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDefElement_Description(), ecorePackage.getEString(), "description", null, 1, 1, DefElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(multiplicityEClass, Multiplicity.class, "Multiplicity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMultiplicity_Lower(), ecorePackage.getEString(), "lower", null, 1, 1, Multiplicity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMultiplicity_Upper(), ecorePackage.getEString(), "upper", null, 1, 1, Multiplicity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMultiplicity_Multiplicity(), this.getAttributeDef(), null, "multiplicity", null, 0, 1, Multiplicity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(attributeEClass, Attribute.class, "Attribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAttribute_InstanceOf(), this.getAttributeDef(), null, "instanceOf", null, 1, 1, Attribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAttribute_Name(), ecorePackage.getEString(), "name", null, 1, 1, Attribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAttribute_Type(), ecorePackage.getEString(), "type", null, 1, 1, Attribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAttribute_Value(), ecorePackage.getEString(), "value", null, 0, 1, Attribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(attributeDefEClass, AttributeDef.class, "AttributeDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAttributeDef_Name(), ecorePackage.getEString(), "name", null, 1, 1, AttributeDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAttributeDef_TypeOfValue(), ecorePackage.getEString(), "typeOfValue", null, 1, 1, AttributeDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAttributeDef_Multiplicity(), this.getMultiplicity(), null, "multiplicity", null, 1, 1, AttributeDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";		
		addAnnotation
		  (getDocElement_ShortName(), 
		   source, 
		   new String[] {
			 "kind", "attribute"
		   });		
		addAnnotation
		  (getDocElement_Type(), 
		   source, 
		   new String[] {
			 "kind", "attribute"
		   });		
		addAnnotation
		  (getDocElement_IsInstance(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "namespace", ""
		   });
	}

} //ModeldocumentationPackageImpl

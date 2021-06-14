/**
 */
package eu.cessar.ct.cid.model.elements.impl;

import eu.cessar.ct.cid.model.ModelPackage;

import eu.cessar.ct.cid.model.datatypes.DatatypesPackage;

import eu.cessar.ct.cid.model.datatypes.impl.DatatypesPackageImpl;

import eu.cessar.ct.cid.model.elements.DependantElement;
import eu.cessar.ct.cid.model.elements.ElementsFactory;
import eu.cessar.ct.cid.model.elements.ElementsPackage;
import eu.cessar.ct.cid.model.elements.NamedElement;
import eu.cessar.ct.cid.model.elements.PropertiesElement;
import eu.cessar.ct.cid.model.elements.TypedElement;

import eu.cessar.ct.cid.model.impl.ModelPackageImpl;

import eu.cessar.ct.cid.model.versioning.VersioningPackage;

import eu.cessar.ct.cid.model.versioning.impl.VersioningPackageImpl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ElementsPackageImpl extends EPackageImpl implements ElementsPackage
{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass namedElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass typedElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dependantElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass propertiesElementEClass = null;

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
	 * @see eu.cessar.ct.cid.model.elements.ElementsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ElementsPackageImpl()
	{
		super(eNS_URI, ElementsFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link ElementsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ElementsPackage init()
	{
		if (isInited)
			return (ElementsPackage) EPackage.Registry.INSTANCE.getEPackage(ElementsPackage.eNS_URI);

		// Obtain or create and register package
		ElementsPackageImpl theElementsPackage = (ElementsPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ElementsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
			: new ElementsPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		ModelPackageImpl theModelPackage = (ModelPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(ModelPackage.eNS_URI) instanceof ModelPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ModelPackage.eNS_URI)
			: ModelPackage.eINSTANCE);
		VersioningPackageImpl theVersioningPackage = (VersioningPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(VersioningPackage.eNS_URI) instanceof VersioningPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(VersioningPackage.eNS_URI)
			: VersioningPackage.eINSTANCE);
		DatatypesPackageImpl theDatatypesPackage = (DatatypesPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(DatatypesPackage.eNS_URI) instanceof DatatypesPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DatatypesPackage.eNS_URI)
			: DatatypesPackage.eINSTANCE);

		// Create package meta-data objects
		theElementsPackage.createPackageContents();
		theModelPackage.createPackageContents();
		theVersioningPackage.createPackageContents();
		theDatatypesPackage.createPackageContents();

		// Initialize created meta-data
		theElementsPackage.initializePackageContents();
		theModelPackage.initializePackageContents();
		theVersioningPackage.initializePackageContents();
		theDatatypesPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theElementsPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ElementsPackage.eNS_URI, theElementsPackage);
		return theElementsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNamedElement()
	{
		return namedElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNamedElement_Name()
	{
		return (EAttribute) namedElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTypedElement()
	{
		return typedElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTypedElement_Type()
	{
		return (EAttribute) typedElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDependantElement()
	{
		return dependantElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDependantElement_Metadata()
	{
		return (EReference) dependantElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDependantElement_Dependencies()
	{
		return (EReference) dependantElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPropertiesElement()
	{
		return propertiesElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPropertiesElement_Properties()
	{
		return (EReference) propertiesElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ElementsFactory getElementsFactory()
	{
		return (ElementsFactory) getEFactoryInstance();
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
	public void createPackageContents()
	{
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		namedElementEClass = createEClass(NAMED_ELEMENT);
		createEAttribute(namedElementEClass, NAMED_ELEMENT__NAME);

		typedElementEClass = createEClass(TYPED_ELEMENT);
		createEAttribute(typedElementEClass, TYPED_ELEMENT__TYPE);

		dependantElementEClass = createEClass(DEPENDANT_ELEMENT);
		createEReference(dependantElementEClass, DEPENDANT_ELEMENT__METADATA);
		createEReference(dependantElementEClass, DEPENDANT_ELEMENT__DEPENDENCIES);

		propertiesElementEClass = createEClass(PROPERTIES_ELEMENT);
		createEReference(propertiesElementEClass, PROPERTIES_ELEMENT__PROPERTIES);
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
	public void initializePackageContents()
	{
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		DatatypesPackage theDatatypesPackage = (DatatypesPackage) EPackage.Registry.INSTANCE.getEPackage(DatatypesPackage.eNS_URI);
		ModelPackage theModelPackage = (ModelPackage) EPackage.Registry.INSTANCE.getEPackage(ModelPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(namedElementEClass, NamedElement.class, "NamedElement", IS_ABSTRACT, !IS_INTERFACE,
			IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNamedElement_Name(), theDatatypesPackage.getIdentifier(), "name", null, 0, 1,
			NamedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);

		initEClass(typedElementEClass, TypedElement.class, "TypedElement", IS_ABSTRACT, !IS_INTERFACE,
			IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTypedElement_Type(), theDatatypesPackage.getIdentifier(), "type", null, 0, 1,
			TypedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);

		initEClass(dependantElementEClass, DependantElement.class, "DependantElement", IS_ABSTRACT, !IS_INTERFACE,
			IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDependantElement_Metadata(), theModelPackage.getMetadata(), null, "metadata", null, 0, 1,
			DependantElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDependantElement_Dependencies(), theModelPackage.getDependency(), null, "dependencies", null,
			0, -1, DependantElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
			!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(dependantElementEClass, theDatatypesPackage.getIStatus(), "evaluateDependencies", 0, 1,
			IS_UNIQUE, IS_ORDERED);

		EOperation op = addEOperation(dependantElementEClass, theDatatypesPackage.getDependencyList(),
			"getDependencies", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "type", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(propertiesElementEClass, PropertiesElement.class, "PropertiesElement", IS_ABSTRACT, !IS_INTERFACE,
			IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPropertiesElement_Properties(), theModelPackage.getProperty(), null, "properties", null, 0,
			-1, PropertiesElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(propertiesElementEClass, theDatatypesPackage.getPropertyList(), "getProperties", 0, 1,
			IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED);

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
	protected void createExtendedMetaDataAnnotations()
	{
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";
		addAnnotation(getDependantElement_Dependencies(), source, new String[] {"kind", "element", "name",
			"dependency", "namespace", "http://www.cessar.eu/cid"});
		addAnnotation(getPropertiesElement_Properties(), source, new String[] {"kind", "element", "name", "property",
			"namespace", "http://www.cessar.eu/cid"});
	}

} //ElementsPackageImpl

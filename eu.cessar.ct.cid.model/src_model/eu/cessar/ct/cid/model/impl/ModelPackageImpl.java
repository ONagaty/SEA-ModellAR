/**
 */
package eu.cessar.ct.cid.model.impl;

import eu.cessar.ct.cid.model.Artifact;
import eu.cessar.ct.cid.model.Cid;
import eu.cessar.ct.cid.model.Delivery;
import eu.cessar.ct.cid.model.Dependency;
import eu.cessar.ct.cid.model.Metadata;
import eu.cessar.ct.cid.model.ModelFactory;
import eu.cessar.ct.cid.model.ModelPackage;
import eu.cessar.ct.cid.model.Property;

import eu.cessar.ct.cid.model.datatypes.DatatypesPackage;

import eu.cessar.ct.cid.model.datatypes.impl.DatatypesPackageImpl;

import eu.cessar.ct.cid.model.elements.ElementsPackage;

import eu.cessar.ct.cid.model.elements.impl.ElementsPackageImpl;

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
public class ModelPackageImpl extends EPackageImpl implements ModelPackage
{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cidEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass deliveryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass metadataEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass propertyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dependencyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass artifactEClass = null;

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
	 * @see eu.cessar.ct.cid.model.ModelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ModelPackageImpl()
	{
		super(eNS_URI, ModelFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link ModelPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ModelPackage init()
	{
		if (isInited)
			return (ModelPackage) EPackage.Registry.INSTANCE.getEPackage(ModelPackage.eNS_URI);

		// Obtain or create and register package
		ModelPackageImpl theModelPackage = (ModelPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ModelPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
			: new ModelPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		VersioningPackageImpl theVersioningPackage = (VersioningPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(VersioningPackage.eNS_URI) instanceof VersioningPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(VersioningPackage.eNS_URI)
			: VersioningPackage.eINSTANCE);
		DatatypesPackageImpl theDatatypesPackage = (DatatypesPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(DatatypesPackage.eNS_URI) instanceof DatatypesPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DatatypesPackage.eNS_URI)
			: DatatypesPackage.eINSTANCE);
		ElementsPackageImpl theElementsPackage = (ElementsPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(ElementsPackage.eNS_URI) instanceof ElementsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ElementsPackage.eNS_URI)
			: ElementsPackage.eINSTANCE);

		// Create package meta-data objects
		theModelPackage.createPackageContents();
		theVersioningPackage.createPackageContents();
		theDatatypesPackage.createPackageContents();
		theElementsPackage.createPackageContents();

		// Initialize created meta-data
		theModelPackage.initializePackageContents();
		theVersioningPackage.initializePackageContents();
		theDatatypesPackage.initializePackageContents();
		theElementsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theModelPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, theModelPackage);
		return theModelPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCid()
	{
		return cidEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCid_Deliveries()
	{
		return (EReference) cidEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDelivery()
	{
		return deliveryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDelivery_Artifacts()
	{
		return (EReference) deliveryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMetadata()
	{
		return metadataEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getProperty()
	{
		return propertyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProperty_Value()
	{
		return (EAttribute) propertyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDependency()
	{
		return dependencyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDependency_Mandatory()
	{
		return (EAttribute) dependencyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDependency_Version()
	{
		return (EReference) dependencyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getArtifact()
	{
		return artifactEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getArtifact_Title()
	{
		return (EAttribute) artifactEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelFactory getModelFactory()
	{
		return (ModelFactory) getEFactoryInstance();
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
		cidEClass = createEClass(CID);
		createEReference(cidEClass, CID__DELIVERIES);

		deliveryEClass = createEClass(DELIVERY);
		createEReference(deliveryEClass, DELIVERY__ARTIFACTS);

		metadataEClass = createEClass(METADATA);

		propertyEClass = createEClass(PROPERTY);
		createEAttribute(propertyEClass, PROPERTY__VALUE);

		dependencyEClass = createEClass(DEPENDENCY);
		createEAttribute(dependencyEClass, DEPENDENCY__MANDATORY);
		createEReference(dependencyEClass, DEPENDENCY__VERSION);

		artifactEClass = createEClass(ARTIFACT);
		createEAttribute(artifactEClass, ARTIFACT__TITLE);
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
		VersioningPackage theVersioningPackage = (VersioningPackage) EPackage.Registry.INSTANCE.getEPackage(VersioningPackage.eNS_URI);
		DatatypesPackage theDatatypesPackage = (DatatypesPackage) EPackage.Registry.INSTANCE.getEPackage(DatatypesPackage.eNS_URI);
		ElementsPackage theElementsPackage = (ElementsPackage) EPackage.Registry.INSTANCE.getEPackage(ElementsPackage.eNS_URI);

		// Add subpackages
		getESubpackages().add(theVersioningPackage);
		getESubpackages().add(theDatatypesPackage);
		getESubpackages().add(theElementsPackage);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		deliveryEClass.getESuperTypes().add(theElementsPackage.getDependantElement());
		deliveryEClass.getESuperTypes().add(theElementsPackage.getNamedElement());
		deliveryEClass.getESuperTypes().add(theElementsPackage.getPropertiesElement());
		metadataEClass.getESuperTypes().add(theElementsPackage.getPropertiesElement());
		propertyEClass.getESuperTypes().add(theElementsPackage.getNamedElement());
		dependencyEClass.getESuperTypes().add(theElementsPackage.getTypedElement());
		dependencyEClass.getESuperTypes().add(theElementsPackage.getPropertiesElement());
		artifactEClass.getESuperTypes().add(theElementsPackage.getDependantElement());
		artifactEClass.getESuperTypes().add(theElementsPackage.getTypedElement());
		artifactEClass.getESuperTypes().add(theElementsPackage.getNamedElement());
		artifactEClass.getESuperTypes().add(theElementsPackage.getPropertiesElement());

		// Initialize classes and features; add operations and parameters
		initEClass(cidEClass, Cid.class, "Cid", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCid_Deliveries(), this.getDelivery(), null, "deliveries", null, 0, -1, Cid.class,
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);

		addEOperation(cidEClass, theDatatypesPackage.getArtifactList(), "getArtifacts", 0, 1, IS_UNIQUE, IS_ORDERED);

		EOperation op = addEOperation(cidEClass, theDatatypesPackage.getArtifactList(), "getArtifacts", 0, 1,
			IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "type", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(cidEClass, theDatatypesPackage.getArtifactList(), "getArtifacts", 0, 1, IS_UNIQUE,
			IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "type", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(deliveryEClass, Delivery.class, "Delivery", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDelivery_Artifacts(), this.getArtifact(), null, "artifacts", null, 0, -1, Delivery.class,
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);

		op = addEOperation(deliveryEClass, theDatatypesPackage.getArtifactList(), "getArtifacts", 0, 1, IS_UNIQUE,
			IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "type", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(deliveryEClass, theDatatypesPackage.getArtifactList(), "getArtifacts", 0, 1, IS_UNIQUE,
			IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "type", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(metadataEClass, Metadata.class, "Metadata", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(propertyEClass, Property.class, "Property", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getProperty_Value(), ecorePackage.getEString(), "value", null, 0, 1, Property.class,
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dependencyEClass, Dependency.class, "Dependency", !IS_ABSTRACT, !IS_INTERFACE,
			IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDependency_Mandatory(), ecorePackage.getEBoolean(), "mandatory", "true", 0, 1,
			Dependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);
		initEReference(getDependency_Version(), theVersioningPackage.getVersionRange(), null, "version", null, 0, 1,
			Dependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
			!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(dependencyEClass, theDatatypesPackage.getIDependencyBinding(), "getConcreteBinding", 0, 1,
			IS_UNIQUE, IS_ORDERED);

		initEClass(artifactEClass, Artifact.class, "Artifact", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getArtifact_Title(), ecorePackage.getEString(), "title", null, 0, 1, Artifact.class,
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(artifactEClass, theDatatypesPackage.getIArtifactBinding(), "getConcreteBinding", 0, 1, IS_UNIQUE,
			IS_ORDERED);

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
	protected void createExtendedMetaDataAnnotations()
	{
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";
		addAnnotation(getCid_Deliveries(), source, new String[] {"kind", "element", "name", "delivery", "namespace",
			"##targetNamespace"});
		addAnnotation(getDelivery_Artifacts(), source, new String[] {"kind", "element", "name", "artifact",
			"namespace", "##targetNamespace"});
	}

} //ModelPackageImpl

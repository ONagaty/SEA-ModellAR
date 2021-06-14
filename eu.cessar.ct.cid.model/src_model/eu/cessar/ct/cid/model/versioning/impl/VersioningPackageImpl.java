/**
 */
package eu.cessar.ct.cid.model.versioning.impl;

import eu.cessar.ct.cid.model.ModelPackage;

import eu.cessar.ct.cid.model.datatypes.DatatypesPackage;

import eu.cessar.ct.cid.model.datatypes.impl.DatatypesPackageImpl;

import eu.cessar.ct.cid.model.elements.ElementsPackage;

import eu.cessar.ct.cid.model.elements.impl.ElementsPackageImpl;

import eu.cessar.ct.cid.model.impl.ModelPackageImpl;

import eu.cessar.ct.cid.model.versioning.Limit;
import eu.cessar.ct.cid.model.versioning.VersionRange;
import eu.cessar.ct.cid.model.versioning.VersioningFactory;
import eu.cessar.ct.cid.model.versioning.VersioningPackage;

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
public class VersioningPackageImpl extends EPackageImpl implements VersioningPackage
{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass versionRangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass limitEClass = null;

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
	 * @see eu.cessar.ct.cid.model.versioning.VersioningPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private VersioningPackageImpl()
	{
		super(eNS_URI, VersioningFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link VersioningPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static VersioningPackage init()
	{
		if (isInited)
			return (VersioningPackage) EPackage.Registry.INSTANCE.getEPackage(VersioningPackage.eNS_URI);

		// Obtain or create and register package
		VersioningPackageImpl theVersioningPackage = (VersioningPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof VersioningPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
			: new VersioningPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		ModelPackageImpl theModelPackage = (ModelPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(ModelPackage.eNS_URI) instanceof ModelPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ModelPackage.eNS_URI)
			: ModelPackage.eINSTANCE);
		DatatypesPackageImpl theDatatypesPackage = (DatatypesPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(DatatypesPackage.eNS_URI) instanceof DatatypesPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DatatypesPackage.eNS_URI)
			: DatatypesPackage.eINSTANCE);
		ElementsPackageImpl theElementsPackage = (ElementsPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(ElementsPackage.eNS_URI) instanceof ElementsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ElementsPackage.eNS_URI)
			: ElementsPackage.eINSTANCE);

		// Create package meta-data objects
		theVersioningPackage.createPackageContents();
		theModelPackage.createPackageContents();
		theDatatypesPackage.createPackageContents();
		theElementsPackage.createPackageContents();

		// Initialize created meta-data
		theVersioningPackage.initializePackageContents();
		theModelPackage.initializePackageContents();
		theDatatypesPackage.initializePackageContents();
		theElementsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theVersioningPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(VersioningPackage.eNS_URI, theVersioningPackage);
		return theVersioningPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVersionRange()
	{
		return versionRangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVersionRange_From()
	{
		return (EReference) versionRangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVersionRange_To()
	{
		return (EReference) versionRangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLimit()
	{
		return limitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLimit_Including()
	{
		return (EAttribute) limitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLimit_Value()
	{
		return (EAttribute) limitEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VersioningFactory getVersioningFactory()
	{
		return (VersioningFactory) getEFactoryInstance();
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
		versionRangeEClass = createEClass(VERSION_RANGE);
		createEReference(versionRangeEClass, VERSION_RANGE__FROM);
		createEReference(versionRangeEClass, VERSION_RANGE__TO);

		limitEClass = createEClass(LIMIT);
		createEAttribute(limitEClass, LIMIT__INCLUDING);
		createEAttribute(limitEClass, LIMIT__VALUE);
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

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(versionRangeEClass, VersionRange.class, "VersionRange", !IS_ABSTRACT, !IS_INTERFACE,
			IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVersionRange_From(), this.getLimit(), null, "from", null, 0, 1, VersionRange.class,
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);
		initEReference(getVersionRange_To(), this.getLimit(), null, "to", null, 0, 1, VersionRange.class,
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
			!IS_DERIVED, IS_ORDERED);

		initEClass(limitEClass, Limit.class, "Limit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLimit_Including(), ecorePackage.getEBoolean(), "including", null, 0, 1, Limit.class,
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLimit_Value(), theDatatypesPackage.getVersionLiteral(), "value", null, 0, 1, Limit.class,
			!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
	}

} //VersioningPackageImpl

/**
 */
package eu.cessar.ct.cid.model.datatypes.impl;

import eu.cessar.ct.cid.model.IArtifactBinding;
import eu.cessar.ct.cid.model.IDependencyBinding;
import eu.cessar.ct.cid.model.ModelPackage;

import eu.cessar.ct.cid.model.datatypes.DatatypesFactory;
import eu.cessar.ct.cid.model.datatypes.DatatypesPackage;

import eu.cessar.ct.cid.model.elements.ElementsPackage;

import eu.cessar.ct.cid.model.elements.impl.ElementsPackageImpl;

import eu.cessar.ct.cid.model.impl.ModelPackageImpl;

import eu.cessar.ct.cid.model.versioning.VersioningPackage;

import eu.cessar.ct.cid.model.versioning.impl.VersioningPackageImpl;

import java.util.List;

import org.eclipse.core.runtime.IStatus;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DatatypesPackageImpl extends EPackageImpl implements DatatypesPackage
{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType versionLiteralEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType identifierEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType iStatusEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType artifactListEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType propertyListEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType dependencyListEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType iArtifactBindingEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType iDependencyBindingEDataType = null;

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
	 * @see eu.cessar.ct.cid.model.datatypes.DatatypesPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DatatypesPackageImpl()
	{
		super(eNS_URI, DatatypesFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link DatatypesPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static DatatypesPackage init()
	{
		if (isInited)
			return (DatatypesPackage) EPackage.Registry.INSTANCE.getEPackage(DatatypesPackage.eNS_URI);

		// Obtain or create and register package
		DatatypesPackageImpl theDatatypesPackage = (DatatypesPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof DatatypesPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
			: new DatatypesPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		ModelPackageImpl theModelPackage = (ModelPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(ModelPackage.eNS_URI) instanceof ModelPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ModelPackage.eNS_URI)
			: ModelPackage.eINSTANCE);
		VersioningPackageImpl theVersioningPackage = (VersioningPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(VersioningPackage.eNS_URI) instanceof VersioningPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(VersioningPackage.eNS_URI)
			: VersioningPackage.eINSTANCE);
		ElementsPackageImpl theElementsPackage = (ElementsPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(ElementsPackage.eNS_URI) instanceof ElementsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ElementsPackage.eNS_URI)
			: ElementsPackage.eINSTANCE);

		// Create package meta-data objects
		theDatatypesPackage.createPackageContents();
		theModelPackage.createPackageContents();
		theVersioningPackage.createPackageContents();
		theElementsPackage.createPackageContents();

		// Initialize created meta-data
		theDatatypesPackage.initializePackageContents();
		theModelPackage.initializePackageContents();
		theVersioningPackage.initializePackageContents();
		theElementsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDatatypesPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(DatatypesPackage.eNS_URI, theDatatypesPackage);
		return theDatatypesPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getVersionLiteral()
	{
		return versionLiteralEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getIdentifier()
	{
		return identifierEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getIStatus()
	{
		return iStatusEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getArtifactList()
	{
		return artifactListEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getPropertyList()
	{
		return propertyListEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getDependencyList()
	{
		return dependencyListEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getIArtifactBinding()
	{
		return iArtifactBindingEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getIDependencyBinding()
	{
		return iDependencyBindingEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DatatypesFactory getDatatypesFactory()
	{
		return (DatatypesFactory) getEFactoryInstance();
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

		// Create data types
		versionLiteralEDataType = createEDataType(VERSION_LITERAL);
		identifierEDataType = createEDataType(IDENTIFIER);
		iStatusEDataType = createEDataType(ISTATUS);
		artifactListEDataType = createEDataType(ARTIFACT_LIST);
		propertyListEDataType = createEDataType(PROPERTY_LIST);
		dependencyListEDataType = createEDataType(DEPENDENCY_LIST);
		iArtifactBindingEDataType = createEDataType(IARTIFACT_BINDING);
		iDependencyBindingEDataType = createEDataType(IDEPENDENCY_BINDING);
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

		// Initialize data types
		initEDataType(versionLiteralEDataType, String.class, "VersionLiteral", IS_SERIALIZABLE,
			!IS_GENERATED_INSTANCE_CLASS);
		initEDataType(identifierEDataType, String.class, "Identifier", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(iStatusEDataType, IStatus.class, "IStatus", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(artifactListEDataType, List.class, "ArtifactList", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS,
			"java.util.List<eu.cessar.ct.cid.model.Artifact>");
		initEDataType(propertyListEDataType, List.class, "PropertyList", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS,
			"java.util.List<eu.cessar.ct.cid.model.Property>");
		initEDataType(dependencyListEDataType, List.class, "DependencyList", IS_SERIALIZABLE,
			!IS_GENERATED_INSTANCE_CLASS, "java.util.List<eu.cessar.ct.cid.model.Dependency>");
		initEDataType(iArtifactBindingEDataType, IArtifactBinding.class, "IArtifactBinding", IS_SERIALIZABLE,
			!IS_GENERATED_INSTANCE_CLASS);
		initEDataType(iDependencyBindingEDataType, IDependencyBinding.class, "IDependencyBinding", IS_SERIALIZABLE,
			!IS_GENERATED_INSTANCE_CLASS);
	}

} //DatatypesPackageImpl

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.cessar.ct.sdk.pm.impl;

import eu.cessar.ct.sdk.pm.IEMFProxyObject;
import eu.cessar.ct.sdk.pm.IPMChoiceContainer;
import eu.cessar.ct.sdk.pm.IPMContainer;
import eu.cessar.ct.sdk.pm.IPMContainerParent;
import eu.cessar.ct.sdk.pm.IPMElement;
import eu.cessar.ct.sdk.pm.IPMInstanceRef;
import eu.cessar.ct.sdk.pm.IPMModuleConfiguration;
import eu.cessar.ct.sdk.pm.IPMNamedElement;
import eu.cessar.ct.sdk.pm.IPMPackage;
import eu.cessar.ct.sdk.pm.IPresentationModel;
import eu.cessar.ct.sdk.pm.MissingContainer;
import eu.cessar.ct.sdk.pm.pmFactory;
import eu.cessar.ct.sdk.pm.pmPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class pmPackageImpl extends EPackageImpl implements pmPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iPresentationModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ipmPackageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ipmElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ipmNamedElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ipmContainerParentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ipmModuleConfigurationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ipmContainerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ipmChoiceContainerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ipmInstanceRefEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass missingContainerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iemfProxyObjectEClass = null;

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
	 * @see eu.cessar.ct.sdk.pm.pmPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private pmPackageImpl() {
		super(eNS_URI, pmFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link pmPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static pmPackage init() {
		if (isInited) return (pmPackage)EPackage.Registry.INSTANCE.getEPackage(pmPackage.eNS_URI);

		// Obtain or create and register package
		pmPackageImpl thepmPackage = (pmPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof pmPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new pmPackageImpl());

		isInited = true;

		// Create package meta-data objects
		thepmPackage.createPackageContents();

		// Initialize created meta-data
		thepmPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		thepmPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(pmPackage.eNS_URI, thepmPackage);
		return thepmPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIPresentationModel() {
		return iPresentationModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIPMPackage() {
		return ipmPackageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIPMElement() {
		return ipmElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIPMNamedElement() {
		return ipmNamedElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIPMContainerParent() {
		return ipmContainerParentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIPMModuleConfiguration() {
		return ipmModuleConfigurationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIPMContainer() {
		return ipmContainerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIPMChoiceContainer() {
		return ipmChoiceContainerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIPMInstanceRef() {
		return ipmInstanceRefEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMissingContainer() {
		return missingContainerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIEMFProxyObject() {
		return iemfProxyObjectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public pmFactory getpmFactory() {
		return (pmFactory)getEFactoryInstance();
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
		iPresentationModelEClass = createEClass(IPRESENTATION_MODEL);

		ipmPackageEClass = createEClass(IPM_PACKAGE);

		ipmElementEClass = createEClass(IPM_ELEMENT);

		ipmNamedElementEClass = createEClass(IPM_NAMED_ELEMENT);

		ipmContainerParentEClass = createEClass(IPM_CONTAINER_PARENT);

		ipmModuleConfigurationEClass = createEClass(IPM_MODULE_CONFIGURATION);

		ipmContainerEClass = createEClass(IPM_CONTAINER);

		ipmChoiceContainerEClass = createEClass(IPM_CHOICE_CONTAINER);

		ipmInstanceRefEClass = createEClass(IPM_INSTANCE_REF);

		missingContainerEClass = createEClass(MISSING_CONTAINER);

		iemfProxyObjectEClass = createEClass(IEMF_PROXY_OBJECT);
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
		iPresentationModelEClass.getESuperTypes().add(this.getIPMElement());
		ipmPackageEClass.getESuperTypes().add(this.getIPMNamedElement());
		ipmElementEClass.getESuperTypes().add(this.getIEMFProxyObject());
		ipmNamedElementEClass.getESuperTypes().add(this.getIPMElement());
		ipmContainerParentEClass.getESuperTypes().add(this.getIPMNamedElement());
		ipmModuleConfigurationEClass.getESuperTypes().add(this.getIPMContainerParent());
		ipmContainerEClass.getESuperTypes().add(this.getIPMContainerParent());
		ipmChoiceContainerEClass.getESuperTypes().add(this.getIPMContainer());
		ipmInstanceRefEClass.getESuperTypes().add(this.getIPMElement());
		missingContainerEClass.getESuperTypes().add(this.getIPMContainer());

		// Initialize classes and features; add operations and parameters
		initEClass(iPresentationModelEClass, IPresentationModel.class, "IPresentationModel", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(ipmPackageEClass, IPMPackage.class, "IPMPackage", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(ipmElementEClass, IPMElement.class, "IPMElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(ipmNamedElementEClass, IPMNamedElement.class, "IPMNamedElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(ipmContainerParentEClass, IPMContainerParent.class, "IPMContainerParent", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		EOperation op = addEOperation(ipmContainerParentEClass, ecorePackage.getEBooleanObject(), "isCompatibleWith", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "qualifiedName", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(ipmModuleConfigurationEClass, IPMModuleConfiguration.class, "IPMModuleConfiguration", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		op = addEOperation(ipmModuleConfigurationEClass, this.getIPMModuleConfiguration(), "asCompatibleModuleConfiguration", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "qualifiedName", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(ipmContainerEClass, IPMContainer.class, "IPMContainer", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		op = addEOperation(ipmContainerEClass, this.getIPMContainer(), "asCompatibleContainer", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "qualifiedName", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(ipmChoiceContainerEClass, IPMChoiceContainer.class, "IPMChoiceContainer", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(ipmInstanceRefEClass, IPMInstanceRef.class, "IPMInstanceRef", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(missingContainerEClass, MissingContainer.class, "MissingContainer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		addEOperation(missingContainerEClass, ecorePackage.getEString(), "getShortName", 0, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(missingContainerEClass, ecorePackage.getEBoolean(), "isSetShortName", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(iemfProxyObjectEClass, IEMFProxyObject.class, "IEMFProxyObject", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// EMF_PROXY
		createEMF_PROXYAnnotations();
	}

	/**
	 * Initializes the annotations for <b>EMF_PROXY</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createEMF_PROXYAnnotations() {
		String source = "EMF_PROXY";		
		addAnnotation
		  (ipmContainerParentEClass.getEOperations().get(0), 
		   source, 
		   new String[] {
			 "TYPE", "OP_isCompatibleWith"
		   });		
		addAnnotation
		  (ipmModuleConfigurationEClass.getEOperations().get(0), 
		   source, 
		   new String[] {
			 "TYPE", "OP_asCompatibleModuleConfiguration"
		   });		
		addAnnotation
		  (ipmContainerEClass.getEOperations().get(0), 
		   source, 
		   new String[] {
			 "TYPE", "OP_asCompatibleContainer"
		   });		
	}

} //pmPackageImpl

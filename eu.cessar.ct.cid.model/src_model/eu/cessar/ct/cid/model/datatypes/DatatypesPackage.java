/**
 */
package eu.cessar.ct.cid.model.datatypes;

import org.eclipse.emf.ecore.EDataType;
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
 * @see eu.cessar.ct.cid.model.datatypes.DatatypesFactory
 * @model kind="package"
 * @generated
 */
public interface DatatypesPackage extends EPackage
{
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "datatypes";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.cessar.eu/cid/datatypes";

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
	DatatypesPackage eINSTANCE = eu.cessar.ct.cid.model.datatypes.impl.DatatypesPackageImpl.init();

	/**
	 * The meta object id for the '<em>Version Literal</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.String
	 * @see eu.cessar.ct.cid.model.datatypes.impl.DatatypesPackageImpl#getVersionLiteral()
	 * @generated
	 */
	int VERSION_LITERAL = 0;

	/**
	 * The meta object id for the '<em>Identifier</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.String
	 * @see eu.cessar.ct.cid.model.datatypes.impl.DatatypesPackageImpl#getIdentifier()
	 * @generated
	 */
	int IDENTIFIER = 1;

	/**
	 * The meta object id for the '<em>IStatus</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.core.runtime.IStatus
	 * @see eu.cessar.ct.cid.model.datatypes.impl.DatatypesPackageImpl#getIStatus()
	 * @generated
	 */
	int ISTATUS = 2;

	/**
	 * The meta object id for the '<em>Artifact List</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.List
	 * @see eu.cessar.ct.cid.model.datatypes.impl.DatatypesPackageImpl#getArtifactList()
	 * @generated
	 */
	int ARTIFACT_LIST = 3;

	/**
	 * The meta object id for the '<em>Property List</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.List
	 * @see eu.cessar.ct.cid.model.datatypes.impl.DatatypesPackageImpl#getPropertyList()
	 * @generated
	 */
	int PROPERTY_LIST = 4;

	/**
	 * The meta object id for the '<em>Dependency List</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.List
	 * @see eu.cessar.ct.cid.model.datatypes.impl.DatatypesPackageImpl#getDependencyList()
	 * @generated
	 */
	int DEPENDENCY_LIST = 5;

	/**
	 * The meta object id for the '<em>IArtifact Binding</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.cid.model.IArtifactBinding
	 * @see eu.cessar.ct.cid.model.datatypes.impl.DatatypesPackageImpl#getIArtifactBinding()
	 * @generated
	 */
	int IARTIFACT_BINDING = 6;

	/**
	 * The meta object id for the '<em>IDependency Binding</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.cid.model.IDependencyBinding
	 * @see eu.cessar.ct.cid.model.datatypes.impl.DatatypesPackageImpl#getIDependencyBinding()
	 * @generated
	 */
	int IDEPENDENCY_BINDING = 7;

	/**
	 * Returns the meta object for data type '{@link java.lang.String <em>Version Literal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Version Literal</em>'.
	 * @see java.lang.String
	 * @model instanceClass="java.lang.String"
	 * @generated
	 */
	EDataType getVersionLiteral();

	/**
	 * Returns the meta object for data type '{@link java.lang.String <em>Identifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Identifier</em>'.
	 * @see java.lang.String
	 * @model instanceClass="java.lang.String"
	 * @generated
	 */
	EDataType getIdentifier();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.core.runtime.IStatus <em>IStatus</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>IStatus</em>'.
	 * @see org.eclipse.core.runtime.IStatus
	 * @model instanceClass="org.eclipse.core.runtime.IStatus"
	 * @generated
	 */
	EDataType getIStatus();

	/**
	 * Returns the meta object for data type '{@link java.util.List <em>Artifact List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Artifact List</em>'.
	 * @see java.util.List
	 * @model instanceClass="java.util.List<eu.cessar.ct.cid.model.Artifact>"
	 * @generated
	 */
	EDataType getArtifactList();

	/**
	 * Returns the meta object for data type '{@link java.util.List <em>Property List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Property List</em>'.
	 * @see java.util.List
	 * @model instanceClass="java.util.List<eu.cessar.ct.cid.model.Property>"
	 * @generated
	 */
	EDataType getPropertyList();

	/**
	 * Returns the meta object for data type '{@link java.util.List <em>Dependency List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Dependency List</em>'.
	 * @see java.util.List
	 * @model instanceClass="java.util.List<eu.cessar.ct.cid.model.Dependency>"
	 * @generated
	 */
	EDataType getDependencyList();

	/**
	 * Returns the meta object for data type '{@link eu.cessar.ct.cid.model.IArtifactBinding <em>IArtifact Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>IArtifact Binding</em>'.
	 * @see eu.cessar.ct.cid.model.IArtifactBinding
	 * @model instanceClass="eu.cessar.ct.cid.model.IArtifactBinding"
	 * @generated
	 */
	EDataType getIArtifactBinding();

	/**
	 * Returns the meta object for data type '{@link eu.cessar.ct.cid.model.IDependencyBinding <em>IDependency Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>IDependency Binding</em>'.
	 * @see eu.cessar.ct.cid.model.IDependencyBinding
	 * @model instanceClass="eu.cessar.ct.cid.model.IDependencyBinding"
	 * @generated
	 */
	EDataType getIDependencyBinding();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DatatypesFactory getDatatypesFactory();

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
		 * The meta object literal for the '<em>Version Literal</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.String
		 * @see eu.cessar.ct.cid.model.datatypes.impl.DatatypesPackageImpl#getVersionLiteral()
		 * @generated
		 */
		EDataType VERSION_LITERAL = eINSTANCE.getVersionLiteral();

		/**
		 * The meta object literal for the '<em>Identifier</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.String
		 * @see eu.cessar.ct.cid.model.datatypes.impl.DatatypesPackageImpl#getIdentifier()
		 * @generated
		 */
		EDataType IDENTIFIER = eINSTANCE.getIdentifier();

		/**
		 * The meta object literal for the '<em>IStatus</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.core.runtime.IStatus
		 * @see eu.cessar.ct.cid.model.datatypes.impl.DatatypesPackageImpl#getIStatus()
		 * @generated
		 */
		EDataType ISTATUS = eINSTANCE.getIStatus();

		/**
		 * The meta object literal for the '<em>Artifact List</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.List
		 * @see eu.cessar.ct.cid.model.datatypes.impl.DatatypesPackageImpl#getArtifactList()
		 * @generated
		 */
		EDataType ARTIFACT_LIST = eINSTANCE.getArtifactList();

		/**
		 * The meta object literal for the '<em>Property List</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.List
		 * @see eu.cessar.ct.cid.model.datatypes.impl.DatatypesPackageImpl#getPropertyList()
		 * @generated
		 */
		EDataType PROPERTY_LIST = eINSTANCE.getPropertyList();

		/**
		 * The meta object literal for the '<em>Dependency List</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.List
		 * @see eu.cessar.ct.cid.model.datatypes.impl.DatatypesPackageImpl#getDependencyList()
		 * @generated
		 */
		EDataType DEPENDENCY_LIST = eINSTANCE.getDependencyList();

		/**
		 * The meta object literal for the '<em>IArtifact Binding</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.cid.model.IArtifactBinding
		 * @see eu.cessar.ct.cid.model.datatypes.impl.DatatypesPackageImpl#getIArtifactBinding()
		 * @generated
		 */
		EDataType IARTIFACT_BINDING = eINSTANCE.getIArtifactBinding();

		/**
		 * The meta object literal for the '<em>IDependency Binding</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.cid.model.IDependencyBinding
		 * @see eu.cessar.ct.cid.model.datatypes.impl.DatatypesPackageImpl#getIDependencyBinding()
		 * @generated
		 */
		EDataType IDEPENDENCY_BINDING = eINSTANCE.getIDependencyBinding();

	}

} //DatatypesPackage

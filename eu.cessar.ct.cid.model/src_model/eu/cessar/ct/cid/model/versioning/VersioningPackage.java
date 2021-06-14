/**
 */
package eu.cessar.ct.cid.model.versioning;

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
 * @see eu.cessar.ct.cid.model.versioning.VersioningFactory
 * @model kind="package"
 * @generated
 */
public interface VersioningPackage extends EPackage
{
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "versioning";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.cessar.eu/cid/versioning";

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
	VersioningPackage eINSTANCE = eu.cessar.ct.cid.model.versioning.impl.VersioningPackageImpl.init();

	/**
	 * The meta object id for the '{@link eu.cessar.ct.cid.model.versioning.impl.VersionRangeImpl <em>Version Range</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.cid.model.versioning.impl.VersionRangeImpl
	 * @see eu.cessar.ct.cid.model.versioning.impl.VersioningPackageImpl#getVersionRange()
	 * @generated
	 */
	int VERSION_RANGE = 0;

	/**
	 * The feature id for the '<em><b>From</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VERSION_RANGE__FROM = 0;

	/**
	 * The feature id for the '<em><b>To</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VERSION_RANGE__TO = 1;

	/**
	 * The number of structural features of the '<em>Version Range</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VERSION_RANGE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link eu.cessar.ct.cid.model.versioning.impl.LimitImpl <em>Limit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see eu.cessar.ct.cid.model.versioning.impl.LimitImpl
	 * @see eu.cessar.ct.cid.model.versioning.impl.VersioningPackageImpl#getLimit()
	 * @generated
	 */
	int LIMIT = 1;

	/**
	 * The feature id for the '<em><b>Including</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIMIT__INCLUDING = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIMIT__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Limit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIMIT_FEATURE_COUNT = 2;

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.cid.model.versioning.VersionRange <em>Version Range</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Version Range</em>'.
	 * @see eu.cessar.ct.cid.model.versioning.VersionRange
	 * @generated
	 */
	EClass getVersionRange();

	/**
	 * Returns the meta object for the containment reference '{@link eu.cessar.ct.cid.model.versioning.VersionRange#getFrom <em>From</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>From</em>'.
	 * @see eu.cessar.ct.cid.model.versioning.VersionRange#getFrom()
	 * @see #getVersionRange()
	 * @generated
	 */
	EReference getVersionRange_From();

	/**
	 * Returns the meta object for the containment reference '{@link eu.cessar.ct.cid.model.versioning.VersionRange#getTo <em>To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>To</em>'.
	 * @see eu.cessar.ct.cid.model.versioning.VersionRange#getTo()
	 * @see #getVersionRange()
	 * @generated
	 */
	EReference getVersionRange_To();

	/**
	 * Returns the meta object for class '{@link eu.cessar.ct.cid.model.versioning.Limit <em>Limit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Limit</em>'.
	 * @see eu.cessar.ct.cid.model.versioning.Limit
	 * @generated
	 */
	EClass getLimit();

	/**
	 * Returns the meta object for the attribute '{@link eu.cessar.ct.cid.model.versioning.Limit#isIncluding <em>Including</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Including</em>'.
	 * @see eu.cessar.ct.cid.model.versioning.Limit#isIncluding()
	 * @see #getLimit()
	 * @generated
	 */
	EAttribute getLimit_Including();

	/**
	 * Returns the meta object for the attribute '{@link eu.cessar.ct.cid.model.versioning.Limit#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see eu.cessar.ct.cid.model.versioning.Limit#getValue()
	 * @see #getLimit()
	 * @generated
	 */
	EAttribute getLimit_Value();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	VersioningFactory getVersioningFactory();

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
		 * The meta object literal for the '{@link eu.cessar.ct.cid.model.versioning.impl.VersionRangeImpl <em>Version Range</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.cid.model.versioning.impl.VersionRangeImpl
		 * @see eu.cessar.ct.cid.model.versioning.impl.VersioningPackageImpl#getVersionRange()
		 * @generated
		 */
		EClass VERSION_RANGE = eINSTANCE.getVersionRange();

		/**
		 * The meta object literal for the '<em><b>From</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VERSION_RANGE__FROM = eINSTANCE.getVersionRange_From();

		/**
		 * The meta object literal for the '<em><b>To</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VERSION_RANGE__TO = eINSTANCE.getVersionRange_To();

		/**
		 * The meta object literal for the '{@link eu.cessar.ct.cid.model.versioning.impl.LimitImpl <em>Limit</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see eu.cessar.ct.cid.model.versioning.impl.LimitImpl
		 * @see eu.cessar.ct.cid.model.versioning.impl.VersioningPackageImpl#getLimit()
		 * @generated
		 */
		EClass LIMIT = eINSTANCE.getLimit();

		/**
		 * The meta object literal for the '<em><b>Including</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LIMIT__INCLUDING = eINSTANCE.getLimit_Including();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LIMIT__VALUE = eINSTANCE.getLimit_Value();

	}

} //VersioningPackage

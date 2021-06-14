/**
 */
package eu.cessar.ct.cid.model.versioning;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see eu.cessar.ct.cid.model.versioning.VersioningPackage
 * @generated
 */
public interface VersioningFactory extends EFactory
{
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	VersioningFactory eINSTANCE = eu.cessar.ct.cid.model.versioning.impl.VersioningFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Version Range</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Version Range</em>'.
	 * @generated
	 */
	VersionRange createVersionRange();

	/**
	 * Returns a new object of class '<em>Limit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Limit</em>'.
	 * @generated
	 */
	Limit createLimit();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	VersioningPackage getVersioningPackage();

} //VersioningFactory

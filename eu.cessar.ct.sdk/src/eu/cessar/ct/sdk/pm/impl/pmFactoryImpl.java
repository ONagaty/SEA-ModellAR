/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.cessar.ct.sdk.pm.impl;

import eu.cessar.ct.sdk.pm.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class pmFactoryImpl extends EFactoryImpl implements pmFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static pmFactory init() {
		try {
			pmFactory thepmFactory = (pmFactory)EPackage.Registry.INSTANCE.getEFactory("http://cessar.eu/PMBase"); 
			if (thepmFactory != null) {
				return thepmFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new pmFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public pmFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case pmPackage.MISSING_CONTAINER: return createMissingContainer();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MissingContainer createMissingContainer() {
		MissingContainerImpl missingContainer = new MissingContainerImpl();
		return missingContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public pmPackage getpmPackage() {
		return (pmPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static pmPackage getPackage() {
		return pmPackage.eINSTANCE;
	}

} //pmFactoryImpl

/**
 */
package eu.cessar.ct.cid.model.versioning.impl;

import eu.cessar.ct.cid.model.versioning.*;

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
public class VersioningFactoryImpl extends EFactoryImpl implements VersioningFactory
{
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static VersioningFactory init()
	{
		try
		{
			VersioningFactory theVersioningFactory = (VersioningFactory) EPackage.Registry.INSTANCE.getEFactory(VersioningPackage.eNS_URI);
			if (theVersioningFactory != null)
			{
				return theVersioningFactory;
			}
		}
		catch (Exception exception)
		{
			EcorePlugin.INSTANCE.log(exception);
		}
		return new VersioningFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VersioningFactoryImpl()
	{
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass)
	{
		switch (eClass.getClassifierID())
		{
			case VersioningPackage.VERSION_RANGE:
				return createVersionRange();
			case VersioningPackage.LIMIT:
				return createLimit();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VersionRange createVersionRange()
	{
		VersionRangeImpl versionRange = new VersionRangeImpl();
		return versionRange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Limit createLimit()
	{
		LimitImpl limit = new LimitImpl();
		return limit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VersioningPackage getVersioningPackage()
	{
		return (VersioningPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static VersioningPackage getPackage()
	{
		return VersioningPackage.eINSTANCE;
	}

} //VersioningFactoryImpl

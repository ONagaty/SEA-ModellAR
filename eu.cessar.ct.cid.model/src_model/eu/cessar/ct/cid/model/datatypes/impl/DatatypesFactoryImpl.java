/**
 */
package eu.cessar.ct.cid.model.datatypes.impl;

import eu.cessar.ct.cid.model.Artifact;
import eu.cessar.ct.cid.model.Dependency;
import eu.cessar.ct.cid.model.IArtifactBinding;
import eu.cessar.ct.cid.model.IDependencyBinding;
import eu.cessar.ct.cid.model.Property;

import eu.cessar.ct.cid.model.datatypes.*;

import java.util.List;

import org.eclipse.core.runtime.IStatus;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
public class DatatypesFactoryImpl extends EFactoryImpl implements DatatypesFactory
{
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DatatypesFactory init()
	{
		try
		{
			DatatypesFactory theDatatypesFactory = (DatatypesFactory) EPackage.Registry.INSTANCE.getEFactory(DatatypesPackage.eNS_URI);
			if (theDatatypesFactory != null)
			{
				return theDatatypesFactory;
			}
		}
		catch (Exception exception)
		{
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DatatypesFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DatatypesFactoryImpl()
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
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue)
	{
		switch (eDataType.getClassifierID())
		{
			case DatatypesPackage.VERSION_LITERAL:
				return createVersionLiteralFromString(eDataType, initialValue);
			case DatatypesPackage.IDENTIFIER:
				return createIdentifierFromString(eDataType, initialValue);
			case DatatypesPackage.ISTATUS:
				return createIStatusFromString(eDataType, initialValue);
			case DatatypesPackage.ARTIFACT_LIST:
				return createArtifactListFromString(eDataType, initialValue);
			case DatatypesPackage.PROPERTY_LIST:
				return createPropertyListFromString(eDataType, initialValue);
			case DatatypesPackage.DEPENDENCY_LIST:
				return createDependencyListFromString(eDataType, initialValue);
			case DatatypesPackage.IARTIFACT_BINDING:
				return createIArtifactBindingFromString(eDataType, initialValue);
			case DatatypesPackage.IDEPENDENCY_BINDING:
				return createIDependencyBindingFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName()
					+ "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue)
	{
		switch (eDataType.getClassifierID())
		{
			case DatatypesPackage.VERSION_LITERAL:
				return convertVersionLiteralToString(eDataType, instanceValue);
			case DatatypesPackage.IDENTIFIER:
				return convertIdentifierToString(eDataType, instanceValue);
			case DatatypesPackage.ISTATUS:
				return convertIStatusToString(eDataType, instanceValue);
			case DatatypesPackage.ARTIFACT_LIST:
				return convertArtifactListToString(eDataType, instanceValue);
			case DatatypesPackage.PROPERTY_LIST:
				return convertPropertyListToString(eDataType, instanceValue);
			case DatatypesPackage.DEPENDENCY_LIST:
				return convertDependencyListToString(eDataType, instanceValue);
			case DatatypesPackage.IARTIFACT_BINDING:
				return convertIArtifactBindingToString(eDataType, instanceValue);
			case DatatypesPackage.IDEPENDENCY_BINDING:
				return convertIDependencyBindingToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName()
					+ "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String createVersionLiteralFromString(EDataType eDataType, String initialValue)
	{
		return (String) super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertVersionLiteralToString(EDataType eDataType, Object instanceValue)
	{
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String createIdentifierFromString(EDataType eDataType, String initialValue)
	{
		return (String) super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIdentifierToString(EDataType eDataType, Object instanceValue)
	{
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IStatus createIStatusFromString(EDataType eDataType, String initialValue)
	{
		return (IStatus) super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIStatusToString(EDataType eDataType, Object instanceValue)
	{
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public List<Artifact> createArtifactListFromString(EDataType eDataType, String initialValue)
	{
		return (List<Artifact>) super.createFromString(initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertArtifactListToString(EDataType eDataType, Object instanceValue)
	{
		return super.convertToString(instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public List<Property> createPropertyListFromString(EDataType eDataType, String initialValue)
	{
		return (List<Property>) super.createFromString(initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPropertyListToString(EDataType eDataType, Object instanceValue)
	{
		return super.convertToString(instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public List<Dependency> createDependencyListFromString(EDataType eDataType, String initialValue)
	{
		return (List<Dependency>) super.createFromString(initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDependencyListToString(EDataType eDataType, Object instanceValue)
	{
		return super.convertToString(instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IArtifactBinding createIArtifactBindingFromString(EDataType eDataType, String initialValue)
	{
		return (IArtifactBinding) super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIArtifactBindingToString(EDataType eDataType, Object instanceValue)
	{
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IDependencyBinding createIDependencyBindingFromString(EDataType eDataType, String initialValue)
	{
		return (IDependencyBinding) super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIDependencyBindingToString(EDataType eDataType, Object instanceValue)
	{
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DatatypesPackage getDatatypesPackage()
	{
		return (DatatypesPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static DatatypesPackage getPackage()
	{
		return DatatypesPackage.eINSTANCE;
	}

} //DatatypesFactoryImpl

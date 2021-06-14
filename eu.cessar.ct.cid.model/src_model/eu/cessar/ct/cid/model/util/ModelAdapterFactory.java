/**
 */
package eu.cessar.ct.cid.model.util;

import eu.cessar.ct.cid.model.*;

import eu.cessar.ct.cid.model.elements.DependantElement;
import eu.cessar.ct.cid.model.elements.NamedElement;
import eu.cessar.ct.cid.model.elements.PropertiesElement;
import eu.cessar.ct.cid.model.elements.TypedElement;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see eu.cessar.ct.cid.model.ModelPackage
 * @generated
 */
public class ModelAdapterFactory extends AdapterFactoryImpl
{
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ModelPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelAdapterFactory()
	{
		if (modelPackage == null)
		{
			modelPackage = ModelPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object)
	{
		if (object == modelPackage)
		{
			return true;
		}
		if (object instanceof EObject)
		{
			return ((EObject) object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModelSwitch<Adapter> modelSwitch = new ModelSwitch<Adapter>()
	{
		@Override
		public Adapter caseCid(Cid object)
		{
			return createCidAdapter();
		}

		@Override
		public Adapter caseDelivery(Delivery object)
		{
			return createDeliveryAdapter();
		}

		@Override
		public Adapter caseMetadata(Metadata object)
		{
			return createMetadataAdapter();
		}

		@Override
		public Adapter caseProperty(Property object)
		{
			return createPropertyAdapter();
		}

		@Override
		public Adapter caseDependency(Dependency object)
		{
			return createDependencyAdapter();
		}

		@Override
		public Adapter caseArtifact(Artifact object)
		{
			return createArtifactAdapter();
		}

		@Override
		public Adapter caseDependantElement(DependantElement object)
		{
			return createDependantElementAdapter();
		}

		@Override
		public Adapter caseNamedElement(NamedElement object)
		{
			return createNamedElementAdapter();
		}

		@Override
		public Adapter casePropertiesElement(PropertiesElement object)
		{
			return createPropertiesElementAdapter();
		}

		@Override
		public Adapter caseTypedElement(TypedElement object)
		{
			return createTypedElementAdapter();
		}

		@Override
		public Adapter defaultCase(EObject object)
		{
			return createEObjectAdapter();
		}
	};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target)
	{
		return modelSwitch.doSwitch((EObject) target);
	}

	/**
	 * Creates a new adapter for an object of class '{@link eu.cessar.ct.cid.model.Cid <em>Cid</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.cessar.ct.cid.model.Cid
	 * @generated
	 */
	public Adapter createCidAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link eu.cessar.ct.cid.model.Delivery <em>Delivery</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.cessar.ct.cid.model.Delivery
	 * @generated
	 */
	public Adapter createDeliveryAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link eu.cessar.ct.cid.model.Metadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.cessar.ct.cid.model.Metadata
	 * @generated
	 */
	public Adapter createMetadataAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link eu.cessar.ct.cid.model.Property <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.cessar.ct.cid.model.Property
	 * @generated
	 */
	public Adapter createPropertyAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link eu.cessar.ct.cid.model.Dependency <em>Dependency</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.cessar.ct.cid.model.Dependency
	 * @generated
	 */
	public Adapter createDependencyAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link eu.cessar.ct.cid.model.Artifact <em>Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.cessar.ct.cid.model.Artifact
	 * @generated
	 */
	public Adapter createArtifactAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link eu.cessar.ct.cid.model.elements.DependantElement <em>Dependant Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.cessar.ct.cid.model.elements.DependantElement
	 * @generated
	 */
	public Adapter createDependantElementAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link eu.cessar.ct.cid.model.elements.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.cessar.ct.cid.model.elements.NamedElement
	 * @generated
	 */
	public Adapter createNamedElementAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link eu.cessar.ct.cid.model.elements.PropertiesElement <em>Properties Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.cessar.ct.cid.model.elements.PropertiesElement
	 * @generated
	 */
	public Adapter createPropertiesElementAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link eu.cessar.ct.cid.model.elements.TypedElement <em>Typed Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.cessar.ct.cid.model.elements.TypedElement
	 * @generated
	 */
	public Adapter createTypedElementAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter()
	{
		return null;
	}

} //ModelAdapterFactory

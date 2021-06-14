/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.cessar.ct.workspace.internal.modeldocumentation.util;

import eu.cessar.ct.workspace.internal.modeldocumentation.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage
 * @generated
 */
public class ModeldocumentationAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ModeldocumentationPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModeldocumentationAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = ModeldocumentationPackage.eINSTANCE;
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
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModeldocumentationSwitch<Adapter> modelSwitch =
		new ModeldocumentationSwitch<Adapter>() {
			@Override
			public Adapter caseDocElement(DocElement object) {
				return createDocElementAdapter();
			}
			@Override
			public Adapter caseDefElement(DefElement object) {
				return createDefElementAdapter();
			}
			@Override
			public Adapter caseMultiplicity(Multiplicity object) {
				return createMultiplicityAdapter();
			}
			@Override
			public Adapter caseAttribute(Attribute object) {
				return createAttributeAdapter();
			}
			@Override
			public Adapter caseAttributeDef(AttributeDef object) {
				return createAttributeDefAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
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
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link eu.cessar.ct.workspace.internal.modeldocumentation.DocElement <em>Doc Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.DocElement
	 * @generated
	 */
	public Adapter createDocElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link eu.cessar.ct.workspace.internal.modeldocumentation.DefElement <em>Def Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.DefElement
	 * @generated
	 */
	public Adapter createDefElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link eu.cessar.ct.workspace.internal.modeldocumentation.Multiplicity <em>Multiplicity</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.Multiplicity
	 * @generated
	 */
	public Adapter createMultiplicityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link eu.cessar.ct.workspace.internal.modeldocumentation.Attribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.Attribute
	 * @generated
	 */
	public Adapter createAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link eu.cessar.ct.workspace.internal.modeldocumentation.AttributeDef <em>Attribute Def</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.cessar.ct.workspace.internal.modeldocumentation.AttributeDef
	 * @generated
	 */
	public Adapter createAttributeDefAdapter() {
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
	public Adapter createEObjectAdapter() {
		return null;
	}

} //ModeldocumentationAdapterFactory

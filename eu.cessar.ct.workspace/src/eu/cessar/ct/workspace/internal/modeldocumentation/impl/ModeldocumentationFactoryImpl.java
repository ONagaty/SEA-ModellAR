/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.cessar.ct.workspace.internal.modeldocumentation.impl;

import eu.cessar.ct.workspace.internal.modeldocumentation.*;

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
public class ModeldocumentationFactoryImpl extends EFactoryImpl implements ModeldocumentationFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ModeldocumentationFactory init() {
		try {
			ModeldocumentationFactory theModeldocumentationFactory = (ModeldocumentationFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.cessar.eu.ModelDocumentation"); 
			if (theModeldocumentationFactory != null) {
				return theModeldocumentationFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ModeldocumentationFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModeldocumentationFactoryImpl() {
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
			case ModeldocumentationPackage.DOC_ELEMENT: return createDocElement();
			case ModeldocumentationPackage.DEF_ELEMENT: return createDefElement();
			case ModeldocumentationPackage.MULTIPLICITY: return createMultiplicity();
			case ModeldocumentationPackage.ATTRIBUTE: return createAttribute();
			case ModeldocumentationPackage.ATTRIBUTE_DEF: return createAttributeDef();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DocElement createDocElement() {
		DocElementImpl docElement = new DocElementImpl();
		return docElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DefElement createDefElement() {
		DefElementImpl defElement = new DefElementImpl();
		return defElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Multiplicity createMultiplicity() {
		MultiplicityImpl multiplicity = new MultiplicityImpl();
		return multiplicity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Attribute createAttribute() {
		AttributeImpl attribute = new AttributeImpl();
		return attribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AttributeDef createAttributeDef() {
		AttributeDefImpl attributeDef = new AttributeDefImpl();
		return attributeDef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModeldocumentationPackage getModeldocumentationPackage() {
		return (ModeldocumentationPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ModeldocumentationPackage getPackage() {
		return ModeldocumentationPackage.eINSTANCE;
	}

} //ModeldocumentationFactoryImpl

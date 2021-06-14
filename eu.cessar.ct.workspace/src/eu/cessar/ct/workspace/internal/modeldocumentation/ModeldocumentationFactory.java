/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.cessar.ct.workspace.internal.modeldocumentation;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage
 * @generated
 */
public interface ModeldocumentationFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ModeldocumentationFactory eINSTANCE = eu.cessar.ct.workspace.internal.modeldocumentation.impl.ModeldocumentationFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Doc Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Doc Element</em>'.
	 * @generated
	 */
	DocElement createDocElement();

	/**
	 * Returns a new object of class '<em>Def Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Def Element</em>'.
	 * @generated
	 */
	DefElement createDefElement();

	/**
	 * Returns a new object of class '<em>Multiplicity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Multiplicity</em>'.
	 * @generated
	 */
	Multiplicity createMultiplicity();

	/**
	 * Returns a new object of class '<em>Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Attribute</em>'.
	 * @generated
	 */
	Attribute createAttribute();

	/**
	 * Returns a new object of class '<em>Attribute Def</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Attribute Def</em>'.
	 * @generated
	 */
	AttributeDef createAttributeDef();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ModeldocumentationPackage getModeldocumentationPackage();

} //ModeldocumentationFactory

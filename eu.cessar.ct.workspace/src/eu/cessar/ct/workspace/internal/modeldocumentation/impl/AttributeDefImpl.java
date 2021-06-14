/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.cessar.ct.workspace.internal.modeldocumentation.impl;

import eu.cessar.ct.workspace.internal.modeldocumentation.AttributeDef;
import eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage;
import eu.cessar.ct.workspace.internal.modeldocumentation.Multiplicity;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Attribute Def</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.AttributeDefImpl#getName <em>Name</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.AttributeDefImpl#getTypeOfValue <em>Type Of Value</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.AttributeDefImpl#getMultiplicity <em>Multiplicity</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AttributeDefImpl extends EObjectImpl implements AttributeDef {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getTypeOfValue() <em>Type Of Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeOfValue()
	 * @generated
	 * @ordered
	 */
	protected static final String TYPE_OF_VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTypeOfValue() <em>Type Of Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeOfValue()
	 * @generated
	 * @ordered
	 */
	protected String typeOfValue = TYPE_OF_VALUE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMultiplicity() <em>Multiplicity</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultiplicity()
	 * @generated
	 * @ordered
	 */
	protected Multiplicity multiplicity;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AttributeDefImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModeldocumentationPackage.Literals.ATTRIBUTE_DEF;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModeldocumentationPackage.ATTRIBUTE_DEF__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTypeOfValue() {
		return typeOfValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTypeOfValue(String newTypeOfValue) {
		String oldTypeOfValue = typeOfValue;
		typeOfValue = newTypeOfValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModeldocumentationPackage.ATTRIBUTE_DEF__TYPE_OF_VALUE, oldTypeOfValue, typeOfValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Multiplicity getMultiplicity() {
		return multiplicity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMultiplicity(Multiplicity newMultiplicity, NotificationChain msgs) {
		Multiplicity oldMultiplicity = multiplicity;
		multiplicity = newMultiplicity;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModeldocumentationPackage.ATTRIBUTE_DEF__MULTIPLICITY, oldMultiplicity, newMultiplicity);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMultiplicity(Multiplicity newMultiplicity) {
		if (newMultiplicity != multiplicity) {
			NotificationChain msgs = null;
			if (multiplicity != null)
				msgs = ((InternalEObject)multiplicity).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModeldocumentationPackage.ATTRIBUTE_DEF__MULTIPLICITY, null, msgs);
			if (newMultiplicity != null)
				msgs = ((InternalEObject)newMultiplicity).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModeldocumentationPackage.ATTRIBUTE_DEF__MULTIPLICITY, null, msgs);
			msgs = basicSetMultiplicity(newMultiplicity, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModeldocumentationPackage.ATTRIBUTE_DEF__MULTIPLICITY, newMultiplicity, newMultiplicity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModeldocumentationPackage.ATTRIBUTE_DEF__MULTIPLICITY:
				return basicSetMultiplicity(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModeldocumentationPackage.ATTRIBUTE_DEF__NAME:
				return getName();
			case ModeldocumentationPackage.ATTRIBUTE_DEF__TYPE_OF_VALUE:
				return getTypeOfValue();
			case ModeldocumentationPackage.ATTRIBUTE_DEF__MULTIPLICITY:
				return getMultiplicity();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ModeldocumentationPackage.ATTRIBUTE_DEF__NAME:
				setName((String)newValue);
				return;
			case ModeldocumentationPackage.ATTRIBUTE_DEF__TYPE_OF_VALUE:
				setTypeOfValue((String)newValue);
				return;
			case ModeldocumentationPackage.ATTRIBUTE_DEF__MULTIPLICITY:
				setMultiplicity((Multiplicity)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ModeldocumentationPackage.ATTRIBUTE_DEF__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ModeldocumentationPackage.ATTRIBUTE_DEF__TYPE_OF_VALUE:
				setTypeOfValue(TYPE_OF_VALUE_EDEFAULT);
				return;
			case ModeldocumentationPackage.ATTRIBUTE_DEF__MULTIPLICITY:
				setMultiplicity((Multiplicity)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ModeldocumentationPackage.ATTRIBUTE_DEF__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ModeldocumentationPackage.ATTRIBUTE_DEF__TYPE_OF_VALUE:
				return TYPE_OF_VALUE_EDEFAULT == null ? typeOfValue != null : !TYPE_OF_VALUE_EDEFAULT.equals(typeOfValue);
			case ModeldocumentationPackage.ATTRIBUTE_DEF__MULTIPLICITY:
				return multiplicity != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", typeOfValue: ");
		result.append(typeOfValue);
		result.append(')');
		return result.toString();
	}

} //AttributeDefImpl

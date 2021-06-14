/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.cessar.ct.workspace.internal.modeldocumentation.impl;

import eu.cessar.ct.workspace.internal.modeldocumentation.Attribute;
import eu.cessar.ct.workspace.internal.modeldocumentation.AttributeDef;
import eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Attribute</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.AttributeImpl#getInstanceOf <em>Instance Of</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.AttributeImpl#getName <em>Name</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.AttributeImpl#getType <em>Type</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.AttributeImpl#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AttributeImpl extends EObjectImpl implements Attribute {
	/**
	 * The cached value of the '{@link #getInstanceOf() <em>Instance Of</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInstanceOf()
	 * @generated
	 * @ordered
	 */
	protected AttributeDef instanceOf;

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
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final String TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected String type = TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final String VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected String value = VALUE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AttributeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModeldocumentationPackage.Literals.ATTRIBUTE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AttributeDef getInstanceOf() {
		return instanceOf;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetInstanceOf(AttributeDef newInstanceOf, NotificationChain msgs) {
		AttributeDef oldInstanceOf = instanceOf;
		instanceOf = newInstanceOf;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModeldocumentationPackage.ATTRIBUTE__INSTANCE_OF, oldInstanceOf, newInstanceOf);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInstanceOf(AttributeDef newInstanceOf) {
		if (newInstanceOf != instanceOf) {
			NotificationChain msgs = null;
			if (instanceOf != null)
				msgs = ((InternalEObject)instanceOf).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModeldocumentationPackage.ATTRIBUTE__INSTANCE_OF, null, msgs);
			if (newInstanceOf != null)
				msgs = ((InternalEObject)newInstanceOf).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModeldocumentationPackage.ATTRIBUTE__INSTANCE_OF, null, msgs);
			msgs = basicSetInstanceOf(newInstanceOf, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModeldocumentationPackage.ATTRIBUTE__INSTANCE_OF, newInstanceOf, newInstanceOf));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ModeldocumentationPackage.ATTRIBUTE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(String newType) {
		String oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModeldocumentationPackage.ATTRIBUTE__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValue(String newValue) {
		String oldValue = value;
		value = newValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModeldocumentationPackage.ATTRIBUTE__VALUE, oldValue, value));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModeldocumentationPackage.ATTRIBUTE__INSTANCE_OF:
				return basicSetInstanceOf(null, msgs);
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
			case ModeldocumentationPackage.ATTRIBUTE__INSTANCE_OF:
				return getInstanceOf();
			case ModeldocumentationPackage.ATTRIBUTE__NAME:
				return getName();
			case ModeldocumentationPackage.ATTRIBUTE__TYPE:
				return getType();
			case ModeldocumentationPackage.ATTRIBUTE__VALUE:
				return getValue();
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
			case ModeldocumentationPackage.ATTRIBUTE__INSTANCE_OF:
				setInstanceOf((AttributeDef)newValue);
				return;
			case ModeldocumentationPackage.ATTRIBUTE__NAME:
				setName((String)newValue);
				return;
			case ModeldocumentationPackage.ATTRIBUTE__TYPE:
				setType((String)newValue);
				return;
			case ModeldocumentationPackage.ATTRIBUTE__VALUE:
				setValue((String)newValue);
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
			case ModeldocumentationPackage.ATTRIBUTE__INSTANCE_OF:
				setInstanceOf((AttributeDef)null);
				return;
			case ModeldocumentationPackage.ATTRIBUTE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ModeldocumentationPackage.ATTRIBUTE__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case ModeldocumentationPackage.ATTRIBUTE__VALUE:
				setValue(VALUE_EDEFAULT);
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
			case ModeldocumentationPackage.ATTRIBUTE__INSTANCE_OF:
				return instanceOf != null;
			case ModeldocumentationPackage.ATTRIBUTE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ModeldocumentationPackage.ATTRIBUTE__TYPE:
				return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
			case ModeldocumentationPackage.ATTRIBUTE__VALUE:
				return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
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
		result.append(", type: ");
		result.append(type);
		result.append(", value: ");
		result.append(value);
		result.append(')');
		return result.toString();
	}

} //AttributeImpl

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.cessar.ct.workspace.internal.modeldocumentation.impl;

import eu.cessar.ct.workspace.internal.modeldocumentation.Attribute;
import eu.cessar.ct.workspace.internal.modeldocumentation.DefElement;
import eu.cessar.ct.workspace.internal.modeldocumentation.DocElement;
import eu.cessar.ct.workspace.internal.modeldocumentation.ModeldocumentationPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Doc Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.DocElementImpl#getInstanceOf <em>Instance Of</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.DocElementImpl#getAttributes <em>Attributes</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.DocElementImpl#getContains <em>Contains</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.DocElementImpl#getShortName <em>Short Name</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.DocElementImpl#getType <em>Type</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.DocElementImpl#isIsInstance <em>Is Instance</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DocElementImpl extends EObjectImpl implements DocElement {
	/**
	 * The cached value of the '{@link #getInstanceOf() <em>Instance Of</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInstanceOf()
	 * @generated
	 * @ordered
	 */
	protected DefElement instanceOf;

	/**
	 * The cached value of the '{@link #getAttributes() <em>Attributes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttributes()
	 * @generated
	 * @ordered
	 */
	protected EList<Attribute> attributes;

	/**
	 * The cached value of the '{@link #getContains() <em>Contains</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContains()
	 * @generated
	 * @ordered
	 */
	protected EList<DocElement> contains;

	/**
	 * The default value of the '{@link #getShortName() <em>Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShortName()
	 * @generated
	 * @ordered
	 */
	protected static final String SHORT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getShortName() <em>Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShortName()
	 * @generated
	 * @ordered
	 */
	protected String shortName = SHORT_NAME_EDEFAULT;

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
	 * The default value of the '{@link #isIsInstance() <em>Is Instance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsInstance()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_INSTANCE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsInstance() <em>Is Instance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsInstance()
	 * @generated
	 * @ordered
	 */
	protected boolean isInstance = IS_INSTANCE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DocElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModeldocumentationPackage.Literals.DOC_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DefElement getInstanceOf() {
		return instanceOf;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetInstanceOf(DefElement newInstanceOf, NotificationChain msgs) {
		DefElement oldInstanceOf = instanceOf;
		instanceOf = newInstanceOf;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModeldocumentationPackage.DOC_ELEMENT__INSTANCE_OF, oldInstanceOf, newInstanceOf);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInstanceOf(DefElement newInstanceOf) {
		if (newInstanceOf != instanceOf) {
			NotificationChain msgs = null;
			if (instanceOf != null)
				msgs = ((InternalEObject)instanceOf).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModeldocumentationPackage.DOC_ELEMENT__INSTANCE_OF, null, msgs);
			if (newInstanceOf != null)
				msgs = ((InternalEObject)newInstanceOf).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModeldocumentationPackage.DOC_ELEMENT__INSTANCE_OF, null, msgs);
			msgs = basicSetInstanceOf(newInstanceOf, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModeldocumentationPackage.DOC_ELEMENT__INSTANCE_OF, newInstanceOf, newInstanceOf));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Attribute> getAttributes() {
		if (attributes == null) {
			attributes = new EObjectContainmentEList<Attribute>(Attribute.class, this, ModeldocumentationPackage.DOC_ELEMENT__ATTRIBUTES);
		}
		return attributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DocElement> getContains() {
		if (contains == null) {
			contains = new EObjectContainmentEList<DocElement>(DocElement.class, this, ModeldocumentationPackage.DOC_ELEMENT__CONTAINS);
		}
		return contains;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setShortName(String newShortName) {
		String oldShortName = shortName;
		shortName = newShortName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModeldocumentationPackage.DOC_ELEMENT__SHORT_NAME, oldShortName, shortName));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ModeldocumentationPackage.DOC_ELEMENT__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsInstance() {
		return isInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsInstance(boolean newIsInstance) {
		boolean oldIsInstance = isInstance;
		isInstance = newIsInstance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModeldocumentationPackage.DOC_ELEMENT__IS_INSTANCE, oldIsInstance, isInstance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModeldocumentationPackage.DOC_ELEMENT__INSTANCE_OF:
				return basicSetInstanceOf(null, msgs);
			case ModeldocumentationPackage.DOC_ELEMENT__ATTRIBUTES:
				return ((InternalEList<?>)getAttributes()).basicRemove(otherEnd, msgs);
			case ModeldocumentationPackage.DOC_ELEMENT__CONTAINS:
				return ((InternalEList<?>)getContains()).basicRemove(otherEnd, msgs);
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
			case ModeldocumentationPackage.DOC_ELEMENT__INSTANCE_OF:
				return getInstanceOf();
			case ModeldocumentationPackage.DOC_ELEMENT__ATTRIBUTES:
				return getAttributes();
			case ModeldocumentationPackage.DOC_ELEMENT__CONTAINS:
				return getContains();
			case ModeldocumentationPackage.DOC_ELEMENT__SHORT_NAME:
				return getShortName();
			case ModeldocumentationPackage.DOC_ELEMENT__TYPE:
				return getType();
			case ModeldocumentationPackage.DOC_ELEMENT__IS_INSTANCE:
				return isIsInstance();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ModeldocumentationPackage.DOC_ELEMENT__INSTANCE_OF:
				setInstanceOf((DefElement)newValue);
				return;
			case ModeldocumentationPackage.DOC_ELEMENT__ATTRIBUTES:
				getAttributes().clear();
				getAttributes().addAll((Collection<? extends Attribute>)newValue);
				return;
			case ModeldocumentationPackage.DOC_ELEMENT__CONTAINS:
				getContains().clear();
				getContains().addAll((Collection<? extends DocElement>)newValue);
				return;
			case ModeldocumentationPackage.DOC_ELEMENT__SHORT_NAME:
				setShortName((String)newValue);
				return;
			case ModeldocumentationPackage.DOC_ELEMENT__TYPE:
				setType((String)newValue);
				return;
			case ModeldocumentationPackage.DOC_ELEMENT__IS_INSTANCE:
				setIsInstance((Boolean)newValue);
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
			case ModeldocumentationPackage.DOC_ELEMENT__INSTANCE_OF:
				setInstanceOf((DefElement)null);
				return;
			case ModeldocumentationPackage.DOC_ELEMENT__ATTRIBUTES:
				getAttributes().clear();
				return;
			case ModeldocumentationPackage.DOC_ELEMENT__CONTAINS:
				getContains().clear();
				return;
			case ModeldocumentationPackage.DOC_ELEMENT__SHORT_NAME:
				setShortName(SHORT_NAME_EDEFAULT);
				return;
			case ModeldocumentationPackage.DOC_ELEMENT__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case ModeldocumentationPackage.DOC_ELEMENT__IS_INSTANCE:
				setIsInstance(IS_INSTANCE_EDEFAULT);
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
			case ModeldocumentationPackage.DOC_ELEMENT__INSTANCE_OF:
				return instanceOf != null;
			case ModeldocumentationPackage.DOC_ELEMENT__ATTRIBUTES:
				return attributes != null && !attributes.isEmpty();
			case ModeldocumentationPackage.DOC_ELEMENT__CONTAINS:
				return contains != null && !contains.isEmpty();
			case ModeldocumentationPackage.DOC_ELEMENT__SHORT_NAME:
				return SHORT_NAME_EDEFAULT == null ? shortName != null : !SHORT_NAME_EDEFAULT.equals(shortName);
			case ModeldocumentationPackage.DOC_ELEMENT__TYPE:
				return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
			case ModeldocumentationPackage.DOC_ELEMENT__IS_INSTANCE:
				return isInstance != IS_INSTANCE_EDEFAULT;
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
		result.append(" (shortName: ");
		result.append(shortName);
		result.append(", type: ");
		result.append(type);
		result.append(", isInstance: ");
		result.append(isInstance);
		result.append(')');
		return result.toString();
	}

} //DocElementImpl

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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Multiplicity</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.MultiplicityImpl#getLower <em>Lower</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.MultiplicityImpl#getUpper <em>Upper</em>}</li>
 *   <li>{@link eu.cessar.ct.workspace.internal.modeldocumentation.impl.MultiplicityImpl#getMultiplicity <em>Multiplicity</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MultiplicityImpl extends EObjectImpl implements Multiplicity {
	/**
	 * The default value of the '{@link #getLower() <em>Lower</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLower()
	 * @generated
	 * @ordered
	 */
	protected static final String LOWER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLower() <em>Lower</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLower()
	 * @generated
	 * @ordered
	 */
	protected String lower = LOWER_EDEFAULT;

	/**
	 * The default value of the '{@link #getUpper() <em>Upper</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpper()
	 * @generated
	 * @ordered
	 */
	protected static final String UPPER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUpper() <em>Upper</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpper()
	 * @generated
	 * @ordered
	 */
	protected String upper = UPPER_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMultiplicity() <em>Multiplicity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultiplicity()
	 * @generated
	 * @ordered
	 */
	protected AttributeDef multiplicity;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MultiplicityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModeldocumentationPackage.Literals.MULTIPLICITY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLower() {
		return lower;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLower(String newLower) {
		String oldLower = lower;
		lower = newLower;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModeldocumentationPackage.MULTIPLICITY__LOWER, oldLower, lower));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUpper() {
		return upper;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUpper(String newUpper) {
		String oldUpper = upper;
		upper = newUpper;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModeldocumentationPackage.MULTIPLICITY__UPPER, oldUpper, upper));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AttributeDef getMultiplicity() {
		if (multiplicity != null && multiplicity.eIsProxy()) {
			InternalEObject oldMultiplicity = (InternalEObject)multiplicity;
			multiplicity = (AttributeDef)eResolveProxy(oldMultiplicity);
			if (multiplicity != oldMultiplicity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ModeldocumentationPackage.MULTIPLICITY__MULTIPLICITY, oldMultiplicity, multiplicity));
			}
		}
		return multiplicity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AttributeDef basicGetMultiplicity() {
		return multiplicity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMultiplicity(AttributeDef newMultiplicity) {
		AttributeDef oldMultiplicity = multiplicity;
		multiplicity = newMultiplicity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModeldocumentationPackage.MULTIPLICITY__MULTIPLICITY, oldMultiplicity, multiplicity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModeldocumentationPackage.MULTIPLICITY__LOWER:
				return getLower();
			case ModeldocumentationPackage.MULTIPLICITY__UPPER:
				return getUpper();
			case ModeldocumentationPackage.MULTIPLICITY__MULTIPLICITY:
				if (resolve) return getMultiplicity();
				return basicGetMultiplicity();
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
			case ModeldocumentationPackage.MULTIPLICITY__LOWER:
				setLower((String)newValue);
				return;
			case ModeldocumentationPackage.MULTIPLICITY__UPPER:
				setUpper((String)newValue);
				return;
			case ModeldocumentationPackage.MULTIPLICITY__MULTIPLICITY:
				setMultiplicity((AttributeDef)newValue);
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
			case ModeldocumentationPackage.MULTIPLICITY__LOWER:
				setLower(LOWER_EDEFAULT);
				return;
			case ModeldocumentationPackage.MULTIPLICITY__UPPER:
				setUpper(UPPER_EDEFAULT);
				return;
			case ModeldocumentationPackage.MULTIPLICITY__MULTIPLICITY:
				setMultiplicity((AttributeDef)null);
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
			case ModeldocumentationPackage.MULTIPLICITY__LOWER:
				return LOWER_EDEFAULT == null ? lower != null : !LOWER_EDEFAULT.equals(lower);
			case ModeldocumentationPackage.MULTIPLICITY__UPPER:
				return UPPER_EDEFAULT == null ? upper != null : !UPPER_EDEFAULT.equals(upper);
			case ModeldocumentationPackage.MULTIPLICITY__MULTIPLICITY:
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
		result.append(" (lower: ");
		result.append(lower);
		result.append(", upper: ");
		result.append(upper);
		result.append(')');
		return result.toString();
	}

} //MultiplicityImpl

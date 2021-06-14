/**
 */
package eu.cessar.ct.cid.model.versioning.impl;

import eu.cessar.ct.cid.model.CIDEObject;
import eu.cessar.ct.cid.model.versioning.Limit;
import eu.cessar.ct.cid.model.versioning.VersioningPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Limit</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.cessar.ct.cid.model.versioning.impl.LimitImpl#isIncluding <em>Including</em>}</li>
 *   <li>{@link eu.cessar.ct.cid.model.versioning.impl.LimitImpl#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LimitImpl extends CIDEObject implements Limit
{
	/**
	 * The default value of the '{@link #isIncluding() <em>Including</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIncluding()
	 * @generated
	 * @ordered
	 */
	protected static final boolean INCLUDING_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIncluding() <em>Including</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIncluding()
	 * @generated
	 * @ordered
	 */
	protected boolean including = INCLUDING_EDEFAULT;

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
	protected LimitImpl()
	{
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass()
	{
		return VersioningPackage.Literals.LIMIT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIncluding()
	{
		return including;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIncluding(boolean newIncluding)
	{
		boolean oldIncluding = including;
		including = newIncluding;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, VersioningPackage.LIMIT__INCLUDING, oldIncluding,
				including));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValue(String newValue)
	{
		String oldValue = value;
		value = newValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, VersioningPackage.LIMIT__VALUE, oldValue, value));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType)
	{
		switch (featureID)
		{
			case VersioningPackage.LIMIT__INCLUDING:
				return isIncluding();
			case VersioningPackage.LIMIT__VALUE:
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
	public void eSet(int featureID, Object newValue)
	{
		switch (featureID)
		{
			case VersioningPackage.LIMIT__INCLUDING:
				setIncluding((Boolean) newValue);
				return;
			case VersioningPackage.LIMIT__VALUE:
				setValue((String) newValue);
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
	public void eUnset(int featureID)
	{
		switch (featureID)
		{
			case VersioningPackage.LIMIT__INCLUDING:
				setIncluding(INCLUDING_EDEFAULT);
				return;
			case VersioningPackage.LIMIT__VALUE:
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
	public boolean eIsSet(int featureID)
	{
		switch (featureID)
		{
			case VersioningPackage.LIMIT__INCLUDING:
				return including != INCLUDING_EDEFAULT;
			case VersioningPackage.LIMIT__VALUE:
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
	public String toString()
	{
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (including: ");
		result.append(including);
		result.append(", value: ");
		result.append(value);
		result.append(')');
		return result.toString();
	}

} //LimitImpl

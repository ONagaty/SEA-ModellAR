/**
 */
package eu.cessar.ct.cid.model.versioning.impl;

import eu.cessar.ct.cid.model.CIDEObject;
import eu.cessar.ct.cid.model.versioning.Limit;
import eu.cessar.ct.cid.model.versioning.VersionRange;
import eu.cessar.ct.cid.model.versioning.VersioningPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Version Range</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.cessar.ct.cid.model.versioning.impl.VersionRangeImpl#getFrom <em>From</em>}</li>
 *   <li>{@link eu.cessar.ct.cid.model.versioning.impl.VersionRangeImpl#getTo <em>To</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VersionRangeImpl extends CIDEObject implements VersionRange
{
	/**
	 * The cached value of the '{@link #getFrom() <em>From</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFrom()
	 * @generated
	 * @ordered
	 */
	protected Limit from;

	/**
	 * The cached value of the '{@link #getTo() <em>To</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTo()
	 * @generated
	 * @ordered
	 */
	protected Limit to;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VersionRangeImpl()
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
		return VersioningPackage.Literals.VERSION_RANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Limit getFrom()
	{
		return from;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFrom(Limit newFrom, NotificationChain msgs)
	{
		Limit oldFrom = from;
		from = newFrom;
		if (eNotificationRequired())
		{
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
				VersioningPackage.VERSION_RANGE__FROM, oldFrom, newFrom);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFrom(Limit newFrom)
	{
		if (newFrom != from)
		{
			NotificationChain msgs = null;
			if (from != null)
				msgs = ((InternalEObject) from).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
					- VersioningPackage.VERSION_RANGE__FROM, null, msgs);
			if (newFrom != null)
				msgs = ((InternalEObject) newFrom).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
					- VersioningPackage.VERSION_RANGE__FROM, null, msgs);
			msgs = basicSetFrom(newFrom, msgs);
			if (msgs != null)
				msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, VersioningPackage.VERSION_RANGE__FROM, newFrom,
				newFrom));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Limit getTo()
	{
		return to;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTo(Limit newTo, NotificationChain msgs)
	{
		Limit oldTo = to;
		to = newTo;
		if (eNotificationRequired())
		{
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
				VersioningPackage.VERSION_RANGE__TO, oldTo, newTo);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTo(Limit newTo)
	{
		if (newTo != to)
		{
			NotificationChain msgs = null;
			if (to != null)
				msgs = ((InternalEObject) to).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
					- VersioningPackage.VERSION_RANGE__TO, null, msgs);
			if (newTo != null)
				msgs = ((InternalEObject) newTo).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
					- VersioningPackage.VERSION_RANGE__TO, null, msgs);
			msgs = basicSetTo(newTo, msgs);
			if (msgs != null)
				msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, VersioningPackage.VERSION_RANGE__TO, newTo, newTo));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
	{
		switch (featureID)
		{
			case VersioningPackage.VERSION_RANGE__FROM:
				return basicSetFrom(null, msgs);
			case VersioningPackage.VERSION_RANGE__TO:
				return basicSetTo(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
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
			case VersioningPackage.VERSION_RANGE__FROM:
				return getFrom();
			case VersioningPackage.VERSION_RANGE__TO:
				return getTo();
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
			case VersioningPackage.VERSION_RANGE__FROM:
				setFrom((Limit) newValue);
				return;
			case VersioningPackage.VERSION_RANGE__TO:
				setTo((Limit) newValue);
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
			case VersioningPackage.VERSION_RANGE__FROM:
				setFrom((Limit) null);
				return;
			case VersioningPackage.VERSION_RANGE__TO:
				setTo((Limit) null);
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
			case VersioningPackage.VERSION_RANGE__FROM:
				return from != null;
			case VersioningPackage.VERSION_RANGE__TO:
				return to != null;
		}
		return super.eIsSet(featureID);
	}

} //VersionRangeImpl

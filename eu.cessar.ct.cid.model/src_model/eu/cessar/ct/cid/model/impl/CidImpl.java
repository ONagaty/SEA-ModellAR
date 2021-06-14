/**
 */
package eu.cessar.ct.cid.model.impl;

import eu.cessar.ct.cid.model.Artifact;
import eu.cessar.ct.cid.model.CIDEObject;
import eu.cessar.ct.cid.model.Cid;
import eu.cessar.ct.cid.model.Delivery;
import eu.cessar.ct.cid.model.ModelPackage;
import eu.cessar.ct.cid.model.internal.util.CidServices;
import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cid</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.cessar.ct.cid.model.impl.CidImpl#getDeliveries <em>Deliveries</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CidImpl extends CIDEObject implements Cid
{
	/**
	 * The cached value of the '{@link #getDeliveries() <em>Deliveries</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeliveries()
	 * @generated
	 * @ordered
	 */
	protected EList<Delivery> deliveries;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CidImpl()
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
		return ModelPackage.Literals.CID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Delivery> getDeliveries()
	{
		if (deliveries == null)
		{
			deliveries = new EObjectContainmentEList<Delivery>(Delivery.class, this, ModelPackage.CID__DELIVERIES);
		}
		return deliveries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<Artifact> getArtifacts()
	{
		return CidServices.getArtifacts(this);

	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<Artifact> getArtifacts(final String type)
	{
		return CidServices.getArtifacts(this, type);

	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<Artifact> getArtifacts(final String type, final String name)
	{
		return CidServices.getArtifacts(this, type, name);

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
			case ModelPackage.CID__DELIVERIES:
				return ((InternalEList<?>) getDeliveries()).basicRemove(otherEnd, msgs);
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
			case ModelPackage.CID__DELIVERIES:
				return getDeliveries();
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
	public void eSet(int featureID, Object newValue)
	{
		switch (featureID)
		{
			case ModelPackage.CID__DELIVERIES:
				getDeliveries().clear();
				getDeliveries().addAll((Collection<? extends Delivery>) newValue);
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
			case ModelPackage.CID__DELIVERIES:
				getDeliveries().clear();
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
			case ModelPackage.CID__DELIVERIES:
				return deliveries != null && !deliveries.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //CidImpl

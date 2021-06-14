/**
 */
package eu.cessar.ct.cid.model.elements.impl;

import eu.cessar.ct.cid.model.CIDEObject;
import eu.cessar.ct.cid.model.Dependency;
import eu.cessar.ct.cid.model.Metadata;
import eu.cessar.ct.cid.model.elements.DependantElement;
import eu.cessar.ct.cid.model.elements.ElementsPackage;
import eu.cessar.ct.cid.model.internal.util.DependantElementServices;
import java.util.Collection;
import java.util.List;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dependant Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.cessar.ct.cid.model.elements.impl.DependantElementImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link eu.cessar.ct.cid.model.elements.impl.DependantElementImpl#getDependencies <em>Dependencies</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class DependantElementImpl extends CIDEObject implements DependantElement
{
	/**
	 * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetadata()
	 * @generated
	 * @ordered
	 */
	protected Metadata metadata;

	/**
	 * The cached value of the '{@link #getDependencies() <em>Dependencies</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDependencies()
	 * @generated
	 * @ordered
	 */
	protected EList<Dependency> dependencies;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DependantElementImpl()
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
		return ElementsPackage.Literals.DEPENDANT_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Metadata getMetadata()
	{
		return metadata;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMetadata(Metadata newMetadata, NotificationChain msgs)
	{
		Metadata oldMetadata = metadata;
		metadata = newMetadata;
		if (eNotificationRequired())
		{
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
				ElementsPackage.DEPENDANT_ELEMENT__METADATA, oldMetadata, newMetadata);
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
	public void setMetadata(Metadata newMetadata)
	{
		if (newMetadata != metadata)
		{
			NotificationChain msgs = null;
			if (metadata != null)
				msgs = ((InternalEObject) metadata).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
					- ElementsPackage.DEPENDANT_ELEMENT__METADATA, null, msgs);
			if (newMetadata != null)
				msgs = ((InternalEObject) newMetadata).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
					- ElementsPackage.DEPENDANT_ELEMENT__METADATA, null, msgs);
			msgs = basicSetMetadata(newMetadata, msgs);
			if (msgs != null)
				msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ElementsPackage.DEPENDANT_ELEMENT__METADATA,
				newMetadata, newMetadata));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Dependency> getDependencies()
	{
		if (dependencies == null)
		{
			dependencies = new EObjectContainmentEList<Dependency>(Dependency.class, this,
				ElementsPackage.DEPENDANT_ELEMENT__DEPENDENCIES);
		}
		return dependencies;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IStatus evaluateDependencies()
	{
		return DependantElementServices.evaluateDependencies(this);

	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<Dependency> getDependencies(final String type)
	{
		return DependantElementServices.getDependencies(this, type);

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
			case ElementsPackage.DEPENDANT_ELEMENT__METADATA:
				return basicSetMetadata(null, msgs);
			case ElementsPackage.DEPENDANT_ELEMENT__DEPENDENCIES:
				return ((InternalEList<?>) getDependencies()).basicRemove(otherEnd, msgs);
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
			case ElementsPackage.DEPENDANT_ELEMENT__METADATA:
				return getMetadata();
			case ElementsPackage.DEPENDANT_ELEMENT__DEPENDENCIES:
				return getDependencies();
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
			case ElementsPackage.DEPENDANT_ELEMENT__METADATA:
				setMetadata((Metadata) newValue);
				return;
			case ElementsPackage.DEPENDANT_ELEMENT__DEPENDENCIES:
				getDependencies().clear();
				getDependencies().addAll((Collection<? extends Dependency>) newValue);
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
			case ElementsPackage.DEPENDANT_ELEMENT__METADATA:
				setMetadata((Metadata) null);
				return;
			case ElementsPackage.DEPENDANT_ELEMENT__DEPENDENCIES:
				getDependencies().clear();
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
			case ElementsPackage.DEPENDANT_ELEMENT__METADATA:
				return metadata != null;
			case ElementsPackage.DEPENDANT_ELEMENT__DEPENDENCIES:
				return dependencies != null && !dependencies.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //DependantElementImpl

/**
 */
package eu.cessar.ct.cid.model.impl;

import eu.cessar.ct.cid.model.Artifact;
import eu.cessar.ct.cid.model.Delivery;
import eu.cessar.ct.cid.model.ModelPackage;
import eu.cessar.ct.cid.model.Property;

import eu.cessar.ct.cid.model.elements.ElementsPackage;
import eu.cessar.ct.cid.model.elements.NamedElement;
import eu.cessar.ct.cid.model.elements.PropertiesElement;

import eu.cessar.ct.cid.model.elements.impl.DependantElementImpl;

import eu.cessar.ct.cid.model.internal.util.DeliveryServices;
import java.util.Collection;
import java.util.List;

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
 * An implementation of the model object '<em><b>Delivery</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.cessar.ct.cid.model.impl.DeliveryImpl#getName <em>Name</em>}</li>
 *   <li>{@link eu.cessar.ct.cid.model.impl.DeliveryImpl#getProperties <em>Properties</em>}</li>
 *   <li>{@link eu.cessar.ct.cid.model.impl.DeliveryImpl#getArtifacts <em>Artifacts</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DeliveryImpl extends DependantElementImpl implements Delivery
{
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
	 * The cached value of the '{@link #getProperties() <em>Properties</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProperties()
	 * @generated
	 * @ordered
	 */
	protected EList<Property> properties;

	/**
	 * The cached value of the '{@link #getArtifacts() <em>Artifacts</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArtifacts()
	 * @generated
	 * @ordered
	 */
	protected EList<Artifact> artifacts;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeliveryImpl()
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
		return ModelPackage.Literals.DELIVERY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName)
	{
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DELIVERY__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Property> getProperties()
	{
		if (properties == null)
		{
			properties = new EObjectContainmentEList<Property>(Property.class, this, ModelPackage.DELIVERY__PROPERTIES);
		}
		return properties;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Artifact> getArtifacts()
	{
		if (artifacts == null)
		{
			artifacts = new EObjectContainmentEList<Artifact>(Artifact.class, this, ModelPackage.DELIVERY__ARTIFACTS);
		}
		return artifacts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<Artifact> getArtifacts(final String type)
	{
		return DeliveryServices.getArtifacts(this, type);

	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<Artifact> getArtifacts(final String type, final String name)
	{
		return DeliveryServices.getArtifacts(this, type, name);

	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<Property> getProperties(final String name)
	{
		return eu.cessar.ct.cid.model.internal.util.PropertiesElementServices.getProperties(this, name);

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
			case ModelPackage.DELIVERY__PROPERTIES:
				return ((InternalEList<?>) getProperties()).basicRemove(otherEnd, msgs);
			case ModelPackage.DELIVERY__ARTIFACTS:
				return ((InternalEList<?>) getArtifacts()).basicRemove(otherEnd, msgs);
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
			case ModelPackage.DELIVERY__NAME:
				return getName();
			case ModelPackage.DELIVERY__PROPERTIES:
				return getProperties();
			case ModelPackage.DELIVERY__ARTIFACTS:
				return getArtifacts();
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
			case ModelPackage.DELIVERY__NAME:
				setName((String) newValue);
				return;
			case ModelPackage.DELIVERY__PROPERTIES:
				getProperties().clear();
				getProperties().addAll((Collection<? extends Property>) newValue);
				return;
			case ModelPackage.DELIVERY__ARTIFACTS:
				getArtifacts().clear();
				getArtifacts().addAll((Collection<? extends Artifact>) newValue);
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
			case ModelPackage.DELIVERY__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ModelPackage.DELIVERY__PROPERTIES:
				getProperties().clear();
				return;
			case ModelPackage.DELIVERY__ARTIFACTS:
				getArtifacts().clear();
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
			case ModelPackage.DELIVERY__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ModelPackage.DELIVERY__PROPERTIES:
				return properties != null && !properties.isEmpty();
			case ModelPackage.DELIVERY__ARTIFACTS:
				return artifacts != null && !artifacts.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass)
	{
		if (baseClass == NamedElement.class)
		{
			switch (derivedFeatureID)
			{
				case ModelPackage.DELIVERY__NAME:
					return ElementsPackage.NAMED_ELEMENT__NAME;
				default:
					return -1;
			}
		}
		if (baseClass == PropertiesElement.class)
		{
			switch (derivedFeatureID)
			{
				case ModelPackage.DELIVERY__PROPERTIES:
					return ElementsPackage.PROPERTIES_ELEMENT__PROPERTIES;
				default:
					return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass)
	{
		if (baseClass == NamedElement.class)
		{
			switch (baseFeatureID)
			{
				case ElementsPackage.NAMED_ELEMENT__NAME:
					return ModelPackage.DELIVERY__NAME;
				default:
					return -1;
			}
		}
		if (baseClass == PropertiesElement.class)
		{
			switch (baseFeatureID)
			{
				case ElementsPackage.PROPERTIES_ELEMENT__PROPERTIES:
					return ModelPackage.DELIVERY__PROPERTIES;
				default:
					return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //DeliveryImpl

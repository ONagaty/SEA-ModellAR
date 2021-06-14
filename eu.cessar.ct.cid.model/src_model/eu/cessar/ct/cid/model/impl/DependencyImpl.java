/**
 */
package eu.cessar.ct.cid.model.impl;

import eu.cessar.ct.cid.model.Dependency;
import eu.cessar.ct.cid.model.IDependencyBinding;
import eu.cessar.ct.cid.model.ModelPackage;
import eu.cessar.ct.cid.model.Property;

import eu.cessar.ct.cid.model.elements.ElementsPackage;
import eu.cessar.ct.cid.model.elements.PropertiesElement;

import eu.cessar.ct.cid.model.elements.impl.TypedElementImpl;

import eu.cessar.ct.cid.model.internal.util.DependencyServices;
import eu.cessar.ct.cid.model.versioning.VersionRange;

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
 * An implementation of the model object '<em><b>Dependency</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.cessar.ct.cid.model.impl.DependencyImpl#getProperties <em>Properties</em>}</li>
 *   <li>{@link eu.cessar.ct.cid.model.impl.DependencyImpl#isMandatory <em>Mandatory</em>}</li>
 *   <li>{@link eu.cessar.ct.cid.model.impl.DependencyImpl#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DependencyImpl extends TypedElementImpl implements Dependency
{
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
	 * The default value of the '{@link #isMandatory() <em>Mandatory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMandatory()
	 * @generated
	 * @ordered
	 */
	protected static final boolean MANDATORY_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isMandatory() <em>Mandatory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMandatory()
	 * @generated
	 * @ordered
	 */
	protected boolean mandatory = MANDATORY_EDEFAULT;

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected VersionRange version;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DependencyImpl()
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
		return ModelPackage.Literals.DEPENDENCY;
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
			properties = new EObjectContainmentEList<Property>(Property.class, this,
				ModelPackage.DEPENDENCY__PROPERTIES);
		}
		return properties;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isMandatory()
	{
		return mandatory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMandatory(boolean newMandatory)
	{
		boolean oldMandatory = mandatory;
		mandatory = newMandatory;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DEPENDENCY__MANDATORY, oldMandatory,
				mandatory));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VersionRange getVersion()
	{
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetVersion(VersionRange newVersion, NotificationChain msgs)
	{
		VersionRange oldVersion = version;
		version = newVersion;
		if (eNotificationRequired())
		{
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
				ModelPackage.DEPENDENCY__VERSION, oldVersion, newVersion);
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
	public void setVersion(VersionRange newVersion)
	{
		if (newVersion != version)
		{
			NotificationChain msgs = null;
			if (version != null)
				msgs = ((InternalEObject) version).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
					- ModelPackage.DEPENDENCY__VERSION, null, msgs);
			if (newVersion != null)
				msgs = ((InternalEObject) newVersion).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
					- ModelPackage.DEPENDENCY__VERSION, null, msgs);
			msgs = basicSetVersion(newVersion, msgs);
			if (msgs != null)
				msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DEPENDENCY__VERSION, newVersion,
				newVersion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IDependencyBinding getConcreteBinding()
	{
		IDependencyBinding binding = (IDependencyBinding) getField(BINDING_FLAG);
		if (binding == null)
		{
			binding = DependencyServices.getConcreteBinding(this);
			setField(BINDING_FLAG, binding);
		}
		return binding;

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
			case ModelPackage.DEPENDENCY__PROPERTIES:
				return ((InternalEList<?>) getProperties()).basicRemove(otherEnd, msgs);
			case ModelPackage.DEPENDENCY__VERSION:
				return basicSetVersion(null, msgs);
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
			case ModelPackage.DEPENDENCY__PROPERTIES:
				return getProperties();
			case ModelPackage.DEPENDENCY__MANDATORY:
				return isMandatory();
			case ModelPackage.DEPENDENCY__VERSION:
				return getVersion();
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
			case ModelPackage.DEPENDENCY__PROPERTIES:
				getProperties().clear();
				getProperties().addAll((Collection<? extends Property>) newValue);
				return;
			case ModelPackage.DEPENDENCY__MANDATORY:
				setMandatory((Boolean) newValue);
				return;
			case ModelPackage.DEPENDENCY__VERSION:
				setVersion((VersionRange) newValue);
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
			case ModelPackage.DEPENDENCY__PROPERTIES:
				getProperties().clear();
				return;
			case ModelPackage.DEPENDENCY__MANDATORY:
				setMandatory(MANDATORY_EDEFAULT);
				return;
			case ModelPackage.DEPENDENCY__VERSION:
				setVersion((VersionRange) null);
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
			case ModelPackage.DEPENDENCY__PROPERTIES:
				return properties != null && !properties.isEmpty();
			case ModelPackage.DEPENDENCY__MANDATORY:
				return mandatory != MANDATORY_EDEFAULT;
			case ModelPackage.DEPENDENCY__VERSION:
				return version != null;
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
		if (baseClass == PropertiesElement.class)
		{
			switch (derivedFeatureID)
			{
				case ModelPackage.DEPENDENCY__PROPERTIES:
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
		if (baseClass == PropertiesElement.class)
		{
			switch (baseFeatureID)
			{
				case ElementsPackage.PROPERTIES_ELEMENT__PROPERTIES:
					return ModelPackage.DEPENDENCY__PROPERTIES;
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
		result.append(" (mandatory: ");
		result.append(mandatory);
		result.append(')');
		return result.toString();
	}

} //DependencyImpl

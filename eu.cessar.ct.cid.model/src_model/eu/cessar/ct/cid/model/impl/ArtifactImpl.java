/**
 */
package eu.cessar.ct.cid.model.impl;

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

import eu.cessar.ct.cid.model.Artifact;
import eu.cessar.ct.cid.model.IArtifactBinding;
import eu.cessar.ct.cid.model.ModelPackage;
import eu.cessar.ct.cid.model.Property;
import eu.cessar.ct.cid.model.elements.ElementsPackage;
import eu.cessar.ct.cid.model.elements.NamedElement;
import eu.cessar.ct.cid.model.elements.PropertiesElement;
import eu.cessar.ct.cid.model.elements.TypedElement;
import eu.cessar.ct.cid.model.elements.impl.DependantElementImpl;
import eu.cessar.ct.cid.model.internal.util.ArtifactServices;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Artifact</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.cessar.ct.cid.model.impl.ArtifactImpl#getType <em>Type</em>}</li>
 *   <li>{@link eu.cessar.ct.cid.model.impl.ArtifactImpl#getName <em>Name</em>}</li>
 *   <li>{@link eu.cessar.ct.cid.model.impl.ArtifactImpl#getProperties <em>Properties</em>}</li>
 *   <li>{@link eu.cessar.ct.cid.model.impl.ArtifactImpl#getTitle <em>Title</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ArtifactImpl extends DependantElementImpl implements Artifact
{
	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final String TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected String type = TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getProperties() <em>Properties</em>}' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getProperties()
	 * @generated
	 * @ordered
	 */
	protected EList<Property> properties;

	/**
	 * The default value of the '{@link #getTitle() <em>Title</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
	protected static final String TITLE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTitle() <em>Title</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
	protected String title = TITLE_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected ArtifactImpl()
	{
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass()
	{
		return ModelPackage.Literals.ARTIFACT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(String newType)
	{
		String oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.ARTIFACT__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName)
	{
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.ARTIFACT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Property> getProperties()
	{
		if (properties == null)
		{
			properties = new EObjectContainmentEList<Property>(Property.class, this, ModelPackage.ARTIFACT__PROPERTIES);
		}
		return properties;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setTitle(String newTitle)
	{
		String oldTitle = title;
		title = newTitle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.ARTIFACT__TITLE, oldTitle, title));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public IArtifactBinding getConcreteBinding()
	{
		IArtifactBinding binding = (IArtifactBinding) getField(BINDING_FLAG);
		if (binding == null)
		{
			binding = ArtifactServices.getConcreteBinding(this);
			setField(BINDING_FLAG, binding);
		}
		return binding;

	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public List<Property> getProperties(final String name)
	{
		return eu.cessar.ct.cid.model.internal.util.PropertiesElementServices.getProperties(this, name);

	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
	{
		switch (featureID)
		{
			case ModelPackage.ARTIFACT__PROPERTIES:
				return ((InternalEList<?>) getProperties()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType)
	{
		switch (featureID)
		{
			case ModelPackage.ARTIFACT__TYPE:
				return getType();
			case ModelPackage.ARTIFACT__NAME:
				return getName();
			case ModelPackage.ARTIFACT__PROPERTIES:
				return getProperties();
			case ModelPackage.ARTIFACT__TITLE:
				return getTitle();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue)
	{
		switch (featureID)
		{
			case ModelPackage.ARTIFACT__TYPE:
				setType((String) newValue);
				return;
			case ModelPackage.ARTIFACT__NAME:
				setName((String) newValue);
				return;
			case ModelPackage.ARTIFACT__PROPERTIES:
				getProperties().clear();
				getProperties().addAll((Collection<? extends Property>) newValue);
				return;
			case ModelPackage.ARTIFACT__TITLE:
				setTitle((String) newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID)
	{
		switch (featureID)
		{
			case ModelPackage.ARTIFACT__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case ModelPackage.ARTIFACT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ModelPackage.ARTIFACT__PROPERTIES:
				getProperties().clear();
				return;
			case ModelPackage.ARTIFACT__TITLE:
				setTitle(TITLE_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID)
	{
		switch (featureID)
		{
			case ModelPackage.ARTIFACT__TYPE:
				return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
			case ModelPackage.ARTIFACT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ModelPackage.ARTIFACT__PROPERTIES:
				return properties != null && !properties.isEmpty();
			case ModelPackage.ARTIFACT__TITLE:
				return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass)
	{
		if (baseClass == TypedElement.class)
		{
			switch (derivedFeatureID)
			{
				case ModelPackage.ARTIFACT__TYPE:
					return ElementsPackage.TYPED_ELEMENT__TYPE;
				default:
					return -1;
			}
		}
		if (baseClass == NamedElement.class)
		{
			switch (derivedFeatureID)
			{
				case ModelPackage.ARTIFACT__NAME:
					return ElementsPackage.NAMED_ELEMENT__NAME;
				default:
					return -1;
			}
		}
		if (baseClass == PropertiesElement.class)
		{
			switch (derivedFeatureID)
			{
				case ModelPackage.ARTIFACT__PROPERTIES:
					return ElementsPackage.PROPERTIES_ELEMENT__PROPERTIES;
				default:
					return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass)
	{
		if (baseClass == TypedElement.class)
		{
			switch (baseFeatureID)
			{
				case ElementsPackage.TYPED_ELEMENT__TYPE:
					return ModelPackage.ARTIFACT__TYPE;
				default:
					return -1;
			}
		}
		if (baseClass == NamedElement.class)
		{
			switch (baseFeatureID)
			{
				case ElementsPackage.NAMED_ELEMENT__NAME:
					return ModelPackage.ARTIFACT__NAME;
				default:
					return -1;
			}
		}
		if (baseClass == PropertiesElement.class)
		{
			switch (baseFeatureID)
			{
				case ElementsPackage.PROPERTIES_ELEMENT__PROPERTIES:
					return ModelPackage.ARTIFACT__PROPERTIES;
				default:
					return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString()
	{
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (type: ");
		result.append(type);
		result.append(", name: ");
		result.append(name);
		result.append(", title: ");
		result.append(title);
		result.append(')');
		return result.toString();
	}

} // ArtifactImpl

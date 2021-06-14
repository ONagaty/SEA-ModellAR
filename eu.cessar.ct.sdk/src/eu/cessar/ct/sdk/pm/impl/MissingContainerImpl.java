/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.cessar.ct.sdk.pm.impl;

import eu.cessar.ct.sdk.pm.IPMContainer;
import eu.cessar.ct.sdk.pm.MissingContainer;
import eu.cessar.ct.sdk.pm.pmPackage;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Missing Container</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class MissingContainerImpl extends EObjectImpl implements MissingContainer {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MissingContainerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return pmPackage.Literals.MISSING_CONTAINER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getShortName() {
		// it should be a proxy
		org.eclipse.emf.common.util.URI uri = eProxyURI();
		if (uri != null) {
			// return the last segment from the fragment
			String path = uri.fragment();
			if (path != null) {
				path = org.artop.aal.common.resource.AutosarURIFactory.getTrailingAbsoluteQualifiedNameSegment(path);
				if (path != null && path.length() == 0) {
					path = null;
				}
			}
			return path;
		}
		else {
			return null;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetShortName() {
		return getShortName() != null;
		
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IPMContainer asCompatibleContainer(String qualifiedName) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Boolean isCompatibleWith(String qualifiedName) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

} //MissingContainerImpl

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.cessar.ct.sdk.pm;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Missing Container</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see eu.cessar.ct.sdk.pm.pmPackage#getMissingContainer()
 * @model
 * @generated
 */
public interface MissingContainer extends IPMContainer
{

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Return the shortname of the container from the proxy URI
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='// it should be a proxy\r\norg.eclipse.emf.common.util.URI uri = eProxyURI();\r\nif (uri != null)\r\n{\r\n\t// return the last segment from the fragment\r\n\tString path = uri.fragment();\r\n\tif (path != null)\r\n\t{\r\n\t\tpath = org.artop.aal.common.resource.AutosarURIFactory.getTrailingAbsoluteQualifiedNameSegment(path);\r\n\t\tif (path != null && path.length() == 0)\r\n\t\t{\r\n\t\t\tpath = null;\r\n\t\t}\r\n\t}\r\n\treturn path;\r\n}\r\nelse\r\n{\r\n\treturn null;\r\n}'"
	 * @generated
	 */
	String getShortName();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Check if a shortname exists inside the proxy URI
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return getShortName() != null;\r\n'"
	 * @generated
	 */
	boolean isSetShortName();
} // MissingContainer

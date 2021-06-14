/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4449<br/>
 * Jan 30, 2014 10:13:46 AM
 *
 * </copyright>
 */
package eu.cessar.ct.validation;

import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.validation.model.Category;
import org.eclipse.emf.validation.service.ConstraintRegistry;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.emf.validation.service.IParameterizedConstraintDescriptor;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.validation.diagnostic.ExtendedDiagnostic;

import eu.cessar.ct.core.mms.MetaModelUtils;

/**
 * Container for Validation error messages
 *
 * @author uidg4449
 *
 *         %created_by: uidg4449 %
 *
 *         %date_created: Tue Mar 10 19:05:40 2015 %
 *
 *         %version: 5 %
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(
	name = "Error")
@XmlType(
	propOrder = {"message", "objectName", "uri", "resourceName", "eObjectTypeName", "severity", "id", "rule",
		"description"})
public class ValidationMessage
{
	private static String const_ID = "constrId"; //$NON-NLS-1$
	private String message;
	private String objectName;
	private String uri;
	private String resourceName;
	private String eObjectTypeName;
	private String severity;
	private int severityInt;
	private String id;
	private String rule;
	private String description;

	/**
	 * Default empty constructor
	 */
	public ValidationMessage()
	{

	}

	/**
	 * From IStatus extract the necessary information to a more easier way to handle
	 *
	 * @param iStatus
	 *        the Status of the validation result, this is actually the child status from the child status from the
	 *        result
	 *
	 * @param status
	 */
	public ValidationMessage(IStatus iStatus)
	{
		Diagnostic diag = BasicDiagnostic.toDiagnostic(iStatus);
		List<?> data = diag.getData();
		Object obj = null;
		int dataSize = data.size();

		if (dataSize > 0)
		{
			obj = data.get(0);
		}
		else
		{
			return;
		}

		if (obj instanceof EObject)
		{
			EObject eObject = (EObject) data.get(0);
			Resource eResource = eObject.eResource();
			message = iStatus.getMessage();

			EClass clz = eObject.eClass();

			eObjectTypeName = clz.getName();

			String featureName = "shortName"; //$NON-NLS-1$
			EStructuralFeature eStructuralFeature = clz.getEStructuralFeature(featureName);
			if (eStructuralFeature != null && eObject.eIsSet(eStructuralFeature))
			{
				Object eGet = eObject.eGet(eStructuralFeature);
				objectName = eGet.toString();
			}

			uri = MetaModelUtils.getAbsoluteQualifiedName(eObject);

			URI uriPath = eResource.getURI();
			IPath path = EcorePlatformUtil.createAbsoluteFileLocation(uriPath);
			resourceName = path.lastSegment();

			int severityID = iStatus.getSeverity();
			setSeverityInt(severityID);
			EValidationSeverity eValidationSeverity = EValidationSeverity.getSeverity(severityID);
			severity = eValidationSeverity.name();

			setAdditionalInformation(diag);

		}
	}

	private void setAdditionalInformation(Diagnostic diag)
	{
		ExtendedDiagnostic eDiag = (ExtendedDiagnostic) diag;

		String constraintId = eDiag.getConstraintId();

		ConstraintRegistry registry = ConstraintRegistry.getInstance();

		IConstraintDescriptor constraintDescription = registry.getDescriptor(constraintId);
		id = ((IParameterizedConstraintDescriptor) constraintDescription).getParameterValue(const_ID);
		description = constraintDescription.getDescription();

		rule = getRule(constraintDescription);

	}

	/**
	 * Computes the path to the category
	 *
	 * @param cat
	 * @return
	 */
	private String getRule(IConstraintDescriptor constraintDescription)
	{
		String result = new String();
		Set<Category> categories = constraintDescription.getCategories();
		for (Category c: categories)
		{

			result = c.getName();
			Category category = c;
			while (category.getParent() != null)
			{
				category = category.getParent();
				result = category.getName() + " -> " + result; //$NON-NLS-1$
			}

		}
		return result + " -> " + constraintDescription.getName(); //$NON-NLS-1$
	}

	/**
	 * Test if message is valid
	 *
	 * @return true if message is valid false otherwise
	 */
	public boolean isValid()
	{
		return message != null;
	}

	/**
	 * @return the message
	 */
	@XmlElement(
		name = "Message")
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message
	 *        the message to set
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}

	/**
	 * @return the objectName
	 */
	@XmlElement(
		name = "ObjectName")
	public String getObjectName()
	{
		return objectName;
	}

	/**
	 * @param objectName
	 *        the objectName to set
	 */
	public void setObjectName(String objectName)
	{
		this.objectName = objectName;
	}

	/**
	 * @return the uri
	 */
	@XmlElement(
		name = "UriPath")
	public String getUri()
	{
		return uri;
	}

	/**
	 * @param uri
	 *        the uri to set
	 */
	public void setUri(String uri)
	{
		this.uri = uri;
	}

	/**
	 * @return the resourceName
	 */
	@XmlElement(
		name = "ResourceName")
	public String getResourceName()
	{
		return resourceName;
	}

	/**
	 * @param resourceName
	 *        the resourceName to set
	 */
	public void setResourceName(String resourceName)
	{
		this.resourceName = resourceName;
	}

	/**
	 * @return the eObjectTypeName
	 */
	@XmlElement(
		name = "eObjectType")
	public String geteObjectTypeName()
	{
		return eObjectTypeName;
	}

	/**
	 * @param eObjectTypeName
	 *        the eObjectTypeName to set
	 */
	public void seteObjectTypeName(String eObjectTypeName)
	{
		this.eObjectTypeName = eObjectTypeName;
	}

	/**
	 * @return the severity
	 */
	@XmlElement(
		name = "Severity")
	public String getSeverity()
	{
		return severity;
	}

	/**
	 * @param sev
	 *        the severity to set
	 */
	public void setSeverity(int sev)
	{
		severity = EValidationSeverity.getSeverity(sev).name();
	}

	/**
	 * @return the severityInt
	 */
	public int getSeverityInt()
	{
		return severityInt;
	}

	/**
	 * @param severityInt
	 *        the severityInt to set
	 */
	public void setSeverityInt(int severityInt)
	{
		this.severityInt = severityInt;
	}

	/**
	 * @return the id
	 */
	@XmlElement(
		name = "Id")
	public String getId()
	{
		return id;
	}

	/**
	 * @param idd
	 *        the id to set
	 */
	public void setId(String idd)
	{
		id = idd;
	}

	/**
	 * @return the rule
	 */
	@XmlElement(
		name = "Rule")
	public String getRule()
	{
		return rule;
	}

	/**
	 * @param rul
	 *        the rule to set
	 */
	public void setSeverity(String rul)
	{
		rule = rul;
	}

	/**
	 * @return the description
	 */
	@XmlElement(
		name = "Description")
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param desc
	 *        the description to set
	 */
	public void setDescription(String desc)
	{
		description = desc;
	}
}

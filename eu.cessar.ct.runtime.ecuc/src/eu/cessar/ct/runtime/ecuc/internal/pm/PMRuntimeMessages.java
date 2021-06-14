/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 19, 2010 5:59:21 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm;

import java.text.MessageFormat;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.osgi.util.NLS;

/**
 * This class contains messages that will be used by the generated PM model
 */
public class PMRuntimeMessages extends NLS
{

	public static String fmtClassNotAValidClassifier;

	public static String fmtClassNotAValidDataType;

	public static String fmtEnumNotAValidLiteral;

	private final static String BUNDLE_NAME = PMRuntimeMessages.class.getCanonicalName();

	/**
	 * @param classifier
	 * @return
	 */
	public static String getNotAValidClassifierMessage(EClass classifier)
	{
		if (classifier == null)
		{
			return MessageFormat.format(fmtClassNotAValidClassifier, "null"); //$NON-NLS-1$
		}
		else
		{
			return MessageFormat.format(fmtClassNotAValidClassifier, classifier.getName());
		}
	}

	/**
	 * @param dataType
	 * @return
	 */
	public static String getNotAValidDataTypeMessage(EDataType dataType)
	{
		if (dataType == null)
		{
			return MessageFormat.format(fmtClassNotAValidDataType, "null"); //$NON-NLS-1$
		}
		else
		{
			return MessageFormat.format(fmtClassNotAValidDataType, dataType.getName());
		}
	}

	/**
	 * @param dataType
	 * @return
	 */
	public static String getNotAValidLiteralMessage(String literal, EDataType dataType)
	{
		String dataTypeName = dataType == null ? "null" : dataType.getName(); //$NON-NLS-1$
		if (literal == null)
		{
			return MessageFormat.format(fmtEnumNotAValidLiteral, "null", dataTypeName); //$NON-NLS-1$
		}
		else
		{
			return MessageFormat.format(fmtEnumNotAValidLiteral, literal, dataTypeName);
		}
	}

	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, PMRuntimeMessages.class);
	}
}
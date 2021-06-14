/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4449<br/>
 * Jan 30, 2014 11:56:11 AM
 *
 * </copyright>
 */
package eu.cessar.ct.validation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.core.runtime.IStatus;

import eu.cessar.ct.validation.internal.CessarPluginActivator;
import eu.cessar.ct.validation.internal.Messages;

/**
 * Class uses as Store for JAXB containing ValidationMessages
 *
 * @author uidg4449
 *
 *         %created_by: uidg4449 %
 *
 *         %date_created: Mon Mar 9 15:03:11 2015 %
 *
 *         %version: 5 %
 */

@XmlRootElement(
	name = "ValidationReport")
public class ValidationMessageStore
{
	@XmlElementWrapper(
		name = "ValidationMessages")
	@XmlElement(
		name = "ValidationMessage")
	private List<ValidationMessage> validationMessages;

	// /**
	// * @return the validationMessages
	// */
	// public List<ValidationMessage> getValidationMessages()
	// {
	// return validationMessages;
	// }

	/**
	 * @param validationMessages
	 *        the validationMessages to set
	 */
	public void setValidationMessages(List<ValidationMessage> validationMessages)
	{
		this.validationMessages = validationMessages;
	}

	/**
	 * Converts the IStatus result of validation to XML. This method extracts the validation messages from the IStatus
	 * and then converts them to a XML file that is the output of the method
	 *
	 * The IStaus is the result of the validation method.
	 *
	 * @param status
	 * @return String containing the XML output
	 */
	public static String iStatusToXml(IStatus status)
	{
		List<ValidationMessage> validationMessages = extractMesseages(status);
		String output = messagesToXml(validationMessages);

		return output;

	}

	/**
	 * @param validationMessages
	 *        list of validation messages
	 * @return a string formated as xml
	 *
	 */
	public static String messagesToXml(List<ValidationMessage> validationMessages)
	{
		ValidationMessageStore validationMessageStore = new ValidationMessageStore();
		validationMessageStore.setValidationMessages(validationMessages);

		String output = null;
		StringWriter sWriter = new StringWriter();

		try
		{
			JAXBContext context;
			context = JAXBContext.newInstance(ValidationMessageStore.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			m.marshal(validationMessageStore, sWriter);
			output = sWriter.toString();
		}
		catch (JAXBException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return output;
	}

	/**
	 * @param validationMessages
	 *        list of validation messages
	 * @return a string formated as CSV
	 */
	public static String messagesToCSV(List<ValidationMessage> validationMessages)
	{
		String output = null;
		StringBuilder sBuilder = new StringBuilder();
		String csvSeparator = Messages.msgCsv_Separator;
		String newLine = System.getProperty("line.separator"); //$NON-NLS-1$
		for (ValidationMessage validationMessage: validationMessages)
		{
			sBuilder.append(validationMessage.geteObjectTypeName() + csvSeparator);
			sBuilder.append(validationMessage.getMessage() + csvSeparator);
			sBuilder.append(validationMessage.getObjectName() + csvSeparator);
			sBuilder.append(validationMessage.getResourceName() + csvSeparator);
			sBuilder.append(validationMessage.getSeverity() + csvSeparator);
			sBuilder.append(validationMessage.getUri() + csvSeparator);
			sBuilder.append(validationMessage.getClass() + csvSeparator + newLine);
		}
		output = sBuilder.toString();
		return output;
	}

	/**
	 * @param validationMessages
	 *        list of validation messages
	 * @return a string formated as txt
	 */
	public static String messagesToTxt(List<ValidationMessage> validationMessages)
	{
		String output = null;
		StringBuilder sBuilder = new StringBuilder();
		String txtSeparator = Messages.msgTxt_Separator;
		String newLine = System.getProperty(Messages.msgTxt_lineSeparator);
		sBuilder.append(Messages.msgTxt_severity + txtSeparator);
		sBuilder.append(Messages.msgTxt_message + txtSeparator);
		sBuilder.append(Messages.msgTxt_objectUri + txtSeparator);
		sBuilder.append(Messages.msgTxt_objectType + txtSeparator);
		sBuilder.append(Messages.msgTxt_resourcePath + txtSeparator + newLine);
		int numberOfErrors = 0;

		for (ValidationMessage validationMessage: validationMessages)
		{
			sBuilder.append(validationMessage.getSeverity() + txtSeparator);
			sBuilder.append(validationMessage.getMessage() + txtSeparator);
			sBuilder.append(validationMessage.getUri() + txtSeparator);
			sBuilder.append(validationMessage.geteObjectTypeName() + txtSeparator);
			sBuilder.append(validationMessage.getResourceName() + txtSeparator);
			sBuilder.append(newLine);

			if (Messages.msgTxt_error.equalsIgnoreCase(validationMessage.getSeverity()))
			{
				numberOfErrors++;
			}
		}
		sBuilder.append(newLine + Messages.msgTxt_errorsNumber + numberOfErrors);
		sBuilder.append(newLine + Messages.msgTxt_highestSeverity);

		output = sBuilder.toString();
		return output;
	}

	/**
	 * This method receiver a list of {@link ValidationMessage} that is previously extracted from {@link IStatus}.
	 *
	 * This messages are converted to a XML that is passed to a string on witch a StyleSheet is applied.
	 *
	 *
	 * @param validationMessages
	 * @param styleSheet
	 * @return a string formated with the specified StyleSheet
	 */
	public static String messagesWithStyleSheet(List<ValidationMessage> validationMessages, String styleSheet)
	{
		String output = null;

		String xml = messagesToXml(validationMessages);
		StringReader xmlReader = new StringReader(xml);
		Source xmlSource = new StreamSource(xmlReader);
		StringReader styleReader = new StringReader(styleSheet);
		Source styleSource = new StreamSource(styleReader);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try
		{
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(styleSource);

			transformer.transform(xmlSource, new StreamResult(outputStream));

		}
		catch (TransformerException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		try
		{
			outputStream.close();
			output = outputStream.toString();
		}
		catch (IOException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		return output;
	}

	/**
	 * Extracts the validation messages from a IStatus
	 *
	 * @param status
	 * @return return a list of ValidationMessages extracted from IStatus
	 */
	public static List<ValidationMessage> extractMesseages(IStatus status)
	{
		List<ValidationMessage> validationMessages = new ArrayList<>();
		IStatus[] sChildren = status.getChildren();
		for (IStatus iStat: sChildren)
		{
			IStatus[] ss = iStat.getChildren();
			for (IStatus iStatus: ss)
			{
				if (iStatus.getSeverity() != IStatus.OK)
				{
					ValidationMessage validationMessage = new ValidationMessage(iStatus);
					if (validationMessage.isValid())
					{
						validationMessages.add(validationMessage);
					}
				}
			}

		}
		return validationMessages;
	}

}

/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by Thanatos 15.03.2010 22:57:28 </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.core.platform.ui.PlatformUIConstants;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor;
import eu.cessar.ct.core.platform.ui.widgets.SingleGregorianCalendarEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;

/**
 * @author uidl6870
 * 
 */
public class SingleDateTimeEditorPart extends
	AbstractSingleDatatypeEditorPart<XMLGregorianCalendar>
{

	/**
	 * @param editor
	 */
	public SingleDateTimeEditorPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#createDatatypeEditor()
	 */
	@Override
	protected IDatatypeEditor<XMLGregorianCalendar> createDatatypeEditor()
	{
		return new SingleGregorianCalendarEditor(true);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getImage()
	 */
	public Image getImage()
	{
		return PlatformUIUtils.getImage(PlatformUIConstants.IMAGE_ID_DATE_TIME);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getText()
	 */
	@SuppressWarnings("nls")
	public String getText()
	{
		String text = "";
		XMLGregorianCalendar inputData = getInputData();

		if (inputData != null)
		{
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append(String.valueOf(inputData.getMonth() + 1) + "/");
			strBuilder.append(String.valueOf(inputData.getDay()) + "/");
			strBuilder.append(String.valueOf(inputData.getYear()));
			strBuilder.append("  ");
			strBuilder.append((inputData.getHour() >= 10 ? String.valueOf(inputData.getHour())
				: "0" + String.valueOf(inputData.getHour()))
				+ ":");
			strBuilder.append((inputData.getMinute() >= 10 ? String.valueOf(inputData.getMinute())
				: "0" + String.valueOf(inputData.getMinute()))
				+ ":");
			strBuilder.append((inputData.getSecond() >= 10 ? String.valueOf(inputData.getSecond())
				: "0" + String.valueOf(inputData.getSecond())));
			text = strBuilder.toString();
		}

		return text;
	}
}

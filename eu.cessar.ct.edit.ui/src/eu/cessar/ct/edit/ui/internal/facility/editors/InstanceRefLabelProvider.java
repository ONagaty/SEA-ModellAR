/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 12.07.2013 15:42:03
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.editors;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.LabelProvider;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.edit.ui.instanceref.IInstReferenceLabelProvider;
import eu.cessar.ct.edit.ui.instanceref.SystemIRefFeatureWrapper;
import eu.cessar.ct.sdk.utils.ModelUtils;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * TODO: Please comment this class
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Jul 15 19:31:49 2013 %
 * 
 *         %version: 1 %
 */
public class InstanceRefLabelProvider extends LabelProvider implements
	IInstReferenceLabelProvider<SystemIRefFeatureWrapper>
{
	private final SystemIRefEditor editor;

	private static final String DELIM = ", "; //$NON-NLS-1$

	/**
	 * @param editor
	 */
	public InstanceRefLabelProvider(SystemIRefEditor editor)
	{
		this.editor = editor;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IInstReferenceLabelProvider#getContextCaptionTooltip()
	 */
	public String getContextCaptionTooltip(boolean pureMM)
	{
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(editor.getInput().eClass());
		int[][] multiplicities = mmService.getContextFeatureMultiplicities(editor.getInputClass(), pureMM);
		StringBuffer tooltip = new StringBuffer();
		int i = 0;
		for (EReference ref: editor.getContextFeatures())
		{
			tooltip.append("["); //$NON-NLS-1$
			tooltip.append(multiplicities[i][0]);
			tooltip.append("..."); //$NON-NLS-1$

			tooltip.append(multiplicities[i][1] == -1 ? "*" : ref.getUpperBound()); //$NON-NLS-1$
			tooltip.append("] "); //$NON-NLS-1$
			tooltip.append(ref.getEType().getName());
			tooltip.append(" (feature: "); //$NON-NLS-1$
			tooltip.append(ref.getName());
			tooltip.append(")"); //$NON-NLS-1$
			tooltip.append("\n"); //$NON-NLS-1$

			i++;
		}
		return tooltip.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IInstReferenceLabelProvider#getTargetCaptionTooltip()
	 */
	public String getTargetCaptionTooltip()
	{
		StringBuffer tooltip = new StringBuffer();

		EReference targetFeature = editor.getTargetFeature();
		tooltip.append(targetFeature.getEType().getName());
		tooltip.append(" (feature: "); //$NON-NLS-1$
		tooltip.append(targetFeature.getName());
		tooltip.append(")"); //$NON-NLS-1$

		return tooltip.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IReferenceLabelProvider#getTooltip(java.util.List)
	 */
	public String getTooltip(List<SystemIRefFeatureWrapper> elem)
	{
		String tooltip = ""; //$NON-NLS-1$
		String delim = ",\n"; //$NON-NLS-1$
		if (elem == null)
		{
			return tooltip;
		}
		for (SystemIRefFeatureWrapper refWrapper: elem)
		{
			if (refWrapper != null)
			{
				Object value = refWrapper.getValue();
				if (value instanceof List<?>)
				{
					for (GIdentifiable el: (List<GIdentifiable>) value)
					{
						tooltip += ModelUtils.getAbsoluteQualifiedName(el) + OF_TYPE_CONSTANT + el.eClass().getName()
							+ delim;
					}
				}
				else if (value != null)
				{
					tooltip += ModelUtils.getAbsoluteQualifiedName((EObject) value) + OF_TYPE_CONSTANT
						+ ((EObject) value).eClass().getName() + delim;
				}
			}
		}

		if (tooltip.indexOf(delim) > 0)
		{
			tooltip = tooltip.substring(0, tooltip.lastIndexOf(delim));
		}

		return tooltip;
	}

	@Override
	public String getText(Object element)
	{
		String resultText = ""; //$NON-NLS-1$

		if (element instanceof SystemIRefFeatureWrapper)
		{
			Object value = ((SystemIRefFeatureWrapper) element).getValue();
			if (value instanceof GIdentifiable)
			{
				resultText = ModelUtils.getAbsoluteQualifiedName((EObject) value);
			}
			else
			{
				String text = getValueAsString(value);
				resultText = text;
			}
		}
		else if (element instanceof List<?>)
		{
			StringBuffer sb = new StringBuffer();

			@SuppressWarnings("unchecked")
			List<SystemIRefFeatureWrapper> list = (List<SystemIRefFeatureWrapper>) element;
			for (SystemIRefFeatureWrapper featureWrapper: list)
			{
				Object value = featureWrapper.getValue();
				String valueAsString = getValueAsString(value);
				sb.append(valueAsString);
				sb.append(DELIM);
			}

			int index = sb.lastIndexOf(DELIM);
			if (index != -1)
			{
				sb.delete(index, index + 1);
			}

			resultText = sb.toString();
		}
		return resultText;
	}

	private static String getValueAsString(Object value)
	{
		StringBuffer sb = new StringBuffer();

		if (value instanceof List<?>)
		{
			for (GIdentifiable el: (List<GIdentifiable>) value)
			{
				sb.append(el.gGetShortName() + DELIM);
			}
		}
		else if (value != null)
		{
			sb.append(((GIdentifiable) value).gGetShortName() + DELIM);
		}

		int index = sb.lastIndexOf(DELIM);
		if (index != -1)
		{
			sb.delete(index, index + 1);
		}

		return sb.toString();
	}

}

/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Mar 16, 2010 9:04:43 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.ui.PlatformUIConstants;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor;
import eu.cessar.ct.core.platform.ui.widgets.MultiDatatypeValueHandler;
import eu.cessar.ct.core.platform.ui.widgets.MultiIntegerEditor;
import eu.cessar.ct.core.platform.util.CollectionUtils;
import eu.cessar.ct.core.platform.util.ERadix;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.parts.IIntegerEditorPart;

/**
 * @author uidl6870
 * 
 */
public class MultiIntegerEditorPart extends AbstractMultiNumericDatatypeEditorPart<Integer>
	implements IIntegerEditorPart
{
	private MultiIntegerEditor editor;
	private ERadix radix = null;
	/**
	 * @param editor
	 */
	public MultiIntegerEditorPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#createDatatypeEditor()
	 */
	@Override
	protected IDatatypeEditor<List<Integer>> createDatatypeEditor()
	{
		ILabelProvider handlerLabelProvider = new LabelProvider()
		{
			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element)
			{
				Integer value = (Integer) element;
				return Integer.toString(value.intValue(), getRadix().getRadixNumber());
			}
		};
		MultiDatatypeValueHandler<Integer> handler = new MultiDatatypeValueHandler<Integer>(
			handlerLabelProvider, getInputFeature().getName(), getInputFeature().getName());
		// editor = new MultiIntegerEditor(handler, getRadix());
		editor = new MultiIntegerEditor(handler);
		editor.setMaxValues(getInputFeature().getUpperBound());
		return editor;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.IIntegerEditorPart#getRadix()
	 */
	@Override
	public ERadix getRadix()
	{
		if (radix == null)
		{
			EObject input = getEditor().getInput();

			if (input == null)
			{
				radix = ERadix.DECIMAL;
			}
			else
			{
				radix = retrieveRadixFromModel(input, (EAttribute) this.getInputFeature());
				if (radix == null)
				{
					IProject project = MetaModelUtils.getProject(input);
					radix = PlatformUtils.getProjectRadixSettings(project);
				}
			}
			return radix;
		}
		return radix;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.IIntegerEditorPart#setRadix(eu.cessar.ct.core.platform.util.ERadix)
	 */
	public void setRadix(ERadix radix)
	{
		this.radix = radix;
		if (editor != null)
		{
			editor.setRadix(radix);
		}
		updateRadixLabel();
	}

	@Override
	protected void updateRadixLabel()
	{
		if (radixLabel != null && !radixLabel.isDisposed())
		{
			radixLabel.setText(getRadix().getLiteral().substring(0, 1));
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#refresh()
	 */
	@Override
	public void refresh()
	{
		if (editor != null)
		{
			editor.setRadix(getRadix());
			updateRadixLabel();
		}
		super.refresh();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getImage()
	 */
	public Image getImage()
	{
		return PlatformUIUtils.getImage(PlatformUIConstants.IMAGE_ID_INTEGER);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getText()
	 */
	public String getText()
	{
		String text;
		int radixNumber = getRadix().getRadixNumber();
		List<Integer> inputData = getInputData();
		List<String> inputDataInRadix = new ArrayList<String>();

		if (inputData.size() > 0)
		{
			for (Integer data: inputData)
			{
				inputDataInRadix.add(Integer.toString(data, radixNumber));

			}

			text = CollectionUtils.toString(inputDataInRadix, ", "); //$NON-NLS-1$
			if (text != null)
			{
				return text;
			}
		}
		return ""; //$NON-NLS-1$
	}

}

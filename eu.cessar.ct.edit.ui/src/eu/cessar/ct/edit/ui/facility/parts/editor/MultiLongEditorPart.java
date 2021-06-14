/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt6343 Dec 8, 2010 11:23:49 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

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
import eu.cessar.ct.core.platform.ui.widgets.MultiLongEditor;
import eu.cessar.ct.core.platform.util.CollectionUtils;
import eu.cessar.ct.core.platform.util.ERadix;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.parts.IIntegerEditorPart;

/**
 * @author uidt6343
 * 
 */
public class MultiLongEditorPart extends AbstractMultiNumericDatatypeEditorPart<Long> implements
	IIntegerEditorPart
{

	private MultiLongEditor editor;
	private ERadix radix = null;
	/**
	 * @param editor
	 */
	public MultiLongEditorPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#createDatatypeEditor()
	 */
	/**
	 * @return MultiLongEditor with handler
	 */
	@Override
	/*protected IDatatypeEditor<List<Long>> createDatatypeEditor()
	{
		MultiDatatypeValueHandler<Long> handler = new MultiDatatypeValueHandler<Long>(
			new LabelProvider(), getInputFeature().getName(), getInputFeature().getName());
		editor = new MultiLongEditor(handler);
		editor.setMaxValues(getInputFeature().getUpperBound());
		return editor;
	}*/
	protected IDatatypeEditor<List<Long>> createDatatypeEditor()
	{
		ILabelProvider handlerLabelProvider = new LabelProvider()
		{
			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element)
			{
				Long value = (Long) element;
				return Long.toString(value.longValue(), getRadix().getRadixNumber());
			}
		};
		MultiDatatypeValueHandler<Long> handler = new MultiDatatypeValueHandler<Long>(
			handlerLabelProvider, getInputFeature().getName(), getInputFeature().getName());
		// editor = new MultiIntegerEditor(handler, getRadix());
		editor = new MultiLongEditor(handler);
		editor.setMaxValues(getInputFeature().getUpperBound());
		return editor;
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
	/**
	 * loads image for Long from Activator ImageRegistry
	 */
	public Image getImage()
	{
		return PlatformUIUtils.getImage(PlatformUIConstants.IMAGE_ID_LONG);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getText()
	 */
	/**
	 * @return input data, as String, if available; else ""
	 */
	public String getText()
	{
		String text = ""; //$NON-NLS-1$
		List<Long> inputData = getInputData();
		text = CollectionUtils.toString(inputData, ", "); //$NON-NLS-1$
		if (text != null)
		{
			return text;
		}

		return ""; //$NON-NLS-1$
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

	@Override
	protected void updateRadixLabel()
	{
		if (radixLabel != null && !radixLabel.isDisposed())
		{
			radixLabel.setText(getRadix().getLiteral().substring(0, 1));
		}
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

}

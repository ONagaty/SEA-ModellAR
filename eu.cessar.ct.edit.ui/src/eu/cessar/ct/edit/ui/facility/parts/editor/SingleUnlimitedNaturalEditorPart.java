/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Mar 17, 2010 10:47:12 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import java.math.BigInteger;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.ui.PlatformUIConstants;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor;
import eu.cessar.ct.core.platform.ui.widgets.SingleBigIntegerEditor;
import eu.cessar.ct.core.platform.util.ERadix;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.parts.IIntegerEditorPart;

/**
 * @author uidl6870
 * 
 */
public class SingleUnlimitedNaturalEditorPart extends AbstractSingleDatatypeEditorPart<BigInteger>
	implements IIntegerEditorPart
{
	private SingleBigIntegerEditor editor;
	private ERadix radix = null;
	private Label radixLabel;

	/**
	 * @param editor
	 */
	public SingleUnlimitedNaturalEditorPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createContents(Composite parent)
	{
		Composite composite = getFormToolkit().createComposite(parent);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		radixLabel = getFormToolkit().createLabel(composite, ""); //$NON-NLS-1$
		GridData gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		radixLabel.setLayoutData(gridData);

		Control control = super.createContents(composite);
		control.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		return composite;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#createDatatypeEditor()
	 */
	@Override
	protected IDatatypeEditor<BigInteger> createDatatypeEditor()
	{
		editor = new SingleBigIntegerEditor(true, getRadix());
		return editor;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.IIntegerEditorPart#getRadix()
	 */
	public ERadix getRadix()
	{
		if (radix == null)
		{
			EObject input = getEditor().getInput();
			if (input == null)
			{
				return ERadix.DECIMAL;
			}
			else
			{
				IProject project = MetaModelUtils.getProject(input);
				radix = PlatformUtils.getProjectRadixSettings(project);
			}
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

	private void updateRadixLabel()
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
		if (editor != null && !editor.isSetRadixOnUI())
		{
			// IProject project =
			// MetaModelUtils.getProject(getEditor().getInput());
			// radix = PlatformUtils.getProjectRadixSettings(project);
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
		BigInteger inputData = getInputData();
		if (inputData == null)
		{
			return ""; //$NON-NLS-1$
		}

		return inputData.toString(getRadix().getRadixNumber());
	}
}
